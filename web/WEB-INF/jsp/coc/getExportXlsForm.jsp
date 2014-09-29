<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.*,com.jiongsoft.cocit.*,com.jiongsoft.cocit.orm.*,com.jiongsoft.cocit.orm.expr.*,com.jiongsoft.cocit.entity.*,com.jiongsoft.cocit.ui.model.widget.*,com.jiongsoft.cocit.action.*"%>
<%
	/*
	 * 该JSP对应的Action方法是： EntityAction.getExportXlsForm()
	 */

	EntityForm form = (EntityForm) request.getAttribute("obj");
	ActionHelper actionHelper = form.getVar("actionHelper");
%>
<form class="entity-form">
	<input type="hidden" name="query.filterExpr" value="<%=form.getVar("query.filterExpr")%>" />
	<input type="hidden" name="query.parentExpr" value="<%=form.getVar("query.parentExpr")%>" />
	<input type="hidden" name="query.keywords" value="<%=form.getVar("query.keywords")%>" />
	<input type="hidden" name="sortField" value="<%=form.getVar("sortField")%>" />
	<input type="hidden" name="sortOrder" value="<%=form.getVar("sortOrder")%>" />
	<div class="entity-groups">
		<div class="entity-group"><div class="entity-group-header">请选择需要导出的字段</div>
		<table valign="top" width="100%" border="0" cellpadding="0" cellspacing="0">
			<% 
				for(EntityForm.FormField grp: form.getGroupFields()){
					for(EntityForm.FormField fld: grp.getChildren()){
						String mode = fld.getMode();
						if(mode.equals("N")||mode.equals("H")){
							continue;
						}
			%>
			<tr>
				<th class="entity-field-header"><%=fld.getTitle() %></td>
				<td class="entity-field-box"><input type="checkbox" name="columns" checked value="<%=fld.getEntityField().getPropName() %>" /></td>
			</tr>
			<% 
					} //end: grp.getChildren
				} //end: form.getGroupFields 
			%>
		</table>
	</div>
</form>

