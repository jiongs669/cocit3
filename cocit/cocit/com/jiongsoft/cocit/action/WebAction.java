package com.jiongsoft.cocit.action;

import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.mvc.adaptor.EntityParamAdaptor;
import com.jiongsoft.cocit.mvc.adaptor.EntityParamNode;
import com.jiongsoft.cocit.ui.UIModelView;
import com.jiongsoft.cocit.ui.model.JSPModel;
import com.jiongsoft.cocit.util.UrlAPI;
import com.jiongsoft.cocit.util.Log;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
@AdaptBy(type = EntityParamAdaptor.class)
public class WebAction {

	/**
	 * 
	 * @param jspArgs
	 *            JSP路径参数：子目录用冒号分隔，如：visit:index 表示要访问/visit/index.jsp页面。
	 * @param opArgs
	 *            操作参数：表示需要通过JSP页面动态访问指定的模块操作，参数格式为：“moduleID:tableID:operationID”。
	 * @param entityID
	 *            实体ID：表示JSP页面支持动态数据。
	 * @param entityParamNode
	 *            实体参数节点：用来接收HTTP中以entity.开头的参数，这些参数将被注入到实体对象中，继续传递到指定的页面。
	 * @return JSPModel 对象
	 */
	@At(UrlAPI.GET_JSP_MODEL)
	public JSPModel getJspModel(String jspArgs, String opArgs, String entityID, @Param("::entity.") EntityParamNode entityParamNode) {
		Log.debug("WebAction.getJspModel... jspArgs=%s, opArgs=%s, entityID=%s", jspArgs, opArgs, entityID);

		ActionContext actionContext = Cocit.getActionContext();
		ActionHelper actionHelper = ActionHelper.make(opArgs, entityID, entityParamNode);

		String softContextPath = Cocit.getContextPath() + "/" + actionHelper.softService.getCode().replace('.', '_');
		String jspPath = UrlAPI.makeJspPath(jspArgs);

		JSPModel model = JSPModel.make(actionContext.getRequest(), actionContext.getResponse(), softContextPath, jspPath);

		model.set("actionHelper", actionHelper);

		Log.debug("WebAction.getJspModel... model=%s", model);

		return model;
	}
}
