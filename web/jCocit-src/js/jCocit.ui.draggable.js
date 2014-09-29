/**
 * This is jCocit Draggable Plugin
 */
(function($, jCocit) {

	/**
	 * Evaluate left and top coordinate position of the current draggable object and set into "e.data".
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>data: the data property of event contains the following nested properties.
	 * <UL>
	 * <LI>target:
	 * <LI>startLeft:
	 * <LI>startTop:
	 * <LI>startX:
	 * <LI>startY:
	 * <LI>parent:
	 * <LI>left: left will be evaluate and set into "e.data".
	 * <LI>top: top will be evaluate and set into "e.data".
	 * </UL>
	 * <LI>pageX:
	 * <LI>pageY:
	 * </UL>
	 * <P>
	 * <B>Options:</B> the draggable options descriptions
	 * <UL>
	 * <LI>deltaY:
	 * <LI>deltaY:
	 * <LI>axis: value is "h" means that the draggable object only be drag horizontal, "v" means that the draggable object only be drag vertical
	 * </UL>
	 */
	function evalDragData(e) {
		var data = e.data;
		var state = $d(data.target, "draggable");
		var $target = $(data.target);
		var opts = state.options;
		var $proxy = state.proxy;
		var left = data.startLeft + e.pageX - data.startX;
		var top = data.startTop + e.pageY - data.startY;
		if ($proxy) {
			if ($p($proxy)[0] == document.body) {
				if (opts.deltaX != null && opts.deltaX != undefined) {
					left = e.pageX + opts.deltaX;
				} else {
					left = e.pageX - data.offsetWidth;
				}
				if (opts.deltaY != null && opts.deltaY != undefined) {
					top = e.pageY + opts.deltaY;
				} else {
					top = e.pageY - data.offsetHeight;
				}
			} else {
				if (opts.deltaX != null && opts.deltaX != undefined) {
					left += data.offsetWidth + opts.deltaX;
				}
				if (opts.deltaY != null && opts.deltaY != undefined) {
					top += data.offsetHeight + opts.deltaY;
				}
			}
		}
		if (data.parent != document.body) {
			left += $(data.parent).scrollLeft();
			top += $(data.parent).scrollTop();
		}

		/*
		 * jCocit Enhance: support snap
		 */
		left = _snap(left, opts.deltaX);
		top = _snap(top, opts.deltaY);
		function _snap(v, delta) {
			if (delta == null || delta == undefined || delta <= 0) {
				return v;
			}
			var r = parseInt(v / delta) * delta;
			if (Math.abs(v % delta) > delta / 2) {
				r += v > 0 ? delta : (-1 * delta);
			}
			return r;
		}

		/*
		 * jCocit Enhance: inline equal true means that the current DIV cannot move to outer of it's parent
		 */
		if (opts.inline) {
			if (left < 0) {
				left = 0
			}
			if (top < 0) {
				top = 0
			}
			var $parent = $(data.parent);
			if (left + $ow($target) > $w($parent)) {
				left = $w($parent) - $ow($target);
			}
			if (top + $oh($target) > $h($parent)) {
				top = $h($parent) - $oh($target);
			}
		}

		/*
		 * oxis: only drag it on horizontal or vertical
		 */
		if (opts.axis == "h") {
			data.left = left;
		} else if (opts.axis == "v") {
			data.top = top;
		} else {
			data.left = left;
			data.top = top;
		}

	}

	/**
	 * Apply left and top coordinate position to the current draggable object using "e.data".
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>e.data.left:
	 * <LI>e.data.top:
	 * </UL>
	 */
	function applyDragData(e) {
		var state = $d(e.data.target, "draggable");
		var opts = state.options;
		var $proxy = state.proxy;
		if (!$proxy) {
			$proxy = $(e.data.target);
		}
		$proxy.css({
			left : e.data.left,
			top : e.data.top
		});
		$("body").css("cursor", opts.cursor);
	}

	function doMouseDown(e) {
		$d(this, "dragging", true);
		var state = $d(e.data.target, "draggable");
		var opts = state.options;
		var $droppable = $(".droppable").filter(function() {
			return e.data.target != this;
		}).filter(function() {
			var accept = $d(this, "droppable").options.accept;
			if (accept) {
				return $(accept).filter(function() {
					return this == e.data.target;
				}).length > 0;
			} else {
				return true;
			}
		});
		state.droppables = $droppable;
		var $proxy = state.proxy;
		if (!$proxy) {
			if (opts.proxy) {
				if (opts.proxy == "clone") {
					$proxy = $(e.data.target).clone().insertAfter(e.data.target);
				} else {
					$proxy = opts.proxy.call(e.data.target, e.data.target);
				}
				state.proxy = $proxy;
			} else {
				$proxy = $(e.data.target);
			}
		}
		$proxy.css("position", "absolute");
		evalDragData(e);
		applyDragData(e);
		opts.onStartDrag.call(e.data.target, e);
		return false;
	}

	function doMouseMove(e) {
		var state = $d(e.data.target, "draggable");
		evalDragData(e);
		if (state.options.onDrag.call(e.data.target, e) != false) {
			applyDragData(e);
		}
		var target = e.data.target;
		state.droppables.each(function() {
			var $droppable = $(this);
			if ($droppable.droppable("options").disabled) {
				return;
			}
			var p2 = $droppable.offset();
			if (e.pageX > p2.left && e.pageX < p2.left + $ow($droppable) && e.pageY > p2.top && e.pageY < p2.top + $oh($droppable)) {
				if (!this.entered) {
					$(this).trigger("_dragenter", [ target ]);
					this.entered = true;
				}
				$(this).trigger("_dragover", [ target ]);
			} else {
				if (this.entered) {
					$(this).trigger("_dragleave", [ target ]);
					this.entered = false;
				}
			}
		});
		return false;
	}

	function doMouseUp(e) {
		$d(this, "dragging", false);
		doMouseMove(e);
		var state = $d(e.data.target, "draggable");
		var $proxy = state.proxy;
		var opts = state.options;
		if (opts.revert) {
			if (checkDropped() == true) {
				$(e.data.target).css({
					position : e.data.startPosition,
					left : e.data.startLeft,
					top : e.data.startTop
				});
			} else {
				if ($proxy) {
					var left, top;
					if ($p($proxy)[0] == document.body) {
						left = e.data.startX - e.data.offsetWidth;
						top = e.data.startY - e.data.offsetHeight;
					} else {
						left = e.data.startLeft;
						top = e.data.startTop;
					}
					$proxy.animate({
						left : left,
						top : top
					}, function() {
						removeProxy();
					});
				} else {
					$(e.data.target).animate({
						left : e.data.startLeft,
						top : e.data.startTop
					}, function() {
						$(e.data.target).css("position", e.data.startPosition);
					});
				}
			}
		} else {
			$(e.data.target).css({
				position : "absolute",
				left : e.data.left,
				top : e.data.top
			});
			checkDropped();
		}
		opts.onStopDrag.call(e.data.target, e);
		$(document).unbind(".draggable");
		setTimeout(function() {
			$("body").css("cursor", "");
		}, 100);

		/**
		 * remove proxy of draggable object
		 */
		function removeProxy() {
			if ($proxy) {
				$proxy.remove();
			}
			state.proxy = null;
		}

		/**
		 * check whether the current draggable object is dropped into droppable object successfully?
		 */
		function checkDropped() {
			var dropped = false;
			state.droppables.each(function() {
				var $droppable = $(this);
				if ($droppable.droppable("options").disabled) {
					return;
				}
				var p2 = $droppable.offset();
				if (e.pageX > p2.left && e.pageX < p2.left + $ow($droppable) && e.pageY > p2.top && e.pageY < p2.top + $oh($droppable)) {
					if (opts.revert) {
						$(e.data.target).css({
							position : e.data.startPosition,
							left : e.data.startLeft,
							top : e.data.startTop
						});
					}
					removeProxy();
					$(this).trigger("_drop", [ e.data.target ]);
					dropped = true;
					this.entered = false;
					return false;
				}
			});
			if (!dropped && !opts.revert) {
				removeProxy();
			}
			return dropped;
		}

		return false;
	}

	/**
	 * 1. Create draggable object or set draggable properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke draggable method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.draggable = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.draggable.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.draggable');
		}
		return this.each(function() {
			var opts;
			var state = $d(this, "draggable");
			if (state) {
				state.handle.unbind(".draggable");
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, defaults, parseOptions(this), options || {});
			}
			if (opts.disabled == true) {
				$(this).css("cursor", "");
				return;
			}
			var $handle = null;
			if (typeof opts.handle == "undefined" || opts.handle == null) {
				$handle = $(this);
			} else {
				$handle = (typeof opts.handle == "string" ? $(opts.handle, this) : opts.handle);
			}
			$d(this, "draggable", {
				options : opts,
				handle : $handle
			});
			$handle.unbind(".draggable").bind("mousemove.draggable", {
				target : this
			}, function(e) {

				// resizable compatible
				if ($d(this, "dragging") || $d(this, "resizing")) {
					return;
				}
				var opts = $d(e.data.target, "draggable").options;
				if (checkArea(e)) {
					$(this).css("cursor", opts.cursor);

					// resizable compatible
					// } else {
					// $(this).css("cursor", "");
				}
				e.preventDefault();
			}).bind("mouseleave.draggable", {
				target : this
			}, function(e) {
				$(this).css("cursor", "");
				e.preventDefault();
			}).bind("mousedown.draggable", {
				target : this
			}, function(e) {
				if (checkArea(e) == false) {
					return;
				}
				$(this).css("cursor", "");
				var position = $(e.data.target).position();
				var offset = $(e.data.target).offset();
				var data = {
					startPosition : $(e.data.target).css("position"),
					startLeft : position.left,
					startTop : position.top,
					left : position.left,
					top : position.top,
					startX : e.pageX,
					startY : e.pageY,
					offsetWidth : (e.pageX - offset.left),
					offsetHeight : (e.pageY - offset.top),
					target : e.data.target,
					parent : $p($(e.data.target))[0]
				};
				$.extend(e.data, data);
				var opts = $d(e.data.target, "draggable").options;
				if (opts.onBeforeDrag.call(e.data.target, e) == false) {
					return;
				}
				$(document).bind("mousedown.draggable", e.data, doMouseDown);
				$(document).bind("mousemove.draggable", e.data, doMouseMove);
				$(document).bind("mouseup.draggable", e.data, doMouseUp);

				e.preventDefault();
			});

			function checkArea(e) {
				var state = $d(e.data.target, "draggable");

				// resizable compatible
				var edgeResize = 0;
				var stateResize = $d(e.data.target, "resizable");
				if (stateResize && state.handle[0] == e.data.target) {
					edgeResize = stateResize.options.edge;
				}

				var edge = Math.max(edgeResize, state.options.edge);
				var $handle = $(state.handle);
				var offset = $handle.offset();
				var width = $ow($handle);
				var height = $oh($handle);
				var t = e.pageY - offset.top;
				var r = offset.left + width - e.pageX;
				var b = offset.top + height - e.pageY;
				var l = e.pageX - offset.left;

				// $log(", l=" + l + ",b=" + b + ",t=" + t + ",r=" + r + ", adge: " + edge);

				return Math.min(t, r, b, l) > edge;
			}

		});
	};

	/**
	 * define draggable methods
	 */
	$.fn.draggable.methods = {
		options : function(jq) {
			return $d(jq[0], "draggable").options;
		},
		proxy : function(jq) {
			return $d(jq[0], "draggable").proxy;
		},
		enable : function(jq) {
			return jq.each(function() {
				$(this).draggable({
					disabled : false
				});
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				$(this).draggable({
					disabled : true
				});
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var state = $d(this, "draggable");
				if (state) {
					state.handle.unbind(".draggable");
				}
			});
		}
	};

	/**
	 * Parse options from the attributes of the current draggable HTML element
	 */
	function parseOptions(source) {
		var t = $(source);
		return $.extend({}, jCocit.parseOptions(source, [ "cursor", "handle", "axis", {
			"revert" : "b",
			"deltaX" : "n",
			"deltaY" : "n",
			"inline" : "n",
			"edge" : "n"
		} ]), {
			disabled : (t.attr("disabled") ? true : undefined)
		});
	}

	/**
	 * the default options of the draggable object.
	 */
	var defaults = {
		proxy : null,
		revert : false,
		cursor : "move",
		deltaX : null,
		deltaY : null,
		handle : null,
		disabled : false,
		inline : false, // true means the current DIV cannot move to outer of the parent Element.
		edge : 0,
		axis : null,// options: h/v
		onBeforeDrag : $n,// args: e
		onStartDrag : $n,// args: e
		onDrag : $n,// args: e
		onStopDrag : $n
	// args: e
	};

})(jQuery, jCocit);
