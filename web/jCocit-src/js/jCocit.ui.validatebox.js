/**
 * This is validate box UI plugin, used to wrap validate box on the target "TEXT" element.
 * 
 * <P>
 * <B>Dependencies:</B> none
 * 
 * <pre>
 * 	[input class='validatebox-text' type='text'/]
 *  [div class='validatebox-tip']
 * 		[span class='validatebox-tip-content'][/span]
 * 		[span class='validatebox-tip-pointer'][/span]
 * 	[/div]
 * </pre>
 * 
 * <p>
 * <B>Notes:</B>
 * <UL>
 * <LI>validatebox-text: this is target "TEXT" element.
 * <LI>validatebox-tip: this is tip box of "validatebox-text" element. it will be created when needed, otherwise it will be removed.
 * <LI>validatebox-tip-pointer: this is arrow of message box.
 * <LI>validatebox-tip-content: this is error message.
 * </UL>
 */
(function($, jCocit) {

	/**
	 * Wrap the target "text" field to validatebox object.
	 */
	function _init(targetHTML) {
		$(targetHTML).addClass("validatebox-text");
	}

	/**
	 * Destroy the target "text" field from HTML document.
	 */
	function destroy(targetHTML) {
		var state = $.data(targetHTML, "validatebox");
		state.validating = false;
		var $tip = state.tip;
		if ($tip) {
			$tip.remove();
		}
		$(targetHTML).unbind();
		$(targetHTML).remove();
	}

	/**
	 * Bind focus/blur/mouseenter/mouseleave events on the target "text" field.
	 * <p>
	 * <UL>
	 * <LI>focus: validate the target "text" field.
	 * <LI>blur:
	 * <LI>mouseenter:
	 * <LI>mouseleave:
	 * </UL>
	 */
	function _bindEvents(targetHTML) {
		var $target = $(targetHTML);
		var state = $.data(targetHTML, "validatebox");

		$target.unbind(".validatebox").bind("focus.validatebox", function() {
			state.validating = true;
			state.value = undefined;
			(function() {
				if (state.validating) {
					if (state.value != $target.val()) {
						state.value = $target.val();
						if (state.timer) {
							clearTimeout(state.timer);
						}
						state.timer = setTimeout(function() {
							$(targetHTML).validatebox("validate");
						}, state.options.delay);
					} else {
						_showTip(targetHTML);
					}
					setTimeout(arguments.callee, 200);
				}
			})();
		}).bind("blur.validatebox", function() {
			if (state.timer) {
				clearTimeout(state.timer);
				state.timer = undefined;
			}
			state.validating = false;
			removeTip(targetHTML);
		}).bind("mouseenter.validatebox", function() {
			if ($target.hasClass("validatebox-invalid")) {
				showTip(targetHTML);
			}
		}).bind("mouseleave.validatebox", function() {
			if (!state.validating) {
				removeTip(targetHTML);
			}
		});
	}

	/**
	 * Show tip box of the target "text" field
	 */
	function showTip(targetHTML) {
		var msg = $.data(targetHTML, "validatebox").message;
		var $tip = $.data(targetHTML, "validatebox").tip;
		if (!$tip) {
			$tip = $('<div class="validatebox-tip"><span class="validatebox-tip-content"></span><span class="validatebox-tip-pointer"></span></div>').appendTo("body");
			$.data(targetHTML, "validatebox").tip = $tip;
		}
		$tip.find(".validatebox-tip-content").html(msg);
		_showTip(targetHTML);
	}

	function _showTip(targetHTML) {
		var state = $.data(targetHTML, "validatebox");
		if (!state) {
			return;
		}
		var tip = state.tip;
		if (tip) {
			var $target = $(targetHTML);
			var $pointer = tip.find(".validatebox-tip-pointer");
			var $content = tip.find(".validatebox-tip-content");
			tip.show();
			tip.css("top", $target.offset().top - ($content.oh() - $target.oh()) / 2);
			if (state.options.tipPosition == "left") {
				tip.css("left", $target.offset().left - tip.ow());
				tip.addClass("validatebox-tip-left");
			} else {
				tip.css("left", $target.offset().left + $target.ow());
				tip.removeClass("validatebox-tip-left");
			}
			$pointer.css("top", ($content.oh() - $pointer.oh()) / 2);
		}
	}

	/**
	 * Remove tip box of the target "text" field
	 */
	function removeTip(targetHTML) {
		var tip = $.data(targetHTML, "validatebox").tip;
		if (tip) {
			tip.remove();
			$.data(targetHTML, "validatebox").tip = null;
		}
	}

	/**
	 * Validate the target "text" field value.
	 * <P>
	 * <B>Return: </B> true means that the field value is valid. otherwise the field value is invalid and show message tip.
	 */
	function validate(targetHTML) {
		var state = $.data(targetHTML, "validatebox");
		var opts = state.options;
		var tip = state.tip;
		var $target = $(targetHTML);
		var value = $target.val();

		function _setMessage(msg) {
			state.message = msg;
		}

		function _isValid(validType) {
			// length[1,9] parsed to types[1]=length types[2]=[1,9]
			var types = /([a-zA-Z_]+)(.*)/.exec(validType);
			var rule = opts.rules[types[1]];
			if (rule && value) {
				var args = eval(types[2]);
				if (!rule["validator"](value, args)) {
					$target.addClass("validatebox-invalid");
					var msg = rule["message"];
					if (args) {
						for ( var i = 0; i < args.length; i++) {
							msg = msg.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
						}
					}
					_setMessage(opts.invalidMessage || msg);
					if (state.validating) {
						showTip(targetHTML);
					}
					return false;
				}
			}
			return true;
		}

		if (opts.required) {
			if (value.trim() == "") {
				$target.addClass("validatebox-invalid");
				_setMessage(opts.missingMessage);
				if (state.validating) {
					showTip(targetHTML);
				}
				return false;
			}
		}
		if (opts.validType) {
			if (typeof opts.validType == "string") {
				if (!_isValid(opts.validType)) {
					return false;
				}
			} else {
				for ( var i = 0; i < opts.validType.length; i++) {
					if (!_isValid(opts.validType[i])) {
						return false;
					}
				}
			}
		}
		$target.removeClass("validatebox-invalid");
		removeTip(targetHTML);

		return true;
	}

	/**
	 * Create validatebox object or invoke validatebox method.
	 * <p>
	 * <b>Parameters:</b>
	 * <UL>
	 * <LI>options: type is "string" means validatebox method will be invoked. otherwise validatebox object will be created.
	 * <LI>args: it is arguments validatebox methods needed.
	 * </UL>
	 */
	$.fn.validatebox = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.validatebox.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.validatebox');
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "validatebox");
			if (state) {
				$.extend(state.options, options);
			} else {
				_init(this);
				$.data(this, "validatebox", {
					options : $.extend({}, $.fn.validatebox.defaults, $.fn.validatebox.parseOptions(this), options)
				});
			}
			_bindEvents(this);
		});
	};

	$.fn.validatebox.methods = {
		destroy : $X(destroy),
		validate : $X(validate),
		isValid : $x(validate)
	};

	$.fn.validatebox.parseOptions = function(targetHTML) {
		var $target = $(targetHTML);
		return $.extend({}, jCocit.parseOptions(targetHTML, [ "validType", "missingMessage", "invalidMessage", "tipPosition", {
			delay : "n"
		} ]), {
			required : ($target.attr("required") ? true : undefined)
		});
	};

	$.fn.validatebox.defaults = {
		required : false,
		validType : null,
		delay : 200,
		missingMessage : "This field is required.",
		invalidMessage : null,
		tipPosition : "right",// options are "left/right"
		rules : {
			email : {
				validator : function(strValue) {
					return /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(strValue);
				},
				message : "Please enter a valid email address."
			},
			url : {
				validator : function(strValue) {
					var strRegex = "^((https|http|ftp|rtsp|mms)?://)" + "?(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" // ftp的user@
							+ "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
							+ "|" // 允许IP和DOMAIN（域名）
							+ "([0-9a-zA-Z_!~*'()-]+\.)*" // 域名- www.
							+ "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\." // 二级域名
							+ "[a-zA-Z]{2,6})" // first level domain- .com or .museum
							+ "(:[0-9]{1,4})?" // 端口- :80
							+ "((/?)|" + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
					return new RegExp(strRegex).test(strValue);
				},
				message : "Please enter a valid URL."
			},
			length : {
				validator : function(strValue, lenBetween) {
					var len = $.trim(strValue).length;
					return len >= lenBetween[0] && len <= lenBetween[1];
				},
				message : "Please enter a value between {0} and {1}."
			},
			remote : {
				/**
				 * config[0]: url
				 * <p>
				 * config[1]: field
				 */
				validator : function(strValue, config) {
					var data = {};
					data[config[1]] = strValue;
					var json = $.doAjax({
						url : config[0],
						dataType : "json",
						data : data,
						async : false,
						cache : false,
						type : "post"
					}).responseText;
					return json == "true";
				},
				message : "Please fix this field."
			}
		}
	};
})(jQuery, jCocit);
