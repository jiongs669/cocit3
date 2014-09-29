
/**
 * This is jCocit Resizable Plugin
 */
(function($, jCocit) {

	/**
	 * Defined jQuery resizable object
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>options: this is a JSON object means that a resizable object will be created, options is a String means that it is a method name and will be invoked.
	 * <LI>args: this is JSON object used for arguments when invoking method specified by "options".
	 * </UL>
	 * <P>
	 * <B>Return:</B> jQuery resizable object.
	 */
	$.fn.resizable = function(options, args) {
		// Invoke method specified by options if the parameter options is a String.
		if (typeof options == "string") {
			return $.fn.resizable.methods[options](this, args);
		}

		/**
		 * Evaluate resize data
		 */
		function evalResizeData(e) {
			var resizeData = e.data;
			var options = $.data(resizeData.target, "resizable").options;
			if (resizeData.dir.indexOf("e") != -1) {
				var width = resizeData.startWidth + e.pageX - resizeData.startX;
				width = Math.min(Math.max(width, options.minWidth), options.maxWidth);
				resizeData.width = width;
			}
			if (resizeData.dir.indexOf("s") != -1) {
				var height = resizeData.startHeight + e.pageY - resizeData.startY;
				height = Math.min(Math.max(height, options.minHeight), options.maxHeight);
				resizeData.height = height;
			}
			if (resizeData.dir.indexOf("w") != -1) {
				resizeData.width = resizeData.startWidth - e.pageX + resizeData.startX;
				if (resizeData.width >= options.minWidth && resizeData.width <= options.maxWidth) {
					resizeData.left = resizeData.startLeft + e.pageX - resizeData.startX;
				}
			}
			if (resizeData.dir.indexOf("n") != -1) {
				resizeData.height = resizeData.startHeight - e.pageY + resizeData.startY;
				if (resizeData.height >= options.minHeight && resizeData.height <= options.maxHeight) {
					resizeData.top = resizeData.startTop + e.pageY - resizeData.startY;
				}
			}
		}

		/**
		 * Apply resize data
		 */
		function applyResizeData(e) {
			var resizeData = e.data;
			var target = resizeData.target;
			if (!$.boxModel && $.browser.msie) {
				$(target).css({
					width : resizeData.width,
					height : resizeData.height,
					left : resizeData.left,
					top : resizeData.top
				});
			} else {
				$(target).css({
					width : resizeData.width - resizeData.deltaWidth,
					height : resizeData.height - resizeData.deltaHeight,
					left : resizeData.left,
					top : resizeData.top
				});
			}
		}

		function doMouseDown(e) {
			$.data(e.data.target, "resizing", true);
			$.data(e.data.target, "resizable").options.onStartResize.call(e.data.target, e);
			return false;
		}

		function doMouseMove(e) {
			evalResizeData(e);
			if ($.data(e.data.target, "resizable").options.onResize.call(e.data.target, e) != false) {
				applyResizeData(e);
			}
			return false;
		}

		function doMouseUp(e) {
			$.data(e.data.target, "resizing", false);
			evalResizeData(e, true);
			applyResizeData(e);
			$.data(e.data.target, "resizable").options.onStopResize.call(e.data.target, e);
			$(document).unbind(".resizable");
			$("body").css("cursor", "");
			return false;
		}

		return this.each(function() {
			var $this = $(this);
			var opts = null;
			
			// get resizable state
			var state = $.data(this, "resizable");
			if (state) {
				$this.unbind(".resizable");
				opts = $.extend(state.options, options || {});
			} else {
				opts = $.extend({}, defaults, parseOptions(this), options || {});
				$.data(this, "resizable", {
					options : opts
				});
			}
			
			// disabled resizable
			if (opts.disabled == true) {
				return;
			}

			// get direction
			function getDirection(e) {
				var dir = "";
				var offset = $this.offset();
				var width = $this.outerWidth();
				var height = $this.outerHeight();
				var edge = opts.edge;
				if (e.pageY > offset.top && e.pageY < offset.top + edge) {
					dir += "n";
				} else {
					if (e.pageY < offset.top + height && e.pageY > offset.top + height - edge) {
						dir += "s";
					}
				}
				if (e.pageX > offset.left && e.pageX < offset.left + edge) {
					dir += "w";
				} else {
					if (e.pageX < offset.left + width && e.pageX > offset.left + width - edge) {
						dir += "e";
					}
				}
				var handles = opts.handles.split(",");
				for ( var i = 0; i < handles.length; i++) {
					var handle = handles[i].replace(/(^\s*)|(\s*$)/g, "");
					if (handle == "all" || handle == dir) {
						return dir;
					}
				}
				return "";
			}

			// bind resizable mousemove/mousedown/mouseleave events
			$(this).bind("mousemove.resizable", {
				target : this
			}, function(e) {
				if ($.data(this, "resizing")) {
					return;
				}
				var dir = getDirection(e);
				if (dir == "") {
					if (!$.data(this, "draggable"))
						$this.css("cursor", "");
				} else {
					$this.css("cursor", dir + "-resize");
				}
			}).bind("mousedown.resizable", {
				target : this
			}, function(e) {
				
				// get direction
				var dir = getDirection(e);
				if (dir == "") {
					return;
				}

				// evaluate resize data
				var left = $this.getCssValue("left");
				var top = $this.getCssValue("top");
				var width = $this.outerWidth();
				var height = $this.outerHeight();
				var data = {
					target : e.data.target,
					dir : dir,
					startLeft : left,
					startTop : top,
					left : left,
					top : top,
					startX : e.pageX,
					startY : e.pageY,
					startWidth : width,
					startHeight : height,
					width : width,
					height : height,
					deltaWidth : width - $this.width(),
					deltaHeight : height - $this.height()
				};
				
				// bind resize events on document
				$(document).bind("mousedown.resizable", data, doMouseDown);
				$(document).bind("mousemove.resizable", data, doMouseMove);
				$(document).bind("mouseup.resizable", data, doMouseUp);
				
				// set cursor style
				$("body").css("cursor", dir + "-resize");
				
			}).bind("mouseleave.resizable", {
				target : this
			}, function(e) {
				if (!$.data(this, "resizing")) {
					$this.css("cursor", "");
				}
			});

		});
	};
	
	/**
	 * Degine resizable methods
	 */
	$.fn.resizable.methods = {
		options : function(jq) {
			return $.data(jq[0], "resizable").options;
		},
		enable : function(jq) {
			return jq.each(function() {
				$(this).resizable({
					disabled : false
				});
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				$(this).resizable({
					disabled : true
				});
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var state = $.data(this, "resizable");
				if (state) {
					state.unbind(".resizable");
				}
			});
		}
	};
	
	/**
	 * Parse options from the attributes of the current resizable HTML element 
	 */
	function parseOptions(target) {
		var $target = $(target);
		return $.extend({}, jCocit.parseOptions(target, [ "handles", {
			minWidth : "number",
			minHeight : "number",
			maxWidth : "number",
			maxHeight : "number",
			edge : "number"
		} ]), {
			disabled : ($target.attr("disabled") ? true : undefined)
		});
	}
	
	/**
	 * Default settings of resizable object
	 */
	var defaults = {
		disabled : false,
		handles : "n, e, s, w, ne, se, sw, nw, all",
		minWidth : 10,
		minHeight : 10,
		maxWidth : 10000,
		maxHeight : 10000,
		edge : 10,
		onStartResize : function(e) {
		},
		onResize : function(e) {
		},
		onStopResize : function(e) {
		}
	};
})(jQuery, jCocit);