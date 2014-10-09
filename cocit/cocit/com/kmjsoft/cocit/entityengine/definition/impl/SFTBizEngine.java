package com.kmjsoft.cocit.entityengine.definition.impl;

import static com.kmjsoft.cocit.entity.EntityConst.F_CODE;
import static com.kmjsoft.cocit.entity.EntityConst.F_MODE;
import static com.kmjsoft.cocit.entity.EntityConst.F_PROP_NAME;
import static com.kmjsoft.cocit.entity.EntityConst.F_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.F_TYPE_CODE;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.nutz.lang.Mirror;

import com.jiongsoft.cocit.entitydef.field.IRuntimeField;
import com.jiongsoft.cocit.entitydef.field.SubSystem;
import com.jiongsoft.cocit.entitydef.field.Upload;
import com.jiongsoft.cocit.lang.Cls;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.lang.JSON;
import com.jiongsoft.cocit.lang.Obj;
import com.jiongsoft.cocit.lang.Option;
import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.entity.DataEntity;
import com.kmjsoft.cocit.entity.definition.IEntityAction;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.entity.definition.IFieldDataType;
import com.kmjsoft.cocit.entity.definition.IEntityColumn;
import com.kmjsoft.cocit.entity.impl.config.Dic;
import com.kmjsoft.cocit.entity.impl.config.DicCategory;
import com.kmjsoft.cocit.entity.impl.definition.ActionDefinition;
import com.kmjsoft.cocit.entity.impl.definition.EntityAction;
import com.kmjsoft.cocit.entity.impl.definition.EntityCatalog;
import com.kmjsoft.cocit.entity.impl.definition.EntityDefinition;
import com.kmjsoft.cocit.entity.impl.definition.EntityColumn;
import com.kmjsoft.cocit.entity.impl.definition.EntityColumnGroup;
import com.kmjsoft.cocit.entity.impl.definition.SystemDataType;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.orm.ExtOrm;
import com.kmjsoft.cocit.orm.annotation.CocCatalog;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;
import com.kmjsoft.cocit.orm.expr.CndExpr;
import com.kmjsoft.cocit.orm.expr.Expr;

public class SFTBizEngine extends BizEngine {

	public SFTBizEngine() {
		compiler = new SFTBizCompiler(this);
	}

	@Override
	public String getPackageOfAutoSystem(IEntityDefinition system) {
		return "com.kmetop.demsy.comlib.impl.sft.dynamic.system";
	}

	public String getSimpleClassName(IEntityDefinition system) {
		return "SFTSystem_" + Long.toHexString(system.getId());
	}

	@Override
	public String getExtendClassName(IEntityDefinition system) {
		String extClass = system.getExtendClass();
		if (Str.isEmpty(extClass)) {
			return DataEntity.class.getName();
		} else {
			extClass = extClass.substring(extClass.lastIndexOf(".") + 1);
			if (extClass.equals("AbstractSFTEntity")) {
				return DataEntity.class.getName();
			}
		}
		if (extClass.indexOf(".") < 0) {
			return "com.kmetop.demsy.comlib.entity.base." + extClass;
		}
		return extClass;
	}

	@Override
	public String getExtendSimpleClassName(IEntityDefinition system) {
		String extClass = system.getExtendClass();
		if (Str.isEmpty(extClass)) {
			return DataEntity.class.getSimpleName();
		} else {
			extClass = extClass.substring(extClass.lastIndexOf(".") + 1);
			if (extClass.equals("AbstractSFTEntity")) {
				return DataEntity.class.getSimpleName();
			}
		}
		return extClass;
	}

	@Override
	public String getTargetPropName(IEntityColumn field) {
		return ("childrenMapping_" + Integer.toHexString(field.getSystem().getCode().hashCode()) + "_" + getPropName(field)).toLowerCase();
	}

	protected Option[] geDicOptions(IEntityColumn field) {
		EntityColumn fld = (EntityColumn) field;
		DicCategory dicc = fld.getDicCategory();

		List<Option> oplist = new ArrayList();
		if (dicc != null && !dicc.isDisabled()) {
			List<Dic> list = dicc.getDics();
			for (Dic dic : list) {
				if (!dic.isDisabled() && !dic.isDisabled())
					oplist.add(new Option("" + dic.getId(), dic.getName()));
			}
		}

		Option[] ret = new Option[oplist.size()];
		ret = oplist.toArray(ret);

		return ret;
	}

