package com.cloudok.uc.service;

import java.util.List;

import javax.validation.Valid;

import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;
import com.cloudok.uc.dto.SimpleMemberDTO;
import com.cloudok.uc.dto.SimpleMemberInfo;
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

import lombok.Data;

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
	
	SimpleMemberDTO getSimpleMemberInfo();
	
	IdenticalCountVO identical(Long id);
	
	@Data
	public class IdenticalCountVO{
		private int friends;
		
		private int tags;
	}

	

	Page<WholeMemberDTO> suggest(Integer filterType,String threadId,  Integer pageNo, Integer pageSize);

	Page<SimpleMemberInfo> friend(Integer type, Integer pageNo, Integer pageSize);

	Page<SimpleMemberInfo>  getSecondDegreeRecognized(Long memberId, Integer pageNo, Integer pageSize);

	Page<WholeMemberDTO> getMemberCircles(Integer type, Long businessId, Integer pageNo, Integer pageSize);
	
	MemberVO getMemberDetails(Long memberId);
	
	WholeMemberDTO  getWholeMemberInfo(Long memberId);
	
	WholeMemberDTO  getWholeMemberInfo(Long memberId,boolean includeSecurityInfo);
	 
	List<WholeMemberDTO> getWholeMemberInfo(List<Long> memberIdList);
	
	List<SimpleMemberInfo> getSimpleMemberInfo(List<Long> memberIdList);

	List<WholeMemberDTO> getWholeMemberInfoByVOList(List<MemberVO> memberList);

}
