package com.kmjsoft.cocit.entity.web;

import java.util.Date;

import com.jiongsoft.cocit.entitydef.field.Upload;
import com.kmjsoft.cocit.entity.module.IEntityColumn;
import com.kmjsoft.cocit.entity.security.IFunMenu;

/**
 * 上传文件信息接口
 * 
 * @author yongshan.ji
 * 
 */
public interface IUploadInfo {

	/**
	 * 获取上传时的本地文件名
	 * 
	 * @return 返回原始文件名称
	 */
	String getLocalName();

	/**
	 * 获取文件相对路径
	 * 
	 * @return 返回SERVLET环境下的文件相对路径
	 */
	Upload getPath();

	/**
	 * 获取谁上传的该文件
	 * 
	 * @return 用户帐号
	 */
	String getUploadBy();

	/**
	 * 获取什么时候上传的该文件
	 * 
	 * @return 上传日期
	 */
	Date getUploadDate();

	/**
	 * 获取上传文件内容字节大小
	 * 
	 * @return
	 */
	Long getContentLength();

	/**
	 * 获取上传文件内容类型
	 * 
	 * @return
	 */
	String getContentType();

	void setTenantId(Long id);

	void setContentType(String contentType);

	void setExtName(String fileExtension);

	void setLocalName(String fileLocalName);

	void setContentLength(long length);

	void setPath(Upload path);

	void setModule(IFunMenu module);

	void setBzfield(IEntityColumn field);

}
