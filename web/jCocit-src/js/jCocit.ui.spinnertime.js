/**
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference:
 * <LI>ReferencedBy: datetimebox
 * <LI>SubClass:
 * <LI>SuperClass: spinner
 * </UL>
 */
(function($, jCocit) {
	function _init(selfHTML) {
		var opts = $d(selfHTML, "spinnertime").options;
		$ac("spinnertime-f", $(selfHTML));
		$(selfHTML).spinner(opts);
		$(selfHTML).unbind(".spinnertime");
		$(selfHTML).bind("click.spinnertime", function() {
			var selectionIndex = 0;
			if (this.selectionStart != null) {
				selectionIndex = this.selectionStart;
			} else {
				if (this.createTextRange) {
					var textRange = selfHTML.createTextRange();
					var range = document.selection.createRange();
					range.setEndPoint("StartToStart", textRange);
					selectionIndex = range.text.length;
				}
			}
			if (selectionIndex >= 0 && selectionIndex <= 2) {
				opts.highlight = 0;
			} else {
				if (selectionIndex >= 3 && selectionIndex <= 5) {
					opts.highlight = 1;
				} else {
					if (selectionIndex >= 6 && selectionIndex <= 8) {
						opts.highlight = 2;
					}
				}
			}
			_selectTimeRange(selfHTML);
		}).bind("blur.spinnertime", function() {
			setValue(selfHTML);
		});
	}

	function _selectTimeRange(selfHTML) {
		var opts = $d(selfHTML, "spinnertime").options;
		var from = 0, to = 0;
		if (opts.highlight == 0) {
			from = 0;
			to = 2;
		} else {
			if (opts.highlight == 1) {
				from = 3;
				to = 5;
			} else {
				if (opts.highlight == 2) {
					from = 6;
					to = 8;
				}
			}
		}
		if (selfHTML.selectionStart != null) {
			selfHTML.setSelectionRange(from, to);
		} else {
			if (selfHTML.createTextRange) {
				var textRange = selfHTML.createTextRange();
				textRange.collapse();
				textRange.moveEnd("character", to);
				textRange.moveStart("character", from);
				textRange.select();
			}
		}
		$(selfHTML).focus();
	}

	function _parseTime(selfHTML, strTime) {
		var opts = $d(selfHTML, "spinnertime").options;
		if (!strTime) {
			return null;
		}
		var timeArray = strTime.split(opts.separator);
		for ( var i = 0; i < timeArray.length; i++) {
			if (isNaN(timeArray[i])) {
				return null;
			}
		}
		while (timeArray.length < 3) {
			timeArray.push(0);
		}
		return new Date(1900, 0, 0, timeArray[0], timeArray[1], timeArray[2]);
	}

	function setValue(selfHTML) {
		var opts = $d(selfHTML, "spinnertime").options;
		var strTime = $(selfHTML).val();
		var timeObj = _parseTime(selfHTML, strTime);
		if (!timeObj) {
			timeObj = _parseTime(selfHTML, opts.value);
		}
		if (!timeObj) {
			opts.value = "";
			$(selfHTML).val("");
			return;
		}
		var minTime = _parseTime(selfHTML, opts.min);
		var maxTime = _parseTime(selfHTML, opts.max);
		if (minTime && minTime > timeObj) {
			timeObj = minTime;
		}
		if (maxTime && maxTime < timeObj) {
			timeObj = maxTime;
		}
		var timeArray = [ _timeToString(timeObj.getHours()), _timeToString(timeObj.getMinutes()) ];
		if (opts.showSeconds) {
			timeArray.push(_timeToString(timeObj.getSeconds()));
		}
		var val = timeArray.join(opts.separator);
		opts.value = val;
		$(selfHTML).val(val);

		function _timeToString(iValue) {
			return (iValue < 10 ? "0" : "") + iValue;
		}

	}

	function doSpin(selfHTML, down) {
		var opts = $d(selfHTML, "spinnertime").options;
		var strTime = $(selfHTML).val();
		if (strTime == "") {
			strTime = [ 0, 0, 0 ].join(opts.separator);
		}
		var timeArray = strTime.split(opts.separator);
		for ( var i = 0; i < timeArray.length; i++) {
			timeArray[i] = parseInt(timeArray[i], 10);
		}
		if (down == true) {
			timeArray[opts.highlight] -= opts.increment;
		} else {
			timeArray[opts.highlight] += opts.increment;
		}
		$(selfHTML).val(timeArray.join(opts.separator));
		setValue(selfHTML);
		_selectTimeRange(selfHTML);
	}

	$.fn.spinnertime = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.spinnertime.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.spinner(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "spinnertime");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "spinnertime", {
					options : $.extend({}, $.fn.spinnertime.defaults, $.fn.spinnertime.parseOptions(this), options)
				});
				_init(this);
			}
		});
	};

	$.fn.spinnertime.methods = {
		options : function(jq) {
			var opts = $d(jq[0], "spinnertime").options;
			return $.extend(opts, {
				value : jq.val(),
				originalValue : jq.spinner("options").originalValue
			});
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				$(this).val(value);
				setValue(this);
			});
		},
		getHours : function(jq) {
			var opts = $d(jq[0], "spinnertime").options;
			var timeArray = jq.val().split(opts.separator);
			return parseInt(timeArray[0], 10);
		},
		getMinutes : function(jq) {
			var opts = $d(jq[0], "spinnertime").options;
			var timeArray = jq.val().split(opts.separator);
			return parseInt(timeArray[1], 10);
		},
		getSeconds : function(jq) {
			var opts = $d(jq[0], "spinnertime").options;
			var timeArray = jq.val().split(opts.separator);
			return parseInt(timeArray[2], 10) || 0;
		}
	};

	$.fn.spinnertime.parseOptions = function(selfHTML) {
		return $.extend({}, $.fn.spinner.parseOptions(selfHTML), jCocit.parseOptions(selfHTML, [ "separator", {
			showSeconds : "b",
			highlight : "n"
		} ]));
	};

	$.fn.spinnertime.defaults = $.extend({}, $.fn.spinner.defaults, {
		separator : ":",
		showSeconds : false,
		highlight : 0,
		spin : function(down) {
			doSpin(this, down);
		}
	});
})(jQuery, jCocit);
