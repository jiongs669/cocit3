package com.kmjsoft.cocit.entity.impl.config;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_DEMSY_ADMIN;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_DATASOURCE;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_DEMSY_DATASOURCE;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.jiongsoft.cocit.config.IDataSourceConfig;
import com.jiongsoft.cocit.lang.Ex;
import com.jiongsoft.cocit.lang.Props;
import com.kmjsoft.cocit.entity.IDynaEntity;
import com.kmjsoft.cocit.entity.NamedEntity;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "数据源管理", GUID = BIZSYS_DEMSY_DATASOURCE, catalog = BIZCATA_DEMSY_ADMIN, SN = ORDER_DEMSY_DATASOURCE, isBuildin = true//
, actions = { @CocAction(name = "新增数据源", type = TYPE_BZFORM_NEW, mode = "c")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "数据源名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "人工顺序", propName = "orderby") //
		, @CocColumn(name = "数据库URL", propName = "url", mode = "c:M e:M") //
		, @CocColumn(name = "数据库驱动", propName = "driver", mode = "c:M e:M") //
		, @CocColumn(name = "数据库用户", propName = "user", mode = "c:M e:M") //
		, @CocColumn(name = "数据库密码", propName = "pwd") //
		, @CocColumn(name = "数据源描述", propName = "desc") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "*:P") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "*:P") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:P") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class DataSource extends NamedEntity implements IDataSourceConfig, IDynaEntity {
	private String url;

	private String driver;

	@Column(length = 64)
	private String user;

	@Column(length = 64)
	private String pwd;

	@Column(name = "_ext_status_code")
	protected Integer extStatusCode;

	@Column(name = "_ext_field_values", columnDefinition = "text")
	protected String extFieldValues;

	@Transient
	protected Properties extProps;

	public String getUrl() {
		return url;
	}

	public String getDriver() {
		return driver;
	}

	public String getUser() {
		return user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setExtFieldValues(String props) {
		extFieldValues = props;
		try {
			extProps = Props.toProps(props);
		} catch (IOException e) {
			throw Ex.throwEx(e);
		}
	}

	@Override
	public Properties getProperties() {
		if (extProps == null) {
			try {
				extProps = Props.toProps(this.extFieldValues);
			} catch (IOException e) {
				throw Ex.throwEx(e);
			}
		}
		return extProps;
	}

	public String get(String key) {
		return (String) getProperties().get(key);
	}

	public void set(String key, Object value) {
		if (value == null)
			getProperties().remove(key);
		else
			getProperties().put(key, value);

		try {
			this.extFieldValues = Props.toString(extProps);
		} catch (IOException e) {
			throw Ex.throwEx(e);
		}
	}

}
