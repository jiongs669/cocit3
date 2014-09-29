/**
 * 
 * This is combobox UI plugin, used to convert the target "SELECT/INPUT" box to combobox UI plugin.
 * <p>
 * This UI plugin extends from "combo" and add "combobox-item" to panel body drop-down list.
 * 
 * <P>
 * <B>Codes Specification:</B>
 * <UL>
 * <LI>Variable is "target" means that it is the current HTML element wrapped to jQuery UI plug-in.
 * <LI>Variable starts with "$" means that it is a jQuery object.
 * <LI>Variable ends with upper case HTML tag name means that it is a HTML element.
 * <LI>Variable ends with "Array" means that it is a Array.
 * <LI>Function starts with "_" means that it is an inner function, it cannot be invoked by outer JS codes.
 * </UL>
 * 
 * <P>
 * <B>Dependencies:</B> panel, validatebox, combo
 * 
 * <pre>
 * 	[select class='combo-f'][/select]
 *  [span class='combo']
 * 		[input class='combo-text validatebox-text' type='text'/]
 * 		[span][span class='combo-arrow'][/span][/span]
 * 		[input class='combo-value' name='fieldname' type='hidden' value='' /]
 * 	[/span]
 * 	[div class='panel combo-p']
 * 		[div class='combo-panel panel-body']
 * 			[div class='combobox-item' value='v1']s1[/div]
 * 			[div class='combobox-item' value='v2']s2[/div]
 * 			[div class='combobox-item' value='v3']s3[/div]
 * 			...
 * 		[/div]
 * 	[/div]
 * 	[div class='validatebox-tip']...[/div]
 * </pre>
 * 
 * <p>
 * <B>Notes:</B>
 * <UL>
 * <LI>combo-f: this is target "SELECT/INPUT" element.
 * <LI>combo: this is combo span.
 * <LI>combo-text: this is "text" field. it will NOT be submit when submitting "FORM"
 * <LI>combo-arrow: this is drop-down button, this combo drop-down panel will be shown when clicking it.
 * <LI>combo-value: this is hidden "value" field. it will be submit when submitting "FORM".
 * <LI>combo-p: this is combo drop-down panel.
 * <LI>combobox-item: this is drop-down list item.
 * <LI>validatebox-tip: this is tip box of "validatebox-text" element.
 * </UL>
 */
