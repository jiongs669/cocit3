/**
 * 
 */
(function($) {
	function _indexOfRow(rows, row) {
		for ( var i = 0, len = rows.length; i < len; i++) {
			if (rows[i] == row) {
				return i;
			}
		}
		return -1;
	}

	function _removeRow(rows, row) {
		var index = _indexOfRow(rows, row);
		if (index != -1) {
			rows.splice(index, 1);
		}
	}

	function _initDataGrid(gridTable) {
		var opts = $d(gridTable, "treegrid").options;

		$(gridTable).datagrid($.extend({}, opts, {
			url : null,
			data : null,
			loader : function() {
				return false;
			},
			onLoadSuccess : function() {
			},
			onResizeColumn : function(field, width) {
				fixRowHeight(gridTable);
				opts.onResizeColumn.call(gridTable, field, width);
			},
			onSortColumn : function(sortField, sortOrder) {
				opts.sortField = sortField;
				opts.sortOrder = sortOrder;
				if (opts.sortOnRemote) {
					_sortOnRemote(gridTable);
				} else {
					var data = $(gridTable).treegrid("getData");
					loadData(gridTable, 0, data);
				}
				opts.onSortColumn.call(gridTable, sortField, sortOrder);
			},
			onBeforeEdit : function(id, row) {
				if (opts.onBeforeEdit.call(gridTable, row) == false) {
					return false;
				}
			},
			onAfterEdit : function(id, row, changedRow) {
				opts.onAfterEdit.call(gridTable, row, changedRow);
			},
			onCancelEdit : function(id, row) {
				opts.onCancelEdit.call(gridTable, row);
			},
			onSelect : function(id) {
				opts.onSelect.call(gridTable, findNode(gridTable, id));
			},
			onUnselect : function(id) {
				opts.onUnselect.call(gridTable, findNode(gridTable, id));
			},
			onSelectAll : function() {
				opts.onSelectAll.call(gridTable, $d(gridTable, "treegrid").data);
			},
			onUnselectAll : function() {
				opts.onUnselectAll.call(gridTable, $d(gridTable, "treegrid").data);
			},
			onCheck : function(id) {
				opts.onCheck.call(gridTable, findNode(gridTable, id));
			},
			onUncheck : function(id) {
				opts.onUncheck.call(gridTable, findNode(gridTable, id));
			},
			onCheckAll : function() {
				opts.onCheckAll.call(gridTable, $d(gridTable, "treegrid").data);
			},
			onUncheckAll : function() {
				opts.onUncheckAll.call(gridTable, $d(gridTable, "treegrid").data);
			},
			onClickRow : function(id) {
				opts.onClickRow.call(gridTable, findNode(gridTable, id));
			},
			onDblClickRow : function(id) {
				opts.onDblClickRow.call(gridTable, findNode(gridTable, id));
			},
			onClickCell : function(id, field) {
				opts.onClickCell.call(gridTable, field, findNode(gridTable, id));
			},
			onDblClickCell : function(id, field) {
				opts.onDblClickCell.call(gridTable, field, findNode(gridTable, id));
			},
			onRowContextMenu : function(e, id) {
				opts.onContextMenu.call(gridTable, e, findNode(gridTable, id));
			}
		}));

		if (opts.pagination) {
			var $pager = $(gridTable).datagrid("getPager");
			$pager.pagination({
				pageIndex : opts.pageIndex,
				pageSize : opts.pageSize,
				pageOptions : opts.pageOptions,
				onSelectPage : function(pageIndex, pageSize) {
					opts.pageIndex = pageIndex;
					opts.pageSize = pageSize;
					_sortOnRemote(gridTable);
				}
			});
			opts.pageSize = $pager.pagination("options").pageSize;
		}
	}

	function fixRowHeight(gridTable, id) {
		var opts = $d(gridTable, "datagrid").options;
		var dc = $d(gridTable, "datagrid").dc;

		if (!dc.body1.is(":empty") && (!opts.nowrap || opts.autoRowHeight)) {
			if (id != undefined) {
				var rows = getChildren(gridTable, id);
				for ( var i = 0; i < rows.length; i++) {
					_fixRowHeight(rows[i][opts.idField]);
				}
			}
		}

		$(gridTable).datagrid("fixRowHeight", id);

		function _fixRowHeight(id) {
			var tr1 = opts.finder.getTr(gridTable, id, "body", 1);
			var tr2 = opts.finder.getTr(gridTable, id, "body", 2);
			tr1.css("height", "");
			tr2.css("height", "");
			var maxHeight = Math.max($h(tr1), $h(tr2));
			tr1.css("height", maxHeight);
			tr2.css("height", maxHeight);
		}

	}

	function _initDataGridRowNumbers(gridTable) {
		var dc = $d(gridTable, "datagrid").dc;
		var opts = $d(gridTable, "treegrid").options;
		if (!opts.rownumbers) {
			return;
		}
		$f("div.datagrid-cell-rownumber", dc.body1).each(function(i) {
			$(this).html(i + 1);
		});
	}

	function _bindBodyEvents(gridTable) {
		var dc = $d(gridTable, "datagrid").dc;
		var $body = dc.body1.add(dc.body2);
		var handler = ($d($body[0], "events") || $._data($body[0], "events")).click[0].handler;

		$body.bind("mouseover", function(e) {
			var tt = $(e.target);
			var tr = $l("tr.datagrid-row", tt);
			if (!tr.length) {
				return;
			}
			if ($hc("TrH", tt)) {
				$hc("TrH-E", tt) ? $ac("TrH-EH", tt) : $ac("TrH-CH", tt);
			}
			e.stopPropagation();
		}).bind("mouseout", function(e) {
			var tt = $(e.target);
			var tr = $l("tr.datagrid-row", tt);
			if (!tr.length) {
				return;
			}
			if ($hc("TrH", tt)) {
				$hc("TrH-E", tt) ? $rc("TrH-EH", tt) : $rc("TrH-CH", tt);
			}
			e.stopPropagation();
		}).unbind("click").bind("click", function(e) {
			var tt = $(e.target);
			var tr = $l("tr.datagrid-row", tt);
			if (!tr.length) {
				return;
			}
			if ($hc("TrH", tt)) {
				toggle(gridTable, tr.attr("node-id"));
			} else {
				handler(e);
			}
			e.stopPropagation();
		});
	}

	function _insertRowAfter(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var tr1 = opts.finder.getTr(gridTable, id, "body", 1);
		var tr2 = opts.finder.getTr(gridTable, id, "body", 2);
		var frozenFieldSize = $(gridTable).datagrid("getColumnFields", true).length + (opts.rownumbers ? 1 : 0);
		var fieldSize = $(gridTable).datagrid("getColumnFields", false).length;

		_insertTrAfter(tr1, frozenFieldSize);
		_insertTrAfter(tr2, fieldSize);

		function _insertTrAfter(tr, fieldSize) {
			$('<tr class="treegrid-tr-tree"><td style="border:0px" colspan="' + fieldSize + '"><div></div></td></tr>').insertAfter(tr);
		}

	}

	function loadData(gridTable, id, treeNodes, append) {
		var opts = $d(gridTable, "treegrid").options;
		var dc = $d(gridTable, "datagrid").dc;

		treeNodes = opts.loadFilter.call(gridTable, treeNodes, id);
		var node = findNode(gridTable, id);

		if (node) {
			var $tr1 = opts.finder.getTr(gridTable, id, "body", 1);
			var $tr2 = opts.finder.getTr(gridTable, id, "body", 2);
			var $body1 = $c("div", $c("td", $tr1.next("tr.treegrid-tr-tree")));
			var $body2 = $c("div", $c("td", $tr2.next("tr.treegrid-tr-tree")));
		} else {
			var $body1 = dc.body1;
			var $body2 = dc.body2;
		}

		if (!append) {
			$d(gridTable, "treegrid").data = [];
			$body1.empty();
			$body2.empty();
		}

		if (opts.view.onBeforeRender) {
			opts.view.onBeforeRender.call(opts.view, gridTable, id, treeNodes);
		}

		opts.view.render.call(opts.view, gridTable, $body1, true);
		opts.view.render.call(opts.view, gridTable, $body2, false);
		if (opts.showFooter) {
			opts.view.renderFooter.call(opts.view, gridTable, dc.footer1, true);
			opts.view.renderFooter.call(opts.view, gridTable, dc.footer2, false);
		}
		if (opts.view.onAfterRender) {
			opts.view.onAfterRender.call(opts.view, gridTable);
		}

		opts.onLoadSuccess.call(gridTable, node, treeNodes);
		if (!id && opts.pagination) {
			var total = $d(gridTable, "treegrid").total;
			var $pager = $(gridTable).datagrid("getPager");
			if ($pager.pagination("options").total != total) {
				$pager.pagination({
					total : total
				});
			}
		}

		fixRowHeight(gridTable);
		_initDataGridRowNumbers(gridTable);
		$(gridTable).treegrid("autoSizeColumn");
	}

	function _sortOnRemote(gridTable, id, queryParams, _49, _4a) {
		var opts = $d(gridTable, "treegrid").options;
		var $body = $f("div.datagrid-body", $(gridTable).datagrid("getPanel"));
		if (queryParams) {
			opts.queryParams = queryParams;
		}
		var queryParameters = $.extend({}, opts.queryParams);
		if (opts.pagination) {
			$.extend(queryParameters, {
				page : opts.pageIndex,
				rows : opts.pageSize
			});
		}
		if (opts.sortField) {
			$.extend(queryParameters, {
				sortField : opts.sortField,
				sortOrder : opts.sortOrder
			});
		}
		var row = findNode(gridTable, id);
		if (opts.onBeforeLoad.call(gridTable, row, queryParameters) == false) {
			return;
		}
		var $treeFolder = $f("tr[node-id=" + id + "] span.TrI-F", $body);
		$ac("TrL", $treeFolder);
		$(gridTable).treegrid("loading");

		var isLoading = opts.loader.call(gridTable, queryParameters, function(treeNodes) {
			$rc("TrL", $treeFolder);
			$(gridTable).treegrid("loaded");
			loadData(gridTable, id, treeNodes, _49);
			if (_4a) {
				_4a();
			}
		}, function() {
			$rc("TrL", $treeFolder);
			$(gridTable).treegrid("loaded");
			opts.onLoadError.apply(gridTable, arguments);
			if (_4a) {
				_4a();
			}
		});
		if (isLoading == false) {
			$rc("TrL", $treeFolder);
			$(gridTable).treegrid("loaded");
		}
	}

	function getRoot(gridTable) {
		var roots = getRoots(gridTable);
		if (roots.length) {
			return roots[0];
		} else {
			return null;
		}
	}

	function getRoots(gridTable) {
		return $d(gridTable, "treegrid").data;
	}

	function getParent(gridTable, id) {
		var row = findNode(gridTable, id);
		if (row._parentId) {
			return findNode(gridTable, row._parentId);
		} else {
			return null;
		}
	}

	function getChildren(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var $body2 = $f("div.datagrid-view2 div.datagrid-body", $(gridTable).datagrid("getPanel"));

		var childrenData = [];

		if (id) {
			_pushChild(id);
		} else {
			var rootsData = getRoots(gridTable);
			for ( var i = 0; i < rootsData.length; i++) {
				childrenData.push(rootsData[i]);
				_pushChild(rootsData[i][opts.idField]);
			}
		}

		function _pushChild(id) {
			var node = findNode(gridTable, id);
			if (node && node.children) {
				for ( var i = 0, len = node.children.length; i < len; i++) {
					var child = node.children[i];
					childrenData.push(child);
					_pushChild(child[opts.idField]);
				}
			}
		}

		return childrenData;
	}

	function getSelected(gridTable) {
		var nodes = getSelections(gridTable);
		if (nodes.length) {
			return nodes[0];
		} else {
			return null;
		}
	}

	function getSelections(gridTable) {
		var nodes = [];
		var $panelContent = $(gridTable).datagrid("getPanel");
		$f("div.datagrid-view2 div.datagrid-body tr.datagrid-row-selected", $panelContent).each(function() {
			var id = $(this).attr("node-id");
			nodes.push(findNode(gridTable, id));
		});
		return nodes;
	}

	function getLevel(gridTable, id) {
		if (!id) {
			return 0;
		}
		var opts = $d(gridTable, "treegrid").options;
		var $view = $c("div.datagrid-view", $(gridTable).datagrid("getPanel"));
		var $treeField = $c("td[field=" + opts.treeField + "]", $f("div.datagrid-body tr[node-id=" + id + "]", $view));
		return $f("span.TrD,span.TrH", $treeField).length;
	}

	function findNode(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var data = $d(gridTable, "treegrid").data;
		var arrayData = [ data ];
		while (arrayData.length) {
			var rows = arrayData.shift();
			for ( var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (row[opts.idField] == id) {
					return row;
				} else {
					if (row["children"]) {
						arrayData.push(row["children"]);
					}
				}
			}
		}
		return null;
	}

	function collapse(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var row = findNode(gridTable, id);
		var tr = opts.finder.getTr(gridTable, id);
		var hit = $f("span.TrH", tr);
		if (hit.length == 0) {
			return;
		}
		if ($hc("TrH-C", hit)) {
			return;
		}
		if (opts.onBeforeCollapse.call(gridTable, row) == false) {
			return;
		}
		$ac("TrH-C", $rc("TrH-E TrH-EH", hit));
		$rc("TrI-FO", hit.next());
		row.state = "closed";
		tr = tr.next("tr.treegrid-tr-tree");
		var cc = $c("div", $c("td", tr));
		if (opts.animate) {
			cc.slideUp("normal", function() {
				$(gridTable).treegrid("autoSizeColumn");
				fixRowHeight(gridTable, id);
				opts.onCollapse.call(gridTable, row);
			});
		} else {
			cc.hide();
			$(gridTable).treegrid("autoSizeColumn");
			fixRowHeight(gridTable, id);
			opts.onCollapse.call(gridTable, row);
		}
	}

	function expand(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var tr = opts.finder.getTr(gridTable, id);
		var hit = $f("span.TrH", tr);
		var row = findNode(gridTable, id);
		if (hit.length == 0) {
			return;
		}
		if ($hc("TrH-E", hit)) {
			return;
		}
		if (opts.onBeforeExpand.call(gridTable, row) == false) {
			return;
		}
		$ac("TrH-E", $rc("TrH-C TrH-CH", hit));
		$ac("TrI-FO", hit.next());
		var $subtree = tr.next("tr.treegrid-tr-tree");
		if ($subtree.length) {
			var $subtreeGrid = $c("div", $c("td", $subtree));
			_showSubtreeGrid($subtreeGrid);
		} else {
			_insertRowAfter(gridTable, row[opts.idField]);
			var $subtree = tr.next("tr.treegrid-tr-tree");
			var $subtreeGrid = $c("div", $c("td", $subtree));
			$subtreeGrid.hide();
			_sortOnRemote(gridTable, row[opts.idField], {
				id : row[opts.idField]
			}, true, function() {
				if ($subtreeGrid.is(":empty")) {
					$subtree.remove();
				} else {
					_showSubtreeGrid($subtreeGrid);
				}
			});
		}

		function _showSubtreeGrid($subtreeGrid) {
			row.state = "open";
			if (opts.animate) {
				$subtreeGrid.slideDown("normal", function() {
					$(gridTable).treegrid("autoSizeColumn");
					fixRowHeight(gridTable, id);
					opts.onExpand.call(gridTable, row);
				});
			} else {
				$subtreeGrid.show();
				$(gridTable).treegrid("autoSizeColumn");
				fixRowHeight(gridTable, id);
				opts.onExpand.call(gridTable, row);
			}
		}

	}

	function toggle(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var tr = opts.finder.getTr(gridTable, id);
		var hit = $f("span.TrH", tr);
		if ($hc("TrH-E", hit)) {
			collapse(gridTable, id);
		} else {
			expand(gridTable, id);
		}
	}

	function collapseAll(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var rows = getChildren(gridTable, id);
		if (id) {
			rows.unshift(findNode(gridTable, id));
		}
		for ( var i = 0; i < rows.length; i++) {
			collapse(gridTable, rows[i][opts.idField]);
		}
	}

	function expandAll(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var rows = getChildren(gridTable, id);
		if (id) {
			rows.unshift(findNode(gridTable, id));
		}
		for ( var i = 0; i < rows.length; i++) {
			expand(gridTable, rows[i][opts.idField]);
		}
	}

	function expandTo(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var ids = [];
		var p = getParent(gridTable, id);
		while (p) {
			var id = p[opts.idField];
			ids.unshift(id);
			p = getParent(gridTable, id);
		}
		for ( var i = 0; i < ids.length; i++) {
			expand(gridTable, ids[i]);
		}
	}

	function appendNode(gridTable, nodeConfig) {
		var opts = $d(gridTable, "treegrid").options;
		if (nodeConfig.parent) {
			var tr = opts.finder.getTr(gridTable, nodeConfig.parent);
			if (tr.next("tr.treegrid-tr-tree").length == 0) {
				_insertRowAfter(gridTable, nodeConfig.parent);
			}
			var $node = $c("div.datagrid-cell", $c("td[field=" + opts.treeField + "]", tr));
			var $nodeIcon = $c("span.TrI", $node);
			if ($hc("TrI-L", $nodeIcon)) {
				$ac("TrI-F", $rc("TrI-L", $nodeIcon));
				var hit = $('<span class="TrH TrH-E"></span>').insertBefore($nodeIcon);
				if (hit.prev().length) {
					hit.prev().remove();
				}
			}
		}
		loadData(gridTable, nodeConfig.parent, nodeConfig.data, true);
	}

	function insertNode(gridTable, nodeConfig) {
		var ref = nodeConfig.before || nodeConfig.after;
		var opts = $d(gridTable, "treegrid").options;
		var parent = getParent(gridTable, ref);
		appendNode(gridTable, {
			parent : (parent ? parent[opts.idField] : null),
			data : [ nodeConfig.data ]
		});

		_9b(1);
		_9b(2);
		_initDataGridRowNumbers(gridTable);

		function _9b(viewIndex) {
			var $tr = opts.finder.getTr(gridTable, nodeConfig.data[opts.idField], "body", viewIndex);
			var $bodyTable = $l("table.datagrid-btable", $tr);
			$tr = $c($p($tr));
			var $refTr = opts.finder.getTr(gridTable, ref, "body", viewIndex);
			if (nodeConfig.before) {
				$tr.insertBefore($refTr);
			} else {
				var sub = $refTr.next("tr.treegrid-tr-tree");
				$tr.insertAfter(sub.length ? sub : $refTr);
			}
			$bodyTable.remove();
		}

	}

	function removeNode(gridTable, id) {
		var opts = $d(gridTable, "treegrid").options;
		var tr = opts.finder.getTr(gridTable, id);
		tr.next("tr.treegrid-tr-tree").remove();
		tr.remove();
		var parent = del(id);
		if (parent) {
			if (parent.children.length == 0) {
				tr = opts.finder.getTr(gridTable, parent[opts.idField]);
				tr.next("tr.treegrid-tr-tree").remove();
				var $node = $c("div.datagrid-cell", $c("td[field=" + opts.treeField + "]", tr));
				$ac("TrI-L", $rc("TrI-F", $f(".TrI", $node)));
				$f(".TrH", $node).remove();
				$('<span class="TrD"></span>').prependTo($node);
			}
		}
		_initDataGridRowNumbers(gridTable);

		function del(id) {
			var rows;
			var parent = getParent(gridTable, id);
			if (parent) {
				rows = parent.children;
			} else {
				rows = $(gridTable).treegrid("getData");
			}
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i][opts.idField] == id) {
					rows.splice(i, 1);
					break;
				}
			}
			return parent;
		}

	}

	$.fn.treegrid = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.treegrid.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.datagrid(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "treegrid");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "treegrid", {
					options : $.extend({}, $.fn.treegrid.defaults, $.fn.treegrid.parseOptions(this), options),
					data : []
				});
			}
			_initDataGrid(this);
			if (state.options.data) {
				$(this).treegrid("loadData", state.options.data);
			}
			_sortOnRemote(this);
			_bindBodyEvents(this);
		});
	};

	$.fn.treegrid.methods = {
		options : function(jq) {
			return $d(jq[0], "treegrid").options;
		},
		// resize : function(jq, size) {
		// return jq.each(function() {
		// $(this).datagrid("resize", size);
		// });
		// },
		fixRowHeight : $X(fixRowHeight),
		loadData : function(jq, data) {
			return jq.each(function() {
				loadData(this, null, data);
			});
		},
		reload : function(jq, id) {
			return jq.each(function() {
				if (id) {
					var node = $(this).treegrid("find", id);
					if (node.children) {
						node.children.splice(0, node.children.length);
					}
					var $body = $f("div.datagrid-body", $(this).datagrid("getPanel"));
					var tr = $f("tr[node-id=" + id + "]", $body);
					tr.next("tr.treegrid-tr-tree").remove();
					var hit = $f("span.TrH", tr);
					$ac("TrH-C", $rc("TrH-E TrH-EH", hit));
					expand(this, id);
				} else {
					_sortOnRemote(this, null, {});
				}
			});
		},
		reloadFooter : function(jq, footerData) {
			return jq.each(function() {
				var opts = $d(this, "treegrid").options;
				var dc = $d(this, "datagrid").dc;
				if (footerData) {
					$d(this, "treegrid").footer = footerData;
				}
				if (opts.showFooter) {
					opts.view.renderFooter.call(opts.view, this, dc.footer1, true);
					opts.view.renderFooter.call(opts.view, this, dc.footer2, false);
					if (opts.view.onAfterRender) {
						opts.view.onAfterRender.call(opts.view, this);
					}
					$(this).treegrid("fixRowHeight");
				}
			});
		},
		getData : function(jq) {
			return $d(jq[0], "treegrid").data;
		},
		getFooterRows : function(jq) {
			return $d(jq[0], "treegrid").footer;
		},
		getRoot : $x(getRoot),
		getRoots : $x(getRoots),
		getParent : $x(getParent),
		getChildren : $x(getChildren),
		getSelected : $x(getSelected),
		getSelections : $x(getSelections),
		getLevel : $x(getLevel),
		find : $x(findNode),
		isLeaf : function(jq, id) {
			var opts = $d(jq[0], "treegrid").options;
			var tr = opts.finder.getTr(jq[0], id);
			var hit = $f("span.TrH", tr);
			return hit.length == 0;
		},
		// select : function(jq, id) {
		// return jq.each(function() {
		// $(this).datagrid("selectRow", id);
		// });
		// },
		// unselect : function(jq, id) {
		// return jq.each(function() {
		// $(this).datagrid("unselectRow", id);
		// });
		// },
		collapse : $X(collapse),
		expand : $X(expand),
		toggle : $X(toggle),
		collapseAll : $X(collapseAll),
		expandAll : $X(expandAll),
		expandTo : $X(expandTo),
		append : $X(appendNode),
		insert : $X(insertNode),
		remove : $X(removeNode),
		pop : function(jq, id) {
			var row = jq.treegrid("find", id);
			jq.treegrid("remove", id);
			return row;
		},
		refresh : function(jq, id) {
			return jq.each(function() {
				var opts = $d(this, "treegrid").options;
				opts.view.refreshRow.call(opts.view, this, id);
			});
		},
		update : function(jq, rowConfig) {
			return jq.each(function() {
				var opts = $d(this, "treegrid").options;
				opts.view.updateRow.call(opts.view, this, rowConfig.id, rowConfig.row);
			});
		},
		beginEdit : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("beginEdit", id);
				$(this).treegrid("fixRowHeight", id);
			});
		}
	// , endEdit : function(jq, id) {
	// return jq.each(function() {
	// $(this).datagrid("endEdit", id);
	// });
	// },
	// cancelEdit : function(jq, id) {
	// return jq.each(function() {
	// $(this).datagrid("cancelEdit", id);
	// });
	// }
	};

	$.fn.treegrid.parseOptions = function(gridTable) {
		return $.extend({}, $.fn.datagrid.parseOptions(gridTable), jCocit.parseOptions(gridTable, [ "treeField", {
			animate : "b"
		} ]));
	};

	var viewRender = $.extend({}, $.fn.datagrid.defaults.view, {
		render : function(gridTable, $body, frozen) {
			var opts = $d(gridTable, "treegrid").options;
			var columnFields = $(gridTable).datagrid("getColumnFields", frozen);
			var rowIdPrefix = $d(gridTable, "datagrid").rowIdPrefix;
			if (frozen) {
				if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))) {
					return;
				}
			}
			var self = this;
			var arrayHtml = _makeHtml(frozen, this.treeLevel, this.treeNodes);
			$($body).append(arrayHtml.join(""));

			function _makeHtml(frozen, treeLevel, treeNodes) {
				var html = [ '<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>' ];
				for ( var i = 0; i < treeNodes.length; i++) {
					var row = treeNodes[i];
					if (row.state != "open" && row.state != "closed") {
						row.state = "open";
					}
					var rowStyle = opts.rowStyler ? opts.rowStyler.call(gridTable, row) : "";
					var styleAttr = rowStyle ? 'style="' + rowStyle + '"' : '';
					var rowId = rowIdPrefix + "-" + (frozen ? 1 : 2) + "-" + row[opts.idField];
					html.push('<tr id="' + rowId + '" class="datagrid-row" node-id=' + row[opts.idField] + ' ' + styleAttr + '>');
					html = html.concat(self.renderRow.call(self, gridTable, columnFields, frozen, treeLevel, row));
					html.push("</tr>");
					if (row.children && row.children.length) {
						var tt = _makeHtml(frozen, treeLevel + 1, row.children);
						var v = row.state == "closed" ? "none" : "block";
						html.push('<tr class="treegrid-tr-tree"><td style="border:0px" colspan=' + (columnFields.length + (opts.rownumbers ? 1 : 0)) + '><div style="display:' + v + '">');
						html = html.concat(tt);
						html.push("</div></td></tr>");
					}
				}
				html.push("</tbody></table>");
				return html;
			}

		},
		renderFooter : function(gridTable, $footer, frozen) {
			var opts = $d(gridTable, "treegrid").options;
			var footerData = $d(gridTable, "treegrid").footer || [];
			var columnFields = $(gridTable).datagrid("getColumnFields", frozen);
			var htmlArray = [ '<table class="datagrid-ftable" cellspacing="0" cellpadding="0" border="0"><tbody>' ];
			for ( var i = 0; i < footerData.length; i++) {
				var row = footerData[i];
				row[opts.idField] = row[opts.idField] || ("foot-row-id" + i);
				htmlArray.push('<tr class="datagrid-row" node-id=' + row[opts.idField] + '>');
				htmlArray.push(this.renderRow.call(this, gridTable, columnFields, frozen, 0, row));
				htmlArray.push("</tr>");
			}
			htmlArray.push("</tbody></table>");
			$($footer).html(htmlArray.join(""));
		},
		renderRow : function(gridTable, fields, rownumbers, _d4, row) {
			var opts = $d(gridTable, "treegrid").options;
			var html = [];
			if (rownumbers && opts.rownumbers) {
				html.push('<td class="datagrid-td-rownumber"><div class="datagrid-cell-rownumber">0</div></td>');
			}
			for ( var i = 0; i < fields.length; i++) {
				var field = fields[i];
				var col = $(gridTable).datagrid("getColumnOption", field);
				if (col) {
					var styleText = col.styler ? (col.styler(row[field], row) || "") : "";
					var styleAttr = col.hidden ? 'style="display:none;' + styleText + '"' : (styleText ? 'style="' + styleText + '"' : '');
					html.push('<td field="' + field + '" ' + styleAttr + '>');
					if (col.checkbox) {
						var styleAttr = "";
					} else {
						var styleAttr = "";
						if (col.align) {
							styleAttr += "text-align:" + col.align + ";";
						}
						if (!opts.nowrap) {
							styleAttr += "white-space:normal;height:auto;";
						} else {
							if (opts.autoRowHeight) {
								styleAttr += "height:auto;";
							}
						}
					}
					html.push('<div style="' + styleAttr + '" ');
					if (col.checkbox) {
						html.push('class="datagrid-cell-check ');
					} else {
						html.push('class="datagrid-cell ' + col.cellClass);
					}
					html.push('">');
					if (col.checkbox) {
						if (row.checked) {
							html.push('<input type="checkbox" checked="checked"');
						} else {
							html.push('<input type="checkbox"');
						}
						html.push(' name="' + field + '" value="' + (row[field] != undefined ? row[field] : '') + '"/>');
					} else {
						var val = null;
						if (col.formatter) {
							val = col.formatter(row[field], row);
						} else {
							val = row[field];
						}
						if (field == opts.treeField) {
							for ( var j = 0; j < _d4; j++) {
								html.push('<span class="TrD"></span>');
							}
							if (row.state == "closed") {
								html.push('<span class="TrH TrH-C"></span>');
								html.push('<span class="TrI TrI-F ' + (row.iconCls ? row.iconCls : '') + '"></span>');
							} else {
								if (row.children && row.children.length) {
									html.push('<span class="TrH TrH-E"></span>');
									html.push('<span class="TrI TrI-F TrI-FO ' + (row.iconCls ? row.iconCls : '') + '"></span>');
								} else {
									html.push('<span class="TrD"></span>');
									html.push('<span class="TrI TrI-L ' + (row.iconCls ? row.iconCls : '') + '"></span>');
								}
							}
							html.push('<span class="TrT">' + val + '</span>');
						} else {
							html.push(val);
						}
					}
					html.push("</div>");
					html.push("</td>");
				}
			}
			return html.join("");
		},
		refreshRow : function(gridTable, id) {
			this.updateRow.call(this, gridTable, id, {});
		},
		updateRow : function(gridTable, id, row) {
			var opts = $d(gridTable, "treegrid").options;
			var node = $(gridTable).treegrid("find", id);
			$.extend(node, row);
			var level = $(gridTable).treegrid("getLevel", id) - 1;
			var styleText = opts.rowStyler ? opts.rowStyler.call(gridTable, node) : "";

			function _updateRowNumberCheckbox(frozen) {
				var columnFields = $(gridTable).treegrid("getColumnFields", frozen);
				var tr = opts.finder.getTr(gridTable, id, "body", (frozen ? 1 : 2));
				var rowNumber = $f("div.datagrid-cell-rownumber", tr).html();
				var checked = $f("div.datagrid-cell-check input[type=checkbox]", tr).is(":checked");
				tr.html(this.renderRow(gridTable, columnFields, frozen, level, node));
				tr.attr("style", styleText || "");
				$f("div.datagrid-cell-rownumber", tr).html(rowNumber);
				if (checked) {
					$f("div.datagrid-cell-check input[type=checkbox]", tr)._attr("checked", true);
				}
			}

			_updateRowNumberCheckbox.call(this, true);
			_updateRowNumberCheckbox.call(this, false);
			$(gridTable).treegrid("fixRowHeight", id);
		},
		onBeforeRender : function(gridTable, id, treeNodes) {
			if (!treeNodes) {
				return false;
			}
			var opts = $d(gridTable, "treegrid").options;
			if (treeNodes.length == undefined) {
				if (treeNodes.footer) {
					$d(gridTable, "treegrid").footer = treeNodes.footer;
				}
				if (treeNodes.total) {
					$d(gridTable, "treegrid").total = treeNodes.total;
				}
				treeNodes = this.transfer(gridTable, id, treeNodes.rows);
			} else {
				function _setParent(rows, parentId) {
					for ( var i = 0; i < rows.length; i++) {
						var row = rows[i];
						row._parentId = parentId;
						if (row.children && row.children.length) {
							_setParent(row.children, row[opts.idField]);
						}
					}
				}

				_setParent(treeNodes, id);
			}
			var node = findNode(gridTable, id);
			if (node) {
				if (node.children) {
					node.children = node.children.concat(treeNodes);
				} else {
					node.children = treeNodes;
				}
			} else {
				$d(gridTable, "treegrid").data = $d(gridTable, "treegrid").data.concat(treeNodes);
			}
			if (!opts.sortOnRemote) {
				this.sort(gridTable, treeNodes);
			}
			this.treeNodes = treeNodes;
			this.treeLevel = $(gridTable).treegrid("getLevel", id);
		},
		sort : function(gridTable, treeNodes) {
			var opts = $d(gridTable, "treegrid").options;
			var opt = $(gridTable).treegrid("getColumnOption", opts.sortField);
			if (opt) {
				var sorter = opt.sorter || function(a, b) {
					return (a > b ? 1 : -1);
				};
				_sort(treeNodes);
			}
			function _sort(rows) {
				rows.sort(function(r1, r2) {
					return sorter(r1[opts.sortField], r2[opts.sortField]) * (opts.sortOrder == "asc" ? 1 : -1);
				});
				for ( var i = 0; i < rows.length; i++) {
					var children = rows[i].children;
					if (children && children.length) {
						_sort(children);
					}
				}
			}

		},
		transfer : function(gridTable, id, rows) {
			var opts = $d(gridTable, "treegrid").options;
			var tmpRows = [];
			for ( var i = 0; i < rows.length; i++) {
				tmpRows.push(rows[i]);
			}
			var rootRows = [];
			for ( var i = 0; i < tmpRows.length; i++) {
				var row = tmpRows[i];
				if (!id) {
					if (!row._parentId) {
						rootRows.push(row);
						_removeRow(tmpRows, row);
						i--;
					}
				} else {
					if (row._parentId == id) {
						rootRows.push(row);
						_removeRow(tmpRows, row);
						i--;
					}
				}
			}
			var tmpRootRows = [];
			for ( var i = 0; i < rootRows.length; i++) {
				tmpRootRows.push(rootRows[i]);
			}
			while (tmpRootRows.length) {
				var rootRow = tmpRootRows.shift();
				for ( var i = 0; i < tmpRows.length; i++) {
					var row = tmpRows[i];
					if (row._parentId == rootRow[opts.idField]) {
						if (rootRow.children) {
							rootRow.children.push(row);
						} else {
							rootRow.children = [ row ];
						}
						tmpRootRows.push(row);
						_removeRow(tmpRows, row);
						i--;
					}
				}
			}
			return rootRows;
		}
	});

	$.fn.treegrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		treeField : null,
		animate : false,
		singleSelect : true,
		view : viewRender,
		loader : function(queryParams, doLoadSuccess, doLoadError) {
			var opts = $(this).treegrid("options");
			if (!opts.url) {
				return false;
			}
			$.doAjax({
				type : opts.method,
				url : opts.url,
				data : queryParams,
				dataType : "json",
				success : function(data) {
					doLoadSuccess(data);
				},
				error : function() {
					doLoadError.apply(this, arguments);
				}
			});
		},
		loadFilter : function(data, id) {
			return data;
		},
		finder : {
			getTr : function(gridTable, id, type, viewIndex) {
				type = type || "body";
				viewIndex = viewIndex || 0;
				var dc = $d(gridTable, "datagrid").dc;
				if (viewIndex == 0) {
					var opts = $d(gridTable, "treegrid").options;
					var tr1 = opts.finder.getTr(gridTable, id, type, 1);
					var tr2 = opts.finder.getTr(gridTable, id, type, 2);
					return tr1.add(tr2);
				} else if (type == "body") {
					var tr = $("#" + $d(gridTable, "datagrid").rowIdPrefix + "-" + viewIndex + "-" + id);
					if (!tr.length) {
						tr = $f("tr[node-id=" + id + "]", (viewIndex == 1 ? dc.body1 : dc.body2));
					}
					return tr;
				} else if (type == "footer")
					return $f("tr[node-id=" + id + "]", (viewIndex == 1 ? dc.footer1 : dc.footer2));
				else if (type == "selected")
					return $f("tr.datagrid-row-selected", (viewIndex == 1 ? dc.body1 : dc.body2));
				else if (type == "last")
					return $f("tr:last[node-id]", (viewIndex == 1 ? dc.body1 : dc.body2));
				else if (type == "allbody")
					return $f("tr[node-id]", (viewIndex == 1 ? dc.body1 : dc.body2));
				else if (type == "allfooter")
					return $f("tr[node-id]", (viewIndex == 1 ? dc.footer1 : dc.footer2));

			},
			getRow : function(gridTable, p) {
				var id = (typeof p == "object") ? p.attr("node-id") : p;
				return $(gridTable).treegrid("find", id);
			}
		},
		/**
		 * arg: row , queryParams-
		 */
		onBeforeLoad : $n,
		/**
		 * arg: row , treeNodes-
		 */
		onLoadSuccess : $n,
		onLoadError : $n,
		/**
		 * arg: row -
		 */
		onBeforeCollapse : $n,
		/**
		 * arg: row -
		 */
		onCollapse : $n,
		/**
		 * arg: row -
		 */
		onBeforeExpand : $n,
		/**
		 * arg: row -
		 */
		onExpand : $n,
		/**
		 * arg: row -
		 */
		onClickRow : $n,
		/**
		 * arg: row -
		 */
		onDblClickRow : $n,
		/**
		 * arg: field, row -
		 */
		onClickCell : $n,
		/**
		 * arg: field, row -
		 */
		onDblClickCell : $n,
		/**
		 * arg: e, row -
		 */
		onContextMenu : $n,
		/**
		 * arg: row -
		 */
		onBeforeEdit : $n,
		/**
		 * args: row, changedRow
		 */
		onAfterEdit : $n,
		/**
		 * arg: row -
		 */
		onCancelEdit : $n
	});
})(jQuery);
