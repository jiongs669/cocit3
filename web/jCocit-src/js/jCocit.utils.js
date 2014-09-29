/**
 * . * This is nil function without implement.
 * <P>
 * Used for plug-in default settings
 */
function $n() {
}

/**
 * eval proxy
 */
function $fn(fn) {
	if ($.type(fn) == "function")
		return fn;

	return (new Function("return " + fn))();
}

/**
 * eval javascript,
 * 
 * @param s
 *            javascript, can contains "ctx" string.
 * @param ctx
 * @returns
 */
function $exec(s, ctx) {
	return eval(s);
}

/**
 * Proxy Function: return jQuery object.
 */
function $X(fn, param) {
	return function($self, args) {
		return $self.each(function() {
			if ($.type(param) == "undefined")
				fn(this, args);
			else if ($.type(args) == "undefined")
				fn(this, param);
			else
				fn(this, args, param);
		});
	}
}

/**
 * Proxy Function: return value determine with proxied function.
 */
function $x(fn, param) {
	return function($self, args) {
		if ($.type(param) == "undefined")
			return fn($self[0], args);
		else if ($.type(args) == "undefined")
			fn($self[0], param);
		else
			return fn($self[0], args, param);
	}
}

/*
 * Define jQuery Proxy Functions
 */
/**
 * Children
 */
function $c(selector, $obj) {
	if (!$obj) {
		// trace
		// if ($.type(selector.children) != "function")
		// return selector.children();
		//		
		return selector.children();
	}

	return $obj.children(selector);
}

/**
 * Find
 */
function $f(selector, $obj) {
	return $obj.find(selector);
}

/**
 * Closest
 */
function $l(selector, $context, $obj) {
	if ($obj)
		return $obj.closest(selector, $context);

	return $context.closest(selector);
}

/**
 * Add Class
 */
function $ac(cls, $obj) {
	// trace
	// if ($.type($obj.addClass) != "function")
	// $obj.addClass(cls);

	return $obj.addClass(cls);
}

/**
 * Has Class
 */
function $hc(cls, $obj) {
	// trace
	// if ($.type($hasClass) != "function")
	// $hasClass(cls);

	return $obj.hasClass(cls);
}

/**
 * Remove Class
 */
function $rc(cls, $obj) {
	return $obj.removeClass(cls);
}

/**
 * Data
 */
function $d(el, prop, data) {
	return $.data(el, prop, data);
}

/**
 * Parent
 */
function $p($obj) {
	return $obj.parent();
}

/**
 * Width
 * 
 * @param width
 * @param $obj
 * @returns
 */
function $w(width, $obj) {
	if (!$obj)
		return width.width();

	return $obj.width(width);
}

/**
 * Height
 * 
 * @param height
 * @param $obj
 * @returns
 */
function $h(height, $obj) {
	if (!$obj)
		return height.height();

	return $obj.height(height);
}
/**
 * Outer Width
 * 
 * @param options
 * @param $obj
 * @returns
 */
function $ow(options, $obj) {
	if (!$obj)
		return options.outerWidth();

	return $obj.outerWidth(options);
}

/**
 * Outer Height
 * 
 * @param options
 * @param $obj
 * @returns
 */
function $oh(options, $obj) {
	if (!$obj)
		return options.outerHeight();

	return $obj.outerHeight(options);
}

/**
 * Extends
 * 
 * @param opts1
 * @param opts2
 */
function $e(opts1, opts2) {
	return $.extend(opts1, opts2);
}
/*
 * END: Define jQuery Proxy Functions
 */

function $log(msg) {
	if (jCocit && !jCocit.defaults.debug) {
		return;
	}
	if (arguments.length > 1) {
		args = Array.prototype.slice.call(arguments, 1);
		args[0] = args[0] ? ("elapse: " + (new Date().getTime() - args[0])) : new Date().toString();
		msg = (msg + " -------- {0}").format(args);
	}
	if (jCocit && jCocit.defaults.logFilter) {
		if (!msg.startsWith(jCocit.defaults.logFilter))
			return;
	}
	try {
		if ($.browser.mozilla) {
			console.log(msg);
		} else {
			_log();
		}
	} catch (e) {
		_log();
	}

	function _log() {
		var $log = $("#jCocit_logconsole");
		if ($log.length == 0)
			$log = $('<div id="__jCocit_log_console"></div>').appendTo("body");

		$log.prepend("<div>{0}</div>".format(msg));
	}
}

