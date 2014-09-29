
/**
 * This is jCocit Login Plugin
 */
(function($, jCocit) {

	/**
	 * Define Login Form Plugin.
	 * <p>
	 * <B>Login Form:</B>
	 * <UL>
	 * <LI>submit: this is submit button which class name contains "submit", asynchronously submit login form when clicking it.
	 * <LI>reset: this is reset button which class name contains "reset".
	 * <LI>remembetMe: remember login user name and password when login success.
	 * </UL>
	 * <P>
	 * <B>Parameters:</B> the parameter "options" contains the following properties.
	 * <UL>
	 * <LI>options:
	 * </UL>
	 */
	$.fn.loginform = function(options) {
		return this.each(function() {
			var form = this;
			
			// process submit button in login form.
			$(".submit", form).click(function() {

				var data = $(form).serialize();
				$.ajax({
					type : 'POST',
					url : jCocit.urls.login + "?loginCode=ajaxLogin",
					async : false,
					data : data,
					success : function(jqXHR, status, response) {
						var responseText = response.responseText;
						var json = jCocit.toJson(responseText);

						//
						if ($.type(json.statusCode) == "undefined") {
						} else if (json.statusCode == jCocit.status.SUCCESS) {

							// remember me: write user name and password to cookie.
							$(".rememberMe", form).each(function() {
								if (this.checked) {
									jCocit.cookie.set("jrs.auth_uid", form["jrs.auth_uid"].value);
									jCocit.cookie.set("jrs.auth_pwd", form["jrs.auth_pwd"].value);
								} else {
									jCocit.cookie.remove("jrs.auth_uid");
									jCocit.cookie.remove("jrs.auth_pwd");
								}
							});

							// submit form
							form.submit();

						} else if (json.statusCode == jCocit.status.ERROR) {
							$(".message", form).html(json.message);
						}
					},
					error : jCocit.doError
				});

				return false;
			});

			// process reset button in login form
			$(".reset", form).click(function() {
				$(".message", form).html("");
				this.form.reset();
				return false;
			});

			// process remember me checkbox in login form to read user name and password from cookie
			$(".rememberMe", form).each(function() {
				var uid = jCocit.cookie.get("jrs.auth_uid");
				if (uid) {
					this.checked = true;
					form["jrs.auth_uid"].value = uid
					form["jrs.auth_pwd"].value = jCocit.cookie.get("jrs.auth_pwd");
				}
			});

			return false;
		});
	};

	/**
	 * Define Login Dialog Plugin
	 */
	$.fn.logindialog = function(options) {
		return this.each(function() {
			$(this).removeClass("logindialog");

			// wrap login form content in wrapper
			var wrapper = $('<div id="loginformwrapper"></div>');
			$(this).attr("type", "ajax");
			$(this).wrap(wrapper);
			wrapper = $("#loginformwrapper");

			// open login dialog
			jCocit.dialog.open(null, "login", {
				width : 398,
				height : 222,
				//headerTitle : jCocit.nls.login,
				//headerSubTitle : jCocit.nls.login2,
				headerHeight: 50,
				styleName: "logindialog",
				modal : true,
				maxable : false,
				closable : false,
				draggable : false
			});

			// set login dialog HTML content to dialog content
			jCocit.dialog.setContent(wrapper.html(), "login");
			wrapper.html("");

		});
	}

})(jQuery, jCocit);