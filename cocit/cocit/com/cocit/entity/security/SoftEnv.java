package com.cocit.entity.security;

import static com.cocit.biz.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;

import com.cocit.api.APIConst;
import com.cocit.api.security.ISoftEnv;
import com.cocit.entity.NamedEntity;
import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;

@Entity
@CocTable(name = "企业用户管理", code = APIConst.BIZSYS_DEMSY_CORP, catalog = APIConst.BIZCATA_DEMSY_ADMIN, orderby = APIConst.ORDER_DEMSY_CORP, buildin = true//
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
public class SoftEnv extends NamedEntity implements ISoftEnv {

	/**
	 * @deprecated
	 */
	public void setTenantId(Long id) {
		// throw new java.lang.UnsupportedOperationException("不能为实体设置应用ID");
	}

}
