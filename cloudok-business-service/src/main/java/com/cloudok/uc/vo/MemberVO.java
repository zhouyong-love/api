package com.cloudok.uc.vo;

import java.io.Serializable;

import com.cloudok.core.vo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MemberVO extends VO {

	private static final long serialVersionUID = 212382535356088200L;
	
	
	private String userName;
	
	
	private String email;
	
	
	private String password;
	
	
	private String nickName;
	
	
	private String realName;
	
	
	private java.sql.Date birthDate;
	
	
	private String sex;
	
	
	private String phone;
	
	
	private Long avatar;
	
	private String description;
	
	private UserState state;
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class UserState implements Serializable{

		private static final long serialVersionUID = 863132281510960136L;
		
		/**
		 * 是否完善了用户信息
		 */
		private boolean fillUserInfo;
		
		/**
		 * 是否完善了教育信息
		 */
		private boolean fillEduInfo;
		
		/**
		 * 是否验证了邮箱
		 */
		private boolean checkEmail;
		
		public static UserState build(Long state) {
			return UserState.builder()
					.fillUserInfo((state&1)==1)
					.fillEduInfo(((state>>1)&1)==1)
					.checkEmail(((state>>2)&1)==1)
					.build();
		}
	}
}
