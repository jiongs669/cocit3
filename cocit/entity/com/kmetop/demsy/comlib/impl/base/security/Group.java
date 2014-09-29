package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_USER_GROUP;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.security.IGroup;

@Entity
@CocTable(name = "后台帐号分组", catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_USER_GROUP, buildin = false//
, actions = { @CocOperation(name = "新增分组", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @CocOperation(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = { @CocField(name = "分组名称", property = "name", mode = "c:M e:M")//
		, @CocField(name = "分组编号", property = "code", mode = "c:M e:M") //
		, @CocField(property = "parent") //
		, @CocField(name = "分组描述", property = "desc") //
		, @CocField(name = "创建时间", property = "created", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocField(name = "更新时间", property = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocField(name = "创建帐号", property = "createdBy", mode = "v:S *:N") //
		, @CocField(name = "更新帐号", property = "updatedBy", mode = "v:S *:N") //
}) }// end groups
)
public class Group extends BizComponent implements IGroup {

	@ManyToOne
	@CocField(name = "上级分组")
	protected Group parent;

	protected boolean disabledSelected;

	public Group getParent() {
		return parent;
	}

	public void setParent(Group parent) {
		this.parent = parent;
	}

	public boolean isDisabledSelected() {
		return disabledSelected;
	}

	public void setDisabledSelected(boolean disabledSelected) {
		this.disabledSelected = disabledSelected;
	}

}
