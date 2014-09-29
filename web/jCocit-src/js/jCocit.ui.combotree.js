/**
 * 
 * This is combotree UI plugin, used to convert the target "SELECT/INPUT" box to combo UI plugin.
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: panel, tree, validatebox(TODO, Optional)
 * <LI>ReferencedBy: none
 * <LI>SubClass: none
 * <LI>SuperClass: combo
 * </UL>
 * 
 * <pre>
 *  [div class='combo']
 * 		[div class='combo-box']
 * 			[input type='text' class='combo-text combo-f' comboname='fieldname' /]
 * 			[input type='text' class='combo-arrow' readonly /]
 * 		[/div]
 * 		[div class='panel combo-p']
 * 			[div class='combo-panel panel-body'][ul class='tree'][/ul][/div]
 * 		[/div]
 * 		[input class='combo-value' name='fieldname' type='hidden' value='' /]
 * 	[/div]
 * 	[div class='validatebox-tip']...[/div]
 * </pre>
 * 
 * <p>
 * <B>Notes:</B>
 * <UL>
 * <LI>combo-f: this is target "SELECT/INPUT/TEXTAREA" element.
 * <LI>combo: this is combo div.
 * <LI>combo-box:
 * <LI>combo-text: this is "text" field. it will NOT be submit when submitting "FORM"
 * <LI>combo-arrow: this is drop-down button, this combo drop-down panel will be shown when clicking it.
 * <LI>combo-value: this is hidden "value" field. it will be submit when submitting "FORM".
 * <LI>combo-p: this is combo drop-down panel.
 * <LI>validatebox-tip: this is tip box of "validatebox-text" element.
 * </UL>
 */
