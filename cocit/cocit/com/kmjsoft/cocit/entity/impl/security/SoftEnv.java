package com.kmjsoft.cocit.entity.impl.security;

import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;

import com.kmjsoft.cocit.entity.EntityConst;
import com.kmjsoft.cocit.entity.BaseNamedEntity;
import com.kmjsoft.cocit.entity.security.ISystem;
import com.kmjsoft.cocit.orm.annotation.CocField;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocOperation;
import com.kmjsoft.cocit.orm.annotation.CocTable;

@Entity
@CocTable(name = "企业用户管理", code = EntityConst.BIZSYS_DEMSY_CORP, catalog = EntityConst.BIZCATA_DEMSY_ADMIN, orderby = EntityConst.ORDER_DEMSY_CORP, buildin = true//
, actions = { @CocOperation(name = "新增企业", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @CocOperation(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = { @CocField(name = "企业名称", property = "name")//
		, @CocField(name = "企业编号", property = "code")//
		, @CocField(name = "人工顺序", property = "orderby") //
		, @CocField(name = "软件描述", property = "desc") //
		, @CocField(name = "内置状态", property = "buildin", mode = "*:N") //
		, @CocField(name = "创建时间", property = "created", mode = "*:P") //
		, @CocField(name = "更新时间", property = "updated", mode = "*:P") //
		, @CocField(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @CocField(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class SoftEnv extends BaseNamedEntity implements ISystem {

	/**
	 * @deprecated
	 */
	public void setTenantId(Long id) {
		// throw new java.lang.UnsupportedOperationException("不能为实体设置应用ID");
	}

}
