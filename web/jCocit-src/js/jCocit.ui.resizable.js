/**
 * This is jCocit Resizable Plugin
 */
(function($, jCocit) {

	/**
	 * 1. Create resizable object or set resizable properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke resizable method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.resizable = function(options, args) {
		if (typeof options == "string") {
			var fn = methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.resizable');
		}

		function evalResizeData(e) {
			var resizeData = e.data;
			var opts = $d(resizeData.target, "resizable").options;
			if (resizeData.dir.indexOf("e") != -1) {
				var width = resizeData.startWidth + e.pageX - resizeData.startX;
				width = Math.min(Math.max(width, opts.minWidth), opts.maxWidth);
				resizeData.width = width;
			}
			if (resizeData.dir.indexOf("s") != -1) {
				var height = resizeData.startHeight + e.pageY - resizeData.startY;
				height = Math.min(Math.max(height, opts.minHeight), opts.maxHeight);
				resizeData.height = height;
			}
			if (resizeData.dir.indexOf("w") != -1) {
				resizeData.width = resizeData.startWidth - e.pageX + resizeData.startX;
				if (resizeData.width >= opts.minWidth && resizeData.width <= opts.maxWidth) {
					resizeData.left = resizeData.startLeft + e.pageX - resizeData.startX;
				}
			}
			if (resizeData.dir.indexOf("n") != -1) {
				resizeData.height = resizeData.startHeight - e.pageY + resizeData.startY;
				if (resizeData.height >= opts.minHeight && resizeData.height <= opts.maxHeight) {
					resizeData.top = resizeData.startTop + e.pageY - resizeData.startY;
				}
			}
		}

		function applyResizeData(e) {
			var resizeData = e.data;
			var target = resizeData.target;
			$(target).css({
				left : resizeData.left,
				top : resizeData.top
			});
			$(target).ow(resizeData.width).oh(resizeData.height);
		}

		function doMouseDown(e) {
			$d(e.data.target, "resizing", true);
			$d(e.data.target, "resizable").options.onStartResize.call(e.data.target, e);
			return false;
		}

		function doMouseMove(e) {
			evalResizeData(e);
			if ($d(e.data.target, "resizable").options.onResize.call(e.data.target, e) != false) {
				applyResizeData(e);
			}
			return false;
		}

		function doMouseUp(e) {
			$d(e.data.target, "resizing", false);
			evalResizeData(e, true);
			applyResizeData(e);
			$d(e.data.target, "resizable").options.onStopResize.call(e.data.target, e);
			$(document).unbind(".resizable");
			$("body").css("cursor", "");
			return false;
		}

		return this.each(function() {
			var opts = null;
			var state = $d(this, "resizable");

			if (state) {
				$(this).unbind(".resizable");
				opts = $.extend(state.options, options || {});
			} else {
				opts = $.extend({}, defaults, parseOptions(this), options || {});
				$d(this, "resizable", {
					options : opts
				});
			}

			if (opts.disabled == true) {
				return;
			}

			$(this).bind("mousemove.resizable", {
				target : this
			}, function(e) {
				if ($d(this, "resizing")) {
					return;
				}
				var dir = getDirection(e);
				if (dir == "") {

					// draggable compatible
					var stateDrag = $d(this, "draggable");
					if (!stateDrag || stateDrag.handle[0] != this) {
						$(e.data.target).css("cursor", "");
					}

				} else {
					$(e.data.target).css("cursor", dir + "-resize");
				}
				e.preventDefault();
			}).bind("mouseleave.resizable", {
				target : this
			}, function(e) {
				$(e.data.target).css("cursor", "");
				e.preventDefault();
			}).bind("mousedown.resizable", {
				target : this
			}, function(e) {
				var dir = getDirection(e);
				if (dir == "") {
					return;
				}

				function _css(css) {
					var val = parseInt($(e.data.target).css(css));
					if (isNaN(val)) {
						return 0;
					} else {
						return val;
					}
				}

				var resizeData = {
					target : e.data.target,
					dir : dir,
					startLeft : _css("left"),
					startTop : _css("top"),
					left : _css("left"),
					top : _css("top"),
					startX : e.pageX,
					startY : e.pageY,
					startWidth : $ow($(e.data.target)),
					startHeight : $oh($(e.data.target)),
					width : $ow($(e.data.target)),
					height : $oh($(e.data.target)),
					deltaWidth : $ow($(e.data.target)) - $w($(e.data.target)),
					deltaHeight : $oh($(e.data.target)) - $h($(e.data.target))
				};

				$(document).bind("mousedown.resizable", resizeData, doMouseDown);
				$(document).bind("mousemove.resizable", resizeData, doMouseMove);
				$(document).bind("mouseup.resizable", resizeData, doMouseUp);

				$("body").css("cursor", dir + "-resize");

				e.preventDefault();
			});

			function getDirection(e) {
				var tt = $(e.data.target);
				var dir = "";
				var offset = tt.offset();
				var width = $ow(tt);
				var height = $oh(tt);
				var edge = opts.edge;
				var optHandlers = opts.handles;
				if (e.pageY > offset.top && e.pageY < offset.top + edge) {
					if (optHandlers.indexOf("n") > -1)
						dir += "n";
				} else {
					if (e.pageY < offset.top + height && e.pageY > offset.top + height - edge) {
						if (optHandlers.indexOf("s") > -1)
							dir += "s";
					}
				}
				if (e.pageX > offset.left && e.pageX < offset.left + edge) {
					if (optHandlers.indexOf("w") > -1)
						dir += "w";
				} else {
					if (e.pageX < offset.left + width && e.pageX > offset.left + width - edge) {
						if (optHandlers.indexOf("e") > -1)
							dir += "e";
					}
				}
				var handles = optHandlers.split(",");
				for ( var i = 0; i < handles.length; i++) {
					var handle = handles[i].replace(/(^\s*)|(\s*$)/g, "");
					if (handle == "all" || handle == dir) {
						return dir;
					}
				}
				return "";
			}

		});
	};

	var methods = {
		options : function(jq) {
			return $d(jq[0], "resizable").options;
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
		}
	};

	function parseOptions(target) {
		var $target = $(target);
		return $.extend({}, jCocit.parseOptions(target, [ "handles", {
			minWidth : "n",
			minHeight : "n",
			maxWidth : "n",
			maxHeight : "n",
			edge : "n"
		} ]), {
			disabled : ($target.attr("disabled") ? true : undefined)
		});
	}
	;

	var defaults = {
		disabled : false,
		handles : "n, e, s, w, ne, se, sw, nw, all",
		minWidth : 20,
		minHeight : 20,
		maxWidth : 10000,
		maxHeight : 10000,
		edge : 10,
		onStartResize : $n,// args: e
		onResize : $n,// args: e
		// args: e
		onStopResize : $n
	};

})(jQuery, jCocit);
