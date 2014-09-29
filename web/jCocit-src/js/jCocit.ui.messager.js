/**
 * 
 */
(function($, jCocit) {

	$.messager = {};
	var options = $.messager.defaults = {
		ok : "OK",
		cancel : "Cancel",
		yes : "Yes",
		no : "No",
		title : jCocit.defaults.title
	}
	function _initAlertsDialog(title, contentHTML, buttons) {
		var $alertsContent = $("<div class=\"messager-content\"></div>").appendTo("body");
		contentHTML = '<span style="margin:0;padding:0;border:0;">' + contentHTML + '</span>';
		$alertsContent.html(contentHTML.replace(/\n/g, '<br />'));
		$alertsContent.dialog({
			title : title,
			width : 800,
			height : 500,
			shadow : false,
			modal : true,
			styleName : "window-simple",
			collapsible : false,
			minimizable : false,
			maximizable : false,
			resizable : false,
			draggable : true,
			buttons : buttons,
			onClose : function() {
				setTimeout(function() {
					$alertsContent.dialog("destroy");
				}, 100);
			}
		});

		$ac("messager-window", $alertsContent.dialog("window"));
		$c("a:first", $c("div.messager-buttons", $alertsContent)).focus();

		return $alertsContent;
	}

	function _alerts(msg, title, type, callback) {
		if ($.isFunction(title)) {
			callback = title;
			title = options.title;
		}

		var contentHTML = '<div class="messager-icon messager-{0}"></div><div>{1}</div><div style="clear:both;"/>'.format(type, msg);
		var buttons = [ {
			text : options.ok,
			onClick : function(btn) {
				$(this).dialog('destroy');
				if (callback) {
					callback();
					return false;
				}
			}
		} ];
		return _initAlertsDialog(title, contentHTML, buttons);
	}

	function _confirm(msg, title, callback) {
		if ($.isFunction(title)) {
			callback = title;
			title = options.title;
		}

		var contentHTML = '<div class="messager-icon messager-confirm"></div><div>{0}</div><div style="clear:both;"/>'.format(msg);
		var buttons = [ {
			text : options.yes,
			onClick : function(btn) {
				$(this).dialog('destroy');
				if (callback) {
					callback(true);
					return false;
				}
			}
		}, {
			text : options.no,
			onClick : function(btn) {
				$(this).dialog('destroy');
				if (callback) {
					callback(false);
					return false;
				}
			}
		} ];
		return _initAlertsDialog(title, contentHTML, buttons);
	}

	function _prompt(msg, title, value, callback) {
		if ($.isFunction(title)) {
			callback = title;
			title = options.title;
		}
		var contentHTML = '<div class="messager-icon messager-confirm"></div><div>{0}</div><br/><div style="clear:both;"/><div><input class="messager-input" type="text" value="{1}"/></div>'
				.format(msg, value || "");
		var buttons = [ {
			text : options.ok,
			onClick : function(btn) {
				var $dialog = $(this).dialog('destroy');
				if (callback) {
					callback($(".messager-input", $dialog).val());
					return false;
				}
			}
		}, {
			text : options.cancel,
			onClick : function(btn) {
				$(this).dialog('destroy');
				if (callback) {
					callback(null);
					return false;
				}
			}
		} ];
		var $alertsContent = _initAlertsDialog(title, contentHTML, buttons);
		$c("input.messager-input", $alertsContent).focus();
		return $alertsContent;
	}

	Jerror = function(message, title, callback) {
		_alerts(message, title, "error", callback);
	};
	Jwarn = function(message, title, callback) {
		_alerts(message, title, "warn", callback);
	};
	Jinfo = function(message, title, callback) {
		_alerts(message, title, "info", callback);
	};
	Jsuccess = function(message, title, callback) {
		_alerts(message, title, "success", callback);
	};
	Jconfirm = _confirm;
	Jprompt = _prompt;
	//
	// /**
	// * call by _openDialog function
	// */
	// function _doBeforeOpen(el, showType, showSpeed, timeout) {
	// var $window = $(el).dialog("window");
	// if (!$window) {
	// return;
	// }
	// switch (showType) {
	// case null:
	// $window.show();
	// break;
	// case "slide":
	// $window.slideDown(showSpeed);
	// break;
	// case "fade":
	// $window.fadeIn(showSpeed);
	// break;
	// case "show":
	// $window.show(showSpeed);
	// break;
	// }
	// var _setTimeout = null;
	// if (timeout > 0) {
	// _setTimeout = setTimeout(function() {
	// _doBeforeClose(el, showType, showSpeed);
	// }, timeout);
	// }
	// $window.hover(function() {
	// if (_setTimeout) {
	// clearTimeout(_setTimeout);
	// }
	// }, function() {
	// if (timeout > 0) {
	// _setTimeout = setTimeout(function() {
	// _doBeforeClose(el, showType, showSpeed);
	// }, timeout);
	// }
	// });
	// }
	//
	// /**
	// * call by _openDialog function
	// */
	// function _doBeforeClose(el, showType, showSpeed) {
	// if (el.locked == true) {
	// return;
	// }
	// el.locked = true;
	// var $window = $(el).dialog("window");
	// if (!$window) {
	// return;
	// }
	// switch (showType) {
	// case null:
	// $window.hide();
	// break;
	// case "slide":
	// $window.slideUp(showSpeed);
	// break;
	// case "fade":
	// $window.fadeOut(showSpeed);
	// break;
	// case "show":
	// $window.hide(showSpeed);
	// break;
	// }
	// setTimeout(function() {
	// $(el).dialog("destroy");
	// }, showSpeed);
	// }
	//
	// /**
	// * call by show function
	// */
	// function _openDialog(options) {
	// var opts = $.extend({}, $.fn.window.defaults, {
	// collapsible : false,
	// minimizable : false,
	// maximizable : false,
	// shadow : false,
	// draggable : false,
	// resizable : false,
	// closed : true,
	// style : {
	// left : "",
	// top : "",
	// right : 0,
	// zIndex : $.fn.window.defaults.zIndex++,
	// bottom : -document.body.scrollTop - document.documentElement.scrollTop
	// },
	// onBeforeOpen : function() {
	// _doBeforeOpen(this, opts.showType, opts.showSpeed, opts.timeout);
	// return false;
	// },
	// onBeforeClose : function() {
	// _doBeforeClose(this, opts.showType, opts.showSpeed);
	// return false;
	// }
	// }, {
	// title : "",
	// width : 250,
	// height : 100,
	// showType : "slide",
	// showSpeed : 600,
	// msg : "",
	// timeout : 4000
	// }, options);
	//
	// opts.style.zIndex = $.fn.window.defaults.zIndex++;
	// var $alertsContent = $("<div class=\"messager-content\"></div>").html(opts.msg).appendTo("body");
	// $alertsContent.dialog(opts);
	// $alertsContent.dialog("window").css(opts.style);
	// $alertsContent.dialog("open");
	// return $alertsContent;
	// }
	//
	// function show(options) {
	// return _openDialog(options);
	// }
	// function progress(options) {
	// var opts = {
	// bar : function() {
	// return $f("div.messager-p-bar", $("body>div.messager-window"));
	// },
	// close : function() {
	// var $alertsContent = $("body>div.messager-window>div.messager-content:has(div.messager-progress)");
	// if ($alertsContent.length) {
	// $alertsContent.dialog("close");
	// }
	// }
	// };
	// if (typeof options == "string") {
	// var fn = opts[options];
	// return fn();
	// }
	// var opts = $.extend({
	// title : "",
	// msg : "",
	// text : undefined,
	// interval : 300
	// }, options || {});
	// var contentHTML = "<div class=\"messager-progress\"><div class=\"messager-p-msg\"></div><div class=\"messager-p-bar\"></div></div>";
	// var $alertsContent = _initAlertsDialog(opts.title, contentHTML, null);
	// $f("div.messager-p-msg", $alertsContent).html(opts.msg);
	// var bar = $f("div.messager-p-bar", $alertsContent);
	// bar.progressbar({
	// text : opts.text
	// });
	// $alertsContent.dialog({
	// closable : false,
	// onClose : function() {
	// if (this.timer) {
	// clearInterval(this.timer);
	// }
	// $(this).dialog("destroy");
	// }
	// });
	// if (opts.interval) {
	// $alertsContent[0].timer = setInterval(function() {
	// var v = bar.progressbar("getValue");
	// v += 10;
	// if (v > 100) {
	// v = 0;
	// }
	// bar.progressbar("setValue", v);
	// }, opts.interval);
	// }
	// return $alertsContent;
	// }

})(jQuery, jCocit);