(function($, jCocit) {

	function _init(target) {
		var $target = $(target);
		var state = $d(target, "combobox");
		var opts = state.options;
		$ac("combobox-f", $target);
		$target.combo($.extend({}, opts, {
			onInitPanel : function() {
				if (!opts.url && opts.data)
					loadDropDownData(this, state.options.data);

				reloadDropDownData(this);
			},
			onShowPanel : function() {
				// cancel keywords query
				$f("div.combobox-item", $target.combo("panel")).show();
				_scrollToItem(target, $target.combobox("getValue"));
				opts.onShowPanel.call(target);
			}
		}));
	}

	function _scrollToItem(target, value) {
		var $panelContent = $(target).combo("panel");
		var $matchItem = $f('div.combobox-item[value="' + value + '"]', $panelContent);
		if ($matchItem.length) {
			if ($matchItem.position().top <= 0) {
				var h = $panelContent.scrollTop() + $matchItem.position().top;
				$panelContent.scrollTop(h);
			} else {
				if ($matchItem.position().top + $oh($matchItem) > $h($panelContent)) {
					var h = $panelContent.scrollTop() + $matchItem.position().top + $oh($matchItem) - $h($panelContent);
					$panelContent.scrollTop(h);
				}
			}
		}
	}

	/**
	 * process key: down/up
	 */
	function _doKey(target, key) {
		var $target = $(target);
		var $panelContent = $target.combo("panel");
		var values = $target.combo("getValues");
		var $matchItem = $f('div.combobox-item[value="' + values.pop() + '"]', $panelContent);

		if (key == jCocit.keyCode.DOWN) {
			if ($matchItem.length) {
				var $nextItem = $matchItem.next(":visible");
				if ($nextItem.length)
					$matchItem = $nextItem;
			} else
				$matchItem = $f("div.combobox-item:visible:first", $panelContent);
		} else if (key == jCocit.keyCode.UP) {
			if ($matchItem.length) {
				var $prevItem = $matchItem.prev(":visible");
				if ($prevItem.length)
					$matchItem = $prevItem;
			} else
				$matchItem = $f("div.combobox-item:visible:last", $panelContent);
		}

		var value = $matchItem.attr("value");
		selectItem(target, value);
		_scrollToItem(target, value);
	}

	function _doQuery(target, keywords) {
		var opts = $d(target, "combobox").options;

		if (opts.multiple && !keywords) {
			setValues(target, [], true);
		} else {
			setValues(target, [ keywords ], true);
		}

		if (opts.mode == "remote") {
			reloadDropDownData(target, null, {
				keywords : keywords
			}, true);
		} else {
			var $panelContent = $(target).combo("panel");

			$f("div.combobox-item", $panelContent).hide();
			var dataRows = $d(target, "combobox").data;
			for ( var i = 0; i < dataRows.length; i++) {
				if (opts.filter.call(target, keywords, dataRows[i])) {
					var v = dataRows[i][opts.valueField];
					var s = dataRows[i][opts.textField];
					var $matchItem = $f('div.combobox-item[value="' + v + '"]', $panelContent);
					$matchItem.show();
					if (s == keywords) {
						setValues(target, [ v ], true);
						$ac("combobox-item-selected", $matchItem);
					}
				}
			}
		}
	}

	function selectItem(target, value) {
		var state = $d(target, "combobox");
		var opts = state.options;
		var dataRows = state.data;
		if (opts.multiple) {
			var values = $(target).combo("getValues");
			for ( var i = 0; i < values.length; i++) {
				if (values[i] == value) {
					return;
				}
			}
			values.push(value);
			setValues(target, values);
		} else {
			setValues(target, [ value ]);
		}
		for ( var i = 0; i < dataRows.length; i++) {
			if (dataRows[i][opts.valueField] == value) {
				opts.onSelect.call(target, dataRows[i]);
				return;
			}
		}
	}

	function unselectItem(target, value) {
		var state = $d(target, "combobox");
		var opts = state.options;
		var dataRows = state.data;
		var values = $(target).combo("getValues");
		for ( var i = 0; i < values.length; i++) {
			if (values[i] == value) {
				values.splice(i, 1);
				setValues(target, values);
				break;
			}
		}
		for ( var i = 0; i < dataRows.length; i++) {
			if (dataRows[i][opts.valueField] == value) {
				opts.onUnselect.call(target, dataRows[i]);
				return;
			}
		}
	}

	function setValues(target, values, isQuery) {
		var $target = $(target);
		var state = $d(target, "combobox");
		var opts = state.options;
		var dataRows = state.data || [];
		var $panelContent = $(target).combo("panel");
		$rc("combobox-item-selected", $f("div.combobox-item-selected", $panelContent));
		var vv = [], ss = [];
		for ( var i = 0; i < values.length; i++) {
			var v = values[i];
			var s = v;
			for ( var j = 0; j < dataRows.length; j++) {
				if (dataRows[j][opts.valueField] == v) {
					s = dataRows[j][opts.textField];
					break;
				}
			}
			vv.push(v);
			ss.push(s);
			$ac("combobox-item-selected", $f('div.combobox-item[value="' + v + '"]', $panelContent));
		}
		$target.combo("setValues", vv);
		if (!isQuery) {
			$target.combo("setText", ss.join(opts.separator));
		}
	}

	/**
	 * Parse drop-down list items from "SELECT" options.
	 */
	function _parseDropDownData(target) {
		var opts = $d(target, "combobox").options;
		var dataRows = [];
		$(">option", target).each(function() {
			var item = {};
			item[opts.valueField] = $(this).attr("value") != undefined ? $(this).attr("value") : $(this).html();
			item[opts.textField] = $(this).html();
			item["selected"] = $(this).attr("selected");
			dataRows.push(item);
		});
		return dataRows;
	}

	function loadDropDownData(target, dataRows, isQuery) {
		var $target = $(target);
		var opts = $d(target, "combobox").options;
		var $panelContent = $target.combo("panel");
		$d(target, "combobox").data = dataRows;
		var values = $target.combobox("getValues");
		$panelContent.empty();
		for ( var i = 0; i < dataRows.length; i++) {
			var v = dataRows[i][opts.valueField];
			var s = dataRows[i][opts.textField];
			var $item = $('<div class="combobox-item"></div>').appendTo($panelContent);
			$item.attr("value", v);
			if (opts.formatter) {
				$item.html(opts.formatter.call(target, dataRows[i]));
			} else {
				$item.html(s);
			}
			if (dataRows[i]["selected"]) {
				(function() {
					for ( var i = 0; i < values.length; i++) {
						if (v == values[i]) {
							return;
						}
					}
					values.push(v);
				})();
			}
		}
		if (opts.multiple) {
			setValues(target, values, isQuery);
		} else {
			if (values.length) {
				setValues(target, [ values[values.length - 1] ], isQuery);
			} else {
				setValues(target, [], isQuery);
			}
		}
		opts.onLoadSuccess.call(target, dataRows);
		$(".combobox-item", $panelContent).hover(function() {
			$ac("combobox-item-hover", $(this));
		}, function() {
			$rc("combobox-item-hover", $(this));
		}).click(function() {
			var $item = $(this);
			if (opts.multiple) {
				if ($hc("combobox-item-selected", $item)) {
					unselectItem(target, $item.attr("value"));
				} else {
					selectItem(target, $item.attr("value"));
				}
			} else {
				selectItem(target, $item.attr("value"));
				$target.combo("hidePanel");
			}
		});
	}

	function reloadDropDownData(target, url, options, isQuery) {
		var opts = $d(target, "combobox").options;
		if (url) {
			opts.url = url;
		}
		options = options || {};
		if (opts.onBeforeLoad.call(target, options) == false) {
			return;
		}
		opts.loader.call(target, options, function(dataRows) {
			loadDropDownData(target, dataRows, isQuery);
		}, function() {
			opts.onLoadError.apply(this, arguments);
		});
	}

	$.fn.combobox = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.combobox.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.combo(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "combobox");
			if (state) {
				$.extend(state.options, options);
				_init(this);
			} else {
				state = $d(this, "combobox", {
					options : $.extend({}, $.fn.combobox.defaults, $.fn.combobox.parseOptions(this), options)
				});
				_init(this);
				if (!state.options.data)
					state.options.data = _parseDropDownData(this);
			}
		});
	};

	$.fn.combobox.methods = {
		options : function(jq) {
			var opts = $d(jq[0], "combobox").options;
			opts.originalValue = jq.combo("options").originalValue;
			return opts;
		},
		getData : function(jq) {
			return $d(jq[0], "combobox").data;
		},
		/**
		 * args: values -
		 */
		setValues : $X(setValues),
		setValue : function(jq, value) {
			return jq.each(function() {
				setValues(this, [ value ]);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				$(this).combo("clear");
				var $panelContent = $(this).combo("panel");
				$rc("combobox-item-selected", $f("div.combobox-item-selected", $panelContent));
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).combobox("options");
				if (opts.multiple) {
					$(this).combobox("setValues", opts.originalValue);
				} else {
					$(this).combobox("setValue", opts.originalValue);
				}
			});
		},
		/**
		 * args: dataRows -
		 */
		loadData : $X(loadDropDownData),
		/**
		 * args: url -
		 */
		reload : $X(reloadDropDownData),
		/**
		 * args: value -
		 */
		select : $X(selectItem),
		/**
		 * args: value -
		 */
		unselect : $X(unselectItem)
	};

	$.fn.combobox.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.combo.parseOptions(target), jCocit.parseOptions(target, [ "valueField", "textField", "mode", "method", "url" ]));
	};

	$.fn.combobox.defaults = $.extend({}, $.fn.combo.defaults, {
		valueField : "value",
		textField : "text",
		mode : "local",
		method : "post",
		url : null,
		data : null,
		keyHandler : {
			doKey : function(key) {
				_doKey(this, key);
			},
			doEnter : function() {
				var values = $(this).combobox("getValues");
				$(this).combobox("setValues", values);
				$(this).combobox("hidePanel");
			},
			doQuery : function(keywords) {
				_doQuery(this, keywords);
			}
		},
		filter : function(keywords, row) {
			var opts = $(this).combobox("options");
			return row[opts.textField].toLowerCase().indexOf(keywords.toLowerCase()) >= 0;
		},
		formatter : function(row) {
			var opts = $(this).combobox("options");
			return row[opts.textField];
		},
		loader : function(reqData, doLoadSuccess, doLoadError) {
			var opts = $(this).combobox("options");
			if (!opts.url) {
				return false;
			}
			$.doAjax({
				type : opts.method,
				url : opts.url,
				data : reqData,
				dataType : "json",
				success : function(dataRows) {
					doLoadSuccess(dataRows);
				},
				error : function() {
					doLoadError.apply(this, arguments);
				}
			});
		},
		/**
		 * arg: options -
		 */
		onBeforeLoad : $n,
		onLoadSuccess : $n,
		onLoadError : $n,
		/**
		 * arg: jsonItem -
		 */
		onSelect : $n,
		/**
		 * arg: jsonItem -
		 */
		onUnselect : $n
	});
})(jQuery, jCocit);
