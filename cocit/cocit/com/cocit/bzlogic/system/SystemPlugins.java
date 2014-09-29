package com.cocit.bzlogic.system;

import com.cocit.Demsy;
import com.cocit.api.entitydef.IEntityDefinition;
import com.cocit.lang.Cls;
import com.cocit.orm.IOrm;
import com.jiongsoft.cocit.entity.ActionEvent;
import com.jiongsoft.cocit.entity.plugin.BasePlugin;

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
				String extendClass = Demsy.entityDefEngine.getExtendClassName(sys);
				Demsy.entityDefEngine.parseSystemByAnnotation(Cls.forName(extendClass), sys);
			} catch (ClassNotFoundException e) {
				// throw new DemsyException(e);
			}

			// 将【业务系统】转换成【业务模块】
			Demsy.moduleEngine.makeModule(orm, Demsy.me().getSoft(), sys);
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
				String extendClass = Demsy.entityDefEngine.getExtendClassName(sys);
				Demsy.entityDefEngine.parseSystemByAnnotation(Cls.forName(extendClass), sys);
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
