/**
 * This is jCocit JS framework core.
 * 
 * 
 * <P>
 * <B>Relationship:</B>
 * <UL>
 * <LI>Reference: utils, dialog( > window > dialog, Optional)
 * <LI>SubClass: none
 * <LI>SuperClass: none
 * </UL>
 * 
 * <P>
 * <B>Codes Specification:</B>
 * <UL>
 * <LI>Variable starts with "$" means that this is jQuery object.
 * <LI>Variable ends with upper case HTML tag name means that this is a HTML element.
 * <LI>Variable ends with "Array" means that this is a Array.
 * <LI>Function starts with "_" means that this is an inner function and cannot be invoked by outer JS codes.
 * <LI>Variable equals "selfHTML" means that this is source HTML element.
 * <LI>Variable equals "$self" means that this is jQuery object of source HTML element.
 * </UL>
 */
(function($) {
	/**
	 * jQuery alert settings.
	 */
	if (typeof Jerror == "undefined") {
		Jerror = Jwarn = Jinfo = Jsuccess = alert;
		Jconfirm = confirm;
		Jprompt = prompt;
	}

	/**
	 * Evaluate context path according to jCocit.js script path.
	 */
	var contextPath = "";
	var scripts = document.getElementsByTagName('script');
	for ( var i = 0; i < scripts.length; i++) {
		var src = scripts[i].src;
		if (!src)
			continue;
		var m = src.match(/jCocit\/js\/jCocit\.min\.js(\W|$)/i);
		if (!m) {
			m = src.match(/jCocit\/js\/jCocit\.src\.js(\W|$)/i);
		}
		if (!m) {
			m = src.match(/jCocit\/js\/jCocit\.pack\.js(\W|$)/i);
		}
		if (m) {
			contextPath = src.substring(0, m.index);
			contextPath += "../"
		}
	}

	// test url
	// var loginUrl = "login/loginForm.html";
	// if (contextPath.startsWith("/")) {
	var loginUrl = contextPath + "utils/jsonValidateLogin.jsp";
	// }

	/**
	 * Define jCocit JS object
	 */
	jCocit = {
		loadBeginTime : new Date(),
		contextPath : contextPath,
		URL : {
			login : loginUrl,
			ajaxLogin : loginUrl + "?loginCode=ajaxLogin",
			validateLogin : loginUrl + "?loginCode=validate"
		},
		defaults : {
			debug : false,
			loading : "Loading...",
			title : ""
		},
		/**
		 * This constants used to describe that the return status code after the server side process business logic.
		 * <P>
		 * <UL>
		 * <LI>SUCCESS: means that the server side process business logic is successful.
		 * <LI>ERROR: means that the server side process business logic is failed.
		 * <LI>ERROR_NO_ACCESS: means that the current user has not privilege to process business logic.
		 * </UL>
		 */
		status : {
			SUCCESS : 200,
			SUCCESS_EXTRA_CHANNEL_USER : 201,
			ERROR : 300,
			ERROR_NO_ACCESS : 301,
			ERROR_WHETHER_KICK_OUT : 302,
			ERROR_DONT_KICK_OUT : 303
		},

		/**
		 * Keyboard code
		 */
		keyCode : {
			ENTER : 13,
			ESC : 27,
			END : 35,
			HOME : 36,
			SHIFT : 16,
			TAB : 9,
			LEFT : 37,
			RIGHT : 39,
			UP : 38,
			DOWN : 40,
			PGUP : 33,
			PGDN : 34,
			DELETE : 46,
			BACKSPACE : 8
		},

		/**
		 * Check whether is the current user login? Login dialog will NOT be pop-up if the current user don't login.
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>callback: this is a call-back function, it will be invoked if login success.
		 * </UL>
		 * <p>
		 * <b>Server Return Value:</b> server side return JSON object to describe whether is the current user login? For example: {"statusCode":300, "message": "error message"}.
		 * <UL>
		 * <LI>invalid JSON object: means that the current user don't login.
		 * <LI>valid JSON object: "statusCode" property existed in server return value JSON object.
		 * <UL>
		 * <LI>statusCode equals SUCCESS(200): means that the current user already login success.
		 * <LI>statusCode not equals SUCCESS(200): means that the current user don't login, the error message specified by "message" property of server return value JSON object will NOT be pop-up.
		 * </UL>
		 * </UL>
		 * <p>
		 * <b>Return:</b>
		 * <UL>
		 * <LI>true: means login success.
		 * <LI>false: means don't login.
		 * </UL>
		 * <p>
		 * <b>Exception:</b>
		 * <UL>
		 * <LI>AJAX request error: invoke jCocit.doError method to process error.
		 * <LI>execute JS code error: pop-up alert error box.
		 * </UL>
		 */
		checkLogin : function(callback) {
			var self = this;
			var success = false;
			try {
				$.ajax({
					type : 'GET',
					url : self.URL.validateLogin,
					async : false,
					success : function(responseData, status, jqXHR) {
						var responseText = jqXHR.responseText;
						var serverJson = jCocit.toJson(responseText);

						if (serverJson == null || $.type(serverJson.statusCode) == "undefined") {
							success = false;
						} else if (serverJson.statusCode == jCocit.status.SUCCESS) {
							if ($.isFunction(callback)) {
								callback();
							}
							success = true;
						} else {
							success = false;
						}
					},
					error : self.doError
				});
			} catch (e) {
				alert("Check Login Error: " + e);
			}

			return success;
		},

		/**
		 * Check whether is the current user login? Login dialog will be pop-up if the current user don't login.
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>title: the login dialog title.
		 * <LI>callback: the callback function will be invoke if login success.
		 * <LI>"title" will be regarded as callback function if the type of "title" is function.
		 * </UL>
		 * <p>
		 * <b>Server Return Value:</b> Server side return value can be JSON object or HTML content. For example: {"statusCode":300, "message": "error message"}.
		 * <UL>
		 * <LI>invalid JSON object: the current user don't login means that the server side return HTML content of login form and pop-up login dialog.
		 * <LI>valid JSON object: "statusCode" property existed in server return value JSON object.
		 * <UL>
		 * <LI>statusCode equals SUCCESS(200): means that the current user already login success.
		 * <LI>statusCode not equals SUCCESS(200): means that the current user don't login, the error message specified by "message" property of server return value JSON object will be pop-up.
		 * </UL>
		 * </UL>
		 * <p>
		 * <b>Return:</b>
		 * <UL>
		 * <LI>true: means login success.
		 * <LI>false: means don't login.
		 * </UL>
		 * <p>
		 * <b>Exception:</b>
		 * <UL>
		 * <LI>AJAX request error: invoke jCocit.doError method to process error.
		 * <LI>execute JS code error: pop-up alert error box.
		 * </UL>
		 */
		login : function(title, callback) {
			if ($.isFunction(title)) {
				callback = title;
				title = null;
			}
			var self = this;
			var success = false;
			try {
				$.ajax({
					type : 'GET',
					url : self.URL.validateLogin,
					async : false,
					success : function(responseData, status, jqXHR) {
						var responseText = jqXHR.responseText;
						var serverJson = jCocit.toJson(responseText);

						// return value is invalid JSON object
						if (serverJson == null || $.type(serverJson.statusCode) == "undefined") {
							if ($.type(self.dialog) == "undefined") {
								return;
							}

							// open login dialog
							jCocit.loginDialog = self.dialog.open(null, "__jCocit_dialog_login", {
								width : 398,
								height : 222,
								styleName : "logindialog",
								modal : true,
								shadow : false,
								maxable : false,
								draggable : true,
								content : responseText,
								doConfirm : callback
							});

							success = false;

						} else if (serverJson.statusCode == jCocit.status.SUCCESS) {
							// login success: invoke "callback" function

							if ($.isFunction(callback)) {
								callback();
							}
							success = true;

						} else {
							// login failed: pop-up error message box.

							Jerror(serverJson.message);
							success = false;

						}
					},
					error : self.doError
				});
			} catch (e) {
				alert("Login Error: " + e);
			}

			return success;
		},

		/**
		 * Parse string specified by "str" to JSON object.
		 * <p>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>str: this is JSON string.
		 * </UL>
		 * <p>
		 * <b>Return:</b>
		 * <UL>
		 * <LI>JSON object will be return if "str" is a valid JSON string.
		 * <LI>Empty JSON object will be return if "str" is a invalid JSON string.
		 * <LI>"str" self will be return if "str" is not a string.
		 * </UL>
		 * <p>
		 * <b>Exception:</b>
		 * <UL>
		 * <LI>Empty JSON object will be return if parse JSON occur error.
		 * </UL>
		 */
		toJson : function(str) {
			try {
				if ($.type(str) == 'string')
					return $.parseJSON(str);
				else
					return str;
			} catch (e) {
				return {};
			}
		},

		/**
		 * This is a generic method to process AJAX request error.
		 */
		doError : function(jqXHR, statusText, responseError) {
			// Jerror('<table>' + //
			// '<tr><th style="text-align:right" valign="top">Status:</th><td valign="top">' + jqXHR.status + '</td></tr>' + //
			// '<tr><th style="text-align:right" valign="top">Status Text:</th><td valign="top">' + statusText + '</td></tr>' + //
			// '<tr><th style="text-align:right" valign="top">Response Error:</th><td valign="top">' + responseError + '</td></tr>' + //
			// '<tr><th style="text-align:right" valign="top">Response Text:</th><td valign="top">' + jqXHR.responseText + '</td></tr>' + //
			// '</table>');
			Jerror(responseError);
		},

		/**
		 * This is a generic method to process AJAX success.
		 * <P>
		 * <b>Parameters:</b>
		 * <UL>
		 * <LI>responseData: this is response data, it is a JSON object if "dataType" is "json" in options.
		 * <LI>statusText:
		 * <LI>jqXHR:
		 * </UL>
		 * <p>
		 * <b>Return:</b> true means process business success, otherwise failed.
		 */
		doSuccess : function(responseData, statusText, jqXHR) {
			// Jsuccess('<table>' + //
			// '<tr><th style="text-align:right" valign="top">Status:</th><td valign="top">' + jqXHR.status + '</td></tr>' + //
			// '<tr><th style="text-align:right" valign="top">Status Text:</th><td valign="top">' + statusText + '</td></tr>' + //
			// '<tr><th style="text-align:right" valign="top">Response Type:</th><td valign="top">' + $.type(responseData) + '</td></tr>' + //
			// '<tr><th style="text-align:right" valign="top">Response Text:</th><td valign="top">' + jqXHR.responseText + '</td></tr>' + //
			// '</table>');
			Jsuccess(jqXHR.responseText);
		}

	};
})(jQuery);

