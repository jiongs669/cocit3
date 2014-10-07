package com.kmjsoft.cocit.entityengine.definition.impl;

import static com.jiongsoft.cocit.Demsy.appconfig;
import static com.jiongsoft.cocit.Demsy.entityDefManager;
import static com.kmjsoft.cocit.entity.EntityConst.F_CODE;
import static com.kmjsoft.cocit.entity.EntityConst.F_SOFT_ID;
import static com.kmjsoft.cocit.entity.EntityConst.MODULE_OTHER;

import java.util.List;

import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.jiongsoft.cocit.Demsy;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.lang.Obj;
import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.orm.IOrm;
import com.jiongsoft.cocit.orm.nutz.IExtDao;
import com.jiongsoft.cocit.orm.nutz.impl.OrmImpl;
import com.kmjsoft.cocit.entity.IDataEntity;
import com.kmjsoft.cocit.entity.definition.IEntityCatalog;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.entity.impl.entitydef.BizCatalog;
import com.kmjsoft.cocit.entity.impl.security.Module;
import com.kmjsoft.cocit.entity.impl.security.SoftEnv;
import com.kmjsoft.cocit.entity.impl.security.Tenant;
import com.kmjsoft.cocit.entity.security.IModule;
import com.kmjsoft.cocit.entity.security.ISystemTenant;
import com.kmjsoft.cocit.orm.expr.CndExpr;

public class SFTModuleEngine extends ModuleEngine {

	public SFTModuleEngine() {
		init();
	}

	private void init() {
	}

	@Override
	public synchronized void upgradeModules(final ISystemTenant s) {
		Trans.exec(new Atom() {
			public void run() {
				// int size;
				// OrmImpl orm = (OrmImpl) Demsy.orm();
				// DemsySoft soft = (DemsySoft) s;
				// if (soft == null) {
				// throw new ConfigException("请先选择要升级到哪个应用软件!");
				// }

				// log.info("升级功能模块......");
				// size = upgradeBizModule(orm, soft);
				// log.infof("升级功能模块: 结束. [size=%s]", size);

				// upgradeUserLogin(orm, soft);
			}
		});
	}

	@Override
	public synchronized void upgradeWebContent(final ISystemTenant s) {
		Trans.exec(new Atom() {
			public void run() {
				// int size;
				// OrmImpl orm = (OrmImpl) Demsy.orm();
				// DemsySoft soft = (DemsySoft) s;
				// if (soft == null) {
				// throw new ConfigException("请先选择要升级到哪个应用软件!");
				// }

				// log.info("升级网站栏目信息......");
				// size = upgradeWebContent(orm, soft);
				// log.infof("升级网站栏目信息: 结束. [size=%s]", size);
				//
				// log.info("升级网站栏目......");
				// size = upgradeWebCatalog(orm, soft);
				// log.infof("升级网站栏目: 结束. [size=%s]", size);
			}
		});
	}

	public synchronized void setupDemsy() {
		Trans.exec(new Atom() {
			public void run() {
				int size;
				OrmImpl orm = (OrmImpl) Demsy.orm();

				log.info("安装平台默认应用软件......");
				Tenant soft = setupDefaultSoft(orm);
				log.info("安装平台默认应用软件: 结束.");

				log.info("安装组件库到软件中......");
				entityDefManager.setupFromPackage(soft);
				entityDefManager.setupFromPackage(soft);
				log.infof("安装组件库 结束.");

				log.info("安装业务模块......");
				size = setupDemsyModules(orm, soft);
				log.infof("安装业务模块: 结束. [size=%s]", size);

				// log.info("安装静态模块......");
				// size = setupStaticModules(orm, soft);
				// log.infof("安装静态模块: 结束. [size=%s]", size);

				// removeSetupData(orm, false);
				// removeSetupData(orm, true);
			}
		});
	}

