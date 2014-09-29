(function($, jCocit) {

	function _options(selfUL) {
		return $d(selfUL, "tree").options;
	}

	/**
	 * Append nodes to tree.
	 */
	function appendNodes(selfUL, nodeConfig) {
		var $parent = $(nodeConfig.parent);
		var $subtree;
		if ($parent.length == 0) {
			$subtree = $(selfUL);
		} else {
			$subtree = $parent.next();
			if ($subtree.length == 0) {
				$subtree = $("<ul></ul>").insertAfter($parent);
			}
		}
		if (nodeConfig.data && nodeConfig.data.length) {
			var $icon = $f("span.TrI", $parent);
			if ($hc("TrI-L", $icon)) {
				$ac("TrI-F TrI-FO", $rc("TrI-L", $icon));
				var hit = $ac("TrH-E", $('<span class="TrH"></span>')).insertBefore($icon);
				if (hit.prev().length) {
					hit.prev().remove();
				}
			}
		}
		$(selfUL).tree("loadNodes", {
			target : $subtree[0],
			data : nodeConfig.data,
			append : true
		});
		_refreshCheckbox(selfUL, $subtree.prev());
	}

	function _checkNode(selfUL, nodeDIV, checked) {
		return $(selfUL).tree(checked ? "check" : "uncheck", nodeDIV);
	}

	function _refreshCheckbox(selfUL, nodeDIV) {
		var opts = _options(selfUL);
		var $node = $(nodeDIV);
		if ($(selfUL).tree("isLeaf", nodeDIV)) {
			var $checkbox = $f(".TrC", $node);
			if ($checkbox.length) {
				if ($hc("TrC-1", $checkbox))
					_checkNode(selfUL, nodeDIV, true);
				else
					_checkNode(selfUL, nodeDIV, false);

			} else {
				if (opts.onlyLeafCheck)
					$ac("TrC-0", $('<span class="TrC"></span>')).insertBefore($f(".TrT", $node));

			}
		} else {
			var $checkbox = $f(".TrC", $node);
			if ($checkbox.length > 0 && opts.onlyLeafCheck) {
				if (opts.checkLeftCls)
					$ac("TrN-no-checkbox", $node);

				$checkbox.remove();
			} else {
				if ($hc("TrC-1", $checkbox)) {
					_checkNode(selfUL, nodeDIV, true);
				} else {
					if ($hc("TrC-2", $checkbox)) {
						var checked = true;
						var unchecked = true;
						var nodes = $(selfUL).tree("getDescendants", nodeDIV);
						for ( var i = 0; i < nodes.length; i++) {
							if (nodes[i].checked) {
								unchecked = false;
							} else {
								checked = false;
							}
						}
						if (checked) {
							_checkNode(selfUL, nodeDIV, true);
						}
						if (unchecked) {
							_checkNode(selfUL, nodeDIV, false);
						}
					}
				}
			}
		}
	}

	/**
	 * Insert nodes to tree.
	 */
	function insertNode(selfUL, nodeConfig) {
		var refDIV = nodeConfig.before || nodeConfig.after;
		var refParentNode = $(selfUL).tree("getParent", refDIV);
		var $newLI;
		if (refParentNode) {
			appendNodes(selfUL, {
				parent : refParentNode.target,
				data : [ nodeConfig.data ]
			});
			$newLI = $c("li:last", $(refParentNode.target).next());
		} else {
			appendNodes(selfUL, {
				parent : null,
				data : [ nodeConfig.data ]
			});
			$newLI = $c("li:last", $(selfUL));
		}
		if (nodeConfig.before) {
			$newLI.insertBefore($p($(refDIV)));
		} else {
			$newLI.insertAfter($p($(refDIV)));
		}
	}

	/**
	 * Remove node from tree
	 */
	function removeNode(selfUL, nodeDIV) {
		var parentNode = $(selfUL).tree("getParent", nodeDIV);
		var $node = $(nodeDIV);
		var $li = $p($node);
		var $subtree = $p($li);
		$li.remove();
		if ($c("li", $subtree).length == 0) {
			var $node = $subtree.prev();
			$ac("TrI-L", $rc("TrI-F", $f(".TrI", $node)));
			$f(".TrH", $node).remove();
			$('<span class="TrD"></span>').prependTo($node);
			if ($subtree[0] != selfUL) {
				$subtree.remove();
			}
		}
		if (parentNode) {
			_refreshCheckbox(selfUL, parentNode.target);
		}
		$(selfUL).tree("_refreshJoinLines", selfUL);
	}

	function _getNode(selfUL, node) {
		return $(selfUL).tree("getNode", node);
	}

	function updateNode(selfUL, newNode) {
		var $node = $(newNode.target);
		var oldNode = _getNode(selfUL, newNode.target);
		if (oldNode.iconCls) {
			$rc(oldNode.iconCls, $f(".TrI", $node));
		}
		var node = $.extend({}, oldNode, newNode);
		$d(newNode.target, "TrN", node);
		$node.attr("node-id", node.id);
		$f(".TrT", $node).html(node.text);
		if (node.iconCls) {
			$ac(node.iconCls, $f(".TrI", $node));
		}
		if (oldNode.checked != node.checked) {
			_checkNode(selfUL, newNode.target, node.checked);
		}
	}

	function beginEdit(selfUL, nodeDIV) {
		var opts = _options(selfUL);
		var node = _getNode(selfUL, nodeDIV);
		if (opts.onBeforeEdit.call(selfUL, node) == false) {
			return;
		}
		$(nodeDIV).css("position", "relative");
		var $title = $f(".TrT", $(nodeDIV));
		var width = $ow($title);
		$title.empty();
		var $nodeEditor = $("<input class=\"TrE\">").appendTo($title);
		$nodeEditor.val(node.text).focus();
		$w(width + 20, $nodeEditor);
		$h(document.compatMode == "CSS1Compat" ? (18 - ($oh($nodeEditor) - $h($nodeEditor))) : 18, $nodeEditor);
		$nodeEditor.bind("click", function(e) {
			return false;
		}).bind("mousedown", function(e) {
			e.stopPropagation();
		}).bind("mousemove", function(e) {
			e.stopPropagation();
		}).bind("keydown", function(e) {
			if (e.keyCode == 13) {
				endEdit(selfUL, nodeDIV);
				return false;
			} else {
				if (e.keyCode == 27) {
					cancelEdit(selfUL, optsd8);
					return false;
				}
			}
		}).bind("blur", function(e) {
			e.stopPropagation();
			endEdit(selfUL, nodeDIV);
		});
	}

	function endEdit(selfUL, nodeDIV) {
		var opts = _options(selfUL);
		$(nodeDIV).css("position", "");
		var $titleEditor = $f("input.TrE", $(nodeDIV));
		var val = $titleEditor.val();
		$titleEditor.remove();
		var node = _getNode(selfUL, nodeDIV);
		node.text = val;
		updateNode(selfUL, node);
		opts.onAfterEdit.call(selfUL, node);
	}

	function cancelEdit(selfUL, nodeDIV) {
		var $self = $(selfUL);
		var opts = _options(selfUL);
		$self.css("position", "");
		$f("input.TrE", $self).remove();
		var node = _getNode(selfUL, nodeDIV);
		updateNode(selfUL, node);
		opts.onCancelEdit.call(selfUL, node);
	}

	function disableDnd(selfUL) {
		var $node = $f("div.TrN", $(selfUL));
		$node.draggable("disable");
		// $node.css("cursor", "pointer");
	}

	function enableDnd(selfUL) {
		var state = $d(selfUL, "tree");
		var opts = state.options;
		var $tree = state.tree;
		state.disabledDroppableNodes = [];

		// create node draggable object
		$f("div.TrN", $tree).draggable({
			disabled : false,
			revert : true,
			cursor : "pointer",
			proxy : function(nodeDIV) {
				var $proxyNode = $('<div class="TrP"><span class="TrP-N TrP-I">&nbsp;</span></div>').appendTo("body");
				$proxyNode.append($f(".TrT", $(nodeDIV)).html());
				$proxyNode.hide();
				return $proxyNode;
			},
			deltaX : 15,
			deltaY : 15,
			onBeforeDrag : function(e) {
				var $this = $(this);
				var $eventTarget = $(e.target);

				// call-back onBeforeDrag function
				if (opts.onBeforeDrag.call(selfUL, _getNode(selfUL, this)) == false) {
					return false;
				}

				// cannot drag "+/-" icon or checkbox
				if ($hc("TrH", $eventTarget) || $hc("TrC", $eventTarget)) {
					return false;
				}

				// the pressing mouse key is not left
				if (e.which != 1) {
					return false;
				}

				// the node cannot be dragged into it's children nodes
				$f("div.TrN", $this.next("ul")).droppable({
					accept : "no-accept"
				});

				// get indent
				var $indent = $f("span.TrD", $this);
				if ($indent.length) {
					e.data.offsetWidth -= $indent.length * $w($indent);
				}

			},
			onStartDrag : function() {
				$(this).draggable("proxy").css({
					left : -10000,
					top : -10000
				});

				// call-back onStartDrag function
				opts.onStartDrag.call(selfUL, _getNode(selfUL, this));

				//
				var node = _getNode(selfUL, this);
				if (node.id == undefined) {
					node.id = "jCocit_tree_node_id_temp";
					updateNode(selfUL, node);
				}

				// set dragging node id
				state.draggingNodeId = node.id;
			},
			onDrag : function(e) {
				var x1 = e.pageX, y1 = e.pageY, x2 = e.data.startX, y2 = e.data.startY;
				var d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
				if (d > 3) {
					$(this).draggable("proxy").show();
				}
				this.pageY = e.pageY;
			},
			onStopDrag : function() {
				$f("div.TrN", $(this).next("ul")).droppable({
					accept : "div.TrN"
				});
				for ( var i = 0; i < state.disabledDroppableNodes.length; i++) {
					$(state.disabledDroppableNodes[i]).droppable("enable");
				}
				state.disabledDroppableNodes = [];
				var node = $(selfUL).tree("find", state.draggingNodeId);
				if (node.id == "jCocit_tree_node_id_temp") {
					node.id = "";
					updateNode(selfUL, node);
				}
				opts.onStopDrag.call(selfUL, node);
			}
		}).droppable({
			accept : "div.TrN",
			onDragEnter : function(e, draggingNodeDIV) {
				if (opts.onDragEnter.call(selfUL, this, _getNode(selfUL, draggingNodeDIV)) == false) {
					var $this = $(this);
					_refreshProxyNode(nodeDIV, false);
					$rc("TrP-A TrP-T TrP-B", $this);
					$this.droppable("disable");
					state.disabledDroppableNodes.push(this);
				}
			},
			onDragOver : function(e, draggingNodeDIV) {
				var $this = $(this);
				if ($this.droppable("options").disabled) {
					return;
				}
				var pageY = draggingNodeDIV.pageY;
				var top = $this.offset().top;
				var h = top + $oh($this);
				_refreshProxyNode(draggingNodeDIV, true);
				$rc("TrP-A TrP-T TrP-B", $this);
				if (pageY > top + (h - top) / 2) {
					if (h - pageY < 5)
						$ac("TrP-B", $this);
					else
						$ac("TrP-A", $this);

				} else {
					if (pageY - top < 5)
						$ac("TrP-T", $this);
					else
						$ac("TrP-A", $this);

				}
				if (opts.onDragOver.call(selfUL, this, _getNode(selfUL, draggingNodeDIV)) == false) {
					_refreshProxyNode(draggingNodeDIV, false);
					$rc("TrP-A TrP-T TrP-B", $this);
					$this.droppable("disable");
					state.disabledDroppableNodes.push(this);
				}
			},
			onDragLeave : function(e, draggingNodeDIV) {
				_refreshProxyNode(draggingNodeDIV, false);
				$rc("TrP-A TrP-T TrP-B", $(this));
				opts.onDragLeave.call(selfUL, this, _getNode(selfUL, draggingNodeDIV));
			},
			onDrop : function(e, draggingNodeDIV) {
				var $this = $(this);
				var droppingNodeDIV = this;
				var _move, position;
				if ($hc("TrP-A", $this)) {
					_move = _moveInto;
				} else {
					_move = _moveTo;
					position = $hc("TrP-T", $this) ? "top" : "bottom";
				}
				_move(draggingNodeDIV, droppingNodeDIV, position);
				$rc("TrP-A TrP-T TrP-B", $this);
			}
		});

		function _refreshProxyNode(droggingNode, yes) {
			var $dndIcon = $f("span.TrP-I", $(droggingNode).draggable("proxy"));
			$ac(yes ? "TrP-Y" : "TrP-N", $rc("TrP-Y TrP-N", $dndIcon));
		}

		function _moveInto(nodeDIV, parentNodeDIV) {
			if (_getNode(selfUL, parentNodeDIV).state == "closed") {
				$(selfUL).tree("_expand", {
					target : parentNodeDIV,
					callback : function() {
						_appendTo();
					}
				});
			} else {
				_appendTo();
			}

			function _appendTo() {
				var branchNode = $(selfUL).tree("pop", nodeDIV);
				$(selfUL).tree("append", {
					parent : parentNodeDIV,
					data : [ branchNode ]
				});
				opts.onDrop.call(selfUL, parentNodeDIV, branchNode, "append");
			}

		}

		function _moveTo(nodeDIV, refNodeDIV, position) {
			var nodeConfig = {};
			if (position == "top") {
				nodeConfig.before = refNodeDIV;
			} else {
				nodeConfig.after = refNodeDIV;
			}
			var branchNode = $(selfUL).tree("pop", nodeDIV);
			nodeConfig.data = branchNode;
			$(selfUL).tree("insert", nodeConfig);
			opts.onDrop.call(selfUL, refNodeDIV, branchNode, position);
		}

	}

	$.extend($.fn.tree.methods, {
		/**
		 * Append tree node into position specified by argument "nodeConfig"
		 * <p>
		 * args: nodeConfig - this is node JSON data configuration
		 */
		append : $X(appendNodes),
		/**
		 * Insert tree node into position specified by argument "nodeConfig"
		 * <p>
		 * args: nodeConfig - this is node JSON data configuration
		 */
		insert : $X(insertNode),
		/**
		 * Remove tree node specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 */
		remove : $X(removeNode),
		/**
		 * Pop-up specified branch node datas contains all descendants
		 * <p>
		 * args: nodeDIV - this is specified tree node DIV element
		 */
		pop : function(jq, nodeDIV) {
			var branchNode = jq.tree("getBranch", nodeDIV);
			removeNode(jq[0], nodeDIV);
			return branchNode;
		},
		/**
		 * Update tree node with node data specified by argument "node"
		 * <p>
		 * args: node - this is new node JSON data
		 */
		update : $X(updateNode),
		/**
		 * Enable drag tree node
		 */
		enableDnd : $X(enableDnd),
		/**
		 * Disable drag tree node
		 */
		disableDnd : $X(disableDnd),
		/**
		 * Begin edit tree node title specified by argument "nodeDIV"
		 * <p>
		 * args: nodeDIV - this is node DIV element
		 */
		beginEdit : $X(beginEdit),
		/**
		 * End edit tree node title
		 * <p>
		 * args: nodeDIV - this is node DIV element
		 */
		endEdit : $X(endEdit),
		/**
		 * Cancel edit tree node title
		 * <p>
		 * args: nodeDIV - this is node DIV element
		 */
		cancelEdit : $X(cancelEdit)
	});

	// extends default settings
	$.extend($.fn.tree.defaults, {
		/**
		 * editable is true means that the tree node title can be edit.
		 */
		editable : false,
		/**
		 * dnd is true means that the tree node can be drag.
		 */
		dnd : false,
		/**
		 * This call-back function will be invoked before dragging is started
		 * <p>
		 * args: node - this is dragging source node
		 */
		onBeforeDrag : $n,
		/**
		 * This call-back function will be invoked after dragging is started
		 * <p>
		 * args: node - this is dragging source node
		 */
		onStartDrag : $n,
		/**
		 * This call-back function will be invoked after dragging is stoped
		 * <p>
		 * args: node - this is dragging source node
		 */
		onStopDrag : $n,
		/**
		 * This call-back function will be invoked after mouse enter source node.
		 * <p>
		 * <B>args: </B>
		 * <UL>
		 * <LI>droppingNodeDIV: this is target node.
		 * <LI>draggingNode: this is dragging source node that will be moved into target node
		 */
		onDragEnter : $n,
		/**
		 * This call-back function will be invoked after mouse over source node.
		 * <p>
		 * <B>args: </B>
		 * <UL>
		 * <LI>droppingNodeDIV: this is target node.
		 * <LI>draggingNode: this is dragging source node that will be moved into target node
		 */
		onDragOver : $n,
		/**
		 * This call-back function will be invoked when mouse leave source node.
		 * <p>
		 * <B>args: </B>
		 * <UL>
		 * <LI>droppingNodeDIV: this is target node.
		 * <LI>draggingNode: this is dragging source node that will be moved into target node
		 */
		onDragLeave : $n,
		/**
		 * This call-back function will be invoked after tree node be dropped into other node.
		 * <p>
		 * <B>args: </B>
		 * <UL>
		 * <LI>droppingNodeDIV: this is target node.
		 * <LI>branchNode: this is dragging source node that will be moved into target node
		 * <LI>position: options value are "append/after/before"
		 * </UL>
		 */
		onDrop : $n,
		/**
		 * This call-back function will be invoked before tree node start edit.
		 * <p>
		 * args: node - this is tree node JSON data.
		 */
		onBeforeEdit : $n,
		/**
		 * This call-back function will be invoked after tree node end edit.
		 * <p>
		 * args: node - this is tree node JSON data.
		 */
		onAfterEdit : $n,
		/**
		 * This call-back function will be invoked after tree node be cancel edit.
		 * <p>
		 * args: node - this is tree node JSON data.
		 */
		onCancelEdit : $n
	});

})(jQuery, jCocit);