	protected boolean isMappingToMaster(IEntityColumn fld) {
		EntityColumn data = (EntityColumn) fld;
		return data.isMappingToMaster() || data.isMapping();
	}

	public boolean isGridField(IEntityColumn fld) {
		EntityColumn data = (EntityColumn) fld;
		return data.isGridField() || data.isShowInGrid();
	}

	@Override
	public IEntityDefinition setupSystemFromClass(ITenant soft, Class klass) throws DemsyException {
		return setupSystemFromClass(soft.getDataGuid(), klass, true);
	}

	private IEntityDefinition setupSystemFromClass(String soft, Class klass, boolean autoUpdate) throws DemsyException {
		Map<String, IFieldDataType> fieldLibs = this.getFieldTypes();
		if (klass == null) {
			return null;
		}
		CocEntity sysann = (CocEntity) klass.getAnnotation(CocEntity.class);
		Entity entity = (Entity) klass.getAnnotation(Entity.class);
		if (sysann == null || entity == null) {
			return null;
		}

		if (log.isTraceEnabled())
			log.tracef("安装业务系统... [%s]", klass.getSimpleName());

		ExtOrm orm = orm();

		// 解析业务系统
		int sysOrder = sysann.SN();
		String sysCode = sysann.GUID();
		if (Str.isEmpty(sysCode)) {
			sysCode = "_" + klass.getSimpleName();
		}
		EntityDefinition system = (EntityDefinition) orm.load(EntityDefinition.class, CndExpr.eq(F_CODE, sysCode));
		if (system != null && !autoUpdate) {
			return system;
		}
		system = this.parseSystem(orm, soft, klass, system, sysann, sysOrder, sysCode);
		orm.save(system);

		Mirror me = Mirror.me(klass);

		// 计算业务操作
		List<EntityAction> newActions = new LinkedList();
		CocAction[] actanns = sysann.actions();
		if (actanns != null) {
			int actionOrder = sysOrder * 100;
			for (int i = 0; i < actanns.length; i++) {
				CocAction actann = actanns[i];

				if (Str.isEmpty(actann.jsonData())) {
					EntityAction action = this.parseBizAction(orm, soft, system, actann, actionOrder++);
					orm.save(action);
					newActions.add(action);
				} else {
					newActions.addAll(this.parseBizAction(orm, soft, system, actann.jsonData(), actionOrder++));
				}

			}
		}
		List<EntityColumnGroup> newGroups = new LinkedList();
		List<IEntityColumn> newFields = new LinkedList();
		CocGroup[] grpanns = sysann.groups();
		if (grpanns != null) {
			int groupOrder = sysOrder * 10;
			int fieldOrder = sysOrder * 100;
			int gridOrder = 100;
			for (int i = 0; i < grpanns.length; i++) {
				CocGroup grpann = grpanns[i];
				EntityColumnGroup group = this.parseBizGroup(orm, soft, system, grpann, groupOrder++);
				orm.save(group);
				newGroups.add(group);

				// 计算业务字段
				CocColumn[] fldanns = grpann.fields();
				if (fldanns != null) {
					for (int j = 0; j < fldanns.length; j++) {
						CocColumn fldann = fldanns[j];
						EntityColumn field = this.parseBizField(orm, soft, me, system, group, fldann, gridOrder++, fieldOrder++, fieldLibs);
						orm.save(field);
						newFields.add(field);
					}
				}
			}
		}

		// 清除垃圾数据
		// if (false) {
		List<EntityAction> oldActions = orm.query(EntityAction.class, Expr.eq(F_SYSTEM, system));
		for (EntityAction a : oldActions) {
			if (!newActions.contains(a)) {
				orm.delete(a);
			}
		}
		// }
		List<EntityColumn> oldFields = orm.query(EntityColumn.class, Expr.eq(F_SYSTEM, system));
		for (EntityColumn a : oldFields) {
			if (!newFields.contains(a)) {
				orm.delete(a);
			}
		}
		List<EntityColumnGroup> oldGroups = orm.query(EntityColumnGroup.class, Expr.eq(F_SYSTEM, system));
		for (EntityColumnGroup a : oldGroups) {
			if (!newGroups.contains(a)) {
				orm.delete(a);
			}
		}

		return system;
	}

