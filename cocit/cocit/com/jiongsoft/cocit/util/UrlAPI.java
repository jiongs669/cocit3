package com.jiongsoft.cocit.util;

/**
 * Action 工具类: 服务于 actions 包下的 XxxAction 类。
 * 
 * @author yongshan.ji
 * 
 */
public abstract class UrlAPI {

	/**
	 * 编码路径参数
	 * 
	 * @param opArgs
	 * @return
	 */
	public static String encodeArgs(Object... args) {
		StringBuffer sb = new StringBuffer();
		for (Object str : args) {
			if (str == null)
				str = "";

			sb.append(str + ":");
		}

		return sb.toString();

		// return StringUtil.encodeHex(sb.toString());
	}

	/**
	 * 解码路径参数
	 * 
	 * @param opArgs
	 * @return
	 */
	public static String[] decodeArgs(String args) {
		String str = args;

		// String str = StringUtil.decodeHex(opArgs);

		return StringUtil.toArray(str, ":");
	}

	/**
	 * 将JSP路径参数转换成以“/”开头的相对路径
	 * 
	 * @param jspPathArgs
	 *            JSP页面路径，有子目录的用冒号分隔，如: /visit/index 路径为： visit:index
	 * @return 返回 /visit/index
	 */
	public static String makeJspPath(String jspPathArgs) {
		return "/" + jspPathArgs.replace(':', '/');
	}

	/*
	 * 参数默认值
	 */

	public static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * JSP 目录
	 */
	public static final String JSP_DIR = "/WEB-INF/jsp/coc";

	/*
	 * 以下是数据管理模块相关功能的访问路径。
	 */

	/**
	 * “业务模块”命名空间（URL Namespace）：路径前缀。
	 */
	public static final String URL_NS = "/coc";

	/**
	 * 获取实体模块管理界面：模块界面可以包括一个主表界面和多个从表界面组成的Tabs界面。
	 * <p>
	 * 参数：moduleID
	 */
	public static final String GET_ENTITY_MODULE_UI = URL_NS + "/getEntityModuleUI/*";

	/**
	 * 获取实体数据表管理界面：数据表管理界面包括左边导航树、顶部操作菜单、GRID；不包括子表。
	 * <p>
	 * 参数：moduleID:tableID
	 */
	public static final String GET_ENTITY_TABLE_UI = URL_NS + "/getEntityTableUI/*";

	public static final String GET_ENTITY_SELECTION_TABLE_UI = URL_NS + "/getEntitySelectionTableUI/*";

	/**
	 * 获取实体表Grid数据：数据格式通常为JSON或XML格式。
	 * <p>
	 * 参数：moduleID:tableID
	 */
	public static final String GET_ENTITY_GRID_DATA = URL_NS + "/getEntityGridData/*";

	/**
	 * 获取实体表列表数据：用于生成Combobox下拉列表。
	 * <p>
	 * 参数：moduleID:tableID
	 */
	public static final String GET_ENTITY_LIST_DATA = URL_NS + "/getEntityListData/*";

	public static final String GET_ENTITY_TREE_DATA = URL_NS + "/getEntityTreeData/*";

	/**
	 * 获取实体导航数据：用于生成实体管理模块中的左边导航树。
	 * <p>
	 * 参数：moduleID:tableID
	 */
	public static final String GET_ENTITY_NAVI_DATA = URL_NS + "/getEntityNaviData/*";

	/**
	 * 获取实体数据行表单：用于添加、修改数据。
	 * <p>
	 * 参数1：moduleID:tableID:opMode
	 * <p>
	 * 参数2：rowID
	 */
	public static final String GET_ENTITY_ROW_FORM = URL_NS + "/getEntityRowForm/*";

	public static final String GET_ENTITY_ROWS_FORM = URL_NS + "/getEntityRowsForm/*";

	/**
	 * 保存实体数据行：将实体数据行保存到数据库中，保存的可能是表单数据。
	 */
	public static final String SAVE_ENTITY_ROW = URL_NS + "/saveEntityRow/*";

	public static final String SAVE_ENTITY_ROWS = URL_NS + "/saveEntityRows/*";

