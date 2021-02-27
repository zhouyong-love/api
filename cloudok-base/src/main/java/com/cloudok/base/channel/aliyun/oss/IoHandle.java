package com.cloudok.base.channel.aliyun.oss;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.cloudok.base.attach.io.AttachIoHandle;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.base.exception.BaseExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("ossIoHandle")
public class IoHandle implements AttachIoHandle {

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private OSS ossClient;


    @Override
    public String sign(AttachVO attachVO,Map<String,String> extend) {
//    	 Date expiration = new Date(new Date().getTime() + TimeUnit.SECONDS.toMillis(ossProperties.getSignTimeout()));
// 		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(ossProperties.getBucket(), attachVO.getAddress());
// 		request.setExpiration(expiration);
// 		if(extend!=null && extend.containsKey("x-oss-process")) { //大图走图片压缩,style/image_compression_jpg_h150
// 			request.addQueryParameter("x-oss-process", extend.get("x-oss-process"));
// 		} 
// 		return ossClient.generatePresignedUrl(request).toString();
    	
    	 Instant expiredAt = Instant.now().plusSeconds(ossProperties.getSignTimeout());
         if (!attachVO.getAddress().startsWith("/")) {
        	 attachVO.setAddress("/" + attachVO.getAddress());
         }
         Long expiredAtTimeStamp = Timestamp.from(expiredAt).getTime() / 1000;
         String data = String.format("%s-%s-0-0-%s", attachVO.getAddress(), expiredAtTimeStamp, ossProperties.getCdnKey());
         String md5hash = DigestUtils.md5DigestAsHex(data.getBytes());
         String url = String.format("https://%s%s?auth_key=%s-0-0-%s", ossProperties.getCdnDomain(), attachVO.getAddress(), expiredAtTimeStamp, md5hash);
         return url;
    }

    @Override
    public String url(String path) {
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(ossProperties.getSignTimeout()));
        return ossClient.generatePresignedUrl(ossProperties.getBucket(), path, expiration).toString();
    }

    @Override
    public String write(InputStream ins, String business, String fileType, String fileName) throws IOException {
        String file = genFilePath(business, fileType, fileName) + File.separator + UUID.randomUUID().toString() + "." + FileUtil.getSuffix(fileName);
        ossClient.putObject(ossProperties.getBucket(), file, ins);
        return file;
    }

    @Override
    public String write(byte[] bs, String business, String fileType, String fileName) throws IOException {
        String file = genFilePath(business, fileType, fileName) + File.separator + UUID.randomUUID().toString() + "." + FileUtil.getSuffix(fileName);
        try (InputStream ins = new ByteArrayInputStream(bs)) {
            ossClient.putObject(ossProperties.getBucket(), file, ins);
        } catch (Exception e) {
            throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_ERROR);
        }
        return file;
    }

    @Override
    public InputStream read(AttachVO vo) throws IOException {
        return ossClient.getObject(ossProperties.getBucket(), vo.getAddress()).getObjectContent();
    }

    private static final String STORAGETYPE = "oss";

    @Override
    public String getStorageType() {
        return STORAGETYPE;
    }


    private String genFilePath(String business, String fileType, String fileName) {
        Calendar cal = Calendar.getInstance();
        return ossProperties.getBaseDir() + File.separator + business + File.separator + fileType + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE);
    }

    public String multipartUpload(MultipartFile file, String business, String fileType) throws IOException {
        String filePath = genFilePath(business, fileType, file.getOriginalFilename()) + File.separator + UUID.randomUUID().toString() + "." + FileUtil.getSuffix(file.getOriginalFilename());
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(ossProperties.getBucket(), filePath);
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        String uploadId = upresult.getUploadId();
        List<PartETag> partETags = new ArrayList<PartETag>();
        // 计算文件有多少个分片。
        final long partSize = 5 * 1024 * 1024L;   // 5MB
        long fileLength = file.getSize();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream instream = file.getInputStream();
            // 跳过已经上传的分片。
            instream.skip(startPos);
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(ossProperties.getBucket());
            uploadPartRequest.setKey(filePath);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(instream);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
            uploadPartRequest.setPartNumber(i + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
            partETags.add(uploadPartResult.getPartETag());
        }
        // 创建CompleteMultipartUploadRequest对象。
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(ossProperties.getBucket(), filePath, uploadId, partETags);

        // 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
        // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);

        // 完成上传。
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        log.debug("upload success with status code={}!",completeMultipartUploadResult.getResponse().getStatusCode());
        // 关闭OSSClient。
        return filePath;
    }
}