	// private void removeSetupData(IOrm orm, boolean warn) {
	// long time = Demsy.swStart();
	// List<Class<?>> types = bizEngine.listTypes();
	// for (Class klass : types) {
	// try {
	// if (Mirror.me(klass).getField(F_UPDATED) == null) {
	// continue;
	// }
	// } catch (NoSuchFieldException e1) {
	// continue;
	// }
	// CocTable sys = (CocTable) klass.getAnnotation(CocTable.class);
	// if (sys == null || sys.id() > 0) {
	// continue;
	// }
	// int page = 0;
	// while (true) {
	// page++;
	// Pager pager = new Pager(klass);
	// CndExpr expr = Expr.le(F_UPDATED, new Date(time));
	// expr.setPager(page, 20);
	// pager.setQueryExpr(expr);
	// List list = orm.query(pager);
	// if (list == null || list.size() == 0) {
	// break;
	// }
	// for (Object obj : list) {
	// Boolean isBuildin = Mirrors.getValue(obj, F_BUILDIN);
	// String guid = Mirrors.getValue(obj, F_GUID);
	// if ((isBuildin != null && isBuildin && Str.isEmpty(guid)) //
	// || (guid != null &&
	// guid.startsWith(EnObj.getType(obj.getClass()).getSimpleName().toUpperCase()
	// + "_"))) {
	// try {
	// orm.delete(obj);
	// log.warnf("安装平台时删除数据成功! %s", ObjectUtil.toJson(obj));
	// } catch (Throwable e) {
	// if (warn)
	// log.warnf("安装平台时删除数据失败! [%s] %s", ObjectUtil.toJson(obj), e);
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// private int evalActionType(CommonOperation commonOP) {
	// int typeCode = 0;
	// String method = null;
	// List<OperationScript> scripts = commonOP.getScripts();
	// for (OperationScript script : scripts) {
	// method = script.getScript();
	// if (method.trim().startsWith("SystemUtil."))
	// break;
	// }
	// if (!Strings.isEmpty(method)) {
	// if (method.indexOf(".updateGrid(") > 0) {
	// typeCode = BizConst.TYPE_BZFORM_EDIT;
	// } else if (method.indexOf(".insertGrid(") > 0) {
	// typeCode = BizConst.TYPE_BZFORM_NEW;
	// } else if (method.indexOf(".updateBatch(") > 0) {
	// typeCode = BizConst.TYPE_BZFORM_EDIT_N;
	// } else if (method.indexOf(".updateBatchImmediately(") > 0) {
	// typeCode = BizConst.TYPE_BZ_EXEC_SYNC;
	// } else if (method.indexOf(".executeBusiness(system,'msg_sms')") > 0) {
	// typeCode = BizConst.TYPE_BZFORM_EXEC_ASYN;
	// } else if (method.indexOf(".executeBusiness(system,'msg_online')") > 0) {
	// typeCode = BizConst.TYPE_BZFORM_EXEC_ASYN;
	// } else if (method.indexOf(".updateBackground(") > 0) {
	// typeCode = BizConst.TYPE_BZ_EXEC_ASYN;
	// } else if (method.indexOf(".customize(") > 0) {
	// } else if (method.indexOf(".deleteGrid(") > 0) {
	// typeCode = BizConst.TYPE_BZ_DEL;
	// } else if (method.indexOf(".deleteGridAll(") > 0) {
	// typeCode = BizConst.TYPE_BZ_DEL;
	// } else if (method.indexOf("SystemUtil.updateGridImmediately(system)") > -1) {
	// typeCode = BizConst.TYPE_BZ_SAVE;
	// // } else if (method.indexOf(".orderBy(system,false)") > 0)
	// // {
	// // disabled = true;
	// // } else if (method.indexOf(".orderBy(system,true)") > 0) {
	// // disabled = true;
	// // } else if (method.indexOf(".reverse(system)") > 0) {
	// // disabled = true;
	// // } else if (method.indexOf(".exportRtf(") > 0) {
	// // disabled = true;
	// // } else if (method.indexOf(".exportXls(") > 0) {
	// // disabled = true;
	// // } else if (method.indexOf(".exportPdf(") > 0) {
	// // disabled = true;
	// // } else if (method.indexOf(".exportHtml(") > 0) {
	// // disabled = true;
	// // } else if (method.indexOf(".importXls(") > -1) {
	// // disabled = true;
	// // } else if
	// // (method.indexOf("WorkflowUtil.restartWorkflow(workflow)")
	// // > -1) {
	// // disabled = true;
	// // } else if
	// // (method.indexOf("WorkflowUtil.suspendWorkflow(workflow)")
	// // > -1) {
	// // disabled = true;
	// // } else if
	// // (method.indexOf("WorkflowUtil.killWorkflow(workflow)") >
	// // -1) {
	// // disabled = true;
	// // } else if
	// // (method.indexOf("WorkflowUtil.doAction(workflow)") > -1)
	// // {
	// // disabled = true;
	// // } else if
	// // (method.indexOf("WorkflowUtil.assignStep(workflow)") >
	// // -1) {
	// // disabled = true;
	// // } else if
	// // (method.indexOf("WorkflowUtil.updateDrawAction(workflow)")
	// // > -1) {
	// // disabled = true;
	// // } else if
	// // (method.indexOf("WorkflowUtil.updateCancelDrawAction(workflow)")
	// // > -1) {
	// // disabled = true;
	// } else {
	// log.debugf("忽略公共操作 [%s: %s]", commonOP.getName(), method);
	// }
	// }
	//
	// return typeCode;
	// }
	//
	// private void upgradeBizAction(IOrm orm, Resource resource) {
	// if (resource == null || !(resource instanceof SystemResource)) {
	// return;
	// }
	//
	// SFTSystem system = ((SystemResource) resource).getSystem();
	// if (system == null)
	// return;
	//
	// CndExpr expr = Expr.eq(F_RESOURCE, resource);
	//
	// List<ResourceOperation> oldOpList = orm.query(ResourceOperation.class, expr);
	// for (ResourceOperation oldOp : oldOpList) {
	// int typeCode = this.evalActionType(oldOp.getCommon());
	//
	// ActionLib actionLib = (ActionLib) orm.load(ActionLib.class, CndExpr.eq(F_TYPE_CODE, typeCode));
	//
	// BizAction action = (BizAction) orm.load(BizAction.class, CndExpr.eq(F_UPGRADE_FROM, oldOp.getId()));
	// if (action == null) {
	// action = new BizAction();
	// }
	// action.setName(oldOp.getName());
	// action.setDesc(oldOp.getDesc());
	// action.setCode(oldOp.getCode());
	// action.setEntityGuid(oldOp.getEntityGuid());
	// action.setMode(oldOp.getMode());
	// action.setLogo(new Upload(oldOp.getEnableImageName()));
	// action.setUpgradeFrom(oldOp.getId());
	// action.setOrderby(oldOp.getOrderby());
	// action.setSystem(system);
	// action.setActionLib(actionLib);
	// if (actionLib != null) {
	// action.setTypeCode(actionLib.getTypeCode());
	// action.setDisabled(oldOp.isDisabled() || actionLib.isDisabled());
	// } else {
	// action.setTypeCode(0);
	// action.setDisabled(true);
	// }
	//
	// orm.save(action);
	// log.debugf("升级业务系统操作 [%s: %s]", system.getName(), oldOp.getName());
	//
	// }
	// }

