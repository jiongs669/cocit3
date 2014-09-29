/**
 * This is calendar UI plugin, used to present calendar in the target "DIV".
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference:
 * <LI>ReferencedBy: combodate, datetimebox
 * <LI>SubClass:
 * <LI>SuperClass:
 * </UL>
 * 
 * <pre>
 * 	[div class='calendar']
 * 		[div class='calendar-header']
 * 			[div class='calendar-prevyear'][/div]
 * 			[div class='calendar-prevmonth'][/div]
 * 			[div class='calendar-title'][span]Aprial 2010[/span][/div]
 * 			[div class='calendar-nextmonth'][/div]
 * 			[div class='calendar-nextyear'][/div]
 * 		[/div]
 * 		[div class='calendar-body']
 * 			[div class='calendar-menu']
 * 				[div class='calendar-menu-year-inner']
 * 					[span class='calendar-menu-prev'][/span]
 * 					[span][input class='calendar-menu-year' type='text'][/input][/span]
 * 					[span class='calendar-menu-next'][/span]
 * 				[/div]
 * 				[div class='calendar-menu-month-inner']
 * 					[table]
 * 						[tr][td class='calendar-menu-month'][/td][td][/td][td][/td][td][/td][/tr]
 * 						[tr][td][/td][td][/td][td][/td][td][/td][/tr]
 * 						[tr][td][/td][td][/td][td][/td][td][/td][/tr]
 * 					[/table]
 * 				[/div]
 * 			[/div]
 * 			[table]
 * 				[thead]
 * 					[tr][th][/th][th][/th][th][/th][th][/th][th][/th][th][/th][th][/th][/tr]
 * 				[/thead]
 * 				[tbody]
 * 					[tr][td class='calendar-day calendar-other-month'][/td][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][/tr]
 * 					[tr][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][/tr]
 * 					[tr][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][/tr]
 * 					[tr][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][/tr]
 * 					[tr][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][/tr]
 * 					[tr][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][td][/td][/tr]
 * 				[/tbody]
 * 			[/table]
 * 		[/div]
 * 	[/div]
 * </pre>
 * 
 * <p>
 * <UL>
 * <LI>calendar: this is target "DIV".
 * <LI>calendar-menu: default this menu is hidden. it will be shown when clicking "calendar-title" element.
 * <LI>calendar-header:
 * <LI>calendar-prevyear: the calendar body table render previous year when clicking it.
 * <LI>calendar-prevmonth: the calendar body table render previous month when clicking it.
 * <LI>calendar-title:
 * <LI>calendar-nextmonth: the calendar body table render next month when clicking it.
 * <LI>calendar-nextyear: the calendar body table render next year when clicking it.
 * <LI>calendar-body:
 * <LI>calendar-menu: this is calendar menu used to select year and month.
 * <LI>calendar-body > table: this is calendar body table used to present the current month dates. this table contains 6 rows 7 columns.
 * <LI>calendar-menu > table: this is calendar menu table used to present months menu. this table contains 3 rows 4 columns.
 * </UL>
 */
