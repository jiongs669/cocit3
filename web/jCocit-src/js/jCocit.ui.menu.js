/**
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference:
 * <LI>ReferencedBy: tree
 * <LI>SubClass:
 * <LI>SuperClass:
 * </UL>
 * 
 * <pre>
 * 	[div class='menu-top menu']
 * 		[div class='menu-line'][/div]
 * 		[div name='new' class='menu-item']
 * 			[div class='menu-text']New[/div]
 * 		[/div]
 * 		[div name='save' class='menu-item']
 * 			[div class='menu-text']Save[/div]
 * 			[div class='menu-icon icon-save'][/div]
 * 		[/div]
 * 		[div name='print' class='menu-item']
 * 			[div class='menu-text']Print[/div]
 * 			[div class='menu-icon icon-print'][/div]
 * 		[/div]
 * 		[div class='menu-sep'][/div]
 * 		[div name='exit' class='menu-item']
 * 			[div class='menu-text']Exit[/div]
 * 		[/div]
 * 	[/div]
 * 	[div class='menu-shadow'][/div]
 * </pre>
 * 
 */
(function($, jCocit) {

	function _init(target) {
		var $menu = $(target);
		var opts = $d(target, "menu").options;

		$menu.appendTo("body");
		$ac("menu-top", $menu);
		if (opts.styleName) {
			$ac(opts.styleName, $menu);
		}

		// mouse down document, hide menu.
		$(document).unbind(".menu").bind("mousedown.menu", function(e) {
			var $currentMenu = $("div.menu:visible");
			var $menu = $l("div.menu", $currentMenu, $(e.target));
			if ($menu.length) {
				return;
			}

			$("div.menu-top:visible").menu("hide");
		});

		var $menuArray = _initMenu($menu);
		for ( var i = 0; i < $menuArray.length; i++) {
			_initItems($menuArray[i]);
		}

		function _initMenu($menu) {
			var $menuArray = [];
			$ac("menu", $menu);
			if (!$menu[0].style.width) {
				$menu[0].autowidth = true;
			}
			$menuArray.push($menu);
			if (!$hc("menu-content", $menu)) {
				$c("div", $menu).each(function() {
					var $submenu = $c("div", $(this));
					if ($submenu.length) {
						$submenu.insertAfter(target);
						this.submenu = $submenu;
						var $submenuArray = _initMenu($submenu);
						$menuArray = $menuArray.concat($submenuArray);
					}
				});
			}
			return $menuArray;
		}

		function _initItems($menu) {
			if (!$hc("menu-content", $menu)) {
				$c("div", $menu).each(function() {
					var $item = $(this);
					if ($hc("menu-sep", $item)) {
					} else {
						var item = $.extend({}, jCocit.parseOptions(this, [ "name", "iconCls", "href" ]), {
							disabled : ($item.attr("disabled") ? true : undefined)
						});
						$item.attr("name", item.name || "").attr("href", item.href || "");
						var itemText = $ac("menu-item", $item).html();
						$item.empty().append($('<div class="menu-text"></div>').html(itemText));
						if (item.iconCls) {
							$ac(item.iconCls, $('<div class="menu-icon"></div>')).appendTo($item);
						}
						if (item.disabled) {
							disableItem(target, $item[0], true);
						}
						if ($item[0].submenu) {
							$('<div class="menu-rightarrow"></div>').appendTo($item);
						}
						$item.oh(22);
						_bindItemEvents(target, $item);
					}
				});
				$('<div class="menu-line"></div>').prependTo($menu);
			}
			_setMenuWidth(target, $menu);
			$menu.hide();
			_bindMenuEvents(target, $menu);
		}

	}

	function _setMenuWidth(target, $menu) {
		var opts = $d(target, "menu").options;
		var display = $menu.css("display");
		$menu.css({
			display : "block",
			left : -10000
		});
		var itemWidth = $menu.ow();
		var maxWidth = 0;
		$f("div.menu-text", $menu).each(function() {
			var $itemText = $(this);
			if (maxWidth < $itemText.ow()) {
				maxWidth = $itemText.ow();
			}
		});
		maxWidth += 15;
		$menu.ow(Math.max(itemWidth, maxWidth, opts.minWidth));
		$menu.css("display", display);
	}

	function _bindMenuEvents(target, $menu) {
		var state = $d(target, "menu");
		$menu.bind("contextmenu", function(e) {
			e.preventDefault();
		});
		if (!state.options.hideOnMouseLeave) {
			return;
		}
		$menu.unbind(".menu").bind("mouseenter.menu", function() {
			if (state.timer) {
				clearTimeout(state.timer);
				state.timer = null;
			}
		}).bind("mouseleave.menu", function() {
			state.timer = setTimeout(function() {
				hideMenu(target);
			}, 100);
		});
	}

	function _bindItemEvents(target, $item) {
		var state = $d(target, "menu");
		$item.unbind(".menu");
		$item.bind("click.menu", function(e) {
			if ($hc("menu-item-disabled", $(this))) {
				return;
			}

			if (!this.submenu) {
				if (state.options.hideOnMouseClick)
					hideMenu(target);
				var href = $(this).attr("href");
				if (href) {
					location.href = href;
				}
			}

			var item = getItem(target, this);
			$d(target, "menu").options.onClick.call(target, item);

			e.stopPropagation();

		}).bind("mouseenter.menu", function(e) {

			$item.siblings().each(function() {
				if (this.submenu) {
					_hideMenu(this.submenu);
				}
				$rc("menu-active", $(this));
			});

			$ac("menu-active", $item);
			if ($hc("menu-item-disabled", $(this))) {
				$ac("menu-active-disabled", $item);
				return;
			}

			var $submenu = $item[0].submenu;
			if ($submenu) {
				$(target).menu("show", {
					menu : $submenu,
					parent : $item
				});
			}

			e.stopPropagation();

		}).bind("mouseleave.menu", function(e) {
			$rc("menu-active menu-active-disabled", $item);
			var $submenu = $item[0].submenu;
			if ($submenu) {
				if (e.pageX >= parseInt($submenu.css("left"))) {
					$ac("menu-active", $item);
				} else {
					_hideMenu($submenu);
				}
			} else {
				$rc("menu-active", $item);
			}

			e.stopPropagation();

		}).bind("contextmenu", function(e) {
			e.preventDefault();
		});
	}

	function hideMenu(target) {
		var state = $d(target, "menu");
		if (state) {
			if ($(target).is(":visible")) {
				_hideMenu($(target));
				state.options.onHide.call(target);
			}
		}
		return false;
	}

	function showMenu(target, menuConfig) {
		var left, top;
		var $menu = $(menuConfig.menu || target);
		if ($hc("menu-top", $menu)) {
			var opts = $d(target, "menu").options;
			left = opts.left;
			top = opts.top;
			if (menuConfig.alignTo) {
				var at = $(menuConfig.alignTo);
				left = at.offset().left;
				top = at.offset().top + at.oh();
			}
			if (menuConfig.left != undefined) {
				left = menuConfig.left;
			}
			if (menuConfig.top != undefined) {
				top = menuConfig.top;
			}
			if (left + $ow($menu) > $(window).ow() + $(document)._scrollLeft()) {
				left = $(window).ow() + $(document).scrollLeft() - $ow($menu) - 5;
			}
			if (top + $oh($menu) > $(window).oh() + $(document).scrollTop()) {
				top -= $oh($menu);
			}
		} else {
			var $parentItem = menuConfig.parent;
			left = $parentItem.offset().left + $ow($parentItem) - 2;
			if (left + $ow($menu) + 5 > $(window).ow() + $(document).scrollLeft()) {
				left = $parentItem.offset().left - $ow($menu) + 2;
			}
			var top = $parentItem.offset().top - 3;
			if (top + $oh($menu) > $(window).oh() + $(document).scrollTop()) {
				top = $(window).oh() + $(document).scrollTop() - $oh($menu) - 5;
			}
		}
		$menu.css({
			left : left,
			top : top
		});
		$menu.show(0, function() {
			if (!$menu[0].shadow) {
				$menu[0].shadow = $('<div class="menu-shadow"></div>');
			}
			$menu[0].shadow.insertAfter($menu);
			$menu[0].shadow.css({
				display : "block",
				zIndex : defaults.zIndex++,
				left : $menu.css("left"),
				top : $menu.css("top"),
				width : $ow($menu),
				height : $oh($menu)
			});
			$menu.css("z-index", defaults.zIndex++);
			if ($hc("menu-top", $menu)) {
				$d($menu[0], "menu").options.onShow.call($menu[0]);
			}
		});
	}

	function _hideMenu($menu) {
		if (!$menu) {
			return;
		}
		_hideShadow($menu);
		$f("div.menu-item", $menu).each(function() {
			if (this.submenu) {
				_hideMenu(this.submenu);
			}
			$rc("menu-active", $(this));
		});

		function _hideShadow($m) {
			$m.stop(true, true);
			if ($m[0].shadow) {
				$m[0].shadow.hide();
			}
			$m.hide();
		}
	}

	function getItem(target, itemDIV) {
		var $item = $(itemDIV);
		var item = $.extend({}, jCocit.parseOptions(itemDIV, [ "name", "iconCls", "href" ]), {
			target : itemDIV,
			id : $item.attr("id"),
			text : $.trim($c("div.menu-text", $item).html()),
			disabled : $hc("menu-item-disabled", $item),
			href : $item.attr("href"),
			name : $item.attr("name")
		});
		// var item = {
		// target : itemDIV,
		// id : $item.attr("id"),
		// text : $.trim($c("div.menu-text", $item).html()),
		// disabled : $hc("menu-item-disabled", $item),
		// href : $item.attr("href"),
		// name : $item.attr("name"),
		// onclick : itemDIV.onclick
		// };
		var $itemIcon = $c("div.menu-icon", $item);
		if ($itemIcon.length) {
			var iconClassNames = [];
			var classNames = $itemIcon.attr("class").split(" ");
			for ( var i = 0; i < classNames.length; i++) {
				if (classNames[i] != "menu-icon") {
					iconClassNames.push(classNames[i]);
				}
			}
			item.iconCls = iconClassNames.join(" ");
		}

		return item;
	}

	function findItem(target, text) {
		var $menu = $(target);
		var item = null;
		var $tmp = $("<div></div>");

		function _findItem($submenu) {
			$c("div.menu-item", $submenu).each(function() {
				var tmpItem = getItem(target, this);
				var tmpText = $tmp.empty().html(tmpItem.text).text();
				if (text == $.trim(tmpText)) {
					item = tmpItem;
				} else {
					if (this.submenu && !item) {
						_findItem(this.submenu);
					}
				}
			});
		}

		_findItem($menu);
		$tmp.remove();
		return item;
	}

	function disableItem(target, itemDIV, disabled) {
		var $item = $(itemDIV);
		if (disabled) {
			$ac("menu-item-disabled", $item);
			if (itemDIV.onclick) {
				itemDIV.onclick1 = itemDIV.onclick;
				itemDIV.onclick = null;
			}
		} else {
			$rc("menu-item-disabled", $item);
			if (itemDIV.onclick1) {
				itemDIV.onclick = itemDIV.onclick1;
				itemDIV.onclick1 = null;
			}
		}
	}

	function appendItem(target, item) {
		var $menu = $(target);
		if (item.parent) {
			if (!item.parent.submenu) {
				var $submenu = $('<div class="menu"><div class="menu-line"></div></div>').appendTo("body");
				$submenu[0].autowidth = true;
				$submenu.hide();
				item.parent.submenu = $submenu;
				$('<div class="menu-rightarrow"></div>').appendTo(item.parent);
			}
			$menu = item.parent.submenu;
		}
		var $item = $('<div class="menu-item"></div>').appendTo($menu);
		$('<div class="menu-text"></div>').html(item.text).appendTo($item);
		if (item.iconCls) {
			$ac(item.iconCls, $('<div class="menu-icon"></div>')).appendTo($item);
		}
		if (item.id) {
			$item.attr("id", item.id);
		}
		if (item.href) {
			$item.attr("href", item.href);
		}
		if (item.name) {
			$item.attr("name", item.name);
		}
		if (item.onclick) {
			if (typeof item.onclick == "string") {
				$item.attr("onclick", item.onclick);
			} else {
				$item[0].onclick = $fn(item.onclick);
			}
		}
		if (item.handler) {
			$item[0].onclick = $fn(item.handler);
		}
		_bindItemEvents(target, $item);
		if (item.disabled) {
			disableItem(target, $item[0], true);
		}
		_bindMenuEvents(target, $menu);
		_setMenuWidth(target, $menu);
	}

	function removeItem(target, itemDIV) {
		function _removeItem(_itemDIV) {
			if (_itemDIV.submenu) {
				$c("div.menu-item", _itemDIV.submenu).each(function() {
					_removeItem(this);
				});
				var $shadow = _itemDIV.submenu[0].shadow;
				if ($shadow) {
					$shadow.remove();
				}
				_itemDIV.submenu.remove();
			}
			$(_itemDIV).remove();
		}

		_removeItem(itemDIV);
	}

	function destroyMenu(target) {
		$c("div.menu-item", $(target)).each(function() {
			removeItem(target, this);
		});
		if (target.shadow) {
			target.shadow.remove();
		}
		$(target).remove();
	}

	/**
	 * 1. Create menu UI object or set menu properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke menu method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.menu = function(options, args) {
		if (typeof options == "string") {
			var fn = methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.menu');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "menu");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "menu", {
					options : $.extend({}, defaults, parseOptions(this), options)
				});
				_init(this);
			}
			$(this).css({
				left : state.options.left,
				top : state.options.top
			});
		});
	};

	var methods = {
		options : function(jq) {
			return $d(jq[0], "menu").options;
		},
		show : $X(showMenu),
		hide : $X(hideMenu),
		destroy : $X(destroyMenu),
		setText : function(jq, itemData) {
			return jq.each(function() {
				$c("div.menu-text", $(itemData.target)).html(item.text);
			});
		},
		setIcon : function(jq, itemConfig) {
			return jq.each(function() {
				var item = $(this).menu("getItem", itemConfig.target);
				if (item.iconCls) {
					$(item.target).children("div.menu-icon").removeClass(item.iconCls).addClass(itemConfig.iconCls);
				} else {
					$("<div class=\"menu-icon\"></div>").addClass(itemConfig.iconCls).appendTo(itemConfig.target);
				}
			});
		},
		getItem : $x(getItem),
		find : $x(findItem),
		append : $X(appendItem),
		remove : $X(removeItem),
		enable : $X(disableItem, false),
		disable : $X(disableItem, true)
	};

	function parseOptions(target) {
		return $.extend({}, jCocit.parseOptions(target, [ "left", "top", "styleName", {
			minWidth : "n",
			hideOnMouseLeave : "b"
		} ]));
	}

	var defaults = {
		zIndex : 110000,
		left : 0,
		top : 0,
		minWidth : 120,
		styleName : null,
		hideOnMouseLeave : false,
		hideOnMouseClick : true,
		onShow : $n,
		onHide : $n,
		/**
		 * args: item - this is menu item
		 */
		onClick : $n
	};
})(jQuery, jCocit);
