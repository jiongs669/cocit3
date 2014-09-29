
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
		var opts = $.data(e.data.target, "draggable").options;
		var dragData = e.data;

		// evaluate left and top coordination position of the current draggable object.
		var left = dragData.startLeft + e.pageX - dragData.startX;
		var top = dragData.startTop + e.pageY - dragData.startY;
		if (opts.deltaX != null && opts.deltaX != undefined) {
			left = e.pageX + opts.deltaX;
		}
		if (opts.deltaY != null && opts.deltaY != undefined) {
			top = e.pageY + opts.deltaY;
		}
		if (e.data.parent != document.body) {
			left += $(e.data.parent).scrollLeft();
			top += $(e.data.parent).scrollTop();
		}

		// axis is "h" means that only horizontal coordination position will be set.
		if (opts.axis == "h") {
			dragData.left = left;
		} else {
			// axis is "h" means that only vertical coordination position will be set.
			if (opts.axis == "v") {
				dragData.top = top;
			} else {
				dragData.left = left;
				dragData.top = top;
			}
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
		var opts = $.data(e.data.target, "draggable").options;
		var proxy = $.data(e.data.target, "draggable").proxy;
		if (proxy) {
			proxy.css("cursor", opts.cursor);
		} else {
			proxy = $(e.data.target);
			$.data(e.data.target, "draggable").handle.css("cursor", opts.cursor);
		}
		proxy.css({
			left : e.data.left,
			top : e.data.top
		});
	}

	/**
	 * Process Press Mouse Down
	 * <P>
	 * <B>Parameters:</B> the parameter "e" is event data, "e.data" contains the following properties
	 * <UL>
	 * <LI>target: used to describe the current dragging HTML element.
	 * </UL>
	 */
	function doMouseDown(e) {

		// Set the current draggable object is dragging
		$.data(this, "dragging", true);

		// Get options of state of the current draggable object
		var opts = $.data(e.data.target, "draggable").options;

		// Get droppable objects which the current draggable object can be drop into.
		var droppables = $(".droppable").filter(function() {
			return e.data.target != this;
		}).filter(function() {
			var accept = $.data(this, "droppable").options.accept;
			if (accept) {
				return $(accept).filter(function() {
					return this == e.data.target;
				}).length > 0;
			} else {
				return true;
			}
		});

		// Set droppable objects into state of the current draggable object.
		$.data(e.data.target, "draggable").droppables = droppables;

		// Get proxy of draggable object.
		var proxy = $.data(e.data.target, "draggable").proxy;
		if (!proxy) {
			if (opts.proxy) {
				if (opts.proxy == "clone") {
					proxy = $(e.data.target).clone().insertAfter(e.data.target);
				} else {
					proxy = opts.proxy.call(e.data.target, e.data.target);
				}
				$.data(e.data.target, "draggable").proxy = proxy;
			} else {
				proxy = $(e.data.target);
			}
		}

		// Set proxy positon to absolute
		proxy.css("position", "absolute");

		// evaluate left and top and save into "e.data"
		evalDragData(e);

		// set left and top into the current draggable proxy object
		applyDragData(e);

		// call-back onStartDrag function
		opts.onStartDrag.call(e.data.target, e);

		return false;
	}

	/**
	 * Process Move Mouse
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>
	 * </UL>
	 */
	function doMouseMove(e) {
		// evaluate left and top and save into "e.data"
		evalDragData(e);

		// call-back onDrag function of options, if return false, the left and top will be ignore.
		if ($.data(e.data.target, "draggable").options.onDrag.call(e.data.target, e) != false) {
			applyDragData(e);
		}

		var source = e.data.target;
		$.data(e.data.target, "draggable").droppables.each(function() {
			var dropObj = $(this);
			var p2 = $(this).offset();

			// the current draggable object is moved into droppable element and process that the droppable element will accept it.
			if (e.pageX > p2.left && e.pageX < p2.left + dropObj.outerWidth() && e.pageY > p2.top && e.pageY < p2.top + dropObj.outerHeight()) {
				if (!this.entered) {
					$(this).trigger("_dragenter", [ source ]);
					this.entered = true;
				}
				$(this).trigger("_dragover", [ source ]);
			} else {
				if (this.entered) {
					$(this).trigger("_dragleave", [ source ]);
					this.entered = false;
				}
			}
		});

		return false;
	}

	/**
	 * Process Mouse Up
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>
	 * </UL>
	 */
	function doMouseUp(e) {
		// Set dragging end
		$.data(this, "dragging", false);

		// evaluate drag data, contains left and top.
		evalDragData(e);

		// get proxy of draggable object
		var proxy = $.data(e.data.target, "draggable").proxy;

		// get options of draggable object
		var opts = $.data(e.data.target, "draggable").options;

		// revert original position of the current draggable object.
		if (opts.revert) {

			// the current draggable object is dropped into droppable object.
			if (checkDropped() == true) {
				removeProxy();
				$(e.data.target).css({
					position : e.data.startPosition,
					left : e.data.startLeft,
					top : e.data.startTop
				});
			} else {
				if (proxy) {
					proxy.animate({
						left : e.data.startLeft,
						top : e.data.startTop
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
			// set left and top of the current draggable object
			$(e.data.target).css({
				position : "absolute",
				left : e.data.left,
				top : e.data.top
			});

			// remove proxy
			removeProxy();

			// check drop
			checkDropped();
		}

		// call-back onStopDrag function
		opts.onStopDrag.call(e.data.target, e);

		// unbind draggable event
		$(document).unbind(".draggable");
		setTimeout(function() {
			$("body").css("cursor", "auto");
		}, 100);

		/**
		 * remove proxy of draggable object
		 */
		function removeProxy() {
			if (proxy) {
				proxy.remove();
			}
			$.data(e.data.target, "draggable").proxy = null;
		}

		/**
		 * check whether the current draggable object is dropped into droppable object successfully?
		 */
		function checkDropped() {
			var dropped = false;

			$.data(e.data.target, "draggable").droppables.each(function() {
				var dropObj = $(this);
				var p2 = $(this).offset();

				// the current draggable object move into droppable object.
				if (e.pageX > p2.left && e.pageX < p2.left + dropObj.outerWidth() && e.pageY > p2.top && e.pageY < p2.top + dropObj.outerHeight()) {
					if (opts.revert) {
						$(e.data.target).css({
							position : e.data.startPosition,
							left : e.data.startLeft,
							top : e.data.startTop
						});
					}
					
					// trigger drop event on droppable object
					$(this).trigger("_drop", [ e.data.target ]);
					
					dropped = true;
					this.entered = false;
				}

			});

			return dropped;
		}

		return false;
	}

	/**
	 * Defined jQuery draggable object
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>options: this is a JSON object means that a draggable object will be created, options is a String means that it is a method name and will be invoked.
	 * <LI>args: this is JSON object used for arguments when invoking method specified by "options".
	 * </UL>
	 * <P>
	 * <B>Return:</B> jQuery draggable object.
	 */
	$.fn.draggable = function(options, args) {
		// Invoke method specified by options if the parameter options is a String.
		if (typeof options == "string") {
			return $.fn.draggable.methods[options](this, args);
		}
		return this.each(function() {
			var opts;

			// get already existed state of draggable object from "draggable" property of the current HTML element
			var state = $.data(this, "draggable");
			if (state) {
				state.handle.unbind(".draggable");
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, defaults, parseOptions(this), options || {});
			}

			// disabled draggable means that cursor is default.
			if (opts.disabled == true) {
				$(this).css("cursor", "default");
				return;
			}

			// handle is HTML element, pressing handle means that the jQuery element is draggable.
			var handle = null;
			if (typeof opts.handle == "undefined" || opts.handle == null) {
				handle = $(this);
			} else {
				handle = (typeof opts.handle == "string" ? $(opts.handle, this) : opts.handle);
			}

			// cache the state of draggable object into "draggable" property of the current HTML element.
			$.data(this, "draggable", {
				options : opts,
				handle : handle
			});

			// bind mousemove/mouseleave/mousedown events.
			handle.unbind(".draggable").bind("mousemove.draggable", {
				target : this
			}, function(e) {

				// ignore if the current HTML element is dragging or resizing
				if ($.data(this, "dragging") || $.data(this, "resizing")) {
					return;
				}

				var opts = $.data(e.data.target, "draggable").options;
				if (checkArea(e)) {
					$(this).css("cursor", opts.cursor);
					// } else {
					// $(this).css("cursor", "");
				}
			}).bind("mouseleave.draggable", {
				target : this
			}, function(e) {
				if (!$.data(this, "dragging") && !$.data(this, "resizing"))
					$(this).css("cursor", "");
			}).bind("mousedown.draggable", {
				target : this
			}, function(e) {
				// check whether the mouse located over the current HTML element
				if (checkArea(e) == false) {
					return;
				}

				// get the position of the current draggable object
				var position = $(e.data.target).position();

				// build event data
				var data = {
					startPosition : $(e.data.target).css("position"),
					startLeft : position.left,
					startTop : position.top,
					left : position.left,
					top : position.top,
					startX : e.pageX,
					startY : e.pageY,
					target : e.data.target,
					parent : $(e.data.target).parent()[0]
				};
				$.extend(e.data, data);

				// call-back onBeforeDrag function
				var options = $.data(e.data.target, "draggable").options;
				if (options.onBeforeDrag.call(e.data.target, e) == false) {
					return;
				}

				// bind mousedown/mousemove/mouseup events to document
				$(document).bind("mousedown.draggable", e.data, doMouseDown);
				$(document).bind("mousemove.draggable", e.data, doMouseMove);
				$(document).bind("mouseup.draggable", e.data, doMouseUp);

				// set cursor style
				$("body").css("cursor", options.cursor);
			});

			/**
			 * check whether the mouse located over the current HTML element
			 */
			function checkArea(e) {
				var dragObj = $.data(e.data.target, "draggable");
				var handle = dragObj.handle;
				var offset = $(handle).offset();
				var width = $(handle).outerWidth();
				var height = $(handle).outerHeight();
				var t = e.pageY - offset.top;
				var r = offset.left + width - e.pageX;
				var b = offset.top + height - e.pageY;
				var l = e.pageX - offset.left;
				return Math.min(t, r, b, l) > dragObj.options.edge;
			}
		});
	};

	/**
	 * define draggable methods
	 */
	$.fn.draggable.methods = {
		options : function(jq) {
			return $.data(jq[0], "draggable").options;
		},
		proxy : function(jq) {
			return $.data(jq[0], "draggable").proxy;
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
				var state = $.data(this, "draggable");
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
			"revert" : "boolean",
			"deltaX" : "number",
			"deltaY" : "number",
			"edge" : "number"
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
		edge : 0,
		axis : null,
		onBeforeDrag : function(e) {
		},
		onStartDrag : function(e) {
		},
		onDrag : function(e) {
		},
		onStopDrag : function(e) {
		}
	};
})(jQuery, jCocit);
