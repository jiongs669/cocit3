(function($, jCocit) {
	$e(jCocit.defaults, {
		loading : "正在加载...",
		title : ""
	});

	if ($.alerts)
		$e($.alerts.defaults, {
			ok : "确定",
			cancel : "取消",
			yes : "是",
			no : "否",
			warn : "警告",
			error : "错误",
			info : "信息",
			success : "成功",
			confirm : "确认",
			prompt : "提示"
		});

	if ($.fn.loginform)
		$e($.fn.loginform.defaults, {
			errorMsg : "用户名或密码非法！"
		});

	if ($.fn.calendar)
		$e($.fn.calendar.defaults, {
			weeks : [ "日", "一", "二", "三", "四", "五", "六" ],
			months : [ "01月", "02月", "03月", "04月", "05月", "06月", "07月", "08月", "09月", "10月", "11月", "12月" ],
			titleFormater : function(year, month) {
				var opts = $d(this, "calendar").options;
				return year + "年" + opts.months[month - 1];
			}
		});

	$e($.fn.combo.defaults, {
		width : 200
	});

	if ($.fn.combodate)
		$e($.fn.combodate.defaults, {
			todayText : "今天",
			closeText : "关闭",
			okText : "确定"
		});

	if ($.fn.combodatetime && $.fn.combodate)
		$e($.fn.combodatetime.defaults, {
			todayText : $.fn.combodate.defaults.todayText,
			closeText : $.fn.combodate.defaults.closeText,
			okText : $.fn.combodate.defaults.okText
		});

	if ($.fn.datagrid)
		$e($.fn.datagrid.defaults, {
			loadMsg : '正在加载数据，请稍待...'
		});

	if ($.fn.pagination)
		$e($.fn.pagination.defaults, {
			beforePageText : '第',// 第?页
			afterPageText : '页 共{pages}页',// 共?页
			displayMsg : '显示{from}-{to}条 共{total}条'
		});

	if (jCocit.entity)
		$e(jCocit.entity.defaults, {
			confirm : "确 定",
			cancel : "取 消",
			unsupport : "不支持该操作！",
			deleteWarn : "你确定要删除选中的 {0} 条记录吗？",
			unselectedAny : "请先选中一条或复选多条记录！",
			unselectedOne : "请先选中一条记录！"
		});

})(jQuery, jCocit);