	// private int clearModule(IOrm orm, DemsyCocSoft soft) {
	// int ret = 0;
	//
	// List<Module> modules = orm.query(Module.class, Expr.eq(F_SOFT_ID, soft));
	// for (Module moduleID : modules) {
	// ret += clearModule(orm, moduleID);
	// }
	//
	// return ret;
	// }

	// private int clearModule(IOrm orm, Module parent) {
	// int ret = 0;
	// parent = getEntity(Module.class, parent.getId());
	//
	// if (parent == null) {
	// return 0;
	// }
	//
	// List<Module> children = parent.getChildren();
	// if (children != null) {
	// for (Module child : children) {
	// ret += clearModule(orm, child);
	// }
	// }
	//
	// if (parent.getType() == IModule.TYPE_FOLDER && orm.count(Module.class,
	// Expr.eq(F_PARENT, parent)) == 0) {
	// orm.delete(parent);
	//
	// ret++;
	// }
	//
	// return ret;
	// }
	//
	// private Module convert(IOrm orm, DemsySoft soft, Resource resource) {
	// if (resource == null)
	// return null;
	//
	// int type = 0;
	//
	// SFTSystem system = null;
	// if (resource instanceof SystemResource) {
	// type = IModule.TYPE_BIZ;
	// system = ((SystemResource) resource).getSystem();
	// system.setSoftID(soft.getId());
	// orm.save(system, Expr.fieldRexpr(F_SOFT_ID, false));
	// } else if (resource instanceof SFTWorkflowResource) {
	// return null;
	// // } else if (resource instanceof WebContentCategoryResource) {
	// // return null;
	// } else if (resource instanceof SFTReportResource) {
	// return null;
	// } else if (resource instanceof HypoResource) {
	// return null;
	// } else if (Strings.isEmpty(resource.getPath())) {
	// type = IModule.TYPE_FOLDER;
	// } else {
	// log.debugf("忽略其他模块 [%s]", resource.getName());
	// return null;
	// }
	//
	// Module module = (Module) orm.load(Module.class, CndExpr.eq(F_UPGRADE_FROM, resource.getId()));
	// if (module == null) {
	// module = new Module();
	// }
	// module.setName(resource.getName());
	// module.setCode(resource.getCode());
	// module.setEntityGuid(resource.getEntityGuid());
	// module.setType(type);
	// module.setRefSystem(system);
	// module.setPathPrefix(system.getPathPrefix());
	// module.setUpgradeFrom(resource.getId());
	// Menu menu = (Menu) orm.load(Menu.class, CndExpr.eq(F_RESOURCE, resource));
	// module.setOrderby(menu != null ? menu.getOrderby() : resource.getOrderby());
	// module.setDisabled(resource.isDisabled());
	// module.setHidden(resource.isHideMenu());
	// module.setSoftID(soft.getId());
	//
	// orm.save(module);
	//
	// return module;
	// }
	//
	// @SuppressWarnings("unused")
	// private int upgradeBizModule(IOrm orm, DemsySoft soft) {
	// int ret = 0;
	//
	// List<SFTSystem> systems = orm.query(SFTSystem.class);
	// for (SFTSystem sys : systems) {
	// Resource oldBizModule = sys.getResource();
	// if (oldBizModule == null) {
	// continue;
	// }
	//
	// // 升级业务操作
	// this.upgradeBizAction(orm, oldBizModule);
	//
	// // 获取文件夹
	// Resource oldFolder = null;
	// Menu menu = (Menu) orm.load(Menu.class, CndExpr.eq(F_RESOURCE, oldBizModule));
	// if (menu != null) {
	// oldFolder = menu.getParentResource();
	// while (oldFolder instanceof SystemResource) {
	// menu = (Menu) orm.load(Menu.class, CndExpr.eq(F_RESOURCE, oldFolder));
	// if (menu == null)
	// break;
	//
	// oldFolder = menu.getParentResource();
	// }
	// }
	//
	// // 对业务系统进行归类
	// if (oldFolder != null) {
	// Resource tmpFolder = oldFolder;
	// Module rootModule = null;
	// while (true) {
	// Menu tmpMenu = (Menu) orm.load(Menu.class, CndExpr.eq(F_RESOURCE, tmpFolder));
	// if (tmpMenu == null) {
	// break;
	// }
	// Module tmpNewParentFolder = this.convert(orm, soft, tmpMenu.getParentResource());
	// Module tmpNewFolder = this.convert(orm, soft, tmpFolder);
	// if (tmpNewParentFolder != null) {
	// tmpNewFolder.setParent(tmpNewParentFolder);
	// orm.save(tmpNewFolder, Expr.fieldRexpr("parent", false));
	// rootModule = tmpNewParentFolder;
	// }
	//
	// tmpFolder = tmpMenu.getParentResource();
	// if (tmpFolder == null)
	// break;
	// }
	//
	// Module parentModule = this.convert(orm, soft, oldFolder);
	// if (rootModule == null) {
	// rootModule = parentModule;
	// }
	//
	// BizCatalog catalog = convert(orm, soft, rootModule);
	// sys.setCatalog(catalog);
	// orm.save(sys, Expr.fieldRexpr(F_CATALOG, false));
	//
	// Module module = this.convert(orm, soft, oldBizModule);
	// module.setParent(parentModule);
	// orm.save(module, Expr.fieldRexpr(F_PARENT, false));
	//
	// ret++;
	// }
	//
	// }
	//
	// return ret;
	// }

