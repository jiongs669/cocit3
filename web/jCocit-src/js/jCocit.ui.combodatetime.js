/**
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: spinnertime
 * <LI>ReferencedBy:
 * <LI>SubClass:
 * <LI>SuperClass: combodate
 * </UL>
 */
(function($) {
	function _init(selfHTML) {
		var state = $d(selfHTML, "combodatetime");
		var opts = state.options;
		$(selfHTML).combodate($.extend({}, opts, {
			onInitPanel : function() {
				_initTimeSpinner();
				if (opts.onInitPanel)
					opts.onInitPanel.call(selfHTML);
			},
			onShowPanel : function() {
				var strDateTime = $(selfHTML).combodatetime("getValue");
				setValue(selfHTML, strDateTime, true);
				opts.onShowPanel.call(selfHTML);
			},
			formatter : $.fn.combodate.defaults.formatter,
			parser : $.fn.combodate.defaults.parser
		}));
		$ac("combodatetime-f", $rc("combodate-f", $(selfHTML)));
		setValue(selfHTML, opts.value);

		function _initTimeSpinner() {
			$(selfHTML).combodate("calendar").calendar({
				onSelect : function(date) {
					opts.onSelect.call(selfHTML, date);
				}
			});

			var $panel = $(selfHTML).combodate("panel");
			if (!state.spinner) {
				var p = $('<div style="padding:2px"><input style="width:80px"></div>').insertAfter($c("div.combodate-calendar-inner", $panel));
				state.spinner = $c("input", p);
				var $btnBar = $c("div.combodate-button", $panel);
				var $okBtn = $('<a href="javascript:void(0)" class="combodate-ok"></a>').html(opts.okText).appendTo($btnBar);
				$okBtn.hover(function() {
					$ac("combodate-button-hover", $(this));
				}, function() {
					$rc("combodate-button-hover", $(this));
				}).click(function() {
					doEnter(selfHTML);
				});
			}

			state.spinner.spinnertime({
				showSeconds : opts.showSeconds,
				separator : opts.timeSeparator
			}).unbind(".combodatetime").bind("mousedown.combodatetime", function(e) {
				e.stopPropagation();
			});
		}
	}

	function _getDate(selfHTML) {
		var $calendar = $(selfHTML).combodatetime("calendar");
		var $spinner = $(selfHTML).combodatetime("spinner");
		var currentDate = $calendar.calendar("getValue");
		return new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), $spinner.spinnertime("getHours"), $spinner.spinnertime("getMinutes"), $spinner.spinnertime("getSeconds"));
	}

	function doQuery(selfHTML, keyword) {
		setValue(selfHTML, keyword, true);
	}

	function doEnter(selfHTML) {
		var opts = $d(selfHTML, "combodatetime").options;
		var date = _getDate(selfHTML);
		setValue(selfHTML, opts.formatter.call(selfHTML, date));
		$(selfHTML).combo("hidePanel");
	}

	function setValue(target, strDate, ignoreFormat) {
		var opts = $d(target, "combodatetime").options;
		$(target).combo("setValue", strDate);
		if (!ignoreFormat) {
			if (strDate) {
				var date = opts.parser.call(target, strDate);
				$(target).combo("setValue", opts.formatter.call(target, date));
				$(target).combo("setText", opts.formatter.call(target, date));
			} else {
				$(target).combo("setText", strDate);
			}
		}
		var date = opts.parser.call(target, strDate);
		var $calendar = $(target).combodatetime("calendar");
		if ($calendar)
			$calendar.calendar("setValue", date);
		var $spinner = $(target).combodatetime("spinner");
		if ($spinner)
			$spinner.spinnertime("setValue", _getTime(date));

		function _getTime(date) {
			function _timeToString(iValue) {
				return (iValue < 10 ? "0" : "") + iValue;
			}

			var strTime = [ _timeToString(date.getHours()), _timeToString(date.getMinutes()) ];
			if (opts.showSeconds) {
				strTime.push(_timeToString(date.getSeconds()));
			}
			return strTime.join($(target).combodatetime("spinner").spinnertime("options").separator);
		}

	}

	$.fn.combodatetime = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.combodatetime.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.combodate(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "combodatetime");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "combodatetime", {
					options : $.extend({}, $.fn.combodatetime.defaults, $.fn.combodatetime.parseOptions(this), options)
				});
			}
			_init(this);
		});
	};

	$.fn.combodatetime.methods = {
		options : function(jq) {
			var opts = $d(jq[0], "combodatetime").options;
			opts.originalValue = jq.combodate("options").originalValue;
			return opts;
		},
		spinner : function(jq) {
			return $d(jq[0], "combodatetime").spinner;
		},
		setValue : $X(setValue),
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).combodatetime("options");
				$(this).combodatetime("setValue", opts.originalValue);
			});
		}
	};

	$.fn.combodatetime.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.combodate.parseOptions(target), jCocit.parseOptions(target, [ "timeSeparator", {
			showSeconds : "b"
		} ]));
	};

	$.fn.combodatetime.defaults = $.extend({}, $.fn.combodate.defaults, {
		showSeconds : true,
		timeSeparator : ":",
		keyHandler : {
			doKey : $n,
			doEnter : function() {
				doEnter(this);
			},
			doQuery : function(keyword) {
				doQuery(this, keyword);
			}
		},
		formatter : function(date) {
			var h = date.getHours();
			var M = date.getMinutes();
			var s = date.getSeconds();

			function formatTime(val) {
				return (val < 10 ? "0" : "") + val;
			}

			var sep = $(this).combodatetime("options").timeSeparator;
			var r = $.fn.combodate.defaults.formatter(date) + " " + formatTime(h) + sep + formatTime(M);
			if ($(this).combodatetime("options").showSeconds) {
				r += sep + formatTime(s);
			}
			return r;
		},
		parser : function(s) {
			if ($.trim(s) == "") {
				return new Date();
			}
			var dt = s.split(" ");
			var d = $.fn.combodate.defaults.parser(dt[0]);
			if (dt.length < 2) {
				return d;
			}
			var sep = $(this).combodatetime("options").timeSeparator;
			var tt = dt[1].split(sep);
			var HH = parseInt(tt[0], 10) || 0;
			var mm = parseInt(tt[1], 10) || 0;
			var ss = parseInt(tt[2], 10) || 0;
			return new Date(d.getFullYear(), d.getMonth(), d.getDate(), HH, mm, ss);
		}
	});

})(jQuery);
