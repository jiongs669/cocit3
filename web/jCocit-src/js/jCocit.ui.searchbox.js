/**
 * 
 */
(function($, jCocit) {
	function _init(targetInput) {
		$(targetInput).hide();
		var $searchbox = $('<span class="searchbox"></span>').insertAfter(targetInput);
		var $searchText = $('<input type="text" class="searchbox-text">').appendTo($searchbox);
		$('<span class="searchbox-button"><div class="searchbox-button-icon"></div></span>').appendTo($searchbox);
		var name = $(targetInput).attr("name");
		if (name) {
			$searchText.attr("name", name);
			$(targetInput).removeAttr("name").attr("searchboxName", name);
		}
		return $searchbox;
	}

	function resize(targetInput, width) {
		var opts = $d(targetInput, "searchbox").options;
		var sb = $d(targetInput, "searchbox").searchbox;
		if (width) {
			opts.width = width;
		}
		sb.appendTo("body");
		if (isNaN(opts.width)) {
			opts.width = sb.ow();
		}
		var $searchButton = $f("span.searchbox-button", sb);
		var $searchMenu = $f("a.searchbox-menu", sb);
		var $searchText = $f("input.searchbox-text", sb);
		sb.ow(opts.width).oh(opts.height);
		$searchText.ow($w(sb) - $searchMenu.ow() - $searchButton.ow());
		$searchText.css({
			height : $h(sb) + "px",
			lineHeight : $h(sb) + "px"
		});
		$searchMenu.oh($h(sb));
		$searchButton.oh($h(sb));
		var $leftButton = $f("span.l-btn-left", $searchMenu);
		$leftButton.oh($h(sb));
		$f("span.l-btn-text,span.m-btn-downarrow", $leftButton).css({
			height : $h($leftButton) + "px",
			lineHeight : $h($leftButton) + "px"
		});
		sb.insertAfter(targetInput);
	}

	function _initMenu(targetInput) {
		var state = $d(targetInput, "searchbox");
		var opts = state.options;

		if (opts.menu) {
			state.menu = $(opts.menu).menu({
				onClick : function(itemData) {
					_doClickMenu(itemData);
				}
			});
			var $selectedMenuItem = $c("div.menu-item:first", state.menu);
			$c("div.menu-item", state.menu).each(function() {
				var itemData = $.extend({}, jCocit.parseOptions(this), {
					selected : ($(this).attr("selected") ? true : undefined)
				});
				if (itemData.selected) {
					$selectedMenuItem = $(this);
					return false;
				}
			});
			$selectedMenuItem.triggerHandler("click");
		} else {
			$f("a.searchbox-menu", state.searchbox).remove();
			state.menu = null;
		}

		function _doClickMenu(itemData) {
			$f("a.searchbox-menu", state.searchbox).remove();
			var len = itemData.text.length;
			var mb = $('<a class="searchbox-menu" href="javascript:void(0)" ></a>').html(itemData.text.substring(0, (len < 5 ? len : 5))).attr("title", itemData.text);
			mb.prependTo(state.searchbox).menubar({
				menu : state.menu,
				iconCls : itemData.iconCls
			});
			var $box = $f("input.searchbox-text", state.searchbox).attr("name", $(itemData.target).attr("name") || itemData.name);
			// $box.val("");
			// if(!itemData.name||itemData.name.trim().length==0){
			// $box.attr("disabled",true);
			// }else{
			// $box.attr("disabled",false);
			// }
			resize(targetInput);
			state.menu.menu("hide");
			$box[0].focus();
		}

	}

	function _bindEvents(targetInput) {
		var state = $d(targetInput, "searchbox");
		var opts = state.options;

		var $searchText = $f("input.searchbox-text", state.searchbox);
		var $searchButton = $f(".searchbox-button", state.searchbox);

		$searchText.unbind(".searchbox").bind("blur.searchbox", function(e) {
			opts.value = $(this).val();
			if (opts.value == "") {
				$(this).val(opts.prompt);
				$ac("searchbox-prompt", $(this));
			} else {
				$rc("searchbox-prompt", $(this));
			}
		}).bind("focus.searchbox", function(e) {
			if ($(this).val() != opts.value) {
				$(this).val(opts.value);
			}
			$rc("searchbox-prompt", $(this));
		}).bind("keydown.searchbox", function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				var name = $.fn.prop ? $searchText.prop("name") : $searchText.attr("name");
				opts.value = $(this).val();
				opts.onSearch.call(targetInput, opts.value, name);
				return false;
			}
		});

		$searchButton.unbind(".searchbox").bind("click.searchbox", function() {
			var _1e = $.fn.prop ? $searchText.prop("name") : $searchText.attr("name");
			opts.onSearch.call(targetInput, opts.value, _1e);
		}).bind("mouseenter.searchbox", function() {
			$ac("searchbox-button-hover", $(this));
		}).bind("mouseleave.searchbox", function() {
			$rc("searchbox-button-hover", $(this));
		});
	}

	function _initOriginalValue(targetInput) {
		var state = $d(targetInput, "searchbox");
		var opts = state.options;
		var $searchText = $f("input.searchbox-text", state.searchbox);
		if (opts.value == "") {
			$searchText.val(opts.prompt);
			$ac("searchbox-prompt", $searchText);
		} else {
			$searchText.val(opts.value);
			$rc("searchbox-prompt", $searchText);
		}
	}

	$.fn.searchbox = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.searchbox.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.searchbox');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "searchbox");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "searchbox", {
					options : $.extend({}, $.fn.searchbox.defaults, $.fn.searchbox.parseOptions(this), options),
					searchbox : _init(this)
				});
			}
			_initMenu(this);
			_initOriginalValue(this);
			_bindEvents(this);
			resize(this);
		});
	};

	$.fn.searchbox.methods = {
		options : function(jq) {
			return $d(jq[0], "searchbox").options;
		},
		menu : function(jq) {
			return $d(jq[0], "searchbox").menu;
		},
		textbox : function(jq) {
			return $f("input.searchbox-text", $d(jq[0], "searchbox").searchbox);
		},
		getValue : function(jq) {
			return $d(jq[0], "searchbox").options.value;
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				$(this).searchbox("options").value = value;
				$(this).searchbox("textbox").val(value);
				$(this).searchbox("textbox").blur();
			});
		},
		getName : function(jq) {
			return $f("input.searchbox-text", $d(jq[0], "searchbox").searchbox).attr("name");
		},
		selectName : function(jq, name) {
			return jq.each(function() {
				var $menu = $d(this, "searchbox").menu;
				if ($menu) {
					$c('div.menu-item[name="' + name + '"]', $menu).triggerHandler("click");
				}
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var $menu = $(this).searchbox("menu");
				if ($menu) {
					$menu.menu("destroy");
				}
				$d(this, "searchbox").searchbox.remove();
				$(this).remove();
			});
		},
		/**
		 * arg: width -
		 */
		resize : $X(resize)
	};

	$.fn.searchbox.parseOptions = function(targetInput) {
		var t = $(targetInput);
		return $.extend({}, jCocit.parseOptions(targetInput, [ "width", "height", "prompt", "menu" ]), {
			value : t.val(),
			onSearch : (t.attr("onSearch") ? $fn(t.attr("onSearch")) : undefined)
		});
	};

	$.fn.searchbox.defaults = {
		width : "auto",
		height : 22,
		prompt : "",
		value : "",
		menu : null,
		/**
		 * args: value, menuMame
		 */
		onSearch : $n
	};
})(jQuery, jCocit);
