package com.kmjsoft.cocit.entityengine.definition.impl;

import static com.jiongsoft.cocit.Demsy.entityDefManager;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_ACTION;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_FIELD;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_FIELD_GROUP;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.F_ORDER_BY;
import static com.kmjsoft.cocit.entity.EntityConst.F_REFRENCE_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.F_SYSTEM;

import java.util.List;

import com.jiongsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.definition.IEntityAction;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.entity.definition.IEntityField;
import com.kmjsoft.cocit.entity.definition.IFieldGroup;
import com.kmjsoft.cocit.orm.expr.CndExpr;
import com.kmjsoft.cocit.orm.expr.Expr;

class CacheBiz {

	private IEntityDefinition system;

	private List<? extends IFieldGroup> groups;

	private List<? extends IEntityField> fields;

	private List<? extends IEntityField> fieldsOfExport;

	private List<? extends IEntityAction> actions;

	CacheBiz(BizEngine engine, Long systemID) {
		if (systemID != null) {
			system = (IEntityDefinition) Demsy.orm().load(entityDefManager.getStaticType(BIZSYS_BZUDF_SYSTEM), systemID);

			if (system != null && Demsy.appconfig.isProductMode())
				engine.bizCache.put(systemID, this);
		}
	}

	IEntityDefinition get() {
		return system;
	}

	List<? extends IFieldGroup> groups() {
		if (groups == null) {
			groups = Demsy.orm().query(entityDefManager.getStaticType(BIZSYS_BZUDF_FIELD_GROUP), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
		}

		return groups;
	}

	List<? extends IEntityField> fields() {
		if (fields == null) {
			fields = Demsy.orm().query(entityDefManager.getStaticType(BIZSYS_BZUDF_FIELD), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
		}

		return fields;
	}

	List<? extends IEntityAction> actions() {
		if (actions == null) {
			actions = Demsy.orm().query(entityDefManager.getStaticType(BIZSYS_BZUDF_ACTION), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
		}

		return actions;
	}

	IEntityAction action(Long id) {
		actions();
		for (IEntityAction a : actions) {
			if (a.getId().equals(id)) {
				return a;
			}
		}

		return null;
	}

	IEntityAction action(String mode) {
		if (mode == null)
			return null;
		
		actions();
		for (IEntityAction a : actions) {
			if (mode.equals(a.getMode())) {
				return a;
			}
		}

		return null;
	}

	List<? extends IEntityField> fieldsOfExport() {
		if (fieldsOfExport == null) {
			fieldsOfExport = Demsy.orm().query(entityDefManager.getStaticType(BIZSYS_BZUDF_FIELD), CndExpr.eq(F_REFRENCE_SYSTEM, system));
		}

		return fieldsOfExport;
	}
}
