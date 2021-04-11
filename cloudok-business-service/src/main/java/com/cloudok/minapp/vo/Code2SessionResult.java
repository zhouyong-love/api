package com.cloudok.minapp.vo;

import com.cloudok.uc.vo.TokenVO;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("code2session接口返回")
public class Code2SessionResult {
	@ApiModelProperty("如果登录成功，会返回这个token信息，里面包含了accessToken和用户信息")
	private TokenVO token;
	@ApiModelProperty("code2session 接口才有返回")
	private WxMaJscode2SessionResult code2SessionResult;
}
