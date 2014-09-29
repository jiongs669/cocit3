package com.jiongsoft.cocit.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.mvc.adaptor.EntityParamNode;
import com.jiongsoft.cocit.orm.Orm;
import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.orm.expr.Expr;
import com.jiongsoft.cocit.service.EntityManager;
import com.jiongsoft.cocit.service.ModuleService;
import com.jiongsoft.cocit.service.OperationService;
import com.jiongsoft.cocit.service.ServiceFactory;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.service.TableService;
import com.jiongsoft.cocit.ui.model.widget.WidgetModelFactory;
import com.jiongsoft.cocit.util.UrlAPI;
import com.jiongsoft.cocit.util.Json;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;

/**
 * 功能助手类：用于将一个指定的功能操作表达式解析成一个操作助手对象。
 * <p>
 * 如：1:3:c 表示要对模块1上的数据表3中插入一条数据。
 * 
 * @author yongshan.ji
 * 
 */
public class ActionHelper {

	public ActionContext actionContext;

	public SoftService softService;

	public Orm orm;

	public Throwable exception;

	/*
	 * 下列属性只有在funcExpr存在的情况下才会被创建。
	 */
	public String funcExpr;

	public String entityArgs;

	public String moduleID;

	public String tableID;

	public String opMode;

	public ModuleService module;

	public TableService table;

	public OperationService op;

	public WidgetModelFactory widgetFactory;

	/*
	 * 
	 */
	public EntityManager entityManager;

	public Object entity;

	public String[] entityID;

	/**
	 * 创建一个“操作助手”对象
	 * 
	 * @param funcExpr
	 *            功能参数：“moduleID:tableID:opMode”
	 * @return
	 */
	public static ActionHelper make(String funcExpr) {
		return new ActionHelper(funcExpr, null, null);
	}

	/**
	 * 创建一个“操作助手”对象
	 * 
	 * @param funcExpr
	 *            功能参数：“moduleID:tableID:opMode”
	 * @param entityArgs
	 *            要加载的实体数据
	 * @param entityParamNode
	 *            实体HTTP参数节点
	 * @return
	 */
	public static ActionHelper make(String funcExpr, String entityArgs, EntityParamNode entityParamNode) {
		return new ActionHelper(funcExpr, entityArgs, entityParamNode);
	}

	private ActionHelper(String funcExpr, String entityArgs, EntityParamNode entityParamNode) {
		Log.debug("ActionHelper... {funcExpr:%s, entityArgs:%s, entityParamNode:%s}", funcExpr, entityArgs, entityParamNode);

		actionContext = Cocit.getActionContext();

		this.funcExpr = funcExpr;
		this.entityArgs = entityArgs;

		try {

			widgetFactory = Cocit.getWidgetModelFactory();

			// 解析操作参数
			parseFuncExpr(funcExpr);

			// 获取软件服务对象
			softService = actionContext.getSoftService();
			orm = softService.getOrm();
			// 初始化实体管理器
			entityManager = softService.getEntityManager(module, table);

			parseEntityArgs(entityArgs);

			// 注入HTTP参数到实体对象中
			if (entityParamNode != null && entityManager != null) {
				Class type = entityManager.getType();
				entity = entityParamNode.inject(Mirror.me(type), entity, null);
			}

		} catch (Throwable e) {
			Log.warn("", e);
			exception = e;
		}
	}

	private void parseFuncExpr(String funcExpr) {
		if (StringUtil.isNil(funcExpr))
			return;

		String[] array = UrlAPI.decodeArgs(funcExpr);

		moduleID = array.length > 0 ? array[0] : null;
		tableID = array.length > 1 ? array[1] : null;
		opMode = array.length > 2 ? array[2] : null;

		Log.debug("ActionHelper.parseOpArgs: funcExpr = %s {moduleID:%s, tableID:%s, opMode:%s}", funcExpr, moduleID, tableID, opMode);

		ServiceFactory serviceFactory = Cocit.getServiceFactory();
		if (!StringUtil.isNil(moduleID)) {
			try {
				module = serviceFactory.getModule(Long.parseLong(moduleID));
			} catch (NumberFormatException e) {
				module = serviceFactory.getModule(moduleID);
			}
		}
		if (!StringUtil.isNil(tableID)) {
			try {
				table = serviceFactory.getTable(Long.parseLong(tableID));
			} catch (NumberFormatException e) {
				table = serviceFactory.getTable(tableID);
			}
		} else if (module != null) {
			table = serviceFactory.getTable(module);
		}
		op = serviceFactory.getOperation(table, opMode);

		Log.debug("ActionHelper.parseOpArgs: moduleID = %s, tableID = %s", module, table);

	}

	private void parseEntityArgs(String entityArgs) {
		if (StringUtil.isNil(entityArgs))
			return;

		Log.debug("ActionHelper.parseEntityArgs: entityArgs = %s", entityArgs);

		// 加载单条数据
		entityID = StringUtil.toArray(entityArgs);
		if (entityID != null && entityID.length == 1) {
			Long id = StringUtil.castTo(entityID[0], 0L);
			entity = entityManager.load(id, opMode);
		}
	}

