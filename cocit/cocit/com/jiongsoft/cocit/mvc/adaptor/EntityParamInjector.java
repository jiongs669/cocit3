package com.jiongsoft.cocit.mvc.adaptor;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.mvc.adaptor.ParamExtractor;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.adaptor.Params;

import com.jiongsoft.cocit.orm.expr.ExprRule;
import com.jiongsoft.cocit.util.StringUtil;

public class EntityParamInjector implements ParamInjector {

	private String prefix;

	public EntityParamInjector(String prefix) {
		if ("".equals(prefix))
			this.prefix = "entity.";
		else
			this.prefix = prefix;
	}

	public Object get(ServletContext sc, HttpServletRequest req, HttpServletResponse resp, Object refer) {
		return get(sc, req, resp, refer, prefix);
	}

	public static EntityParamNode get(ServletContext sc, HttpServletRequest req, HttpServletResponse resp, Object refer, String prefix) {
		EntityParamNode node = new EntityParamNode();

		List<ExprRule> rules = getExprRules(req, "naviRules");
		for (ExprRule rule : rules) {
			if (rule.getData() != null)
				node.put(prefix + rule.getField(), StringUtil.toArray(rule.getData().toString(), ","));
		}

		ParamExtractor pe = Params.makeParamExtractor(req, refer);
		for (Object name : pe.keys()) {
			String na = (String) name;
			if (na.startsWith(prefix)) {
				node.put(na, pe.extractor(na));
			}
		}

		return node;
	}

	protected static List<ExprRule> getExprRules(HttpServletRequest req, String param) {
		String naviRuleString = req.getParameter(param);
		List<String> ruleList = (List) Json.fromJson(naviRuleString);

		List<ExprRule> rules = new LinkedList();
		if (ruleList != null && ruleList.size() > 0) {
			ExprRule rule = null;
			for (String str : ruleList) {
				if (StringUtil.isNil(str)) {
					continue;
				}

				rule = new ExprRule(str);
				String op = rule.getOp();
				if ("eq".equals(op) || "=".equals(op)) {
					rules.add(rule);
				}

			}
		}

		return rules;
	}
}
