(function($, jCocit) {
	jCocit.entity = {
		/**
		 * 处理功能操作
		 */
		doAction : function(opts) {
			switch (opts.opCode) {
			case 101:// insert record
				doEdit(opts, "");
				break;
			case 9101:// insert records
				doEditRows(opts, "");
				break;
			case 102:// edit record
				var gridID = "#datagrid_" + opts.token;
				var row = $(gridID).datagrid("getSelected");
				if (row) {
					if (opts.opMode.startsWith("v"))
						doView(opts, row.id);
					else
						doEdit(opts, row.id);
				} else {
					Jwarn(jCocit.entity.defaults.unselectedOne);
				}
				break;
			case 107: // export excel
				doExportXls(opts);
				break;
			case 108: // import excel
				doImportXls(opts);
				break;
			case 204:// synchronized exec task
				runPluginOnExpr(opts);
				break;
			case 299:// delete rows
				doDelete(opts);
				break;
			default:
				Jalert(jCocit.entity.defaults.unsupport + "{opCode: '" + opts.opCode + "', funcExpr: '" + opts.funcExpr + "', token:" + opts.token + "}");
			}
		},
		doSetting : function(opts) {
			Jalert(jCocit.entity.defaults.unsupport);
		},
		/**
		 * 在SearchBox框上执行查询操作时将调用该方法：刷新token对应的业务表Grid数据
		 */
		doSearch : function(value, name) {
			doGridRefresh($(this).searchbox("options").token);
		},
		/**
		 * 在导航树上执行“Select/Check”操作时将调用该方法：刷新token对应的业务表Grid数据。
		 */
		doTreeSelect : function(node) {
			doGridRefresh($(this).tree("options").token);
		},
		/**
		 * 在Grid上执行“Select/UnSelect/Check/UnCheck/CheckAll/UnCheckAll”等操作时将调用该方法：刷新子业务表的Grid数据。
		 */
		doGridSelect : function(rowIndex, row) {
			// 查找当前GRID
			var gridOptions = $(this).datagrid("options");
			// 查找子业务表Tabs
			var $childrenTabs = $("#childrentabs_" + gridOptions.token);
			if ($childrenTabs.length > 0) {
				// 查找选中的子业务表Tab
				var $childTab = $childrenTabs.tabs("getSelected");
				// 查找子业务表Grid
				var $childGrid = $(".jCocit-datagrid", $childTab);
				// 刷新子业务表Grid数据
				doGridRefresh($childGrid.datagrid("options").token);
			}
		},
		/**
		 * Grid数据刷新之前将调用该方法：准备Grid查询参数。
		 * <UL>
		 * <LI>主要用于切换到尚未加载的业务子表Tab选项时，这是Grid尚未加载，只有等待Grid加载数据的时候才会调用该方法；
		 * <LI>如果Tab项已经加载，则应该调用onTabsSelect方法；
		 * </UL>
		 */
		doGridBeforeLoad : function(queryParams) {
			// 查找当前GRID
			var gridOptions = $(this).datagrid("options");
			_prepareGridQueryParams(gridOptions.token, queryParams);
		},
		doGridHeaderContextMenu : function(e, field) {
			var $grid = $(this);
			var gridOptions = $grid.datagrid("options");

			// 查找Grid表头环境菜单
			var contextMenuID = "gridContextMenu_" + gridOptions.token;
			var $contextMenu = $("#" + contextMenuID);

			// 创建Grid表头环境菜单
			if ($contextMenu.length == 0) {
				$contextMenu = $('<div id="' + contextMenuID + '"/>').appendTo('body');
				$contextMenu.menu({
					hideOnMouseLeave : true,
					hideOnMouseClick : false,
					onClick : function(item) {
						if (item.iconCls == 'icon-check') {
							$grid.datagrid('hideColumn', item.name);
							$contextMenu.menu('setIcon', {
								target : item.target,
								iconCls : 'icon-uncheck'
							});
						} else {
							$grid.datagrid('showColumn', item.name);
							$contextMenu.menu('setIcon', {
								target : item.target,
								iconCls : 'icon-check'
							});
						}
					}
				});
				var fields = $grid.datagrid('getColumnFields');
				for ( var i = 2; i < fields.length; i++) {
					var field = fields[i];
					var col = $grid.datagrid('getColumnOption', field);
					$contextMenu.menu('append', {
						text : col.title,
						name : field,
						iconCls : 'icon-check'
					});
				}
			}
			$contextMenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			});
			e.preventDefault();
		},
		/**
		 * 切换业务子表Tab项时将调用该方法，如果Tab中的Grid已经存在，则刷新Grid数据。
		 */
		doTabsSelect : function(tabTitle, tabIndex) {
			// 查找选中的子业务表Tab
			var $childTab = $(this).tabs("getSelected");
			// 查找子业务表Grid
			var $childGrid = $(".jCocit-datagrid", $childTab);
			// 刷新子业务表Grid数据
			if ($childGrid.length)
				doGridRefresh($childGrid.datagrid("options").token);
		},
		getGridQueryParams : function(token) {
			var ret = {};
			_prepareGridQueryParams(token, ret);
			return ret;
		},
		getSelectedGridRows : function(token, field) {
			return _getSelectedGridRows(token, field);
		}
	};

	/**
	 * 准备加载实体表单时的参数。
	 * <p>
	 * 执行getEntityForm操作之前将调用该方法准备业务表单数据。
	 */
	function _prepareEntityFormParams(token, formData) {
		// 根据令牌查找导航树、Grid、搜索框对象
		var $tree = $("#tree_" + token);
		var $grid = $("#datagrid_" + token);

		/*
		 * Navi Tree JSON Expression
		 */
		// 获取导航树中选中的节点
		if ($tree.length > 0) {
			var node = $tree.tree("getSelected");
			if (node) {
				var nodeID = node.id;
				var idx = nodeID.indexOf(":");
				if (idx > 0) {
					var fld = nodeID.substring(0, idx);
					var val = nodeID.substring(idx + 1);
					if (fld.length > 0 && val.length > 0)
						formData["entity." + fld] = val;
					idx = fld.indexOf(".id");
					if (idx > 0)
						fld = fld.substring(0, idx);
					formData["entity." + fld + ".name"] = node.text;

				}
			}
		}

		/*
		 * Main Grid JSON Expression
		 */
		// 查找Grid所在的Tab
		var $gridTab = $grid.closest(".jCocit-gridtab");
		if ($gridTab.length > 0) {// 找到Grid所在的Tab

			// 获取Tab属性；获取父Grid令牌
			var gridTabOptions = $gridTab.panel("options");
			var parentToken = gridTabOptions.token;

			if (parentToken) {
				// 查找父Grid
				var $parentGrid = $("#datagrid_" + parentToken);
				if ($parentGrid.length > 0) {
					// 获取父Grid中选中的行
					var row = $parentGrid.datagrid("getSelected");
					if (row) {
						formData["entity." + gridTabOptions.fkfield + ".id"] = row.id;
						formData["entity." + gridTabOptions.fkfield + ".name"] = row.name;
					}

				}
			}
		}
	}
	/**
	 * 准备Grid查询参数。
	 * <UL>
	 * <LI>通过token查找Grid、Tree、Searchbox对象；
	 * <LI>queryParams：即为Grid查询参数JSON对象，查询条件直接放入该对象中；
	 * </UL>
	 */
	function _prepareGridQueryParams(token, queryParams) {
		// 根据令牌查找导航树、Grid、搜索框对象
		var $tree = $("#tree_" + token);
		var $grid = $("#datagrid_" + token);
		var $searchbox = $("#searchbox_" + token);

		/*
		 * Navi Tree JSON Expression
		 */
		// 获取导航树中选中（包括单选和多选）的节点
		if ($tree.length > 0) {
			var nodes = $tree.tree("getChecked");
			var node = $tree.tree("getSelected");
			if (node)
				nodes.push(node);

			// 生成导航树过滤表达式。导航树节点ID格式为“字段名:字段值”。
			var naviTreeExpr = {};
			var nodeID, idx, fld, val, arr;
			for (i = 0; i < nodes.length; i++) {
				nodeID = nodes[i].id;
				idx = nodeID.indexOf(":");
				fld = "";
				val = "";
				if (idx > 0) {
					fld = nodeID.substring(0, idx);
					val = nodeID.substring(idx + 1);
				}

				if (fld.length > 0 && val.length > 0) {
					arr = naviTreeExpr[fld];
					if (!arr) {
						arr = [];
						naviTreeExpr[fld] = arr;
					}
					arr.push(val);
				}
			}
			queryParams["query.filterExpr"] = $.toJsonString(naviTreeExpr);
		}

		/*
		 * Search Box JSON Expression
		 */
		if ($searchbox.length > 0) {
			var searchBoxField = $searchbox.searchbox("getName");// 字段名
			var searchBoxValue = $searchbox.searchbox("getValue");// 字段值
			// 生成关键字查询表达式
			if (searchBoxField != null && searchBoxField.trim().length > 0) {
				var searchBoxExpr = {};
				searchBoxExpr[searchBoxField] = searchBoxValue;
				queryParams["query.keywords"] = $.toJsonString(searchBoxExpr);
			} else {
				queryParams["query.keywords"] = searchBoxValue;
			}
		}

		/*
		 * Main Grid JSON Expression
		 */
		// 查找Grid所在的Tab
		var $gridTab = $grid.closest(".jCocit-gridtab");
		if ($gridTab.length > 0) {// 找到Grid所在的Tab

			// 获取Tab属性；获取父Grid令牌
			var gridTabOptions = $gridTab.panel("options");
			var parentToken = gridTabOptions.token;

			if (parentToken) {
				// 查找父Grid
				var $parentGrid = $("#datagrid_" + parentToken);
				if ($parentGrid.length > 0) {
					// 获取父Grid中选中（包括seleted和checked）的行
					var row, parentGridExpr = {}, idArray = [];
					var rows = $parentGrid.datagrid("getChecked");
					row = $parentGrid.datagrid("getSelected");
					if (row)
						rows.push(row);

					// 创建父Grid中选中行的ID数组
					for (i = 0; i < rows.length; i++) {
						row = rows[i];
						idArray.push(row.id);
					}
					// 创建父Grid过滤表达式
					if (idArray.length > 0) {
						parentGridExpr[gridTabOptions.fkfield] = idArray;
						queryParams["query.parentExpr"] = $.toJsonString(parentGridExpr);
					} else {
						queryParams["query.parentExpr"] = "";
					}
				}
			}
		}
	}
	/**
	 * 刷新Grid数据，通过token查找Grid对象
	 */
	function doGridRefresh(token) {
		var $grid = $("#datagrid_" + token);
		if ($grid.length > 0)
			$grid.datagrid("reload");
	}
	function doSave(opts, dataID, data, funcSuccess, funcComplete) {
		$.doAjax({
			type : "POST",
			dataType : "json",
			url : "/coc/saveEntityRow/" + opts.funcExpr + "/" + dataID,
			data : data,
			success : funcSuccess,
			complete : funcComplete
		});
	}
	function doSaveRows(opts, dataID, data, funcSuccess, funcComplete) {
		$.doAjax({
			type : "POST",
			dataType : "json",
			url : "/coc/saveEntityRows/" + opts.funcExpr + "/" + dataID,
			data : data,
			success : funcSuccess,
			complete : funcComplete
		});
	}
	function doView(opts, dataID) {
		var loadFormUrl = "/coc/getEntityRowForm/" + opts.funcExpr + "/" + dataID;
		jCocit.dialog.open(loadFormUrl, "dialog_" + opts.token + "_" + opts.opCode, {
			title : opts.text,
			width : 800,
			height : 600,
			logoCls : opts.iconCls || 'icon-logo',
			buttons : [ {
				text : jCocit.entity.defaults.cancel,
				onClick : function(data) {
					$(this).dialog('close');
				}
			} ],
		});
	}
	function doEdit(opts, dataID) {
		var data = {};
		_prepareEntityFormParams(opts.token, data);
		var loadFormUrl = (opts.actionPath || ("/coc/getEntityRowForm/" + opts.funcExpr + "/")) + dataID + "?" + $.param(data);
		jCocit.dialog.open(loadFormUrl, "dialog_" + opts.token + "_" + opts.opCode, {
			title : opts.text,
			width : 800,
			height : 600,
			logoCls : opts.iconCls || 'icon-logo',
			buttons : [ {
				text : jCocit.entity.defaults.confirm,
				onClick : function(data) {
					var $form = $("form", this);
					var $dialog = $(this);

					var $btn = $(data.target);
					$btn.attr("disabled", true);
					doSave(opts, dataID, $form.serialize(), function() {
						$("#datagrid_" + opts.token).datagrid("reload");
						if (dataID && dataID != "") {
							$dialog.dialog('close');
						} else {
							Jsuccess("操作成功");
						}
					}, function() {
						$btn.attr("disabled", false);
					});
				}
			}, {
				text : jCocit.entity.defaults.cancel,
				onClick : function(data) {
					$(this).dialog('close');
				}
			} ],
		});
	}
	function doEditRows(opts, dataID) {
		var data = {};
		_prepareEntityFormParams(opts.token, data);
		var loadFormUrl = (opts.actionPath || ("/coc/getEntityRowsForm/" + opts.funcExpr + "/")) + dataID + "?" + $.param(data);
		if (opts.actionWindow == '_blank') {
			window.open(loadFormUrl, "").focus();
		} else {
			jCocit.dialog.open(loadFormUrl, "dialog_" + opts.token + "_" + opts.opCode, {
				title : opts.text,
				width : 800,
				height : 600,
				logoCls : opts.iconCls || 'icon-logo',
				buttons : [ {
					text : jCocit.entity.defaults.confirm,
					onClick : function(data) {
						var $form = $("form", this);
						var $dialog = $(this);

						var $btn = $(data.target);
						$btn.attr("disabled", true);
						doSaveRows(opts, dataID, $form.serialize(), function() {
							Jsuccess("操作成功");
							$("#datagrid_" + opts.token).datagrid("reload");
							$dialog.dialog('close');
						}, function() {
							$btn.attr("disabled", false);
						});
					}
				}, {
					text : jCocit.entity.defaults.cancel,
					onClick : function(data) {
						$(this).dialog('close');
					}
				} ],
			});
		}
	}
	function doExportXls(opts) {
		var $grid = $("#datagrid_" + opts.token);
		var rows = _getSelectedGridRows(opts.token);
		var gridOptions = $grid.datagrid("options");

		var data = {};
		_prepareGridQueryParams(opts.token, data);
		data["sortField"] = gridOptions["sortField"];
		data["sortOrder"] = gridOptions["sortOrder"];

		var loadFormUrl = "/coc/getExportXlsForm/" + opts.funcExpr + "/" + rows.join(",") + "?" + $.param(data);
		jCocit.dialog.open(loadFormUrl, "dialog_" + opts.token + "_" + opts.opCode, {
			title : opts.text,
			width : 800,
			height : 600,
			logoCls : opts.iconCls || 'icon-logo',
			buttons : [ {
				text : jCocit.entity.defaults.confirm,
				onClick : function(data) {
					var $form = $("form", this);
					var form = $form[0];
					form.action = "/coc/doExportXlsOnExpr/" + opts.funcExpr + "/" + rows.join(",");
					form.method = "POST";
					form.target = "_blank";

					form.submit();
					$(this).dialog('close');
				}
			}, {
				text : jCocit.entity.defaults.cancel,
				onClick : function(data) {
					$(this).dialog('close');
				}
			} ],
		});
	}
	function doImportXls(opts) {
		var $grid = $("#datagrid_" + opts.token);
		var rows = _getSelectedGridRows(opts.token);
		var gridOptions = $grid.datagrid("options");

		var data = {};
		_prepareGridQueryParams(opts.token, data);

		var loadFormUrl = "/coc/getImportXlsForm/" + opts.funcExpr + "/" + rows.join(",") + "?" + $.param(data);
		jCocit.dialog.open(loadFormUrl, "dialog_" + opts.token + "_" + opts.opCode, {
			title : opts.text,
			width : 800,
			height : 600,
			logoCls : opts.iconCls || 'icon-logo',
			buttons : [ {
				text : jCocit.entity.defaults.confirm,
				onClick : function(data) {
					var $form = $("form", this);
					var $btn = $(this);
					$.doAjax({
						type : "POST",
						dataType : "json",
						data : $form.serialize(),
						url : "/coc/doImportXlsOnExpr/" + opts.funcExpr + "/" + rows.join(","),
						success : function(json) {
							alert(json.message);
							if (json.statusCode == 200) {
								$grid.datagrid("reload");
								$btn.dialog('close');
							}
						}
					});
				}
			}, {
				text : jCocit.entity.defaults.cancel,
				onClick : function(data) {
					$(this).dialog('close');
				}
			} ],
		});
	}
	function runPluginOnExpr(opts) {
		$.doAjax({
			type : "POST",
			dataType : "json",
			url : "/coc/runPluginOnExpr/" + opts.funcExpr,
			success : function(json) {
				alert(json.message);
				$("#datagrid_" + opts.token).datagrid("reload");
			}
		});
	}
	/**
	 * 获取选中(selected/checked)行的ID数组
	 */
	function _getSelectedGridRows(token, field) {
		var gridID = "#datagrid_" + token;
		var rows = $(gridID).datagrid("getChecked");
		if (rows.length == 0) {
			var row = $(gridID).datagrid("getSelected");
			if (row)
				rows[0] = row;
		}

		if (!field)
			field = "id";

		var ids = new Array();
		for (i = 0; i < rows.length; i++) {
			ids[ids.length] = rows[i][field];
		}

		return ids;
	}
	/**
	 * 删除选中(selected/checked)的行
	 */
	function doDelete(opts) {
		var rows = _getSelectedGridRows(opts.token);

		if (rows.length == 0) {
			Jwarn(jCocit.entity.defaults.unselectedAny);
		} else {
			Jconfirm(jCocit.entity.defaults.deleteWarn.format(rows.length), "", function(ok) {
				if (!ok)
					return;

				$.doAjax({
					type : "POST",
					dataType : "json",
					url : (opts.actionPath || ("/coc/delEntityRows/" + opts.funcExpr + "/")) + rows.join(","),
					success : function() {
						$("#datagrid_" + opts.token).datagrid("reload");
					}
				});
			});
		}
	}
	//
	// function _init(mdlDIV) {
	// }
	// $.fn.entity = function(options, args) {
	// if (typeof options == "string") {
	// var fn = $.fn.entity.methods[options];
	// if (fn)
	// return fn(this, args);
	// }
	// options = options || {};
	// return this.each(function() {
	// var state = $d(this, "entity");
	// if (state) {
	// $.extend(state.options, options);
	// } else {
	// $d(this, "entity", {
	// options : $.extend({}, $.fn.entity.defaults, $.fn.entity.parseOptions(this), options)
	// });
	// }
	// _init(this);
	// });
	// };
	//
	// $.fn.entity.parseOptions = function(html) {
	// return $.extend({}, jCocit.parseOptions(html, []));
	// };
	//
	// $.fn.entity.methods = {
	// options : function(jq) {
	// return $d(jq[0], "entity").options;
	// }
	// };

	jCocit.entity.defaults = {
		confirm : "Confirm",
		cancel : "Cancel",
		unsupport : "Not Support!",
		deleteWarn : "Are you sure delete the selected {0} records?",
		unselectedAny : "Please select one record at least",
		unselectedOne : "Please first select one record"
	};

})(jQuery, jCocit);
