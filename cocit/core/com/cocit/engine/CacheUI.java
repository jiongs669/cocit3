package com.cocit.engine;

import static com.cocit.Demsy.entityDefEngine;
import static com.cocit.api.APIConst.BIZSYS_UIUDF_PAGE;
import static com.cocit.api.APIConst.BIZSYS_UIUDF_PAGE_BLOCK;
import static com.cocit.api.APIConst.F_DISABLED;
import static com.cocit.api.APIConst.F_ORDER_BY;
import static com.cocit.api.APIConst.F_UI_PAGE;

import java.util.List;

import com.cocit.Demsy;
import com.cocit.api.webdef.IPage;
import com.cocit.api.webdef.IPageBlock;
import com.jiongsoft.cocit.orm.expr.Expr;

class CacheUI {

	private IPage page;

	private List<? extends IPageBlock> blocks;

	CacheUI(UiEngine engine, Long pageID) {
		if (pageID != null) {
			page = (IPage) Demsy.orm().load(entityDefEngine.getStaticType(BIZSYS_UIUDF_PAGE), pageID);

			if (page != null) {
				blocks = Demsy.orm().query(entityDefEngine.getStaticType(BIZSYS_UIUDF_PAGE_BLOCK), Expr.eq(F_UI_PAGE, page).and(Expr.eq(F_DISABLED, false)).addAsc(F_ORDER_BY));

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
