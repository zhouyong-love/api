package com.cloudok.uc.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.uc.dto.WholeMemberDTO;
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

	TokenVO signup(SingupVO vo);

	Boolean sendVerifycode(VerifyCodeRequest vo);

	Boolean checkEmail(UserCheckRequest request);
	
	Boolean checkPhone(UserCheckRequest request);

	Boolean checkUserName(UserCheckRequest request);

	Boolean changePassword(ChangePasswordRequest vo);

	Boolean bind(BindRequest vo);

	MemberVO fillAccountInfo(@Valid MemberVO vo);
	
	WholeMemberDTO  getWholeMemberInfo(Long memberId);
	 
	List<WholeMemberDTO> getWholeMemberInfo(List<Long> memberIdList);

}
