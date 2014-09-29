/**
 * This is dialog UI plugin, extends from window UI plugin.
 * <P>
 * Used to convert the original "DIV" element to dialog UI object.
 * <P>
 * <B>Only dialog(panel and window don't) support to:
 * <UL>
 * <LI>toolbar:
 * <LI>content-panel:
 * <LI>buttons:
 * </UL>
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: resizable, draggable, button(TODO Optional)
 * <LI>ReferencedBy: login
 * <LI>SubClass:
 * <LI>SuperClass: panel, window
 * </UL>
 * 
 * <pre>
 *  [div class='Pn Wd']
 *  	[div class='PnH WdH']
 *  		[div class='PnHR'][div class='PnHC']
 *  			[div class='PnHL'][/div]
 *  			[div class='PnHT'] Here is window title. [/div]
 *  			[div class='PnHB']
 *  				[a class='PnHBB' href='javascript:void(0)'][/a]
 *  				[a class='PnHBL' href='javascript:void(0)'][/a]
 *  				[a class='PnHBN' href='javascript:void(0)'][/a]
 *  				[a class='PnHBX' href='javascript:void(0)'][/a]
 *  				[a class='PnHBC' href='javascript:void(0)'][/a]
 *  			[/div]
 *  		[/div][/div]
 * 		[/div]
 * 		[div class='PnB WdB']
 * 			[div class='DlTB'][table cellspacing='0' cellpadding='0'][tr] Here is dialog tool buttons. [/tr][/table][/div]
 * 			[div class='PnBC'] Here is window content. [/div] 
 * 			[div class='DlBS'] Here is dialog buttons. [/div]
 * 		[/div]
 * 		[div class='PnF WdF'][div class='PnFR'][div class='PnFC'][/div][/div][/div]
 *  [/div]
 *  [div class='WdS'][/div]
 *  [div class='WdM'][/div]
 * </pre>
 * 
 * <p>
 * <B>Notes:</B>
 * <UL>
 * <LI>DlTB: Dialog Tool Bar
 * <LI>DlTBS: Dialog Tool Bar Button Separator.
 * <LI>DlBS: Dialog Buttons
 * <LI>LkB: Dialog Toolbar/Buttons - Link Button
 * </UL>
 */
(function($, jCocit) {

	function _init(selfHTML) {
		var $self = $(selfHTML);
		var state = $d(selfHTML, "dialog");
		var opts = state.options;

		$self.window($.extend({}, opts, {
			onResize : function(width, height) {
				var $dialogBody = $self.panel("body");
				var $dialogContent = $self.panel("content");
				var h = ($dialogBody.oh() - $c("div.DlTB", $dialogBody).oh() - $c("div.DlBS", $dialogBody).oh());
				$dialogContent.oh(height == "auto" ? "auto" : h, true);
				opts.onResize.call(selfHTML, width, height);
			}
		}));

		var $dialogBody = $self.panel("body");
		var $dialogContent = $self.panel("content");

		// prepend toolbar to dialog body
		if (opts.toolbar) {
			if (typeof opts.toolbar == "string") {
				$ac("DlTB", $(opts.toolbar)).prependTo($dialogBody);
				$(opts.toolbar).show();
			} else {
				$f("div.DlTB", $dialogBody).remove();
				var $toolbar = $('<div class="DlTB"><table cellspacing="0" cellpadding="0"><tr></tr></table></div>').prependTo($dialogBody);
				var $tr = $f("tr", $toolbar);
				for ( var i = 0; i < opts.toolbar.length; i++) {
					var btnOption = opts.toolbar[i];
					if (btnOption == "-") {
						$('<td><div class="DlTBS"></div></td>').appendTo($tr);
					} else {
						var $td = $("<td></td>").appendTo($tr);
						_addButton(btnOption, $td);
					}
				}
			}
		} else {
			$f("div.DlTB", $dialogBody).remove();
		}

		// append buttons to dialog body
		if (opts.buttons) {
			if (typeof opts.buttons == "string") {
				$ac("DlBS", $(opts.buttons)).appendTo($dialogBody);
				$(opts.buttons).show();
			} else {
				$f("div.DlBS", $dialogBody).remove();
				var $buttons = $('<div class="DlBS"></div>').appendTo($dialogBody);
				for ( var i = 0; i < opts.buttons.length; i++) {
					var btnOption = opts.buttons[i];
					_addButton(btnOption, $buttons);
				}
			}
		} else {
			$f("div.DlBS", $dialogBody).remove();
		}

		function _addButton(btnOption, $context) {
			var $btn = $('<button class="LkB"></button>').html(btnOption.text).appendTo($context);
			$d($btn[0], "options", btnOption);
			$btn.bind("click", function() {
				var btnOpts = $d(this, "options");
				if (btnOpts.onClick)
					// invoke onClick function on selfHTML.
					btnOpts.onClick.call(selfHTML, {
						options : btnOpts,
						target : this
					});
			});
			// if ($.fn.button)
			// $btn.button(btnOption);
		}
	}

	jCocit.dialog = $.extend({}, jCocit.window, {
		open : function(url, dialogId, options) {
			return jCocit.window.open(url, dialogId, options, "dialog");
		}
	});

	/**
	 * Dialog Link: Click it, a Dialog will be open.
	 */
	$.fn.dialoglink = function(options) {
		return this.each(function() {
			$(this).click(function() {
				var opts = $.extend({}, $.fn.dialog.defaults, $.fn.dialog.parseOptions(this), options || {});
				opts.id = "__jCocit_dialog_" + opts.id;
				jCocit.dialog.open(opts.url, opts.id, opts);
				return false;
			});
		})
	}

	/**
	 * 1. Create dialog UI object or set dialog properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke dialog method or window method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.dialog = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.dialog.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.window(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "dialog");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, $.fn.dialog.defaults, $.fn.dialog.parseOptions(this), options);

				$d(this, "dialog", {
					options : opts
				});
			}

			if (!opts.title || opts.title.trim().length == 0)
				opts.title = $.fn.dialog.defaults.title;

			_init(this);

			var $this = $(this);
			if (opts.doSize == true) {
				$this.window("window").css("display", "block");
				$this.window("resize");
			}
			// if (opts.closed == true || opts.minimized == true) {
			// $this.window("window").hide();
			// } else {
			// $this.window("open");
			// }
		});
	};

	/**
	 * Dialog Methods
	 */
	$.fn.dialog.methods = {
		options : function(jq) {
			var opts = $d(jq[0], "dialog").options;
			var panelOpts = jq.panel("options");

			$.extend(opts, {
				closed : panelOpts.closed,
				collapsed : panelOpts.collapsed,
				minimized : panelOpts.minimized,
				maximized : panelOpts.maximized
			});

			// var $dialogContent = $d(jq[0], "dialog").contentPanel;

			return opts;
		},
		dialog : function(jq) {
			return jq.window("window");
		}
	};

	$.fn.dialog.parseOptions = function(selfHTML) {
		return $.extend({}, $.fn.window.parseOptions(selfHTML), jCocit.parseOptions(selfHTML, [ "toolbar", "buttons" ]));
	};

	$.fn.dialog.defaults = $.extend({}, $.fn.window.defaults, {
		title : jCocit.defaults.title,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		resizable : false,
		toolbar : null,
		buttons : null
	});
})(jQuery, jCocit);
