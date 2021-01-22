package com.cloudok.base.message.enums;

import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.EnumValue;

@Enum(type = "messageType",label = "Message type")
public class MessageType   {
    @EnumValue(sn = 1)
    public static final EnumInfo II = new EnumInfo("站内信", "ii","站内信");
    @EnumValue(sn = 2)
    public static final EnumInfo SMS = new EnumInfo("短信", "sms","短信");
    @EnumValue(sn = 3)
    public static final EnumInfo VOICE = new EnumInfo("语音电话", "voice","语音电话");
    @EnumValue(sn = 4)
    public static final EnumInfo JPUSH = new EnumInfo("app推送", "jpush","app推送");
    @EnumValue(sn = 4)
    public static final EnumInfo EMAIL = new EnumInfo("Email", "email","Email");
    
    
    public static EnumInfo parse(String value) {
		switch (value) {
		case "ii": {
			return II;
		}
		case "sms": {
			return SMS;
		}
		case "voice": {
			return VOICE;
		}
		case "jpush": {
			return JPUSH;
		}
		case "email": {
			return EMAIL;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + value);
		}
	}
}