	public void parseSystemByAnnotation(Class klass, IEntityDefinition system) {
		ExtOrm orm = orm();
		String tenantId = system.getTenantOwnerGuid();

		Map<String, IFieldDataType> fieldLibs = this.getFieldTypes();
		CocEntity sysann = (CocEntity) klass.getAnnotation(CocEntity.class);
		if (sysann == null) {
			return;
		}

		Integer sysOrder = system.getSerialNumber();

		Mirror me = Mirror.me(klass);

		// 计算业务操作
		List<EntityAction> newActions = new LinkedList();
		CocAction[] actanns = sysann.actions();
		if (actanns != null) {
			int actionOrder = sysOrder * 100;
			for (int i = 0; i < actanns.length; i++) {
				CocAction actann = actanns[i];

				if (Str.isEmpty(actann.jsonData())) {
					EntityAction action = this.parseBizAction(orm, tenantId, system, actann, actionOrder++);
					orm.save(action);
					newActions.add(action);
				} else {
					newActions.addAll(this.parseBizAction(orm, tenantId, system, actann.jsonData(), actionOrder++));
				}

			}
		}
		List<EntityColumnGroup> newGroups = new LinkedList();
		List<IEntityColumn> newFields = new LinkedList();
		CocGroup[] grpanns = sysann.groups();
		if (grpanns != null) {
			int groupOrder = sysOrder * 10;
			int fieldOrder = sysOrder * 100;
			int gridOrder = 100;
			for (int i = 0; i < grpanns.length; i++) {
				CocGroup grpann = grpanns[i];
				EntityColumnGroup group = this.parseBizGroup(orm, tenantId, system, grpann, groupOrder++);
				orm.save(group);
				newGroups.add(group);

				// 计算业务字段
				CocColumn[] fldanns = grpann.fields();
				if (fldanns != null) {
					for (int j = 0; j < fldanns.length; j++) {
						CocColumn fldann = fldanns[j];
						EntityColumn field = this.parseBizField(orm, tenantId, me, system, group, fldann, gridOrder++, fieldOrder++, fieldLibs);
						orm.save(field);
						newFields.add(field);
					}
				}
			}
		}
	}

	private EntityDefinition parseSystem(ExtOrm orm, String soft, Class klass, EntityDefinition system, CocEntity sysann, int sysOrder, String code) {
		if (system == null) {
			system = new EntityDefinition();
		}
		system.setName(sysann.name());

		system.setCode(code);
		system.setSortExpr(sysann.dataSortExpr());
		system.setLayout(sysann.layout());
		system.setSerialNumber(sysOrder);
		system.setPageTemplate(sysann.uiTemplate());
		system.setPathPrefix(sysann.pathPrefix());
		system.setMappingClass(klass.getName());
		system.setExtendClass(klass.getSuperclass().getSimpleName());
		system.setBuildin(true);

		EntityCatalog catalog;
		if (!Str.isEmpty(sysann.catalog())) {
			catalog = (EntityCatalog) orm.load(EntityCatalog.class, Expr.eq(F_CODE, sysann.catalog()));
		} else {
			catalog = parseCatalog(orm, soft, klass.getPackage());
		}
		system.setCatalog(catalog);
		system.setTenantOwnerGuid(soft);

		Obj.makeSetupGuid(system, code);

		return system;
	}

	private EntityCatalog parseCatalog(ExtOrm orm, String soft, Package pkg) {
		if (pkg == null)
			return null;
		CocCatalog ann = pkg.getAnnotation(CocCatalog.class);
		if (ann == null)
			return null;

		// 计算code
		String code = ann.GUID();
		String pkgname = pkg.getName();
		int dot = pkgname.lastIndexOf(".");
		String ppkgname = pkgname.substring(0, dot);
		if (Str.isEmpty(code)) {
			code = "_" + pkgname.substring(dot + 1);
		}

		// 计算对象
		EntityCatalog obj = (EntityCatalog) orm.load(EntityCatalog.class, CndExpr.eq(F_CODE, code));
		if (obj == null) {
			obj = new EntityCatalog();
			obj.setName(ann.name());
			obj.setCode(code);
			obj.setSerialNumber(ann.SN());
			obj.setBuildin(true);
			obj.setParent(parseCatalog(orm, soft, Package.getPackage(ppkgname)));
		}

		Obj.makeSetupGuid(obj, code);

		orm.save(obj);

		return obj;
	}

