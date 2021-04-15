package com.cloudok.minapp.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cloudok.base.attach.io.AttachRWHandle;
import com.cloudok.base.attach.service.AttachService;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.enums.MemberProfileType;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.minapp.service.IMinAppService;
import com.cloudok.minapp.vo.Code2SessionResult;
import com.cloudok.minapp.vo.InfoRequest;
import com.cloudok.minapp.vo.InfoRequestV2;
import com.cloudok.minapp.vo.LoginWithPhoneResult;
import com.cloudok.minapp.vo.PhoneRequest;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.event.MemberProfileEvent;
import com.cloudok.uc.mapping.MemberMapping;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.TokenVO;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import me.chanjar.weixin.common.error.WxErrorException;

@Service
public class MinAppServiceImpl implements IMinAppService {

	@Autowired
	private WxMaService wxService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private AttachService attacheService;

	@Value("${server.tomcat.basedir:/tmp/api}")
	private String temp;

	@Override
	public Code2SessionResult code2session(String code) {
		Code2SessionResult result = new Code2SessionResult();
		try {
			WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
			result.setCode2SessionResult(session);
			MemberVO member = memberService.get(QueryBuilder.create(MemberMapping.class).and(MemberMapping.openId, session.getOpenid()).end());
			if (member != null) {
				TokenVO token = memberService.authByMember(member);
				result.setToken(token);
			}
		} catch (WxErrorException e) {
			throw new SystemException(CloudOKExceptionMessage.PARSE_WEIXIN_CODE_ERROR);
		}
		return result;
	}

	@Override
	public MemberVO submitMyInfo(InfoRequest infoRequest) {
		// 用户信息校验
		if (!wxService.getUserService().checkUserInfo(infoRequest.getSessionKey(), infoRequest.getRawData(), infoRequest.getSignature())) {
			throw new SystemException(CloudOKExceptionMessage.PARSE_WEIXIN_USER_INFO_ERROR);
		}
		// 解密用户信息
		WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(infoRequest.getSessionKey(), infoRequest.getEncryptedData(), infoRequest.getIv());
		MemberVO db = this.memberService.get(SecurityContextHelper.getCurrentUserId());
		MemberVO member = new MemberVO();
		member.setId(db.getId());
		switch (userInfo.getGender()) {
			case "0":
				break;
			case "1":
				member.setSex("0");
				break;
			case "2":
				member.setSex("1");
				break;
			default:
				break;
		}
		member.setNickName(userInfo.getNickName());
		AttachVO attach = this.downloadImage(userInfo.getAvatarUrl(), member.getId());
		if (attach != null) {
			member.setAvatar(attach.getId());
		}
		this.memberService.merge(member);
		member = this.memberService.get(member.getId());
		SpringApplicationContext.publishEvent(MemberProfileEvent.update(db.getId(),MemberProfileType.base,member,db));
		return member;
	}
	@Override
	public MemberVO submitMyInfoV2(InfoRequestV2 infoRequest) {
		MemberVO db = this.memberService.get(SecurityContextHelper.getCurrentUserId());
		MemberVO member = new MemberVO();
		member.setId(db.getId());
		switch (infoRequest.getGender()) {
			case "0":
				break;
			case "1":
				member.setSex("0");
				break;
			case "2":
				member.setSex("1");
				break;
			default:
				break;
		}
		member.setNickName(infoRequest.getNickName());
		AttachVO attach = this.downloadImage(infoRequest.getAvatarUrl(), member.getId());
		if (attach != null) {
			member.setAvatar(attach.getId());
		}
		this.memberService.merge(member);
		member = this.memberService.get(member.getId());
		SpringApplicationContext.publishEvent(MemberProfileEvent.update(db.getId(),MemberProfileType.base,member,db));
		return member;
	}

	private AttachVO downloadImage(String avatarUrl, Long userId) {
		URL url = null;
		URLConnection con = null;
		InputStream is = null;
		FileOutputStream os = null;
		File file = null;
		AttachVO avo = null;
		try {
			url = new URL(avatarUrl);
			con = url.openConnection();
			is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			file = new File(temp + File.separator + UUID.randomUUID().toString() + ".jpg");
			os = new FileOutputStream(file, true);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			os.flush();
			avo = AttachRWHandle.upload(file, "avatar", "image", file.getName(), Collections.emptyMap());
			AttachRWHandle.used(avo.getId(), userId, "");

			AttachVO m = new AttachVO();
			m.setId(avo.getId());
			m.setBusinessId(avo.getBusinessId());
			attacheService.merge(m);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 完毕，关闭所有链接
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (file != null) {
				file.delete();
			}
		}
		return avo;
	}

	@Override
	public LoginWithPhoneResult loginWithPhone(PhoneRequest phoneRequest) {
		// 解密
		WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(phoneRequest.getSessionKey(), phoneRequest.getEncryptedData(), phoneRequest.getIv());
		LoginWithPhoneResult result = new LoginWithPhoneResult();
		TokenVO token = this.memberService.loginOrCreateByPhone(phoneNoInfo.getPhoneNumber());
		if(!StringUtils.isEmpty(phoneRequest.getOpenId())) {
			memberService.bindOpenId(token.getUserInfo().getId(), phoneRequest.getOpenId());
		}
		result.setToken(token);
		result.setOpenId(phoneRequest.getOpenId());
		result.setPhone(phoneNoInfo.getPhoneNumber());
		return result;
	}

	@Override
	public Code2SessionResult bind(String code) {
		Code2SessionResult result = new Code2SessionResult();
		try {
			WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
			result.setCode2SessionResult(session);
			memberService.bindOpenId(SecurityContextHelper.getCurrentUserId(), session.getOpenid());
		} catch (WxErrorException e) {
			throw new SystemException(CloudOKExceptionMessage.PARSE_WEIXIN_CODE_ERROR);
		}
		return result;
	}

	@Override
	public Boolean unbind(Long currentUserId) {
		memberService.unbindOpenId(SecurityContextHelper.getCurrentUserId());
		return true;
	}

}
