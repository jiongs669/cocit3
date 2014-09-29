package com.cocit.api.webdef;

import com.cocit.api.entity.INamedEntity;
import com.cocit.api.entitydef.field.FakeSubSystem;

public interface IPage extends INamedEntity {
	public static final int USAGE_TPL = 0;

	public static final int USAGE_IDX = 1;

	public static final int USAGE_LOGIN = 2;

	public static final int USAGE_ADMIN = 3;

	public String getPageWidth();

	public String getPageHeight();

	public String getUiTemplate();

	public IStyle getStyle();

	String getKeywords();

	public FakeSubSystem<? extends IStyleItem> getStyleItems();
}
