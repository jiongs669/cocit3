/**
 * This is login plugin.
 * <P>
 * Used to process "submit/reset/rememberMe" buttons business in login form.
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: dialog ( > window > dialog )
 * <LI>ReferencedBy: none
 * <LI>SubClass: none
 * <LI>SuperClass: none
 * </UL>
 * 
 */
(function($, jCocit) {

	function login(url, form, extraData) {
		var data = $(form).serialize();

		$.ajax({
			type : 'POST',
			url : url,
			async : false,
			data : data + (extraData ? extraData : ""),
			success : function(responseData, statusText, jqXHR) {
				var responseText = jqXHR.responseText;
				var json = jCocit.toJson(responseText);

				//
				if ($.type(json.statusCode) == "undefined") {
					$(".message", form).html($.fn.loginform.defaults.errorMsg);
				} else if (json.statusCode == jCocit.status.SUCCESS || json.statusCode == jCocit.status.SUCCESS_EXTRA_CHANNEL_USER) {

					function _doSuccess() {
						// Remember me: save username and password to cookie.
						$(".rememberMe", form).each(function() {
							if (this.checked) {
								jCocit.cookie.set("jrs.auth_uid", form["jrs.auth_uid"].value);
								jCocit.cookie.set("jrs.auth_pwd", form["jrs.auth_pwd"].value);
							} else {
								jCocit.cookie.remove("jrs.auth_uid");
								jCocit.cookie.remove("jrs.auth_pwd");
							}
						});

						if (jCocit.loginDialog) {

							// callback function if login success.
							var doConfirm = jCocit.loginDialog.dialog("options").doConfirm;

							// destroy login dialog if login success.
							jCocit.loginDialog.dialog("destroy");
							jCocit.loginDialog = null;

							if ($.isFunction(doConfirm)) {
								doConfirm();
							}
						} else {

							// submit form
							form.submit();

						}
					}
					
					if (json.statusCode == jCocit.status.SUCCESS_EXTRA_CHANNEL_USER) {
						Jinfo(json.message, _doSuccess);
					} else {
						_doSuccess();
					}

				} else if (json.statusCode == jCocit.status.ERROR) {
					$(".message", form).html(json.message);
				} else if (json.statusCode == jCocit.status.ERROR_WHETHER_KICK_OUT) {
					Jconfirm(json.message, function(yes) {
						if (yes) {
							login(url, form, "&jrs.kickOutOldSsn=Yes");
						} else {
							login(url, form, "&jrs.kickOutOldSsn=No");
						}
					});
				} else if (json.statusCode == jCocit.status.ERROR_DONT_KICK_OUT) {
					Jinfo(json.message);
				}
			},
			error : jCocit.doError
		});
	}

	/**
	 * Create Login Form Plugin.
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
			var $form = $ac("loginform", $(this));

			// Bind click event on "submit" button
			$(".submit", form).click(function() {
				var url = $(this).attr("url") || jCocit.URL.ajaxLogin;

				login(url, form);

				return false;
			});

			// Bind click event on "reset" button
			$(".reset", form).click(function() {
				$(".message", form).html("");
				this.form.reset();
				return false;
			});

			// Read username and password from cookie
			$(".rememberMe", form).each(function() {
				var uid = jCocit.cookie.get("jrs.auth_uid");
				if (uid) {
					this.checked = true;
					form["jrs.auth_uid"].value = uid
					form["jrs.auth_pwd"].value = jCocit.cookie.get("jrs.auth_pwd");
				}
			});

			if (form["jrs.auth_uid"]) {
				form["jrs.auth_uid"].focus();
			}

			return false;
		});
	};

	$.fn.loginform.defaults = {
		errorMsg : "Invalid User Name or Password"
	}

})(jQuery, jCocit);
