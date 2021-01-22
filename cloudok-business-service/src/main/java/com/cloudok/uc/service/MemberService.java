package com.cloudok.uc.service;

import com.cloudok.core.service.IService;
import com.cloudok.uc.po.MemberPO;
import com.cloudok.uc.vo.BindRequest;
import com.cloudok.uc.vo.ChangePasswordRequest;
import com.cloudok.uc.vo.ForgotVO;
import com.cloudok.uc.vo.LoginVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.SingupVO;
import com.cloudok.uc.vo.TokenVO;
import com.cloudok.uc.vo.UserCheckRequest;
import com.cloudok.uc.vo.VerifyCodeRequest;

public interface MemberService extends IService<MemberVO,MemberPO>{

	TokenVO login(LoginVO vo);

	Boolean logout();

	TokenVO refreshToken(String refreshToken);

	MemberVO getCurrentUserInfo();

	Boolean resetPwd(ForgotVO vo);

	Boolean signup(SingupVO vo);

	Boolean sendVerifycode(VerifyCodeRequest vo);

	Boolean checkEmail(UserCheckRequest request);

	Boolean checkUserName(UserCheckRequest request);

	Boolean changePassword(ChangePasswordRequest vo);

	Boolean bind(BindRequest vo);

}
