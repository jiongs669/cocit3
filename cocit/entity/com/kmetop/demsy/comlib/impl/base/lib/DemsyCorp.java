package com.kmetop.demsy.comlib.impl.base.lib;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.entity.IDemsyCorp;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@CocTable(name = "企业用户管理", code = LibConst.BIZSYS_DEMSY_CORP, catalog = LibConst.BIZCATA_DEMSY_ADMIN, orderby = LibConst.ORDER_DEMSY_CORP, buildin = true//
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
public class DemsyCorp extends BizComponent implements IDemsyCorp {

	/**
	 * @deprecated
	 */
	public void setSoftID(Long id) {
		// throw new java.lang.UnsupportedOperationException("不能为实体设置应用ID");
	}

	/**
	 * @deprecated
	 */
	public Long getSoftID() {
		// throw new java.lang.UnsupportedOperationException("不能获取实体应用ID");
		return null;
	}

}
