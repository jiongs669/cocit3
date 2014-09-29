<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统自定义</title>
<link href="/jCocit/css/jCocit.min.css" rel="stylesheet" type="text/css" media="screen" />
<link href="/jCocit-src/css/jCocit.ui.icon.css" rel="stylesheet" type="text/css" media="screen" />
<link href="/jCocit-src/css/jCocit.ui.searchbox.css" rel="stylesheet" type="text/css" media="screen" />
<link href="/jCocit-src/css/jCocit.plugin.entity.css" rel="stylesheet" type="text/css" media="screen" />

<script src="/jCocit/common/jquery.min.js" type="text/javascript"></script>
<script src="/jCocit/js/jCocit.pack.js" type="text/javascript"></script>
<script src="/jCocit-src/js/jCocit.ui.combo.js" type="text/javascript"></script>
<script src="/jCocit-src/js/jCocit.ui.combotree.js" type="text/javascript"></script>
<script src="/jCocit-src/js/jCocit.ui.searchbox.js" type="text/javascript"></script>
<script src="/jCocit-src/js/jCocit.plugin.entity.js" type="text/javascript"></script>
<script src="/jCocit/js/min/jCocit.nls.zh.js" type="text/javascript"></script>
<style rel="stylesheet" type="text/css" media="screen" >
<!--
.add-system-form .entity-group{
	margin-top: 10px;
}
.add-system-form .entity-group-header{
	text-align: left;
}
.add-system-form .entity-field-header, .add-system-form .entity-field-grid-header{
	width: 60px;
}
.add-system-form .spinner,.add-system-form .input,.add-system-form .select,.add-system-form .CbB, .entity-group-header .input {
    border: 1px solid #c5dbec;
}
.add-system-form .entity-group-header{
    background-color: #ffffff;
    border: 0;
}
.entity-group-header .input{
    height: 18px;
}
.add-system-form .spinner-arrow-up,.add-system-form .spinner-arrow-down{
    width: 18px;
}
.add-system-form .spinner-text{
    border-color: transparent;
}
.add-system-form .entity-field-box .input,.add-system-form .entity-field-box .select,.add-system-form .entity-field-box .textarea{
    width: 141px;
}
.add-system-form .entity-field-box .select{
	width: 143px;
}
.add-system-form .entity-field-grid-box .input{
	width: 80px;
}
.system-advanced-props{
	display:none;
}
.add-system-form .entity-field-box .input2{
	width: 180px;
}
.add-system-form .op-button{
	font-size:12px;
	cursor: pointer;
}
-->
</style>
<script type="text/javascript">
$(document).ready(function(){
	$("#op_button_add_field").click(function(){
		var $table = $("#table_fields");
		var $tableTemplate = $("#div_field_template table tbody");
		var $newRow = $($tableTemplate.html()).appendTo($table);
		$(".jCocit-ui-tpl", $newRow).each(function(){
			$(this).addClass("jCocit-ui").removeClass("jCocit-ui-tpl");
		})
		$(".op_button_remove_field", $newRow).click(removeRow);
		jCocit.parseUI($newRow);
		
		return false;
	});
	$("#op_button_add_op").click(function(){
		var $table = $("#table_ops");
		var $tableTemplate = $("#div_op_template table tbody");
		var $newRow = $($tableTemplate.html()).appendTo($table);
		$(".jCocit-ui-tpl", $newRow).each(function(){
			$(this).addClass("jCocit-ui").removeClass("jCocit-ui-tpl");
		})
		$(".op_button_remove_op", $newRow).click(removeRow);
		jCocit.parseUI($newRow);
		
		return false;
	});
	var removeRow = function(){
		$(this).closest("tr").remove();
		
		return false;
	}
	$(".op_button_remove_field").click(removeRow);
	$(".op_button_remove_op").click(removeRow);
});
</script>
</head>
<body>
<form class="entity-form add-system-form">
	<input name="entity.id" type="hidden" value="">
	<div class="entity-groups">
		<div class="entity-group">
			<div class="entity-group-header">模块信息</div>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<th class="entity-field-header">模块分类</th>
					<td class="entity-field-box"><input name="entity.category.id" class="jCocit-ui jCocit-combotree" data-options="
						value:'',
						text:'',
						onlyLeafValue:false,
						styleName: 'folder-tree',
						url: '/coc/getEntityTreeData/_biz_catalog:_biz_catalog:'
						" /> <span class="icon-mode-M">&nbsp;&nbsp;&nbsp;</span>
					</td>
					<th class="entity-field-header">路径前缀</th>
					<td class="entity-field-box"><select name="entity.pathPrefix" class="select" >
							<option value=""></option>
							<option value="/coc" selected>/coc</option>
						</select>
					</td>
					<th class="entity-field-header">窗体模版</th>
					<td class="entity-field-box"><input name="entity.template" value="" class="input" /></td>
				</tr>
				<tr>
					<th class="entity-field-header">模块名称</th>
					<td class="entity-field-box"><input name="entity.name" value="" class="input" /> <span class="icon-mode-M">&nbsp;&nbsp;&nbsp;</span></td>
					<th class="entity-field-header">模块编号</th>
					<td class="entity-field-box"><input name="entity.code" value="" class="input" /></td>
					<th class="entity-field-header">排序表达式</th>
					<td class="entity-field-box"><input name="entity.sortExpr" value="" class="input" /></td>
				</tr>
				<tr class="system-advanced-props">
					<th class="entity-field-header">映射数据表</th>
					<td class="entity-field-box"><input name="entity.mappingTable" value="" class="input" /></td>
					<th class="entity-field-header">映射实体类</th>
					<td class="entity-field-box"><input name="entity.mappingClass" value="" class="input" /></td>
				</tr>
			</table>
		</div>
		<div class="entity-group">
			<div class="entity-group-header">操作管理</div>
			<div style="display:none;" id="div_op_template">
				<table>
					<tr>
						<td class="entity-field-box entity-field-grid-box"><input name="op.name" value="" class="input" /></td>
						<td class="entity-field-box entity-field-grid-box"><input name="op.mode" value="" class="input" /></td>
						<td class="entity-field-box entity-field-grid-box"><input name="op.typeCode" class="jCocit-ui-tpl jCocit-combogrid"
							data-options="value:'',text:'',
								panelWidth: 400,
								panelHeight: 300,
								singleSelect: false,
								idField: 'code',
								textField: 'name',
								url: '/coc/getEntityGridData/_demsy_lib_action:_demsy_lib_action:',
								rownumbers: true,
								pagination: true,
								mode: 'remote',
								pageSize: 20,
								columns: [[
								{field:'id',title:'ID',width:80,align:'right',checkbox:true},
								{field:'name',title:'名称',width:150,sortable:true,align:'left'},
								{field:'code',title:'编号',width:150,sortable:true,align:'left'}
								]],
								fitColumns: true" />
						</td>
						<td class="entity-field-box entity-field-grid-box"><input name="op.plugin" value="" class="input input2" /></td>
						<td class="entity-field-box entity-field-grid-box"><input name="op.template" value="" class="input input2" /></td>
						<td class="entity-field-box entity-field-grid-box" style="text-align: center;"><button class="op-button op_button_remove_op">删除</button></td>
					</tr>
				</table>
			</div>
			<table border="0" cellpadding="0" cellspacing="0" id="table_ops">
				<tr>
					<th class="entity-field-grid-header">操作名称</th>
					<th class="entity-field-grid-header">操作模式</th>
					<th class="entity-field-grid-header">操作类型</th>
					<th class="entity-field-grid-header">业务插件</th>
					<th class="entity-field-grid-header">界面模版</th>
					<th class="entity-field-grid-header"><button class="op-button" id="op_button_add_op">添加</button></th>
				</tr>
			</table>
		</div>
		<div class="entity-group">
			<div class="entity-group-header">字段管理</div>
			<div style="display:none;" id="div_field_template">
				<table>
					<tr>
						<td class="entity-field-box entity-field-grid-box"><input name="field.name" value="" class="input" /></td>
						<td class="entity-field-box entity-field-grid-box"><input name="field.code" value="" class="input" /></td>
						<td class="entity-field-box entity-field-grid-box"><input name="field.type" class="jCocit-ui-tpl jCocit-combotree" data-options="value:'',text:'',
							onlyLeafValue:true,
							url: '/coc/getEntityTreeData/_demsy_lib_field:_demsy_lib_field:'
						" />
						</td>
						<td class="entity-field-box entity-field-grid-box"><input name="field.mode" value="" class="input" /></td>
						<td class="entity-field-box entity-field-grid-box"><input name="field.gridOrder" value="" class="input jCocit-ui-tpl jCocit-spinnernumber" data-options="min:1,groupSeparator:','" /></td>
						<td class="entity-field-box entity-field-grid-box" style="text-align: center;"><button class="op-button op_button_remove_field">删除</button></td>
					</tr>
				</table>
			</div>
			<table border="0" cellpadding="0" cellspacing="0" id="table_fields">
				<tr>
					<th class="entity-field-grid-header">字段名称</th>
					<th class="entity-field-grid-header">字段编号</th>
					<th class="entity-field-grid-header">字段类型</th>
					<th class="entity-field-grid-header">字段模式</th>
					<th class="entity-field-grid-header">表头顺序</th>
					<th class="entity-field-grid-header"><button class="op-button" id="op_button_add_field">添加</button></th>
				</tr>
			</table>
		</div>
	</div>
</form>
</body></html>