	private EntityColumn parseBizField(ExtOrm orm, String soft, Mirror me, IEntityDefinition system, EntityColumnGroup group, CocColumn fldann, int gridOrder, int fieldOrder, Map fieldLibs) {
		String prop = fldann.propName();

		EntityColumn field = (EntityColumn) orm.load(EntityColumn.class, CndExpr.eq(F_PROP_NAME, prop).and(CndExpr.eq(F_SYSTEM, system)));
		field = (EntityColumn) this.parseBizField(soft, me, prop, system, field);
		field.setDataGroup(group);

		Field f = null;
		try {
			f = me.getField(prop);
		} catch (NoSuchFieldException iglore) {
		}
		if (f == null || f.getAnnotation(CocColumn.class) == null) {
			copyProperties(fldann, field);
			if (!Str.isEmpty(fldann.fkEntity())) {
				field.setRefrenceSystem((EntityDefinition) setupSystemFromClass(soft, getStaticType(fldann.fkEntity()), false));
				field.setType((SystemDataType) fieldLibs.get("System"));
			}
		}
		field.setSerialNumber(fieldOrder);

		if (fldann.gridOrder() > 0)
			field.setGridOrder(fldann.gridOrder());
		else
			field.setGridOrder(gridOrder);

		// 字段类型
		if (field.getType() == null) {
			throw new DemsyException("未知字段类型! [system: %s, field: %s]", me.getType().getSimpleName(), prop);
		}
		field.setBuildin(true);
		Obj.makeSetupGuid(field, system.getCode(), field.getCode());

		return field;
	}

	private EntityColumnGroup parseBizGroup(ExtOrm orm, String soft, IEntityDefinition system, CocGroup grpann, int groupOrder) {
		EntityColumnGroup group = (EntityColumnGroup) orm.load(EntityColumnGroup.class, CndExpr.eq(F_CODE, grpann.GUID()).and(CndExpr.eq(F_SYSTEM, system)));
		if (group == null) {
			group = new EntityColumnGroup();
		}
		group.setSystem((EntityDefinition) system);
		group.setName(grpann.name());
		group.setCode(grpann.GUID());
		group.setSerialNumber(groupOrder);
		group.setTenantOwnerGuid(soft);
		group.setBuildin(true);

		Obj.makeSetupGuid(group, system.getCode(), grpann.GUID());

		return group;
	}

	private EntityAction parseBizAction(ExtOrm orm, String soft, IEntityDefinition system, CocAction actann, int actionOrder) {
		EntityAction action = (EntityAction) orm.load(EntityAction.class, CndExpr.eq(F_TYPE_CODE, actann.type()).and(CndExpr.eq(F_MODE, actann.mode())).and(CndExpr.eq(F_SYSTEM, system.getId())));
		if (action == null) {
			action = new EntityAction();
		}
		action.setSystem(system);
		action.setName(actann.name());
		action.setTypeCode(actann.type());
		action.setMode(actann.mode());
		action.setCode(actann.GUID());
		action.setSerialNumber(actionOrder);
		action.setTargetUrl(actann.targetUrl());
		action.setTargetWindow(actann.targetWindow());
		action.setPlugin(actann.pluginName());
		Class plugin = actann.plugin();
		if (!plugin.equals(void.class)) {
			action.setPlugin(plugin.getName());
		}
		action.setImage(new Upload(actann.image()));
		action.setLogo(new Upload(actann.logo()));
		action.setPageTemplate(actann.pageTemplate());
		action.setInfo(actann.info());
		action.setError(actann.error());
		action.setWarn(actann.warn());
		action.setParams(actann.params());
		action.setDisabled(actann.disabled());

		IEntityAction actionLib = (IEntityAction) orm.load(ActionDefinition.class, CndExpr.eq(F_TYPE_CODE, actann.type()));
		action.setActionLib((ActionDefinition) actionLib);
		action.setTenantOwnerGuid(soft);
		action.setBuildin(true);
		Obj.makeSetupGuid(action, system.getCode(), "" + actann.type(), actann.mode());

		return action;
	}

	private List<EntityAction> parseBizAction(ExtOrm orm, String soft, IEntityDefinition system, String jsonData, int actionOrder) {
		List<EntityAction> actions = JSON.loadFromJson(EntityAction.class, jsonData);
		List<EntityAction> ret = new ArrayList();
		for (EntityAction newAction : actions) {
			this.parseBizAction(ret, orm, soft, system, newAction, null, actionOrder++);
		}

		return ret;
	}

