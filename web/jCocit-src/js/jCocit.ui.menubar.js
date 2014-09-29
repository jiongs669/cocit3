/**
 * 
 */
(function($, jCocit) {
	function _init(buttonHTML) {
		var opts = $d(buttonHTML, "menubar").options;
		var $button = $(buttonHTML);

		$ac("m-btn", $rc("m-btn-active m-btn-plain-active", $button));

		$button.button($.extend({}, opts, {
			text : opts.text + (opts.menu ? '<span class="m-btn-downarrow">&nbsp;</span>' : "")
		}));

		if (opts.menu) {
			$(opts.menu).menu({
				onShow : function() {
					$ac((opts.plain == true) ? "m-btn-plain-active" : "m-btn-active", $button);
				},
				onHide : function() {
					$rc((opts.plain == true) ? "m-btn-plain-active" : "m-btn-active", $button);
				}
			});
		}

		disable(buttonHTML, opts.disabled);
	}

	function disable(buttonHTML, disabled) {
		var opts = $d(buttonHTML, "menubar").options;
		opts.disabled = disabled;
		var $button = $(buttonHTML);

		if (disabled) {
			$button.button("disable");
			$button.unbind(".menubar");
		} else {
			$button.button("enable");
			$button.unbind(".menubar");

			$button.bind("click.menubar", function() {
				_showMenu();
				return false;
			});

			var _fnTimeout = null;

			(opts.split ? $ac("m-btn-downarrow-split", $f(".m-btn-downarrow", $button)) : $button).bind("mouseenter.menubar", function() {
				_fnTimeout = setTimeout(function() {
					_showMenu();
				}, opts.duration);
				return false;
			}).bind("mouseleave.menubar", function() {
				if (_fnTimeout) {
					clearTimeout(_fnTimeout);
				}
			});
		}

		function _showMenu() {
			if (!opts.menu) {
				return;
			}
			$("body>div.menu-top").menu("hide");
			$(opts.menu).menu("show", {
				alignTo : $button
			});
			$button.blur();
		}

	}

	$.fn.menubar = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.menubar.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.menubar');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "menubar");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "menubar", {
					options : $.extend({}, $.fn.menubar.defaults, $.fn.menubar.parseOptions(this), options)
				});
				$(this).removeAttr("disabled");
			}
			_init(this);
		});
	};

	$.fn.menubar.methods = {
		options : function(jq) {
			return $d(jq[0], "menubar").options;
		},
		enable : $X(disable, false),
		disable : $X(disable, true),
		destroy : function(jq) {
			return jq.each(function() {
				var opts = $(this).menubar("options");
				if (opts.menu) {
					$(opts.menu).menu("destroy");
				}
				$(this).remove();
			});
		}
	};

	$.fn.menubar.parseOptions = function(buttonHTML) {
		var t = $(buttonHTML);
		return $.extend({}, $.fn.button.parseOptions(buttonHTML), jCocit.parseOptions(buttonHTML, [ "menu", {
			plain : "b",
			duration : "n"
		} ]));
	};

	$.fn.menubar.defaults = $.extend({}, $.fn.button.defaults, {
		plain : true,
		menu : null,
		/**
		 * true means that this is a split drop-down menu.
		 */
		split : false,
		duration : 100
	});
})(jQuery, jCocit);