	private Tenant setupDefaultSoft(OrmImpl orm) {
		IExtDao dao = orm.getDao();

		SoftEnv user = (SoftEnv) getCorpByDefault();
		if (user == null) {
			user = new SoftEnv();
		}
		user.setName(appconfig.getDefaultCorpName());
		user.setCode(appconfig.getDefaultCorpCode());
		Obj.makeSetupGuid(user, appconfig.getDefaultCorpCode());
		dao.save(user, null, false);

		Tenant soft = (Tenant) getSoftByDefault();
		if (soft == null) {
			soft = new Tenant();
		}
		soft.setName(appconfig.getDefaultSoftName());
		soft.setUsername(appconfig.getDefaultSoftCode());
		soft.setRawPassword(appconfig.getDefaultSoftCode());
		soft.setRawPassword2(appconfig.getDefaultSoftCode());
		soft.setBuildin(true);
		soft.setCorp(user);
		Obj.makeSetupGuid(soft, appconfig.getDefaultSoftCode());
		dao.save(soft);

		return soft;
	}

	// private BizCatalog convert(IOrm orm, DemsySoft soft, Module module) {
	// if (module == null) {
	// return null;
	// }
	// if (module.getType() != IModule.TYPE_FOLDER) {
	// return null;
	// }
	// module = (Module) orm.load(Module.class, module.getId());
	//
	// BizCatalog catalog = (BizCatalog) orm.load(BizCatalog.class, CndExpr.eq(F_CODE, "" + module.getId()));
	// if (catalog == null) {
	// catalog = new BizCatalog();
	// }
	// catalog.setName(module.getName());
	// catalog.setCode("" + module.getId());
	// catalog.setOrderby(module.getOrderby());
	// catalog.setBuildin(module.isBuildin());
	// catalog.setDisabled(module.isDisabled());
	// catalog.setParent(convert(orm, soft, module.getParent()));
	// catalog.setSoftID(soft.getId());
	// Obj.makeSetupGuid(catalog, catalog.getCode());
	//
	// orm.save(catalog);
	//
	// return catalog;
	// }

