/**
 * This is number spinner UI plug-in.
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: numberbox, validatebox(TODO, Optional)
 * <LI>ReferencedBy:
 * <LI>SubClass:
 * <LI>SuperClass: spinner
 * </UL>
 * 
 * <pre>
 * 	[span class='spinner']
 * 		[input class='spinnernumber-f spinner-text spinner-f numberbox-f']
 * 		[input type='hidden' value='']
 * 		[span class='spinner-arrow']
 * 			[span class='spinner-arrow-up'][/span]
 * 			[span class='spinner-arrow-down'][/span]
 * 		[/span]
 * 	[/span]
 * </pre>
 */
(function($, jCocit) {

	/**
	 * wrap target "input" HTML element to number spinner UI
	 */
	function _init(target) {
		$ac("spinnernumber-f", $(target));
		var opts = $d(target, "spinnernumber").options;
		$(target).spinner(opts).numberbox(opts);
	}

	/**
	 * Process spinner arrow events
	 */
	function _doSpin(target, down) {
		var opts = $d(target, "spinnernumber").options;
		var v = parseFloat($(target).numberbox("getValue") || opts.value) || 0;
		if (down == true) {
			v -= opts.increment;
		} else {
			v += opts.increment;
		}
		$(target).numberbox("setValue", v);
	}

	/**
	 * Create spinnernumber object or invoke spinnernumber method.
	 * <p>
	 * <b>Parameters:</b>
	 * <UL>
	 * <LI>options: type is "string" means spinnernumber method will be invoked. otherwise spinnernumber object will be created.
	 * <LI>args: it is arguments spinnernumber methods needed.
	 * </UL>
	 */
	$.fn.spinnernumber = function(options, args) {
		if (typeof options == "string") {
			var fn = methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.spinner(options, args);
			}
		}

		options = options || {};
		return this.each(function() {
			var state = $d(this, "spinnernumber");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "spinnernumber", {
					options : $.extend({}, defaults, parseOptions(this), options)
				});
			}
			_init(this);
		});
	};

	var methods = {
		options : function(jq) {
			var opts = $d(jq[0], "spinnernumber").options;
			return $.extend(opts, {
				value : jq.numberbox("getValue"),
				originalValue : jq.numberbox("options").originalValue
			});
		},
		setValue : function(jq, numValue) {
			return jq.each(function() {
				$(this).numberbox("setValue", numValue);
			});
		},
		getValue : function(jq) {
			return jq.numberbox("getValue");
		},
		clear : function(jq) {
			return jq.each(function() {
				$(this).spinner("clear");
				$(this).numberbox("clear");
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).spinnernumber("options");
				$(this).spinnernumber("setValue", opts.originalValue);
			});
		}
	};

	var parseOptions = function(target) {
		return $.extend({}, $.fn.spinner.parseOptions(target), $.fn.numberbox.parseOptions(target), {});
	};

	var defaults = $.extend({}, $.fn.spinner.defaults, $.fn.numberbox.defaults, {
		spin : function(down) {
			_doSpin(this, down);
		}
	});
})(jQuery, jCocit);