	/**
	 * 删除“数据行集”：即批量删除“数据行集”指定的实体数据。操作码：299
	 * <p>
	 * 参数1：moduleID:tableID:opMode
	 * <p>
	 * 参数2：rows：逗号分隔的实体数据ID列表，rows是必需的。
	 */
	public static final String DEL_ENTITY_ROWS = URL_NS + "/delEntityRows/*";

	/**
	 * 按“查询条件”执行删除操作：即批量删除满足“查询条件”的所有实体数据集合。
	 * <p>
	 * 参数1：moduleID:tableID:opMode
	 * <p>
	 * 参数2：rows：逗号分隔的实体数据ID列表，如果rows存在则，查询条件失效。
	 */
	public static final String DEL_ENTITY_ON_EXPR = URL_NS + "/delEntityOnExpr/*";

	/**
	 * 基于“单行数据”执行业务逻辑。
	 * <p>
	 * “单行数据”：指一条数据的数据ID。如：/runPluginOnRow/1:2:c/3
	 * <p>
	 * 3——即为单行数据ID
	 */
	public static final String RUN_PLUGIN_ON_ROW = URL_NS + "/runPluginOnRow/*";

	/**
	 * 基于“数据行集”执行业务逻辑。
	 * <p>
	 * “数据行集”：指由多条数据ID组成的数组。如：“/runPluginOnRows/1:2:c1/1,2,3,5,7,89,567”
	 * <p>
	 * 1,2,3,5,7,89,567——即为“数据行集”ID数组
	 */
	public static final String RUN_PLUGIN_ON_ROWS = URL_NS + "/runPluginOnRows/*";

	/**
	 * 基于“查询条件”执行业务逻辑。操作码：204
	 * <p>
	 * “查询结果”：满足查询条件所有记录。
	 */
	public static final String RUN_PLUGIN_ON_EXPR = URL_NS + "/runPluginOnExpr/*";

	/**
	 * 导出“查询结果”到Excel文件，之前先获取导出表单，以便设置要导出的Excel字段。
	 */
	public static final String GET_EXPORT_XLS_FORM = URL_NS + "/getExportXlsForm/*";

	/**
	 * 基于“查询条件”导出“查询结果”到Excel文件。
	 * <p>
	 * “查询结果”与“行集”：“行集”是由实体ID组成的数组；而“查询结果”则是满足查询条件的所有记录。
	 */
	public static final String DO_EXPORT_XLS_ON_EXPR = URL_NS + "/doExportXlsOnExpr/*";

	public static final String GET_IMPORT_XLS_FORM = URL_NS + "/getImportXlsForm/*";

	public static final String DO_IMPORT_XLS_ON_EXPR = URL_NS + "/doImportXlsOnExpr/*";

	/**
	 * 获取手机验证码
	 */
	public static final String GET_IMG_VERIFY_CODE = URL_NS + "/getImgVerifyCode";

	public static final String CHK_IMG_VERIFY_CODE = URL_NS + "/chkImgVerifyCode/*";

	/**
	 * 获取短信验证码
	 * <p>
	 * 参数：手机号码
	 */
	public static final String GET_SMS_VERIFY_CODE = URL_NS + "/getSmsVerifyCode/*";

	public static final String GET_SMS_VERIFY_CODE2 = URL_NS + "/getSmsVerifyCode2/*";

	public static final String CHK_SMS_VERIFY_CODE = URL_NS + "/chkSmsVerifyCode/*";

	/**
	 * 心跳监测程序，用来保证浏览器客户端用不过期。
	 */
	public static final String CHK_HEARTBEAT = URL_NS + "/chkHeartbeat/*";

	public static final String GET_FILE_MANAGER = URL_NS + "/getFileManager/*";

	public static final String GET_FILE_GRID_DATA = URL_NS + "/getFileGridData/*";

	public static final String GET_FILE_TREE_DATA = URL_NS + "/getFileTreeData/*";

	public static final String DEL_DISK_FILES = URL_NS + "/deleteDiskFiles/*";

	/*
	 * 以下是报表管理模块相关功能的访问路径
	 */
	// TODO:

	/*
	 * 以下是流程管理模块相关功能的访问路径
	 */
	// TODO:

	/*
	 * 以下是访问网站页面的相关路径
	 */
	/**
	 * 获取JSP页面
	 */
	public static final String GET_JSP_MODEL = "/jsp/*";

	// TODO:
}
