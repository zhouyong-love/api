package com.cloudok.authority.service;

import com.cloudok.authority.po.UserPO;
import com.cloudok.authority.vo.UserVO;
import com.cloudok.core.service.IService;

public interface UserService extends IService<UserVO,UserPO>{

    boolean exists(String username);

}
