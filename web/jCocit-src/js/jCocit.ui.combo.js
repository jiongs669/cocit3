/**
 * This is combo UI plugin, used to convert the target "SELECT/INPUT/TEXTAREA" box to combo UI plugin.
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: panel, validatebox(TODO, Optional)
 * <LI>ReferencedBy: none
 * <LI>SubClass: combotree, combobox(TODO)
 * <LI>SuperClass: none
 * </UL>
 * 
 * <pre>
 *  [div class='Cb']
 * 		[div class='CbB']
 * 			[input type='text' class='CbT Cb-f' /]
 * 			[input type='text' class='CbA' readonly /]
 * 		[/div]
 * 		[div class='Pn CbPn']
 * 			[div class='PnB CbPnB']
 * 				[div class='CbPnBC PnBC'] Here is combo content. [/div]
 * 			[/div]
 * 		[/div]
 * 		[input class='CbV' name='{field}' type='hidden' value='' /]
 * 	[/div]
 * 	[div class='validatebox-tip']...[/div]
 * </pre>
 * 
 * <p>
 * <B>Notes:</B>
 * <UL>
 * <LI>selfHTML: may be "combo-text(default)", "combo", "combo-box".
 * <LI>Cb: this is combo root "DIV".
 * <LI>CbB: this is combo box without drop-down panel.
 * <LI>CbT: this is "text" field. it will NOT be submit to remote server.
 * <LI>Cb-f:
 * <LI>CbA: this is drop-down button arrow, this combo drop-down panel will be shown if click it.
 * <LI>CbAD: this is drop-down button arrow disabled status.
 * <LI>CbV: this is "value" field. it will be submit to remote server.
 * <LI>CbPn: this is combo drop-down panel.
 * <LI>CbPnB: this is combo panel body.
 * <LI>CbPnBC: this is combo panel body content.
 * <LI>validatebox-tip: this is tip box of "validatebox-text" element.
 * </UL>
 */
