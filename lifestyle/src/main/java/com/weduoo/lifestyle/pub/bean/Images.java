package com.weduoo.lifestyle.pub.bean;

public class Images {

	private String id;
	private String ImageName;
	private String ImagePath;
	private String ReName;
	
	public String getReName() {
		return ReName;
	}
	public void setReName(String reName) {
		ReName = reName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageName() {
		return ImageName;
	}
	public void setImageName(String imageName) {
		ImageName = imageName;
	}
	public String getImagePath() {
		return ImagePath;
	}
	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}
}
