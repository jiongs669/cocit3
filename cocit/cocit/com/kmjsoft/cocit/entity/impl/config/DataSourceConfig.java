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
import com.kmjsoft.cocit.entity.BaseNamedEntity;
import com.kmjsoft.cocit.orm.annotation.CocField;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocOperation;
import com.kmjsoft.cocit.orm.annotation.CocTable;

@Entity
@CocTable(name = "应用系统数据源", code = BIZSYS_DEMSY_DATASOURCE, catalog = BIZCATA_DEMSY_ADMIN, orderby = ORDER_DEMSY_DATASOURCE, buildin = true//
, actions = { @CocOperation(name = "新增数据源", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @CocOperation(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = { @CocField(name = "数据源名称", property = "name", mode = "c:M e:M")//
		, @CocField(name = "人工顺序", property = "orderby") //
		, @CocField(name = "数据库URL", property = "url", mode = "c:M e:M") //
		, @CocField(name = "数据库驱动", property = "driver", mode = "c:M e:M") //
		, @CocField(name = "数据库用户", property = "user", mode = "c:M e:M") //
		, @CocField(name = "数据库密码", property = "pwd") //
		, @CocField(name = "数据源描述", property = "desc") //
		, @CocField(name = "创建时间", property = "created", mode = "*:P") //
		, @CocField(name = "更新时间", property = "updated", mode = "*:P") //
		, @CocField(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @CocField(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class DataSourceConfig extends BaseNamedEntity implements IDataSourceConfig, IDynaEntity {
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

	public void set(byte index, boolean flag) {
		int MASK = new Double(Math.pow(2, new Double(index - 1))).intValue();
		if (extStatusCode == null) {
			extStatusCode = 0;
		}
		synchronized (extStatusCode) {
			if (flag) {// 隐藏
				extStatusCode |= MASK;
			} else {// 不隐藏
				int mask = ~MASK;// 反码
				extStatusCode &= mask;// 与
			}
		}
	}

	public boolean is(byte index) {
		int MASK = new Double(Math.pow(2, new Double(index - 1))).intValue();
		if (extStatusCode == null) {
			return false;
		}
		synchronized (extStatusCode) {
			return (extStatusCode & MASK) > 0;
		}
	}

}
