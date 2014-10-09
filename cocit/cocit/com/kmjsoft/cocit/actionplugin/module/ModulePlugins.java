package com.kmjsoft.cocit.actionplugin.module;

import com.jiongsoft.cocit.lang.Cls;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.actionplugin.ActionEvent;
import com.kmjsoft.cocit.entity.actionplugin.ActionPlugin;
import com.kmjsoft.cocit.entity.module.IEntityModule;
import com.kmjsoft.cocit.orm.ExtOrm;

public abstract class ModulePlugins {

	public static class AddModule extends ActionPlugin {

		@Override
		public void before(ActionEvent event) {

		}

		@Override
		public void after(ActionEvent event) {
			ExtOrm orm = (ExtOrm) event.getOrm();
			IEntityModule sys = (IEntityModule) event.getEntity();
			if (sys == null) {
				return;
			}

			try {
				String extendClass = Demsy.entityModuleManager.getExtendClassName(sys);
				Demsy.entityModuleManager.parseSystemByAnnotation(Cls.forName(extendClass), sys);
			} catch (ClassNotFoundException e) {
				// throw new DemsyException(e);
			}

			// 将【业务系统】转换成【业务模块】
			Demsy.funMenuManager.makeModule(orm, Demsy.me().getTenant(), sys);
		}

		@Override
		public void loaded(ActionEvent event) {

		}

	}

	public static class AddModules extends ActionPlugin {

		@Override
		public void before(ActionEvent event) {

		}

	}

	public static class EditModule extends ActionPlugin {

		@Override
		public void before(ActionEvent event) {
		}

		@Override
		public void after(ActionEvent event) {
			IEntityModule sys = (IEntityModule) event.getEntity();
			if (sys == null) {
				return;
			}

			try {
				String extendClass = Demsy.entityModuleManager.getExtendClassName(sys);
				Demsy.entityModuleManager.parseSystemByAnnotation(Cls.forName(extendClass), sys);
			} catch (ClassNotFoundException e) {
				// throw new DemsyException(e);
			}
		}

		@Override
		public void loaded(ActionEvent event) {
			// TODO Auto-generated method stub

		}

	}
}
