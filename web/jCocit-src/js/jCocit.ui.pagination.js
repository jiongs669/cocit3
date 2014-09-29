/**
 * 
 */
(function($, jCocit) {
	function _init(selfHTML) {
		var state = $d(selfHTML, "pagination");
		var opts = state.options;
		var bb = state.bb = {};
		var $self = $ac("pagination", $(selfHTML)).html('<table cellspacing="0" cellpadding="0" border="0"><tr></tr></table>');
		var tr = $f("tr", $self);

		function _initNav(navName) {
			var navOption = opts.nav[navName];
			var $btn = $('<a href="javascript:void(0)"></a>').appendTo(tr);
			$btn.wrap("<td></td>");
			$btn.button({
				iconCls : navOption.iconCls,
				plain : true
			}).unbind(".pagination").bind("click.pagination", function() {
				navOption.handler.call(selfHTML);
			});
			return $btn;
		}

		if (opts.showPageList) {
			var $pageSize = $('<select class="pagination-page-list"></select>');
			$pageSize.bind("change", function() {
				opts.pageSize = parseInt($(this).val());
				opts.onChangePageSize.call(selfHTML, opts.pageSize);
				select(selfHTML, opts.pageIndex);
			});
			for ( var i = 0; i < opts.pageOptions.length; i++) {
				$("<option></option>").text(opts.pageOptions[i]).appendTo($pageSize);
			}
			$("<td></td>").append($pageSize).appendTo(tr);
			$('<td><div class="pagination-btn-separator"></div></td>').appendTo(tr);
		}
		bb.first = _initNav("first");
		bb.prev = _initNav("prev");
		$('<td><div class="pagination-btn-separator"></div></td>').appendTo(tr);
		$('<span style="padding-left:6px;"></span>').html(opts.beforePageText).appendTo(tr).wrap("<td></td>");
		bb.num = $('<input class="pagination-num" type="text" value="1" size="2">').appendTo(tr).wrap("<td></td>");
		bb.num.unbind(".pagination").bind("keydown.pagination", function(e) {
			if (e.keyCode == 13) {
				var _9 = parseInt($(this).val()) || 1;
				select(selfHTML, _9);
				return false;
			}
		});
		bb.after = $('<span style="padding-right:6px;"></span>').appendTo(tr).wrap("<td></td>");
		$('<td><div class="pagination-btn-separator"></div></td>').appendTo(tr);
		bb.next = _initNav("next");
		bb.last = _initNav("last");
		if (opts.showRefresh) {
			$('<td><div class="pagination-btn-separator"></div></td>').appendTo(tr);
			bb.refresh = _initNav("refresh");
		}
		if (opts.buttons) {
			$('<td><div class="pagination-btn-separator"></div></td>').appendTo(tr);
			for ( var i = 0; i < opts.buttons.length; i++) {
				var btnOption = opts.buttons[i];
				if (btnOption == "-") {
					$('<td><div class="pagination-btn-separator"></div></td>').appendTo(tr);
				} else {
					var td = $("<td></td>").appendTo(tr);
					$('<a href="javascript:void(0)" title=\"' + btnOption.title + '\"></a>').appendTo(td).button($.extend(btnOption, {
						plain : true
					})).bind("click", $fn(btnOption.handler || function() {
					}));
				}
			}
		}
		$('<div class="pagination-info"></div>').appendTo($self);
		$('<div style="clear:both;"></div>').appendTo($self);
	}

	function select(selfHTML, pageIndex) {
		var opts = $d(selfHTML, "pagination").options;
		var maxPageIndex = Math.ceil(opts.total / opts.pageSize) || 1;
		opts.pageIndex = pageIndex;
		if (opts.pageIndex < 1) {
			opts.pageIndex = 1;
		}
		if (opts.pageIndex > maxPageIndex) {
			opts.pageIndex = maxPageIndex;
		}
		refresh(selfHTML, {
			pageIndex : opts.pageIndex
		});
		opts.onSelectPage.call(selfHTML, opts.pageIndex, opts.pageSize);
	}

	function refresh(selfHTML, options) {
		var opts = $d(selfHTML, "pagination").options;
		var bb = $d(selfHTML, "pagination").bb;
		$.extend(opts, options || {});
		var $pageSize = $f("select.pagination-page-list", $(selfHTML));
		if ($pageSize.length) {
			$pageSize.val(opts.pageSize + "");
			opts.pageSize = parseInt($pageSize.val());
		}
		var maxPageIndex = Math.ceil(opts.total / opts.pageSize) || 1;
		bb.num.val(opts.pageIndex);
		$w(10 + ("" + opts.pageIndex).length * 7, bb.num);
		bb.after.html(opts.afterPageText.replace(/{pages}/, maxPageIndex));
		var displayMsg = opts.displayMsg;
		displayMsg = displayMsg.replace(/{from}/, opts.total == 0 ? 0 : opts.pageSize * (opts.pageIndex - 1) + 1);
		displayMsg = displayMsg.replace(/{to}/, Math.min(opts.pageSize * (opts.pageIndex), opts.total));
		displayMsg = displayMsg.replace(/{total}/, opts.total);
		$f("div.pagination-info", $(selfHTML)).html(displayMsg);
		bb.first.add(bb.prev).button({
			disabled : (opts.pageIndex == 1)
		});
		bb.next.add(bb.last).button({
			disabled : (opts.pageIndex == maxPageIndex)
		});
		load(selfHTML, opts.loading);
	}

	function load(selfHTML, loading) {
		var opts = $d(selfHTML, "pagination").options;
		var bb = $d(selfHTML, "pagination").bb;
		opts.loading = loading;
		if (opts.showRefresh) {
			if (opts.loading) {
				bb.refresh.button({
					iconCls : "pagination-loading"
				});
			} else {
				bb.refresh.button({
					iconCls : "pagination-load"
				});
			}
		}
	}

	$.fn.pagination = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.pagination.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.pagination');
		}
		options = options || {};
		return this.each(function() {
			var opts;
			var state = $d(this, "pagination");
			if (state) {
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, $.fn.pagination.defaults, $.fn.pagination.parseOptions(this), options);
				$d(this, "pagination", {
					options : opts
				});
			}
			_init(this);
			refresh(this);
		});
	};

	$.fn.pagination.methods = {
		options : function(jq) {
			return $d(jq[0], "pagination").options;
		},
		loading : function(jq) {
			return jq.each(function() {
				load(this, true);
			});
		},
		loaded : function(jq) {
			return jq.each(function() {
				load(this, false);
			});
		},
		refresh : function(jq, _1e) {
			return jq.each(function() {
				refresh(this, _1e);
			});
		},
		select : function(jq, _1f) {
			return jq.each(function() {
				select(this, _1f);
			});
		}
	};

	$.fn.pagination.parseOptions = function(selfHTML) {
		var t = $(selfHTML);
		return $.extend({}, jCocit.parseOptions(selfHTML, [ {
			total : "n",
			pageSize : "n",
			pageIndex : "n"
		}, {
			loading : "b",
			showPageList : "b",
			showRefresh : "b"
		} ]), {
			pageOptions : (t.attr("pageOptions") ? $fn(t.attr("pageOptions")) : undefined)
		});
	};

	$.fn.pagination.defaults = {
		total : 1,
		pageSize : 10,
		pageIndex : 1,
		pageOptions : [ 10, 20, 30, 50 ],
		loading : false,
		buttons : null,
		showPageList : true,
		showRefresh : true,
		onSelectPage : function(pageIndex, pageSize) {
		},
		onBeforeRefresh : function(pageIndex, pageSize) {
		},
		onRefresh : function(pageIndex, pageSize) {
		},
		onChangePageSize : function(pageSize) {
		},
		beforePageText : "Page",
		afterPageText : "of {pages}",
		displayMsg : "Displaying {from} to {to} of {total} items",
		nav : {
			first : {
				iconCls : "pagination-first",
				handler : function() {
					var opts = $(this).pagination("options");
					if (opts.pageIndex > 1) {
						$(this).pagination("select", 1);
					}
				}
			},
			prev : {
				iconCls : "pagination-prev",
				handler : function() {
					var opts = $(this).pagination("options");
					if (opts.pageIndex > 1) {
						$(this).pagination("select", opts.pageIndex - 1);
					}
				}
			},
			next : {
				iconCls : "pagination-next",
				handler : function() {
					var opts = $(this).pagination("options");
					var maxPageIndex = Math.ceil(opts.total / opts.pageSize);
					if (opts.pageIndex < maxPageIndex) {
						$(this).pagination("select", opts.pageIndex + 1);
					}
				}
			},
			last : {
				iconCls : "pagination-last",
				handler : function() {
					var opts = $(this).pagination("options");
					var maxPageIndex = Math.ceil(opts.total / opts.pageSize);
					if (opts.pageIndex < maxPageIndex) {
						$(this).pagination("select", maxPageIndex);
					}
				}
			},
			refresh : {
				iconCls : "pagination-refresh",
				handler : function() {
					var opts = $(this).pagination("options");
					if (opts.onBeforeRefresh.call(this, opts.pageIndex, opts.pageSize) != false) {
						$(this).pagination("select", opts.pageIndex);
						opts.onRefresh.call(this, opts.pageIndex, opts.pageSize);
					}
				}
			}
		}
	};
})(jQuery, jCocit);