	private void parseBizAction(List<EntityAction> list, ExtOrm orm, String soft, IEntityDefinition system, EntityAction newAction, EntityAction parentAction, int actionOrder) {
		EntityAction action = (EntityAction) orm.load(EntityAction.class, CndExpr.eq(F_TYPE_CODE, newAction.getTypeCode()).and(CndExpr.eq(F_MODE, newAction.getMode())).and(CndExpr.eq(F_SYSTEM, system.getId())));
		if (action == null) {
			action = newAction;
		}
		action.setSystem(system);
		action.setName(newAction.getName());
		action.setTypeCode(newAction.getTypeCode());
		action.setMode(newAction.getMode());
		action.setCode(newAction.getCode());
		action.setSerialNumber(actionOrder++);
		action.setTargetUrl(newAction.getTargetUrl());
		action.setTargetWindow(newAction.getTargetWindow());
		action.setPlugin(newAction.getPlugin());
		action.setImage(newAction.getImage());
		action.setLogo(newAction.getLogo());
		action.setPageTemplate(newAction.getPageTemplate());
		action.setInfo(newAction.getInfo());
		action.setError(newAction.getError());
		action.setWarn(newAction.getWarn());
		action.setDisabled(newAction.isDisabled());
		action.setParentAction(parentAction);

		IEntityAction actionLib = (IEntityAction) orm.load(ActionDefinition.class, CndExpr.eq(F_TYPE_CODE, newAction.getTypeCode()));
		action.setActionLib((ActionDefinition) actionLib);
		action.setTenantOwnerGuid(soft);
		action.setBuildin(true);
		Obj.makeSetupGuid(action, system.getCode(), "" + newAction.getTypeCode(), newAction.getMode());

		orm.save(action);
		list.add(action);

		List<EntityAction> children = newAction.getChildren();
		if (children != null)
			for (EntityAction newchild : children) {
				this.parseBizAction(list, orm, soft, system, newchild, action, actionOrder++);
			}
	}

	public IEntityColumn parseBizField(String soft, Mirror entityObj, String fldProp, IEntityDefinition bzSystem, IEntityColumn oldBzField) {
		Map<String, IFieldDataType> fieldLibs = this.getFieldTypes();

		EntityColumn newBzField = (EntityColumn) oldBzField;
		if (newBzField == null) {
			newBzField = new EntityColumn();
		}
		newBzField.setPropName(fldProp);
		newBzField.setCode(fldProp);
		newBzField.setSystem((EntityDefinition) bzSystem);

		Class type = String.class;
		Class[] genericTypes = null;

		try {
			// 字段注释
			CocColumn annBzFld = null;
			Column annColumn = null;
			ManyToOne manyToOne = null;
			try {
				Field f = entityObj.getField(fldProp);
				if (f != null) {
					type = f.getType();
					annColumn = f.getAnnotation(Column.class);
					annBzFld = f.getAnnotation(CocColumn.class);
					manyToOne = f.getAnnotation(ManyToOne.class);
					genericTypes = Mirror.getGenericTypes(f);
				} else {
					throw new DemsyException("field not existed");
				}
			} catch (Throwable e) {
				String uprop = fldProp.substring(0, 1).toUpperCase() + fldProp.substring(1);
				Method m = null;
				try {
					m = entityObj.findMethod("get" + uprop);
				} catch (Throwable e1) {
					try {
						m = entityObj.findMethod("is" + uprop);
					} catch (Throwable e2) {
					}
				}
				if (m != null) {
					type = m.getReturnType();
					annColumn = m.getAnnotation(Column.class);
					annBzFld = m.getAnnotation(CocColumn.class);
					manyToOne = m.getAnnotation(ManyToOne.class);
				}
			}

			// 字段精度
			if (annColumn != null) {
				int p = annColumn.precision();
				if (p == 0) {
					p = annColumn.length();
				}
				if (p > 0)
					newBzField.setPrecision(p);
				newBzField.setScale(annColumn.scale());
			}

			if (annBzFld != null) {
				copyProperties(annBzFld, newBzField);
			}

			// 解析字段类型
			IFieldDataType fieldLib;
			if (annBzFld != null && !Str.isEmpty(annBzFld.fkEntity())) {
				newBzField.setRefrenceSystem((EntityDefinition) setupSystemFromClass(soft, getStaticType(annBzFld.fkEntity()), false));
				fieldLib = fieldLibs.get("System");
			} else if (manyToOne != null) {
				newBzField.setRefrenceSystem((EntityDefinition) setupSystemFromClass(soft, type, false));
				fieldLib = fieldLibs.get("System");
			} else if (annBzFld != null && !Str.isEmpty(annBzFld.type())) {
				fieldLib = fieldLibs.get(annBzFld.type());
			} else if (SubSystem.class.isAssignableFrom(type)) {// 子系统字段
				if (genericTypes != null && genericTypes.length > 0) {
					newBzField.setRefrenceSystem((EntityDefinition) setupSystemFromClass(soft, genericTypes[0], false));
				}
				newBzField.setRefrenceFields(annBzFld.fkField());
				type = Cls.getObjectType(type);
				fieldLib = fieldLibs.get(type.getSimpleName());
			} else {
				type = Cls.getObjectType(type);
				fieldLib = fieldLibs.get(type.getSimpleName());
			}

			// reference field
			int dot = fldProp.indexOf(".");
			if (dot > 0) {
				String parentProp = fldProp.substring(0, dot);
				EntityColumn parentField = (EntityColumn) orm().load(EntityColumn.class, CndExpr.eq(F_PROP_NAME, parentProp).and(CndExpr.eq(F_SYSTEM, bzSystem)));

				EntityDefinition refSystem = parentField.getRefrenceSystem();
				String refProp = fldProp.substring(dot + 1);
				newBzField.setRefrenceSystem(refSystem);
				newBzField.setRefrenceData((EntityColumn) orm().load(EntityColumn.class, CndExpr.eq(F_PROP_NAME, refProp).and(CndExpr.eq(F_SYSTEM, refSystem))));
			}

			newBzField.setType((SystemDataType) fieldLib);
			newBzField.setTenantOwnerGuid(soft);

			return newBzField;
		} catch (Throwable e) {
			log.error("解析字段出错", e);
			throw new DemsyException("解析字段出错! [%s.%s] %s", entityObj.getType().getSimpleName(), fldProp, e.getMessage());
		}

	}