	// 用于安装平台时转换业务类为模块
	private IModule convert(IOrm orm, ISystemTenant soft, IEntityCatalog icatalog) {
		BizCatalog catalog = (BizCatalog) icatalog;
		if (catalog == null) {
			return null;
		}

		catalog = (BizCatalog) orm.load(BizCatalog.class, catalog.getId());
		if (catalog == null)
			return null;

		Module module = (Module) orm.load(Module.class, CndExpr.eq(F_CODE, catalog.getCode()));
		if (module == null) {
			module = new Module();
		}

		module.setName(catalog.getName());
		module.setType(IModule.TYPE_FOLDER);
		module.setParent((Module) convert(orm, soft, catalog.getParent()));
		module.setTenantGuid(soft.getDataGuid());
		module.setSerialNumber(catalog.getSerialNumber());
		module.setCode(catalog.getCode());
		module.setBuildin(true);

		Obj.makeSetupGuid(module, module.getCode());

		orm.save(module);

		return module;
	}

	/**
	 * 供插件调用，用于自动生成模块文件夹
	 */
	public IModule makeModule(IOrm orm, ISystemTenant soft, IEntityCatalog catalog) {
		if (catalog == null || (Str.isEmpty(catalog.getDataGuid()) && Str.isEmpty(catalog.getCode())))
			return null;

		Module module = (Module) orm.load(Module.class, CndExpr.eq(F_CODE, catalog.getDataGuid()));
		if (module == null && !Str.isEmpty(catalog.getCode())) {
			module = (Module) orm.load(Module.class, CndExpr.eq(F_CODE, catalog.getCode()));
		}
		if (module == null) {
			module = new Module();
			module.setName(catalog.getName());
			module.setCode(catalog.getDataGuid());
			module.setType(IModule.TYPE_FOLDER);
			module.setParent((Module) makeModule(orm, soft, (IEntityCatalog) catalog.getParent()));
			module.setTenantGuid(soft.getDataGuid());

			orm.save(module);
		}

		return module;
	}

