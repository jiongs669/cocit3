package com.jiongsoft.cocit.entitydef.field;

import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.orm.annotation.CocColumn;

@CocColumn(uiTemplate = "ui.widget.field.Composite")
public class CssLink extends JsonField<CssLink> {
	@CocColumn(name = "链接", SN = 10, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont a;

	@CocColumn(name = "未访问的链接", SN = 11, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont aLink;

	@CocColumn(name = "已访问的链接", SN = 12, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont aVisited;

	@CocColumn(name = "鼠标悬停链接", SN = 13, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont aHover;

	@CocColumn(name = "被选中的链接", SN = 14, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont aActive;

	public CssLink() {
		this("");
	}

	public CssLink(String str) {
		super(str);
	}

	@Override
	protected void init(CssLink obj) {
		if (obj != null) {
			this.a = obj.a;
			this.aLink = obj.aLink;
			this.aVisited = obj.aVisited;
			this.aHover = obj.aHover;
			this.aActive = obj.aActive;
		}
	}

	public String toCssStyle(String cssClass) {
		StringBuffer sb = new StringBuffer();

		if (a != null) {
			String css = a.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a{").append(css).append("}");
		}
		if (aLink != null) {
			String css = aLink.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a:link{").append(css).append("}");
		}
		if (aVisited != null) {
			String css = aVisited.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a:visited{").append(css).append("}");
		}
		if (aHover != null) {
			String css = aHover.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a:hover{").append(css).append("}");
		}
		if (aActive != null) {
			String css = aActive.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a:active{").append(css).append("}");
		}

		return sb.toString();
	}

}