	private void copyProperties(CocColumn ann, EntityColumn fld) {
		fld.setName(ann.name());
		fld.setGridField(ann.gridField());
		fld.setPassword(ann.isPassword());
		fld.setMode(ann.mode());
		fld.setUiTemplate(ann.uiTemplate());
		fld.setOptions(ann.options());
		fld.setPattern(ann.pattern());
		fld.setRegexpMask(ann.regexpMask());
		fld.setMappingToMaster(ann.isFkChild());
		fld.setDisabled(ann.disabled());
		fld.setDisabledNavi(ann.isDimension());
		fld.setCascadeMode(ann.cascadeMode());
		fld.setGroupBy(ann.groupBy());
		fld.setFileType(ann.uploadType());
		fld.setDefaultValue(ann.defalutValue());
		if (ann.SN() > 0)
			fld.setSerialNumber(ann.SN());
		if (ann.gridOrder() > 0)
			fld.setGridOrder(ann.gridOrder());
		fld.setTransientField(ann.isTransient());
		if (ann.precision() > 0)
			fld.setPrecision(ann.precision());
		// fld.setPrivacy(ann.privacy() ? "1" : "0");
	}

	private void makeFields(IRuntimeField runtimeCustom, List<EntityColumn> fields) {
		if (runtimeCustom == null)
			return;

		makeFields(runtimeCustom.getParent(), fields);

		if (runtimeCustom.getCustomFields() == null)
			return;

		List<EntityColumn> list = (List<EntityColumn>) runtimeCustom.getCustomFields().getList();

		if (list != null) {
			Map<String, EntityColumn> map = new HashMap();
			for (EntityColumn fld : fields) {
				String prop = fld.getPropName();
				if (map.get(prop) == null)
					map.put(prop, fld);
			}
			for (EntityColumn fld : list) {
				String prop = fld.getPropName();
				if (map.get(prop) == null) {
					map.put(prop, fld);
					fields.add(fld);
				}
			}
		}
	}

	public List<? extends IEntityColumn> makeFields(IRuntimeField runtimeCustom) {
		List<EntityColumn> fields = new LinkedList();

		Map<String, IFieldDataType> types = this.getFieldTypes();
		try {
			this.makeFields(runtimeCustom, fields);
		} catch (Throwable e) {
			log.errorf("创建运行时自定义字段出错！", e);
		}

		for (EntityColumn f : fields) {
			if (f.getType() == null) {
				f.setType(types.get("String"));
			}
		}

		return fields;
	}
}
