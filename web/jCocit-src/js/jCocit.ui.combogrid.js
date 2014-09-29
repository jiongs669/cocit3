/**
 * 
 */
(function($, jCocit) {
	function _init(target) {
		var opts = $d(target, "combogrid").options;
		$ac("combogrid-f", $(target));
		$(target).combo($.extend({}, opts, {
			onInitPanel : function() {
				_initDataGrid(target);
			}
		}));
	}

	function _initDataGrid(target) {
		var opts = $d(target, "combogrid").options;
		var $grid = $d(target, "combogrid").grid;
		var $panel = $(target).combo("panel");
		if (!$grid) {
			$grid = $("<table></table>").appendTo($panel);
			$d(target, "combogrid").grid = $grid;
		}
		$grid.datagrid($.extend({}, opts, {
			border : false,
			fit : true,
			singleSelect : (!opts.multiple),
			onLoadSuccess : function(_6) {
				var remainText = $d(target, "combogrid").remainText;
				var values = $(target).combo("getValues");
				setValues(target, values, remainText);
				opts.onLoadSuccess.apply(target, arguments);
			},
			onClickRow : _doClickRow,
			onSelect : function(rowIndex, row) {
				_doSelectRow();
				opts.onSelect.call(this, rowIndex, row);
			},
			onUnselect : function(rowIndex, row) {
				_doSelectRow();
				opts.onUnselect.call(this, rowIndex, row);
			},
			onSelectAll : function(rows) {
				_doSelectRow();
				opts.onSelectAll.call(this, rows);
			},
			onUnselectAll : function(rows) {
				if (opts.multiple) {
					_doSelectRow();
				}
				opts.onUnselectAll.call(this, rows);
			}
		}));

		function _doClickRow(rowIndex, row) {
			$d(target, "combogrid").remainText = false;
			_doSelectRow();
			if (!opts.multiple) {
				$(target).combo("hidePanel");
			}
			opts.onClickRow.call(this, rowIndex, row);
		}

		function _doSelectRow() {
			var remainText = $d(target, "combogrid").remainText;
			var selectedRows = $grid.datagrid("getSelections");
			var vv = [], ss = [];
			for ( var i = 0; i < selectedRows.length; i++) {
				vv.push(selectedRows[i][opts.idField]);
				ss.push(selectedRows[i][opts.textField]);
			}
			if (!opts.multiple) {
				$(target).combo("setValues", (vv.length ? vv : [ "" ]));
			} else {
				$(target).combo("setValues", vv);
			}
			if (!remainText) {
				$(target).combo("setText", ss.join(opts.separator));
			}
		}

		return $grid;
	}

	function doKey(target, key) {
		var keyType = 0;
		if (key == jCocit.keyCode.UP)
			keyType = -1;
		else if (key == jCocit.keyCode.DOWN)
			keyType = 1;

		var opts = $d(target, "combogrid").options;
		var $grid = $d(target, "combogrid").grid;
		var len = $grid.datagrid("getRows").length;
		if (!len) {
			return;
		}
		$d(target, "combogrid").remainText = false;
		var rowIndex;
		var selectedRows = $grid.datagrid("getSelections");
		if (selectedRows.length) {
			rowIndex = $grid.datagrid("getRowIndex", selectedRows[selectedRows.length - 1][opts.idField]);
			rowIndex += keyType;
			if (rowIndex < 0) {
				rowIndex = 0;
			}
			if (rowIndex >= len) {
				rowIndex = len - 1;
			}
		} else {
			if (keyType > 0) {
				rowIndex = 0;
			} else if (keyType < 0) {
				rowIndex = len - 1;
			} else {
				rowIndex = -1;

			}
		}
		if (rowIndex >= 0) {
			$grid.datagrid("clearSelections");
			$grid.datagrid("selectRow", rowIndex);
		}
	}

	function setValues(target, values, remainText) {
		var opts = $d(target, "combogrid").options;
		var $grid = $d(target, "combogrid").grid;
		var rows = $grid.datagrid("getRows");
		var ss = [];
		for ( var i = 0; i < values.length; i++) {
			var rowIndex = $grid.datagrid("getRowIndex", values[i]);
			if (rowIndex >= 0) {
				$grid.datagrid("selectRow", rowIndex);
				ss.push(rows[rowIndex][opts.textField]);
			} else {
				ss.push(values[i]);
			}
		}
		if ($(target).combo("getValues").join(",") == values.join(",")) {
			return;
		}
		$(target).combo("setValues", values);
		if (!remainText) {
			$(target).combo("setText", ss.join(opts.separator));
		}
	}

	function doQuery(target, keyword) {
		var opts = $d(target, "combogrid").options;
		var $grid = $d(target, "combogrid").grid;
		$d(target, "combogrid").remainText = true;
		if (opts.multiple && !keyword) {
			setValues(target, [], true);
		} else {
			setValues(target, [ keyword ], true);
		}
		if (opts.mode == "remote") {
			$grid.datagrid("clearSelections");
			$grid.datagrid("load", $.extend({}, opts.queryParams, {
				"query.keywords" : keyword
			}));
		} else {
			if (!keyword) {
				return;
			}
			var rows = $grid.datagrid("getRows");
			for ( var i = 0; i < rows.length; i++) {
				if (opts.filter.call(target, keyword, rows[i])) {
					$grid.datagrid("clearSelections");
					$grid.datagrid("selectRow", i);
					return;
				}
			}
		}
	}

	$.fn.combogrid = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.combogrid.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return $.fn.combo.methods[options](this, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "combogrid");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "combogrid", {
					options : $.extend({}, $.fn.combogrid.defaults, $.fn.combogrid.parseOptions(this), options)
				});
			}
			_init(this);
		});
	};
	$.fn.combogrid.methods = {
		options : function(jq) {
			var opts = $d(jq[0], "combogrid").options;
			opts.originalValue = jq.combo("options").originalValue;
			return opts;
		},
		grid : function(jq) {
			var $grid = $d(jq[0], "combogrid").grid;
			if (!$grid)
				$grid = _initDataGrid(jq[0]);

			return $grid;
		},
		setValues : $X(setValues),
		setValue : function(jq, value) {
			return jq.each(function() {
				setValues(this, [ value ]);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				$(this).combogrid("grid").datagrid("clearSelections");
				$(this).combo("clear");
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).combogrid("options");
				if (opts.multiple) {
					$(this).combogrid("setValues", opts.originalValue);
				} else {
					$(this).combogrid("setValue", opts.originalValue);
				}
			});
		}
	};
	$.fn.combogrid.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.combo.parseOptions(target), $.fn.datagrid.parseOptions(target), jCocit.parseOptions(target, [ "idField", "textField", "mode" ]));
	};
	$.fn.combogrid.defaults = $.extend({}, $.fn.combo.defaults, $.fn.datagrid.defaults, {
		loadMsg : null,
		idField : 'id',
		textField : 'name',
		mode : "local",
		keyHandler : {
			doKey : function(key) {
				doKey(this, key);
			},
			doEnter : function() {
				doKey(this);
				$(this).combo("hidePanel");
			},
			doQuery : function(keyword) {
				doQuery(this, keyword);
			}
		},
		filter : function(keyword, row) {
			var opts = $(this).combogrid("options");
			return row[opts.textField].indexOf(keyword) == 0;
		}
	});
})(jQuery, jCocit);
