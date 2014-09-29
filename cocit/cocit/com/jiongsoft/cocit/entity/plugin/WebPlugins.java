package com.jiongsoft.cocit.entity.plugin;

import java.util.List;

import com.jiongsoft.cocit.entity.ActionEvent;
import com.jiongsoft.cocit.entity.WebCatalogEntity;
import com.jiongsoft.cocit.entity.WebContentEntity;
import com.jiongsoft.cocit.orm.Orm;
import com.jiongsoft.cocit.orm.expr.Expr;
import com.jiongsoft.cocit.util.CocException;
import com.jiongsoft.cocit.util.StringUtil;

/**
 * 处理网站栏目和网站信息发布的业务逻辑插件。
 * 
 * @author yongshan.ji
 * 
 */
public abstract class WebPlugins {
	/*
	 * 保存网站栏目前：检查栏目编号的唯一性
	 */
	public static class SaveWebCatalog extends BasePlugin<WebCatalogEntity> {
		@Override
		public void before(ActionEvent<WebCatalogEntity> event) {
			synchronized (SaveWebCatalog.class) {
				Orm orm = event.getOrm();

				WebCatalogEntity entity = event.getEntity();

				String catalogCode = entity.getCode();
				if (!StringUtil.isNil(catalogCode)) {
					WebCatalogEntity existedCatalog = (WebCatalogEntity) orm.get(entity.getClass(), Expr.eq("code", catalogCode));
					if (existedCatalog != null && catalogCode.equals(existedCatalog.getCode())) {
						if (entity.getId() != existedCatalog.getId()) {
							throw new CocException("栏目编码已经被占用！");
						}
					}
				}
			}
		}
	}

	/**
	 * 信息发布前：设置冗余字段“栏目编码”
	 * 
	 * @author yongshan.ji
	 * 
	 */
	public static class SaveWebContent extends BasePlugin<WebContentEntity> {

		@Override
		public void before(ActionEvent<WebContentEntity> event) {
			Orm orm = event.getOrm();

			WebContentEntity entity = event.getEntity();

			/*
			 * 获取栏目编码
			 */
			WebCatalogEntity catalog = entity.getCatalog();
			String catalogCode = catalog.getCode();
			if (StringUtil.isNil(catalogCode)) {
				catalog = (WebCatalogEntity) orm.load(catalog.getClass(), catalog.getId());
				catalogCode = catalog.getCode();
			}

			/**
			 * 设置栏目编码（冗余字段）
			 */
			entity.setCatalogCode(catalogCode);
		}
	}

	/**
	 * 批量“变更栏目”前：调整网站内容实体的冗余字段“栏目编码”
	 * 
	 * @author yongshan.ji
	 * 
	 */
	public static class SaveWebContentList extends BasePlugin<List<WebContentEntity>> {

		@Override
		public void before(ActionEvent<List<WebContentEntity>> event) {
			Orm orm = event.getOrm();

			WebCatalogEntity catalog = null;
			String catalogCode = "";

			List<WebContentEntity> list = event.getEntity();
			for (WebContentEntity entity : list) {

				/*
				 * 获取栏目编码
				 */
				if (catalog == null) {
					catalog = entity.getCatalog();
					catalogCode = catalog.getCode();
					if (StringUtil.isNil(catalogCode)) {
						catalog = (WebCatalogEntity) orm.load(catalog.getClass(), catalog.getId());
						catalogCode = catalog.getCode();
					}
				}

				/**
				 * 设置栏目编码（冗余字段）
				 */
				entity.setCatalogCode(catalogCode);
			}
		}
	}
}