	private CndExpr makeRuleExprFromJson(String jsonExpr, StringBuffer logExpr) {

		CndExpr retExpr = null;
		if (StringUtil.isNil(jsonExpr)) {
			return null;
		} else if (jsonExpr.charAt(0) != '{') {
			retExpr = Expr.contains("name", jsonExpr);
			retExpr = retExpr.or(Expr.contains("code", jsonExpr));
			retExpr = retExpr.or(Expr.contains("desc", jsonExpr));
		} else {
			Map map = Json.fromJson(Map.class, jsonExpr);
			Iterator<String> exprs = map.keySet().iterator();
			while (exprs.hasNext()) {
				String prop = exprs.next();

				if (!StringUtil.isNil(prop)) {
					String value = map.get(prop).toString();

					if (!StringUtil.isNil(value)) {
						if (retExpr == null) {
							retExpr = Expr.contains(prop, value);

							logExpr.append("(" + prop + " contains " + value + ")");
						} else {
							retExpr = retExpr.and(Expr.contains(prop, value));

							logExpr.append(" and (" + prop + " contains " + value + ")");
						}
					}
				}
			}
		}

		return retExpr;
	}

	/**
	 * 将JSON表达式转换成 in 表达式，即JSON对象中的字段值为数组。
	 * <p>
	 * JSON格式：
	 * <p>
	 * <code>
	 * {field-1:["value-1","value-2",...,"value-n"], field-2:[...], ... , field-n: [...]}
	 * </code>
	 */
	private CndExpr makeInExprFromJson(String jsonExpr, StringBuffer logExpr) {

		if (StringUtil.isNil(jsonExpr) || jsonExpr.charAt(0) != '{') {
			return null;
		}

		CndExpr retExpr = null;

		Map map = Json.fromJson(Map.class, jsonExpr);
		Iterator<String> exprs = map.keySet().iterator();
		while (exprs.hasNext()) {
			String prop = exprs.next();
			Object value = map.get(prop);
			if (value instanceof List) {
				List valueList = (List) value;

				if (retExpr == null) {
					retExpr = Expr.in(prop, valueList);

					logExpr.append("(" + prop + " in " + Json.toJson(valueList) + ")");
				} else {
					retExpr = retExpr.and(Expr.in(prop, valueList));

					logExpr.append(" and (" + prop + " in " + Json.toJson(valueList) + ")");
				}
			} else {
				String str = value.toString();

				if (retExpr == null) {
					retExpr = Expr.eq(prop, str);

					logExpr.append("(" + prop + " eq " + str + ")");
				} else {
					retExpr = retExpr.and(Expr.eq(prop, str));

					logExpr.append("and (" + prop + " in " + str + ")");
				}
			}
		}

		return retExpr;
	}

	/**
	 * 解析查询条件，可以支持如下三种查询表达式：
	 * <UL>
	 * <LI>参数(query.filterExpr)格式：{field-1:["value-1","value-2",...,"value-n"], field-2:[...], ... , field-n: [...]}
	 * <LI>参数(query.parentExpr)格式：{field-1:["value-1","value-2",...,"value-n"], field-2:[...], ... , field-n: [...]}
	 * <LI>参数(query.keywords)格式：{field-1 eq: "value-1", field-2 gt: value_2, ...}
	 * </UL>
	 */
	@SuppressWarnings("unused")
	public CndExpr makeExpr() {
		StringBuffer logExpr = new StringBuffer();
		CndExpr retExpr = null;

		String queryFilterExpr = actionContext.getParameterValue("query.filterExpr", "");
		String queryParentExpr = actionContext.getParameterValue("query.parentExpr", "");
		String queryKeywords = actionContext.getParameterValue("query.keywords", "");

		CndExpr naviTreeExpr = this.makeInExprFromJson(queryFilterExpr, logExpr);
		CndExpr parentGridExpr = this.makeInExprFromJson(queryParentExpr, logExpr);
		CndExpr searchBoxExpr = this.makeRuleExprFromJson(queryKeywords, logExpr);
		if (naviTreeExpr != null) {
			if (retExpr == null)
				retExpr = naviTreeExpr;
			else
				retExpr = retExpr.and(naviTreeExpr);
		}
		if (parentGridExpr != null) {
			if (retExpr == null)
				retExpr = parentGridExpr;
			else
				retExpr = retExpr.and(parentGridExpr);
		}
		if (searchBoxExpr != null) {
			if (retExpr == null)
				retExpr = searchBoxExpr;
			else
				retExpr = retExpr.and(searchBoxExpr);
		}

		// 解析JSON表达式
		if (retExpr == null) {
			retExpr = Expr.notNull("id");
			logExpr.append("(id not null)");
		}

		/*
		 * 解析排序
		 */
		String sortField = actionContext.getParameterValue("sortField", "id");
		String sortOrder = actionContext.getParameterValue("sortOrder", "desc");
		if (!StringUtil.isNil(sortField)) {
			if (sortOrder.toLowerCase().equals("asc")) {
				retExpr = retExpr.addAsc(sortField);
			} else {
				retExpr = retExpr.addDesc(sortField);
			}
			logExpr.append("( order by " + sortField + " " + sortOrder + ")");
		}

		/*
		 * 解析分页
		 */
		int pageIndex = actionContext.getParameterValue("pageIndex", 0);
		int pageSize = actionContext.getParameterValue("pageSize", 0);
		if (pageIndex > 0 && pageSize > 0) {
			retExpr = retExpr.setPager(pageIndex, pageSize);
			logExpr.append("(pageIndex=" + pageIndex + " pageSize=" + pageSize + ")");
		}

		/*
		 * 返回解析结果
		 */
		Log.debug("查询条件：funcExpr = %s, queryExpr = %s", funcExpr, logExpr.toString());
		return retExpr;
	}
}
