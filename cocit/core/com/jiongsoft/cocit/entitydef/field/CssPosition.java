package com.jiongsoft.cocit.entitydef.field;

import com.kmjsoft.cocit.orm.annotation.CocColumn;

@CocColumn(precision = 2000, uiTemplate = "ui.widget.field.Composite")
public class CssPosition extends JsonField<CssPosition> {

	@CocColumn(name = "区域", SN = 1, options = "page:内容,top:顶部,bottom:底部")
	private String area;

	@CocColumn(name = "位置", SN = 2, options = "absolute:绝对位置,relative:相对位置")
	private String position;

	@CocColumn(name = "对齐", SN = 3, isDimension = true, options = "1:左,2:右,3:顶,4:底")
	private Integer align;

	@CocColumn(name = "距左", SN = 4, uiTemplate = "ui.widget.field.Spinner")
	private Integer left;

	@CocColumn(name = "距顶", SN = 5, uiTemplate = "ui.widget.field.Spinner")
	private Integer top;

	@CocColumn(name = "宽度", SN = 6, uiTemplate = "ui.widget.field.Spinner")
	private Integer width;

	@CocColumn(name = "高度", SN = 7, uiTemplate = "ui.widget.field.Spinner")
	private Integer height;

	public CssPosition() {
		this("");
	}

	public CssPosition(String str) {
		super(str);
	}

	@Override
	protected void init(CssPosition obj) {
		if (obj != null) {
			this.area = obj.area;
			this.position = obj.position;
			this.align = obj.align;
			this.left = obj.left;
			this.top = obj.top;
			this.width = obj.width;
			this.height = obj.height;
		}
	}

	public String getPosition() {
		return position;
	}

	public Integer getLeft() {
		return left;
	}

	public Integer getTop() {
		return top;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getAlign() {
		return align;
	}

	public void setAlign(Integer align) {
		this.align = align;
	}

}
