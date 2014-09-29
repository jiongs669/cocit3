/**
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: panel
 * <LI>ReferencedBy:
 * <LI>SubClass:
 * <LI>SuperClass:
 * </UL>
 * 
 * <pre>
 * 	[div class='accordion']
 * 		[div class='Pn']...[/div]
 * 		[div class='Pn']...[/div]
 * 		[div class='Pn']...[/div]
 * 	[/div]
 * </pre>
 * 
 * <p>
 * <UL>
 * <LI>accordion:
 * <LI>Pn: panel, accordion item
 * </UL>
 */
(function($, jCocit) {

	function _state(selfUL, data) {
		return $d(selfUL, "accordion", data);
	}

	function _init(accordionDIV) {
		var $accordion = $(accordionDIV);
		$ac("accordion", $accordion);
		var array$panel = [];

		$c("div", $accordion).each(function() {
			var $panel = $(this);
			var panelOptions = $.extend({}, jCocit.parseOptions(this), {
				selected : ($panel.attr("selected") ? true : undefined)
			});
			array$panel.push($panel);
			_initPanel(accordionDIV, $panel, panelOptions);
		});

		$accordion.bind("_resize", function(e, fit) {
			var opts = _state(accordionDIV).options;
			if (opts.fit == true || fit) {
				resize(accordionDIV);
			}
			return false;
		});

		return {
			accordion : $accordion,
			panels : array$panel
		};
	}

	function _initEvent(accordionDIV) {
		var opts = _state(accordionDIV).options;
		if (opts.resizeOnWindow) {
			$(window).bind('resize.accordion', function() {
				resize(accordionDIV);
			});
		}
	}

	function _initPanel(accordionDIV, $panel, panelOptions) {
		$panel.panel($.extend({}, panelOptions, {
			collapsible : false,
			minimizable : false,
			maximizable : false,
			closable : false,
			doSize : false,
			collapsed : true,
			headerCls : "accordion-header",
			bodyCls : "accordion-body",
			onBeforeExpand : function() {
				var $selectedPanel = getSelected(accordionDIV);

				// unselect panel
				if ($selectedPanel) {
					var $panelHeader = $selectedPanel.panel("header");
					$rc("accordion-header-selected", $panelHeader);
					$f(".accordion-collapse", $panelHeader).triggerHandler("click");
				}

				// select the current panel
				var $panelHeader = $panel.panel("header");
				$ac("accordion-header-selected", $panelHeader);
				$rc("accordion-expand", $f(".accordion-collapse", $panelHeader));
			},
			onExpand : function() {
				var opts = _state(accordionDIV).options;
				opts.onSelect.call(accordionDIV, $panel.panel("options").title, getPanelIndex(accordionDIV, this));
			},
			onBeforeCollapse : function() {
				var $panelHeader = $panel.panel("header");
				$rc("accordion-header-selected", $panelHeader);
				$ac("accordion-expand", $f(".accordion-collapse", $panelHeader));
			}
		}));

		// add arrow into panel header
		var $panelHeader = $panel.panel("header");
		var $arrow = $('<a class="accordion-collapse accordion-expand" href="javascript:void(0)"></a>').appendTo($f("div.PnHB", $panelHeader));

		// click panel header arrow to expand/collapse panel
		$arrow.bind("click", function(e) {
			var animate = _state(accordionDIV).options.animate;
			_stopPanel(accordionDIV);
			if ($panel.panel("options").collapsed) {
				$panel.panel("expand", animate);
			} else {
				$panel.panel("collapse", animate);
			}
			return false;
		});

		// click panel header to expand/collapse panel
		$panelHeader.click(function() {
			$f(".accordion-collapse", $(this)).triggerHandler("click");
			return false;
		});
	}

	function _initStyle(accordionDIV) {
		var opts = _state(accordionDIV).options;
		var $accordion = $(accordionDIV);
		if (opts.border) {
			$rc("accordion-noborder", $accordion);
		} else {
			$ac("accordion-noborder", $accordion);
		}
	}

	function _initSelected(accordionDIV) {
		var array$panel = _state(accordionDIV).panels;

		// select panel by options.selected property
		for ( var i = 0; i < array$panel.length; i++) {
			if (array$panel[i].panel("options").selected) {
				_select(i);
				return;
			}
		}

		// selected the first item panel
		if (array$panel.length) {
			_select(0);
		}

		/**
		 * select panel without animate
		 */
		function _select(index) {
			var opts = _state(accordionDIV).options;
			var animate = opts.animate;
			opts.animate = false;
			select(accordionDIV, index);
			opts.animate = animate;
		}

	}

	function _stopPanel(accordionDIV) {
		var array$panel = _state(accordionDIV).panels;
		for ( var i = 0; i < array$panel.length; i++) {
			array$panel[i].stop(true, true);
		}
	}

	/**
	 * Resize accordion width and height
	 */
	function resize(accordionDIV) {
		var opts = _state(accordionDIV).options;
		var array$panel = _state(accordionDIV).panels;
		var $accordion = $(accordionDIV);

		// fit accordion width/height to parent
		opts.fit ? $.extend(opts, $accordion._fit()) : $accordion._fit(false);

		// set accordion width
		if (opts.width > 0) {
			$accordion.ow(opts.width);
		}

		// set accordion height and evaluate panel height
		var panelHeight = "auto";
		var panelHeaderHeight = null;
		if ($.type(opts.height) == "string" && opts.height.endsWith("%")) {
			var $parent = $p($accordion);
			if ($parent[0] == document.body)
				$parent = $(window);
			panelHeight = parseInt($parent.oh() * parseInt(opts.height) / 100);
		}
		if (opts.height > 0) {
			$accordion.oh(opts.height);
			panelHeaderHeight = array$panel.length ? array$panel[0].panel("header").css("height", "").oh() : "auto";
			panelHeight = $h($accordion) - (array$panel.length - 1) * panelHeaderHeight;
		}

		// set every panel size
		for ( var i = 0; i < array$panel.length; i++) {
			var $panel = array$panel[i];
			var $panelHeader = $panel.panel("header");
			$panelHeader.oh(panelHeaderHeight);
			$panel.panel("resize", {
				width : $w($accordion),
				height : panelHeight
			});
		}
	}

	function getSelected(accordionDIV) {
		var array$panel = _state(accordionDIV).panels;
		for ( var i = 0; i < array$panel.length; i++) {
			var $panel = array$panel[i];
			if ($panel.panel("options").collapsed == false) {
				return $panel;
			}
		}
		return null;
	}

	function getPanelIndex(accordionDIV, panelDIV) {
		var array$panel = _state(accordionDIV).panels;
		for ( var i = 0; i < array$panel.length; i++) {
			if (array$panel[i][0] == $(panelDIV)[0]) {
				return i;
			}
		}
		return -1;
	}

	function getPanel(accordionDIV, indexOrTitle, remove) {
		var array$panel = _state(accordionDIV).panels;
		if (typeof indexOrTitle == "number") {
			if (indexOrTitle < 0 || indexOrTitle >= array$panel.length) {
				return null;
			} else {
				var $panel = array$panel[indexOrTitle];
				if (remove) {
					array$panel.splice(indexOrTitle, 1);
				}
				return $panel;
			}
		}
		for ( var i = 0; i < array$panel.length; i++) {
			var $panel = array$panel[i];
			if ($panel.panel("options").title == indexOrTitle) {
				if (remove) {
					array$panel.splice(i, 1);
				}
				return $panel;
			}
		}
		return null;
	}

	function select(accordionDIV, indexOrTitle) {
		var $panel = getPanel(accordionDIV, indexOrTitle);
		if (!$panel) {
			return;
		}

		// get old selected panel
		var $oldSelectedPanel = getSelected(accordionDIV);
		if ($oldSelectedPanel && $oldSelectedPanel[0] == $panel[0]) {
			return;
		}

		// fire click on header
		$panel.panel("header").triggerHandler("click");
	}

	function add(accordionDIV, panelOptions) {
		var opts = _state(accordionDIV).options;
		var array$panel = _state(accordionDIV).panels;
		if (panelOptions.selected == undefined) {
			panelOptions.selected = true;
		}
		_stopPanel(accordionDIV);
		var $panel = $("<div></div>").appendTo(accordionDIV);
		array$panel.push($panel);
		_initPanel(accordionDIV, $panel, panelOptions);
		resize(accordionDIV);
		opts.onAdd.call(accordionDIV, panelOptions.title, array$panel.length - 1);
		if (panelOptions.selected) {
			select(accordionDIV, array$panel.length - 1);
		}
	}

	function remove(accordionDIV, indexOrTitle) {
		var opts = _state(accordionDIV).options;
		var array$panels = _state(accordionDIV).panels;
		_stopPanel(accordionDIV);
		var $panel = getPanel(accordionDIV, indexOrTitle);
		var title = $panel.panel("options").title;
		var index = getPanelIndex(accordionDIV, $panel);
		if (opts.onBeforeRemove.call(accordionDIV, title, index) == false) {
			return;
		}
		var $panel = getPanel(accordionDIV, indexOrTitle, true);
		if ($panel) {
			$panel.panel("destroy");
			if (array$panels.length) {
				resize(accordionDIV);
				var selected = getSelected(accordionDIV);
				if (!selected) {
					select(accordionDIV, 0);
				}
			}
		}
		opts.onRemove.call(accordionDIV, title, index);
	}

	$.fn.accordion = function(options, args) {
		if (typeof options == "string") {
			var fn = methods[options]
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.accordion');
		}
		options = options || {};
		return this.each(function() {
			var state = _state(this);
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.opts = opts;
			} else {
				opts = $.extend({}, defaults, parseOptions(this), options);
				var r = _init(this);
				_state(this, {
					options : opts,
					accordion : r.accordion,
					panels : r.panels
				});
			}
			_initEvent(this);
			_initStyle(this);
			resize(this);
			_initSelected(this);
		});
	};

	var methods = {
		/**
		 * Get accordion options.
		 * <p>
		 * args: none
		 */
		options : function(jq) {
			return _state(jq[0]).options;
		},
		/**
		 * Get panel array in accordion which element is panel object.
		 * <p>
		 * args: none
		 */
		panels : function(jq) {
			return _state(jq[0]).panels;
		},
		/**
		 * Resize accordion height and width
		 * <p>
		 * args: none
		 */
		resize : $X(resize),
		/**
		 * Get selected panel in accordion
		 * <p>
		 * args: none
		 */
		getSelected : $x(getSelected),
		/**
		 * Get panel object in accordion specified by title or index
		 * <p>
		 * args: titleOrIndex used to describe panel title or index
		 */
		getPanel : $x(getPanel),
		/**
		 * Get panel index in accordion specified by panel "DIV" element
		 * <p>
		 * args: panelDIV used to describe panel "DIV" element
		 */
		getPanelIndex : $x(getPanelIndex),
		/**
		 * Select panel in accordion specified by title or index
		 * <p>
		 * args: titleOrIndex used to describe panel title or index
		 */
		select : $X(select),
		/**
		 * Add panel into accordion with panel options JSON data
		 * <p>
		 * args: panelOptions used to describe panel data
		 */
		add : $X(add),
		/**
		 * Remove panel from accordion specified by title or index
		 * <p>
		 * args: titleOrIndex used to describe panel title or index
		 */
		remove : $X(remove)
	};

	var parseOptions = function(accordionDIV) {
		return $.extend({}, jCocit.parseOptions(accordionDIV, [ "width", "height", {
			fit : "b",
			border : "b",
			animate : "b"
		} ]));
	};

	var defaults = {
		width : "auto",
		/**
		 * height is percentage means that the height will be auto evaluated by window size.
		 */
		height : "auto",
		fit : false,
		/**
		 * border is true means that the accordion has border.
		 */
		border : true,
		/**
		 * animate is true means that the panel of accordion will be expanded/collapsed with animation
		 */
		animate : true,
		/**
		 * resizeOnWindow is true means that the accordion will be resized when browser window size changed.
		 */
		resizeOnWindow : true,
		/**
		 * This call-back function will be invoked after panel be expanded.
		 * <p>
		 * args: panelTitle, panelIndex
		 */
		onSelect : $n,
		/**
		 * This call-back function will be invoked after panel be added.
		 * <p>
		 * args: panelTitle, panelIndex
		 */
		onAdd : $n,
		/**
		 * This call-back function will be invoked before panel will be removed.
		 * <p>
		 * args: panelTitle, panelIndex
		 */
		onBeforeRemove : $n,
		/**
		 * This call-back function will be invoked after panel be removed.
		 * <P>
		 * args: panelTitle, panelIndex
		 */
		onRemove : $n
	};
})(jQuery, jCocit);
