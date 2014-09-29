package com.kmetop.demsy.comlib.biz.field;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.kmetop.demsy.lang.Str;

@CocField(uiTemplate = "ui.widget.field.Composite")
public class Css extends JsonField<Css> {
	@CocField(name = "宽度", order = 1)
	protected String width;

	@CocField(name = "高度", order = 2)
	protected String height;

	@CocField(name = "背景", order = 3, uiTemplate = "ui.widget.field.CssBackground")
	protected CssBackground background;

	@CocField(name = "边框", order = 4, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder border;

	@CocField(name = "上边框", order = 5, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder borderTop;

	@CocField(name = "下边框", order = 6, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder borderBottom;

	@CocField(name = "左边框", order = 7, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder borderLeft;

	@CocField(name = "右边框", order = 8, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder borderRight;

	@CocField(name = "字体", order = 9, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont font;

	public Css() {
		this("");
	}

	public Css(String str) {
		super(str);
	}

	@Override
	protected void init(Css obj) {
		if (obj != null) {
			this.width = obj.width;
			this.height = obj.height;
			this.background = obj.background;
			this.border = obj.border;
			this.borderTop = obj.borderTop;
			this.borderRight = obj.borderRight;
			this.borderBottom = obj.borderBottom;
			this.borderLeft = obj.borderLeft;
			this.font = obj.font;
		}
	}

	public String toCssStyle() {
		StringBuffer sb = new StringBuffer();

		if (!Str.isEmpty(width))
			sb.append("width:").append(width).append(";");
		if (!Str.isEmpty(height))
			sb.append("height:").append(height).append(";");
		if (background != null) {
			sb.append(background.toCssStyle());
		}
		if (border != null)
			sb.append(border.toCssStyle(""));
		if (borderTop != null)
			sb.append(borderTop.toCssStyle("-top"));
		if (borderRight != null)
			sb.append(borderRight.toCssStyle("-right"));
		if (borderBottom != null)
			sb.append(borderBottom.toCssStyle("-bottom"));
		if (borderLeft != null)
			sb.append(borderLeft.toCssStyle("-left"));
		if (font != null)
			sb.append(font.toCssStyle());

		return sb.toString();
	}

	public CssBackground getBackground() {
		return background;
	}

	public CssBorder getBorder() {
		return border;
	}

	public CssFont getFont() {
		return font;
	}

	public void setBackground(CssBackground background) {
		this.background = background;
	}

	public void setBorder(CssBorder border) {
		this.border = border;
	}

	public void setFont(CssFont font) {
		this.font = font;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public CssBorder getBorderTop() {
		return borderTop;
	}

	public void setBorderTop(CssBorder borderTop) {
		this.borderTop = borderTop;
	}

	public CssBorder getBorderRight() {
		return borderRight;
	}

	public void setBorderRight(CssBorder borderRight) {
		this.borderRight = borderRight;
	}

	public CssBorder getBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(CssBorder borderBottom) {
		this.borderBottom = borderBottom;
	}

	public CssBorder getBorderLeft() {
		return borderLeft;
	}

	public void setBorderLeft(CssBorder borderLeft) {
		this.borderLeft = borderLeft;
	}

}
