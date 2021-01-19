package com.cloudok.base.attach;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "attach")
public class AttachProperties {

	private Map<String, AttachBusiness> business;

	public Map<String, AttachBusiness> getBusiness() {
		return business;
	}

	public void setBusiness(Map<String, AttachBusiness> business) {
		this.business = business;
	}

	public static class AttachBusiness {
		private String name;

		private String storageType;

		private String authorityInterface;

		private Map<String, FileType> fileTypes;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getStorageType() {
			return storageType;
		}

		public void setStorageType(String storageType) {
			this.storageType = storageType;
		}

		public String getAuthorityInterface() {
			return authorityInterface;
		}

		public void setAuthorityInterface(String authorityInterface) {
			this.authorityInterface = authorityInterface;
		}

		public Map<String, FileType> getFileTypes() {
			return fileTypes;
		}

		public void setFileTypes(Map<String, FileType> fileTypes) {
			this.fileTypes = fileTypes;
		}

		public static class FileType {
			private String name;
			private Integer maxSize;
			private String[] suffix;
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public Integer getMaxSize() {
				return maxSize;
			}
			public void setMaxSize(Integer maxSize) {
				if(maxSize!=null) {
					this.maxSize = maxSize*1024*1024;
				}else {
					this.maxSize = 5*1024*1024;
				}
			}
			public String[] getSuffix() {
				return suffix;
			}
			public void setSuffix(String[] suffix) {
				this.suffix = suffix;
				if(suffix!=null&&suffix.length>0) {
					for(int i=0;i<this.suffix.length;i++) {
						this.suffix[i]=this.suffix[i]!=null?this.suffix[i].toUpperCase():"";
					}
				}
			}
		}
	}
}
