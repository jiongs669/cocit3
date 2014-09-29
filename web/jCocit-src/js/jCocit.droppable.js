
/**
 * This is jCocit Droppable Plugin
 */
(function($, jCocit) {

	/**
	 * Initialize Droppable Object
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>target: this is droppable HTML element.
	 * </UL>
	 */
	function init(target) {
		// get droppable options
		var options = $.data(target, "droppable").options;

		// Bind droppable dragenter/dragleave/dragover/drop/ events
		$(target).addClass("droppable").bind("_dragenter", function(e, source) {
			options.onDragEnter.apply(target, [ e, source ]);
		}).bind("_dragleave", function(e, source) {
			options.onDragLeave.apply(target, [ e, source ]);
		}).bind("_dragover", function(e, source) {
			options.onDragOver.apply(target, [ e, source ]);
		}).bind("_drop", function(e, source) {
			options.onDrop.apply(target, [ e, source ]);
		});
	}

	/**
	 * Defined jQuery droppable object
	 * <P>
	 * <B>Parameters:</B>
	 * <UL>
	 * <LI>options: this is a JSON object means that a droppable object will be created, options is a String means that it is a method name and will be invoked.
	 * <LI>args: this is JSON object used for arguments when invoking method specified by "options".
	 * </UL>
	 * <P>
	 * <B>Return:</B> jQuery droppable object.
	 */
	$.fn.droppable = function(options, args) {
		if (typeof options == "string") {
			return $.fn.droppable.methods[options](this, args);
		}
		options = options || {};
		return this.each(function() {
			var dropObj = $.data(this, "droppable");
			if (dropObj) {
				$.extend(dropObj.options, options);
			} else {
				init(this);
				$.data(this, "droppable", {
					options : $.extend({}, defaults, parseOptions(this), options)
				});
			}
		});
	};

	/**
	 * Droppable Methods
	 */
	$.fn.droppable.methods = {};

	/**
	 * Parse options from the attributes of the current drappable HTML element
	 */
	function parseOptions(target) {
		return jCocit.parseOptions(target, [ "accept" ]);
	}

	/**
	 * Drappable Default Settings
	 */
	var defaults = {
		accept : null,
		onDragEnter : function(e, source) {
		},
		onDragOver : function(e, source) {
		},
		onDragLeave : function(e, source) {
		},
		onDrop : function(e, source) {
		}
	};
})(jQuery, jCocit);