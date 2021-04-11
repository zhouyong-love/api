package com.cloudok.minapp.vo;

import com.cloudok.uc.vo.TokenVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginWithPhoneResult {
	@ApiModelProperty("openId")
	private String openId;
	@ApiModelProperty("调用phone返回数据，")
	private String phone;
	@ApiModelProperty("token信息，里面包含了accessToken和用户信息")
	private TokenVO token;
}