(function($, jCocit) {

	/**
	 * validatebox plugin is optional for combo
	 */
	var validatebox = $.fn.validatebox || false;

	function _init(selfHTML) {
		var $target = $(selfHTML);
		var $combo = $target;

		// the target HTML is "div" element
		if ($target.is("div")) {
			$ac("Cb", $target);
			$ac("CbB", $f(".combo-box", $combo));
			$ac("CbT", $f(".combo-text", $combo));
			$ac("CbA", $f(".combo-arrow", $combo));
			$ac("CbV", $f(".combo-value", $combo));
			$ac("CbPnBC", $f(".combo-content", $combo));
		} else {
			$combo = $('<div class="Cb"><div class="CbB"><input type="text" class="CbT" /><input type="text" class="CbA" /></div><input type="hidden" class="CbV" /></div>').insertAfter(selfHTML);
			$ac("combo-f", $target).prependTo($f(".CbB", $combo)).hide();
		}

		// set "name" attribute of the target "INPUT" to "combo-value" field.
		var name = $target.attr("name");
		if (name) {
			$f(".CbV", $combo).attr("name", name);
			$target.removeAttr("name").attr("comboName", name);
		}
		if ($target.is("input") || $target.is("textarea")) {
			$f(".CbT", $combo).remove();
			$ac("CbT", $target).show();
		}

		$f(".CbT", $combo).attr("autocomplete", "off");

		return $combo;
	}

	function resize(selfHTML, width) {
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $combo = state.combo;

		if (width)
			opts.width = width;

		// auto get width;
		if (isNaN(opts.width)) {
			var c = $(selfHTML).clone();
			c.css("visibility", "hidden");
			c.appendTo("body");
			opts.width = $ow(c);
			c.remove();
		}

		var $comboBox = $f(".CbB", $combo);
		var $comboText = $f(".CbT", $combo);
		var $comboArrow = $f(".CbA", $combo);
		var arrowWidth = opts.hasDownArrow ? $comboArrow.ow() : 0;

		$combo.ow(opts.width).oh(opts.height);
		$comboBox.ow(opts.width).oh(opts.height - 1);
		$comboText.ow($w($combo) - arrowWidth - 1).oh($h($comboBox) - 1);
		// $comboArrow.oh($h($combo));

		if (state.panel) {
			var $comboContent = state.panel;
			$comboContent.panel("resize", {
				width : (opts.panelWidth ? opts.panelWidth : $ow($combo)),
				height : opts.panelMinHeight
			});
		}
	}

	function _initDropDownButton(selfHTML) {
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $combo = state.combo;

		if (opts.hasDownArrow) {
			$f(".CbA", $combo).show();
		} else {
			$f(".CbA", $combo).hide();
		}
	}

	function destroy(selfHTML) {
		var state = $d(selfHTML, "combo");
		var $comboText = $f(".CbT", state.combo);

		if (validatebox)
			$comboText.validatebox("destroy");
		if (state.panel)
			state.panel.panel("destroy");
		state.combo.remove();
		$(selfHTML).remove();
	}

	function _bindComboEvnets(selfHTML) {
		var $target = $(selfHTML);
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $combo = state.combo;
		var $comboText = $f(".CbT", $combo);
		var $comboArrow = $f(".CbA", $combo);

		$(document).unbind(".combo").bind("mousedown.combo", function(e) {
			// mouse down on the current combo or panel
			var $comboOrPanel = $l("div.combo,div.CbPnBC", $(e.target));
			if ($comboOrPanel.length) {
				return;
			}

			// close all combo panel
			var $comboPanel = $("div.CbPn div.CbPnBC");
			$comboPanel.panel("close");
		});

		$combo.unbind(".combo");
		if (state.panel)
			state.panel.unbind(".combo");
		$comboText.unbind(".combo");
		$comboArrow.unbind(".combo");

		if (!opts.disabled) {
			var keyHandler = function(e) {
				var keyCode = jCocit.keyCode;
				switch (e.keyCode) {
				case keyCode.UP:// UP key
				case keyCode.DOWN:// DOWN key
				case keyCode.PGUP:// PgUp key
				case keyCode.PGDN:// PgDn key
					if (!state.panel || !state.panel.is(":visible"))
						$target.combo("showPanel");

					opts.keyHandler.doKey.call(selfHTML, e.keyCode);
					e.preventDefault();
					break;
				case keyCode.ENTER:// Enter Key
					opts.keyHandler.doEnter.call(selfHTML);
					e.preventDefault();

					return false;
				case keyCode.TAB:
				case keyCode.ESC:// ESC
					hidePanel(selfHTML);
					break;
				default:// other Key to query
					if (opts.editable) {
						if (state.timer) {
							clearTimeout(state.timer);
						}
						state.timer = setTimeout(function() {
							var text = $comboText.val();
							if (state.previousValue != text) {
								state.previousValue = text;
								$target.combo("showPanel");
								opts.keyHandler.doQuery.call(selfHTML, $comboText.val());
								validate(selfHTML, true);
							}
						}, opts.delay);
					}
				}
			}
			$comboText.bind("mousedown.combo", function(e) {

				// mouse down the current combo text, close other panels
				if (state.panel)
					$("div.CbPnBC").not(state.panel).panel("close");

				e.stopPropagation();

			})//.bind("keydown.combo", keyHandler)//
			.bind("keyup.combo", keyHandler);

			// click down arrow toggle combo panel
			$comboArrow.bind("click.combo", function() {
				if (state.panel && state.panel.is(":visible")) {
					hidePanel(selfHTML);
				} else {
					if (state.panel)
						state.panel.panel("close");
					$target.combo("showPanel");
				}
				$comboText.focus();
			}).bind("mouseenter.combo", function() {
				$ac("CbAH", $(this));
			}).bind("mouseleave.combo", function() {
				$rc("CbAH", $(this));
			}).bind("mousedown.combo", function() {
			});

		}
	}

	function showPanel(selfHTML) {
		var $target = $(selfHTML);
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $combo = state.combo;

		if (opts.onBeforeShowPanel.call(selfHTML) == false)
			return;

		if (!state.panel)
			_initPanel(selfHTML);

		var $comboContent = state.panel;

		if ($.fn.window)
			$comboContent.panel("panel").css("z-index", $.fn.window.defaults.zIndex++);

		if (!opts.panelInline)
			$comboContent.panel("move", {
				left : $combo.offset().left,
				top : _evalPanelTop()
			});

		if ($comboContent.panel("options").closed) {
			$comboContent.panel("open");

			resizePanel(selfHTML);

			opts.onShowPanel.call(selfHTML);
		}

		if (!opts.panelInline)
			(function() {
				if ($comboContent.is(":visible")) {
					$comboContent.panel("move", {
						left : _evalPanelLeft(),
						top : _evalPanelTop()
					});
					setTimeout(arguments.callee, 50);
				}
			})();

		function _evalPanelLeft() {
			var left = $combo.offset().left;
			if (left + $comboContent.ow() > $(window).ow() + $(document).scrollLeft()) {
				left = $(window).ow() + $(document).scrollLeft() - $comboContent.ow();
			}
			if (left < 0) {
				left = 0;
			}
			return parseInt(left);
		}

		function _evalPanelTop() {
			var top = $combo.offset().top + $combo.oh();
			if (top + $comboContent.oh() > $(window).oh() + $(document).scrollTop()) {
				top = $combo.offset().top - $comboContent.oh();
			}
			if (top < $(document).scrollTop()) {
				top = $combo.offset().top + $combo.oh();
			}
			return parseInt(top) - 1;
		}
	}

	function _initPanel(selfHTML) {
		var $target = $(selfHTML);
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $combo = state.combo;

		var $comboContent = $f(".CbPnBC", $target);
		if (!$comboContent.length)
			$comboContent = $('<div class="CbPnBC"></div>').appendTo(opts.panelInline ? $combo : "body");
		var panelState = $d($comboContent[0], "panel");
		if (!panelState || !panelState.panel)
			$comboContent.panel({
				doSize : false,
				closed : true,
				border : false,
				url : opts.panelUrl,
				styleName : "CbPn",
				bodyCls : "CbPnB",
				style : {
					position : "absolute",
					zIndex : 10
				},
				onOpen : function() {
					$(this).panel("resize");
				}
			});
		state.panel = $comboContent;
		resizePanel(selfHTML);

		opts.onInitPanel.call(selfHTML);

		return $comboContent;
	}

	/**
	 * Auto adjust the combo drop-down panel height
	 */
	function resizePanel(selfHTML, height) {
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $comboContent = state.panel;

		// auto eval drop-down panel height
		if (!height) {
			if (opts.panelHeight) {
				height = opts.panelHeight;
			} else {
				var $cc = $c($comboContent);
				var comboContentHeight = $cc.length == 1 ? $cc.oh() + 5 : $comboContent.oh();
				height = Math.min(comboContentHeight, opts.panelMaxHeight);
				height = Math.max(height, opts.panelMinHeight);
			}
		}
		$comboContent.panel("resize", {
			width : (opts.panelWidth ? opts.panelWidth : $ow(state.combo)),
			height : height
		});
	}

	function hidePanel(selfHTML) {
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $comboContent = state.panel;

		$comboContent.panel("close");
		opts.onHidePanel.call(selfHTML);
	}

	function validate(selfHTML, validate) {
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $comboText = $f(".CbT", state.combo);

		if (validatebox) {
			$comboText.validatebox(opts);
			if (validate) {
				$comboText.validatebox("validate");
			}
		}
	}

	function disable(selfHTML, disabled) {
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $combo = state.combo;

		if (disabled) {
			opts.disabled = true;
			$(selfHTML).attr("disabled", true);
			$f(".CbV", $combo).attr("disabled", true);
			$f(".CbT", $combo).attr("disabled", true);
			$f(".CbA", $combo).attr("disabled", true);
			$ac("CbAD", $f(".CbA", $combo));
		} else {
			opts.disabled = false;
			$(selfHTML).removeAttr("disabled");
			$f(".CbV", $combo).removeAttr("disabled");
			$f(".CbT", $combo).removeAttr("disabled");
			$f(".CbA", $combo).removeAttr("disabled");
			$rc("CbAD", $f(".CbA", $combo));
		}
	}

	function clear(selfHTML) {
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var $combo = state.combo;

		if (opts.multiple) {
			$f(".CbV", $combo).remove();
		} else {
			$f(".CbV", $combo).val("");
		}
		$f(".CbT", $combo).val("");
	}

	function getText(selfHTML) {
		var $combo = $d(selfHTML, "combo").combo;
		return $f(".CbT", $combo).val();
	}

	function setText(selfHTML, text) {
		var state = $d(selfHTML, "combo");
		var $combo = state.combo;
		$f(".CbT", $combo).val(text);
		validate(selfHTML, true);
		state.previousValue = text;
	}

	function getValues(selfHTML) {
		var values = [];
		var $combo = $d(selfHTML, "combo").combo;
		$f(".CbV", $combo).each(function() {
			values.push($(this).val());
		});
		return values;
	}

	function setValues(selfHTML, newValues) {
		var state = $d(selfHTML, "combo");
		var opts = state.options;
		var oldValues = getValues(selfHTML);
		var $combo = state.combo;
		$f(".CbV", $combo).remove();
		var comboName = $(selfHTML).attr("comboName");
		for ( var i = 0; i < newValues.length; i++) {
			var $comboValue = $('<input type="hidden" class="CbV">').appendTo($combo);
			if (comboName) {
				$comboValue.attr("name", comboName);
			}
			$comboValue.val(newValues[i]);
		}
		var tmp = [];
		for ( var i = 0; i < oldValues.length; i++) {
			tmp[i] = oldValues[i];
		}
		var sameValues = [];
		for ( var i = 0; i < newValues.length; i++) {
			for ( var j = 0; j < tmp.length; j++) {
				if (newValues[i] == tmp[j]) {
					sameValues.push(newValues[i]);
					tmp.splice(j, 1);
					break;
				}
			}
		}
		if (sameValues.length != newValues.length || newValues.length != oldValues.length) {
			if (opts.multiple) {
				opts.onChange.call(selfHTML, newValues, oldValues);
			} else {
				opts.onChange.call(selfHTML, newValues[0], oldValues[0]);
			}
		}
	}

	function getValue(selfHTML) {
		var values = getValues(selfHTML);
		return values.length > 0 ? values[0] : null;
	}

	function setValue(selfHTML, value) {
		setValues(selfHTML, [ value ]);
	}

	function _initOriginalValue(selfHTML) {
		var opts = $d(selfHTML, "combo").options;
		var fn = opts.onChange;
		opts.onChange = function() {
		};
		if (opts.multiple) {
			if (opts.value) {
				if (typeof opts.value == "object") {
					setValues(selfHTML, opts.value);
				} else {
					setValue(selfHTML, opts.value);
				}
			} else {
				setValues(selfHTML, []);
			}
			opts.originalValue = getValues(selfHTML);
		} else {
			setValue(selfHTML, opts.value);
			opts.originalValue = opts.value;
		}
		if (opts.text)
			setText(selfHTML, opts.text);

		opts.onChange = fn;
	}

	/**
	 * 1. Create combo UI object or set combo properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke combo method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.combo = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.combo.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.combo');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "combo");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $d(this, "combo", {
					options : $.extend({}, $.fn.combo.defaults, $.fn.combo.parseOptions(this), options),
					combo : _init(this),
					previousValue : null
				});
				$(this).removeAttr("disabled");
			}
			$(".CbT", state.combo).attr("readonly", !state.options.editable);
			_initDropDownButton(this);
			disable(this, state.options.disabled);
			resize(this);
			_bindComboEvnets(this);
			validate(this);
			_initOriginalValue(this);
		});
	};

	$.fn.combo.methods = {
		options : function(jq) {
			return $d(jq[0], "combo").options;
		},
		panel : function(jq) {
			var $panel = $d(jq[0], "combo").panel;
			if (!$panel)
				$panel = _initPanel(jq[0]);
			return $panel;
		},
		textbox : function(jq) {
			return $f(".CbT", $d(jq[0], "combo").combo);
		},
		destroy : $X(destroy),
		resize : $X(resize),
		resizePanel : $X(resizePanel),
		showPanel : $X(showPanel),
		hidePanel : $X(hidePanel),
		disable : function(jq) {
			return jq.each(function() {
				disable(this, true);
				_bindComboEvnets(this);
			});
		},
		enable : function(jq) {
			return jq.each(function() {
				disable(this, false);
				_bindComboEvnets(this);
			});
		},
		validate : $X(validate),
		isValid : function(jq) {
			var $comboText = $f(".CbT", $d(jq[0], "combo").combo);
			return validatebox ? $comboText.validatebox("isValid") : true;
		},
		clear : $X(clear),
		reset : function(jq) {
			return jq.each(function() {
				var opts = $d(this, "combo").options;
				if (opts.multiple) {
					$(this).combo("setValues", opts.originalValue);
				} else {
					$(this).combo("setValue", opts.originalValue);
				}
			});
		},
		getText : $x(getText),
		setText : $X(setText),
		getValues : $x(getValues),
		setValues : $X(setValues),
		getValue : $x(getValue),
		setValue : $X(setValue)
	};

	$.fn.combo.parseOptions = function(selfHTML) {
		var $target = $(selfHTML);
		return $.extend({}, validatebox ? validatebox.parseOptions(selfHTML) : {}, jCocit.parseOptions(selfHTML, [ "width", "height", "text", "separator", {
			panelWidth : "n",
			panelHeight : "s",
			panelMaxHeight : "n",
			panelMinHeight : "n",
			editable : "b",
			hasDownArrow : "b",
			delay : "n"
		} ]), {
			multiple : ($target.attr("multiple") ? true : undefined),
			disabled : ($target.attr("disabled") ? true : undefined),
			value : ($target.val() || undefined)
		});
	};

	$.fn.combo.defaults = $.extend({}, validatebox ? validatebox.defaults : {}, {
		width : "auto",
		height : 22,
		panelWidth : null,
		panelHeight : null,// this is drop-down panel fixed height
		panelMaxHeight : 400,// this is drop-down panel max height
		panelMinHeight : 200,// this is drop-down panel min height
		panelUrl : null,
		/**
		 * drop-down list is inline means that the panel cannot be append to "body".
		 * <p>
		 * true means that the panel can render in combo.
		 */
		panelInline : true,
		multiple : false,
		separator : ",",// used to join multiple text to display.
		editable : true,
		disabled : false,
		hasDownArrow : true,
		value : "",
		text : "",
		delay : 50,// delay to show combo panel
		keyHandler : {
			doKey : $n,
			doEnter : $n,
			// args: keyword
			doSearch : $n
		},
		onInitPanel : $n,
		onShowPanel : $n,
		onBeforeShowPanel : $n,
		onHidePanel : $n,
		/**
		 * args: (newValue, oldValue)
		 */
		onChange : $n
	});
})(jQuery, jCocit);
