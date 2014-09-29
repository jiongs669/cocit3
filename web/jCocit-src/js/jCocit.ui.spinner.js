/**
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: validatebox(TODO, Optional)
 * <LI>ReferencedBy: datetimebox
 * <LI>SubClass: spinnertime, spinnernumber
 * <LI>SuperClass:
 * </UL>
 * 
 * <pre>
 * 	[span class='spinner']
 * 		[span class='spinner-arrow']
 * 			[span class='spinner-arrow-up'][/span]
 * 			[span class='spinner-arrow-down'][/span]
 * 		[/span]
 * 	[/span]
 * </pre>
 * 
 * <UL>
 * <LI>spinner:
 * <LI>spinner-arrow:
 * <LI>spinner-arrow-up:
 * <LI>spinner-arrow-down:
 * </UL>
 */
(function($, jCocit) {

	var validatebox = $.fn.validatebox || false;

	/**
	 * Wrap the target "text" field to spinner object.
	 */
	function _init(targetHTML) {
		var $spinner = $('<span class="spinner"><span class="spinner-arrow"><span class="spinner-arrow-up"></span><span class="spinner-arrow-down"></span></span></span>').insertAfter(targetHTML);

		// prepend target "input" HTML element into the spinner
		$rc("spinner", $ac("spinner-text spinner-f", $(targetHTML))).prependTo($spinner);

		return $spinner;
	}

	/**
	 * Resize spinner width
	 */
	function resize(targetHTML, width) {
		var state = $d(targetHTML, "spinner");
		var opts = state.options;
		var $spinner = state.spinner;
		if (width) {
			opts.width = width;
		}

		// remember spinner position
		var $spinnerPosition = $('<div style="display:none"></div>').insertBefore($spinner);
		$spinner.appendTo("body");
		if (isNaN(opts.width)) {
			opts.width = $ow($(targetHTML));
		}

		// set spinner/input/arrow width and height
		var $arrow = $f(".spinner-arrow", $spinner);
		$spinner.ow(opts.width).oh(opts.height);
		$(targetHTML).ow($w($spinner) - $ow($arrow));
		$(targetHTML).css({
			height : $h($spinner) + "px",
			lineHeight : $h($spinner) + "px"
		});
		$arrow.oh($spinner.oh());
		$f("span", $arrow).oh($arrow.oh() / 2 + 1);

		// restore spinner position
		$spinner.insertAfter($spinnerPosition);

		//
		$spinnerPosition.remove();
	}

	/**
	 * Bind mouseenter/mouseleave/click events on spinner arrow-up/arrow-down
	 */
	function _bindSpinnerEvents(targetHTML) {
		var opts = $d(targetHTML, "spinner").options;
		var $spinner = $d(targetHTML, "spinner").spinner;
		$f(".spinner-arrow-up,.spinner-arrow-down", $spinner).unbind(".spinner");

		// spinner is enabled
		if (!opts.disabled) {

			// mouse enter/leave/click arrow-up
			$f(".spinner-arrow-up", $spinner).bind("mouseenter.spinner", function() {
				$ac("spinner-arrow-hover", $(this));
			}).bind("mouseleave.spinner", function() {
				$rc("spinner-arrow-hover", $(this));
			}).bind("click.spinner", function() {
				opts.spin.call(targetHTML, false);
				opts.onSpinUp.call(targetHTML);
				if (validatebox)
					$(targetHTML).validatebox("validate");
			});

			// mouse enter/leave/click arrow-down
			$f(".spinner-arrow-down", $spinner).bind("mouseenter.spinner", function() {
				$ac("spinner-arrow-hover", $(this));
			}).bind("mouseleave.spinner", function() {
				$rc("spinner-arrow-hover", $(this));
			}).bind("click.spinner", function() {
				opts.spin.call(targetHTML, true);
				opts.onSpinDown.call(targetHTML);
				if (validatebox)
					$(targetHTML).validatebox("validate");
			});

		}
	}

	/**
	 * Disable/Enable spinner UI
	 * <UL>
	 * <LI>disabled: true means that the spinner will be disabled. otherwise the spinner is enabled.
	 * </UL>
	 */
	function disable(targetHTML, disabled) {
		var state = $d(targetHTML, "spinner");
		var opts = state.options;
		var $spinner = state.spinner;
		if (disabled) {
			opts.disabled = true;
			$(targetHTML).attr("disabled", true);
			$ac("spinner-arrow-disabled", $f(".spinner-arrow", $spinner));
		} else {
			opts.disabled = false;
			$(targetHTML).removeAttr("disabled");
			$rc("spinner-arrow-disabled", $f(".spinner-arrow", $spinner));
		}
	}

	/**
	 * Create spinner object or invoke spinner method.
	 * <p>
	 * <b>Parameters:</b>
	 * <UL>
	 * <LI>options: type is "string" means spinner method will be invoked. otherwise spinner object will be created.
	 * <LI>args: it is arguments spinner methods needed.
	 * </UL>
	 */
	$.fn.spinner = function(options, args) {

		// invoke spinner method or validate spinner box
		if (typeof options == "string") {
			var fn = $.fn.spinner.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				if (validatebox)
					return this.validatebox(options, args);
				else
					$.error('The method ' + options + ' does not exist in $.fn.spinner');
			}
		}

		// create spinner
		options = options || {};
		return this.each(function() {
			var $this = $(this);
			var state = $d(this, "spinner");

			// set state and options of spinner
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "spinner", {
					options : $.extend({}, $.fn.spinner.defaults, $.fn.spinner.parseOptions(this), options),
					spinner : _init(this)
				});
				$this.removeAttr("disabled");
			}

			//
			state.options.originalValue = state.options.value;
			$this.val(state.options.value);
			$this.attr("readonly", !state.options.editable);

			//
			disable(this, state.options.disabled);
			resize(this);
			if (validatebox)
				$this.validatebox(state.options);

			// bind spinner events
			_bindSpinnerEvents(this);

		});

	};

	$.fn.spinner.methods = {
		options : function(jq) {
			var opts = $d(jq[0], "spinner").options;
			return $.extend(opts, {
				value : jq.val()
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var $spinner = $d(this, "spinner").spinner;
				if (validatebox)
					$(this).validatebox("destroy");
				$spinner.remove();
			});
		},
		resize : function(jq, width) {
			return jq.each(function() {
				resize(this, width);
			});
		},
		enable : function(jq) {
			return jq.each(function() {
				disable(this, false);
				_bindSpinnerEvents(this);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				disable(this, true);
				_bindSpinnerEvents(this);
			});
		},
		getValue : function(jq) {
			return jq.val();
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				var opts = $d(this, "spinner").options;
				opts.value = value;
				$(this).val(value);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				var opts = $d(this, "spinner").options;
				opts.value = "";
				$(this).val("");
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).spinner("options");
				$(this).spinner("setValue", opts.originalValue);
			});
		}
	};

	$.fn.spinner.parseOptions = function(targetHTML) {
		var t = $(targetHTML);
		return $.extend({}, validatebox ? validatebox.parseOptions(targetHTML) : {}, jCocit.parseOptions(targetHTML, [ "width", "height", "min", "max", {
			increment : "n",
			editable : "b"
		} ]), {
			value : (t.val() || undefined),
			disabled : (t.attr("disabled") ? true : undefined)
		});
	};

	$.fn.spinner.defaults = $.extend({}, validatebox ? validatebox.defaults : {}, {
		width : "auto",
		height : 22,
		value : "",
		min : null,
		max : null,
		increment : 1,
		editable : true,
		disabled : false,
		/**
		 * this method will be invoked when clicking the arrow-up/arrow-down of the spinner
		 * <p>
		 * arg: down - true means that the down-arrow be clicked.
		 */
		spin : $n,
		/**
		 * this method will be invoked when clicking the arrow-up of the spinner
		 */
		onSpinUp : $n,
		/**
		 * this method will be invoked when clicking the arrow-down of the spinner
		 */
		onSpinDown : $n
	});
})(jQuery, jCocit);
