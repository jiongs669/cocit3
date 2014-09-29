(function($, jCocit) {

	function _init(selfUL) {
		var $tree = $(selfUL);
		var state = $d(selfUL, "listtree");
		var opts = state.options;

		$tree.addClass("listtree");

		$tree.tree($.extend({}, opts, {
			checkLeftCls : "listtree-CL",
			openAll : 0,
			checkAll : 1,
			rowHeight : 21,
			indent : false,
			openOnQuery : true,
			loadOnQuery : true,
			/**
			 * Load success: expand the current node or root node
			 */
			onLoadSuccess : function(node, dataArray, times) {
				// collapse all
				// var $subtree = null;
				// if (node) {
				// $subtree = $(node.target).next("ul");
				// }
				//
				// for ( var i = 0; i < dataArray.length; i++) {
				// var oneData = dataArray[i];
				// var oneNode = $tree.tree("find", {
				// field : "id",
				// value : oneData.id,
				// target : $subtree
				// });
				// if (oneNode)
				// $tree.tree("collapseAll", oneNode.target);
				// }

				// forward to root
				if (!node && times == 2 && !$d(selfUL, "tree").options.keywords)
					forwardTo(selfUL, null);

				opts.onLoadSuccess.call(this, node);
			},
			onBeforeCollapse : function(node) {
				return false;
			},
			onExpand : function(node, times) {
				if ($tree.tree("isLeaf", node.target))
					return;

				$log("ui.listtree:onExpand: node=[" + node.text + "]");

				if ($.type(times) == "undefined" || times == 2)
					forwardTo(selfUL, node, times);
				else
					$(node.target).next("UL").css("display", "none");

				opts.onExpand.call(this, node, times);
			},
			onSelect : function(node) {
				$log("ui.listtree:onSelect: node=[" + node.text + "]");

				forwardTo(selfUL, $tree.tree("getParent", node.target));
				_scrollToNode();

				function _scrollToNode() {
					var $node = $(node.target);
					// scroll to target node.
					var $treeContainer = $tree.parent();
					if ($node.position().top <= 0) {
						var h = $treeContainer.scrollTop() + $node.position().top;
						$treeContainer.scrollTop(h);
					} else {
						if ($node.position().top + $oh($node) > $h($treeContainer)) {
							var h = $treeContainer.scrollTop() + $node.position().top + $oh($node) - $h($treeContainer);
							$treeContainer.scrollTop(h);
						}
					}
				}

				opts.onSelect.call(this, node);
			},
			onDblClick : function(node) {
				$tree.tree("expand", node.target);

				opts.onDblClick.call(this, node);
			},
			onEndQuery : function(subtreeUL) {
				if ($.type(opts._originalFolder) == 'undefined') {
					opts._originalFolder = opts._currentFolder || null;
				}

				// $log("ui.listtree:onEndQuery: keywords=[" + $d(selfUL, "tree").options.keywords + "], subtreeUL=" + subtreeUL);

				if (opts.keywords && opts.keywords.trim().length == 0) {

					forwardTo(this, opts._originalFolder);
					opts._originalFolder = undefined;

				} else {

					var $subtree = $(subtreeUL);
					var $tree = $(this);

					if (subtreeUL != this) {
						var len = _collapseAll($subtree);
						$log("ui.listtree:onEndQuery: _collapseAll! $subtree=[{1}], len={2}", 0, $subtree.prev("div.TrN").attr("title") || "", len);
					}

					var $keywordsResult = $tree.find("span.TrT-keywords:first");

					if ($keywordsResult.length) {
						var $node = $keywordsResult.parent().parent();
						var folder = $tree.tree("getParent", $node[0]);

						$log("ui.listtree:onEndQuery: $subtree=[{1}], result=[{2}], folder=[{3}]", 0, $subtree.prev("div.TrN").attr("title") || "", $node.attr("title"), folder.text);

						forwardTo(this, folder);
					}

				}

				//opts.onEndQuery.call(this);
			}
		}));
	}

	function _collapseAll($tree) {
		return $f("span.TrH-E", $tree.add($tree.prev("div.TrN"))).each(function() {
			var hit = $(this);
			var $subtree = hit.parent().next("UL").css("display", "none");
			$c("div.TrN-P", $subtree).css("display", "none");

			$ac("TrH-C", $rc("TrH-E TrH-EH", hit));
			$rc("TrI-FO", hit.next());
		}).length;

	}

	/**
	 * forward to current folder node.
	 */
	function forwardTo(selfUL, currentFolder, expandTimes) {
		var time0 = new Date().getTime();

		var $tree = $(selfUL);
		var state = $d(selfUL, "listtree");
		var opts = state.options;

		//
		if (opts._currentFolder && currentFolder && opts._currentFolder.target == currentFolder.target) {
			_makeNodeButtons($tree, opts, currentFolder, expandTimes);

			return;
		}

		$log('ui.listtree:forwardTo: BEGIN...... currentFolder=' + (currentFolder ? currentFolder.text : ""));

		_collapseAll($tree);

		// restore the current folder node
		_restoreFolder($tree, opts);

		// set the current node
		opts._currentFolder = currentFolder;

		if (currentFolder) {

			_makeNodeButtons($tree, opts, currentFolder, expandTimes);

			// set current folder
			_showFolder($tree, opts);
		} else {
			var roots = $tree.tree("getRoots");

			for ( var i = 0; i < roots.length; i++) {
				var $root = $(roots[i].target);

				$root.addClass("TrN-CR");
			}
		}

		function _makeNodeButtons($tree, opts, currentFolder, expandTimes) {
			var time0 = new Date().getTime();

			var $currentFolder = $(currentFolder.target);

			// build check all text or back to text
			var folderCheckbox = opts.checkAllText && opts.checkbox && !opts.onlyLeafCheck;
			if (folderCheckbox || opts.backToCls) {

				if ($hc(opts.backToCls, $currentFolder))
					return;

				$currentFolder.addClass("TrN-CF");
				if ($.type(expandTimes) == "undefined" || expandTimes == 2) {
					// process node check box on left
					if (opts.checkboxLeft)
						$currentFolder.addClass("LtN-no-icon");

					// append back to text
					if (opts.backToCls) {
						$currentFolder.addClass(opts.backToCls);
						$('<span title="' + opts.backToTip + '"></span>').addClass(opts.backToCls).insertAfter($currentFolder.children(".TrI")).bind("click", function(e) {
							$tree.listtree("backTo");
							e.stopPropagation();
						});
					} else {
						$currentFolder.addClass("LtN-no-back");
					}

					// append check all text
					if (folderCheckbox) {
						$('<span class="TrN-all-text" title="' + opts.checkAllTip + '"></span>').text(opts.checkAllText).appendTo($currentFolder).bind("click", function(e) {
							$(this).parent().children(".TrC").click();
							e.stopPropagation();
						});
					}

					// hide node title/icon
					$currentFolder.children(".TrT,.TrI").hide();
				} else
					$currentFolder.children(".TrT").hide();
			}

			// $log('ui.listtree:_makeNodeButtons. ', time0);
		}

		$log('ui.listtree:forwardTo: END! currentFolder=' + (currentFolder ? currentFolder.text : ""), time0);
	}

	function _showFolder($tree, opts) {
		var time0 = new Date().getTime();

		var currentFolder = opts._currentFolder;
		var $currentFolder = $(currentFolder.target);
		$currentFolder.next("UL").addClass("TrN-CF");

		// expand the current node
		$tree.tree("expandTo", {
			target : currentFolder.target,
			ignoreOnExpand : true
		});
		// $log('ui.listtree:_showFolder: expandTo! currentFolder=[{1}]', time0, currentFolder.text);

		$tree.tree("expand", {
			target : currentFolder.target,
			ignoreOnExpand : true
		});
		// $log('ui.listtree:_showFolder: expand! currentFolder=[{1}]', time0, currentFolder.text);
	}

	/**
	 * restore the current folder node settings
	 */
	function _restoreFolder($tree, opts) {
		if (!opts._currentFolder) {
			var roots = $tree.tree("getRoots");

			for ( var i = 0; i < roots.length; i++) {
				var $root = $(roots[i].target);

				$root.removeClass("TrN-CR");
			}

			return;
		}

		var $currentFolder = $(opts._currentFolder.target);
		$currentFolder.removeClass("TrN-CF");
		$currentFolder.next("UL").removeClass("TrN-CF");

		// destroy "Check All Button" and "Back To Button"
		var folderCheckbox = opts.checkAllText && opts.checkbox && !opts.onlyLeafCheck;
		if (folderCheckbox || opts.backToCls) {

			// show node title/icon
			$currentFolder.children(".TrT,.TrI").show();

			// process node check box on left
			if (opts.checkboxLeft)
				$currentFolder.removeClass("LtN-no-icon");

			// remove back to text
			if (opts.backToCls) {
				$currentFolder.removeClass(opts.backToCls);
				$currentFolder.children("span." + opts.backToCls).remove();
			} else {
				$currentFolder.removeClass("LtN-no-back");
			}

			// remove check all text
			if (folderCheckbox) {
				$currentFolder.children("span.TrN-all-text").remove();
			}
		}
	}

	$.fn.listtree = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.listtree.methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.tree(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "listtree");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "listtree", {
					options : $.extend({}, $.fn.listtree.defaults, $.fn.listtree.parseOptions(this), options)
				});
			}
			_init(this);
		});
	}

	$.fn.listtree.methods = {
		options : function(jq) {
			return $d(jq[0], "listtree").options;
		},
		backTo : function(jq) {
			return jq.each(function() {
				var currentFolder = $d(jq[0], "listtree").options._currentFolder;
				if (currentFolder)
					forwardTo(jq[0], jq.tree("getParent", currentFolder.target));
			});
		}
	}

	$.fn.listtree.parseOptions = function(selfUL) {
		return $.extend({}, $.fn.tree.parseOptions(selfUL), jCocit.parseOptions(selfUL, [ "checkAllText", "backToCls" ]));
	}

	$.fn.listtree.defaults = $.extend({}, $.fn.tree.defaults, {
		/**
		 * check all text is specified, the current node will be shown before the current sub tree list.
		 */
		checkAllText : "All",
		checkAllTip : "Check All",
		/**
		 * click the back text, the list tree will be back to parent of the current node.
		 */
		backToCls : "",
		backToTip : "Back to Super Folder"
	});

})(jQuery, jCocit);
