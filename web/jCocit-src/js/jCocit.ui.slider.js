/**
 * 
 */
(function($, jCocit) {
	function _initSlider(selfHTML) {
		var $slider = $('<div class="slider">' + '<div class="slider-inner">' + '<a href="javascript:void(0)" class="slider-handle"></a>' + '<span class="slider-tip"></span>' + '</div>' + '<div class="slider-rule"></div>' + '<div class="slider-rulelabel"></div>' + '<div style="clear:both"></div>' + '<input type="hidden" class="slider-value">' + '</div>').insertAfter(selfHTML);
		var name = $(selfHTML).hide().attr("name");
		if (name) {
			$f("input.slider-value", $slider).attr("name", name);
			$(selfHTML).removeAttr("name").attr("sliderName", name);
		}
		return $slider;
	}

	function resize(selfHTML, size) {
		var opts = $d(selfHTML, "slider").options;
		var $slider = $d(selfHTML, "slider").slider;
		if (size) {
			if (size.width) {
				opts.width = size.width;
			}
			if (size.height) {
				opts.height = size.height;
			}
		}
		if (opts.mode == "h") {
			$slider.css("height", "");
			$c("div", $slider).css("height", "");
			if (!isNaN(opts.width)) {
				$w(opts.width, $slider);
			}
		} else {
			$slider.css("width", "");
			$c("div", $slider).css("width", "");
			if (!isNaN(opts.height)) {
				$h(opts.height, $slider);
				$h(opts.height, $f("div.slider-rule", $slider));
				$h(opts.height, $f("div.slider-rulelabel", $slider));
				$f("div.slider-inner", $slider).oh(opts.height);
			}
		}
		_setValue(selfHTML);
	}

	function _initRule(selfHTML) {
		var opts = $d(selfHTML, "slider").options;
		var $slider = $d(selfHTML, "slider").slider;
		var ruleItems = opts.mode == "h" ? opts.rule : opts.rule.slice(0).reverse();
		if (opts.reversed) {
			ruleItems = ruleItems.slice(0).reverse();
		}
		ruleRuleItems(ruleItems);

		function ruleRuleItems(ruleItems) {
			var $rule = $f("div.slider-rule", $slider);
			var $ruleLabel = $f("div.slider-rulelabel", $slider);
			$rule.empty();
			$ruleLabel.empty();
			for ( var i = 0; i < ruleItems.length; i++) {
				var leftOrTop = i * 100 / (ruleItems.length - 1) + "%";
				var $ruleItem = $("<span></span>").appendTo($rule);
				$ruleItem.css((opts.mode == "h" ? "left" : "top"), leftOrTop);
				if (ruleItems[i] != "|") {
					$ruleItem = $("<span></span>").appendTo($ruleLabel);
					$ruleItem.html(ruleItems[i]);
					if (opts.mode == "h") {
						$ruleItem.css({
							left : leftOrTop,
							marginLeft : -Math.round($ow($ruleItem) / 2)
						});
					} else {
						$ruleItem.css({
							top : leftOrTop,
							marginTop : -Math.round($oh($ruleItem) / 2)
						});
					}
				}
			}
		}

	}

	function enable(selfHTML) {
		var opts = $d(selfHTML, "slider").options;
		var $slider = $d(selfHTML, "slider").slider;
		$rc("slider-h slider-v slider-disabled", $slider);
		$ac(opts.mode == "h" ? "slider-h" : "slider-v", $slider);
		$ac(opts.disabled ? "slider-disabled" : "", $slider);
		$f("a.slider-handle", $slider).draggable({
			axis : opts.mode,
			cursor : "pointer",
			disabled : opts.disabled,
			onDrag : function(e) {
				var leftOrTop = e.data.left;
				var widthOrHeight = $w($slider);
				if (opts.mode != "h") {
					leftOrTop = e.data.top;
					widthOrHeight = $h($slider);
				}
				if (leftOrTop < 0 || leftOrTop > widthOrHeight) {
					return false;
				} else {
					var value = _evalValue(selfHTML, leftOrTop);
					_changeValue(value);
					return false;
				}
			},
			onStartDrag : function() {
				opts.onSlideStart.call(selfHTML, opts.value);
			},
			onStopDrag : function(e) {
				var value = _evalValue(selfHTML, (opts.mode == "h" ? e.data.left : e.data.top));
				_changeValue(value);
				opts.onSlideEnd.call(selfHTML, opts.value);
			}
		});

		function _changeValue(value) {
			var s = Math.abs(value % opts.step);
			if (s < opts.step / 2) {
				value -= s;
			} else {
				value = value - s + opts.step;
			}
			setValue(selfHTML, value);
		}

	}

	function setValue(selfHTML, value) {
		var opts = $d(selfHTML, "slider").options;
		var $slider = $d(selfHTML, "slider").slider;
		var oldValue = opts.value;
		if (value < opts.min) {
			value = opts.min;
		}
		if (value > opts.max) {
			value = opts.max;
		}
		opts.value = value;
		$(selfHTML).val(value);
		$f("input.slider-value", $slider).val(value);
		var posLeftOrTop = _getPosLeftOrTop(selfHTML, value);
		var $tip = $f(".slider-tip", $slider);
		if (opts.showTip) {
			$tip.show();
			$tip.html(opts.tipFormatter.call(selfHTML, opts.value));
		} else {
			$tip.hide();
		}
		if (opts.mode == "h") {
			var styleText = "left:" + posLeftOrTop + "px;";
			$f(".slider-handle", $slider).attr("style", styleText);
			$tip.attr("style", styleText + "margin-left:" + (-Math.round($ow($tip) / 2)) + "px");
		} else {
			var styleText = "top:" + posLeftOrTop + "px;";
			$f(".slider-handle", $slider).attr("style", styleText);
			$tip.attr("style", styleText + "margin-left:" + (-Math.round($ow($tip))) + "px");
		}
		if (oldValue != value) {
			opts.onChange.call(selfHTML, value, oldValue);
		}
	}

	function _setValue(selfHTML) {
		var opts = $d(selfHTML, "slider").options;
		var fn = opts.onChange;
		opts.onChange = function() {
		};
		setValue(selfHTML, opts.value);
		opts.onChange = fn;
	}

	function _getPosLeftOrTop(selfHTML, value) {
		var opts = $d(selfHTML, "slider").options;
		var $slider = $d(selfHTML, "slider").slider;
		if (opts.mode == "h") {
			var pos = (value - opts.min) / (opts.max - opts.min) * $w($slider);
			if (opts.reversed) {
				pos = $w($slider) - pos;
			}
		} else {
			var pos = $h($slider) - (value - opts.min) / (opts.max - opts.min) * $h($slider);
			if (opts.reversed) {
				pos = $h($slider) - pos;
			}
		}
		return pos.toFixed(0);
	}

	function _evalValue(selfHTML, pos) {
		var opts = $d(selfHTML, "slider").options;
		var $slider = $d(selfHTML, "slider").slider;
		if (opts.mode == "h") {
			var value = opts.min + (opts.max - opts.min) * (pos / $w($slider));
		} else {
			var value = opts.min + (opts.max - opts.min) * (($h($slider) - pos) / $h($slider));
		}
		return opts.reversed ? opts.max - value.toFixed(0) : value.toFixed(0);
	}

	$.fn.slider = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.slider.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.slider');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "slider");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "slider", {
					options : $.extend({}, $.fn.slider.defaults, $.fn.slider.parseOptions(this), options),
					slider : _initSlider(this)
				});
				$(this).removeAttr("disabled");
			}
			enable(this);
			_initRule(this);
			resize(this);
		});
	};

	$.fn.slider.methods = {
		options : function(jq) {
			return $d(jq[0], "slider").options;
		},
		destroy : function(jq) {
			return jq.each(function() {
				$d(this, "slider").slider.remove();
				$(this).remove();
			});
		},
		/**
		 * arg: size -
		 */
		resize : $X(resize),
		getValue : function(jq) {
			return jq.slider("options").value;
		},
		/**
		 * arg: value -
		 */
		setValue : $X(setValue),
		enable : function(jq) {
			return jq.each(function() {
				$d(this, "slider").options.disabled = false;
				enable(this);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				$d(this, "slider").options.disabled = true;
				enable(this);
			});
		}
	};

	$.fn.slider.parseOptions = function(selfHTML) {
		var t = $(selfHTML);
		return $.extend({}, jCocit.parseOptions(selfHTML, [ "width", "height", "mode", {
			reversed : "b",
			showTip : "b",
			min : "n",
			max : "n",
			step : "n"
		} ]), {
			value : (t.val() || undefined),
			disabled : (t.attr("disabled") ? true : undefined),
			rule : (t.attr("rule") ? $fn(t.attr("rule")) : undefined)
		});
	};

	$.fn.slider.defaults = {
		width : "auto",
		height : "auto",
		mode : "h",
		reversed : false,
		showTip : false,
		disabled : false,
		value : 0,
		min : 0,
		max : 100,
		step : 1,
		rule : [],
		/**
		 * arg: value -
		 */
		tipFormatter : $n,
		/**
		 * arg:
		 * <UL>
		 * <LI>value -
		 * <LI>oldValue -
		 * </UL>
		 */
		onChange : $n,
		/**
		 * arg: value -
		 */
		onSlideStart : $n,
		/**
		 * arg: value -
		 */
		onSlideEnd : $n
	};
})(jQuery, jCocit);
