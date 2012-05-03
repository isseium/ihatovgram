package com.ipuweb.ihatovgram.Viewer;

public class IhatovPhoto {
	private int id;
	private String original_url;
	private String thumbnail_url;
	
	public IhatovPhoto(int id, String original_url, String thumbnail_url){
		this.id = id;
		this.original_url = original_url;
		this.thumbnail_url = thumbnail_url;
	}
	
	public void setUrl(String thumbnail, String original){
		thumbnail_url = thumbnail;
		original_url = original;
	}
	
	public int getId(){
		return id;
	}
	
	public String getThumbnailUrl() {
		return thumbnail_url;
	}
	
	public String getOriginalUrl(){
		return original_url;
	}
}
