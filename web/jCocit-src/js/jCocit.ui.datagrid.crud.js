(function($, jCocit) {

	var validatebox = $.fn.validatebox || false;

	function _indexOfRow(rows, row) {
		for ( var i = 0, len = rows.length; i < len; i++) {
			if (rows[i] == row) {
				return i;
			}
		}
		return -1;
	}

	function _removeRow(rows, idField, row) {
		if (typeof idField == "string") {
			for ( var i = 0, len = rows.length; i < len; i++) {
				if (rows[i][idField] == row) {
					rows.splice(i, 1);
					return;
				}
			}
		} else {
			var index = _indexOfRow(rows, idField);
			if (index != -1) {
				rows.splice(index, 1);
			}
		}
	}

	function resizeEditorWidth(gridTable) {
		var dc = $d(gridTable, "datagrid").dc;
		$f("div.datagrid-editable", dc.view).each(function() {
			var $editor = $(this);
			var field = $p($editor).attr("field");
			var col = $(gridTable).datagrid("getColumnOption", field);
			$editor.ow(col.width);
			var ed = $d(this, "datagrid.editor");
			if (ed.actions.resize) {
				ed.actions.resize(ed.target, $w($editor));
			}
		});
	}

	function beginEdit(gridTable, rowIndex) {
		var opts = $d(gridTable, "datagrid").options;
		var tr = opts.finder.getTr(gridTable, rowIndex);
		var row = opts.finder.getRow(gridTable, rowIndex);

		if ($hc("datagrid-row-editing", tr)) {
			return;
		}
		if (opts.onBeforeEdit.call(gridTable, rowIndex, row) == false) {
			return;
		}
		$ac("datagrid-row-editing", tr);
		_addRowCellEditor(gridTable, rowIndex);
		resizeEditorWidth(gridTable);
		$f("div.datagrid-editable", tr).each(function() {
			var field = $p($(this)).attr("field");
			var ed = $d(this, "datagrid.editor");
			ed.actions.setValue(ed.target, row[field]);
		});
		validateRow(gridTable, rowIndex);
	}

	function endEdit(gridTable, rowIndex, cancel) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var updatedRows = state.updatedRows;
		var insertedRows = state.insertedRows;

		var tr = opts.finder.getTr(gridTable, rowIndex);
		var row = opts.finder.getRow(gridTable, rowIndex);
		if (!$hc("datagrid-row-editing", tr)) {
			return;
		}
		if (!cancel) {
			if (!validateRow(gridTable, rowIndex)) {
				return;
			}

			var changed = false;
			var changedRow = {};
			$f("div.datagrid-editable", tr).each(function() {
				var field = $p($(this)).attr("field");
				var ed = $d(this, "datagrid.editor");
				var value = ed.actions.getValue(ed.target);
				if (row[field] != value) {
					row[field] = value;
					changed = true;
					changedRow[field] = value;
				}
			});
			if (changed) {
				if (_indexOfRow(insertedRows, row) == -1) {
					if (_indexOfRow(updatedRows, row) == -1) {
						updatedRows.push(row);
					}
				}
			}
		}
		$rc("datagrid-row-editing", tr);
		_removeRowCellEditor(gridTable, rowIndex);
		$(gridTable).datagrid("refreshRow", rowIndex);
		if (!cancel) {
			opts.onAfterEdit.call(gridTable, rowIndex, row, changedRow);
		} else {
			opts.onCancelEdit.call(gridTable, rowIndex, row);
		}
	}

	function getEditors(gridTable, rowIndex) {
		var opts = $d(gridTable, "datagrid").options;
		var tr = opts.finder.getTr(gridTable, rowIndex);

		var editors = [];
		$c("td", tr).each(function() {
			var cell = $f("div.datagrid-editable", $(this));
			if (cell.length) {
				var ed = $d(cell[0], "datagrid.editor");
				editors.push(ed);
			}
		});

		return editors;
	}

	function getEditor(gridTable, col) {
		var editors = getEditors(gridTable, col.index);
		for ( var i = 0; i < editors.length; i++) {
			if (editors[i].field == col.field) {
				return editors[i];
			}
		}
		return null;
	}

	function _addRowCellEditor(gridTable, rowIndex) {
		var opts = $d(gridTable, "datagrid").options;
		var tr = opts.finder.getTr(gridTable, rowIndex);

		$c("td", tr).each(function() {
			var cell = $f("div.datagrid-cell", $(this));
			var field = $(this).attr("field");
			var col = $(gridTable).datagrid("getColumnOption", field);
			if (col && col.editor) {
				var editorType, editorOptions;
				if (typeof col.editor == "string") {
					editorType = col.editor;
				} else {
					editorType = col.editor.type;
					editorOptions = col.editor.options;
				}
				var editor = opts.editors[editorType];
				if (editor) {
					var oldHtml = cell.html();
					var cellWidth = cell.ow();
					$ac("datagrid-editable", cell);
					cell.ow(cellWidth);
					cell.html('<table border="0" cellspacing="0" cellpadding="1"><tr><td></td></tr></table>');
					$c("table", cell).bind("click dblclick contextmenu", function(e) {
						e.stopPropagation();
					});
					$d(cell[0], "datagrid.editor", {
						actions : editor,
						target : editor.init($f("td", cell), editorOptions),
						field : field,
						type : editorType,
						oldHtml : oldHtml
					});
				}
			}
		});
		$(gridTable).datagrid("fixRowHeight", {
			rowIndex : rowIndex,
			syncFrozen : true
		});
	}

	function _removeRowCellEditor(gridTable, rowIndex) {
		var opts = $d(gridTable, "datagrid").options;
		var tr = opts.finder.getTr(gridTable, rowIndex);
		$c("td", tr).each(function() {
			var cell = $f("div.datagrid-editable", $(this));
			if (cell.length) {
				var ed = $d(cell[0], "datagrid.editor");
				if (ed.actions.destroy) {
					ed.actions.destroy(ed.target);
				}
				cell.html(ed.oldHtml);
				$.removeData(cell[0], "datagrid.editor");
				$rc("datagrid-editable", cell);
				cell.css("width", "");
			}
		});
	}

	function validateRow(gridTable, rowIndex) {
		var tr = $d(gridTable, "datagrid").options.finder.getTr(gridTable, rowIndex);
		if (!$hc("datagrid-row-editing", tr)) {
			return true;
		}
		var vbox = $f(".validatebox-text", tr);
		if (validatebox)
			vbox.validatebox("validate");
		vbox.trigger("mouseleave");
		var $invalid = $f(".validatebox-invalid", tr);
		return $invalid.length == 0;
	}

	function getChangedRows(gridTable, changedType) {
		var state = $d(gridTable, "datagrid");
		var insertedRows = state.insertedRows;
		var deletedRows = state.deletedRows;
		var updatedRows = state.updatedRows;
		if (!changedType) {
			var rows = [];
			rows = rows.concat(insertedRows);
			rows = rows.concat(deletedRows);
			rows = rows.concat(updatedRows);
			return rows;
		} else {
			if (changedType == "inserted") {
				return insertedRows;
			} else if (changedType == "deleted") {
				return deletedRows;
			} else if (changedType == "updated") {
				return updatedRows;
			}

		}
		return [];
	}

	function insertRow(gridTable, rowConfig) {
		var data = $d(gridTable, "datagrid").data;
		var view = $d(gridTable, "datagrid").options.view;
		var insertedRows = $d(gridTable, "datagrid").insertedRows;
		view.insertRow.call(view, gridTable, rowConfig.index, rowConfig.row);
		insertedRows.push(rowConfig.row);
		$(gridTable).datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}

	function appendRow(gridTable, row) {
		var data = $d(gridTable, "datagrid").data;
		var view = $d(gridTable, "datagrid").options.view;
		var insertedRows = $d(gridTable, "datagrid").insertedRows;
		view.insertRow.call(view, gridTable, null, row);
		insertedRows.push(row);
		$(gridTable).datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}

	function deleteRow(gridTable, rowIndex) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var data = state.data;
		var insertedRows = state.insertedRows;
		var deletedRows = state.deletedRows;
		var $grid = $(gridTable).datagrid("cancelEdit", rowIndex);

		var row = data.rows[rowIndex];
		if (_indexOfRow(insertedRows, row) >= 0) {
			_removeRow(insertedRows, row);
		} else {
			deletedRows.push(row);
		}
		_removeRow(state.selectedRows, opts.idField, data.rows[rowIndex][opts.idField]);
		_removeRow(state.checkedRows, opts.idField, data.rows[rowIndex][opts.idField]);
		opts.view.deleteRow.call(opts.view, gridTable, rowIndex);
		if (opts.height == "auto") {
			$grid.datagrid("fixRowHeight");
		}
		$grid.datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}

	function acceptChanges(gridTable) {
		var data = $d(gridTable, "datagrid").data;
		var ok = true;
		for ( var i = 0, len = data.rows.length; i < len; i++) {
			if (validateRow(gridTable, i)) {
				endEdit(gridTable, i, false);
			} else {
				ok = false;
			}
		}
		if (ok) {
			$(gridTable).datagrid("clearChanged");
		}
	}

	function rejectChanges(gridTable) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var originalRows = state.originalRows;
		var insertedRows = state.insertedRows;
		var deletedRows = state.deletedRows;
		var selectedRows = state.selectedRows;
		var checkedRows = state.checkedRows;
		var data = state.data;
		var $grid = $(gridTable);

		function _getIds(rows) {
			var ids = [];
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i][opts.idField]);
			}
			return ids;
		}

		function _selectRow(ids, type) {
			for ( var i = 0; i < ids.length; i++) {
				var rowIndex = $grid.datagrid("getRowIndex", ids[i]);
				$grid.datagrid((type == "s" ? "selectRow" : "checkRow"), {
					rowIndex : rowIndex,
					ignoreCheck : true
				});
			}
		}

		for ( var i = 0; i < data.rows.length; i++) {
			endEdit(gridTable, i, true);
		}
		var selectedIds = _getIds(selectedRows);
		var checkedIds = _getIds(checkedRows);
		selectedRows.splice(0, selectedRows.length);
		checkedRows.splice(0, checkedRows.length);
		data.total += deletedRows.length - insertedRows.length;
		data.rows = originalRows;
		$grid.datagrid("loadData", data);
		_selectRow(selectedIds, "s");
		_selectRow(checkedIds, "c");
		$(gridTable).datagrid("clearChanged");
	}

	var editors = {
		text : {
			init : function(td, options) {
				var $editable = $('<input type="text" class="datagrid-editable-input">').appendTo(td);
				return $editable;
			},
			getValue : function(input) {
				return $(input).val();
			},
			setValue : function(input, value) {
				$(input).val(value);
			},
			resize : function(input, w) {
				$(input).ow(w);
			}
		},
		textarea : {
			init : function(td, options) {
				var $editable = $('<textarea class="datagrid-editable-input"></textarea>').appendTo(td);
				return $editable;
			},
			getValue : function(input) {
				return $(input).val();
			},
			setValue : function(input, v) {
				$(input).val(v);
			},
			resize : function(input, w) {
				$(input).ow(w);
			}
		},
		checkbox : {
			init : function(td, options) {
				var $editor = $('<input type="checkbox">').appendTo(td);
				$editor.val(options.on);
				$editor.attr("offval", options.off);
				return $editor;
			},
			getValue : function(input) {
				if ($(input).is(":checked")) {
					return $(input).val();
				} else {
					return $(input).attr("offval");
				}
			},
			setValue : function(input, v) {
				var checked = false;
				if ($(input).val() == v) {
					checked = true;
				}
				$(input)._attr("checked", checked);
			}
		},
		numberbox : {
			init : function(td, options) {
				var $editor = $('<input type="text" class="datagrid-editable-input">').appendTo(td);
				$editor.numberbox(options);
				return $editor;
			},
			destroy : function(input) {
				$(input).numberbox("destroy");
			},
			getValue : function(input) {
				$(input).blur();
				return $(input).numberbox("getValue");
			},
			setValue : function(input, value) {
				$(input).numberbox("setValue", value);
			},
			resize : function(input, width) {
				$(input).ow(width);
			}
		},
		validatebox : {
			init : function(td, options) {
				var $editor = $('<input type="text" class="datagrid-editable-input">').appendTo(td);
				if (validatebox)
					$editor.validatebox(options);
				return $editor;
			},
			destroy : function(input) {
				if (validatebox)
					$(input).validatebox("destroy");
			},
			getValue : function(input) {
				return $(input).val();
			},
			setValue : function(input, value) {
				$(input).val(value);
			},
			resize : function(input, width) {
				$(input).ow(width);
			}
		},
		combodate : {
			init : function(td, options) {
				var $editor = $('<input type="text"/>').appendTo(td);
				$editor.combodate(options);
				return $editor;
			},
			destroy : function(input) {
				$(input).combodate("destroy");
			},
			getValue : function(input) {
				return $(input).combodate("getValue");
			},
			setValue : function(input, value) {
				$(input).combodate("setValue", value);
			},
			resize : function(input, width) {
				$(input).combodate("resize", width);
			}
		},
		combobox : {
			init : function(td, options) {
				var $editor = $('<input type="text">').appendTo(td);
				$editor.combobox(options || {});
				return $editor;
			},
			destroy : function(input) {
				$(input).combobox("destroy");
			},
			getValue : function(input) {
				return $(input).combobox("getValue");
			},
			setValue : function(input, value) {
				$(input).combobox("setValue", value);
			},
			resize : function(input, width) {
				$(input).combobox("resize", width);
			}
		},
		combotree : {
			init : function(td, options) {
				var $editor = $('<input type="text">').appendTo(td);
				$editor.combotree(options);
				return $editor;
			},
			destroy : function(input) {
				$(input).combotree("destroy");
			},
			getValue : function(input) {
				return $(input).combotree("getValue");
			},
			setValue : function(input, value) {
				$(input).combotree("setValue", value);
			},
			resize : function(input, width) {
				$(input).combotree("resize", width);
			}
		}
	};

	$.extend($.fn.datagrid.methods, {
		resizeEditor : $X(resizeEditorWidth),
		refreshRow : function(jq, rowData) {
			return jq.each(function() {
				var opts = $d(this, "datagrid").options;
				opts.view.refreshRow.call(opts.view, this, rowData);
			});
		},
		validateRow : $x(validateRow),
		updateRow : function(jq, rowConfig) {
			return jq.each(function() {
				var opts = $d(this, "datagrid").options;
				opts.view.updateRow.call(opts.view, this, rowConfig.index, rowConfig.row);
			});
		},
		/**
		 * arg: rowData -
		 */
		appendRow : $X(appendRow),
		/**
		 * arg: rowData -
		 */
		insertRow : $X(insertRow),
		/**
		 * arg: rowIndex -
		 */
		deleteRow : $X(deleteRow),
		/**
		 * arg: changedType -
		 */
		getChanges : $x(getChangedRows),
		acceptChanges : $X(acceptChanges),
		rejectChanges : $X(rejectChanges),
		/**
		 * arg: rowIndex -
		 */
		beginEdit : $X(beginEdit),
		/**
		 * arg: rowIndex -
		 */
		endEdit : $X(endEdit, false),
		/**
		 * arg: rowIndex -
		 */
		cancelEdit : $X(endEdit, true),
		/**
		 * arg: rowIndex -
		 */
		getEditors : $x(getEditors),
		/**
		 * arg: col -
		 */
		getEditor : $x(getEditor)
	});

	//
	$.extend($.fn.datagrid.defaults.view, {

		refreshRow : function(gridTable, rowIndex) {
			this.updateRow.call(this, gridTable, rowIndex, {});
		},
		updateRow : function(gridTable, rowIndex, row) {
			var opts = $d(gridTable, "datagrid").options;
			var rows = $(gridTable).datagrid("getRows");
			$.extend(rows[rowIndex], row);
			var styleText = opts.rowStyler ? opts.rowStyler.call(gridTable, rowIndex, rows[rowIndex]) : "";

			function _updateRow(frozen) {
				var fields = $(gridTable).datagrid("getColumnFields", frozen);
				var tr = opts.finder.getTr(gridTable, rowIndex, "body", (frozen ? 1 : 2));
				var $checked = $f("div.datagrid-cell-check input[type=checkbox]", tr).is(":checked");
				tr.html(this.renderRow.call(this, gridTable, fields, frozen, rowIndex, rows[rowIndex]));
				tr.attr("style", styleText || "");
				if ($checked) {
					$f("div.datagrid-cell-check input[type=checkbox]", tr)._attr("checked", true);
				}
			}

			_updateRow.call(this, true);
			_updateRow.call(this, false);
			$(gridTable).datagrid("fixRowHeight", rowIndex);
		},
		insertRow : function(gridTable, rowIndex, row) {
			var state = $d(gridTable, "datagrid");
			var opts = state.options;
			var dc = state.dc;
			var data = state.data;
			if (rowIndex == undefined || rowIndex == null) {
				rowIndex = data.rows.length;
			}
			if (rowIndex > data.rows.length) {
				rowIndex = data.rows.length;
			}

			function _updateRownumbers(frozen) {
				var viewIndex = frozen ? 1 : 2;
				for ( var i = data.rows.length - 1; i >= rowIndex; i--) {
					var tr = opts.finder.getTr(gridTable, i, "body", viewIndex);
					tr.attr("datagrid-row-index", i + 1);
					tr.attr("id", state.rowIdPrefix + "-" + viewIndex + "-" + (i + 1));
					if (frozen && opts.rownumbers) {
						var rownumber = i + 2;
						if (opts.pagination) {
							rownumber += (opts.pageIndex - 1) * opts.pageSize;
						}
						$f("div.datagrid-cell-rownumber", tr).html(rownumber);
					}
				}
			}

			function _insertRow(frozen) {
				var viewIndex = frozen ? 1 : 2;
				var fields = $(gridTable).datagrid("getColumnFields", frozen);
				var rowId = state.rowIdPrefix + "-" + viewIndex + "-" + rowIndex;
				var tr = '<tr id="' + rowId + '" class="datagrid-row" datagrid-row-index="' + rowIndex + '"></tr>';
				if (rowIndex >= data.rows.length) {
					if (data.rows.length) {
						opts.finder.getTr(gridTable, "", "last", viewIndex).after(tr);
					} else {
						var $body = frozen ? dc.body1 : dc.body2;
						$body.html('<table cellspacing="0" cellpadding="0" border="0"><tbody>' + tr + '</tbody></table>');
					}
				} else {
					opts.finder.getTr(gridTable, rowIndex + 1, "body", viewIndex).before(tr);
				}
			}

			_updateRownumbers.call(this, true);
			_updateRownumbers.call(this, false);
			_insertRow.call(this, true);
			_insertRow.call(this, false);
			data.total += 1;
			data.rows.splice(rowIndex, 0, row);
			this.refreshRow.call(this, gridTable, rowIndex);
		},
		deleteRow : function(gridTable, rowIndex) {
			var state = $d(gridTable, "datagrid");
			var opts = state.options;
			var data = state.data;

			function _updateRownumbers(frozen) {
				var viewIndex = frozen ? 1 : 2;
				for ( var i = rowIndex + 1; i < data.rows.length; i++) {
					var tr = opts.finder.getTr(gridTable, i, "body", viewIndex);
					tr.attr("datagrid-row-index", i - 1);
					tr.attr("id", state.rowIdPrefix + "-" + viewIndex + "-" + (i - 1));
					if (frozen && opts.rownumbers) {
						var rownumber = i;
						if (opts.pagination) {
							rownumber += (opts.pageIndex - 1) * opts.pageSize;
						}
						$f("div.datagrid-cell-rownumber", tr).html(rownumber);
					}
				}
			}

			opts.finder.getTr(gridTable, rowIndex).remove();
			_updateRownumbers.call(this, true);
			_updateRownumbers.call(this, false);
			data.total -= 1;
			data.rows.splice(rowIndex, 1);
		}
	});

	// extends default settings
	$.extend($.fn.datagrid.defaults, {
		editors : editors,
		/**
		 * rowIndex, row
		 */
		onBeforeEdit : $n,
		/**
		 * rowIndex, row, changedRow
		 */
		onAfterEdit : $n,
		/**
		 * rowIndex, row
		 */
		onCancelEdit : $n
	});

})(jQuery, jCocit);
