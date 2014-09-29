/**
 * This is jCocit Dialog Plugin.
 */
(function($, jCocit) {

	// this is dialog HTML template
	var dialogTemplate = '\
		<div class="dialog" style="top:150px; left:300px;">\
			<div class="dialogHeader" onselectstart="return false;" oncopy="return false;" onpaste="return false;" oncut="return false;">\
				<div class="dialogHeader_r">\
					<div class="dialogHeader_c">\
						<a class="close" href="#close"></a>\
						<a class="maximize" href="#maximize"></a>\
						<a class="restore" href="#restore"></a>\
						<a class="minimize" style="display:none;" href="#minimize"></a>\
						<div class="headerTitle"></div>\
						<div class="headerSubTitle"></div>\
					</div>\
				</div>\
			</div>\
			<div class="dialogBody"><div class="dialogContent"></div></div>\
			<div class="dialogFooter"><div class="dialogFooter_r"><div class="dialogFooter_c"></div></div></div>\
		</div>';

	// this is dialog shadow HTML template
	var shadowTemplate = '\
		<div id="dialogShadow" class="shadow" style="width:508px; top:148px; left:296px;">\
			<div class="shadow_h">\
				<div class="shadow_h_l"></div>\
				<div class="shadow_h_r"></div>\
				<div class="shadow_h_c"></div>\
			</div>\
			<div class="shadow_c">\
				<div class="shadow_c_l" style="height:296px;"></div>\
				<div class="shadow_c_r" style="height:296px;"></div>\
				<div class="shadow_c_c" style="height:296px;"></div>\
			</div>\
			<div class="shadow_f">\
				<div class="shadow_f_l"></div>\
				<div class="shadow_f_r"></div>\
				<div class="shadow_f_c"></div>\
			</div>\
		</div>';

	// this is modal dialog background
	var backgroundTemplate = '<div id="dialogBackground" class="dialogBackground"></div>';

	function getExternalWidth($target) {
		return $target.cssValue("padding-left") + $target.cssValue("padding-right") + $target.cssValue("border-left-width") + $target.cssValue("border-right-width");
	}

	function getExternalHeight($target) {
		return $target.cssValue("padding-top") + $target.cssValue("padding-bottom") + $target.cssValue("border-top-width") + $target.cssValue("border-bottom-width");
	}

	/**
	 * define jCocit dialog class
	 */
	jCocit.dialog = {
		// this is active dialog on the current page.
		currentDialog : null,
		zIndex : 42,

		/**
		 * Open existed dialog specified by parameter "dialogId" or create a new dialog.
		 * <P>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>url: load dialog content from this URL. The dialog content will be reload if url no equals "url" property of dialog data.
		 * <LI>dialogId: this is dialog key, used to cache dialog into data of "body" element. A new dialog will be created if dialog is not existed in cached data of "body" element.
		 * <LI>opts: this parameter contains the following properties
		 * <UL>
		 * <LI>doSuccess: this is call-back function, it will be invoked when HTML content is reloaded successfully.
		 * <LI>doConfirm: this is call-back function, it will be invoked when "Confirm" button is clicked.
		 * <LI>doCancel: this is call-back function, it will be invoked when "Cancel" button is clicked.
		 * <LI>doReset: this is call-back function, it will be invoked when "Reset" button is clicked.
		 * <LI>doHelp: this is call-back function, it will be invoked when "Help" button is clicked.
		 * <LI>doClose: this is call-back function, it will be invoked when "Close" icon is clicked.
		 * <LI>refresh: true means that the dialog content will be reload.
		 * <LI>param: this is dialog parameter used for close call-back function parameter.
		 * <LI>headerTitle: this is dialog title.
		 * <LI>headerSubTitle: this is the dialog subtitle.
		 * <LI>draggable: true means that the dialog can be draggable.
		 * <LI>closable: true means that the dialog can be closable.
		 * <LI>maxable: true means that the dialog can be maxable.
		 * <LI>max: true means that the dialog is max or full screen.
		 * <LI>modal: true means that the dialog is modal dialog.
		 * <LI>height: this is dialog height.
		 * <LI>width: this is dialog width.
		 * </UL>
		 * </UL>
		 * <p>
		 * <b>Server Return Value:</b>
		 * <UL>
		 * <LI>"Confirm" button: this is a "Confirm" button specified by class name "Confirm", the "doConfirm" call-back function will be invoked and the dialog will be closed if "doConfirm" return true when the button is clicked.
		 * <LI>"Cancel" button: this is a "Cancel" button specified by class name "Cancel", the "doCancel" call-back function will be invoked and the dialog will be closed if "doCancel" return true when the button is clicked.
		 * <LI>"Reset" button: this is a "Reset" button specified by class name "Reset". the "doReset" call-back function will be invoked and the dialog will be closed if "doReset" return true when the button is clicked.
		 * <LI>"Help" button: this is a "Help" button specified by class name "Help". the "doHelp" call-back function will be invoked and the dialog will be closed if "doHelp" return true when the button is clicked.
		 * </UL>
		 * <p>
		 * <b>Return:</b> no return.
		 */
		open : function(url, dialogId, opts) {
			var self = this;
			var options = $.extend({}, defaults, opts);
			var $dialog = $("body").data(dialogId);

			// dialog already existed.
			if ($dialog) {

				// show it if dialog is hidden.
				if ($dialog.is(":hidden")) {
					$dialog.show();
				}

				// refresh dialog content
				if (options.refresh || url != $dialog.data("url")) {
					if ($.type(url) == "undefined" || url == null || url.trim().length() == 0) {
						url = $dialog.data("url");
					}
					$dialog.data("url", url);

					// set dialog header
					$dialog.find(".dialogHeader").find(".headerTitle").html(options.headerTitle || "");
					$dialog.find(".dialogHeader").find(".headerSubTitle").html(options.headerSubTitle || "");

					// switch dialog to active current dialog
					self.switchDialog($dialog);

					// reload dialog content
					var $content = $dialog.find(".dialogContent");
					if (url) {
						$content.doGet(url, {}, function() {
							self._bindButtonCallback($dialog);
							if ($.isFunction(options.doSuccess))
								options.doSuccess(response);
						});
					}
				}

			} else {

				// create a new dialog
				$("body").append(dialogTemplate);
				$dialog = $(">.dialog:last-child", "body");

				// set customized CSS
				if (options.styleName) {
					$dialog.addClass(options.styleName);
				}

				// set dialog data "id" and "url"
				$dialog.data("id", dialogId);
				$dialog.data("url", url);

				// cache call-back function
				if (options.doConfirm)
					$dialog.data("doConfirm", options.doConfirm);
				if (options.doCancel)
					$dialog.data("doCancel", options.doCancel);
				if (options.doClose)
					$dialog.data("doClose", options.doClose);
				if (options.doReset)
					$dialog.data("doReset", options.doReset);
				if (options.doHelp)
					$dialog.data("doHelp", options.doHelp);
				if (options.param)
					$dialog.data("param", options.param);

				//
				($.fn.bgiframe && $dialog.bgiframe());

				// set dialog header
				var dialogHeader = $dialog.find(".dialogHeader").find(".headerTitle").html(options.headerTitle || "");
				$dialog.find(".dialogHeader").find(".headerSubTitle").html(options.headerSubTitle || "");
				$dialog.css("zIndex", (self.zIndex += 2));
				var $shadow = self.getShadow()
				if ($shadow != null)
					$shadow.css("zIndex", self.zIndex - 3).show();

				self._init($dialog, options);

				// set to the active dialog when clicking it
				$dialog.click(function() {
					self.switchDialog($dialog);
				});

				// dialog is support draggable
				if (options.draggable)
					$dialog.dialogDrag();

				// dialog is support closable
				if (options.closable) {
					$("a.close", $dialog).click(function(event) {
						self.close($dialog);
						return false;
					}).attr("title", jCocit.nls.close);
				} else {
					$("a.close", $dialog).hide();
				}

				// dialog is support maxable
				if (options.maxable) {
					$("a.maximize", $dialog).show().click(function(event) {
						self.switchDialog($dialog);
						self.maxsize($dialog);
						$dialog.dialogDrag("destroy");
						return false;
					}).attr("title", jCocit.nls.maximize);
				} else {
					$("a.maximize", $dialog).hide();
				}

				// restore dialog when click restore icon of right-top
				$("a.restore", $dialog).click(function(event) {
					self.restore($dialog);
					$dialog.dialogDrag();
					return false;
				}).attr("title", jCocit.nls.restore);

				// restore or maximize dialog when double click dialog header
				$("a", dialogHeader).mousedown(function() {
					return false;
				}).dblclick(function() {
					if ($("a.restore", $dialog).is(":hidden"))
						$("a.maximize", $dialog).trigger("click");
					else
						$("a.restore", $dialog).trigger("click");
				});

				// the dialog is full screen
				if (options.max) {
					self.switchDialog($dialog);
					self.maxsize($dialog);
					$dialog.dialogDrag("destroy");
				}

				// cache dialog into data of "body" element and set to current dialog
				$("body").data(dialogId, $dialog);
				self.currentDialog = $dialog;

				// attach shadow on behind of the dialog
				self.attachShadow($dialog);

				// load dialog content
				var $content = $(".dialogContent", $dialog);
				if (url) {
					$content.doGet(url, {}, function() {
						self._bindButtonCallback($dialog);
						if ($.isFunction(options.doSuccess))
							options.doSuccess(response);
					});
				}
			}

			// modal dialog means that the page focus is the current dialog.
			if (options.modal) {
				$dialog.css("zIndex", 1000);
				$dialog.data("modal", true);
				self.getBackground().show();
			}
		},

		/**
		 * Reload HTML content of dialog specified by "dialogId" property of the parameter "opts" from URL specified by the parameter "url".
		 * <p>
		 * Reload HTML content of the current dialog if "dialogId" property is undefined in the parameter "opts".
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>url: this is URL
		 * <LI>dialogId: this is dialog key, used to get dialog from cached data of "body" element. if dialogId is undefined or cached dialog is not existed, the current dialog content will be reload.
		 * <LI>opts: this parameter contains the following properties
		 * <UL>
		 * <LI>data: this is request data, will be send to server side.
		 * <LI>doSuccess: this is call-back function, it will be invoked when HTML content is reloaded successfully.
		 * <LI>
		 * </UL>
		 * </UL>
		 * <p>
		 * <b>Server Return Value:</b>
		 * <UL>
		 * <LI>"Confirm" button: this is a "Confirm" button specified by class name "Confirm", the "doConfirm" call-back function will be invoked and the dialog will be closed if "doConfirm" return true when the button is clicked.
		 * <LI>"Cancel" button: this is a "Cancel" button specified by class name "Cancel", the "doCancel" call-back function will be invoked and the dialog will be closed if "doCancel" return true when the button is clicked.
		 * <LI>"Reset" button: this is a "Reset" button specified by class name "Reset". the "doReset" call-back function will be invoked and the dialog will be closed if "doReset" return true when the button is clicked.
		 * <LI>"Help" button: this is a "Help" button specified by class name "Help". the "doHelp" call-back function will be invoked and the dialog will be closed if "doHelp" return true when the button is clicked.
		 * </UL>
		 * <p>
		 * <b>Return:</b> no return.
		 */
		reload : function(url, dialogId, opts) {
			var self = this;
			var options = $.extend({
				data : {}
			}, opts);

			// get dialog
			var $dialog = (dialogId && $("body").data(dialogId)) || self.currentDialog;
			if ($dialog) {
				var $content = $dialog.find(".dialogContent");
				$content.doLoad({
					type : "POST",
					url : url,
					data : options.data,
					doSuccess : function(response) {
						self._bindButtonCallback($dialog);
						if ($.isFunction(options.doSuccess))
							options.doSuccess(response);
					}
				});
			}
		},

		/**
		 * Set HTML content specified by the parameter "content" into dialog specified by "dialogId".
		 * <P>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>content: This is HTML text, used to HTML content of dialog content element. The HTML elements specified by class name "jCocit-ui" nested in the content will be auto parsed to jQuery object.
		 * <LI>dialogId: This is dialog key, used to get dialog from cached data of "body" element. if dialogId is undefined or cached dialog is not existed, the current dialog will be used.
		 * </UL>
		 * <p>
		 * <b>Return:</b> no return.
		 */
		setContent : function(content, dialogId) {
			var self = this;
			var $dialog = (dialogId && $("body").data(dialogId)) || self.currentDialog;
			var $content = $dialog.find(".dialogContent");
			$content.html(content);
			jCocit.parse($content);
			self._bindButtonCallback($dialog);
		},

		/**
		 * Switch dialog and set to active current dialog.
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>$dialog: this is dialog jQuery object.
		 * </UL>
		 */
		switchDialog : function($dialog) {
			var self = this;
			var index = $dialog.css("zIndex");

			// attach shadow on behind of dialog
			self.attachShadow($dialog);

			// switch zIndex of the current dialog and dialog
			if (self.currentDialog) {
				var cindex = $(self.currentDialog).css("zIndex");
				self.currentDialog.css("zIndex", index);
				$dialog.css("zIndex", cindex);
				var $shadow = self.getShadow()
				if ($shadow != null)
					$shadow.css("zIndex", cindex - 1);
			}

			// set dialog to active current dialog
			self.currentDialog = $dialog;
		},

		/**
		 * Get the active dialog on the current page.
		 * <p>
		 * <b>Return:</b> the active dialog on the current page.
		 */
		getCurrent : function() {
			return this.currentDialog;
		},

		/**
		 * Get dialog content jQuery object.
		 * <P>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>dialogId: This is dialog key, used to get dialog from cached data of "body" element. if dialogId is undefined or cached dialog is not existed, the current dialog will be used.
		 * </UL>
		 * <p>
		 * <b>Return:</b> jQuery object used to describe dialog content.
		 */
		getContent : function(dialogId) {
			var $dialog = (dialogId && $("body").data(dialogId)) || this.currentDialog;
			return $dialog.find(".dialogContent");
		},

		/**
		 * Get dialog shadow jQuery object, shadow used for active current dialog.
		 * <P>
		 * <B>Return:</B> dialog shadow jQuery object
		 */
		getShadow : function() {
			return null;// no support shadow;

			var $shadow = $("#dialogShadow");
			if ($shadow.length == 0) {
				$shadow = $(shadowTemplate).appendTo($("body"));
			}
			return $shadow;
		},

		/**
		 * Get dialog background jQuery object, background used for modal dialog.
		 * <P>
		 * <B>Return:</B> dialog background jQuery object
		 */
		getBackground : function() {
			var background = $("#dialogBackground");
			if (background.length == 0) {
				background = $(backgroundTemplate).appendTo($("body"));
			}
			return background;
		},

		/**
		 * Attach shadow on behind of dialog.
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>$dialog: this is dialog jQuery object.
		 * </UL>
		 */
		attachShadow : function($dialog) {
			var shadow = this.getShadow();

			if (shadow == null) {
				return;
			}

			// show shadow
			if (shadow.is(":hidden"))
				shadow.show();

			// set shadow position and size
			shadow.css({
				top : parseInt($dialog[0].style.top) - 2,
				left : parseInt($dialog[0].style.left) - 4,
				height : parseInt($dialog.height()) + 8,
				width : parseInt($dialog.width()) + 8,
				zIndex : parseInt($dialog.css("zIndex")) - 1
			});

			// set shadow height
			$(".shadow_c", shadow).children().andSelf().each(function() {
				$(this).css("height", $dialog.outerHeight() - 4);
			});
		},

		/**
		 * Close the Dialog
		 */
		close : function($dialog) {
			this._doCallback($dialog, "doClose");
		},

		/**
		 * Close the current active dialog
		 */
		closeCurrent : function() {
			this.close(this.currentDialog);
		},

		checkTimeout : function() {
			var $conetnt = $(".dialogContent", this.currentDialog);
			var json = jCocit.toJson($conetnt.html());
			if (json && json.statusCode == jCocit.status.ERROR_NO_ACCESS)
				this.closeCurrent();
		},

		/**
		 * Maxsize the dialog
		 * <P>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>$dialog: this is dialog jQuery object
		 * </UL>
		 */
		maxsize : function($dialog) {
			$dialog.data("original", {
				top : $dialog.css("top"),
				left : $dialog.css("left"),
				width : $dialog.css("width"),
				height : $dialog.css("height")
			});
			$("a.maximize", $dialog).hide();
			$("a.restore", $dialog).show();
			var iContentW = $(window).width();
			var iContentH = $(window).height();
			$dialog.css({
				top : "0px",
				left : "0px",
				width : iContentW + "px",
				height : iContentH + "px"
			});
			this._resizeContent($dialog, iContentW, iContentH);
		},

		/**
		 * Restore dialog from max to normal.
		 * <P>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>$dialog: this is dialog jQuery object
		 * </UL>
		 */
		restore : function($dialog) {
			var self = this;
			var original = $dialog.data("original");
			var dwidth = parseInt(original.width);
			var dheight = parseInt(original.height);
			$dialog.css({
				top : original.top,
				left : original.left,
				width : dwidth,
				height : dheight
			});
			self._resizeContent($dialog, dwidth, dheight);
			$("a.maximize", $dialog).show();
			$("a.restore", $dialog).hide();
			self.attachShadow($dialog);
		},

		/**
		 * Initialize dialog using options specified by parameter "opts".
		 * <p>
		 * <B>Parameters:</b>
		 * <UL>
		 * <LI>$dialog:
		 * <LI>opts:
		 * </UL>
		 */
		_init : function($dialog, opts) {
			var options = $.extend({}, defaults, opts);

			// get dialog header
			var $header = $(".dialogHeader", $dialog);

			// set dialog header height
			$header.height(options.headerHeight);
			$(".dialogHeader_c", $header).height(options.headerHeight);
			if (options.headerTitle.trim().length == 0) {
				$(".headerTitle", $header).hide();
			}
			if (options.headerSubTitle.trim().length == 0) {
				$(".headerSubTitle", $header).hide();
			}

			// set dialog footer
			var $footer = $(".dialogFooter", $dialog);

			// evaluate dialog height and width
			var height = options.height > options.minHeight ? options.height : options.minHeight;
			var width = options.width > options.minWidth ? options.width : options.minWidth;

			// set dialog height
			if (isNaN($dialog.height()) || $dialog.height() < height) {
				$dialog.height(height);
			}

			// set dialog width
			if (isNaN($dialog.css("width")) || $dialog.width() < width) {
				$dialog.width(width);
			}

			// set body and content size
			this._resizeContent($dialog, width, height);

			// set dialog position
			var iTop = ($(window).height() - $dialog.height()) / 2;
			$dialog.css({
				left : ($(window).width() - $dialog.width()) / 2,
				top : iTop > 0 ? iTop : 0
			});
		},

		/**
		 * Bind Button Call-back function.
		 */
		_bindButtonCallback : function($dialog) {
			var self = this;
			$(":button.Confirm", $dialog).click(function() {
				self._doCallback($dialog, "doConfirm");
				return false;
			});
			$(":button.Reset", $dialog).click(function() {
				self._doCallback($dialog, "doReset");
				return false;
			});
			$(":button.Cancel", $dialog).click(function() {
				self._doCallback($dialog, "doCancel");
				return false;
			});
			$(":button.Help", $dialog).click(function() {
				self._doCallback($dialog, "doHelp");
				return false;
			});
		},

		/**
		 * Process Button Logic of Dialog
		 * <p>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>$dialog: this is dialog jQuery object.
		 * <LI>callback: this is call-back function name. the dialog will be close if call-back function return true. so "Help" button cannot return true.
		 * </UL>
		 */
		_doCallback : function($dialog, callback) {
			if (typeof $dialog == 'string')
				$dialog = $("body").data($dialog);

			// get call-back function
			var doCallback = $dialog.data(callback);

			//
			var go = true;
			if (doCallback && $.isFunction(doCallback)) {

				// get call-back function parameters
				var param = $dialog.data("param");
				if (param && param != "") {
					param = jCocit.toJson(param);
					go = doCallback($dialog, param);
				} else {
					go = doCallback($dialog);
				}
				if (!go)
					return;
			}

			// destroy editor
			if ($.fn.xheditor) {
				$("textarea.editor", $dialog).xheditor(false);
			}

			// unbind click function
			$dialog.unbind("click").hide();
			$("div.dialogContent", $dialog).html("");

			// hide shadow and background
			var $shadow = this.getShadow();
			if ($shadow != null)
				$shadow.hide();

			if ($dialog.data("modal")) {
				this.getBackground().hide();
			}

			// remove dialog from "body"
			$("body").removeData($dialog.data("id"));
			$dialog.remove();
		},

		/**
		 * Resize dialog content with width and height
		 * <P>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>$dialog: this is dialog jQuery object
		 * <LI>width: this is dialog width
		 * <LI>height: this is dialog height
		 * </UL>
		 */
		_resizeContent : function($dialog, dialogWidth, dialogHeight) {
			var $dialogBody = $(".dialogBody", $dialog);
			var $dialogContent = $(".dialogContent", $dialog);

			var footerHeight = $(".dialogFooter", $dialog).outerHeight();
			var headerHeight = $(".dialogHeader", $dialog).outerHeight();

			$dialogBody.css({
				width : dialogWidth - getExternalWidth($dialogBody),
				height : dialogHeight - headerHeight - footerHeight - getExternalHeight($dialogBody)
			});

			$dialogContent.css({
				width : $dialogBody.width() - getExternalWidth($dialogContent),
				height : $dialogBody.outerHeight() - getExternalHeight($dialogContent) - 20
			});
		}

	};

	/**
	 * Define dialog drag class
	 */
	jCocit.dialogDrag = {

		/**
		 * Initialize dialog proxy when dragging dialog
		 */
		_init : function($dialog) {
			var $proxy = $("#dialogProxy");
			if ($proxy.length == 0) {
				$proxy = $(dialogTemplate).attr("id", "dialogProxy").addClass("dialogProxy");
				$("body").append($proxy);
			}
			$(".headerTitle", $proxy).html($(".dialogHeader .headerTitle", $dialog).text());
			$("a.close", $proxy).hide();
			$("a.maximize", $proxy).hide();

			return $proxy;
		},

		/**
		 * Start drag dialog
		 * <p>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>$dialog: this is dialog jQuery object
		 * <LI>event:
		 * </UL>
		 */
		start : function($dialog, event) {
			var $proxy = this._init($dialog);

			// set dialog proxy position and size to dialog object
			$proxy.css({
				left : $dialog.css("left"),
				top : $dialog.css("top"),
				height : $dialog.css("height"),
				width : $dialog.css("width"),
				zIndex : parseInt($dialog.css("zIndex")) + 1
			}).show();

			// set dialog proxy body height.
			$("div.dialogBody", $proxy).css("height", $("div.dialogBody", $dialog).css("height"));
			$("div.dialogContent", $proxy).css("height", $("div.dialogContent", $dialog).css("height"));
			$proxy.data("dialog", $dialog);
			$dialog.css({
				left : "-10000px",
				top : "-10000px"
			});
			$("#dialogShadow").hide();

			// drag dialog when clicking proxy header
			$proxy.draggable({
				handle : ".dialogHeader",
				onStopDrag : this.stop,
				event : event
			});
			event.type = "mousedown";
			$.data($proxy[0], "draggable").handle.trigger(event);

			return false;
		},

		/**
		 * Dragging Dialog End
		 */
		stop : function() {
			var $shadow = $(arguments[0].data.target);
			var $dialog = $shadow.data("dialog");
			$dialog.css({
				left : $shadow.css("left"),
				top : $shadow.css("top")
			});
			jCocit.dialog.attachShadow($dialog);
			$shadow.hide();
		}
	}

	/**
	 * Define Dialog Drag jQuery Object
	 */
	$.fn.dialogDrag = function(options) {
		if (typeof options == 'string') {
			if (options == 'destroy')
				return this.each(function() {
					var dialog = this;
					$("div.dialogHeader", dialog).unbind("mousedown");
				});
		}

		return this.each(function() {
			var dialog = $(this);
			var header = $("div.dialogHeader", dialog).mousedown(function(e) {
				jCocit.dialog.switchDialog(dialog);
				jCocit.dialogDrag.start(dialog, e);
				return false;
			}).mouseup(function(e) {
				return false;
			}).css("cursor", "move");
			$("a", header).mousedown(function(event) {
				event.stopPropagation();
			});
		});
	};

	$.fn.dialoglink = function(options) {
		return this.each(function() {
			$(this).click(function() {

				// parse dialog options
				var opts = $.extend({}, defaults, parseOptions(this), options || {});

				// open dialog
				jCocit.dialog.open(opts.url, opts.dialogId, opts);

				return false;
			});
		})
	}

	/**
	 * This is default settings
	 */
	var defaults = {
		width : 500,// Dialog Width
		height : 300,// Dialog Height
		minHeight : 40,// Dialog Minimum Height
		minWidth : 50,// Dialog Minimum Width
		headerHeight : 27,// Dialog Header Height
		// total : 20,
		headerTitle : "",// empty string means that the dialog header title is hidden
		headerSubTitle : "", // empty string means that the dialog header subtitle is hidden
		max : false,// true means that dialog max, full screen
		modal : false,// true means that dialog is modal
		// resizable : true,// true means that dialog is resizable
		draggable : true,// true means that dialog is draggable
		closable : true,// true means that dialog is closable
		maxable : true,// true means that dialog is maxable
		refresh : true
	// true means that dialog content will be refresh
	};

	/**
	 * Parse container options
	 */
	function parseOptions($container) {
		return jCocit.parseOptions($container, [ "url", "dialogId", "param", "headerTitle", "headerSubTitle", "styleName", {
			height : "number",
			width : "number",
			minHeight : "number",
			minWidth : "number",
			headerHeight : "number",
			max : "boolean",
			modal : "boolean",
			// resizable : "boolean",
			draggable : "boolean",
			closable : "boolean",
			maxable : "boolean",
			refresh : "boolean",
			doSuccess : "function",
			doConfirm : "function",
			doCancel : "function",
			doReset : "function",
			doHelp : "function",
			doClose : "function"
		} ]);
	}
})(jQuery, jCocit);