	/**
	 * 供插件调用，用于自动生成模块文件夹
	 */
	public IModule makeModule(IOrm orm, ISystemTenant soft, IEntityDefinition system) {
		if (system == null)
			return null;

		Module module = (Module) orm.load(Module.class, CndExpr.eq(F_CODE, system.getDataGuid()));
		if (module == null) {
			module = new Module();
			module.setName(system.getName());
			module.setCode(system.getDataGuid());
			module.setType(IModule.TYPE_BIZ);
			module.setParent((Module) this.makeModule(orm, soft, system.getCatalog()));
			module.setRefSystem(system);
			module.setPathPrefix(system.getPathPrefix());
			module.setHidden(entityDefManager.isSlave(system));
			module.setTenantGuid(soft.getDataGuid());

			orm.save(module);
		}

		return module;
	}

	//
	// private int setupStaticModules(IOrm orm, DemsyCocSoft soft) {
	// int ret = 0;
	//
	// BizSystemAnn sysann = (BizSystemAnn)
	// Module.class.getAnnotation(BizSystemAnn.class);
	// if (sysann == null || Strings.isEmpty(sysann.comlibData())) {
	// return 0;
	// }
	//
	// List<Module> list = ((SFTBizEngine) bizEngine).setupComponents(orm, soft,
	// Module[].class, Module.class, F_CODE, sysann);
	// ret += list.size();
	//
	// return ret;
	// }

	private int setupDemsyModules(IOrm orm, Tenant soft) {
		int ret = 0;

		String moduleFolder = MODULE_OTHER;
		Module otherModule = (Module) orm.load(Module.class, CndExpr.eq(F_CODE, moduleFolder).and(CndExpr.eq(F_SOFT_ID, soft)));

		List<IEntityDefinition> systems;
		try {
			systems = entityDefManager.setupSystemFromPackage(soft);
		} catch (DemsyException e) {
			throw new RuntimeException(e);
		}
		for (IEntityDefinition sys : systems) {
			String code = sys.getCode();

			Module module = (Module) orm.load(Module.class, CndExpr.eq(F_CODE, code));
			if (module == null) {
				module = new Module();
			}
			module.setName(sys.getName());
			module.setSerialNumber(sys.getSerialNumber());
			module.setCode(code);
			module.setType(IModule.TYPE_BIZ);
			module.setRefSystem(sys);
			module.setPathPrefix(sys.getPathPrefix());
			module.setHidden(entityDefManager.isSlave(sys));
			module.setStatusCode(IDataEntity.STATUS_CODE_BUILDIN);
			module.setTenantGuid(soft.getDataGuid());

			Module parentModule = (Module) convert(orm, soft, (BizCatalog) sys.getCatalog());
			if (parentModule == null) {
				if (otherModule == null) {
					otherModule = new Module();
					otherModule.setName("==其他模块==");
					otherModule.setCode(moduleFolder);
					otherModule.setType(IModule.TYPE_FOLDER);
					otherModule.setSerialNumber(9999);
					otherModule.setBuildin(true);
					otherModule.setTenantGuid(soft.getDataGuid());
					otherModule.setParent(null);
					Obj.makeSetupGuid(otherModule, moduleFolder);
					orm.save(otherModule);
				}
			}
			module.setParent(parentModule == null ? otherModule : parentModule);
			Obj.makeSetupGuid(module, code);

			orm.save(module);
			ret++;
		}

		return ret;
	}

