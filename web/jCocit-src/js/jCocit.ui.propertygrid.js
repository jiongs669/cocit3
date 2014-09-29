/**
 * 
 */
(function($, jCocit) {
	var currentGridTable;
	function _init(gridTable) {
		var state = $.data(gridTable, "propertygrid");
		var opts = state.options;
		$(gridTable).datagrid($.extend({}, opts, {
			styleName : "propertygrid",
			view : (opts.showGroup ? viewRender : undefined),
			onClickRow : function(rowIndex, row) {
				if (currentGridTable != this) {
					_processEdit(currentGridTable);
					currentGridTable = this;
				}
				if (opts.editIndex != rowIndex && row.editor) {
					var valueOption = $(this).datagrid("getColumnOption", "value");
					valueOption.editor = row.editor;
					_processEdit(currentGridTable);
					$(this).datagrid("beginEdit", rowIndex);
					$(this).datagrid("getEditors", rowIndex)[0].target.focus();
					opts.editIndex = rowIndex;
				}
				opts.onClickRow.call(gridTable, rowIndex, row);
			},
			loadFilter : function(gridData) {
				_processEdit(this);
				return opts.loadFilter.call(this, gridData);
			},
			onLoadSuccess : function(gridData) {
				$(gridTable).datagrid("getPanel").find("div.datagrid-group").attr("style", "");
				opts.onLoadSuccess.call(gridTable, gridData);
			}
		}));
		$(document).unbind(".propertygrid").bind("mousedown.propertygrid", function(e) {
			var p = $(e.target).closest("div.datagrid-view,div.CbPn");
			if (p.length) {
				return;
			}
			_processEdit(currentGridTable);
			currentGridTable = undefined;
		});
	}

	function _processEdit(gridTable) {
		var $grid = $(gridTable);
		if (!$grid.length) {
			return;
		}
		var opts = $.data(gridTable, "propertygrid").options;
		var editIndex = opts.editIndex;
		if (editIndex == undefined) {
			return;
		}
		var ed = $grid.datagrid("getEditors", editIndex)[0];
		if (ed) {
			ed.target.blur();
			if ($grid.datagrid("validateRow", editIndex)) {
				$grid.datagrid("endEdit", editIndex);
			} else {
				$grid.datagrid("cancelEdit", editIndex);
			}
		}
		opts.editIndex = undefined;
	}

	$.fn.propertygrid = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.propertygrid.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.datagrid(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "propertygrid");
			if (state) {
				$.extend(state.options, options);
			} else {
				var opts = $.extend({}, $.fn.propertygrid.defaults, $.fn.propertygrid.parseOptions(this), options);
				opts.frozenColumns = $.extend(true, [], opts.frozenColumns);
				opts.columns = $.extend(true, [], opts.columns);
				$.data(this, "propertygrid", {
					options : opts
				});
			}
			_init(this);
		});
	};

	$.fn.propertygrid.methods = {
		options : function(jq) {
			return $.data(jq[0], "propertygrid").options;
		}
	};

	$.fn.propertygrid.parseOptions = function(gridTable) {
		var $grid = $(gridTable);
		return $.extend({}, $.fn.datagrid.parseOptions(gridTable), jCocit.parseOptions(gridTable, [ {
			showGroup : "b"
		} ]));
	};

	var viewRender = $.extend({}, $.fn.datagrid.defaults.view, {
		render : function(gridTable, $body, frozen) {
			var state = $.data(gridTable, "datagrid");
			var opts = state.options;
			var rows = state.data.rows;
			var columnFields = $(gridTable).datagrid("getColumnFields", frozen);
			var html = [];
			var rowIndex = 0;
			var groups = this.groups;
			for ( var i = 0; i < groups.length; i++) {
				var group = groups[i];
				html.push("<div class=\"datagrid-group\" group-index=" + i + " style=\"height:25px;overflow:hidden;border-bottom:1px solid #ccc;\">");
				html.push("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"height:100%\"><tbody>");
				html.push("<tr>");
				html.push("<td style=\"border:0;\">");
				if (!frozen) {
					html.push("<span style=\"color:#666;font-weight:bold;\">");
					html.push(opts.groupFormatter.call(gridTable, group.fvalue, group.rows));
					html.push("</span>");
				}
				html.push("</td>");
				html.push("</tr>");
				html.push("</tbody></table>");
				html.push("</div>");
				html.push("<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>");
				for ( var j = 0; j < group.rows.length; j++) {
					var cls = (rowIndex % 2 && opts.striped) ? "class=\"datagrid-row datagrid-row-alt\"" : "class=\"datagrid-row\"";
					var styleText = opts.rowStyler ? opts.rowStyler.call(gridTable, rowIndex, group.rows[j]) : "";
					var styleAttr = styleText ? "style=\"" + styleText + "\"" : "";
					var rowIdPrefix = state.rowIdPrefix + "-" + (frozen ? 1 : 2) + "-" + rowIndex;
					html.push("<tr id=\"" + rowIdPrefix + "\" datagrid-row-index=\"" + rowIndex + "\" " + cls + " " + styleAttr + ">");
					html.push(this.renderRow.call(this, gridTable, columnFields, frozen, rowIndex, group.rows[j]));
					html.push("</tr>");
					rowIndex++;
				}
				html.push("</tbody></table>");
			}
			$($body).html(html.join(""));
		},
		onAfterRender : function(gridTable) {
			var opts = $.data(gridTable, "datagrid").options;
			var dc = $.data(gridTable, "datagrid").dc;
			var $view = dc.view;
			var $view1 = dc.view1;
			var $view2 = dc.view2;
			$.fn.datagrid.defaults.view.onAfterRender.call(this, gridTable);
			if (opts.rownumbers || opts.frozenColumns.length) {
				var $group = $view1.find("div.datagrid-group");
			} else {
				var $group = $view2.find("div.datagrid-group");
			}
			$("<td style=\"border:0;text-align:center;width:25px\"><span class=\"datagrid-row-expander datagrid-row-collapse\" style=\"display:inline-block;width:16px;height:16px;cursor:pointer\">&nbsp;</span></td>").insertBefore($group.find("td"));
			$view.find("div.datagrid-group").each(function() {
				var groupIndex = $(this).attr("group-index");
				$(this).find("span.datagrid-row-expander").bind("click", {
					groupIndex : groupIndex
				}, function(e) {
					if ($(this).hasClass("datagrid-row-collapse")) {
						$(gridTable).datagrid("collapseGroup", e.data.groupIndex);
					} else {
						$(gridTable).datagrid("expandGroup", e.data.groupIndex);
					}
				});
			});
		},
		onBeforeRender : function(gridTable, rows) {
			var opts = $.data(gridTable, "datagrid").options;
			var groups = [];
			for ( var i = 0; i < rows.length; i++) {
				var row = rows[i];
				var group = _findGroup(row[opts.groupField]);
				if (!group) {
					group = {
						fvalue : row[opts.groupField],
						rows : [ row ],
						startRow : i
					};
					groups.push(group);
				} else {
					group.rows.push(row);
				}
			}
			function _findGroup(groupValue) {
				for ( var i = 0; i < groups.length; i++) {
					var group = groups[i];
					if (group.fvalue == groupValue) {
						return group;
					}
				}
				return null;
			}

			this.groups = groups;
			var rows = [];
			for ( var i = 0; i < groups.length; i++) {
				var group = groups[i];
				for ( var j = 0; j < group.rows.length; j++) {
					rows.push(group.rows[j]);
				}
			}
			$.data(gridTable, "datagrid").data.rows = rows;
		}
	});

	$.extend($.fn.datagrid.methods, {
		expandGroup : function(jq, groupIndex) {
			return jq.each(function() {
				var $view = $.data(this, "datagrid").dc.view;
				if (groupIndex != undefined) {
					var $group = $view.find("div.datagrid-group[group-index=\"" + groupIndex + "\"]");
				} else {
					var $group = $view.find("div.datagrid-group");
				}
				var $groupExpander = $group.find("span.datagrid-row-expander");
				if ($groupExpander.hasClass("datagrid-row-expand")) {
					$groupExpander.removeClass("datagrid-row-expand").addClass("datagrid-row-collapse");
					$group.next("table").show();
				}
				$(this).datagrid("fixRowHeight");
			});
		},
		collapseGroup : function(jq, groupIndex) {
			return jq.each(function() {
				var $view = $.data(this, "datagrid").dc.view;
				if (groupIndex != undefined) {
					var $group = $view.find("div.datagrid-group[group-index=\"" + groupIndex + "\"]");
				} else {
					var $group = $view.find("div.datagrid-group");
				}
				var $groupExpander = $group.find("span.datagrid-row-expander");
				if ($groupExpander.hasClass("datagrid-row-collapse")) {
					$groupExpander.removeClass("datagrid-row-collapse").addClass("datagrid-row-expand");
					$group.next("table").hide();
				}
				$(this).datagrid("fixRowHeight");
			});
		}
	});
	$.fn.propertygrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		singleSelect : true,
		remoteSort : false,
		fitColumns : true,
		loadMsg : "",
		frozenColumns : [ [ {
			field : "f",
			title : "",
			width : 20,
			resizable : false
		} ] ],
		columns : [ [ {
			field : "name",
			title : "Name",
			width : 100,
			sortable : true
		}, {
			field : "value",
			title : "Value",
			width : 100,
			resizable : false
		} ] ],
		showGroup : false,
		groupField : "group",
		groupFormatter : function(fvalue, rows) {
			return fvalue;
		}
	});
})(jQuery, jCocit);
