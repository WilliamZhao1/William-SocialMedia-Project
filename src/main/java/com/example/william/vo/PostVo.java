package com.example.william.vo;

import java.util.Date;

public class PostVo {
	private Integer pid;
	
	private String title;
	
	private String content;
	
	private String pictureUrl;
	
	private Date postTime;
	
	private String strPostTime;
	
	private Integer authorId;
	
	private String authorName;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public String getStrPostTime() {
		return strPostTime;
	}

	public void setStrPostTime(String strPostTime) {
		this.strPostTime = strPostTime;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
}
