package com.cloudok.log.vo;

import java.util.List;

import com.cloudok.core.json.JSON;
import com.cloudok.core.vo.Response;
import com.cloudok.core.vo.VO;
import com.cloudok.log.enums.LogSwitch;
import com.cloudok.log.enums.SysLogLevel;
import com.cloudok.security.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysLogVO extends VO {

	private static final long serialVersionUID = 453001377940405200L;
	
	
	private String modelCode;
	
	
	private String modelName;
	
	
	private String interfaceCode;
	
	
	private String interfaceName;
	
	
	private Long userId;
	
	
	private String userName;
	
	
	private String requestUrl;
	
	
	private String requestMethod;
	
	
	private String input;
	
	
	private Integer inputLogSwitch;
	
	
	private String logLevel;
	
	
	private Integer outputLogSwitch;
	
	
	private  String output;
	
	
	private Boolean status;
	
	
	private String formatedMessage;
	
	
	private Integer httpCode;
	
	
	private Long businessId;
	
	
	private Long relationBusinessId;
	

	public static SysLogVO build(String moduleCode, String moduleName, String interfaceCode, String interfaceName,
			LogSwitch inputLogSwitch, LogSwitch outputLogSwitch, String requestURI, String method, int httpCode, SysLogLevel level,
			List<Object> inputParameterList, Response response,
			User user, Long businessId, Long relationBusinessId, String formatedMessage) {
		SysLogVO loggerVO = new SysLogVO();
		
		loggerVO.setModelCode(moduleCode);
		loggerVO.setModelName(moduleName);
		loggerVO.setInterfaceCode(interfaceCode);
		loggerVO.setInterfaceName(interfaceName);
		loggerVO.setStatus(response.isStatus());
		
		loggerVO.setLogLevel(level.name());
		
		if(user != null) {
			loggerVO.setUserId(user.getId());
			loggerVO.setUserName(user.getFullName());
			loggerVO.setCreateBy(user.getId());
			loggerVO.setUpdateBy(user.getId());
		}
		
		loggerVO.setFormatedMessage(formatedMessage);
		loggerVO.setBusinessId(businessId);
		loggerVO.setRelationBusinessId(relationBusinessId);
	
		loggerVO.setRequestUrl(requestURI);
		loggerVO.setRequestMethod(method);
		
		loggerVO.setInputLogSwitch(inputLogSwitch.getSwtich());
		loggerVO.setOutputLogSwitch(outputLogSwitch.getSwtich());
		
		loggerVO.setHttpCode(httpCode);
		
		loggerVO.setInput(JSON.toJSONString(inputParameterList));
		loggerVO.setOutput(JSON.toJSONString(response));

		return loggerVO;
	}
}
