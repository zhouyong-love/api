package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;

public class AttachMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping STORAGEMODEL=new Mapping("storageModel", "t.storage_model");
	
	public static final Mapping ADDRESS=new Mapping("address", "t.address");
	
	public static final Mapping BUSINESSID=new Mapping("businessId", "t.business_id");
	
	public static final Mapping BUSINESSKEY=new Mapping("businessKey", "t.business_key");
	
	public static final Mapping USED=new Mapping("used", "t.used");
	
	public static final Mapping FILENAME=new Mapping("fileName", "t.file_name");
	
	public static final Mapping SUFFIX=new Mapping("suffix", "t.suffix");
	
	public static final Mapping FILESIZE=new Mapping("fileSize", "t.file_size");
	
	public static final Mapping FILETYPE=new Mapping("fileType", "t.file_type");
	
	public static final Mapping BUSINESSREMARK=new Mapping("businessRemark", "t.business_remark");
	
}