	// private int upgradeWebContent(IOrm orm, DemsySoft soft) {
	// int ret = 0;
	//
	// int pageIndex = 0;
	// while (true) {
	// pageIndex++;
	//
	// Pager pager = new Pager(WebContent.class);
	// pager.setQueryExpr(Expr.page(pageIndex, 20));
	//
	// List<WebContent> list = orm.query(pager);
	// if (list == null || list.size() == 0)
	// break;
	//
	// for (WebContent ele : list) {
	// Dic type = ele.getType();
	// if (type == null) {
	// continue;
	// }
	//
	// String code = Strings.isEmpty(type.getCode()) ? "0" : type.getCode();
	// int typeCode = Integer.parseInt(code);
	//
	// // 信息推荐
	// String[] ids = Str.toArray(ele.getPublishToStr(), ",;");
	// for (String id : ids) {
	// WebContentCategory catalog = (WebContentCategory) orm.load(WebContentCategory.class, Long.parseLong(id));
	// List<WebContent> infos = orm.query(WebContent.class, Expr.eq(F_CATALOG, catalog).and(Expr.eq(F_TYPE_CODE, IWebContent.TYPE_REFER)).and(Expr.eq("refrence", ele)));
	// if (infos.size() == 0) {
	// WebContent info = new WebContent();
	// info.setCatalog(catalog);
	// info.setTypeCode(IWebContent.TYPE_REFER);
	// info.setRefrence(ele);
	// info.setName(ele.getName());
	// info.setDate(ele.getCreated());
	// info.setOrderby(ele.getOrderby());
	// info.setUpdated(ele.getCreated());
	//
	// log.tracef("升级信息推荐 [info=%s, catalog=%s]", info, catalog);
	// orm.save(info);
	// } else if (infos.size() == 1) {
	// WebContent info = infos.get(0);
	// info.setDate(ele.getUpdated());
	// info.setUpdated(ele.getCreated());
	// info.setOrderby(ele.getOrderby());
	//
	// log.tracef("升级信息推荐 [info=%s, catalog=%s]", info, catalog);
	// orm.save(info, Expr.fieldRexpr("date$|updated$|orderby$", false));
	// } else {
	// for (int i = infos.size() - 1; i >= 1; i--) {
	// WebContent info = infos.get(i);
	//
	// log.tracef("删除重复信息推荐 [info=%s, catalog=%s]", info, catalog);
	// orm.delete(info);
	// }
	// }
	// }
	//
	// ele.setTypeCode(typeCode);
	// ele.setUpdated(ele.getCreated());
	// ele.setDate(ele.getUpdated());
	// ele.setSoftID(soft.getId());
	// // 访问统计
	// WebContentVisit visit = (WebContentVisit) orm.load(WebContentVisit.class, Expr.eq("content", ele));
	// if (visit != null) {
	// ele.setClickNum(Integer.parseInt("" + visit.getCount()));
	// }
	// int num = orm.count(Comment.class, Expr.eq("webContent", ele));
	// ele.setCommentNum(num);
	//
	// orm.save(ele, Expr.fieldRexpr("typeCode$|date$|updated$|softID$|clickNum$|commentNum$", false));
	// ret++;
	// }
	// }
	//
	// return ret;
	// }
	//
	// private int upgradeWebCatalog(IOrm orm, DemsySoft soft) {
	// int ret = 0;
	//
	// int pageIndex = 0;
	// while (true) {
	// pageIndex++;
	//
	// Pager pager = new Pager(WebContentCategory.class);
	// pager.setQueryExpr(Expr.page(pageIndex, 20));
	//
	// List<WebContentCategory> list = orm.query(pager);
	// if (list == null || list.size() == 0)
	// break;
	//
	// for (WebContentCategory ele : list) {
	//
	// // 升级栏目类型
	// int type = IWebContentCatalog.TYPE_FOLDER;
	// if (orm.count(WebContentCategory.class, Expr.eq(F_PARENT, ele)) > 0) {
	// type = IWebContentCatalog.TYPE_FOLDER;
	// } else if (ele.getRefrence() != null) {
	// type = IWebContentCatalog.TYPE_REF;
	// } else {
	// type = IWebContentCatalog.TYPE_INFO;
	// }
	//
	// // 是否支持检索
	// // boolean searchhidden = false;
	// // WebContentCategory tmp = ele;
	// // while (tmp != null) {
	// // if (tmp.isInfoDisabledSearch()) {
	// // searchhidden = true;
	// // break;
	// // }
	// // tmp = tmp.getParent();
	// // }
	//
	// // ele.setInfoDisabledSearch(searchhidden);
	// ele.setType(type);
	// ele.setSoftID(soft.getId());
	// log.tracef("升级栏目检索 [catalog=%s, type=%s]", ele, type);
	//
	// orm.save(ele, Expr.fieldRexpr("infoDesiabledSearch$|type$|softID$", false));
	// ret++;
	// }
	// }
	//
	// return ret;
	// }
	//
	// private void upgradeUserLogin(IOrm orm, IDemsySoft soft) {
	// IBizSystem sys = bizEngine.getSystem("demsy_web_member");
	// Class type = bizEngine.getType(sys);
	// if (!BaseUser.class.isAssignableFrom(type)) {
	// return;
	// }
	//
	// int pageIndex = 0;
	// while (true) {
	// pageIndex++;
	//
	// Pager pager = new Pager(type);
	// pager.setQueryExpr(Expr.page(pageIndex, 200));
	//
	// List<BaseUser> list = orm.query(pager);
	// if (list == null || list.size() == 0)
	// break;
	//
	// for (BaseUser newUser : list) {
	// String username = newUser.getUsername();
	// try {
	// UserLogin user = (UserLogin) orm.load(UserLogin.class, Expr.eq("code", username));
	//
	// if (user != null) {
	// newUser.setCode(user.getCode());
	// newUser.setName(user.getName());
	// newUser.setPassword(user.getPassword());
	// newUser.setPwdEncoder(user.getPwdEncoder());
	// newUser.setPwdQuestion(user.getPwdQuestion());
	// newUser.setPwdAnswer(user.getPwdAnswer());
	// newUser.setLogo(user.getLogo());
	// newUser.setImage(user.getImage());
	// String img = Obj.getValue(newUser, "head_portrait");
	// if (!Str.isEmpty(img)) {
	// if (newUser.getLogo() == null || Str.isEmpty(newUser.getLogo().toString())) {
	// newUser.setLogo(new Upload(img));
	// }
	// if (newUser.getImage() == null || Str.isEmpty(newUser.getImage().toString())) {
	// newUser.setImage(new Upload(img));
	// }
	// }
	// newUser.setSoftID(soft.getId());
	//
	// orm.save(newUser, Expr.fieldRexpr("code$|name$|password$|pwdEncoder$|pwdAnswer$|pwdQuestion$|logo$|image$|softID$", true));
	// }
	// } catch (Throwable e) {
	// log.errorf("升级会员信息出错! [%s] %s", username, e);
	// }
	// }
	// }
	// }

}
