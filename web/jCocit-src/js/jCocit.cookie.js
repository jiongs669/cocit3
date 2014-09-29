
/**
 * This is jCocit Cookie Manager.
 */
(function($, jCocit) {
	
	/**
	 * Define jCocit cookie manager
	 */
	jCocit.cookie = {
			
		/**
		 * Cache data specified by name and value into the browser cookie.
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>name: cached data key.
		 * <LI>value: cached data value.
		 * <LI>hours: cached period of validity, time unit is hour.
		 * </UL>
		 */
		set : function(name, value, hours) {
			var expires = "";
			if (hours != null) {
				var d = new Date();
				d.setTime(d.getTime() + 60 * 60 * 1000 * hours);
				expires = "; expires=" + d.toGMTString();
			}
			document.cookie = escape(name) + "=" + escape(value) + ";path=/" + expires;
		},
		
		/**
		 * Get cached data from the browser cookie.
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>name: cached data key.
		 * </UL>
		 */
		get : function(name) {
			var m = document.cookie.match(new RegExp("(^|; )(" + escape(name) + ")\\=([^;]*)(;|$)", "i"));
			return m == null ? null : unescape(m[3]);
		},
		
		/**
		 * Remove cached data from the browser cookie.
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>name: cached data key.
		 * </UL>
		 */
		remove : function(name) {
			if (this.get(name) != null)
				this.set(name, "", -1);
		}
	};
})(jQuery, jCocit);