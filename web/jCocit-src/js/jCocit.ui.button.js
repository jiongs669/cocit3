/**
 * 
 */
(function($, jCocit) {
	function _init(buttonHTML) {
		var $button = $(buttonHTML);
		var opts = $d(buttonHTML, "button").options;

		$button.empty();
		$ac("l-btn", $button);

		if (opts.id) {
			$button.attr("id", opts.id);
		} else {
			$button.attr("id", "");
		}

		if (opts.plain) {
			$ac("l-btn-plain", $button);
		} else {
			$rc("l-btn-plain", $button);
		}

		if (opts.text) {
			// $button.html(opts.text).wrapInner('<span class="l-btn-left"><span class="l-btn-text"></span></span>');
			// if (opts.iconCls) {
			// $ac(opts.iconAlign == "left" ? "l-btn-icon-left" : "l-btn-icon-right", $ac(opts.iconCls, $f(".l-btn-text", $button)));
			// }

			$button.html(opts.text).wrapInner('<span class="l-btn-left"><span class="l-btn-text"></span></span>');
			if (opts.iconCls) {
				$('<div class="l-btn-icon"></div>').addClass(opts.iconCls).prependTo($f(".l-btn-left", $button));
			}
		} else {
			$button.html("&nbsp;").wrapInner('<span class="l-btn-left"><span class="l-btn-text"><span class="l-btn-empty"></span></span></span>');
			if (opts.iconCls) {
				$ac(opts.iconCls, $f(".l-btn-empty", $button));
			}
		}

		$button.unbind(".button").bind("focus.button", function() {
			if (!opts.disabled) {
				$ac("l-btn-focus", $f("span.l-btn-text", $(this)));
			}
		}).bind("blur.button", function() {
			$rc("l-btn-focus", $f("span.l-btn-text", $(this)));
		}).bind("click.button", function(e) {
			if ($hc("l-btn-disabled", $(this))) {
				return;
			}

			if (opts.onClick)
				opts.onClick.call(buttonHTML, opts);

			e.stopPropagation();

		});

		disable(buttonHTML, opts.disabled);
	}

	function disable(buttonHTML, disabled) {
		var $button = $(buttonHTML);
		var state = $d(buttonHTML, "button");

		if (disabled) {
			state.options.disabled = true;
			var href = $button.attr("href");
			if (href) {
				state.href = href;
				$button.attr("href", "javascript:void(0)");
			}
			if (buttonHTML.onclick) {
				state.onclick = buttonHTML.onclick;
				buttonHTML.onclick = null;
			}
			$ac("l-btn-disabled", $button);
		} else {
			state.options.disabled = false;
			if (state.href) {
				$button.attr("href", state.href);
			}
			if (state.onclick) {
				buttonHTML.onclick = state.onclick;
			}
			$rc("l-btn-disabled", $button);
		}
	}

	$.fn.button = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.button.methods[options]
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.button');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "button");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "button", {
					options : $.extend({}, $.fn.button.defaults, $.fn.button.parseOptions(this), options)
				});
				$(this).removeAttr("disabled");
			}
			_init(this);
		});
	};

	$.fn.button.methods = {
		options : function(jq) {
			return $d(jq[0], "button").options;
		},
		enable : $X(disable, false),
		disable : $X(disable, true)
	};

	$.fn.button.parseOptions = function(buttonHTML) {
		var $button = $(buttonHTML);
		return $.extend({}, jCocit.parseOptions(buttonHTML, [ "id", "iconCls", "iconAlign", {
			plain : "b"
		} ]), {
			disabled : ($button.attr("disabled") ? true : undefined),
			text : $.trim($button.html()),
			iconCls : ($button.attr("icon") || $button.attr("iconCls"))
		});
	};

	$.fn.button.defaults = {
		id : null,
		disabled : false,
		plain : false,
		text : "",
		iconCls : null,
		iconAlign : "left"
	};
})(jQuery, jCocit);
