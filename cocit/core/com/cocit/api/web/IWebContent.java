package com.cocit.api.web;

import com.cocit.api.entitydef.field.RichText;
import com.cocit.api.entitydef.field.Upload;

/**
 * 网站信息
 * 
 * @author yongshan.ji
 * 
 */
public interface IWebContent {
	static final String SYS_CODE = "WebContent";

	public static final int TYPE_EDITOR = 0;

	public static final int TYPE_LINK = 1;

	public static final int TYPE_REFER = 99;

	IWebContentCatalog getInfoCatalog();

	IWebContent getRefrence();

	Long getInfoID();

	String getInfoName();

	String getInfoAuthor();

	String getInfoOrigin();

	RichText getInfoContent();

	String getInfoLinkPath();

	// String getInfoLinkTarget();

	String getInfoLogo();

	String getInfoImage();

	String getInfoDate();

	Integer getTypeCode();

	String getName();

	Upload getImage();

	String getInfoDesc();

}
