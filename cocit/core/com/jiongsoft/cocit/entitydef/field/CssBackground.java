package com.jiongsoft.cocit.entitydef.field;

import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.mvc.MvcConst.MvcUtil;
import com.kmjsoft.cocit.orm.annotation.CocColumn;

@CocColumn(name = "背景", uiTemplate = "ui.widget.field.Composite")
public class CssBackground extends JsonField<CssBackground> {

	@CocColumn(name = "背景颜色", uiTemplate = "ui.widget.field.CssColor", SN = 1)
	protected String color;

	@CocColumn(name = "背景图片", uploadType = "*.jpg;*.gif;*.png", SN = 2)
	protected Upload image;

	@CocColumn(name = "图片平铺", options = ":平铺,repeat-x:横向,repeat-y:纵向,no-repeat:不平铺,inherit: 继承", SN = 3)
	protected String repeat;

	@CocColumn(name = "水平对齐", options = ":水平,left:居左,center:居中,right:居右", SN = 4)
	protected String positionX;

	@CocColumn(name = "垂直对齐", options = ":垂直,top:居顶,center:居中,bottom:居底", SN = 5)
	protected String positionY;

	public CssBackground() {
		this("");
	}

	public CssBackground(String str) {
		super(str);
	}

	@Override
	protected void init(CssBackground obj) {
		if (obj != null) {
			this.color = obj.color;
			this.image = obj.image;
			this.repeat = obj.repeat;
			this.positionX = obj.positionX;
			this.positionY = obj.positionY;
		}
	}

	public String toCssStyle() {
		if (Str.isEmpty(color) && (image == null || Str.isEmpty(image.toString())) && Str.isEmpty(repeat) && Str.isEmpty(positionX) && Str.isEmpty(positionY)) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		sb.append("background:");
		if (!Str.isEmpty(color))
			sb.append(color);
		if (image != null && !Str.isEmpty(image.toString()))
			sb.append(" ").append("url(").append(MvcUtil.contextPath(image.toString())).append(")");
		if (!Str.isEmpty(repeat))
			sb.append(" ").append(repeat);
		if (!Str.isEmpty(positionX))
			sb.append(" ").append(positionX);
		if (!Str.isEmpty(positionY))
			sb.append(" ").append(positionY);
		sb.append(";");
//
//		if (image != null && !Str.isEmpty(image.toString()) && image.toString().toLowerCase().endsWith(".png")) {
//			sb.append("filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true,");
//			if (repeat != null && !"no-repeat".equals(repeat)) {
//				sb.append("sizingMethod=scale,");
//			}else{
//				sb.append("sizingMethod=image,");//corp,image
//			}
//			sb.append("src='");
//			sb.append(MvcUtil.contextPath(image.toString().trim()));
//			sb.append("');_background:none;");
//		}

		return sb.toString();
	}

	public String getColor() {
		return color;
	}

	public Upload getImage() {
		return image;
	}

	public String getRepeat() {
		return repeat;
	}

	public String getPositionX() {
		return positionX;
	}

	public String getPositionY() {
		return positionY;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}

	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}

}
