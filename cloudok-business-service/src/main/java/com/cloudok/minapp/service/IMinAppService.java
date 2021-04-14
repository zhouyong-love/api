package com.cloudok.minapp.service;

import com.cloudok.minapp.vo.Code2SessionResult;
import com.cloudok.minapp.vo.InfoRequest;
import com.cloudok.minapp.vo.InfoRequestV2;
import com.cloudok.minapp.vo.LoginWithPhoneResult;
import com.cloudok.minapp.vo.PhoneRequest;
import com.cloudok.uc.vo.MemberVO;

public interface IMinAppService {

	public Code2SessionResult code2session(String code);
	
	public MemberVO submitMyInfo(InfoRequest infoRequest);
	
	public LoginWithPhoneResult loginWithPhone(PhoneRequest phoneRequest);

	public Code2SessionResult bind(String code);

	public Boolean unbind(Long currentUserId);

	public MemberVO submitMyInfoV2(InfoRequestV2 infoRequest);
}
