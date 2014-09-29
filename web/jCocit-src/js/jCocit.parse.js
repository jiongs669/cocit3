/**
 * They are extend functions from jCocit object to support auto parse plugins of HTML UI element.
 */
(function($, jCocit) {
	$.extend(jCocit, {
		/**
		 * Parse HTML element to plugin. This function will be auto invoked after HTML document loaded.
		 * 
		 * <p>
		 * <b>Note:</b>
		 * 
		 * <pre>
		 *  [DIV class='jCocit-ui jCocit-draggable'][/DIV]
		 *  [UL class='jCocit-ui jCocit-tree'][/UL]
		 *  [INPUT class='jCocit-ui jCocit-combo'][/UL]
		 * </pre>
		 * 
		 * <UL>
		 * <LI>jCocit-ui: This HTML element will be automatic parsed if it's "class" contains "jCocit-ui".
		 * <LI>jCocit-{plugin}: This HTML element will be parsed to "plugin" object. It must be used combine with "jCocit-ui". (eg: jCocit-draggable, jCocit-resizable, jCocit-menu etc.)
		 * </UL>
		 * 
		 * <p>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>context: this is HTML element or jQuery object.
		 * </UL>
		 */
		parseUI : function(context) {
			var pluginObjs = {};
			var prefix = "jCocit-";
			var self = this;
			var time0 = new Date().getTime();
			var $uis = $(".jCocit-ui", context);
			$uis.each(function() {
				var $self = $(this);
				var classNames = $self.attr("class").split(" ");

				for ( var i = 0; i < classNames.length; i++) {
					var className = classNames[i];
					if (className.startsWith(prefix)) {
						var plugin = className.trim().substring(prefix.length);
						if (plugin != "ui" && $self[plugin]) {
							// try {
							var time1 = new Date().getTime();

							var $pluginObj = $self[plugin]();
							if (pluginObjs[plugin])
								pluginObjs[plugin] = pluginObjs[plugin].add($pluginObj);
							else
								pluginObjs[plugin] = $pluginObj;

							$log("ui.{1}: created (title = {2})", time1, plugin, this.title || this.id);
							// } catch (e) {
							// $log("jCocit.parseUI (" + plugin + ") Error: " + e);
							// }
						}
					}
				}

			});

			if (jCocit.defaults.debug) {
				var s = "";
				for ( var name in pluginObjs) {
					var $pluginObj = pluginObjs[name];
					s += ", " + name + "(" + $pluginObj.length + ")";
				}
				$log("Parse UI: Finished (total = {1}){2}", time0, $uis.length, s);
			}

			return pluginObjs;
		},

		/**
		 * Parse options of HTML element jQuery object specified "sourceHTML".
		 * <p>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>sourceHTML: This is HTML element or it's jQuery object.
		 * <LI>props: This is a property description array, used to describe attributes of HTML element which will be parsed and regard as sourceHTML options settings.
		 * </UL>
		 * <B>Return:</B> JSON object. these attributes of HTML element specified by the parameter "props" will be regard as the properties of JSON object.
		 */
		parseOptions : function(sourceHTML, props) {
			return $.parseOptions(sourceHTML, props);
		}
	});
})(jQuery, jCocit);

/**
 * Auto Parse jCocit UI element specified by class name "jCocit-ui" when the document is ready.
 */
$(document).ready(function() {
	jCocit.parseUI();
});
