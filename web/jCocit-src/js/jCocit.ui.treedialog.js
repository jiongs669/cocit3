/**
 * This is treedialog UI plugin, extends from dialog UI plugin.
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: resizable, draggable, button(TODO Optional)
 * <LI>ReferencedBy: login, tree
 * <LI>SubClass:
 * <LI>SuperClass: panel, window, dialog
 * </UL>
 * 
 * <pre>
 *  [div class='Pn Wd']
 *  	[div class='PnH WdH']
 *  		[div class='PnHR'][div class='PnHC']
 *  			[div class='PnHL'][/div]
 *  			[div class='PnHT'] Here is window title. [/div]
 *  			[div class='PnHB']
 *  				[a class='PnHBB' href='javascript:void(0)'][/a]
 *  				[a class='PnHBL' href='javascript:void(0)'][/a]
 *  				[a class='PnHBN' href='javascript:void(0)'][/a]
 *  				[a class='PnHBX' href='javascript:void(0)'][/a]
 *  				[a class='PnHBC' href='javascript:void(0)'][/a]
 *  			[/div]
 *  		[/div][/div]
 * 		[/div]
 * 		[div class='PnB WdB']
 * 			[div class='DlTB'][table cellspacing='0' cellpadding='0'][tr] Here is dialog tool buttons. [/tr][/table][/div]
 * 			[div class='PnBC'][ul class='Tr'][/ul][/div] 
 * 			[div class='DlBS'] Here is dialog buttons. [/div]
 * 		[/div]
 * 		[div class='PnF WdF'][div class='PnFR'][div class='PnFC'][/div][/div][/div]
 *  [/div]
 *  [div class='WdS'][/div]
 *  [div class='WdM'][/div]
 * </pre>
 */
(function($, jCocit) {

	function _init(selfHTML) {
		var $self = $(selfHTML);
		var state = $d(selfHTML, "treedialog");
		var opts = state.options;

		// Build Dialog
		$self.dialog($.extend({}, opts, {
			url : null,
			styleName : "treedialog",
			content : null,
			onResize : function() {
				if (state.tree)
					state.tree.height($self.dialog("content").height());
			}
		}));

		// Build Tree
		var $tree = state.tree;
		if (!$tree) {
			var $dialogContent = $self.dialog("content");
			$tree = $("<ul></ul>").appendTo($dialogContent);
			state.tree = $tree;
		}
		$tree.tree($.extend({}, opts, {
			styleName : opts.treeStyleName,
			height : $self.dialog("content").height(),
			onDblClick : function(node) {
				$self.dialog("close");
				opts.onDblClick.call(this, node);
			}
		}));
	}

	jCocit.treedialog = $.extend({}, jCocit.window, {
		open : function(url, dialogId, options) {
			return jCocit.window.open(url, dialogId, options, "treedialog");
		}
	});

	/**
	 * 1. Create treedialog UI object or set treedialog properties if "options" is JSON object.
	 * <P>
	 * 2. Invoke treedialog method or dialog method with arguments specified by "args" if "options" is method name.
	 */
	$.fn.treedialog = function(options, args) {
		if (typeof options == "string") {
			var fn = methods[options];
			if (fn) {
				return fn(this, args);
			} else {
				return this.dialog(options, args);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "treedialog");
			if (state) {
				$.extend(state.options, options);
			} else {
				$d(this, "treedialog", {
					options : $.extend({}, defaults, parseOptions(this), options)
				});
			}
			_init(this);
		});
	};

	var methods = {
		options : function(jq) {
			return $d(jq[0], "treedialog").options;
		},
		tree : function(jq) {
			return $d(jq[0], "treedialog").tree;
		},
		reload : function(jq, url) {
			return jq.each(function() {
				var state = $d(this, "treedialog");
				var opts = state.options;
				var $tree = state.tree;
				if (url) {
					opts.url = url;
				}
				$tree.tree({
					url : opts.url
				});
			});
		}
	};

	var parseOptions = function(selfHTML) {
		return $.extend({}, $.fn.dialog.parseOptions(selfHTML), $.fn.tree.parseOptions(selfHTML), jCocit.parseOptions(selfHTML, [ "treeStyleName" ]));
	};

	var defaults = $.extend({}, $.fn.dialog.defaults, $.fn.tree.defaults, {
		treeStyleName : ""
	});

})(jQuery, jCocit);
