/**
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: calendar, panel
 * <LI>ReferencedBy:
 * <LI>SubClass: datetimebox
 * <LI>SuperClass: combo
 * </UL>
 * 
 * <pre>
 * 	[div class='Cb']
 * 		[div class='CbB combodate']
 * 			[input class='combodate-f Cb-f CbT' autocomplete='off' /]
 * 			[input class='CbA' type='text' /]
 * 		[/div]
 * 		[div class='Pn CbPn combodate-panel']
 * 			[div class='PnB PnB-NH PnB-NB CbPnB']
 * 				[div class='CbPnBC PnBC']
 * 					[div class='combodate-calendar-inner']
 * 						[div class='calendar calendar-noborder']
 * 							[div class='calendar-header']...[/div]
 * 							[div class='calendar-body']...[/div]
 * 						[/div]
 * 					[/div]
 * 					[div class='combodate-button']
 * 						[a class='combodate-today' href='javascript:void(0)']Today[/a]
 * 						[a class='combodate-close' href='javascript:void(0)']Close[/a]
 * 					[/div]
 * 				[/div]
 * 			[/div]
 * 		[/div]
 * 		[input class='CbV' type='hidden' value='']
 * 	[/div]
 * </pre>
 * 
 * <p>
 * <UL>
 * <LI>combodate:
 * <LI>combodate-panel:
 * <LI>combodate-calendar-inner:
 * <LI>calendar:
 * <LI>calendar-noborder:
 * <LI>combodate-button:
 * <LI>combodate-today:
 * <LI>combodate-close:
 * </UL>
 */
(function($, jCocit) {
	function _init(selfHTML) {
		var state = $d(selfHTML, "combodate");
		var opts = state.options;
		$ac("combodate-f", $(selfHTML));
		var $combo = $(selfHTML).combo($.extend({}, opts, {
			onInitPanel : function() {
				_initCalendar();
				if (opts.onInitPanel)
					opts.onInitPanel.call(selfHTML);
			},
			onShowPanel : function() {
				opts.onShowPanel.call(selfHTML);
				state.calendar.calendar("resize");
			}
		}));
		$ac("combodate", $p($(selfHTML).combo("textbox")));
		setValue(selfHTML, opts.value);

		function _initCalendar() {
			var $panel = $(selfHTML).combo("panel");
			$ac("combodate-panel", $panel.panel("panel"));
			state.calendar = $("<div></div>").appendTo($panel).wrap('<div class="combodate-calendar-inner"></div>');
			state.calendar.calendar({
				fit : true,
				border : false,
				onSelect : function(date) {
					var strDate = opts.formatter(date);
					setValue(selfHTML, strDate);
					$(selfHTML).combo("hidePanel");
					opts.onSelect.call(selfHTML, date);
				}
			});
			var $button = $('<div class="combodate-button"></div>').appendTo($panel);
			$('<a href="javascript:void(0)" class="combodate-today"></a>').html(opts.todayText).appendTo($button);
			$('<a href="javascript:void(0)" class="combodate-close"></a>').html(opts.closeText).appendTo($button);
			$f(".combodate-today,.combodate-close", $button).hover(function() {
				$ac("combodate-button-hover", $(this));
			}, function() {
				$rc("combodate-button-hover", $(this));
			});
			$f(".combodate-today", $button).click(function() {
				state.calendar.calendar({
					year : new Date().getFullYear(),
					month : new Date().getMonth() + 1,
					current : new Date()
				});
			});
			$f(".combodate-close", $button).click(function() {
				$(selfHTML).combo("hidePanel");
			});
		}

	}

	function _doQuery(selfHTML, keyword) {
		setValue(selfHTML, keyword);
	}

	function _doEnter(selfHTML) {
		var opts = $d(selfHTML, "combodate").options;
		var c = $d(selfHTML, "combodate").calendar;
		var strDate = opts.formatter(c.calendar("options").current);
		setValue(selfHTML, strDate);
		$(selfHTML).combo("hidePanel");
	}

	function setValue(selfHTML, strDate) {
		var state = $d(selfHTML, "combodate");
		var opts = state.options;
		$(selfHTML).combo("setValue", strDate).combo("setText", strDate);
		if (state.calendar)
			state.calendar.calendar("setValue", opts.parser(strDate));
	}

	$.fn.combodate = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.combodate.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.combo(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "combodate");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "combodate", {
					options : $.extend({}, $.fn.combodate.defaults, $.fn.combodate.parseOptions(this), options)
				});
			}
			_init(this);
		});
	};

	$.fn.combodate.methods = {
		options : function(jq) {
			var opts = $d(jq[0], "combodate").options;
			opts.originalValue = jq.combo("options").originalValue;
			return opts;
		},
		calendar : function(jq) {
			return $d(jq[0], "combodate").calendar;
		},
		setValue : $X(setValue),
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).combodate("options");
				$(this).combodate("setValue", opts.originalValue);
			});
		}
	};

	$.fn.combodate.parseOptions = function(selfHTML) {
		return $.extend({}, $.fn.combo.parseOptions(selfHTML), {});
	};

	$.fn.combodate.defaults = $.extend({}, $.fn.combo.defaults, {
		panelWidth : 200,
		panelHeight : "auto",
		todayText : "Today",
		closeText : "Close",
		okText : "Ok",
		keyHandler : {
			doKey : $n,
			doEnter : function() {
				_doEnter(this);
			},
			doQuery : function(keyword) {
				_doQuery(this, keyword);
			}
		},
		formatter : function(date) {
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			var d = date.getDate();
			return y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
		},
		parser : function(s) {
			var arr = s.split("-");
			if (arr.length) {
				return new Date(arr[0] || 0, (parseInt(arr[1], 10) || 1) - 1, parseInt(arr[2], 10) || 1);
			} else {
				return new Date();
			}
		},
		onSelect : $n,
		onInitPanel : $n
	});
})(jQuery, jCocit);
