package com.kmetop.demsy.comlib.impl.sft.system.plugins;

import com.jiongsoft.cocit.entity.ActionEvent;
import com.jiongsoft.cocit.entity.plugin.BasePlugin;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.orm.IOrm;

public abstract class SystemPlugins {

	public static class AddSystem extends BasePlugin {

		@Override
		public void before(ActionEvent event) {

		}

		@Override
		public void after(ActionEvent event) {
			IOrm orm = (IOrm) event.getOrm();
			IBizSystem sys = (IBizSystem) event.getEntity();
			if (sys == null) {
				return;
			}

			try {
				String extendClass = Demsy.bizEngine.getExtendClassName(sys);
				Demsy.bizEngine.parseSystemByAnnotation(Cls.forName(extendClass), sys);
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
			IBizSystem sys = (IBizSystem) event.getEntity();
			if (sys == null) {
				return;
			}

			try {
				String extendClass = Demsy.bizEngine.getExtendClassName(sys);
				Demsy.bizEngine.parseSystemByAnnotation(Cls.forName(extendClass), sys);
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
