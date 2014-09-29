<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.*,com.jiongsoft.cocit.*,com.jiongsoft.cocit.orm.*,com.jiongsoft.cocit.orm.expr.*,com.jiongsoft.cocit.entity.*,com.jiongsoft.cocit.ui.model.widget.*,com.jiongsoft.cocit.action.*"%>
<%
	/*
	 * 该JSP对应的Action方法是： EntityAction.getImportXlsForm()
	 */

	EntityForm form = (EntityForm) request.getAttribute("obj");
	ActionHelper actionHelper = form.getVar("actionHelper");
%>
<form class="entity-form">
	<div class="entity-groups">
		<div class="entity-group"><div class="entity-group-header">导入EXCEL数据</div>
		<table valign="top" width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th class="entity-field-header">请选择需要导入的EXCEL文件：</td>
				<td class="entity-field-box">
					<input class="jCocit-ui jCocit-upload" type="file" id="excelFilePath" name="excelFilePath" value="" data-options="
						fileTypeExts : '*.xls; *.xlsx',
						fileTypeDesc : 'EXCEL数据文件！',
						comboWidth : 280,
						comboHeight : 26
					" />
				</td>
			</tr>
		</table>
	</div>
</form>