(function($, jCocit) {

	/**
	 * Resize the target to size of the parent DIV or size specified by options
	 */
	function resize(targetDIV) {
		var opts = $d(targetDIV, "calendar").options;
		var $target = $(targetDIV);

		// set size to parent
		if (opts.fit == true) {
			var p = $p($target);
			opts.width = $w(p);
			opts.height = $h(p);
		}

		var $header = $f(".calendar-header", $target);

		// set the target "DIV" width/height
		$target.ow(opts.width);
		$target.oh(opts.height);

		$f(".calendar-body", $target).oh($h($target) - $header.oh());
	}

	/**
	 * Bind hover/click events on the calendar title and calendar header buttons.
	 */
	function _bindCalendarEvents(targetDIV) {
		var $target = $(targetDIV);

		$ac("calendar", $target).wrapInner(
				'<div class="calendar-header">' + '<div class="calendar-prevmonth"></div>' + '<div class="calendar-nextmonth"></div>' + '<div class="calendar-prevyear"></div>' + '<div class="calendar-nextyear"></div>' + '<div class="calendar-title"><span>Aprial 2010</span></div>' + '</div>' + '<div class="calendar-body">' + '<div class="calendar-menu">' + '<div class="calendar-menu-year-inner">' + '<span class="calendar-menu-prev"></span>'
						+ '<span><input class="calendar-menu-year" type="text"></input></span>' + '<span class="calendar-menu-next"></span>' + '</div>' + '<div class="calendar-menu-month-inner"></div>' + '</div>' + '</div>');

		// bind hover/click events on the calendar title
		$f(".calendar-title span", $target).hover(function() {
			$ac("calendar-menu-hover", $(this));
		}, function() {
			$rc("calendar-menu-hover", $(this));
		}).click(function() {
			var $menu = $f(".calendar-menu", $target);
			if ($menu.is(":visible")) {
				$menu.hide();
			} else {
				_openMonthMenu(targetDIV);
			}
		});

		// bind hover/click events on the calendar header buttons
		$(".calendar-prevmonth,.calendar-nextmonth,.calendar-prevyear,.calendar-nextyear", targetDIV).hover(function() {
			$ac("calendar-nav-hover", $(this));
		}, function() {
			$rc("calendar-nav-hover", $(this));
		});
		$f(".calendar-nextmonth", $target).click(function() {
			_increaseMonth(targetDIV, 1);
		});
		$f(".calendar-prevmonth", $target).click(function() {
			_increaseMonth(targetDIV, -1);
		});
		$f(".calendar-nextyear", $target).click(function() {
			_increaseYear(targetDIV, 1);
		});
		$f(".calendar-prevyear", $target).click(function() {
			_increaseYear(targetDIV, -1);
		});

		// bind resize event on calendar
		$target.bind("_resize", function() {
			var opts = $d(targetDIV, "calendar").options;
			if (opts.fit == true) {
				resize(targetDIV);
			}
			return false;
		});
	}

	/**
	 * Increase month
	 */
	function _increaseMonth(targetDIV, v) {
		var opts = $d(targetDIV, "calendar").options;

		// increase month
		opts.month += v;
		if (opts.month > 12) {
			opts.year++;
			opts.month = 1;
		} else {
			if (opts.month < 1) {
				opts.year--;
				opts.month = 12;
			}
		}

		// append month day table to calendar body.
		_appendMonthDayTable(targetDIV);

		// set selected month style of calendar menu
		var $monthInner = $f(".calendar-menu-month-inner", $(targetDIV));
		$rc("calendar-selected", $f("td.calendar-selected", $monthInner));
		$ac("calendar-selected", $f("td:eq(" + (opts.month - 1) + ")", $monthInner));
	}

	/**
	 * Increase year
	 */
	function _increaseYear(targetDIV, v) {
		var opts = $d(targetDIV, "calendar").options;

		// increase month
		opts.year += v;

		// append month day table to calendar body.
		_appendMonthDayTable(targetDIV);

		// set year value for calendar menu
		var $menuYear = $f(".calendar-menu-year", $(targetDIV));
		$menuYear.val(opts.year);
	}

	/**
	 * Open the calendar month menu, the calendar month menu contains year "text" box and month menu table.
	 * <p>
	 * the month menu table contains 3 rows 4 columns.
	 * <p>
	 * It will be shown when clicking the calendar title.
	 */
	function _openMonthMenu(targetDIV) {
		var $target = $(targetDIV);
		var opts = $d(targetDIV, "calendar").options;

		// show calendar menu
		$f(".calendar-menu", $target).show();

		// append calendar month menu table
		if ($f(".calendar-menu-month-inner", $target).is(":empty")) {
			$f(".calendar-menu-month-inner", $target).empty();

			// append month menu to calendar body
			var $menuMonthTable = $("<table></table>").appendTo($f(".calendar-menu-month-inner", $target));
			var idx = 0;
			for ( var i = 0; i < 3; i++) {
				var tr = $('<tr></tr>').appendTo($menuMonthTable);
				for ( var j = 0; j < 4; j++) {
					$('<td class="calendar-menu-month"></td>').html(opts.months[idx++]).attr("abbr", idx).appendTo(tr);
				}
			}

			// month menu:
			$f(".calendar-menu-prev,.calendar-menu-next", $target).hover(function() {
				$ac("calendar-menu-hover", $(this));
			}, function() {
				$rc("calendar-menu-hover", $(this));
			});

			// click menu next-icon to change year
			$f(".calendar-menu-next", $target).click(function() {
				var y = $f(".calendar-menu-year", $target);
				if (!isNaN(y.val())) {
					y.val(parseInt(y.val()) + 1);
				}
			});

			// click menu prev-icon to change year
			$f(".calendar-menu-prev", $target).click(function() {
				var y = $f(".calendar-menu-year", $target);
				if (!isNaN(y.val())) {
					y.val(parseInt(y.val() - 1));
				}
			});

			// year menu events
			$f(".calendar-menu-year", $target).keypress(function(e) {
				if (e.keyCode == 13) {
					_hideMenu();
				}
			});

			// month menu events
			$f(".calendar-menu-month", $target).hover(function() {
				$ac("calendar-menu-hover", $(this));
			}, function() {
				$rc("calendar-menu-hover", $(this));
			}).click(function() {
				var $menu = $f(".calendar-menu", $target);
				$rc("calendar-selected", $f(".calendar-selected", $menu));
				$ac("calendar-selected", $(this));
				_hideMenu();
			});
		}

		function _hideMenu() {
			var $menu = $f(".calendar-menu", $target);
			var year = $f(".calendar-menu-year", $menu).val();
			var month = $f(".calendar-selected", $menu).attr("abbr");
			if (!isNaN(year)) {
				opts.year = parseInt(year);
				opts.month = parseInt(month);
				_appendMonthDayTable(targetDIV);
			}
			$menu.hide();
		}

		var $body = $f(".calendar-body", $target);
		var $menu = $f(".calendar-menu", $target);
		var $menuYearInner = $f(".calendar-menu-year-inner", $menu);
		var $menuMonthInner = $f(".calendar-menu-month-inner", $menu);

		// selected the current year and month
		$f("input", $menuYearInner).val(opts.year).focus();
		$rc("calendar-selected", $f("td.calendar-selected", $menuMonthInner));
		$ac("calendar-selected", $f("td:eq(" + (opts.month - 1) + ")", $menuMonthInner));

		// set month menu height and width
		$menu.ow($body.ow());
		$menu.oh($body.oh());
		$menuMonthInner.oh($h($menu) - $menuYearInner.oh());
	}

	/**
	 * Get month weeks array, this weeks array is 6X7 array.
	 * <p>
	 * array[i] is a week, contains 7 days
	 * <P>
	 * array[i][i] is a date.
	 * <P>
	 * <B>Return: </B>the weeks array.
	 */
	function _getMonthWeeks(targetDIV, year, month) {
		var opts = $d(targetDIV, "calendar").options;

		// get the current month last date, can be 28/29/30/31, and create dates (year/month/date) array
		var monthDates = [];
		var date = new Date(year, month, 0).getDate();
		for ( var i = 1; i <= date; i++) {
			monthDates.push([ year, month, i ]);
		}

		var monthWeeks = [], weekDates = [];

		// lastDay is the last day in this week, that's the last day in the current row.
		var lastDay = -1;
		while (monthDates.length > 0) {

			// pop the first date element from array and push week dates array
			var monthDate = monthDates.shift();
			weekDates.push(monthDate);

			// can be 0-6
			var day = new Date(monthDate[0], monthDate[1] - 1, monthDate[2]).getDay();

			if (lastDay == day) {
				day = 0;
			} else {
				if (day == (opts.firstDay == 0 ? 7 : opts.firstDay) - 1) {
					monthWeeks.push(weekDates);
					weekDates = [];
				}
			}

			lastDay = day;
		}

		if (weekDates.length) {
			monthWeeks.push(weekDates);
		}

		var firstWeekDates = monthWeeks[0];

		// fill the first week dates array to 7 with the previous month dates.
		if (firstWeekDates.length < 7) {
			while (firstWeekDates.length < 7) {
				var weekDate = firstWeekDates[0];

				// 
				var prevDateObj = new Date(weekDate[0], weekDate[1] - 1, weekDate[2] - 1);

				// add previous date to the first week dates array
				firstWeekDates.unshift([ prevDateObj.getFullYear(), prevDateObj.getMonth() + 1, prevDateObj.getDate() ]);
			}
		} else {

			// add a week dates to the month weeks
			var weekDate = firstWeekDates[0];
			var weekDates = [];
			for ( var i = 1; i <= 7; i++) {
				var dateObj = new Date(weekDate[0], weekDate[1] - 1, weekDate[2] - i);
				weekDates.unshift([ dateObj.getFullYear(), dateObj.getMonth() + 1, dateObj.getDate() ]);
			}

			// add to the first of the week dates array
			monthWeeks.unshift(weekDates);
		}

		var lastWeekDates = monthWeeks[monthWeeks.length - 1];

		// fill the last week dates array to 7 with the next month dates
		while (lastWeekDates.length < 7) {
			var weekDate = lastWeekDates[lastWeekDates.length - 1];
			var nextDateObj = new Date(weekDate[0], weekDate[1] - 1, weekDate[2] + 1);
			lastWeekDates.push([ nextDateObj.getFullYear(), nextDateObj.getMonth() + 1, nextDateObj.getDate() ]);
		}

		// fill to 6 weeks
		if (monthWeeks.length < 6) {
			var lastWeekDate = lastWeekDates[lastWeekDates.length - 1];
			var weekDates = [];
			for ( var i = 1; i <= 7; i++) {
				var dateObj = new Date(lastWeekDate[0], lastWeekDate[1] - 1, lastWeekDate[2] + i);
				weekDates.push([ dateObj.getFullYear(), dateObj.getMonth() + 1, dateObj.getDate() ]);
			}
			monthWeeks.push(weekDates);
		}

		return monthWeeks;
	}

	/**
	 * Append month day table to calendar body.
	 * <p>
	 * The month day table contains 6 rows 7 columns.
	 */
	function _appendMonthDayTable(targetDIV) {
		var $target = $(targetDIV);
		var opts = $d(targetDIV, "calendar").options;
		var $body = $f("div.calendar-body", $target);

		// set calendar title with the curent year month.
		$f(".calendar-title span", $target).html(opts.titleFormater.call(targetDIV, opts.year, opts.month));
		$f(">table", $body).remove();

		var $monthDayTable = $('<table cellspacing="0" cellpadding="0" border="0"><thead></thead><tbody></tbody></table>').prependTo($body);

		// header
		var tr = $('<tr></tr>').appendTo($f("thead", $monthDayTable));
		for ( var i = opts.firstDay; i < opts.weeks.length; i++) {
			tr.append("<th>" + opts.weeks[i] + "</th>");
		}
		for ( var i = 0; i < opts.firstDay; i++) {
			tr.append("<th>" + opts.weeks[i] + "</th>");
		}

		// append the current month weeks to table
		var monthWeeks = _getMonthWeeks(targetDIV, opts.year, opts.month);
		for ( var i = 0; i < monthWeeks.length; i++) {
			var weekDates = monthWeeks[i];
			var tr = $('<tr></tr>').appendTo($f("tbody", $monthDayTable));
			for ( var j = 0; j < weekDates.length; j++) {
				var day = weekDates[j];
				$('<td class="calendar-day calendar-other-month"></td>').attr("abbr", day[0] + "," + day[1] + "," + day[2]).html(day[2]).appendTo(tr);
			}
		}

		// remove "calendar-other-month" style from the current month
		$rc("calendar-other-month", $f('td[abbr^="' + opts.year + ',' + opts.month + '"]', $monthDayTable));

		// set the today style
		var now = new Date();
		var today = now.getFullYear() + "," + (now.getMonth() + 1) + "," + now.getDate();
		$ac("calendar-today", $f('td[abbr="' + today + '"]', $monthDayTable));

		// selected the current date
		if (opts.current) {
			$rc("calendar-selected", $f(".calendar-selected", $monthDayTable));
			var currentDate = opts.current.getFullYear() + "," + (opts.current.getMonth() + 1) + "," + opts.current.getDate();
			$ac("calendar-selected", $f('td[abbr="' + currentDate + '"]', $monthDayTable));
		}

		// set sturday and sunday style
		var saturday = 6 - opts.firstDay;
		var sunday = saturday + 1;
		if (saturday >= 7) {
			saturday -= 7;
		}
		if (sunday >= 7) {
			sunday -= 7;
		}
		$ac("calendar-saturday", $f("td:eq(" + saturday + ")", $f("tr", $monthDayTable)));
		$ac("calendar-sunday", $f("td:eq(" + sunday + ")", $f("tr", $monthDayTable)));

		// bind hover/click events on month day cell
		$f("td", $monthDayTable).hover(function() {
			$ac("calendar-hover", $(this));
		}, function() {
			$rc("calendar-hover", $(this));
		}).click(function() {
			$rc("calendar-selected", $f(".calendar-selected", $monthDayTable));
			$ac("calendar-selected", $(this));
			var yearMonthDate = $(this).attr("abbr").split(",");
			opts.current = new Date(yearMonthDate[0], parseInt(yearMonthDate[1]) - 1, yearMonthDate[2]);
			opts.onSelect.call(targetDIV, opts.current);
		});

	}

	/**
	 * Create calendar object or invoke calendar method.
	 * <p>
	 * <b>Parameters:</b>
	 * <UL>
	 * <LI>options: type is "string" means calendar method will be invoked. otherwise calendar object will be created.
	 * <LI>args: it is arguments calendar methods needed.
	 * </UL>
	 */
	$.fn.calendar = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.calendar.methods[options]
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.calendar');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "calendar");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "calendar", {
					options : $.extend({}, $.fn.calendar.defaults, $.fn.calendar.parseOptions(this), options)
				});
				_bindCalendarEvents(this);
			}
			if (state.options.border == false) {
				$ac("calendar-noborder", $(this));
			}
			resize(this);
			_appendMonthDayTable(this);
			$f("div.calendar-menu", $(this)).hide();
		});
	};

	$.fn.calendar.methods = {
		/**
		 * Get the calendar options
		 */
		options : function(jq) {
			return $d(jq[0], "calendar").options;
		},
		/**
		 * Resize calendar size
		 */
		resize : $X(resize),
		/**
		 * Set the calendar current date object
		 * <p>
		 * args: date - this date will be set to the calendar.
		 */
		setValue : function(jq, date) {
			return jq.each(function() {
				$(this).calendar({
					year : date.getFullYear(),
					month : date.getMonth() + 1,
					current : date
				});
			});
		},
		/**
		 * Get the calendar current date object
		 */
		getValue : function(jq) {
			return $d(jq[0], "calendar").options.current;
		}
	};

	$.fn.calendar.parseOptions = function(targetDIV) {
		var t = $(targetDIV);
		return $.extend({}, jCocit.parseOptions(targetDIV, [ "width", "height", {
			firstDay : "n",
			fit : "b",
			border : "b"
		} ]));
	};

	$.fn.calendar.defaults = {
		/**
		 * this is calendar UI width
		 */
		width : 180,
		/**
		 * this is calendar UI height
		 */
		height : 180,
		/**
		 * fit is true means that the calendar size will be fit to parent element of the target DIV.
		 */
		fit : false,
		/**
		 * border is true means that the calendar contains border
		 */
		border : true,
		/**
		 * means that which day in this week will be shown the first column of calendar. 0-Saturday, 1-Monday,...,6-Sunday
		 */
		firstDay : 0,
		/**
		 * this is calendar current year
		 */
		year : new Date().getFullYear(),
		/**
		 * this is calendar current month
		 */
		month : new Date().getMonth() + 1,
		/**
		 * Current date
		 */
		current : new Date(),
		weeks : [ "S", "M", "T", "W", "T", "F", "S" ],
		months : [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ],
		/**
		 * Format calendar title
		 */
		titleFormater : function(year, month) {
			var opts = $d(this, "calendar").options;
			return year + " " + opts.months[month - 1];
		},
		/**
		 * this call-back function will be invoked when date be selected.
		 * <p>
		 * args: date - this is selected date
		 */
		onSelect : $n
	};
})(jQuery, jCocit);
