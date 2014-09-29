package com.cocit.engine;

import static com.cocit.Demsy.entityDefEngine;
import static com.cocit.api.APIConst.BIZSYS_BZUDF_ACTION;
import static com.cocit.api.APIConst.BIZSYS_BZUDF_FIELD;
import static com.cocit.api.APIConst.BIZSYS_BZUDF_FIELD_GROUP;
import static com.cocit.api.APIConst.BIZSYS_BZUDF_SYSTEM;
import static com.cocit.api.APIConst.F_ORDER_BY;
import static com.cocit.api.APIConst.F_REFRENCE_SYSTEM;
import static com.cocit.api.APIConst.F_SYSTEM;

import java.util.List;

import com.cocit.Demsy;
import com.cocit.api.entitydef.IEntityAction;
import com.cocit.api.entitydef.IEntityDefinition;
import com.cocit.api.entitydef.IFieldDefinition;
import com.cocit.api.entitydef.IFieldGroup;
import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.orm.expr.Expr;

class CacheBiz {

	private IEntityDefinition system;

	private List<? extends IFieldGroup> groups;

	private List<? extends IFieldDefinition> fields;

	private List<? extends IFieldDefinition> fieldsOfExport;

	private List<? extends IEntityAction> actions;

	CacheBiz(BizEngine engine, Long systemID) {
		if (systemID != null) {
			system = (IEntityDefinition) Demsy.orm().load(entityDefEngine.getStaticType(BIZSYS_BZUDF_SYSTEM), systemID);

			if (system != null && Demsy.appconfig.isProductMode())
				engine.bizCache.put(systemID, this);
		}
	}

	IEntityDefinition get() {
		return system;
	}

	List<? extends IFieldGroup> groups() {
		if (groups == null) {
			groups = Demsy.orm().query(entityDefEngine.getStaticType(BIZSYS_BZUDF_FIELD_GROUP), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
		}

		return groups;
	}

	List<? extends IFieldDefinition> fields() {
		if (fields == null) {
			fields = Demsy.orm().query(entityDefEngine.getStaticType(BIZSYS_BZUDF_FIELD), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
		}

		return fields;
	}

	List<? extends IEntityAction> actions() {
		if (actions == null) {
			actions = Demsy.orm().query(entityDefEngine.getStaticType(BIZSYS_BZUDF_ACTION), Expr.eq(F_SYSTEM, system).addAsc(F_ORDER_BY));
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

	List<? extends IFieldDefinition> fieldsOfExport() {
		if (fieldsOfExport == null) {
			fieldsOfExport = Demsy.orm().query(entityDefEngine.getStaticType(BIZSYS_BZUDF_FIELD), CndExpr.eq(F_REFRENCE_SYSTEM, system));
		}

		return fieldsOfExport;
	}
}
