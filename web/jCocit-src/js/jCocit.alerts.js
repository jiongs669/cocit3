/**
 * Alerts Box
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: draggable(Optional)
 * <LI>ReferencedBy: unknown
 * <LI>SubClass: none
 * <LI>SuperClass: none
 * </UL>
 * 
 * <UL>
 * <LI>AlC: alerts container
 * <LI>AlB: alerts box
 * <LI>AlHL: alerts header left
 * <LI>AlHT: alerts header title
 * <LI>AlHR: alerts header right
 * <LI>AlHC: alerts header close
 * <LI>AlBM: alerts body message
 * <LI>AlBP: alerts body panel
 * <LI>AlBtP: alerts button panel
 * <LI>AlP: alerts prompt
 * <LI>AlBL: alerts body left
 * <LI>AlBtL: alerts button left
 * <LI>AlBR: alerts body right
 * <LI>AlBtR: alerts button right
 * <LI>AlFL: alerts footer left
 * <LI>AlFC: alerts footer center
 * <LI>AlFR: alerts footer right
 * <LI>AlBt: alerts button
 * <LI>AlBI: alerts body icon
 * </UL>
 */
(function($, jCocit) {

	$.alerts = {}
	var options = $.alerts.defaults = {
		vOffset : -75,
		hOffset : 0,
		/**
		 * true means that alerts box will be moved to specified position when browser window size be changed.
		 */
		moveToOnResize : true,
		maskOpacity : .01,
		maskColor : '#FFF',
		// draggable : true,
		dialogClass : null,
		minWidth : 360,
		ok : "OK",
		cancel : "Cancel",
		yes : "Yes",
		no : "No",
		warn : "Warn",
		error : "Error",
		info : "Info",
		success : "Success",
		confirm : "Confim",
		prompt : "Prompt",
		title : jCocit.defaults.title
	}

	var template = '<div id="AlC">' + //
	'<table id="AlB" border="0" cellpadding="0" cellspacing="0">' + //
	'<tr>' + //
	'<td id="AlHL"></td>' + //
	'<td id="AlHT"></td>' + //
	'<td id="AlHC"><div></div></td>' + //
	'<td id="AlHR"></td>' + //
	'</tr>' + //
	'<tr>' + //
	'<td id="AlBL"></td>' + //
	'<td id="AlBP" colspan="2">' + //
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">' + //
	'<tr>' + //
	'<td id="AlBI" width="32px"></td>' + //
	'<td width="12px"></td>' + //
	'<td><div id="AlBM"></div></td>' + //
	'</tr>' + //
	'</table>' + //
	'</td>' + //
	'<td id="AlBR"></td>' + //
	'</tr>' + //
	'<tr>' + //
	'<td id="AlBtL"></td>' + //
	'<td id="AlBtP" colspan="2"></td>' + //
	'<td id="AlBtR"></td>' + //
	'</tr>' + //
	'<tr>' + //
	'<td id="AlFL"></td>' + //
	'<td id="AlFC" colspan="2"></td>' + //
	'<td id="AlFR"></td>' + //
	'</tr>' + //
	'</table>' + //
	'</div>';

	/**
	 * Show Message
	 * <p>
	 * <b>Parameters:</b>
	 * <UL>
	 * <LI>title:
	 * <LI>message:
	 * <LI>value:
	 * <LI>type:
	 * <LI>callback:
	 * </UL>
	 */
	function show(msg, title, value, type, callback) {
		if (!title)
			title = options.title;
		if ($.isFunction(title)) {
			callback = title;
			title = options.title;
		}
		msg = '<span style="margin:0;padding:0;border:0;">' + msg + '</span>';

		hide();
		mask('show');

		// create alerts box container
		var $container = $(template).appendTo($("BODY"));

		// set CSS
		if (options.dialogClass)
			$ac(options.dialogClass, $container);

		// IE6 Fix
		var pos = ($.browser.msie && parseInt($.browser.version) <= 6) ? 'absolute' : 'fixed';

		//
		$container.css({
			position : pos,
			zIndex : 99999,
			padding : 0,
			margin : 0,
			left : 99999,
			top : 99999
		});

		var $headerTitle = $("#AlHT", $container).text(title);
		var $bodyMessage = $("#AlBM", $container).html(msg.replace(/\n/g, '<br />'));
		var $buttonPanel = $("#AlBtP", $container);
		$ac(type, $("#AlBI"));

		$("#AlHC", $container).click(function() {
			hide();
			if (callback) {
				switch (type) {
				case 'info':
				case 'alert':
				case 'error':
				case 'success':
					callback(true);
					break;
				case 'confirm':
					callback(false);
					break;
				case 'prompt':
					callback(null);
					break;
				}
			}
		});

		switch (type) {
		case 'confirm':
			$buttonPanel.append('<input id="AlBt_ok" type="button" value="{0}" class="AlBt" /><input id="AlBt_cancel" type="button" value="{1}" class="AlBt" />'.format(options.yes, options.no));
			var $ok = $("#AlBt_ok", $container);
			var $cancel = $("#AlBt_cancel", $container);
			$ok.click(function() {
				hide();
				if (callback)
					callback(true);
			}).focus().keypress(function(e) {
				if (e.keyCode == jCocit.keyCode.ENTER)
					$ok.trigger('click');
				if (e.keyCode == jCocit.keyCode.ESC)
					$cancel.trigger('click');
			});
			$cancel.click(function() {
				hide();
				if (callback)
					callback(false);
			}).keypress(function(e) {
				if (e.keyCode == jCocit.keyCode.ENTER)
					$ok.trigger('click');
				if (e.keyCode == jCocit.keyCode.ESC)
					$cancel.trigger('click');
			});

			break;
		case 'prompt':
			$c("span", $bodyMessage).append('<br/><input type="text" id="AlP" />')
			$buttonPanel.append('<input id="AlBt_ok" type="button" value="{0}" class="AlBt" /><input id="AlBt_cancel" type="button" value="{1}" class="AlBt" />'.format(options.ok, options.cancel));
			var $prompt = $("#AlP", $container);
			var $ok = $("#AlBt_ok", $container);
			var $cancel = $("#AlBt_cancel", $container);
			$ok.click(function() {
				var val = $prompt.val();
				hide();
				if (callback)
					callback(val);
			}).keypress(function(e) {
				if (e.keyCode == jCocit.keyCode.ENTER)
					$ok.trigger('click');
				if (e.keyCode == jCocit.keyCode.ESC)
					$cancel.trigger('click');
			});
			$cancel.click(function() {
				hide();
				if (callback)
					callback(null);
			}).keypress(function(e) {
				if (e.keyCode == jCocit.keyCode.ENTER)
					$ok.trigger('click');
				if (e.keyCode == jCocit.keyCode.ESC)
					$cancel.trigger('click');
			});
			if (value)
				$prompt.val(value);
			$prompt.focus().select();

			break;
		default:
			$buttonPanel.append('<input id="alerts_ok" type="button" value="{0}" class="AlBt" />'.format(options.ok));
			var $ok = $("#alerts_ok", $container);
			$ok.click(function() {
				hide();
				if (callback)
					callback(true);
			}).focus().keypress(function(e) {
				if (e.keyCode == jCocit.keyCode.ENTER || e.keyCode == jCocit.keyCode.ESC)
					$ok.trigger('click');
			});

			break;
		}

		moveTo();
		bindResizeEvent(true);

		// Make draggable
		if ($.fn.draggable) {
			try {
				$container.draggable({
					handle : $headerTitle
				});
				$headerTitle.css({
					cursor : 'move'
				});
			} catch (e) {
				_alert(e);
			}
		}
	}

	/*
	 * Hidden message box
	 */
	function hide() {
		$("#AlC").remove();
		mask('hide');
		bindResizeEvent(false);
	}

	/*
	 * mask window content when message box pop-up
	 */
	function mask(status) {
		switch (status) {
		case 'show':
			mask('hide');
			$('<div id="alerts_mask"></div>').css({
				position : 'absolute',
				zIndex : 99998,
				top : '0px',
				left : '0px',
				width : '100%',
				height : $h($(document)),
				background : options.maskColor,
				opacity : options.maskOpacity
			}).appendTo($("BODY"));

			break;
		case 'hide':
			$("#alerts_mask").remove();

			break;
		}
	}

	/*
	 * Evaluate position of message box
	 */
	function moveTo() {
		var $container = $("#AlC");
		var $window = $(window);

		var $msgDiv = $f("#AlBM", $container);
		var $msgTd = $p($msgDiv);
		var winWidth = $w($window);
		var winHeight = $h($window);
		var maxWidth = winWidth / 2;
		var maxHeight = winHeight / 2;

		var originCss = {
			position : $msgDiv.css("position"),
			overflow : $msgDiv.css("overflow"),
			left : $msgDiv.css("left"),
			top : $msgDiv.css("top")
		}

		$msgDiv.css({
			position : "absolute",
			overflow : "auto",
			height : maxHeight,
			width : maxWidth,
			left : -1 * winWidth,
			top : -1 * winHeight
		}).appendTo("body");
		var $msgSpan = $c("span", $msgDiv);

		var marginWidth = $msgSpan._css("margin-left") + $msgSpan._css("margin-right");
		var marginHeight = $msgSpan._css("margin-top") + $msgSpan._css("margin-bottom");

		// 61 = 12(left width) + 32(icon width) + 12(msg left space) + 5(right width)
		var boxWidth = Math.max(options.minWidth, $msgSpan.ow() + marginWidth + 61);
		var msgWidth = Math.min(maxWidth, boxWidth) - 61;
		$w(msgWidth, $msgDiv);
		$w(msgWidth - 22, $f("#AlP", $msgDiv));

		var outerHeight = $msgSpan.oh();
		// scroll-x bar
		if ($msgDiv[0].scrollWidth > $msgDiv[0].offsetWidth) {
			outerHeight += 22;
		}
		// 73 = 27(top height) + 12(body-panel padding-top) + 12(body-panel padding-bottom) + 20(button-panel height) + 12(bottom height)
		var boxHeight = Math.min(outerHeight + marginHeight + 73, maxHeight);
		var msgHeight = boxHeight - 70;
		$h(msgHeight, $msgDiv);

		$msgDiv.css(originCss).appendTo($msgTd);

		// scroll-y bar
		if ($msgDiv[0].scrollHeight > $msgDiv[0].offsetHeight) {
			$f("#AlBP", $container).css({
				"padding-right" : "0px"
			});
			$w(msgWidth + 22, $msgDiv);
		}

		// set box position
		var top = ((winHeight / 2) - ($oh($container) / 2)) + options.vOffset;
		var left = ((winWidth / 2) - ($ow($container) / 2)) + options.hOffset;
		if (top < 0)
			top = 0;
		if (left < 0)
			left = 0;

		// IE6 fix
		if ($.browser.msie && parseInt($.browser.version) <= 6)
			top = top + $window.scrollTop();

		$container.css({
			top : top + 'px',
			left : left + 'px'
		});

		$h($h($(document)), $("#alerts_mask"));
	}

	/*
	 * Bind browser window "resize" event
	 */
	function bindResizeEvent(status) {
		if (options.moveToOnResize) {
			switch (status) {
			case true:
				$(window).bind('resize', moveTo);
				break;
			case false:
				$(window).unbind('resize', moveTo);
				break;
			}
		}
	}

	/*
	 * Common Functions
	 */
	Jerror = function(message, title, callback) {
		if (!title || title.trim() == 0)
			title = $.alerts.defaults.error;
		show(message, title, null, "error", callback);
	};
	Jwarn = function(message, title, callback) {
		if (!title || title.trim() == 0)
			title = $.alerts.defaults.warn;
		show(message, title, null, "warn", callback);
	};
	Jinfo = function(message, title, callback) {
		if (!title || title.trim() == 0)
			title = $.alerts.defaults.info;
		show(message, title, null, "info", callback);
	};
	Jalert = Jinfo;
	Jsuccess = function(message, title, callback) {
		if (!title || title.trim() == 0)
			title = $.alerts.defaults.success;
		show(message, title, null, "success", callback);
	};
	Jconfirm = function(message, title, callback) {
		if (!title || title.trim() == 0)
			title = $.alerts.defaults.confirm;
		show(message, title, null, 'confirm', callback);
	};
	Jprompt = function(message, value, title, callback) {
		if (!title || title.trim() == 0)
			title = $.alerts.defaults.prompt;
		show(message, title, value, 'prompt', callback);
	};

})(jQuery, jCocit);
