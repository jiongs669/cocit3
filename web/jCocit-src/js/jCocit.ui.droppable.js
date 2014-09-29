/**
 * This is jCocit Droppable Plugin
 */
(function($, jCocit) {

	function init(target) {
		$ac("droppable", $(target));
		$(target).bind("_dragenter", function(e, source) {
			$d(target, "droppable").options.onDragEnter.apply(target, [ e, source ]);
		});
		$(target).bind("_dragleave", function(e, source) {
			$d(target, "droppable").options.onDragLeave.apply(target, [ e, source ]);
		});
		$(target).bind("_dragover", function(e, source) {
			$d(target, "droppable").options.onDragOver.apply(target, [ e, source ]);
		});
		$(target).bind("_drop", function(e, source) {
			$d(target, "droppable").options.onDrop.apply(target, [ e, source ]);
		});
	}

	/**
	 * 1. Create droppable object or set droppable properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke droppable method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.droppable = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.droppable.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.droppable');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "droppable");
			if (state) {
				$.extend(state.options, options);
			} else {
				init(this);
				$d(this, "droppable", {
					options : $.extend({}, defaults, parseOptions(this), options)
				});
			}
		});
	};

	$.fn.droppable.methods = {
		options : function(jq) {
			return $d(jq[0], "droppable").options;
		},
		enable : function(jq) {
			return jq.each(function() {
				$(this).droppable({
					disabled : false
				});
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				$(this).droppable({
					disabled : true
				});
			});
		}
	};

	function parseOptions(target) {
		var t = $(target);
		return $.extend({}, jCocit.parseOptions(target, [ "accept" ]), {
			disabled : (t.attr("disabled") ? true : undefined)
		});
	}

	var defaults = {
		accept : null,
		disabled : false,
		onDragEnter : $n,// args e, source
		onDragOver : $n,// args e, source,
		onDragLeave : $n,// args e, source,
		onDrop : $n
	// args e, source
	};
})(jQuery, jCocit);
