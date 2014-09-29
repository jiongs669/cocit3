/**
 * This is panel UI plugin.
 * <P>
 * Used to convert the original "DIV" element to panel UI object.
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: none
 * <LI>ReferencedBy: combo
 * <LI>SubClass: window, dialog
 * <LI>SuperClass: none
 * </UL>
 * 
 * <pre>
 *  [div class='Pn']
 *  	[div class='PnH']
 *  		[div class='PnHR'][div class='PnHC']
 *  			[div class='PnHL'][/div]
 *  			[div class='PnHT'] Here is panel title. [/div]
 *  			[div class='PnHB']
 *  				[a class='PnHBB' href='javascript:void(0)'][/a]
 *  				[a class='PnHBL' href='javascript:void(0)'][/a]
 *  				[a class='PnHBN' href='javascript:void(0)'][/a]
 *  				[a class='PnHBX' href='javascript:void(0)'][/a]
 *  				[a class='PnHBC' href='javascript:void(0)'][/a]
 *  			[/div]
 *  		[/div][/div]
 * 		[/div]
 * 		[div class='PnB']
 * 			[div class='PnBC'] Here is panel content. [/div]
 * 		[/div]
 * 		[div class='PnF'][div class='PnFR'][div class='PnFC'][/div][/div][/div]
 *  [/div]
 * </pre>
 * 
 * <p>
 * <B>Notes:</B>
 * <UL>
 * <LI>selfHTML: may be "panel-content(default)", "panel-body", "panel".
 * <LI>panel:
 * <LI>panel-body:
 * <LI>panel-content:
 * <LI>Pn: Panel, this is panel root "DIV" element.
 * <LI>PnH: Panel Header
 * <LI>PnH-NB: Panel Header No Border
 * <LI>PnHR: Panel Header Right
 * <LI>PnHC: Panel Header Center
 * <LI>PnHL: Panel Header Logo
 * <LI>PnHT: Panel Header Title
 * <LI>PnHT-WL: Panel Header Title With Logo
 * <LI>PnHB: Panel Header Button
 * <LI>PnHBB: Panel Header Button(customized)
 * <LI>PnHBL: Panel Header Button(collapse)
 * <LI>PnHBE: Panel Header Button(expand)
 * <LI>PnHBN: Panel Header Button(minimize)
 * <LI>PnHBX: Panel Header Button(maximize)
 * <LI>PnHBR: Panel Header Button(restore)
 * <LI>PnHBC: Panel Header Button(close)
 * <LI>PnB: Panel Body
 * <LI>PnB-NB: Panel Body No Border
 * <LI>PnB-NH: Panel Body No Header
 * <LI>PnBC: Panel Body Content
 * <LI>PnBCL: Panel Body Content Loading
 * <LI>PnF: Panel Footer
 * <LI>PnF-NB: Panel Footer No Border
 * <LI>PnFR: Panel Footer Right
 * <LI>PnFC: Panel Footer Center
 * </UL>
 */
