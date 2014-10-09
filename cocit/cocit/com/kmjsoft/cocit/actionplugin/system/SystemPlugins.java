package com.kmjsoft.cocit.actionplugin.system;

import com.jiongsoft.cocit.lang.Cls;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.actionplugin.ActionEvent;
import com.kmjsoft.cocit.entity.actionplugin.ActionPlugin;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.orm.ExtOrm;

public abstract class SystemPlugins {

	public static class AddSystem extends ActionPlugin {

		@Override
		public void before(ActionEvent event) {

		}

		@Override
		public void after(ActionEvent event) {
			ExtOrm orm = (ExtOrm) event.getOrm();
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
			Demsy.moduleManager.makeModule(orm, Demsy.me().getTenant(), sys);
		}

		@Override
		public void loaded(ActionEvent event) {

		}

	}

	public static class AddSystems extends ActionPlugin {

		@Override
		public void before(ActionEvent event) {

		}

	}

	public static class EditSystem extends ActionPlugin {

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
