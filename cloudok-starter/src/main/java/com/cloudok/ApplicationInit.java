package com.cloudok;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cloudok.primarkey.SnowflakePrimaryKeyGenerator;

@Component
@Order(1)
public class ApplicationInit implements ApplicationRunner {

    private final Logger log= LoggerFactory.getLogger(this.getClass());

    public void run(ApplicationArguments args) throws Exception {
        int nodeId=0;
        if(args.getNonOptionArgs()!=null&&args.getNonOptionArgs().size()>0) {
            try {
                nodeId=Integer.parseInt(args.getNonOptionArgs().get(0));
                log.info("nodeId 设置成功 值为 "+nodeId);
            }catch (Exception e) {
                log.error("启动参数错误，参数 1 期望 为数字 实际为 "+args.getNonOptionArgs().get(0) +" 设置 nodeId 为默认参数 0");
                nodeId=0;
            }
        }else {
            nodeId=0;
            log.error("缺少启动参数，设置 nodeId 为默认参数 0");
        }
        SnowflakePrimaryKeyGenerator.SEQUENCE.setWorkId(nodeId); //分布式主键算法初始化
    }
}
