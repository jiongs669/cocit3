(function($, jCocit) {

	if ($.alerts)
		$e($.alerts.defaults, {
			ok : "OK",
			cancel : "Cancel",
			yes : "Yes",
			no : "No"
		});

	if ($.fn.loginform)
		$e($.fn.loginform.defaults, {
			errorMsg : "Invalid User Name or Password"
		});

})(jQuery, jCocit);
