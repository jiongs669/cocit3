package com.kmjsoft.cocit.bizplugin.system;

import com.jiongsoft.cocit.Demsy;
import com.jiongsoft.cocit.lang.Cls;
import com.jiongsoft.cocit.orm.IOrm;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.entityengine.bizplugin.ActionEvent;
import com.kmjsoft.cocit.entityengine.bizplugin.BasePlugin;

public abstract class SystemPlugins {

	public static class AddSystem extends BasePlugin {

		@Override
		public void before(ActionEvent event) {

		}

		@Override
		public void after(ActionEvent event) {
			IOrm orm = (IOrm) event.getOrm();
			IEntityDefinition sys = (IEntityDefinition) event.getEntity();
			if (sys == null) {
				return;
			}

			try {
				String extendClass = Demsy.entityDefManager.getExtendClassName(sys);
				Demsy.entityDefManager.parseSystemByAnnotation(Cls.forName(extendClass), sys);
			} catch (ClassNotFoundException e) {
				// throw new DemsyException(e);
			}

			// 将【业务系统】转换成【业务模块】
			Demsy.moduleManager.makeModule(orm, Demsy.me().getSoft(), sys);
		}

		@Override
		public void loaded(ActionEvent event) {

		}

	}

	public static class AddSystems extends BasePlugin {

		@Override
		public void before(ActionEvent event) {

		}

	}

	public static class EditSystem extends BasePlugin {

		@Override
		public void before(ActionEvent event) {
		}

		@Override
		public void after(ActionEvent event) {
			IEntityDefinition sys = (IEntityDefinition) event.getEntity();
			if (sys == null) {
				return;
			}

			try {
				String extendClass = Demsy.entityDefManager.getExtendClassName(sys);
				Demsy.entityDefManager.parseSystemByAnnotation(Cls.forName(extendClass), sys);
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
