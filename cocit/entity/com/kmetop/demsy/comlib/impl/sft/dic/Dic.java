package com.kmetop.demsy.comlib.impl.sft.dic;

import static com.kmetop.demsy.comlib.LibConst.BIZCATA_BASE;

import java.util.List;

import javax.persistence.Column;
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
@CocTable(name = "字典数据维护", code = "Dic", catalog = BIZCATA_BASE, orderby = 101, buildin = true//
, actions = { @CocOperation(jsonData = "CommonBizAction.data.js"), //
		@CocOperation(name = "设置", typeCode = BizConst.TYPE_BZ_AUTO_MAKED_UPDATE_MENUS, mode = "set") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = {
//
		@CocField(property = "category") //
		, @CocField(name = "名称", property = "name", mode = "c:M e:M", tostring = true)//
		, @CocField(name = "编号", property = "code", mode = "c:M e:M")//
		, @CocField(property = "extCode") //
		, @CocField(name = "描述", property = "desc", gridField = false) //
		, @CocField(name = "停用状态", property = "disabled", disabledNavi = true, mode = "set:E", options = "1:停用,0:启用") //
		, @CocField(name = "人工顺序", property = "orderby", mode = "*:N v:P", gridField = false) //
		, @CocField(name = "创建时间", property = "created", mode = "*:N v:P") //
		, @CocField(name = "创建帐号", property = "createdBy", mode = "*:N v:P", gridField = false) //
		, @CocField(name = "更新时间", property = "updated", mode = "*:N v:P") //
		, @CocField(name = "更新帐号", property = "updatedBy", mode = "*:N v:P", gridField = false) //

}) //
}// end groups
)
public class Dic extends SFTBizComponent {
	public static final int MASK_DISABLED = 4;// 2^2

	@ManyToOne
	@CocField(name = "字典分类")
	protected DicCategory category;

	@ManyToOne
	protected Dic parent;

	@OneToMany(mappedBy = "parent")
	protected List<Dic> children;

	@Column(length = 255)
	@CocField(name = "扩展编号")
	protected String extCode;

	public boolean isDisabled() {
		return super.getMask(MASK_DISABLED);
	}

	public Boolean getDisabled() {
		return this.isDisabled();
	}

	public void setDisabled(boolean disabled) {
		super.setMask(MASK_DISABLED, disabled);
	}

	public DicCategory getCategory() {
		return category;
	}

	public void setCategory(DicCategory category) {
		this.category = category;
	}

	public String getExtCode() {
		return extCode;
	}

	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}

	public void setParent(Dic parent) {
		this.parent = parent;
	}

	public void setChildren(List<Dic> children) {
		this.children = children;
	}
}
