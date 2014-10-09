package com.kmjsoft.cocit.entityengine.definition.impl;

import static com.kmjsoft.cocit.Demsy.entityDefManager;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_UIUDF_PAGE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_UIUDF_PAGE_BLOCK;
import static com.kmjsoft.cocit.entity.EntityConst.F_DISABLED;
import static com.kmjsoft.cocit.entity.EntityConst.F_ORDER_BY;
import static com.kmjsoft.cocit.entity.EntityConst.F_UI_PAGE;

import java.util.List;

import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.webdef.IPage;
import com.kmjsoft.cocit.entity.webdef.IPageBlock;
import com.kmjsoft.cocit.orm.expr.Expr;

class CacheUI {

	private IPage page;

	private List<? extends IPageBlock> blocks;

	CacheUI(UiEngine engine, Long pageID) {
		if (pageID != null) {
			page = (IPage) Demsy.orm().load(entityDefManager.getStaticType(BIZSYS_UIUDF_PAGE), pageID);

			if (page != null) {
				blocks = Demsy.orm().query(entityDefManager.getStaticType(BIZSYS_UIUDF_PAGE_BLOCK), Expr.eq(F_UI_PAGE, page).and(Expr.eq(F_DISABLED, false)).addAsc(F_ORDER_BY));

				if (Demsy.appconfig.isProductMode()) {
					engine.pageCache.put(pageID, this);
					for (IPageBlock block : blocks) {
						engine.pageBlockCache.put(block.getId(), block);
					}
				}
			}
		}
	}

	IPage get() {
		return page;
	}

	List<? extends IPageBlock> blocks() {
		return blocks;
	}
}