(function($, jCocit) {

	function _state(selfHTML, data) {
		return $d(selfHTML, "panel", data);
	}

	function _panel(selfHTML) {
		return _state(selfHTML).panel;
	}

	/**
	 * Initialize panel UI object.
	 * <P>
	 * <B>Return: </B> "panel" jQuery object
	 */
	function _initPanel(selfHTML) {
		var $self = $(selfHTML);

		/*
		 * Wrap self HTML become "Panel"
		 */
		var $panel = null;
		if ($hc("panel", $self)) {
			$panel = $ac("Pn", $self);

			// Wrap inner HTML become "Panel Body"
			var $panelBody = $c(".panel-body", $panel);
			if (!$panelBody.length)
				$panelBody = $c($panel.wrapInner('<div class="PnB"></div>'));
			else
				$ac("PnB", $panelBody);

			// Wrap inner HTML become "Panel Body Content"
			var $panelContent = $c(".panel-content", $panelBody);
			if (!$panelContent.length)
				$panelBody.wrapInner('<div class="PnBC"></div>');
			else
				$ac("PnBC", $panelContent);

		} else if ($hc("panel-body", $self)) {
			var $panelBody = $ac("PnB", $self);

			// Wrap "PnB" become "Panel"
			$panel = $p($panelBody.wrap('<div class="Pn"></div>'));

			// Wrap inner HTML become "PnBC"
			var $panelContent = $c(".PnBC", $panelBody);
			if (!$panelContent.length)
				$panelBody.wrapInner('<div class="PnBC"></div>');
			else
				$ac("PnBC", $panelContent);

		} else {
			var $panelContent = $ac("PnBC", $self);

			// Wrap "PnBC" become "panel"
			$panel = $p($p($panelContent.wrap('<div class="Pn"><div class="PnB"></div></div>')));

		}

		/*
		 * Bind resize event on the panel
		 */
		$panel.bind("_resize", function() {
			var opts = _state(selfHTML).options;
			if (opts.fit == true) {
				resizePanel(selfHTML);
			}

			return false;
		});

		return $panel;
	}

	/**
	 * Initialize Panel Header
	 */
	function _initPanelHeaderAndFooter(selfHTML) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		/*
		 * No Panel Header
		 */
		if (!opts.title || opts.noheader) {
			$ac("PnB-NH", $c(".PnB", $panel));

			/*
			 * Remove panel header/footer
			 */
			_destroy($c(".PnH", $panel));
			_destroy($c(".PnF", $panel));

			return;
		}

		/*
		 * Move old tool buttons into toolbar specified by "tools" in options
		 */
		// if (opts.tools && typeof opts.tools == "string")
		// $f(">.PnH>.PnHB .PnHBB"， $panel).appendTo(opts.tools);
		//
		/*
		 * Init Panel Header
		 */
		var $panelHeader = $c(".PnH", $panel);
		if (!$panelHeader.length)
			$panelHeader = $(
					'<div class="PnH"><div class="PnHR"><div class="PnHC"><div class="PnHL"></div><div class="PnHT" unselectable="on"></div><div class="PnHB" unselectable="on"><a class="PnHBL" href="javascript:void(0)"></a><a class="PnHBN" href="javascript:void(0)"></a><a class="PnHBX" href="javascript:void(0)"></a><a class="PnHBC" href="javascript:void(0)"></a></div></div></div></div>')
					.prependTo($panel);

		// Set Panel Title
		var $panelTitle = $f(".PnHT", $panelHeader).html(opts.title);

		// Set Panel Logo
		if (opts.logoCls) {
			$ac("PnHT-WL", $panelTitle);
			$ac(opts.logoCls, $f(".PnHL", $panelHeader));
		}

		// add tool bar to panel header
		var $panelTools = $f(".PnHB", $panelHeader);
		$panelTools.bind("click", function(e) {
			e.stopPropagation();
		}).bind("mouseover", function(e) {
			e.stopPropagation();
		});

		var $panelCollapse = $f(".PnHBL", $panelHeader);

		// add customized tool button to panel tool bar
		if (opts.tools) {
			var tools = opts.tools;
			if (typeof tools == "string") {
				$c($(tools)).each(function() {
					var $toolBtn = $(this);
					$ac("PnHBB", $ac($toolBtn.attr("iconCls"), $toolBtn)).insertBefore($panelCollapse);
				});
			} else {
				for ( var i = 0; i < tools.length; i++) {
					var tool = tools[i];
					var $toolBtn = $ac(tool.iconCls, $('<a href="javascript:void(0)"></a>')).insertBefore($panelCollapse);
					if (tool.handler) {
						if ($.type(tool.handler) == "function")
							$toolBtn.bind("click", tool.handler);// eval function cannot be compress
						else
							$toolBtn.bind("click", $fn(tool.handler));
					}
				}
			}
		}

		// show/remove collapse button
		if (opts.collapsible) {
			$panelCollapse.show().bind("click", function() {
				if (opts.collapsed == true)
					expandPanel(selfHTML, true);
				else
					collapsePanel(selfHTML, true);

				return false;
			});
		} else
			$panelCollapse.remove();

		// show/remove minimize button
		if (opts.minimizable)
			$f(".PnHBN", $panelHeader).show().bind("click", function() {
				minimizePanel(selfHTML);
				return false;
			});
		else
			$f(".PnHBN", $panelHeader).remove();

		// show/remove maximize button
		if (opts.maximizable)
			$f(".PnHBX", $panelHeader).show().bind("click", function() {
				if (opts.maximized == true) {
					restorePanel(selfHTML);
				} else {
					maximizePanel(selfHTML);
				}
				return false;
			});
		else
			$f(".PnHBX", $panelHeader).remove();

		// show/remove close button
		if (opts.closable)
			$f(".PnHBC", $panelHeader).show().bind("click", function() {
				closePanel(selfHTML);
				return false;
			});
		else
			$f(".PnHBC", $panelHeader).remove();

		// add footer to panel
		if (!$c(".PnF", $panel).length)
			$('<div class="PnF"><div class="PnFR"><div class="PnFC"></div></div></div>').appendTo($panel);

		//
		$rc("PnB-NH", $c(".PnB", $panel));
	}

	/**
	 * Destroy element specified by "$source" from document
	 */
	function _destroy($source) {
		$source.each(function() {
			$(this).remove();
			if ($.browser.msie) {
				this.outerHTML = "";
			}
		});
	}

	/**
	 * Resize panel with the "width/height/left/top" specified by "sizeData"
	 */
	function resizePanel(selfHTML, sizeData) {
		var $self = $(selfHTML);
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		var $panelHeader = $c(".PnH", $panel);
		var $panelBody = $c(".PnB", $panel);
		var $panelContent = $f(">.PnB>.PnBC", $panel);
		var $panelFooter = $c(".PnF", $panel);

		// save size data into options
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

		// fit equals true means that fit the panel "width/height" to parent
		opts.fit ? $.extend(opts, $panel._fit()) : $panel._fit(false);

		// set panel position
		$panel.css({
			left : opts.left,
			top : opts.top
		});

		// set panel width
		if (!isNaN(opts.width)) {
			$panel.ow(opts.width);
		} else {
			if ($.type(opts.width) == "string" && opts.width.endsWith("%")) {
				var $parent = $p($panel);
				if ($parent[0] == document.body)
					$parent = $(window);
				$panel.ow(parseInt($parent.ow() * parseInt(opts.width) / 100));
			} else {
				$w("auto", $panel);
			}
		}

		// set panel "header/body" width
		$panelHeader.add($panelBody).add($panelFooter).ow($w($panel));
		// var borderWidth = $panelBody._css("border-left-width") + $panelBody._css("border-right-width");
		// if (borderWidth > 0 && $panelContent._css("border-left-width") > 0)
		// borderWidth += 2;
		$panelContent.ow($w($panelBody), true);

		// set panel and panel body height
		if (!isNaN(opts.height)) {
			$panel.oh(opts.height);
			$panelBody.oh($h($panel) - $panelHeader.oh() - $panelFooter.oh(), true);
			$panelContent.oh($panelBody.oh(), true);
		} else {
			if ($.type(opts.height) == "string" && opts.height.endsWith("%")) {
				var $parent = $p($panel);
				if ($parent[0] == document.body)
					$parent = $(window);
				$panel.oh(parseInt($parent.oh() * parseInt(opts.height) / 100));
				$panelBody.oh($h($panel) - $panelHeader.oh() - $panelFooter.oh(), true);
				$panelContent.oh($panelBody.oh(), true);
			} else {
				$h("auto", $panelBody);
				$h("auto", $panelContent);
			}
		}
		$panel.css("height", "");

		// trigger resize event on panel body "DIV"
		opts.onResize.apply(selfHTML, [ opts.width, opts.height ]);

		// trigger resize event on the panel content "DIV"
		$f(">.PnB>.PnBC>div", $panel).triggerHandler("_resize");
	}

	/**
	 * Move panel to specified by "position"
	 */
	function moveTo(selfHTML, position) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		// save position data into options
		if (position) {
			if (position.left != null)
				opts.left = position.left;

			if (position.top != null)
				opts.top = position.top;

		}

		// set panel position
		$panel.css({
			left : opts.left,
			top : opts.top
		});

		// trigger onMove event on panel body "DIV"
		opts.onMove.apply(selfHTML, [ opts.left, opts.top ]);
	}

	/**
	 * Refresh panel content with "href/content" in options
	 */
	function refreshContent(selfHTML) {
		var $self = $(selfHTML);
		var state = _state(selfHTML);
		var opts = state.options;

		var $panelContent = $f(">.PnB>.PnBC", state.panel);

		// refresh panel body with "url" specified by options
		if (opts.url) {
			if (!state.isLoaded || !opts.cache) {
				state.isLoaded = false;
				_destroyPanelBody(selfHTML);
				if (opts.loading) {
					$panelContent.html($("<div class=\"PnBCL\"></div>").html(opts.loading));
				}
				$.doAjax({
					url : opts.url,
					cache : false,
					dataType : "html",
					success : function(responseHTML) {
						state.isLoaded = true;
						_setContent(opts.extractor.call(selfHTML, responseHTML));
						opts.onLoad.apply(selfHTML, arguments);
					}
				});
			}
		} else {
			// refresh panel body with "content" specified by options
			if (opts.content) {
				if (!state.isLoaded) {
					_destroyPanelBody(selfHTML);
					_setContent(opts.content);
					state.isLoaded = true;
				}
			}
		}

		function _setContent(content) {
			$panelContent.html(content);
			jCocit.parseUI($panelContent);
		}

	}

	/**
	 * Destroy panel body content
	 */
	function _destroyPanelBody(selfHTML) {
		var $self = $(selfHTML);
		var $panelContent = $f(">.PnB>.PnBC", _state(selfHTML).panel);

		$f(".combo-f", $panelContent).each(function() {
			$(this).combo("destroy");
		});
		$f(".m-btn", $panelContent).each(function() {
			$(this).menubar("destroy");
		});
		$f(".s-btn", $panelContent).each(function() {
			if($.fn.splitbutton)
				$(this).splitbutton("destroy");
		});
	}

	/**
	 * Trigger resize event on all panel body content
	 */
	function _triggerResize(selfHTML) {
		$f(".panel:visible,.accordion:visible,.tabs-container:visible,.layout:visible", $(selfHTML)).each(function() {
			$(this).triggerHandler("_resize", [ true ]);
		});
	}

	/**
	 * Open panel
	 */
	function openPanel(selfHTML, ignoreOnBeforeOpen) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		// call-back onBeforeOpen function
		if (ignoreOnBeforeOpen != true) {
			if (opts.onBeforeOpen.call(selfHTML) == false) {
				return;
			}
		}

		// show panel
		$panel.add($c(".PnB", $panel)).add($f(">.PnB>.PnBC", $panel)).show();

		opts.closed = false;
		opts.minimized = false;

		// has restore button
		var $btnRestore = $f("a.PnHBR", $c(".PnH", $panel));
		if ($btnRestore.length) {
			opts.maximized = true;
		}

		// call-back onOpen function
		opts.onOpen.call(selfHTML);

		// maximize panel
		if (opts.maximized == true) {
			opts.maximized = false;
			maximizePanel(selfHTML);
		}

		// collapse panel
		if (opts.collapsed == true) {
			opts.collapsed = false;
			collapsePanel(selfHTML);
		}

		//
		if (!opts.collapsed) {
			refreshContent(selfHTML);
			_triggerResize(selfHTML);
		}
	}

	/**
	 * Close panel
	 */
	function closePanel(selfHTML, ignoreOnBeforeClose) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		// call-back onBeforeClose function
		if (ignoreOnBeforeClose != true) {
			if (opts.onBeforeClose.call(selfHTML) == false) {
				return;
			}
		}

		// hide panel
		$panel._fit(false);
		$panel.hide();

		// call-back onClose function
		opts.closed = true;
		opts.onClose.call(selfHTML);
	}

	/**
	 * Destroy panel
	 */
	function destroyPanel(selfHTML, ignoreOnBeforeDestroy) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		// call-back onBeforeDestroy function
		if (ignoreOnBeforeDestroy != true) {
			if (opts.onBeforeDestroy.call(selfHTML) == false) {
				return;
			}
		}

		_destroyPanelBody(selfHTML);
		_destroy($panel);

		opts.onDestroy.call(selfHTML);
	}

	/**
	 * Collapse panel
	 */
	function collapsePanel(selfHTML, animate) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;
		var $panelBody = $c(".PnB", $panel);

		var $btnCollapse = $f("a.PnHBL", $c(".PnH", $panel));

		// panel has been collapsed
		if (opts.collapsed == true) {
			return;
		}

		// stop animation
		$panelBody.stop(true, true);

		// call-back onBeforeCollapse function
		if (opts.onBeforeCollapse.call(selfHTML) == false) {
			return;
		}

		// change toggle tool button to expand
		$ac("PnHBE", $btnCollapse);
		if (animate == true) {
			$panelBody.slideUp("normal", function() {
				opts.collapsed = true;
				opts.onCollapse.call(selfHTML);
			});
		} else {
			$panelBody.hide();
			opts.collapsed = true;
			opts.onCollapse.call(selfHTML);
		}
	}

	/**
	 * Expand panel
	 */
	function expandPanel(selfHTML, animate) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		var $panelBody = $c(".PnB", $panel);

		var $btnCollapse = $f("a.PnHBL", $c(".PnH", $panel));

		// the curren status is expand
		if (opts.collapsed == false) {
			return;
		}

		// stop animation
		$panelBody.stop(true, true);

		// call-back onBeforeExpand function
		if (opts.onBeforeExpand.call(selfHTML) == false) {
			return;
		}

		// remove tool expand button
		$rc("PnHBE", $btnCollapse);

		// expand panel
		if (animate == true) {
			$panelBody.slideDown("normal", function() {
				opts.collapsed = false;
				opts.onExpand.call(selfHTML);
				refreshContent(selfHTML);
				_triggerResize(selfHTML);
			});
		} else {
			$panelBody.show();
			opts.collapsed = false;
			opts.onExpand.call(selfHTML);
			refreshContent(selfHTML);
			_triggerResize(selfHTML);
		}
	}

	function maximizePanel(selfHTML) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		var $btnMax = $f("a.PnHBX", $c(".PnH", $panel));

		if (opts.maximized == true) {
			return;
		}

		$ac("PnHBR", $btnMax);
		if (!_state(selfHTML).original) {
			_state(selfHTML).original = {
				width : opts.width,
				height : opts.height,
				left : opts.left,
				top : opts.top,
				fit : opts.fit
			};
		}

		opts.left = 0;
		opts.top = 0;
		opts.fit = true;

		resizePanel(selfHTML);

		opts.minimized = false;
		opts.maximized = true;

		opts.onMaximize.call(selfHTML);
	}

	function minimizePanel(selfHTML) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		$panel._fit(false);
		$panel.hide();
		opts.minimized = true;
		opts.maximized = false;

		opts.onMinimize.call(selfHTML);
	}

	function restorePanel(selfHTML) {
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		var $btnMax = $f("a.PnHBX", $c(".PnH", $panel));

		if (opts.maximized == false) {
			return;
		}

		$panel.show();
		$rc("PnHBR", $btnMax);
		$.extend(opts, _state(selfHTML).original);

		resizePanel(selfHTML);

		opts.minimized = false;
		opts.maximized = false;

		_state(selfHTML).original = null;

		opts.onRestore.call(selfHTML);
	}

	function _initPanelStyle(selfHTML) {
		var $self = $(selfHTML);
		var state = _state(selfHTML);
		var opts = state.options;
		var $panel = state.panel;

		var $panelHeader = $c(".PnH", $panel);
		var $panelBody = $c(".PnB", $panel);
		var $panelContent = $f(">.PnB>.PnBC", $panel);
		var $panelFooter = $c(".PnF", $panel);

		$panel.css(opts.style);
		$ac(opts.styleName, $panel);

		if (opts.border) {
			$rc("PnH-NB", $panelHeader);
			$rc("PnB-NB", $panelBody);
			$rc("PnF-NB", $panelFooter);
		} else {
			$ac("PnH-NB", $panelHeader);
			$ac("PnB-NB", $panelBody);
			$ac("PnF-NB", $panelFooter);
		}

		$ac(opts.headerCls, $panelHeader);
		$ac(opts.bodyCls, $panelBody);
		$ac(opts.footerCls, $panelFooter);

		if (opts.id) {
			$panelContent.attr("id", opts.id);
		} else {
			$panelContent.attr("id", "");
		}
	}

	/**
	 * Set panel header title
	 */
	function setTitle(selfHTML, title) {
		_state(selfHTML).options.title = title;
		$f(".PnHT", $c(".PnH", $panel)).html(title);
	}

	// /**
	// * the all panel size will be changed when the browser window size is changed.
	// */
	// var _setTimeout = false;
	// var _resized = true;
	// $(window).unbind(".panel").bind("resize.panel", function() {
	// if (!_resized) {
	// return;
	// }
	// if (_setTimeout !== false) {
	// clearTimeout(_setTimeout);
	// }
	// _setTimeout = setTimeout(function() {
	// _resized = false;
	// var $body = $("body.layout");
	// if ($body.length) {
	// $body.layout("resize");
	// } else {
	// $c(".panel,.accordion,.tabs-container,.layout", $("body")).triggerHandler("_resize");
	// }
	// _resized = true;
	// _setTimeout = false;
	// }, 200);
	// });

	/**
	 * 1. Create panel UI object or set panel properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke panel method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.panel = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.panel.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.panel');
		}
		options = options || {};
		return this.each(function() {
			var state = _state(this);
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.isLoaded = false;
			} else {
				opts = $.extend({}, $.fn.panel.defaults, $.fn.panel.parseOptions(this), options);
				$(this).attr("title", "");
				state = _state(this, {
					options : opts,
					panel : _initPanel(this),
					isLoaded : false
				});
			}
			_initPanelHeaderAndFooter(this);
			_initPanelStyle(this);
			if (opts.doSize == true) {
				state.panel.css("display", "block");
				resizePanel(this);
			}
			if (opts.closed == true || opts.minimized == true) {
				state.panel.hide();
			} else {
				openPanel(this);
			}
		});
	};

	$.fn.panel.methods = {
		options : function(jq) {
			return _state(jq[0]).options;
		},
		panel : function(jq) {
			return _panel(jq[0]);
		},
		header : function(jq) {
			return $c(".PnH", _panel(jq[0]));
		},
		body : function(jq) {
			return $c(".PnB", _panel(jq[0]));
		},
		content : function(jq) {
			return $f(">.PnB>.PnBC", _panel(jq[0]));
		},
		footer : function(jq) {
			return $c(".PnF", _panel(jq[0]));
		},
		setTitle : $X(setTitle),
		open : $X(openPanel),
		close : $X(closePanel),
		destroy : $X(destroyPanel),
		refresh : function(jq, url) {
			return jq.each(function() {
				_state(this).isLoaded = false;
				if (url) {
					_state(this).options.url = url;
				}
				refreshContent(this);
			});
		},
		resize : $X(resizePanel),
		move : $X(moveTo),
		maximize : $X(maximizePanel),
		minimize : $X(minimizePanel),
		restore : $X(restorePanel),
		collapse : $X(collapsePanel),
		expand : $X(expandPanel)
	};

	$.fn.panel.parseOptions = function(selfHTML) {
		var $self = $(selfHTML);
		return $.extend({}, jCocit.parseOptions(selfHTML, [ "id", "width", "height", "left", "top", "title", "logoCls", "styleName", "headerCls", "bodyCls", "footerCls", "tools", "url", {
			cache : "b",
			fit : "b",
			border : "b",
			noheader : "b"
		}, {
			collapsible : "b",
			minimizable : "b",
			maximizable : "b"
		}, {
			closable : "b",
			collapsed : "b",
			minimized : "b",
			maximized : "b",
			closed : "b"
		} ]), {
			loading : ($self.attr("loading") != undefined ? $self.attr("loading") : undefined)
		});
	};

	$.fn.panel.defaults = {
		id : null,
		title : null,
		logoCls : null,
		width : "auto",
		height : "auto",
		left : null,
		top : null,
		styleName : null,
		headerCls : null,
		bodyCls : null,
		footerCls : null,
		style : {},
		// href : null,
		cache : true,
		fit : false,// true means that this panel with/height will be auto fit to parent.
		border : true,
		doSize : true,
		noheader : false,
		content : null,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closable : false,
		collapsed : false,
		minimized : false,
		maximized : false,
		closed : false,
		tools : null,
		url : null,
		loading : jCocit.defaults.loading,
		extractor : function(responseHTML) {
			var reg = /<body[^>]*>((.|[\n\r])*)<\/body>/im;
			var content = reg.exec(responseHTML);
			if (content) {
				return content[1];
			} else {
				return responseHTML;
			}
		},
		onLoad : $n,
		onBeforeOpen : $n,
		onOpen : $n,
		onBeforeClose : $n,
		onClose : $n,
		onBeforeDestroy : $n,
		onDestroy : $n,
		onResize : $n,// args: width, height
		onMove : $n,// args: left, top
		onMaximize : $n,
		onRestore : $n,
		onMinimize : $n,
		onBeforeCollapse : $n,
		onBeforeExpand : $n,
		onCollapse : $n,
		/**
		 * 
		 */
		onExpand : $n
	};

})(jQuery, jCocit);
