<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<form class="entity-form">
	<div class="entity-group">
		<table valign="top" width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th class="entity-field-header">用户类型：</th>
				<td class="entity-field-box">
				    <input class="jCocit-ui jCocit-combobox" name="entity.userType" url="/coc/getEntityListData/37:?query.filterExpr={refSystemExtends: ['BaseUser']}" data-options="
									valueField:'id',
									textField:'text',
									panelHeight:'auto',
									onChange: function(newValue, oldValue){
									   $('#userRule').combodialog('options').url = '/coc/getEntityTableUI/' + newValue + '?_uiHeight=530&_uiWidth=800&__token__='+ new Date().getTime();
									}
							" />
				</td>
			</tr>
			<tr>
				<th class="entity-field-header">授权用户：</th>
				<td class="entity-field-box">
				    <input id="userRule" class="jCocit-ui jCocit-combodialog" name="entity.userRule" data-options="
				        dialogTitle: '选择用户',
				        onSelect: function(comboHTML, buttonData){
				            var $combo = $(comboHTML);
				            var $dialog = $(this);
				            var $grid = $('.jCocit-datagrid', $dialog);
				            var gridOptions = $grid.datagrid('options');
				            
				            // get selected grid rows
                            var rows = jCocit.entity.getSelectedGridRows(gridOptions.token, 'code');
					        if(rows.length > 0){
				                $combo.combo('setValue', rows.join(','));
				                $combo.combo('setText', rows.join(','));
				            }else{
                                var expr = jCocit.entity.getGridQueryParams(gridOptions.token);
                                var str = $.toJsonString(expr);
                                $combo.combo('setValue', str);
                                $combo.combo('setText', str);
				            }
				            
				            //
				        }
				    " />
				</td>
			</tr>
			<tr>
				<th></th>
				<td></td>
			</tr>
		</table>
	</div>
</form>