/**
 * Extends jQuery methods
 */
(function($, jCocit) {
	$.extend({

		doAjax : function(options) {
			$.ajax($.extend({}, options, {
				cache : false,
				success : function(responseData, statusText, jqXHR) {
					var responseText = jqXHR.responseText;
					var serverJson = jCocit.toJson(responseText);

					if (serverJson == null || $.type(serverJson.statusCode) == "undefined") {
						// invalid json object
						//
						// jCocit.login(null, function() {
						// $.ajax(options);
						// });

						// ignore invalid json object
						if ($.isFunction(options.success))
							options.success(responseData, statusText, jqXHR);

					} else if (serverJson.statusCode == jCocit.status.ERROR_NO_ACCESS) {
						// login required

						jCocit.login(null, function() {
							$.ajax(options);
						});

					} else if (serverJson.statusCode == jCocit.status.ERROR) {
						// occurs error

						Jerror(serverJson.message);

					} else {
						// ajax request success.

						if ($.isFunction(options.success))
							options.success(responseData, statusText, jqXHR);

					}

				},
				error : options.error || jCocit.doError
			}));
		}

	});

	/**
	 * Extends instance methods
	 */
	$.fn.extend({

		/**
		 * Load HTML content specified by options.url or options.data and set into the current HTML element.
		 * <p>
		 * <B>Parameters:</B> the parameter "options" contains the following properties.
		 * <UL>
		 * <LI>type: AJAX request type, for example: GET, POST etc.
		 * <LI>url: AJAX request URL.
		 * <LI>data: AJAX request data.
		 * <LI>success: this is callback function, the function will be invoked if server side process business logic success.
		 * </UL>
		 * <P>
		 * <b>Server Return Value:</b> Server side return value can be JSON object or HTML content.
		 * <UL>
		 * <LI>invalid JSON object: server return value is HTML content. means that the server side process business logic success and the server return value is regard as content of the current HTML
		 * element.
		 * <LI>valid JSON object: server return value is a JSON object.
		 * <UL>
		 * <LI>statusCode equals ERROR_NO_ACCESS(301): means that the current user don't have enough permissions and the login dialog will be pop-up, "message" property of server side JSON object
		 * will be regard as login dialog caption.
		 * <LI>statusCode equals ERROR(300): means that server side process business logic occurs error. the error message specified by "message" property of server return value JSON object will be
		 * pop-up.
		 * <LI>statusCode equals others or undefined: means that server side process business logic success and the success callback function will be invoked with the server return value JSON object.
		 * </UL>
		 * </UL>
		 * <p>
		 * <b>Return:</b> no return.
		 * <p>
		 * <b>Exception:</b>
		 * <UL>
		 * <LI>AJAX request error: invoke jCocit.doError method to process error.
		 * </UL>
		 */
		doLoad : function(options) {
			var $this = $(this);
			$.ajax($.extend({}, options, {
				cache : false,
				success : function(responseData, statusText, jqXHR) {
					var responseText = jqXHR.responseText;
					var serverJson = jCocit.toJson(responseText);

					if (serverJson == null || $.type(serverJson.statusCode) == "undefined") {
						// invalid json object

						$this.html(jqXHR.responseText);
						jCocit.parseUI($this);

						if ($.isFunction(options.success))
							options.success(responseData, statusText, jqXHR);

					} else if (serverJson.statusCode == jCocit.status.ERROR_NO_ACCESS) {
						// login required

						jCocit.login(null, function() {

							$.ajax($.extend({}, options, {
								cache : false,
								success : function(responseData, statusText, jqXHR) {
									$this.html(jqXHR.responseText);
									jCocit.parseUI($this);
									if ($.isFunction(options.success))
										options.success(responseData, statusText, jqXHR);
								}
							}));

						});

					} else if (serverJson.statusCode == jCocit.status.ERROR) {
						// occurs error

						Jerror(serverJson.message);

					} else {
						// ajax request success.

						if ($.isFunction(options.success))
							options.success(responseData, statusText, jqXHR);

					}

				},
				error : options.error || jCocit.doError
			}));
		},

		/**
		 * Get HTML content specified by url and data and set into the current HTML element.
		 * <p>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>url: AJAX request URL.
		 * <LI>data: AJAX request data.
		 * <LI>doSuccess: this is callback function, the function will be invoked if server side process business logic success.
		 * </UL>
		 * <p>
		 * More documents refrence doLoad function.
		 */
		doGet : function(url, data, doSuccess) {
			this.doLoad({
				url : url,
				data : data,
				success : doSuccess
			});
		},

		/**
		 * Get HTML content specified by url and data and set into the current HTML element.
		 * <p>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>url: AJAX request URL.
		 * <LI>data: AJAX request data.
		 * <LI>doSuccess: this is callback function, the function will be invoked if server side process business logic success.
		 * </UL>
		 * <p>
		 * More documents refrence doLoad function.
		 */
		doPost : function(url, data, doSuccess) {
			this.doLoad({
				url : url,
				type : "POST",
				data : data,
				success : doSuccess
			});
		},

		/**
		 * Evaluate CSS integer value
		 * <P>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>cssName:
		 * </UL>
		 */
		_css : function(cssName) {
			var css = $(this).css(cssName)
			return css ? css._int() : 0;
		},

		/**
		 * Set or Get element outer width
		 * <p>
		 * width is undefined means that get outer width, otherwize set outer width.
		 */
		ow : function(width, options) {
			if (width == undefined || $.type(width) == "boolean") {
				if (this[0] == window)
					return $w(this) || document.body.clientWidth;

				return $ow(width, this) || 0;
			}
			return this.each(function() {
				if (!$.support.boxModel && $.browser.msie) {
					$w(width, $(this));
				} else {
					var $this = $(this);
					var balance = $ow(options, $this) - $w($this);
					if (balance == 0) {
						balance = $this._css("border-left-width") + $this._css("border-right-width") + $this._css("margin-left") + $this._css("margin-right");
						if (options) {
							balance += $this._css("padding-left") + $this._css("padding-right");
						}
					}
					$w(width - balance, $this);
				}
			});
		},

		/**
		 * Set or Get element outer height
		 * <p>
		 * height is undefined means that get outer height, otherwize set outer height.
		 */
		oh : function(height, options) {
			if (height == undefined || $.type(height) == "boolean") {
				if (this[0] == window)
					return $h(this) || document.body.clientHeight;

				return $oh(height, this) || 0;
			}
			return this.each(function() {
				if (!$.support.boxModel && $.browser.msie) {
					$h(height, $(this));
				} else {
					var $this = $(this);
					var balance = $oh(options, $this) - $h($this);
					if (balance == 0) {
						balance = $this._css("border-top-width") + $this._css("border-bottom-width") + $this._css("margin-top") + $this._css("margin-bottom");
						if (options) {
							balance += $this._css("padding-top") + $this._css("padding-bottom");
						}
					}
					$h(height - balance, $this);
				}
			});
		},

		_scrollLeft : function(left) {
			if (left == undefined) {
				return this.scrollLeft();
			} else {
				return this.each(function() {
					$(this).scrollLeft(left);
				});
			}
		},

		_fit : function(fit) {
			fit = fit == undefined ? true : fit;
			var parentTAG = $p(this)[0];
			var $parent = $(parentTAG);
			var target = this[0];
			var fcount = parentTAG.fcount || 0;
			if (fit) {
				if (!target.fitted) {
					target.fitted = true;
					parentTAG.fcount = fcount + 1;
					$ac("panel-noscroll", $parent);
					if (parentTAG.tagName == "BODY") {
						$ac("panel-fit", $("html"));
					}
				}
			} else {
				if (target.fitted) {
					target.fitted = false;
					parentTAG.fcount = fcount - 1;
					if (parentTAG.fcount == 0) {
						$rc("panel-noscroll", $parent);
						if (parentTAG.tagName == "BODY") {
							$rc("panel-fit", $("html"));
						}
					}
				}
			}
			return {
				width : $w($parent),
				height : $h($parent)
			};
		},
		/**
		 * setClass: used to replace addClass and removeClass in order to enhance performance.
		 */
		setClass : function(value) {
			for ( var i = this.length - 1; i >= 0; i--)
				this[i].className = value;

			return this;
		}
	});

	$.fn._attr = $.fn.prop || $.fn.attr;

})(jQuery, jCocit);