function _sti(callback, timeout, param) {
	var args = Array.prototype.slice.call(arguments, 2);
	var _cb = function() {
		callback.apply(null, args);
	}
	setInterval(_cb, timeout);
};

function _sto(callback, timeout, param) {
	var args = Array.prototype.slice.call(arguments, 2);
	var _cb = function() {
		callback.apply(null, args);
	}
	setTimeout(_cb, timeout);
};

/**
 * They are extended functions of String class.
 */
$.extend(String.prototype, {
	// /**
	// * Check whether the current String is a positive integer?
	// */
	// isPositiveInteger : function() {
	// return (new RegExp(/^[1-9]\d*$/).test(this));
	// },
	// /**
	// * Check whether the current String is a integer?
	// */
	// isInt : function() {
	// return (new RegExp(/^\d+$/).test(this));
	// },
	// /**
	// * Check whether the current String is a number?
	// */
	// isNumber : function(value, element) {
	// return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
	// },
	/**
	 * Trim front and back space characters of the current String.
	 */
	trim : function() {
		return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
	},
	/**
	 * Check whether does the current String starts with string specified by the parameter "pattern"?
	 */
	startsWith : function(pattern) {
		return this.indexOf(pattern) === 0;
	},
	/**
	 * Check whether does the current String ends with string specified by the parameter "pattern"?
	 */
	endsWith : function(pattern) {
		var d = this.length - pattern.length;
		return d >= 0 && this.lastIndexOf(pattern) === d;
	},
	// /**
	// * Check whether the current String is a password?
	// */
	// isPwd : function() {
	// return (new RegExp(/^([_]|[a-zA-Z0-9]){6,32}$/).test(this));
	// },
	// /**
	// * Check whether the current String is a email address?
	// */
	// isEmail : function() {
	// return (new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trim()));
	// },
	// /**
	// * Check whether the current String is an empty string?
	// */
	// isEmpty : function() {
	// for ( var i = 0; i < this.length; i += 1) {
	// var ch = this.charAt(i);
	// if (ch != ' ' && ch != "\n" && ch != "\t" && ch != "\r") {
	// return false;
	// }
	// }
	// return true;
	// },
	// /**
	// * Check whether the current String is an URL address?
	// */
	// isUrl : function() {
	// return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(this));
	// },
	// /**
	// * Check whether the current String is an external URL address?
	// */
	// isExtUrl : function() {
	// return this.isUrl() && this.indexOf("://" + document.domain) == -1;
	// },
	/**
	 * Format the current String. Replace all "{number}" using values specified by function arguments array.
	 */
	format : function() {
		var args = [];
		if (arguments) {
			if (arguments.length == 1 && $.type(arguments[0]) == "array") {
				args = arguments[0];
			} else {
				args = arguments;
			}
		}
		var result = "" + this;
		for ( var i = 0; i < args.length; i++) {
			result = result.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
		}
		return result;
	},
	/**
	 * convert HTML string to visible text
	 */
	toHtml : function() {
		var str = this.toString();
		if (str == null)
			return "";
		var buf = [];
		for ( var i = 0; i < str.length; ++i) {
			var c = str.charAt(i);
			switch (c) {
			case '<':
			case '>':
			case '&':
			case '"':
			case '\'':
			case '\r':
			case '\t':
			case '\\':
				buf.push("&#" + c.charCodeAt(0) + ";");
				break;
			case ' ':
				buf.push("&nbsp;");
				break;
			case '\n':
				buf.push("<br/>");
				break;
			default:
				buf.push(c);
				break;
			}
		}
		return buf.join('');
	},
	// toArray : function(sep) {
	// return this.split(sep);
	// },
	/**
	 * Parse String to Integer value
	 */
	_int : function() {
		var ret = parseInt(this.toString());
		if (isNaN(ret)) {
			ret = 0;
		}
		return ret;
	},
	toJson : function() {
		return $.parseJSON(this.toString());
	}
});
//
// $.type = function(o) {
// var _toS = Object.prototype.toString;
// var _types = {
// 'undefined' : 'undefined',
// 'number' : 'number',
// 'boolean' : 'boolean',
// 'string' : 'string',
// '[object Function]' : 'function',
// '[object RegExp]' : 'regexp',
// '[object Array]' : 'array',
// '[object Date]' : 'date',
// '[object Error]' : 'error'
// };
// return _types[typeof o] || _types[_toS.call(o)] || (o ? 'object' : 'null');
// };
var $specialChars = {
	'\b' : '\\b',
	'\t' : '\\t',
	'\n' : '\\n',
	'\f' : '\\f',
	'\r' : '\\r',
	'"' : '\\"',
	'\\' : '\\\\'
};
var $replaceChars = function(chr) {
	return $specialChars[chr] || '\\u00' + Math.floor(chr.charCodeAt() / 16).toString(16) + (chr.charCodeAt() % 16).toString(16);
};
/**
 * 将JSON对象转换成字符串
 */
