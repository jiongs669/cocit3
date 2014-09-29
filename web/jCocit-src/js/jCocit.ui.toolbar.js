(function($, jCocit) {
	function _init(html) {
		var options = $.data(html, "toolbar").options;
		var $btn = $(html);
		$btn.removeClass("s-btn-active s-btn-plain-active").addClass("s-btn");
		if (options.menu) {
			$btn.button($.extend({}, options, {
				text : options.text + "&nbsp;<span class=\"s-btn-downarrow\">&nbsp;</span>"
			}));
			$(options.menu).menu({
				onShow : function() {
					$btn.addClass((options.plain == true) ? "s-btn-plain-active" : "s-btn-active");
				},
				onHide : function() {
					$btn.removeClass((options.plain == true) ? "s-btn-plain-active" : "s-btn-active");
				}
			});
		} else {
			$btn.button($.extend({}, options));
		}
		disable(html, options.disabled);
	}

	function disable(html, disabled) {
		var opts = $.data(html, "toolbar").options;
		opts.disabled = disabled;
		var $btn = $(html);
		var $downarrow = $btn.find(".s-btn-downarrow");
		if (disabled) {
			$btn.button("disable");
			$downarrow.unbind(".toolbar");
		} else {
			$btn.button("enable");
			$downarrow.unbind(".toolbar");
			$downarrow.bind("click.toolbar", function() {
				_showMenu();
				return false;
			});
			var _fnTimeout = null;
			$downarrow.bind("mouseenter.toolbar", function() {
				_fnTimeout = setTimeout(function() {
					_showMenu();
				}, opts.duration);
				return false;
			}).bind("mouseleave.toolbar", function() {
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
				alignTo : $btn
			});
			$btn.blur();
		}

	}

	$.fn.toolbar = function(html, options) {
		if (typeof html == "string") {
			return $.fn.toolbar.methods[html](this, options);
		}
		html = html || {};
		return this.each(function() {
			var state = $.data(this, "toolbar");
			if (state) {
				$.extend(state.options, html);
			} else {
				$.data(this, "toolbar", {
					options : $.extend({}, $.fn.toolbar.defaults, $.fn.toolbar.parseOptions(this), html)
				});
				$(this).removeAttr("disabled");
			}
			_init(this);
		});
	};
	$.fn.toolbar.methods = {
		options : function(jq) {
			return $.data(jq[0], "toolbar").options;
		},
		enable : function(jq) {
			return jq.each(function() {
				disable(this, false);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				disable(this, true);
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var _10 = $(this).toolbar("options");
				if (_10.menu) {
					$(_10.menu).menu("destroy");
				}
				$(this).remove();
			});
		}
	};
	$.fn.toolbar.parseOptions = function(html) {
		return $.extend({}, $.fn.button.parseOptions(html), jCocit.parseOptions(html, [ "menu", {
			plain : "boolean",
			duration : "number"
		} ]));
	};
	$.fn.toolbar.defaults = $.extend({}, $.fn.button.defaults, {
		plain : true,
		menu : null,
		duration : 100
	});
})(jQuery, jCocit);
