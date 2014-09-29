/**
 * <pre>
 * 	[div class='tabs-container']
 * 		[div class='tabs-header']
 * 			[div class='tabs-scroller-left'][/div]
 * 			[div class='tabs-scroller-right'][/div]
 * 			[div class='tabs-wrap']
 * 				[ul class='tabs'][/ul]
 * 			[/div]
 * 		[/div]
 * 		[div class='tabs-panels']
 * 			[div class='Pn']...[/div]
 * 		[/div]
 * 	[/div]
 * </pre>
 * 
 * <UL>
 * <LI>tabs: this is root DIV element. is tabs container, has 2 children, one is tabs header, other is tabs panels.
 * <LI>tabs header: this is tabs header.
 * <LI>tab header: this is tab item header.
 * <LI>tabs list: this is UL element, they are collection of all tabs.
 * <LI>tab item: this is LI element, single tab item.
 * <LI>tabs tool: this is tabs tool bar, contains all tabs tool buttons.
 * <LI>tab tool: this is tab item tool bar, contains all tab item tool buttons.
 * <LI>tabs panels: this is DIV element, panels container. it's children are all tab item panel.
 * <LI>tab panel: this is DIV element, tab item panel, tab item content.
 * </UL>
 */
(function($, jCocit) {
	function _initScroller(targetDIV) {
		var opts = $d(targetDIV, "tabs").options;
		if (opts.tabPosition == "left" || opts.tabPosition == "right") {
			return;
		}
		var $tabsHeader = $c("div.tabs-header", $(targetDIV));
		var $tabsTool = $c("div.tabs-tool", $tabsHeader);
		var $scrollerLeft = $c("div.tabs-scroller-left", $tabsHeader);
		var $scrollerRight = $c("div.tabs-scroller-right", $tabsHeader);
		var $tabsWrap = $c("div.tabs-wrap", $tabsHeader);
		$tabsTool.oh($oh($tabsHeader) - (opts.plain ? 2 : 0));
		var tabsTotalWidth = 0;
		$("ul.tabs li", $tabsHeader).each(function() {
			tabsTotalWidth += $ow(true, $(this));
		});
		var tabsUsableWidth = $w($tabsHeader) - $tabsTool.ow();

		// add scroller
		if (tabsTotalWidth > tabsUsableWidth) {
			$scrollerLeft.show();
			$scrollerRight.show();
			if (opts.toolPosition == "left") {
				$tabsTool.css({
					left : $ow($scrollerLeft),
					right : ""
				});
				$tabsWrap.css({
					marginLeft : $ow($scrollerLeft) + $tabsTool.ow(),
					marginRight : $scrollerRight.ow(),
					width : tabsUsableWidth - $ow($scrollerLeft) - $ow($scrollerRight)
				});
			} else {
				$tabsTool.css({
					left : "",
					right : $ow($scrollerRight)
				});
				$tabsWrap.css({
					marginLeft : $ow($scrollerLeft),
					marginRight : $ow($scrollerRight) + $tabsTool.ow(),
					width : tabsUsableWidth - $ow($scrollerLeft) - $ow($scrollerRight)
				});
			}
		} else {
			$scrollerLeft.hide();
			$scrollerRight.hide();
			if (opts.toolPosition == "left") {
				$tabsTool.css({
					left : 0,
					right : ""
				});
				$tabsWrap.css({
					marginLeft : $tabsTool.ow(),
					marginRight : 0,
					width : tabsUsableWidth
				});
			} else {
				$tabsTool.css({
					left : "",
					right : 0
				});
				$tabsWrap.css({
					marginLeft : 0,
					marginRight : $tabsTool.ow(),
					width : tabsUsableWidth
				});
			}
		}
	}

	function _initTabsTool(targetDIV) {
		var opts = $d(targetDIV, "tabs").options;
		var $tabsHeader = $c("div.tabs-header", $(targetDIV));
		if (opts.tools) {
			if (typeof opts.tools == "string") {
				$ac("tabs-tool", $(opts.tools)).appendTo($tabsHeader);
				$(opts.tools).show();
			} else {
				$c("div.tabs-tool", $tabsHeader).remove();
				var $tabsTool = $('<div class="tabs-tool"></div>').appendTo($tabsHeader);
				for ( var i = 0; i < opts.tools.length; i++) {
					var $tabsToolBtn = $('<a href="javascript:void(0);"></a>').appendTo($tabsTool);
					// $tabsToolBtn[0].onclick = $fn(opts.tools[i].handler || function() {
					// });
					$tabsToolBtn.button($.extend({}, opts.tools[i], {
						plain : true
					}));
				}
			}
		} else {
			$c("div.tabs-tool", $tabsHeader).remove();
		}
	}

	/**
	 * resize tabs header and panels
	 */
	function resizeTabs(targetDIV) {
		var opts = $d(targetDIV, "tabs").options;
		var $target = $(targetDIV);
		opts.fit ? $.extend(opts, $target._fit()) : $target._fit(false);
		$h(opts.height, $w(opts.width, $target));
		var $tabsHeader = $c("div.tabs-header", $(targetDIV));
		var $tabsPanels = $c("div.tabs-panels", $(targetDIV));
		if (opts.tabPosition == "left" || opts.tabPosition == "right") {
			$tabsHeader.ow(opts.headerWidth);
			$tabsPanels.ow($w($target) - opts.headerWidth);
			$tabsHeader.add($tabsPanels).oh(opts.height);
			var $headerWrap = $f("div.tabs-wrap", $tabsHeader);
			$headerWrap.ow($w($tabsHeader));
			$f(".tabs ", $tabsHeader).ow($w($headerWrap));
		} else {
			$tabsHeader.css("height", "");
			$f("div.tabs-wrap", $tabsHeader).css("width", "");
			$f(".tabs ", $tabsHeader).css("width", "");
			$tabsHeader.ow(opts.width);
			_initScroller(targetDIV);
			var height = opts.height;
			if (!isNaN(height)) {
				$tabsPanels.oh(height - $oh($tabsHeader));
			} else {
				$h("auto", $tabsPanels);
			}
			var width = opts.width;
			if (!isNaN(width)) {
				$tabsPanels.ow(width);
			} else {
				$w("auto", $tabsPanels);
			}
		}
	}

	function _resizeTabPanel(targetDIV) {
		var opts = $d(targetDIV, "tabs").options;
		var $tabPanel = getSelected(targetDIV);
		if ($tabPanel) {
			var $tabsPanels = $c("div.tabs-panels", $(targetDIV));
			var width = opts.width == "auto" ? "auto" : $w($tabsPanels);
			var height = opts.height == "auto" ? "auto" : $h($tabsPanels);
			$tabPanel.panel("resize", {
				width : width,
				height : height
			});
		}
	}

	function _init(targetDIV) {
		var array$tabPanel = $d(targetDIV, "tabs").tabs;
		var $target = $(targetDIV);
		$ac("tabs-container", $target);
		$target.wrapInner('<div class="tabs-panels"/>');
		$('<div class="tabs-header"><div class="tabs-scroller-left"></div><div class="tabs-scroller-right"></div><div class="tabs-wrap"><ul class="tabs"></ul></div></div>').prependTo(targetDIV);
		$c("div", $c("div.tabs-panels", $target)).each(function(i) {
			var tabOptions = $.extend({}, jCocit.parseOptions(this), {
				selected : ($(this).attr("selected") ? true : undefined)
			});
			var $tabPanel = $(this);
			array$tabPanel.push($tabPanel);
			_initTab(targetDIV, $tabPanel, tabOptions);
		});
		$f(".tabs-scroller-left, .tabs-scroller-right", $c("div.tabs-header", $target)).hover(function() {
			$ac("tabs-scroller-over", $(this));
		}, function() {
			$rc("tabs-scroller-over", $(this));
		});
		$target.bind("_resize", function(e, fit) {
			var opts = $d(targetDIV, "tabs").options;
			if (opts.fit == true || fit) {
				resizeTabs(targetDIV);
				_resizeTabPanel(targetDIV);
			}
			return false;
		});
	}

	function _initTabsPosition(targetDIV) {
		var opts = $d(targetDIV, "tabs").options;
		var $tabsHeader = $c("div.tabs-header", $(targetDIV));
		var $tabsPanels = $c("div.tabs-panels", $(targetDIV));
		$rc("tabs-header-top tabs-header-bottom tabs-header-left tabs-header-right", $tabsHeader);
		$rc("tabs-panels-top tabs-panels-bottom tabs-panels-left tabs-panels-right", $tabsPanels);

		if (opts.tabPosition == "top") {
			$tabsHeader.insertBefore($tabsPanels);
		} else if (opts.tabPosition == "bottom") {
			$tabsHeader.insertAfter($tabsPanels);
			$ac("tabs-header-bottom", $tabsHeader);
			$ac("tabs-panels-top", $tabsPanels);
		} else if (opts.tabPosition == "left") {
			$ac("tabs-header-left", $tabsHeader);
			$ac("tabs-panels-right", $tabsPanels);
		} else if (opts.tabPosition == "right") {
			$ac("tabs-header-right", $tabsHeader);
			$ac("tabs-panels-left", $tabsPanels);
		}

		if (opts.plain == true) {
			$ac("tabs-header-plain", $tabsHeader);
		} else {
			$rc("tabs-header-plain", $tabsHeader);
		}

		if (opts.border == true) {
			$rc("tabs-header-noborder", $tabsHeader);
			$rc("tabs-panels-noborder", $tabsPanels);
		} else {
			$ac("tabs-header-noborder", $tabsHeader);
			$ac("tabs-panels-noborder", $tabsPanels);
		}

		$(".tabs-scroller-left", $tabsHeader).unbind(".tabs").bind("click.tabs", function() {
			$(targetDIV).tabs("scrollBy", -opts.scrollIncrement);
		});
		$(".tabs-scroller-right", $tabsHeader).unbind(".tabs").bind("click.tabs", function() {
			$(targetDIV).tabs("scrollBy", opts.scrollIncrement);
		});
	}

	function _initTab(targetDIV, $tabPanel, tabOptions) {
		var state = $d(targetDIV, "tabs");
		tabOptions = tabOptions || {};

		// init tab panel
		$tabPanel.panel($.extend({}, tabOptions, {
			border : false,
			noheader : true,
			closed : true,
			doSize : false,
			iconCls : (tabOptions.icon ? tabOptions.icon : undefined),
			onLoad : function() {
				// if (tabOptions.onLoad) {
				// tabOptions.onLoad.call(this, arguments);
				// }
				state.options.onLoad.call(targetDIV, $(this));
			}
		}));

		// add tab item
		var tabOptions = $tabPanel.panel("options");
		var $tabsUL = $f("ul.tabs", $c("div.tabs-header", $(targetDIV)));
		tabOptions.tab = $("<li></li>").appendTo($tabsUL);
		tabOptions.tab.append('<a href="javascript:void(0)" class="tabs-inner"><span class="tabs-title"></span><span class="tabs-icon"></span></a>');

		// bind tab item events
		tabOptions.tab.unbind(".tabs").bind("click.tabs", {
			p : $tabPanel
		}, function(e) {
			if ($hc("tabs-disabled", $(this))) {
				return;
			}
			select(targetDIV, getTabIndex(targetDIV, e.data.p));
		}).bind("contextmenu.tabs", {
			p : $tabPanel
		}, function(e) {
			if ($hc("tabs-disabled", $(this))) {
				return;
			}
			state.options.onContextMenu.call(targetDIV, e, $f("span.tabs-title", $(this)).html(), getTabIndex(targetDIV, e.data.p));
		});

		$(targetDIV).tabs("update", {
			tab : $tabPanel,
			options : tabOptions
		});
	}

	function add(targetDIV, tabOptions) {
		var opts = $d(targetDIV, "tabs").options;
		var array$tabPanel = $d(targetDIV, "tabs").tabs;

		if (tabOptions.selected == undefined) {
			tabOptions.selected = true;
		}
		var $tabPanel = $("<div></div>").appendTo($c("div.tabs-panels", $(targetDIV)));
		array$tabPanel.push($tabPanel);
		_initTab(targetDIV, $tabPanel, tabOptions);
		opts.onAdd.call(targetDIV, tabOptions.title, array$tabPanel.length - 1);
		_initScroller(targetDIV);
		if (tabOptions.selected) {
			select(targetDIV, array$tabPanel.length - 1);
		}
	}

	function update(targetDIV, tabConfig) {
		var arraySelectHis = $d(targetDIV, "tabs").selectHis;
		var $tabPanel = tabConfig.tab;

		var oldTitle = $tabPanel.panel("options").title;
		$tabPanel.panel($.extend({}, tabConfig.options, {
			iconCls : (tabConfig.options.icon ? tabConfig.options.icon : undefined)
		}));

		var tabOptions = $tabPanel.panel("options");
		var $tabLI = tabOptions.tab;
		var $tabTitle = $f("span.tabs-title", $tabLI);
		var $tabIcon = $f("span.tabs-icon", $tabLI);
		$tabTitle.html(tabOptions.title);
		$tabIcon.attr("class", "tabs-icon");
		$f("a.tabs-close", $tabLI).remove();
		if (tabOptions.closable) {
			$ac("tabs-closable", $tabTitle);
			var $closeBtn = $('<a href="javascript:void(0)" class="tabs-close"></a>').appendTo($tabLI);
			$closeBtn.bind("click.tabs", {
				p : $tabPanel
			}, function(e) {
				if ($hc("tabs-disabled", $p($(this)))) {
					return;
				}
				close(targetDIV, getTabIndex(targetDIV, e.data.p));
				return false;
			});
		} else {
			$rc("tabs-closable", $tabTitle);
		}
		if (tabOptions.iconCls) {
			$ac("tabs-with-icon", $tabTitle);
			$ac(tabOptions.iconCls, $tabIcon);
		} else {
			$rc("tabs-with-icon", $tabTitle);
		}
		if (oldTitle != tabOptions.title) {
			for ( var i = 0; i < arraySelectHis.length; i++) {
				if (arraySelectHis[i] == oldTitle) {
					arraySelectHis[i] = tabOptions.title;
				}
			}
		}
		$f("span.tabs-p-tool", $tabLI).remove();
		if (tabOptions.tools) {
			var $tabTool = $('<span class="tabs-p-tool"></span>').insertAfter($f("a.tabs-inner", $tabLI));
			if (typeof tabOptions.tools == "string") {
				$c($(tabOptions.tools)).appendTo($tabTool);
			} else {
				for ( var i = 0; i < tabOptions.tools.length; i++) {
					var $tabToolBtn = $('<a href="javascript:void(0)"></a>').appendTo($tabTool);
					$ac(tabOptions.tools[i].iconCls, $tabToolBtn);
					if (tabOptions.tools[i].handler) {
						$tabToolBtn.bind("click", {
							handler : tabOptions.tools[i].handler
						}, function(e) {
							if ($hc("tabs-disabled", $(this).parents("li"))) {
								return;
							}
							e.data.handler.call(this);
						});
					}
				}
			}
			var pr = $c($tabTool).length * 12;
			if (tabOptions.closable) {
				pr += 8;
			} else {
				pr -= 3;
				$tabTool.css("right", "5px");
			}
			$tabTitle.css("padding-right", pr + "px");
		}
		_initScroller(targetDIV);
		$d(targetDIV, "tabs").options.onUpdate.call(targetDIV, tabOptions.title, getTabIndex(targetDIV, $tabPanel));
	}

	function close(targetDIV, tabIndexOrTitle) {
		var opts = $d(targetDIV, "tabs").options;
		var array$tabPanel = $d(targetDIV, "tabs").tabs;
		var arraySelectHis = $d(targetDIV, "tabs").selectHis;
		if (!exists(targetDIV, tabIndexOrTitle)) {
			return;
		}
		var $tabPanel = getTab(targetDIV, tabIndexOrTitle);
		var tabTitle = $tabPanel.panel("options").title;
		var tabIndex = getTabIndex(targetDIV, $tabPanel);
		if (opts.onBeforeClose.call(targetDIV, tabTitle, tabIndex) == false) {
			return;
		}
		var $tabPanel = getTab(targetDIV, tabIndexOrTitle, true);
		$tabPanel.panel("options").tab.remove();
		$tabPanel.panel("destroy");
		opts.onClose.call(targetDIV, tabTitle, tabIndex);
		_initScroller(targetDIV);
		for ( var i = 0; i < arraySelectHis.length; i++) {
			if (arraySelectHis[i] == tabTitle) {
				arraySelectHis.splice(i, 1);
				i--;
			}
		}
		var tabTitle = arraySelectHis.pop();
		if (tabTitle) {
			select(targetDIV, tabTitle);
		} else {
			if (array$tabPanel.length) {
				select(targetDIV, 0);
			}
		}
	}

	function getTab(targetDIV, tabIndexOrTitle, remove) {
		var array$tabPanel = $d(targetDIV, "tabs").tabs;
		if (typeof tabIndexOrTitle == "number") {
			if (tabIndexOrTitle < 0 || tabIndexOrTitle >= array$tabPanel.length) {
				return null;
			} else {
				var $tabPanel = array$tabPanel[tabIndexOrTitle];
				if (remove) {
					array$tabPanel.splice(tabIndexOrTitle, 1);
				}
				return $tabPanel;
			}
		}
		for ( var i = 0; i < array$tabPanel.length; i++) {
			var $tabPanel = array$tabPanel[i];
			if ($tabPanel.panel("options").title == tabIndexOrTitle) {
				if (remove) {
					array$tabPanel.splice(i, 1);
				}
				return $tabPanel;
			}
		}
		return null;
	}

	function getTabIndex(targetDIV, tabPanelDIV) {
		var array$tabPanel = $d(targetDIV, "tabs").tabs;
		for ( var i = 0; i < array$tabPanel.length; i++) {
			if (array$tabPanel[i][0] == $(tabPanelDIV)[0]) {
				return i;
			}
		}
		return -1;
	}

	function getSelected(targetDIV) {
		var array$tabPanel = $d(targetDIV, "tabs").tabs;
		for ( var i = 0; i < array$tabPanel.length; i++) {
			var $tabPanel = array$tabPanel[i];
			if ($tabPanel.panel("options").closed == false) {
				return $tabPanel;
			}
		}
		return null;
	}

	function _initSelected(targetDIV) {
		var array$tabPanel = $d(targetDIV, "tabs").tabs;
		for ( var i = 0; i < array$tabPanel.length; i++) {
			if (array$tabPanel[i].panel("options").selected) {
				select(targetDIV, i);
				return;
			}
		}
		if (array$tabPanel.length) {
			select(targetDIV, 0);
		}
	}

	function select(targetDIV, tabIndexOrTitle) {
		var opts = $d(targetDIV, "tabs").options;
		var array$tabPanel = $d(targetDIV, "tabs").tabs;
		var arraySelectHis = $d(targetDIV, "tabs").selectHis;
		if (array$tabPanel.length == 0) {
			return;
		}
		var $tabPanel = getTab(targetDIV, tabIndexOrTitle);
		if (!$tabPanel) {
			return;
		}
		var $selectedTabPanel = getSelected(targetDIV);
		if ($selectedTabPanel) {
			$selectedTabPanel.panel("close");
			$rc("tabs-selected", $selectedTabPanel.panel("options").tab);
		}
		$tabPanel.panel("open");
		var tabTitle = $tabPanel.panel("options").title;
		arraySelectHis.push(tabTitle);
		var tab = $tabPanel.panel("options").tab;
		$ac("tabs-selected", tab);
		var $tabWrap = $f(">div.tabs-header>div.tabs-wrap", $(targetDIV));
		var left = tab.position().left;
		var width = left + $ow(tab);
		if (left < 0 || width > $w($tabWrap)) {
			var offsetLeft = left - ($w($tabWrap) - $w(tab)) / 2;
			$(targetDIV).tabs("scrollBy", offsetLeft);
		} else {
			$(targetDIV).tabs("scrollBy", 0);
		}
		_resizeTabPanel(targetDIV);
		opts.onSelect.call(targetDIV, tabTitle, getTabIndex(targetDIV, $tabPanel));
	}

	function exists(targetDIV, tabIndexOrTitle) {
		return getTab(targetDIV, tabIndexOrTitle) != null;
	}

	$.fn.tabs = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.tabs.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.tabs');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "tabs");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
			} else {
				$d(this, "tabs", {
					options : $.extend({}, $.fn.tabs.defaults, $.fn.tabs.parseOptions(this), options),
					tabs : [],
					selectHis : []
				});
				_init(this);
			}
			_initTabsTool(this);
			_initTabsPosition(this);
			resizeTabs(this);
			_initSelected(this);
		});
	};
	$.fn.tabs.methods = {
		options : function(jq) {
			return $d(jq[0], "tabs").options;
		},
		tabs : function(jq) {
			return $d(jq[0], "tabs").tabs;
		},
		resize : function(jq) {
			return jq.each(function() {
				resizeTabs(this);
				_resizeTabPanel(this);
			});
		},
		add : function(jq, tabOptions) {
			return jq.each(function() {
				add(this, tabOptions);
			});
		},
		close : function(jq, tabIndexOrTitle) {
			return jq.each(function() {
				close(this, tabIndexOrTitle);
			});
		},
		getTab : function(jq, tabIndexOrTitle) {
			return getTab(jq[0], tabIndexOrTitle);
		},
		getTabIndex : function(jq, $tabPanel) {
			return getTabIndex(jq[0], $tabPanel);
		},
		getSelected : function(jq) {
			return getSelected(jq[0]);
		},
		select : function(jq, tabIndexOrTitle) {
			return jq.each(function() {
				select(this, tabIndexOrTitle);
			});
		},
		exists : function(jq, tabIndexOrTitle) {
			return exists(jq[0], tabIndexOrTitle);
		},
		update : function(jq, tabConfig) {
			return jq.each(function() {
				update(this, tabConfig);
			});
		},
		enableTab : function(jq, tabIndexOrTitle) {
			return jq.each(function() {
				$rc("tabs-disabled", $(this).tabs("getTab", tabIndexOrTitle).panel("options").tab);
			});
		},
		disableTab : function(jq, tabIndexOrTitle) {
			return jq.each(function() {
				$ac("tabs-disabled", $(this).tabs("getTab", tabIndexOrTitle).panel("options").tab);
			});
		},
		scrollBy : function(jq, step) {
			return jq.each(function() {
				var opts = $(this).tabs("options");
				var $tabsWrap = $f(">div.tabs-header>div.tabs-wrap", $(this));
				var scrollLeft = Math.min($tabsWrap._scrollLeft() + step, _hiddenWidth());
				$tabsWrap.animate({
					scrollLeft : scrollLeft
				}, opts.scrollDuration);

				function _hiddenWidth() {
					var w = 0;
					var ul = $c("ul", $tabsWrap);
					$c("li", ul).each(function() {
						w += $ow(true, $(this));
					});
					return w - $w($tabsWrap) + ($ow(ul) - $w(ul));
				}

			});
		}
	};
	$.fn.tabs.parseOptions = function(targetDIV) {
		return $.extend({}, jCocit.parseOptions(targetDIV, [ "width", "height", "tools", "toolPosition", "tabPosition", {
			fit : "b",
			border : "b",
			plain : "b",
			headerWidth : "n"
		} ]));
	};
	$.fn.tabs.defaults = {
		width : "auto",
		height : "auto",
		headerWidth : 150,
		plain : false,
		fit : false,
		border : true,
		tools : null,
		toolPosition : "right",
		tabPosition : "top",
		scrollIncrement : 100,
		scrollDuration : 400,
		/**
		 * arg: $tabPanel -
		 */
		onLoad : $n,
		/**
		 * args:
		 * <UL>
		 * <LI>tabTitle
		 * <LI>tabIndex
		 * </UL>
		 */
		onSelect : $n,
		/**
		 * args:
		 * <UL>
		 * <LI>tabTitle
		 * <LI>tabIndex
		 * </UL>
		 */
		onBeforeClose : $n,
		/**
		 * args:
		 * <UL>
		 * <LI>tabTitle
		 * <LI>tabIndex
		 * </UL>
		 */
		onClose : $n,
		/**
		 * args:
		 * <UL>
		 * <LI>tabTitle
		 * <LI>tabIndex
		 * </UL>
		 */
		onAdd : $n,
		/**
		 * args:
		 * <UL>
		 * <LI>tabTitle
		 * <LI>tabIndex
		 * </UL>
		 */
		onUpdate : $n,
		/**
		 * args:
		 * <UL>
		 * <LI>e
		 * <LI>tabTitle
		 * <LI>tabIndex
		 * </UL>
		 */
		onContextMenu : $n
	};
})(jQuery, jCocit);