$.toJsonString = function(o) {
	var s = [];
	switch ($.type(o)) {
	case 'undefined':
		return 'undefined';
		break;
	case 'null':
		return 'null';
		break;
	case 'number':
	case 'boolean':
	case 'date':
	case 'function':
		return o.toString();
		break;
	case 'string':
		return '"' + o.replace(/[\x00-\x1f\\"]/g, $replaceChars) + '"';
		break;
	case 'array':
		for ( var i = 0, l = o.length; i < l; i++) {
			s.push($.toJsonString(o[i]));
		}
		return '[' + s.join(',') + ']';
		break;
	case 'error':
	case 'object':
		for ( var p in o) {
			s.push('"' + p + '":' + $.toJsonString(o[p]));
		}
		return '{' + s.join(',') + '}';
		break;
	default:
		return '';
		break;
	}
};
$.parseOptions = function(sourceHTML, props) {
	var $target = $(sourceHTML);
	var opts = {};
	var s = $.trim($target.attr("data-options"));
	if (s) {
		var flag = s.substring(0, 1);
		var _a = s.substring(s.length - 1, 1);
		if (flag != "{") {
			s = "{" + s;
		}
		if (_a != "}") {
			s = s + "}";
		}
		opts = $fn(s);
	}
	if (props) {
		var options = {};
		for ( var i = 0; i < props.length; i++) {
			var pp = props[i];
			if (typeof pp == "string") {
				if (pp == "width" || pp == "height" || pp == "left" || pp == "top") {
					options[pp] = parseInt(sourceHTML.style[pp]) || undefined;
				} else {
					options[pp] = $target.attr(pp);
				}
			} else {
				for ( var name in pp) {
					var type = pp[name];
					var value = $target.attr(name);
					if (typeof value != "undefined" && ("" + value).trim().length > 0) {
						if (type == "b") {
							if (value == "true" || value == true)
								options[name] = true;
							else if (value == "false" || value == false)
								options[name] = false
						} else if (type == "n") {
							options[name] = parseFloat(value);
							// } else if (type == "json") {
							// options[name] = jCocit.toJson(value);
							// } else if (type == "function") {
							// options[name] = $fn(value);
						}
					}
				}
			}
		}
		$.extend(opts, options);
	}
	return opts;
};

function checkHeartbeat_coc() {
	$.ajax({
		type : 'POST',
		url : "/coc/chkHeartbeat/" + new Date().getTime(),
		async : true,
		data : "",
		dataType : "json",
		success : function(json) {
			_sto(checkHeartbeat, 10800000);//3*60*60*1000
		},
		error : function(jqXHR, statusText, responseError) {
			alert("访问服务器出错！"+responseError);
		}
	});
}