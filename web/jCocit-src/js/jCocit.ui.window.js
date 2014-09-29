/**
 * This is window UI plugin, extends from panel UI plugin.
 * <P>
 * Used to convert the original "DIV" element to window UI object.
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: resizable, draggable
 * <LI>ReferencedBy: unknown
 * <LI>SubClass: dialog
 * <LI>SuperClass: panel
 * </UL>
 * 
 * <P>
 * <B>Only window(panel don't) support to:
 * <UL>
 * <LI>draggable:
 * <LI>resizable:
 * <LI>shadow:
 * <LI>modal:
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
 * 			[div class='PnBC'] Here is window content. [/div] 
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
 * <LI>Wd: this is panel, used to wrap the original window body "DIV" element.
 * <LI>WdH: Window Header
 * <LI>WdB: Window Body
 * <LI>WdF: Window Footer
 * <LI>WdS: Window Shadow
 * <LI>WdM: Window Mask
 * <LI>WdP: Window Proxy for Dragging or Resizing
 * <LI>WdPM: Window Proxy Mask for Resizing
 * </UL>
 */
(function($, jCocit) {

	function _init(selfHTML) {
		var state = $d(selfHTML, "window");
		var opts = state.options;

		// convert to panel
		var $self = $(selfHTML).panel($.extend({}, opts, {
			border : false,
			doSize : true,
			closed : true,
			styleName : "Wd " + (opts.styleName || ""),
			headerCls : "WdH",
			bodyCls : "WdB " + (opts.noheader ? "WdB-NH" : ""),
			footerCls : "WdF",
			onBeforeDestroy : function() {
				if (opts.onBeforeDestroy.call(selfHTML) == false)
					return false;

				if (state.shadow)
					state.shadow.remove();

				if (state.mask)
					state.mask.remove();

			},
			onClose : function() {
				$(selfHTML).panel("destroy");

				opts.onClose.call(selfHTML);
			},
			onOpen : function() {
				if (opts.resizeOnWindow) {
					$(window).bind('resize.window', function() {
						$(selfHTML).window("center");
						resizeWindow(selfHTML);
					});
					$(selfHTML).window("center");
					resizeWindow(selfHTML);
				}

				if (state.mask)
					state.mask.css({
						display : "block",
						zIndex : $.fn.window.defaults.zIndex++
					});

				if (state.shadow)
					state.shadow.css({
						display : "block",
						zIndex : $.fn.window.defaults.zIndex++,
						left : opts.left,
						top : opts.top,
						width : state.window.ow(),
						height : state.window.oh()
					});

				state.window.css("z-index", $.fn.window.defaults.zIndex++);
				opts.onOpen.call(selfHTML);
			},
			onResize : function(width, height) {
				var panelOpts = $(this).panel("options");
				$.extend(opts, {
					width : panelOpts.width,
					height : panelOpts.height,
					left : panelOpts.left,
					top : panelOpts.top
				});
				if (state.shadow) {
					state.shadow.css({
						left : opts.left,
						top : opts.top,
						width : state.window.ow(),
						height : state.window.oh()
					});
				}

				opts.onResize.call(selfHTML, width, height);
			},
			onMinimize : function() {
				if (state.shadow)
					state.shadow.hide();

				if (state.mask)
					state.mask.hide();

				opts.onMinimize.call(selfHTML);
			},
			onBeforeCollapse : function() {
				if (opts.onBeforeCollapse.call(selfHTML) == false)
					return false;

				if (state.shadow)
					state.shadow.hide();

			},
			onExpand : function() {
				if (state.shadow)
					state.shadow.show();

				opts.onExpand.call(selfHTML);
			}
		}));

		// set window object
		state.window = $self.panel("panel");

		// initialize mask for modal window
		if (state.mask) {
			state.mask.remove();
		}
		if (opts.modal == true) {
			state.mask = $("<div class=\"WdM\"></div>").insertAfter(state.window);
			var windowSize = _getBrowserWindowSize();
			state.mask.css({
				width : (opts.inline ? $w($p(state.mask)) : windowSize.width),
				height : (opts.inline ? $h($p(state.mask)) : windowSize.height),
				display : "none"
			});
		}

		// initialize window shadow
		if (state.shadow) {
			state.shadow.remove();
		}
		if (opts.shadow == true) {
			state.shadow = $("<div class=\"WdS\"></div>").insertAfter(state.window);
			state.shadow.css({
				display : "none"
			});
		}

		// set X coordination
		if (opts.left == null) {
			hCenterWindow(selfHTML);
		}

		// set Y coordination
		if (opts.top == null) {
			vCenterWindow(selfHTML);
		}

		// Move window to position specified by window options
		moveWindowTo(selfHTML);

		// open window
		if (opts.closed == false) {
			$self.window("open");
		}
	}

	/**
	 * Initialize Draggable and Resizeable
	 */
	function _initDnr(selfHTML) {
		var state = $d(selfHTML, "window");
		var $window = state.window;
		var opts = state.options;

		// create draggable object for window
		if (opts.draggable)
			$window.draggable({
				handle : ">.PnH .PnHT",
				disabled : opts.draggable == false,
				onStartDrag : function(e) {
					if (state.mask) {
						state.mask.css("z-index", $.fn.window.defaults.zIndex++);
					}
					if (state.shadow) {
						state.shadow.css("z-index", $.fn.window.defaults.zIndex++);
					}
					$window.css("z-index", $.fn.window.defaults.zIndex++);

					// initialize window proxy to drag.
					if (!state.proxy) {
						state.proxy = $("<div class=\"WdP\"></div>").insertAfter($window);
					}
					state.proxy.css({
						display : "none",
						zIndex : $.fn.window.defaults.zIndex++,
						left : e.data.left,
						top : e.data.top
					});
					state.proxy.ow($window.ow());
					state.proxy.oh($window.oh());

					setTimeout(function() {
						if (state.proxy) {
							state.proxy.show();
						}
					}, 500);
				},
				onDrag : function(e) {
					state.proxy.css({
						display : "block",
						left : e.data.left,
						top : e.data.top
					});
					return false;
				},
				onStopDrag : function(e) {
					opts.left = e.data.left;
					opts.top = e.data.top;
					$(selfHTML).window("move");
					state.proxy.remove();
					state.proxy = null;
				}
			});

		// create resizable object for window
		if (opts.resizable)
			$window.resizable({
				disabled : opts.resizable == false,
				handles : "e, s, w, se, sw",
				onStartResize : function(e) {
					state.pmask = $("<div class=\"WdPM\"></div>").insertAfter($window);
					state.pmask.css({
						zIndex : $.fn.window.defaults.zIndex++,
						left : e.data.left,
						top : e.data.top,
						width : $window.ow(),
						height : $window.oh()
					});
					if (!state.proxy) {
						state.proxy = $("<div class=\"WdP\"></div>").insertAfter($window);
					}
					state.proxy.css({
						zIndex : $.fn.window.defaults.zIndex++,
						left : e.data.left,
						top : e.data.top
					});
					state.proxy.ow(e.data.width);
					state.proxy.oh(e.data.height);
				},
				onResize : function(e) {
					state.proxy.css({
						left : e.data.left,
						top : e.data.top
					});
					state.proxy.ow(e.data.width);
					state.proxy.oh(e.data.height);
					return false;
				},
				onStopResize : function(e) {
					$.extend(opts, {
						left : e.data.left,
						top : e.data.top,
						width : e.data.width,
						height : e.data.height
					});
					resizeWindow(selfHTML);
					state.pmask.remove();
					state.pmask = null;
					state.proxy.remove();
					state.proxy = null;
				}
			});

	}

	/**
	 * Resize window with "width/height/left/top" specified by "sizeData"
	 */
	function resizeWindow(selfHTML, sizeData) {
		var opts = $d(selfHTML, "window").options;

		// save size data into window options
		if (sizeData) {
			if (sizeData.width)
				opts.width = sizeData.width;

			if (sizeData.height)
				opts.height = sizeData.height;

			if (sizeData.left != null)
				opts.left = sizeData.left;

			if (sizeData.top != null)
				opts.top = sizeData.top;

		}

		// invoke "resize" method of panel
		$(selfHTML).panel("resize", opts);
	}

	/**
	 * Move window to position specified by "position"
	 */
	function moveWindowTo(selfHTML, position) {
		var state = $d(selfHTML, "window");

		// save position data into window options
		if (position) {
			if (position.left != null)
				state.options.left = position.left;

			if (position.top != null)
				state.options.top = position.top;

		}

		// invoke "move" method of panel
		$(selfHTML).panel("move", state.options);
		if (state.shadow) {
			state.shadow.css({
				left : state.options.left,
				top : state.options.top
			});
		}
	}

	/**
	 * Evaluate window X coordination, "bMoveTo" equals true means that move window to specified position
	 */
	function hCenterWindow(selfHTML, bMoveTo) {
		var state = $d(selfHTML, "window");
		var opts = state.options;

		var width = opts.width;
		if (isNaN(width)) {
			width = state.window.ow();
		}

		if (opts.inline) {
			var $parent = $p(state.window);
			opts.left = ($w($parent) - width) / 2 + $parent.scrollLeft();
			if (opts.left < 5) {
				opts.left = 5;
			}
		} else {
			opts.left = ($(window).ow() - width) / 2 + $(document).scrollLeft();
			if (opts.left < 5) {
				opts.left = 5;
			}
		}

		if (bMoveTo) {
			moveWindowTo(selfHTML);
		}
	}

	/**
	 * Evaluate window Y coordination, "bMoveTo" equals true means that move window to specified position
	 */
	function vCenterWindow(selfHTML, bMoveTo) {
		var state = $d(selfHTML, "window");
		var opts = state.options;

		var height = opts.height;
		if (isNaN(height)) {
			height = state.window.oh();
		}

		if (opts.inline) {
			var $parent = $p(state.window);
			opts.top = ($h($parent) - height) / 2 + $parent.scrollTop();
			if (opts.top < 5) {
				opts.top = 5;
			}
		} else {
			opts.top = ($(window).oh() - height) / 2 + $(document).scrollTop();
			if (opts.top < 5) {
				opts.top = 5;
			}
		}

		if (bMoveTo) {
			moveWindowTo(selfHTML);
		}
	}

	/**
	 * Get browser window size
	 */
	function _getBrowserWindowSize() {
		if (document.compatMode == "BackCompat") {
			return {
				width : Math.max(document.body.scrollWidth, document.body.clientWidth),
				height : Math.max(document.body.scrollHeight, document.body.clientHeight)
			};
		} else {
			return {
				width : Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth),
				height : Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight)
			};
		}
	}

	/**
	 * The window size will be changed when the browser window size changed
	 */
	$(window).resize(function() {
		$("body>.WdM").css({
			width : $(window).ow(),
			height : $(window).oh()
		});
		setTimeout(function() {
			var windowSize = _getBrowserWindowSize();
			$("body>.WdM").css({
				width : windowSize.width,
				height : windowSize.height
			});
		}, 50);
	});

	jCocit.window = {
		open : function(url, windowId, options, subclass) {
			var $windowContent = $("#" + windowId);
			if (!$windowContent.length) {
				$windowContent = $('<div id="' + windowId + '"></div>').appendTo("body");
			}

			if (url)
				options.url = url;

			$windowContent[subclass || "window"](options);

			return $windowContent;
		},
		close : function($windowContent) {
			if (typeof $windowContent == "string") {
				$windowContent = $("#" + $windowContent);
			}
			$windowContent.window("close");
		},
		get : function(windowId) {
			return $("#" + windowId);
		}
	};

	/**
	 * Window Link: Click it, a Window will be open.
	 */
	$.fn.windowlink = function(options) {
		return this.each(function() {
			$(this).click(function() {
				var opts = $.extend({}, $.fn.window.defaults, $.fn.window.parseOptions(this), options || {});
				opts.id = "__jCocit_window_" + opts.id;
				jCocit.window.open(opts.url, opts.dialogId, opts);
				return false;
			});
		})
	};

	/**
	 * 1. Create window UI object or set window properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke window method or panel method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.window = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.window.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.panel(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "window");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, $.fn.window.defaults, $.fn.window.parseOptions(this), options);
				state = $d(this, "window", {
					options : opts
				});
				if (!state.options.inline) {
					document.body.appendChild(this);
				}
			}

			if (!opts.title || opts.title.trim().length == 0)
				opts.title = $.fn.window.defaults.title;

			_init(this);
			_initDnr(this);
		});
	};

	/**
	 * Window methods overwrite $.fn.window.methods
	 */
	$.fn.window.methods = {
		options : function(jq) {
			var panelOpts = jq.panel("options");
			var opts = $d(jq[0], "window").options;
			return $.extend(opts, {
				closed : panelOpts.closed,
				collapsed : panelOpts.collapsed,
				minimized : panelOpts.minimized,
				maximized : panelOpts.maximized
			});
		},
		window : function(jq) {
			return $d(jq[0], "window").window;
		},
		resize : $X(resizeWindow),
		move : $X(moveWindowTo),
		hcenter : $X(hCenterWindow, true),
		vcenter : $X(vCenterWindow, true),
		center : function(jq) {
			return jq.each(function() {
				hCenterWindow(this);
				vCenterWindow(this);
				moveWindowTo(this);
			});
		}
	};

	/**
	 * Window options parser extends from $.fn.panel
	 */
	$.fn.window.parseOptions = function(selfHTML) {
		return $.extend({}, $.fn.panel.parseOptions(selfHTML), jCocit.parseOptions(selfHTML, [ {
			draggable : "b",
			resizable : "b",
			shadow : "b",
			modal : "b",
			inline : "b",
			resizeOnWindow : "b"
		} ]));
	};

	/**
	 * Window default settings extends from $.fn.panel
	 */
	$.fn.window.defaults = $.extend({}, $.fn.panel.defaults, {
		/*
		 * only window own the following properties, the panel don't contains these properties.
		 */
		zIndex : 9000,
		draggable : true,
		resizable : true,
		shadow : true,
		modal : false,
		inline : false,// true means that this window will be shown in parent element.
		/**
		 * resizeOnWindow is true means that the window will be resize when browser window size changed.
		 */
		resizeOnWindow : false,

		/*
		 * the following properties come from panel. here setting window panel default value.
		 */
		title : jCocit.defaults.title,
		minimizable : true,
		maximizable : true,
		closable : true
	});
})(jQuery, jCocit);
