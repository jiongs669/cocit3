/**
 * This is number box UI plugin.
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: validatebox(TODO, Optional)
 * <LI>ReferencedBy: spinnernumber
 * <LI>SubClass:
 * <LI>SuperClass:
 * </UL>
 */
(function($, jCocit) {

	var validatebox = $.fn.validatebox || false;

	/**
	 * Wrap the target "text" field to numberbox UI. a "hidden" field will be insert after of the target "text", the "hidden" field name is same with target "text".
	 * <p>
	 * the target "text" field will NOT be submit to remote server.
	 * <p>
	 * the insert "hidden" field will be submit to remote server.
	 * <P>
	 * <B>Return:</B> jQuery object of "hidden" field.
	 */
	function _initField(targetHTML) {
		$ac("numberbox-f", $(targetHTML));
		var $field = $('<input type="hidden">').insertAfter(targetHTML);
		var name = $(targetHTML).attr("name");
		if (name) {
			$field.attr("name", name);
			$(targetHTML).removeAttr("name").attr("numberboxName", name);
		}
		return $field;
	}

	/**
	 * Initialize the original value, don't trigger onChange event.
	 */
	function _init(targetHTML) {
		var opts = $d(targetHTML, "numberbox").options;

		// remember onChange function in options.
		var fn = opts.onChange;
		opts.onChange = function() {
		};

		fixValue(targetHTML, opts.parser.call(targetHTML, opts.value));

		// restore onChage function in options.
		opts.onChange = fn;

		opts.originalValue = getValue(targetHTML);
	}

	/**
	 * Get field value
	 * <p>
	 * <b>Return: </b>"hidden" field value
	 */
	function getValue(targetHTML) {
		return $d(targetHTML, "numberbox").field.val();
	}

	/**
	 * Fix target "text" field and "hidden" field with value specified by "newValue".
	 */
	function fixValue(targetHTML, newValue) {
		var state = $d(targetHTML, "numberbox");
		var opts = state.options;

		var value = getValue(targetHTML);
		newValue = opts.parser.call(targetHTML, newValue);

		opts.value = newValue;
		state.field.val(newValue);

		$(targetHTML).val(opts.formatter.call(targetHTML, newValue));

		if (value != newValue) {
			opts.onChange.call(targetHTML, newValue, value);
		}
	}

	/**
	 * Bind keypress/blur/focus events on the target HTML element "input".
	 */
	function _bindNumberBoxEvents(targetHTML) {
		var opts = $d(targetHTML, "numberbox").options;

		// bind keypress/blur/focus events
		$(targetHTML).unbind(".numberbox").bind("keypress.numberbox", function(e) {
			if (e.which == 45) {// key code is "-"
				if ($(this).val().indexOf("-") == -1) {
					return true;
				} else {
					return false;
				}
			}
			if (e.which == 46) {// key code is "."
				if ($(this).val().indexOf(".") == -1) {
					return true;
				} else {
					return false;
				}
			} else {
				// key code is number or
				if ((e.which >= 48 && e.which <= 57 && e.ctrlKey == false && e.shiftKey == false) || e.which == 0 || e.which == 8) {
					return true;
				} else {
					if (e.ctrlKey == true && (e.which == 99 || e.which == 118)) {
						return true;
					} else {
						return false;
					}
				}
			}
		}).bind("blur.numberbox", function() {
			fixValue(targetHTML, $(this).val());
			$(this).val(opts.formatter.call(targetHTML, getValue(targetHTML)));
		}).bind("focus.numberbox", function() {
			var vv = getValue(targetHTML);
			if ($(this).val() != vv) {
				$(this).val(vv);
			}
		});
	}

	/**
	 * Validate the target "text" field value with validatebox plugin.
	 */
	function _validate(targetHTML) {
		if (validatebox) {
			var opts = $d(targetHTML, "numberbox").options;
			$(targetHTML).validatebox(opts);
		}
	}

	/**
	 * Disable/Enable the target "text" field.
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>disabled: true means that the target "text" field will be disabled. otherwise it is enabled.
	 * </UL>
	 */
	function disable(targetHTML, disabled) {
		var opts = $d(targetHTML, "numberbox").options;
		if (disabled) {
			opts.disabled = true;
			$(targetHTML).attr("disabled", true);
		} else {
			opts.disabled = false;
			$(targetHTML).removeAttr("disabled");
		}
	}

	/**
	 * Format the number value specified by "numValue" to string value with "groupSeparator/decimalSeparator/prefix/suffix" properties in options.
	 * <P>
	 * <B>Return:</B> formated string value
	 */
	function format(numValue) {
		if (!numValue) {
			return numValue;
		}
		numValue = numValue + "";
		var opts = $(this).numberbox("options");
		var strIntegeral = numValue, strDecimals = "";
		var dotIndex = numValue.indexOf(".");
		if (dotIndex >= 0) {
			strIntegeral = numValue.substring(0, dotIndex);
			strDecimals = numValue.substring(dotIndex + 1, numValue.length);
		}
		if (opts.groupSeparator) {
			var p = /(\d+)(\d{3})/;
			while (p.test(strIntegeral)) {
				strIntegeral = strIntegeral.replace(p, "$1" + opts.groupSeparator + "$2");
			}
		}
		if (strDecimals) {
			return opts.prefix + strIntegeral + opts.decimalSeparator + strDecimals + opts.suffix;
		} else {
			return opts.prefix + strIntegeral + opts.suffix;
		}
	}

	/**
	 * Parse string value specified by "strValue" to number value with "groupSeparator/decimalSeparator/prefix/suffix/precision/min/max" properties in options.
	 * <p>
	 * <B>Return: </B> number value
	 */
	function parse(strValue) {
		strValue = strValue + "";
		var opts = $(this).numberbox("options");
		if (opts.groupSeparator) {
			strValue = strValue.replace(new RegExp("\\" + opts.groupSeparator, "g"), "");
		}
		if (opts.decimalSeparator) {
			strValue = strValue.replace(new RegExp("\\" + opts.decimalSeparator, "g"), ".");
		}
		if (opts.prefix) {
			strValue = strValue.replace(new RegExp("\\" + $.trim(opts.prefix), "g"), "");
		}
		if (opts.suffix) {
			strValue = strValue.replace(new RegExp("\\" + $.trim(opts.suffix), "g"), "");
		}
		strValue = strValue.replace(/\s/g, "");
		var numValue = parseFloat(strValue).toFixed(opts.precision);
		if (isNaN(numValue)) {
			numValue = "";
		} else {
			if (typeof (opts.min) == "number" && numValue < opts.min) {
				numValue = opts.min.toFixed(opts.precision);
			} else {
				if (typeof (opts.max) == "number" && numValue > opts.max) {
					numValue = opts.max.toFixed(opts.precision);
				}
			}
		}
		return numValue;
	}

	/**
	 * Create numberbox object or invoke numberbox method.
	 * <p>
	 * <b>Parameters:</b>
	 * <UL>
	 * <LI>options: type is "string" means numberbox method will be invoked. otherwise numberbox object will be created.
	 * <LI>args: it is arguments numberbox methods needed.
	 * </UL>
	 */
	$.fn.numberbox = function(options, args) {

		// invoke method specified by parameter "options"
		if (typeof options == "string") {
			var fn = $.fn.numberbox.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				if (validatebox)
					return this.validatebox(options, args);
				else
					$.error('The method ' + options + ' does not exist in $.fn.numberbox');
			}
		}

		// create numberbox
		options = options || {};
		return this.each(function() {
			var state = $d(this, "numberbox");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "numberbox", {
					options : $.extend({}, $.fn.numberbox.defaults, $.fn.numberbox.parseOptions(this), options),
					field : _initField(this)
				});
				$(this).removeAttr("disabled");
				$(this).css({
					imeMode : "disabled"
				});
			}
			disable(this, state.options.disabled);
			_bindNumberBoxEvents(this);
			_validate(this);
			_init(this);
		});
	};

	$.fn.numberbox.methods = {
		options : function(jq) {
			return $d(jq[0], "numberbox").options;
		},
		destroy : function(jq) {
			return jq.each(function() {
				$d(this, "numberbox").field.remove();
				if (validatebox)
					$(this).validatebox("destroy");
				$(this).remove();
			});
		},
		disable : $X(disable, true),
		enable : $X(disable, false),
		fix : function(jq) {
			return jq.each(function() {
				fixValue(this, $(this).val());
			});
		},
		/**
		 * arg: numValue -
		 */
		setValue : $X(fixValue),
		getValue : $x(getValue),
		clear : function(jq) {
			return jq.each(function() {
				var state = $d(this, "numberbox");
				state.field.val("");
				$(this).val("");
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).numberbox("options");
				$(this).numberbox("setValue", opts.originalValue);
			});
		}
	};

	$.fn.numberbox.parseOptions = function(targetHTML) {
		var $target = $(targetHTML);
		return $.extend({}, validatebox ? validatebox.parseOptions(targetHTML) : {}, jCocit.parseOptions(targetHTML, [ "decimalSeparator", "groupSeparator", "suffix", {
			min : "n",
			max : "n",
			precision : "n"
		} ]), {
			prefix : ($target.attr("prefix") ? $target.attr("prefix") : undefined),
			disabled : ($target.attr("disabled") ? true : undefined),
			value : ($target.val() || undefined)
		});
	};

	$.fn.numberbox.defaults = $.extend({}, validatebox ? validatebox.defaults : {}, {
		disabled : false,
		value : "",
		min : null,
		max : null,
		/**
		 * eg: 1234567.89123 will be format to 1234567.89 if precision equals 2
		 */
		precision : 0,
		/**
		 * eg: 1234567.89 will be format to 1234567,89 by decimal separator ","
		 */
		decimalSeparator : ".",
		/**
		 * eg: 1234567.89 will be format to 1,234,567.89 by group separator ","
		 */
		groupSeparator : "",
		/**
		 * eg: 1234567.89 will be format to $1234567.89 by prefix "$"
		 */
		prefix : "",
		/**
		 * eg: 1234567.89 will be format to 1234567.89YUAN by suffix "YUAN"
		 */
		suffix : "",
		formatter : format,
		parser : parse,
		/**
		 * args:
		 * <UL>
		 * <LI>newValue:
		 * <LI>oldValue:
		 * </UL>
		 */
		onChange : $n
	});
})(jQuery, jCocit);
