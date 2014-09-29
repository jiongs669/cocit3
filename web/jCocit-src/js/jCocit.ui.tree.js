/**
 * This is tree UI plugin.
 * <P>
 * Used to convert original "UL" to tree object
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: none
 * <LI>ReferencedBy: combotree
 * <LI>SubClass: none
 * <LI>SuperClass: none
 * </UL>
 * 
 * <P>
 * <b>Tree HTML Document Structure:</b>
 * <UL>
 * <LI>tree: The tree is an UL element.
 * <LI>tree node: The tree node is DIV element nested in LI.
 * <LI>sub tree: The subtree is an UL element nested in LI.
 * <LI>root node: The root node means that this node is located in the first level LI.
 * <LI>branch node: this is node contains all descendants nodes, it can be a folder node or leaf node.
 * <LI>folder node: The folder node means that this node contains subtree.
 * <LI>leaf node: The leaf node means that this node NOT contains subtree.
 * </UL>
 * 
 * <pre>
 *  [UL class='Tr']
 *  	[LI]
 *  		[div class='TrN' unselectable='on']
 *  			[span class='TrD'][/span]
 *  			[span class='TrH'][/span]
 *  			[span class='TrI'][/span]
 *  			[span class='TrC'][/span]
 *  			[span class='TrT' unselectable='on'] Here is node title. [/span]
 *  		[/div]
 *  		[UL]
 *  			Here is sub tree.
 *  		[/UL]
 * 		[/LI]
 * 		[LI]...[/LI]
 * 		...
 *  [/UL]
 * </pre>
 * 
 * <P>
 * <b>Tree Node HTML Document Structure:</b> The tree node contains the following SPAN elements.
 * <UL>
 * <LI>Tr: (tree) - Tree.
 * <LI>Tr-CL: (tree-checkbox-left) - Tree Checkbox on the Left of Icon
 * <LI>TrN-no-checkbox: (tree-nocheckbox-left) - Tree Checkbox on the Left of Icon - Node No Checkbox
 * <LI>TrN: (tree-node) - Tree Node
 * <LI>TrN-F: (tree-root-first) - Tree Node - Root First
 * <LI>TrN-O: (tree-root-one) - Tree Node - Root One
 * <LI>TrN-L: (tree-node-last) - Tree Node - Last
 * <LI>TrN-H: (tree-node-hover) - Tree Node - Hover
 * <LI>TrN-S: (tree-node-selected) - Tree Node - Selected
 * <LI>TrN-P: (tree-node-page)
 * <LI>TrD: (tree-indent) - Tree Node Indent
 * <LI>TrD-I: (tree-line) - Tree Node Indent - Line(I)
 * <LI>TrD-T: (tree-join) - Tree Node Indent - Join(T)
 * <LI>TrD-L: (tree-joinbottom) - Tree Node Indent - Join Bottom
 * <LI>TrH: (tree-hit) - Tree Node Hit
 * <LI>TrH-E: (tree-expanded) - Tree Node Hit - Expanded
 * <LI>TrH-EH: (tree-expanded-hover) - Tree Node Hit - Expanded Hover
 * <LI>TrH-C: (tree-collapsed) - Tree Node Hit - Collapsed
 * <LI>TrH-CH: (tree-collapsed-hover) - Tree Node Hit - Collapsed Hover
 * <LI>TrI: (tree-icon) - This is node icon.
 * <LI>TrI-F: (tree-folder) - Tree Node Icon - Folder
 * <LI>TrI-FO: (tree-folder-open) - Tree Node Icon - Folder Opened
 * <LI>TrI-L: (tree-leaf) - Tree Node Icon - Leaf
 * <LI>TrC: (tree-checkbox tree-checkbox0) - Tree Node Checkbox. default unchecked
 * <LI>TrC1: (tree-checkbox1) - Tree Node Checkbox - Checked
 * <LI>TrC2: (tree-checkbox2) - Tree Node Checkbox - Indeterminate
 * <LI>TrT: (tree-title) - Tree Node Title
 * <LI>TrP: (tree-node-proxy) - Tree Node Proxy for Dragging
 * <LI>TrP-I: (tree-dnd-icon) - Tree Node Proxy for Dragging - Icon
 * <LI>TrP-Y: (tree-dnd-yes) - Tree Node Proxy for Dragging - Yes Icon
 * <LI>TrP-N: (tree-dnd-no) - Tree Node Proxy for Dragging - No Icon
 * <LI>TrP-T: (tree-node-top) - Tree Node Proxy - Top: drag node to before of target node.
 * <LI>TrP-B: (tree-node-bottom) - Tree Node Proxy - Bottom: drag node to after of target node.
 * <LI>TrP-A: (tree-node-append) - Tree Node Proxy - Append: drag node to inner of target node.
 * <LI>TrE: (tree-editor) - Tree Node Editor
 * <LI>TrL: (tree-loading) - Tree Loading
 * </UL>
 * 
 * <p>
 * <B>Tree Node JSON Data Structure:</B> The tree data is JSON array, every element is JSON object to hold tree node data.
 * <UL>
 * <LI>id: this is node id.
 * <LI>iconCls: this is icon class name before node title.
 * <LI>state: this is node status used for folder node, the options are "open/closed".
 * <LI>checked: true means that the checkbox of this node is checked.
 * <LI>text: this is node title.
 * <LI>children: this is children nodes array, used to hold sub tree data.
 * <LI>attributes: this is node attributes.
 * </UL>
 * 
 * <P>
 * <B>Node Config Object:</B>
 * <UL>
 * <LI>target: this is the current node DIV element.
 * <LI>parent: this is the parent DIV element of the current node.
 * <LI>before: this is the before DIV element of the current node.
 * <LI>after: this is the after DIV element of the current node.
 * <LI>data: this is node JSON data.
 * </UL>
 */