(function($, jCocit) {

	function _init(comboHTML) {
		var $combo = $(comboHTML);
		var state = $d(comboHTML, "combotree");
		var opts = state.options;

		$ac("combotree-f", $combo);
		$combo.combo($.extend({}, opts, {
			onInitPanel : function() {
				_initTree(comboHTML);
			},
			onShowPanel : function() {
				var oldKeywords = state.tree.tree("options").keywords;
				if (oldKeywords && oldKeywords != "")
					state.tree.tree({
						keywords : ""
					});
				_scrollToValue(comboHTML, $combo.combotree("getValue"));
				$combo.combo("resizePanel");
				opts.onShowPanel.call(comboHTML);
			}
		}));
	}

	function _initTree(comboHTML) {
		var $combo = $(comboHTML);
		var state = $d(comboHTML, "combotree");
		var opts = state.options;
		var $panel = $combo.combo("panel");
		var $tree = state.tree;
		if (!$tree) {
			$tree = $("<ul></ul>").appendTo($panel);
			state.tree = $tree;
		}
		$tree.tree($.extend({}, opts, {
			checkbox : opts.multiple,
			height : opts.panelMaxHeight - 5,
			onLoadSuccess : function(node, rows, times) {

				if (times == 1)
					$combo.combo("resizePanel");

				if (times == 2) {
					// select or check tree nodes
					if (opts.multiple) {
						var values = $combo.combotree("getValues");
						for ( var i = 0; i < values.length; i++) {
							var node = $tree.tree("find", values[i]);
							if (node)
								$tree.tree("check", {
									target : node.target,
									checked : true,
									ignoreOnCheck : false
								});
						}
					} else {
						var value = $combo.combotree("getValue");
						var node = $tree.tree("find", value);
						if (node) {
							$tree.tree("select", {
								target : node.target,
								ignoreOnSelect : true
							});
						}
					}
				}

				opts.onLoadSuccess.call(this, node, rows, times);
			},
			onDblClick : function(node) {
				$combo.combo("hidePanel");

				opts.onDblClick.call(this, node);
			},
			onSelect : function(node) {
				if (!opts.multiple)
					_doSelectTreeNode(comboHTML);

				$combo.focus();
				$combo.combo("hidePanel");

				opts.onSelect.call(this, node);
			},
			onCheck : function(node, checked) {
				_doSelectTreeNode(comboHTML);

				$combo.focus();

				opts.onCheck.call(this, node, checked);
			},
			onExpand : function(node) {
				$(comboHTML).combo("resizePanel");
				opts.onExpand.call(this, node);
			},
			onCollapse : function(node) {
				$(comboHTML).combo("resizePanel");
				opts.onCollapse.call(this, node);
			}
		}));
	}

	function _doSelectTreeNode(comboHTML) {
		var state = $d(comboHTML, "combotree");
		var opts = state.options;
		var $tree = state.tree;

		var vv = [], ss = [];
		if (opts.multiple) {
			var checkNodes = $tree.tree("getValues");
			for ( var i = 0; i < checkNodes.length; i++) {
				vv.push(checkNodes[i].id);
				ss.push(checkNodes[i].text);
			}
		} else {
			var selectedNode = $tree.tree("getValue");
			if (selectedNode) {
				vv.push(selectedNode.id);
				ss.push(selectedNode.text);
			}
		}

		$(comboHTML).combo("setValues", vv).combo("setText", ss.join(opts.separator));

		return vv;
	}

	function setValues(comboHTML, values, isQuery) {
		$log("ui.combotree:setValues: values=" + values.join(","));

		var $combo = $(comboHTML);
		var state = $d(comboHTML, "combotree");
		var opts = state.options;
		var $tree = $combo.combotree("tree");

		// uncheck all
		$tree.tree("uncheck", {
			target : null,
			checked : false,
			ignoreOnCheck : true
		});
		// unselect
		$tree.tree("select", {
			target : null,
			ignoreOnSelect : true
		});

		var vv = [], ss = [];
		for ( var i = 0; i < values.length; i++) {
			var v = values[i];
			vv.push(v);
			ss.push(v);

			var matchNode = $tree.tree("find", isQuery ? {
				field : "text",
				value : v
			} : v);
			if (matchNode) {
				if (opts.multiple) {
					$tree.tree("check", {
						target : matchNode.target,
						checked : true,
						cascadeCheck : false,
						ignoreOnCheck : true
					});
				} else {
					$tree.tree("select", {
						target : matchNode.target,
						ignoreOnSelect : true
					});
				}
			}
		}

		// cascade set combo-tree values
		if (opts.multiple) {
			var nodes = $tree.tree("getValues");
			if (nodes && nodes.length > 0) {
				vv = [];
				ss = [];
				for ( var i = 0; i < nodes.length; i++) {
					vv.push(nodes[i].id);
					ss.push(nodes[i].text);
				}
			}
		} else {
			var node = $tree.tree("getValue");
			if (node) {
				vv = [];
				ss = [];
				vv.push(node.id);
				ss.push(node.text);
			}
		}

		$log("ui.combotree:setValues: vv=" + values.join(",") + ", ss=" + ss.join(","));

		$combo.combo("setValues", vv);
		if (!isQuery)
			$combo.combo("setText", ss.join(opts.separator));

	}

	function _scrollToValue(comboHTML, value) {
		var $combo = $(comboHTML);
		var $tree = $combo.combotree("tree");

		var matchNode = $tree.tree("find", value);
		if (matchNode) {
			_scrollToNode($combo.combo("panel"), $tree, $(matchNode.target));
		}
	}

	function _scrollToNode($panelContent, $tree, $node) {
		$tree.tree("expandTo", $node[0]);

		// "22" is x-scroll bar height
		if ($node.position().top <= 0) {
			var h = $panelContent.scrollTop() + $node.position().top;
			$panelContent.scrollTop(h);
		} else {
			if ($node.position().top + $oh($node) + 22 > $h($panelContent)) {
				var h = $panelContent.scrollTop() + $node.position().top + $oh($node) + 22 - $h($panelContent);
				$panelContent.scrollTop(h);
			}
		}
	}

	function _doKey(comboHTML, key) {
		var $combo = $(comboHTML);
		var $tree = $combo.combotree("tree");

		$tree.tree("doKey", key);

		var selected = $tree.tree("getSelected");
		if (selected)
			_scrollToNode($combo.combo("panel"), $tree, $(selected.target));
	}

	function _doQuery(comboHTML, keywords) {
		var opts = $d(comboHTML, "combotree").options;

		if (opts.multiple && !keywords) {
			setValues(comboHTML, [], true);
		} else {
			setValues(comboHTML, [ keywords ], true);
		}

		$(comboHTML).combotree("tree").tree({
			keywords : keywords
		});
		$(comboHTML).combo("resizePanel");
	}

	/**
	 * 1. Create combotree UI object or set combotree properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke combotree method or combo method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.combotree = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.combotree.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.combo(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "combotree");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "combotree", {
					options : $.extend({}, $.fn.combotree.defaults, $.fn.combotree.parseOptions(this), options)
				});
			}
			_init(this);
		});
	};

	$.fn.combotree.methods = {
		options : function(jq) {
			var opts = $d(jq[0], "combotree").options;
			opts.originalValue = jq.combo("options").originalValue;
			return opts;
		},
		tree : function(jq) {
			var state = $d(jq[0], "combotree");
			if (!state.tree)
				_initTree(jq[0]);

			return state.tree;
		},
		loadData : function(jq, treeDataArray) {
			return jq.each(function() {
				var opts = $d(this, "combotree").options;
				opts.data = treeDataArray;
				var $tree = $d(this, "combotree").tree;
				$tree.tree("loadData", treeDataArray);
			});
		},
		reload : function(jq, url) {
			return jq.each(function() {
				var state = $d(this, "combotree");
				var opts = state.options;
				var $tree = $(jq[0]).combotree("tree");
				if (url) {
					opts.url = url;
				}
				$tree.tree({
					url : opts.url
				});
			});
		},
		setValues : $X(setValues),
		setValue : function(jq, value) {
			return jq.each(function() {
				setValues(this, [ value ]);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				var $tree = $d(this, "combotree").tree;
				$rc("TrN-S", $f("div.TrN-S", $tree));
				var cc = $tree.tree("getChecked");
				for ( var i = 0; i < cc.length; i++) {
					$tree.tree("uncheck", cc[i].target);
				}
				$(this).combo("clear");
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).combotree("options");
				if (opts.multiple) {
					$(this).combotree("setValues", opts.originalValue);
				} else {
					$(this).combotree("setValue", opts.originalValue);
				}
			});
		}
	};

	$.fn.combotree.parseOptions = function(comboHTML) {
		return $.extend({}, $.fn.combo.parseOptions(comboHTML), $.fn.tree.parseOptions(comboHTML));
	};

	$.fn.combotree.defaults = $.extend({}, $.fn.combo.defaults, $.fn.tree.defaults, {
		editable : true,
		keyHandler : {
			doKey : function(key) {
				_doKey(this, key);
			},
			doEnter : function() {
				var values = $(this).combotree("getValues");
				$(this).combotree("setValues", values);
				$(this).combotree("hidePanel");
			},
			doQuery : function(keywords) {
				_doQuery(this, keywords);
			}
		}
	});

})(jQuery, jCocit);
