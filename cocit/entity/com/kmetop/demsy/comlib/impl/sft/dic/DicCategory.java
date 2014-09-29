package com.kmetop.demsy.comlib.impl.sft.dic;

import static com.kmetop.demsy.comlib.LibConst.BIZCATA_BASE;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.kmetop.demsy.biz.BizConst;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
@CocTable(name = "字典分类设置", code = "DicCategory", catalog = BIZCATA_BASE, orderby = 100, buildin = true//
, actions = { @CocOperation(jsonData = "CommonBizAction.data.js"), //
		@CocOperation(name = "设置", typeCode = BizConst.TYPE_BZ_AUTO_MAKED_UPDATE_MENUS, mode = "set") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = {
//
		@CocField(name = "字典名称", property = "name", mode = "c:M e:M", tostring = true)//
		, @CocField(property = "parent") //
		, @CocField(name = "字典描述", property = "desc", gridField = false) //
		, @CocField(name = "停用状态", property = "disabled", disabledNavi = true, mode = "set:E", options = "1:停用,0:启用") //
		, @CocField(name = "人工顺序", property = "orderby", mode = "*:N v:P", gridField = false) //
		, @CocField(name = "创建时间", property = "created", mode = "*:N v:P") //
		, @CocField(name = "创建帐号", property = "createdBy", mode = "*:N v:P", gridField = false) //
		, @CocField(name = "更新时间", property = "updated", mode = "*:N v:P") //
		, @CocField(name = "更新帐号", property = "updatedBy", mode = "*:N v:P", gridField = false) //

}) //
}// end groups
)
public class DicCategory extends SFTBizComponent {

	@OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
	protected List<Dic> dics;

	@ManyToOne
	@CocField(name = "上级分类")
	protected DicCategory parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	protected List<DicCategory> children;

	public List getDics() {
		return dics;
	}

	public void setDics(List<Dic> dics) {
		this.dics = dics;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List<DicCategory> children) {
		this.children = children;
	}

	public DicCategory getParent() {
		return parent;
	}

	public void setParent(DicCategory parent) {
		this.parent = parent;
	}
}