(function($, jCocit) {

	function _state(selfUL, data) {
		return $d(selfUL, "tree", data);
	}

	function _options(selfUL) {
		return $d(selfUL, "tree").options;
	}

	function _init(selfUL, opts) {
		var $tree = $(selfUL);
		if (opts.height) {
			if (opts.height == "auto")
				opts.height = "100%";

			$tree.height(opts.height);
		}

		$ac("Tr", $tree);

		return $tree;
	}

	function _parseTreeDatas(selfUL) {
		var dataRows = [];
		_parseNodeData(dataRows, $(selfUL));

		function _parseNodeData(dataRows, $tree) {
			$c("li", $tree).each(function() {
				var $li = $(this);

				// parse node, it is a JSON object
				var item = _parseNodeOptions(this);

				// parse node title
				item.text = $c("span", $li).html();
				if (!item.text) {
					item.text = $li.html();
				}

				// parse sub tree
				var $subtree = $c("ul", $li);
				if ($subtree.length) {
					item.children = [];
					_parseNodeData(item.children, $subtree);
				}

				// add item to array
				dataRows.push(item);
			});
		}

		return dataRows;
	}

	function _bindTreeEvents(selfUL) {
		var opts = _options(selfUL);
		$(selfUL).unbind().bind("mouseover.tree", function(e) {
			var $eventTarget = $(e.target);

			// get the current node
			var $node = $l("div.TrN", $eventTarget);
			if (!$node.length) {
				return;
			}

			// mouse hover on the node
			// if (!opts.onlyLeafValue || _isLeafNode($node))
			$ac("TrN-H", $node);

			// mouse hover on node-hit SPAN
			if ($hc("TrH", $eventTarget))
				if ($hc("TrH-E", $eventTarget))
					$ac("TrH-EH", $eventTarget);
				else
					$ac("TrH-CH", $eventTarget);

			// stop event propagation
			e.stopPropagation();

		}).bind("mouseout.tree", function(e) {
			var $eventTarget = $(e.target);

			// get the current node
			var $node = $l("div.TrN", $eventTarget);
			if (!$node.length) {
				return;
			}

			// mouse leave the node
			$rc("TrN-H", $node);

			// mouse leave the node-hit SPAN
			if ($hc("TrH", $eventTarget)) {
				if ($hc("TrH-E", $eventTarget)) {
					$rc("TrH-EH", $eventTarget);
				} else {
					$rc("TrH-CH", $eventTarget);
				}
			}

			// stop event propagation
			e.stopPropagation();

		}).bind("click.tree", function(e) {
			var $eventTarget = $(e.target);

			// get the current node
			var $node = $l("div.TrN", $eventTarget);
			if (!$node.length) {
				return;
			}

			if ($hc("TrH", $eventTarget) || $hc("TrI-F", $eventTarget)) {
				// click the node-hit or node-icon of folder node, toggle folder node.

				toggleNode(selfUL, $node[0]);

				return false;
			} else if ($hc("TrC", $eventTarget)) {
				// click the node-checkbox, check or uncheck the node checkbox

				checkNode(selfUL, $node[0], !$hc("TrC1", $eventTarget));

				return false;
			} else if ($hc("TrT", $eventTarget)) {
				// click the node-title

				selectNode(selfUL, $node[0]);
				opts.onClickTitle.call(selfUL, getNode(selfUL, $node[0]));

			} else {
				// click the node other, select node

				selectNode(selfUL, $node[0]);
				opts.onClick.call(selfUL, getNode(selfUL, $node[0]));

			}

			// stop event propagation
			e.stopPropagation();

		}).bind("dblclick.tree", function(e) {
			var $eventTarget = $(e.target);

			// get the current node
			var $node = $l("div.TrN", $(e.target));
			if (!$node.length) {
				return;
			}

			if ($hc("TrH", $eventTarget) || $hc("TrI-F", $eventTarget)) {
				// expandNode(selfUL, $node[0]);
			} else if ($hc("TrC", $eventTarget)) {
				checkNode(selfUL, $node[0], !$hc("TrC1", $eventTarget), true);
				return false;
			} else {
				// click the node-title or other, select node
				selectNode(selfUL, $node[0]);
				opts.onDblClick.call(selfUL, getNode(selfUL, $node[0]));
			}

			// stop event propagation
			e.stopPropagation();

		}).bind("contextmenu.tree", function(e) {

			// get the current node
			var $node = $l("div.TrN", $(e.target));
			if (!$node.length) {
				return;
			}

			// call-back onContextMenu function
			opts.onContextMenu.call(selfUL, e, getNode(selfUL, $node[0]));

			// stop event propagation
			e.stopPropagation();
		}).bind("scroll.tree", function(e) {
			var $tree = $(this);
			var state = _state(this);

			// _doSimpleScroll();
			// function _doSimpleScroll() {
			// var th = $oh($tree) + 10;
			// var $pnodes = $f("div.TrN-P:visible", $tree);
			// for ( var i = $pnodes.length - 1; i >= 0; i--) {
			// var $pnode = $($pnodes[i]);
			// var bottom = th - $pnode.position().top - $oh($pnode);
			// if (bottom < th && bottom > -100) {
			// $pnode.next("div.TrN-P").show();
			// }
			//
			// }
			// }

			if (!state.scrollTO) {
				state.scrollTO = setTimeout(function() {
					_doSequentialScroll();

					clearTimeout(state.scrollTO);
					state.scrollTO = null;
				}, 10);
				// } else {
				// $log("ui.tree: _doSequentialScroll running...");
			}

			// support sequential scroll: need to set UL sub tree height.
			function _doSequentialScroll() {
				var time0 = new Date().getTime(), i = 0, $currPage, $firstPage, $nextPage, $prevPage, //
				$firstPages, //
				// scrollTop = $tree.scrollTop(), treeHeight = $oh($tree) + 10, //
				firstTop, firstHeight, maxPageIndex, //
				currPageIndex, prevHiddenSize;//

				$firstPages = $f(">div.TrN-P:first", $f("UL:visible", $tree));

				for (; i < $firstPages.length; i++) {
					$firstPage = $($firstPages[i]);

					firstTop = $firstPage.position().top;
					firstHeight = $c("li", $firstPage).length * opts.rowHeight;

					maxPageIndex = $f(">div.TrN-P", $firstPage.parent()).length - 1;

					// eval current page index
					currPageIndex = parseInt((-firstTop) / firstHeight);
					if (currPageIndex >= 0) {
						if (currPageIndex > maxPageIndex)
							currPageIndex = maxPageIndex;
						$currPage = $f(">div.TrN-P:eq(" + currPageIndex + ")", $firstPage.parent());

						if ($currPage.length) {
							prevHiddenSize = $currPage.prevAll("div.TrN-P:hidden").show().length;
							$currPage.show();
							$currPage.next("div.TrN-P").show();
						}

						// $log("ui.tree:scroll: firstPage={node:[{1}], top:[{2}], height:[{3}]}, currPageIndex=[{4}], prevHiddenSize=[{5}], currPageSize=[{6}], nextHiddenSize=[{7}]", time0, $firstPage.find(">li:first>div.TrN>span.TrT").text(), firstTop, firstHeight, currPageIndex, prevHiddenSize,
						// $currPage.length, $currPage.nextAll("div.TrN-P:hidden").length);
					}
				}
			}

		}).scrollTop(0);

	}

	function checkNode(selfUL, nodeDIV, checked, cascadeCheck, ignoreOnCheck) {
		var time0 = new Date().getTime();

		if ($.type(nodeDIV.target) != "undefined") {
			checked = nodeDIV.checked;
			cascadeCheck = nodeDIV.cascadeCheck;
			ignoreOnCheck = nodeDIV.ignoreOnCheck;
			nodeDIV = nodeDIV.target;
		}

		// unchecked all nodes
		if (!nodeDIV) {
			$rc("TrC1 TrC2", $f("span.TrC", $(selfUL)));
			return;
		}

		var opts = _options(selfUL);
		if (!opts.checkbox) {
			return;
		}
		var node = getNode(selfUL, nodeDIV);
		if (!ignoreOnCheck && opts.onBeforeCheck.call(selfUL, node, checked) == false) {
			return;
		}
		var $node = $(nodeDIV);
		var $checkbox = $f(".TrC", $node);

		var nodeSelector = "div.TrN";
		// if (opts.keywords && opts.keywords.length > 0) {
		// nodeSelector = "div.TrN-keywords";
		// }

		checked = checked ? 1 : 0;
		if (checked)
			$ac("TrC1", $rc("TrC2", $checkbox));
		else
			$rc("TrC1 TrC2", $checkbox);

		if (cascadeCheck || opts.cascadeCheck) {
			_checkAncestorNodes($node, nodeSelector, checked);
			// $log("ui.tree:checkNode:_checkAncestorNodes", time0);

			_checkDescendantsNodes($node, nodeSelector, checked);
		}
		if (!ignoreOnCheck)
			opts.onCheck.call(selfUL, node, checked);

		function _checkDescendantsNodes($node, nodeSelector, checked) {
			var $subtree = $node.next();

			var nodes = $subtree.find(nodeSelector + ">span.TrC");
			// $log("ui.tree:checkNode:_checkDescendantsNodes: found! nodes = {1}", time0, nodes.length);

			if (nodes.length > 1000) {
				$ac("TrL", $c(".TrI", $node));
				setTimeout(function() {
					if (checked)
						nodes.setClass("TrC TrC1");
					// nodes.removeClass("TrC2").addClass("TrC1");
					else
						nodes.setClass("TrC");
					// nodes.removeClass("TrC1 TrC2");

					$rc("TrL", $c(".TrI", $node));

					// $log("ui.tree:checkNode:_checkDescendantsNodes: end! nodes = {1}", time0, nodes.length);
				}, 200);
			} else {
				if (checked)
					nodes.setClass("TrC TrC1"); // nodes.removeClass("TrC2").addClass("TrC1");
				else
					nodes.setClass("TrC");// nodes.removeClass("TrC1 TrC2");
			}

		}

		function _checkAncestorNodes($node, nodeSelector, checked) {
			var parentNode = getParentNode(selfUL, $node[0]);

			if (parentNode) {
				var $parentNode = $(parentNode.target);
				var $checkbox = $f(".TrC", $parentNode);

				var chk1 = _existSiblings($node, nodeSelector, "TrC1");
				if (chk1 == _existSiblings($node, nodeSelector, "TrC")) {
					$ac("TrC1", $rc("TrC2", $checkbox))
				} else {
					if (!chk1 && _existSiblings($node, nodeSelector, "TrC2") == 0)
						$rc("TrC1 TrC2", $checkbox);
					else
						$ac("TrC2", $rc("TrC1", $checkbox))

				}
				_checkAncestorNodes($parentNode, nodeSelector, checked);
			}

			function _existSiblings($node, nodeSelector, cls) {
				// subtree = $p($p($p($node))): DIV.TrN > LI > DIV.TrN-P > UL
				return $f(">DIV.TrN-P>LI>" + nodeSelector + ">SPAN." + cls, $p($p($p($node)))).length;
			}

		}
	}

	/**
	 * because performence, so using aboved checkNode implements
	 */
	// function checkNode(selfUL, nodeDIV, checked, cascadeCheck, ignoreOnCheck) {
	// var time0 = new Date().getTime();
	//
	// if ($.type(nodeDIV.target) != "undefined") {
	// checked = nodeDIV.checked;
	// cascadeCheck = nodeDIV.cascadeCheck;
	// ignoreOnCheck = nodeDIV.ignoreOnCheck;
	// nodeDIV = nodeDIV.target;
	// }
	//
	// // unchecked all nodes
	// if (!nodeDIV) {
	// $rc("TrC1 TrC2", $ac("TrC0", $f("span.TrC", $(selfUL))));
	// return;
	// }
	//
	// var opts = _options(selfUL);
	// if (!opts.checkbox) {
	// return;
	// }
	// var node = getNode(selfUL, nodeDIV);
	// if (!ignoreOnCheck && opts.onBeforeCheck.call(selfUL, node, checked) == false) {
	// return;
	// }
	//
	// var $node = $(nodeDIV);
	//
	// checked = checked ? 1 : 0;
	//
	// if (cascadeCheck || opts.cascadeCheck) {
	// var nodeSelector = "div.TrN";
	// if (opts.keywords && opts.keywords.length > 0) {
	// nodeSelector = "div.TrN-keywords";
	// }
	//
	// _checkDescendantsNodes($node, nodeSelector, checked);
	//
	// _checkSelf($node, checked);
	// $log("ui.tree:checkNode:_checkSelf", time0);
	//
	// _checkAncestorNodes($node);
	// $log("ui.tree:checkNode:_checkAncestorNodes", time0);
	// } else {
	// var $checkbox = $f(".TrC", $node);
	// _check($node, $checkbox, checked);
	// }
	//
	// if (!ignoreOnCheck)
	// opts.onCheck.call(selfUL, node, checked);
	//
	// function _check($node, $checkbox, checkType) {
	// $rc("TrC0 TrC1 TrC2", $checkbox);
	//
	// if (checkType == 0) {
	// $ac("TrC0", $checkbox);
	// } else if (checkType == 1) {
	// $ac("TrC1", $checkbox);
	// } else {
	// $ac("TrC2", $checkbox);
	// }
	// }
	//
	// function _checkDescendantsNodes($node, nodeSelector, checked) {
	//
	// function _asynCheckDescendantsNodes($node, nodeSelector, checked) {
	//
	// var $subtree = $node.next();
	// $f(">DIV>LI>" + nodeSelector, $subtree).each(function() {
	// var $childNode = $(this);
	// setTimeout(_asynCheckDescendantsNodes, 50, $childNode, nodeSelector, checked);
	//
	// _checkSelf($childNode, checked);
	// });
	//
	// }
	// _asynCheckDescendantsNodes($node, nodeSelector, checked);
	//
	// $log("ui.tree:checkNode:_checkDescendantsNodes", time0);
	// }
	//
	// function _checkSelf($node, checked) {
	// var $checkbox = $f(".TrC", $node);
	// var $child = $(">DIV>LI>div.TrN:first", $node.next("UL"));
	//
	// if ($child.length) {
	// if (_isSiblingsChecked($child)) {
	// _check($node, $checkbox, 1);
	// } else {
	// if (_isSiblingsUnchecked($child)) {
	// _check($node, $checkbox, checked);
	// } else {
	// _check($node, $checkbox, 2);
	// }
	// }
	// } else {
	// _check($node, $checkbox, checked);
	// }
	// }
	//
	// function _checkAncestorNodes($node) {
	// var parentNode = getParentNode(selfUL, $node[0]);
	// if (parentNode) {
	// var $parentNode = $(parentNode.target);
	// var $parentCheckbox = $f(".TrC", $parentNode);
	//
	// if (_isSiblingsChecked($node)) {
	// _check($parentNode, $parentCheckbox, 1);
	// } else {
	// if (_isSiblingsUnchecked($node)) {
	// _check($parentNode, $parentCheckbox, 0);
	// } else {
	// _check($parentNode, $parentCheckbox, 2);
	// }
	// }
	// _checkAncestorNodes($(parentNode.target));
	// }
	// }
	//
	// /**
	// * Parameters:
	// * <UL>
	// * <LI>$node: this is DIV element jQuery object
	// * </UL>
	// */
	// function _isSiblingsChecked($node) {
	// var $checkbox = $f(".TrC", $node);
	// if ($hc("TrC0", $checkbox) || $hc("TrC2", $checkbox)) {
	// return false;
	// }
	// var b = true;
	//
	// // $p($node) is LI, which siblings are LI collection
	// $p($node).siblings().each(function() {
	// if (!$hc("TrC1", $c(".TrC", $c("div.TrN", $(this))))) {
	// b = false;
	// }
	// });
	// return b;
	// }
	//
	// /**
	// * Parameters:
	// * <UL>
	// * <LI>$node: this is DIV element jQuery object
	// * </UL>
	// */
	// function _isSiblingsUnchecked($node) {
	// var $checkbox = $f(".TrC", $node);
	// if ($hc("TrC1", $checkbox) || $hc("TrC2", $checkbox)) {
	// return false;
	// }
	// var b = true;
	//
	// // $p($node) is LI, which siblings are LI collection
	// $p($node).siblings().each(function() {
	// if (!$hc("TrC0", $c(".TrC", $c("div.TrN", $(this))))) {
	// b = false;
	// }
	// });
	// return b;
	// }
	// }
	/**
	 * load datas for subtree specified by "subtreeUL" with datas specified by "data"
	 */
	function loadNodes(selfUL, subtreeUL, dataRows, append, callback, cascadeCheck) {
		var time0 = new Date().getTime();

		if ($.type(subtreeUL.target) != "undefined") {
			dataRows = subtreeUL.data;
			append = subtreeUL.append;
			subtreeUL = subtreeUL.target || selfUL;
		}

		/**
		 * folderNode is the current folder node which sub tree data is being loaded.
		 * <p>
		 * folderNodeCord is data index. eg: "[2][4][5]" means that folder node data located opts.data[2][4][5].
		 */
		var folderNode = null, folderNodeCord = "", opts = _options(selfUL), state = _state(selfUL), $tree = $(selfUL), $subtree = $(subtreeUL), item, openAll, depth, newToken = state.token, keywords = opts.keywords;

		if (selfUL == subtreeUL) {
			opts.data = dataRows;
		} else {
			folderNode = getNode(selfUL, $subtree.prev()[0]);
			folderNodeCord = folderNode._cord_ || "";
			item = _getItem(opts.data, folderNodeCord);
			item.children = dataRows;
		}

		cascadeCheck = cascadeCheck && opts.cascadeCheck;

		// query mode: the openAll is true if options don't specified "openAll"
		openAll = opts.openAll;
		if (opts.keywords && openAll == -1 && opts.openOnQuery)
			openAll = 1;

		// filter data rows, eg: filter by keywords.
		dataRows = opts.dataFilter.call(selfUL, newToken, dataRows, $subtree.prev("div.TrN")[0]);
		if (!append) {
			$subtree.empty();
		}

		// node depth
		depth = $f("span.TrD, span.TrH", $subtree.prev("div.TrN")).length;

		if (!state.QueueLoader) {
			// this Object used to sequence auto load asyn loaded folder subtree.
			state.QueueLoader = {
				loading : false,
				nodes : [],
				add : function(nodeDIVs) {
					var existed = false;
					for ( var i = 0; i < nodeDIVs.length; i++) {
						var folderDepth = $f("span.TrD, span.TrH", $(nodeDIVs[i])).length;
						if (folderDepth > opts.maxQueryDepth) {
							continue;
						}
						for ( var j = 0; j < this.nodes.length; j++) {
							if (this.nodes[j] == nodeDIVs[i]) {
								existed = true;
								break;
							}
						}
						if (!existed) {
							this.nodes.push(nodeDIVs[i]);
							$log("ui.tree:loadNodes:QueueLoader: [" + $(nodeDIVs[i]).attr("nid") + "] queue... nodes = " + this.nodes.length);
						}
					}
				},
				load : function(subtree) {
					var self = this;
					if (!self.subtree) {
						self.subtree = subtree;
					}
					if (self.nodes.length > 0) {
						$log("ui.tree:loadNodes:QueueLoader: [" + $(self.nodes[0]).attr("nid") + "] loading... nodes = " + self.nodes.length);

						_sto(expandNode, 10, selfUL, self.nodes.splice(0, 1), function(times) {
							if (times == 2)
								self.load();

						}, true);
					} else {
						// if (opts.keywords)
						// opts.onEndQuery.call(selfUL, self.subtree);

						self.subtree = null;
					}

				}
			};
		}

		$log("ui.tree:loadNodes: keywords=[{1}], filterDataRows=[{2}], folder=[{3}], openAll=[{4}]...", time0, //
		keywords, dataRows.length, (folderNode ? folderNode.text : ""), openAll);

		// append node to subtree
		_appendNodes2({
			token : newToken,
			depth : depth,
			cordPrefix : folderNodeCord,
			state : state,
			opts : opts,
			$subtree : $subtree,
			$tree : $tree,
			callback : callback,
			folderNode : folderNode,
			dataRows : dataRows,
			time0 : time0,
			openAll : openAll,
			cascadeCheck : cascadeCheck
		}, dataRows);

	}

	function _callback(config, times, nodesSize) {
		var $tree = config.$tree, $subtree = config.$subtree, opts = config.opts, state = config.state, callback = config.callback, selfUL = $tree[0], subtreeUL = $subtree[0], folderNode = config.folderNode, dataRows = config.dataRows, time0 = config.time0;

		if (times != 0)
			$log("ui.tree:loadNodes: NO.{3} Loaded! tree=[{2}], folder=[{5}], keywords=[{4}], nodes=[{1}]", time0, nodesSize, selfUL.title || selfUL.id || "", times, opts.keywords || "", (folderNode ? $(folderNode.target).attr("title") : ""));

		// support sequential scroll: set UL sub tree height
		if (times == 2) {

			// draggable
			if (opts.dnd) {
				$tree.tree("enableDnd");
			} else {
				$tree.tree("disableDnd");
			}

			if (opts.keywords) {

				opts.onEndQuery.call(selfUL, subtreeUL);

				// if not support indent, the depth already is 0.
				if ((opts.indent || !state.QueueLoader.subtree) && opts.loadOnQuery) {
					state.QueueLoader.add($subtree.find("div.TrN[loaded='0']"));
				}

				// 1. support load on query; 2. QueryLoader is not loading.
				if (!state.QueueLoader.subtree)
					state.QueueLoader.load(subtreeUL);

				// filter query result after asyn loaded nodes.
				if (folderNode)
					_cascadeToFilter(config, $(folderNode.target));
			}

			// set sub tree min height
			if (folderNode)
				$subtree.css("min-height", dataRows.length * opts.rowHeight);

			$tree.trigger("scroll.tree");
		}

		if (callback) {
			callback(times);
		}
		opts.onLoadSuccess.call(selfUL, folderNode, dataRows, times);
	}

	function _cascadeToFilter(config, $folderNode) {
		var selfUL = config.$tree[0];

		var $subtree = $folderNode.next("UL");
		if ($subtree.length && $subtree.find(">div>li").length == 0) {
			$subtree.remove();

			// $log("ui.tree:_cascadeToFilter: remove subtree: " + $folderNode.attr("title"));

			_checkNode();

			// } else {
			// if ($folderNode.attr("loaded") != "0")
			// _checkNode();
		}

		function _checkNode() {

			if ($folderNode.find("span.TrT-keywords").length == 0) {
				var parent = getParentNode(selfUL, $folderNode[0]);

				// reset parent subtree min-height
				if (selfUL != $subtree[0]) {
					// div.TrN > LI > div.TrN-P > UL
					var $parentTree = $folderNode.parent().parent().parent();
					$parentTree.css("min-height", $parentTree._css("min-height") - config.opts.rowHeight);
				}

				$folderNode.parent().remove();
				$folderNode.remove();

				// $log("ui.tree:_cascadeToFilter: remove folder: " + $folderNode.attr("title"));

				if (parent)
					_cascadeToFilter(config, $(parent.target));
			}
		}
	}

	function _appendNodes2(config, dataRows) {
		var opts = config.opts;

		// _callback(config, 0, 0);

		// solution 2: one by one load for big data
		var Render = function(config, from, pageSize) {
			this.exec = function() {
				var $subtree = config.$subtree, depth = config.depth, cordPrefix = config.cordPrefix, token = config.token, state = config.state, time0 = config.time0;

				// $log(selfUL.id + ": " + "next: " + next);

				if (from < dataRows.length) {
					if (pageSize) {

						var html = [];
						var nodeSize = _makeRows(config, dataRows, html, depth, cordPrefix, 0, pageSize > dataRows.length ? dataRows.length : pageSize);

						if (token == state.token) {

							$log("ui.tree:loadNodes:_makeRows 1: keywords=[{1}], nodeSize=[{2}], dataRows.length=[{3}]", time0, config.opts.keywords, nodeSize, dataRows.length);

							$subtree.append(html.join(""));

							new Render(config, pageSize).exec();

							// } else {
							// $log("ui.tree:loadNodes:_makeRows 1: keywords is changed from [{1}] to [{2}]", time0, keywords, opts.keywords);
						}

						_sto(_callback, 200, config, 1, nodeSize);

					} else {

						_sto(function() {

							var html = [];
							var nodeSize = _makeRows(config, dataRows, html, depth, cordPrefix, from);

							if (token == state.token) {

								$log("ui.tree:loadNodes:_makeRows 2: keywords=[{1}], nodeSize=[{2}], dataRows.length=[{3}]", time0, config.opts.keywords, nodeSize, dataRows.length);

								$subtree.append(html.join(""));

								// } else {
								// $log("ui.tree:loadNodes:_makeRows 2: keywords is changed from [{1}] to [{2}]", time0, keywords, opts.keywords);
							}

							_sto(_callback, 800, config, 2, nodeSize);

						}, 100);

					}
				} else {
					_callback(config, 2, 0);
				}
			}

			return this;
		};
		new Render(config, 0, opts.pageSize).exec();
		// END: one by one load

		function _makeRows(config, items, resultHTML, depth, cordPrefix, from, len) {
			$log("ui.tree._makeRows: items.length={1}, depth={2}, cordPrefix={3}, from={4}, len={5}", 0, items.length, depth, cordPrefix, from, len);

			var nodeSize = 0, token = config.token, state = config.state;

			if (from == 0)
				resultHTML.push('<div class="TrN-P">');

			if (!len)
				len = items.length;

			var maxIndex = len - 1;
			for ( var i = from; i < len; i++) {
				if (token != state.token) {
					return 0;
				}

				if (i > 0 && (i % opts.pageSize) == 0) {
					resultHTML.push('</div>');
					resultHTML.push('<div class="TrN-P" style="display:none;">');
				}
				nodeSize += _makeRow(token, items[i], resultHTML, depth, (cordPrefix ? (cordPrefix + ",") : "") + i, i == maxIndex, i == 0 && depth == 0 && len == 1);
			}

			resultHTML.push('</div>');

			return nodeSize;
		}

		function _makeRow(token, item, resultHTML, depth, itemCord, isLast, isRootFirst) {
			$log("ui.tree._makeRow: item={1}, token={2}, depth={3}, itemCord={4}, isLast={5}", 0, item, token, depth, itemCord, isLast);

			if (!item)
				return 0;

			var title = item.text.toHtml(), openAll = config.openAll, cascadeCheck = config.cascadeCheck, //
			formatTitle = opts.titleFormater(item, opts.keywords), //
			nodeSize = 1, //
			children = item.url || item.children, // if original children is string, after loaded children is Array.
			asyn = $.type(children) == "string", // children is url means asyn load.
			closed = (openAll == -1 && !item.open) || openAll == 0 || asyn;

			if (!formatTitle) {
				return 0;
			}

			resultHTML.push('<li><div unselectable="on" class="TrN');

			/**
			 * add node class
			 */
			if (opts.checkbox && opts.onlyLeafCheck && children)
				resultHTML.push(" TrN-no-checkbox");
			if (isRootFirst)
				resultHTML.push(" TrN-O");

			if (asyn) {
				if (!item.url) {
					var orgItem = _getItem(opts.data, itemCord);
					if (orgItem)
						orgItem.url = item.children;
					// else {
					// $log("ui.tree:loadNodes:_makeRow[" + item.id + "]: not found! url=[" + item.children + "] itemCord=[" + itemCord + "]");
					// }
				}

				resultHTML.push('" loaded="0');
			}
			resultHTML.push('" cord="');
			resultHTML.push(item._cord_ || itemCord);
			resultHTML.push('" nid="');
			resultHTML.push((item.id || "").toHtml());
			resultHTML.push('" title="');
			resultHTML.push(title);
			resultHTML.push('">');

			/*
			 * add node indent
			 */
			if (opts.indent) {
				for ( var j = 0; j < depth; j++) {
					if (opts.lines)
						resultHTML.push('<span class="TrD TrD-I"></span>');
					else
						resultHTML.push('<span class="TrD"></span>');
				}
				if (!children) {
					if (opts.lines) {
						if (isLast)
							resultHTML.push('<span class="TrD TrD-L"></span>');
						else
							resultHTML.push('<span class="TrD TrD-T"></span>');
					} else {
						resultHTML.push('<span class="TrD"></span>');
					}
				}
			}

			/*
			 * add node hit
			 */
			if (children) {
				resultHTML.push('<span class="TrH');

				if (closed)
					resultHTML.push(" TrH-C");
				else
					resultHTML.push(" TrH-E");

				resultHTML.push('"></span>');
			}

			/*
			 * add node icon
			 */
			resultHTML.push('<span class="TrI ');

			// folder icon
			if (children) {
				resultHTML.push("TrI-F");
				if (!closed)
					resultHTML.push(" TrI-FO ");
			} else {
				// leaf icon
				resultHTML.push("TrI-L ");
			}

			// customize icon
			if (item.iconCls)
				resultHTML.push(item.iconCls);

			resultHTML.push('"></span>');

			/*
			 * add node checkbox
			 */
			if (opts.checkbox && (!opts.onlyLeafCheck || !children)) {
				if (cascadeCheck || (opts.checkAll != -1 ? opts.checkAll : item.checked))
					resultHTML.push('<span class="TrC TrC1"></span>');
				else
					resultHTML.push('<span class="TrC"></span>');
			}

			/**
			 * add node title
			 */
			resultHTML.push('<span unselectable="on" class="TrT">');
			resultHTML.push(formatTitle);
			resultHTML.push('</span>');

			// end node
			resultHTML.push('</div>');

			/*
			 * add sub tree
			 */
			if (children && !asyn) {
				resultHTML.push('<ul style="');
				if (closed)
					resultHTML.push('display:none;');

				resultHTML.push('min-height: ' + children.length * opts.rowHeight + "px;");

				resultHTML.push('">');

				nodeSize += _makeRows(config, children, resultHTML, depth + 1, itemCord, 0);

				resultHTML.push('</ul>');
			}

			// end LI
			resultHTML.push('</li>');

			return nodeSize;

		}// end _makeRow

	}// end _appendNodes2

	/**
	 * Not support dynamic CRUD node
	 */
	function _refreshJoinLines(selfUL, $subtreeUL, isNotRootNode) {
		// var opts = _options(selfUL);
		//
		// if (!opts.lines) {
		// return;
		// }
		//
		// // add line to join root nodes
		// if (!isNotRootNode) {
		// var $tree = $(selfUL);
		// isNotRootNode = true;
		//
		// // first remove old join lines
		// $rc("TrD-I TrD-T TrD-L", $f("span.TrD-I,span.TrD-T,span.TrD-L", $tree));
		// $rc("TrN-L TrN-F TrN-O", $f("div.TrN-O,div.TrN-F,div.TrN-L", $tree));
		//
		// var rootNodes = getRootNodes(selfUL);
		// if (rootNodes.length > 1) {
		// // add line to join first root node
		// $ac("TrN-F", $(rootNodes[0].target));
		// } else {
		// if (rootNodes.length == 1)
		// $ac("TrN-O", $(rootNodes[0].target));
		// }
		// }
		//
		// // add lines to join nodes
		// $f(">DIV>li>DIV", $subtreeUL).each(function() {
		// var $nodes = $(this);
		// var $subtree = $nodes.next("ul");
		// if ($subtree.length) {
		// if ($(this).next().length)
		// _addSubtreeJoinLine($nodes);
		//
		// _refreshJoinLines(selfUL, $subtree, isNotRootNode);
		// } else {
		// /**
		// * add "|-" line to join leaf node
		// */
		// $ac("TrD-T", $f("span.TrI", $nodes).prev("span.TrD"));
		// }
		// });
		//
		// /*
		// * add "|_" line to join last node
		// */
		// var $lastNode = $ac("TrN-L", $c("div.TrN", $f(">DIV>li:last", $subtreeUL)));
		// $ac("TrD-L", $rc("TrD-T", $c("span.TrD-T", $lastNode)));
		//
		// /**
		// * add "|" line to join sub tree
		// */
		// function _addSubtreeJoinLine($nodes) {
		// var len = $f("span.TrD, span.TrH", $nodes).length;
		// $f("div.TrN", $nodes.next()).each(function() {
		// $ac("TrD-I", $c("span:eq(" + (len - 1) + ")", $(this)));
		// });
		// }
	}

	function _loading() {

	}

	function request(selfUL, subtreeUL, folderNode, queryData, callback) {
		var opts = _options(selfUL);
		queryData = queryData || {};
		var node = null;
		var $subtree = $(subtreeUL);
		if (selfUL != subtreeUL) {
			var $node = $subtree.prev();
			node = getNode(selfUL, $node[0]);
		}
		if (opts.onBeforeLoad.call(selfUL, node, queryData) == false) {
			return;
		}

		/*
		 * Getting loading node to show loading message.
		 */
		var $loading;
		if (selfUL == subtreeUL) {
			$(subtreeUL).empty();
			$loading = $f(">div>li>div>span.TrI-F", $subtree);
		} else
			$loading = $c("span.TrI-F", $subtree.prev());

		if ($loading.length != 1)
			$loading = $("<div></div>").css({
				width : 20,
				height : 20
			}).appendTo($("<li></li>").prependTo($subtree));

		// loading sub tree
		$ac("TrL", $loading);
		var loading = opts.loader.call(selfUL, folderNode, queryData, function(dataRows) {
			$log("ui.tree.request: load data success! dataRows.size = " + (dataRows ? dataRows.length : 0));

			_sto(_doLoadNodes, 100, dataRows);

		}, function(jqXHR, statusText, responseError) {
			$log("ui.tree.request: load data error! statusText = " + statusText);

			var dataRows = $fn(jqXHR.responseText);
			if (dataRows && dataRows.length) {
				_sto(_doLoadNodes, 100, dataRows);
			} else {
				$rc("TrL", $loading);
				opts.onLoadError.apply(selfUL, arguments);
				if (callback)
					callback();
			}

		});

		if (loading == false) {
			$rc("TrL", $loading);
		}

		function _doLoadNodes(dataRows) {
			loadNodes(selfUL, subtreeUL, dataRows, false, function(times) {

				if (callback)
					callback(times);

				if (times == 2) {
					if (folderNode)
						$(folderNode.target).attr("loaded", "1");
					$rc("TrL", $loading);
				}

			}, folderNode ? $c("span.TrC1", $(folderNode.target)).length : 0);

		}
	}

	/**
	 * Expand the specified node, don't contains descendants nodes.
	 */
	function expandNode(selfUL, nodeDIV, callback, ignoreOnExpand) {
		// var time0 = new Date().getTime();

		if ($.type(nodeDIV.target) != "undefined") {
			callback = nodeDIV.callback;
			ignoreOnExpand = nodeDIV.ignoreOnExpand;
			nodeDIV = nodeDIV.target;
		}

		var opts = _options(selfUL);
		var hit = $c("span.TrH", $(nodeDIV));
		if (hit.length == 0) {
			return;
		}
		if ($hc("TrH-E", hit)) {
			return;
		}
		var node = getNode(selfUL, nodeDIV);
		if (!ignoreOnExpand && opts.onBeforeExpand.call(selfUL, node) == false) {
			return;
		}
		$ac("TrH-E", $rc("TrH-C TrH-CH", hit));
		$ac("TrI-FO", hit.next());

		var $node = $(nodeDIV);
		var $subtree = $node.next("UL");
		var loaded = $node.attr("loaded");
		var children = node.children;

		// $log("ui.tree:expandNode: [" + node.id + "] loaded=[" + (loaded || "") + "], url=[" + (node.url || "") + "], children=[" + (($.type(children) != "string" && children) ? children.length : 0) + "]");

		if ($.type(loaded) != "undefined" && loaded == "0" && node.url) {
			$node.attr("loaded", "2");// means that this folder subtree is loading.

			if (!$subtree.length)
				$subtree = $('<ul></ul>').insertAfter(nodeDIV);

			// load nodes from children array
			if ($.type(children) != "string" && children && children.length) {
				$loading = $c("span.TrI-F", $node);
				$ac("TrL", $loading);
				_sto(loadNodes, 100, selfUL, $subtree[0], node.children, false, function(times) {
					_doExpand(times);
					if (times == 2)
						$rc("TrL", $loading);
				}, $c("span.TrC1", $node).length);
			} else {
				// load nodes from remote
				request(selfUL, $subtree[0], node, {
					id : node.id
				}, function(times) {
					_doExpand(times);
				});
			}
		} else {
			_doExpand();
		}

		function _doExpand(times) {
			if (opts.animate) {
				$subtree.slideDown("normal", function() {
					if (!ignoreOnExpand)
						opts.onExpand.call(selfUL, node);

					if (callback)
						callback();
				});
			} else {
				$subtree.css("display", "");

				if (!ignoreOnExpand)
					opts.onExpand.call(selfUL, node, times);

				if (callback)
					callback(times);

				// $log("ui.tree:expandNode: END. ", time0);
			}

			$c("div.TrN-P:first", $subtree).css("display", "block");
		}
	}

	/**
	 * Collapse the specified node, don't contains descendants nodes.
	 */
	function collapseNode(selfUL, nodeDIV, igloreOnCollapse) {
		var time0 = new Date().getTime();

		var opts = _options(selfUL);
		var hit = $c("span.TrH", $(nodeDIV));
		if (hit.length == 0) {
			return;
		}
		if ($hc("TrH-C", hit)) {
			return;
		}
		var node = getNode(selfUL, nodeDIV);
		if (!igloreOnCollapse && opts.onBeforeCollapse.call(selfUL, node) == false) {
			return;
		}
		$ac("TrH-C", $rc("TrH-E TrH-EH", hit));
		$rc("TrI-FO", hit.next());
		var $subtree = $(nodeDIV).next();

		if (opts.animate) {
			$subtree.slideUp("normal", function() {
				if (!igloreOnCollapse)
					opts.onCollapse.call(selfUL, node);
			});
		} else {
			$subtree.css("display", "none");

			if (!igloreOnCollapse)
				opts.onCollapse.call(selfUL, node);

			// $log("ui.tree:collapseNode: END. ", time0);
		}

		setTimeout(function() {
			$c("div.TrN-P", $subtree).css("display", "none");
		}, 100);

	}

	/**
	 * Toggle the specified node, don't contains descendants nodes.
	 */
	function toggleNode(selfUL, nodeDIV) {
		var hit = $c("span.TrH", $(nodeDIV));
		if (hit.length == 0) {
			return;
		}
		if ($hc("TrH-E", hit)) {
			collapseNode(selfUL, nodeDIV);
		} else {
			expandNode(selfUL, nodeDIV);
		}
	}

	function getDescendantsFolders(selfUL, nodeDIV) {
		var dataRows = [];
		if (nodeDIV) {
			_parseDescendantFolders($(nodeDIV));
		} else {
			var rootNodes = getRootNodes(selfUL);
			for ( var i = 0; i < rootNodes.length; i++) {
				dataRows.push(rootNodes[i]);
				_parseDescendantFolders($(rootNodes[i].target));
			}
		}

		/**
		 * add decendants of $node to array
		 */
		function _parseDescendantFolders($node) {
			$f("span.TrH", $node.next()).each(function() {
				dataRows.push(getNode(selfUL, $(this).parent()[0]));
			});
		}

		return dataRows;
	}

	/**
	 * Expand the specified node and all descendants nodes.
	 */
	function expandAllNodes(selfUL, nodeDIV) {
		var nodes = getDescendantsFolders(selfUL, nodeDIV);
		if (nodeDIV) {
			nodes.unshift(getNode(selfUL, nodeDIV));
		}
		for ( var i = 0; i < nodes.length; i++) {
			expandNode(selfUL, nodes[i].target);
		}
	}

	/**
	 * Expand the all ancestor nodes of the specified node, besides the current node.
	 */
	function expandToNode(selfUL, nodeDIV, ignoreOnExpand) {
		if ($.type(nodeDIV.target) != "undefined") {
			ignoreOnExpand = nodeDIV.ignoreOnExpand;
			nodeDIV = nodeDIV.target;
		}

		var nodes = [];
		var parentNode = getParentNode(selfUL, nodeDIV);
		while (parentNode) {
			nodes.unshift(parentNode);
			parentNode = getParentNode(selfUL, parentNode.target);
		}
		for ( var i = 0; i < nodes.length; i++) {
			expandNode(selfUL, nodes[i].target, null, ignoreOnExpand);
		}
	}

	/**
	 * Collapse the specified node and all descendants nodes.
	 */
	function collapseAllNodes(selfUL, nodeDIV) {
		var nodes = getDescendantsFolders(selfUL, nodeDIV);
		if (nodeDIV) {
			nodes.unshift(getNode(selfUL, nodeDIV));
		}
		for ( var i = 0; i < nodes.length; i++) {
			collapseNode(selfUL, nodes[i].target);
		}
	}

	/**
	 * Get root node, don't contains descendants nodes.
	 * <P>
	 * <B>Return:</B> node JSON data, don't contains children nodes.
	 */
	function getRootNode(selfUL) {
		var rootNodes = getRootNodes(selfUL);
		if (rootNodes.length) {
			return rootNodes[0];
		} else {
			return null;
		}
	}

	/**
	 * Get root nodes, don't contains descendants nodes.
	 * <P>
	 * <B>Return:</B> nodes Array, it's element is node JSON data. Don't contains children nodes.
	 */
	function getRootNodes(selfUL) {
		var rootNodes = [];
		$f(">DIV>li", $(selfUL)).each(function() {
			var $node = $c("div.TrN", $(this));
			rootNodes.push(getNode(selfUL, $node[0]));
		});
		return rootNodes;
	}

	/**
	 * Get all descendants nodes, don't contains the specified node.
	 * <P>
	 * <B>Return:</B> nodes Array, it's element is node JSON data.
	 */
	function getDescendantsNodes(selfUL, nodeDIV) {
		var dataRows = [];
		if (nodeDIV) {
			_parseDescendants($(nodeDIV));
		} else {
			var rootNodes = getRootNodes(selfUL);
			for ( var i = 0; i < rootNodes.length; i++) {
				dataRows.push(rootNodes[i]);
				_parseDescendants($(rootNodes[i].target));
			}
		}

		/**
		 * add decendants of $node to array
		 */
		function _parseDescendants($node) {
			$f("div.TrN", $node.next()).each(function() {
				dataRows.push(getNode(selfUL, this));
			});
		}

		return dataRows;
	}

	function getChildren(selfUL, nodeDIV) {
		if (nodeDIV) {
			var dataRows = [];

			$f(">DIV>LI>div.TrN", $(nodeDIV).next()).each(function() {
				dataRows.push(getNode(selfUL, this));
			});

			return dataRows;
		} else {
			return getRootNodes(selfUL);
		}
	}

	/**
	 * Get parent node of the specified node.
	 * <P>
	 * <B>Return:</B> node JSON data, don't contains children nodes.
	 */
	function getParentNode(selfUL, nodeDIV) {
		// subtree = DIV > LI > DIV > UL
		var $subtree = $p($p($p($(nodeDIV))));
		if ($subtree[0] == selfUL) {
			return null;
		} else {
			return getNode(selfUL, $subtree.prev()[0]);
		}
	}

	/**
	 * Get checked nodes by the specified status.
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>status: this is specified node check status. the options are checked/unchecked/indeterminate
	 * </UL>
	 * <P>
	 * <B>Return:</B> nodes Array, it's element is node JSON data.
	 */
	function getChecked(selfUL, status) {
		var opts = _options(selfUL);
		status = status || "checked";
		var selector = "";
		if (status == "checked") {
			selector = "span.TrC1";
		} else if (status == "unchecked") {
			selector = "span.TrC0";
		} else if (status == "indeterminate") {
			selector = "span.TrC2";
		}

		var nodes = [];
		$f(selector, $(selfUL)).each(function() {
			var $node = $p($(this));
			nodes.push(getNode(selfUL, $node[0]));
		});

		return nodes;
	}

	/**
	 * Get selected node.
	 * <P>
	 * <B>Return:</B> node JSON data, don't contains children nodes.
	 */
	function getSelected(selfUL) {
		var $selectedNode = $f("div.TrN-S", $(selfUL));
		if ($selectedNode.length) {
			return getNode(selfUL, $selectedNode[0]);
		} else {
			return null;
		}
	}

	/**
	 * Get checked values
	 * <p>
	 * 1. return empty if unchecked any node.
	 * <p>
	 * 2. return value don't contains checked folder node if onlyLeafValue is true.
	 */
	function getCheckedValues(selfUL) {
		var opts = _options(selfUL);

		var nodes = [];
		$f("span.TrC1", $(selfUL)).each(function() {
			var $node = $p($(this));
			if (!opts.onlyLeafValue || _isLeafNode($node))
				nodes.push(getNode(selfUL, $node[0]));
		});

		return nodes;
	}

	/**
	 * Get selected value
	 * <p>
	 * 1. return null if unselected any node.
	 * <p>
	 * 2. return null if onlyLeafValue is true and selected node is folder.
	 */
	function getSelectedValue(selfUL) {
		var opts = _options(selfUL);
		var $selectedNode = $f("div.TrN-S", $(selfUL));

		if (!$selectedNode.length || (opts.onlyLeafValue && !_isLeafNode($selectedNode)))
			return null;

		return getNode(selfUL, $selectedNode[0]);
	}

	/**
	 * Get branch node
	 * <P>
	 * <B>Return:</B> node JSON data, contains all descendants nodes.
	 */
	function getBranchNode(selfUL, nodeDIV) {
		function _parseChildren(dataRows, $tree) {
			$f(">DIV>li", $tree).each(function() {
				var $li = $(this);
				var $node = $c("div.TrN", $li);
				var node = getNode(selfUL, $node[0]);
				var $subtree = $c("ul", $li);
				if ($subtree.length) {
					node.children = [];
					_parseChildren(node.children, $subtree);
				}
				dataRows.push(node);
			});
		}

		if (nodeDIV) {
			var node = getNode(selfUL, nodeDIV);
			node.children = [];
			_parseChildren(node.children, $(nodeDIV).next());
			return node;
		} else {
			return null;
		}
	}

	function _getItem(rows, cord) {
		if (!rows)
			return null;

		var dot = cord.indexOf(",");

		var idx = cord;
		var nextCord;
		if (dot > 0) {
			idx = cord.substring(0, dot);
			if (cord.length >= dot + 1)
				nextCord = cord.substring(dot + 1);
		}

		var item = rows[idx._int()];

		// $log("ui.tree:_getItem: cord=[{1}], dot=[{2}], idx=[{3}], nextCord=[{4}], item.id=[{5}], item.url=[{6}]", 0, cord, dot, "" + idx, nextCord, item.id, item.url);

		if (nextCord) {
			return _getItem(item.children, nextCord);
		}

		return item;
	}

	function getNode(selfUL, nodeDIV) {
		if (!nodeDIV)
			return null;

		var opts = _options(selfUL);

		var $node = $(nodeDIV);
		var _cord_ = $node.attr("cord");
		var item = {};
		if (_cord_)
			item = _getItem(opts.data, _cord_);

		var node = $.extend({}, item, {
			target : nodeDIV,
			_cord_ : _cord_
		});

		if (opts.checkbox) {
			node.checked = $hc("TrC1", $f(".TrC", $node)) ? 1 : 0;
		}
		if (!isLeafNode(selfUL, nodeDIV)) {
			node.open = $hc("TrH-E", $f(".TrH", $node)) ? 1 : 0;
		}

		return node;
	}

	// function findNodeByText(selfUL, text) {
	// var $tree = $(selfUL);
	//
	// if ($.type(text.target) != "undefined") {
	// if (text.target)
	// $tree = $(text.target);
	// text = text.text;
	// }
	//
	// var $nodes = $f("div.TrN", $tree);
	// for ( var i = 0; i < $nodes.length; i++) {
	// var node = $d($nodes[i], "node");
	// if (node.text.toLowerCase() == text.toLowerCase()) {
	// return getNode(selfUL, $nodes[i]);
	// }
	// }
	//
	// return null;
	// }

	function findNode(selfUL, idOrConfig) {
		var $tree = $(selfUL);
		var field = "id";
		var value = idOrConfig;

		if ($.type(idOrConfig) == "object") {
			if (idOrConfig.target)
				$tree = $(idOrConfig.target);

			field = idOrConfig.field;
			value = idOrConfig.value;
		}

		var $node;
		if (field == "id")
			$node = $(selfUL).find("div.TrN[nid='" + value + "']");
		else
			$node = $(selfUL).find("div.TrN[title='" + value + "']");

		if ($node.length) {
			return getNode(selfUL, $node[0]);
		}

		return null;
	}

	function selectNode(selfUL, nodeDIV, ignoreOnSelect) {
		var selected = true;
		if ($.type(nodeDIV.target) != "undefined") {
			ignoreOnSelect = nodeDIV.ignoreOnSelect;
			nodeDIV = nodeDIV.target;
		}

		$rc("TrN-S", $("div.TrN-S", selfUL));

		if (!nodeDIV)
			return;

		var opts = _options(selfUL);
		var $node = $(nodeDIV);
		var node = getNode(selfUL, nodeDIV);
		if (!ignoreOnSelect && opts.onBeforeSelect.call(selfUL, node) == false) {
			return;
		}

		if (opts.checkbox && opts.checkOnSelect) {
			// checkNode(selfUL, nodeDIV, !$hc("TrC1",$f(".TrC",$node)));

			// always checked it when selecting
			checkNode(selfUL, nodeDIV, true);
		}

		$ac("TrN-S", $node);
		if (!ignoreOnSelect)
			opts.onSelect.call(selfUL, node);
	}

	function doKey(selfUL, key) {
		var $tree = $(selfUL);
		var opts = _options(selfUL);

		var $selectedNode = $f("div.TrN-S", $(selfUL));
		var $node = null;

		var nodeSelector = "div.TrN:visible";
		// if (opts.keywords && opts.keywords.length > 0) {
		// nodeSelector = "div.TrN-keywords:visible";
		// }

		var keyCode = jCocit.keyCode;
		switch (key) {
		case keyCode.PGUP:
		case keyCode.LEFT:
			// find parent node
			if ($selectedNode.length) {
				collapseNode(selfUL, $selectedNode[0]);
				var parent = getParentNode(selfUL, $selectedNode[0]);
				if (parent) {
					$node = $(parent.target);
				}
			} else {
				_findPrevVisibleRowNode();
			}
			break;
		case keyCode.PGDN:
		case keyCode.RIGHT:
			if ($selectedNode.length) {
				expandNode(selfUL, $selectedNode[0], function() {
					_findNextVisibleRowNode();
					if ($node && $node.length)
						selectNode(selfUL, $node[0]);
				});
				if (!$selectedNode.attr("url"))
					_findNextVisibleRowNode();
			} else
				_findNextVisibleRowNode();

			break;
		case keyCode.UP:
			_findPrevVisibleRowNode();

			break;
		case keyCode.DOWN:
			_findNextVisibleRowNode();

			break;
		}
		if ($node && $node.length) {
			selectNode(selfUL, $node[0]);
		}

		function _findNextVisibleRowNode() {
			if ($selectedNode.length) {

				// find first visible child of selected node
				var $subtree = $selectedNode.next("UL");
				if ($subtree.length) {
					$node = $f(nodeSelector + ":first", $subtree);

					// $log("ui.tree:_findNextVisibleRowNode: first child = " + $node.length);
				}

				// find next sibling of selected node
				if (!$node || !$node.length) {
					$node = $c(nodeSelector, $selectedNode.parent().nextAll("DIV>LI"));

					$log("ui.tree:_findNextVisibleRowNode: next sibling = " + $node.length);
				}

				// find next uncle of selected node
				if (!$node.length) {
					$node = _findNextUncle($selectedNode);

					$log("ui.tree:_findNextVisibleRowNode: next uncle = " + ($node ? $node.length : 0));
				}
			} else {

				// find first node of tree
				$node = $f(nodeSelector, $tree);
			}
		}

		function _findNextUncle($currentNode) {
			if (!$currentNode || !$currentNode.length)
				return null;

			// DIV > LI > UL -> prev
			var parent = getParentNode(selfUL, $currentNode[0]);
			if (parent) {
				var $parent = $(parent.target);
				var $nextUncle = $c(nodeSelector, $parent.parent().nextAll("LI"));
				if ($nextUncle.length) {
					return $nextUncle;
				}

				return _findNextUncle($parent);
			}

			return null;
		}

		function _findPrevVisibleRowNode() {
			if ($selectedNode.length) {

				// find the prev sibling of selected node
				$node = $c(nodeSelector, $selectedNode.parent().prevAll("LI"));

				$log("ui.tree:_findPrevVisibleRowNode: prev sibling = " + $node.length);

				if ($node.length)
					$node = $($node[0]);

				// find the last decendant of prev siblings of selected node
				if ($node.length) {
					var $subtree = $node.next("UL");
					if ($subtree.length) {
						var $last = $f(nodeSelector + ":last", $subtree);
						if ($last.length)
							$node = $last;

						$log("ui.tree:_findPrevVisibleRowNode: last decendant = " + $node.length);
					}
				}
				// find the parent of selected node
				else {
					var parent = getParentNode(selfUL, $selectedNode[0]);
					if (parent) {
						$node = $(parent.target);

						$log("ui.tree:_findPrevVisibleRowNode: parent = " + $node.length);
					}
				}

			} else {

				// find the last visible node of tree
				$node = $f(nodeSelector + ":last", $tree);
			}
		}
	}

	function _isLeafNode($node) {
		return $c("span.TrH", $node).length == 0;
	}

	function isLeafNode(selfUL, nodeDIV) {
		return _isLeafNode($(nodeDIV));
	}

	/**
	 * NOTE:
	 * <p>
	 * Comment doQuery method: because show/hide node, the performance is too lower. So using the following statement to do query.
	 * <p>
	 * $tree.tree({keywords:keywords});
	 */
	// function doQuery(selfUL, keywords) {
	// if ($.type(keywords) != "undefined" && keywords != null)
	// _options(selfUL).keywords = keywords;
	//
	// _doQuery(selfUL, selfUL, false);
	// }
	//
	// function _doQuery(selfUL, subtreeUL, ignoreEmptyKeywords) {
	// var time0 = new Date().getTime();
	//
	// var state = _state(selfUL);
	// var opts = _options(selfUL);
	// var $tree = $(selfUL);
	//
	// // ignore
	// if (ignoreEmptyKeywords && !opts.keywords)
	// return;
	//
	// var keywords = (opts.keywords || "").toHtml();
	//
	// // remote query by keywords
	// if (opts.mode == "remote") {
	// $tree.empty();
	// request(selfUL, selfUL, null, {
	// keywords : keywords
	// });
	// return;
	// }
	//
	// // local query by keywords
	// var $nodes = $f("div.TrN", $(subtreeUL));
	//
	// $log("ui.tree:doQuery: find nodes = {1}, title = {2}", time0, $nodes.length, selfUL.title || selfUL.id || "");
	//
	// if (keywords.length > 0) {
	//
	// // remember folder status
	// if (!state._expanded) {
	// state._expanded = [];
	// state._collapsed = [];
	// }
	// var remembered = $d(subtreeUL, "remembered");
	// if (!remembered) {
	// $d(subtreeUL, "remembered", true);
	// }
	//
	// // the nodes contains keywords used to onQuery call-back
	// var keywordsNodeDIVs = [];
	// var unsureFolderDIVs = [];
	//
	// $nodes.each(function() {
	// var $node = $(this);
	// var $nodeTitle = $c(".TrT", $node);
	//
	// // remember folder status
	// var isFolder = $c("span.TrH", $node).length > 0;
	// if (!remembered && isFolder) {
	// if ($c("span.TrH-E", $node).length)
	// state._expanded.push(this);
	// else
	// state._collapsed.push(this);
	// }
	//
	// var title = $node.attr("title");
	// var idx = title.toLowerCase().indexOf(keywords.toLowerCase());
	// if (idx > -1) {
	// // wrap keywords
	// var result = [];
	// while (idx > -1 && title.length >= keywords.length) {
	// var len = idx + keywords.length;
	// result.push(title.substring(0, idx).toHtml());
	// result.push('<span class="TrT-keywords">');
	// result.push(title.substring(idx, len).toHtml());
	// result.push("</span>");
	//
	// title = title.substring(len);
	// idx = title.toLowerCase().indexOf(keywords.toLowerCase());
	// }
	// result.push(title.toHtml());
	// $nodeTitle.html(result.join(''));
	//
	// // show node contains keywords
	// $node.addClass("TrN-keywords");
	// $node.css("display", "");
	// keywordsNodeDIVs.push(this);
	// } else {
	// // cancel keywords wrapper
	// $nodeTitle.html(title.toHtml());
	//
	// $node.removeClass("TrN-keywords");
	//
	// // hide leaf node don't contains keywords
	// if (isFolder) {
	// $node.css("display", "");
	//
	// if ($node.attr("url"))
	// $node.addClass("TrN-keywords");
	// else
	// unsureFolderDIVs.push(this);
	// } else {
	// $node.css("display", "none");
	// }
	// }
	// });
	//
	// // expand all match node
	// opts.onQuery.call(selfUL, keywords, keywordsNodeDIVs);
	//
	// // hide node NOT contains keywords
	// for ( var i = unsureFolderDIVs.length - 1; i >= 0; i--) {
	// var $folder = $(unsureFolderDIVs[i]);
	// var $subtree = $folder.next("UL");
	//
	// if ($subtree.length) {
	// if ($subtree.find(">DIV>LI>div.TrN-keywords").length) {
	// $folder.addClass("TrN-keywords");
	// $folder.css("display", "");
	// } else if (!$folder.hasClass("TrN-keywords"))
	// $folder.css("display", "none");
	//
	// } else {
	// $folder.css("display", "none");
	// }
	//
	// }
	// } else {
	// // restore folder status
	// if (state._expanded) {
	// for ( var i = state._expanded.length - 1; i >= 0; i--) {
	// expandNode(selfUL, state._expanded[i], null, true);
	// }
	// for ( var i = state._collapsed.length - 1; i >= 0; i--) {
	// collapseNode(selfUL, state._collapsed[i], true);
	// }
	// state._expanded = undefined;
	// state._collapsed = undefined;
	// $d(subtreeUL, "remembered", false);
	// }
	//
	// opts.onQuery.call(selfUL, keywords);
	//
	// $nodes.each(function() {
	// var $node = $(this);
	// $c(".TrT", $node).text($node.attr("title"));
	//
	// $node.removeClass("TrN-keywords");
	// $node.css("display", "");
	// });
	// }
	//
	// $log("ui.tree:doQuery: nodes = {1}, title = {2}", time0, $nodes.length, selfUL.title || selfUL.id || "");
	// }
	/**
	 * 1. Create tree UI object or set tree properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke tree method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.tree = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.tree.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.tree');
		}
		var options = options || {};
		return this.each(function() {
			var state = _state(this);
			var opts;
			if (state) {
				state.token = new Date().getTime();

				// remove old style name
				if (options.styleName && state.options.styleName) {
					$rc(state.options.styleName, state.tree);
				}

				opts = $.extend(state.options, options);
				state.options = opts;
			} else {
				opts = $.extend({}, $.fn.tree.defaults, $.fn.tree.parseOptions(this), options);
				state = {
					options : opts,
					tree : _init(this, opts)
				};
				state.token = new Date().getTime();
				_state(this, state);
				if (!opts.data) {
					var dataRows = _parseTreeDatas(this);
					if (dataRows.length)
						opts.data = dataRows;
				}
			}

			// set new style name
			if (opts.styleName)
				$ac(opts.styleName, state.tree);
			if (opts.checkLeftCls) {
				if (opts.checkbox)
					$ac(opts.checkLeftCls, state.tree);
				else
					$rc(opts.checkLeftCls, state.tree);
			}

			_bindTreeEvents(this);

			if (opts.data) {
				var $loading = $("<div></div>").css({
					width : 20,
					height : 20
				}).appendTo($("<li></li>").prependTo(state.tree.empty()));

				$ac("TrL", $loading);
				setTimeout(loadNodes, 100, this, this, opts.data, false, function(times) {
					if (times == 2)
						$rc("TrL", $loading);
				});
			} else {
				request(this, this);
			}
		});
	};

	$.fn.tree.methods = {
		/**
		 * Get tree options
		 */
		options : function(jq) {
			return _options(jq[0]);
		},
		/**
		 * Reload tree data
		 * <p>
		 * args: dataRows - this is specified tree array data
		 */
		loadData : function(jq, dataRows) {
			return jq.each(function() {
				loadNodes(this, this, dataRows);
			});
		},
		loadNodes : $X(loadNodes),
		/**
		 * Reload sub tree data specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified sub tree node DIV element
		 */
		reload : function(jq, nodeDIV) {
			return jq.each(function() {
				if (nodeDIV) {
					var $node = $(nodeDIV);
					var hit = $c("span.TrH", $node);
					$ac("TrH-C", $rc("TrH-E TrH-EH", hit));
					$node.next().remove();
					expandNode(this, nodeDIV);
				} else {
					$(this).empty();
					request(this, this);
				}
			});
		},
		/**
		 * Get node data without descendant nodes specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 * <p>
		 * return: node JSON data without descendant nodes
		 */
		getNode : $x(getNode),
		/**
		 * Get branch node data with all descendants nodes specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 * <p>
		 * return: node JSON data with all descendants nodes
		 */
		getBranch : $x(getBranchNode),
		/**
		 * Get root node
		 * <p>
		 * return: node JSON data
		 */
		getRoot : $x(getRootNode),
		/**
		 * Get roots nodes
		 * <p>
		 * return: nodes JSON array data
		 */
		getRoots : $x(getRootNodes),
		/**
		 * Get parent node of the current node specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 * <p>
		 * return: node JSON data
		 */
		getParent : $x(getParentNode),
		/**
		 * Get descendants nodes specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 * <p>
		 * return: nodes JSON array data
		 */
		getDescendants : $x(getDescendantsNodes),
		/**
		 * Get children nodes specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 * <p>
		 * return: nodes JSON array data
		 */
		getChildren : $x(getChildren),
		/**
		 * Get checked nodes
		 * <p>
		 * return: nodes JSON array data
		 */
		getChecked : $x(getChecked),
		/**
		 * Get selected node
		 * <p>
		 * return: node JSON data
		 */
		getSelected : $x(getSelected),
		/**
		 * Get checked node values
		 * <p>
		 * return: nodes JSON array data
		 */
		getValues : $x(getCheckedValues),
		/**
		 * Get selected node value
		 * <p>
		 * return: node JSON data
		 */
		getValue : $x(getSelectedValue),
		/**
		 * Check whether the node is leaf node specified by "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 * <p>
		 * return: true means that the specified node is leaf node, otherwise it is a folder node.
		 */
		isLeaf : $x(isLeafNode),
		/**
		 * Find tree node specified by node id
		 * <p>
		 * args: id - this is specified tree node id or field config {target:UL, field:"text", value:"Root"}
		 * <p>
		 * return: tree node JSON data {target: nodeDIV, ...}
		 */
		find : $x(findNode),
		/**
		 * Select tree node specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 */
		select : $X(selectNode),
		/**
		 * Process Navigation keys. Select tree node specified by navigation key. eg: UP/DOWN/HOME/END/PgUp/PgDn
		 * <UL>
		 * <LI>UP: select the prev visible row node
		 * <LI>DOWN: Select the next visible row node
		 * <LI>HOME: Select the first root node
		 * <LI>END: Select the last visible row node
		 * <LI>PGUP: Collapse the current folder and Select parent folder.
		 * <LI>PGDN: Expand the current folder and Select the next visible folder node.
		 * </UL>
		 */
		doKey : $X(doKey),
		/**
		 * cancel selected node status
		 */
		unselect : function(jq) {
			return jq.each(function() {
				// not specified node means unselect the current selected node
				selectNode(jq[0]);
			});
		},
		/**
		 * Check tree node specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 */
		check : $X(checkNode, true),
		/**
		 * Uncheck tree node specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 */
		uncheck : $X(checkNode, false),
		/**
		 * Collpase sub tree node specified by "nodeDIV", don't contains descendants nodes
		 * <p>
		 * args: nodeDIV - this is specified sub tree node DIV element
		 */
		collapse : $X(collapseNode),
		/**
		 * Expand sub tree node specified by "nodeDIV", don't contains descendants nodes
		 * <p>
		 * args: nodeDIV - this is specified sub tree node DIV element
		 */
		expand : $X(expandNode),
		/**
		 * Collpase sub tree all descendants nodes specified by "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified sub tree node DIV element
		 */
		collapseAll : $X(collapseAllNodes),
		/**
		 * Expand sub tree all descendants nodes specified by "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified sub tree node DIV element
		 */
		expandAll : $X(expandAllNodes),
		/**
		 * Expand all super node specified by "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified sub tree node DIV element
		 */
		expandTo : $X(expandToNode),
		/**
		 * Toggle sub tree node specified by "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified sub tree node DIV element
		 */
		toggle : $X(toggleNode),
		/**
		 * 
		 * NOTE:
		 * <p>
		 * Comment doQuery method: because show/hide node, the performance is too lower. So using the following statement to do query.
		 * <p>
		 * $tree.tree({keywords:keywords});
		 * 
		 * <p>
		 * Search tree:
		 * <p>
		 * args: keywords - this argument is empty means that cancel query
		 */
		// query : $X(doQuery),
		/**
		 * Enable drag tree node
		 */
		enableDnd : $n,
		/**
		 * Disable drag tree node
		 */
		disableDnd : $n,
		_refreshJoinLines : $X(_refreshJoinLines)
	};

	/**
	 * Parse node data from "LI" element
	 */
	function _parseNodeOptions(selfUL) {
		return $.extend({}, jCocit.parseOptions(selfUL, [ "id", "iconCls", {
			open : "n",
			checked : "b"
		} ]));
	}

	$.fn.tree.parseOptions = function(selfUL) {
		return $.extend({}, jCocit.parseOptions(selfUL, [ "width", "height", "url", "method", "styleName", "keywords", {
			checkbox : "b",
			checkLeftCls : "b",
			cascadeCheck : "b",
			onlyLeafCheck : "b",
			checkOnSelect : "b",
			onlyLeafValue : "b",
			editable : "b",
			pageSize : "n",
			checkAll : "n",
			openAll : "n"
		}, {
			animate : "b",
			lines : "b",
			dnd : "b"
		} ]));
	}

	$.fn.tree.defaults = {
		/**
		 * the URL is remote address used to ajax load tree data.
		 */
		url : null,
		/**
		 * this is request method, used to request data from remote server.
		 * <p>
		 * options are "post/get" etc.
		 */
		method : "post",
		/**
		 * animate is true means that the sub tree will be expanded/collapsed with animation
		 */
		animate : false,
		/**
		 * this is initialized tree JSON data
		 */
		data : null,
		/**
		 * one by one render nodes specified by "pageSize" for big data, remaining rows will be render once.
		 * <p>
		 * NOTE: the call-back function will be invoked 2 times.
		 */
		pageSize : 50,
		width : "auto",
		height : "auto",
		/**
		 * keywords existed means that the data will be filter by keywords before tree be drawn.
		 */
		keywords : "",

		/*
		 * checkbox options
		 */
		/**
		 * true means that the tree contains checkbox before title.
		 */
		checkbox : false,
		/**
		 * this is class name means that the checkbox on the left side of icon
		 */
		checkLeftCls : "Tr-CL",
		/**
		 * true means that the all children will be cascade checked/unchecked after the folder checkbox is checked/unchecked
		 * <p>
		 * false means that the all children will be cascade checked/unchecked only after the folder checkbox is double clicked.
		 */
		cascadeCheck : true,
		/**
		 * true means that only the leaf node can be checked, the folder checkbox NOT existed.
		 */
		onlyLeafCheck : false,
		// /**
		// * true means that the node will be selected/unselected only when checked/unchecked node
		// */
		// selectOnCheck : false,
		/**
		 * true means that the node will be checked/unchecked if selected/unselected node
		 */
		checkOnSelect : false,
		/**
		 * true means that only the leaf node(s) can be selected and only the leaf node value(s) will be return when invoking getChecked.
		 */
		onlyLeafValue : false,
		/**
		 * force to set tree node checked status, this value will instead of checked value of data.
		 * <p>
		 * 1: checked, 0: unchecked, -1: using checked value of data.
		 */
		checkAll : -1,
		/**
		 * force to set tree folder node open status, this value will instead of state value of data.
		 * <p>
		 * 1: open, 0: closed, -1: using state value of data
		 */
		openAll : -1,

		/*
		 * style options
		 */
		/**
		 * style name will be add to the tree class name.
		 */
		styleName : null,
		/**
		 * true means that tree node join lines will be shown, it will be combined with "styleName" to use(For examples: styleName equals "tree-lines", lines equals true).
		 * <p>
		 * NOTE: not support dynamic setting, only support to initialize setting.
		 */
		lines : false,
		/**
		 * false means that the node NOT support indent.
		 */
		indent : true,
		/**
		 * row height used to evaluate subtree min height.
		 */
		rowHeight : 20,
		/**
		 * max query depth used to auto load asyn subtree when doing query.
		 */
		maxQueryDepth : 5,
		/**
		 * auto expand closed folder when doing query.
		 */
		openOnQuery : false,
		/**
		 * auto load asyn folder from remote when doing query
		 */
		loadOnQuery : true,

		loader : function(folderNode, queryData, onLoadSuccess, onLoadError) {
			var opts = $(this).tree("options");

			var url = opts.url;

			if (folderNode && $.type(folderNode.url) == "string")
				url = folderNode.url;
			if (!url)
				return false;

			$log("ui.tree:loader: doAjax... url=[" + url + "]");

			$.doAjax({
				type : opts.method,
				url : url,
				data : queryData,
				dataType : "json",
				success : function(node) {
					onLoadSuccess(node);
				},
				error : function() {
					onLoadError.apply(this, arguments);
				}
			});
		},
		dataFilter : function(token, dataRows, nodeDIV) {
			var opts = $(this).tree("options"), folderNodeCord = "", state = _state(this);

			if (nodeDIV) {
				var folderNode = getNode(this, nodeDIV);
				folderNodeCord = folderNode._cord_ || "";
			}

			var k = opts.keywords;
			if (k && k.length > 0) {
				var time0 = new Date().getTime();

				var ret = _filter(token, dataRows, folderNodeCord);

				// $log("ui.tree:dataFilter: keywords=[{1}]", time0, k);
				if (token == state.token)
					return ret;
				else
					return [];

			} else
				return dataRows;

			function _filter(token, rows, itemCordPrefix) {
				var newRows = [];
				var i = 0, item, newItem, cord, newChildren;
				for (; i < rows.length; i++) {
					if (token != state.token)
						return null;

					item = rows[i];
					newItem = {};
					cord = (itemCordPrefix ? (itemCordPrefix + ",") : "") + i;
					newChildren = null;

					if (item.children) {
						if ($.type(item.children) == "string")
							newChildren = item.children;
						else
							newChildren = _filter(token, item.children, cord);
					}

					if (item.text.toLowerCase().indexOf(k.toLowerCase()) > -1 || $.type(newChildren) == "string" || (newChildren && newChildren.length > 0)) {
						newItem.id = item.id;
						newItem.text = item.text;
						newItem._cord_ = cord;
						if (item.iconCls)
							newItem.iconCls = item.iconCls;
						if (newChildren)
							newItem.children = newChildren;

						newRows.push(newItem);
					}
				}

				return newRows;
			}
		},
		titleFormater : function(item, keywords) {
			if (keywords && keywords.length > 0) {
				var title = item.text;
				var idx = title.toLowerCase().indexOf(keywords.toLowerCase());

				var result = [];

				// wrap keywords
				if (idx > -1) {
					while (idx > -1 && title.length >= keywords.length) {
						var len = idx + keywords.length;
						result.push(title.substring(0, idx).toHtml());
						result.push('<span class="TrT-keywords">');
						result.push(title.substring(idx, len).toHtml());
						result.push("</span>");

						title = title.substring(len);
						idx = title.toLowerCase().indexOf(keywords.toLowerCase());
					}
					result.push(title.toHtml());

					return result.join('');

				} else if (item.children) {
					result.push("<i>");
					result.push(title.toHtml());
					result.push("</i>");

					return result.join("");
				} else {

					return false;
				}
			}

			return item.text.toHtml();
		},
		/**
		 * This call-back function will be invoked before the sub tree be loaded.
		 * <p>
		 * <b>args:</b>
		 * <UL>
		 * <LI>node: this is sub tree node
		 * <LI>requsetData: this is query string
		 * </UL>
		 */
		onBeforeLoad : $n,
		/**
		 * This call-back function will be invoked when the sub tree be loaded success.
		 * <p>
		 * <b>args:</b>
		 * <UL>
		 * <LI>node: this is sub tree node
		 * <LI>dataRows: this is sub tree data
		 * </UL>
		 */
		onLoadSuccess : $n,
		/**
		 * This call-back function will be invoked when the sub tree be loaded error.
		 */
		onLoadError : $n,
		/**
		 * This call-back function will be invoked when the node be clicked.
		 * <p>
		 * args: node - this is node JSON data
		 */
		onClick : $n,
		/**
		 * This call-back function will be invoked when the node be double clicked.
		 * <p>
		 * args: node - this is node JSON data
		 */
		onDblClick : $n,
		/**
		 * This call-back function will be invoked when the node title be clicked.
		 * <p>
		 * args: node - this is node JSON data
		 */
		onClickTitle : $n,
		/**
		 * This call-back function will be invoked before the node be expaned.
		 * <p>
		 * args: node - this is node JSON data
		 */
		onBeforeExpand : $n,
		/**
		 * This call-back function will be invoked after the node be expaned.
		 * <p>
		 * args: node - this is node JSON data
		 * <p>
		 * times: 0-node unloaded, 1-loaded nodes count specified by options.pageSize. 2-all nodes loaded.
		 */
		onExpand : $n,
		/**
		 * This call-back function will be invoked before the node be collapsed.
		 * <p>
		 * args: node - this is node JSON data
		 */
		onBeforeCollapse : $n,
		/**
		 * This call-back function will be invoked after the node be collapsed.
		 * <p>
		 * args: node - this is node JSON data
		 */
		onCollapse : $n,
		/**
		 * This call-back function will be invoked before the node be checked
		 * <p>
		 * <b>args:</b>
		 * <UL>
		 * <LI>node: this is node JSON data, it will be checked
		 * <LI>checked: true means that the node is checked, otherwise unchecked
		 * </UL>
		 */
		onBeforeCheck : $n,
		/**
		 * This call-back function will be invoked when the node be checked
		 * <p>
		 * <b>args:</b>
		 * <UL>
		 * <LI>node: this is checked node
		 * <LI>checked: true means that the node is checked, otherwise unchecked
		 * </UL>
		 */
		onCheck : $n,
		/**
		 * This call-back function will be invoked before the node be selected
		 * <p>
		 * args: node - this node will be selected
		 */
		onBeforeSelect : $n,
		/**
		 * This call-back function will be invoked after the node be selected
		 * <p>
		 * args: node - this is selected node
		 */
		onSelect : $n,
		/**
		 * This call-back function will be invoked when context menu on the node
		 * <p>
		 * args: node - this is target node
		 */
		onContextMenu : $n,
		onEndQuery : function(subtreeUL) {
			var i = 0, opts = _options(this), $list = $(this).find("div.TrN");
			for (; i < $list.length; i++) {
				if (i >= opts.pageSize)
					break;

				expandNode(this, $list[i], null, true);
			}
		}
	};
})(jQuery, jCocit);
