package com.jiongsoft.cocit.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiongsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.BaseEntity;
import com.kmjsoft.cocit.entity.IDataEntity;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.entity.definition.IEntityField;
import com.kmjsoft.cocit.entity.security.IAction;
import com.kmjsoft.cocit.entityengine.definition.IEntityDefManager;
import com.kmjsoft.cocit.entityengine.manager.IBizSession;
import com.kmjsoft.cocit.orm.expr.Expr;
import com.kmjsoft.cocit.util.ExcelUtil;

/**
 * 子系统Excel导入导出工具库:
 * 
 * @author yongshan.ji
 * 
 */
public class SystemExcel {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	private IEntityDefManager entityDefManager;

	private Class systemClass;

	private Map<String, IEntityField> fields = new HashMap();// <字段名称, 字段>

	private String[] excelHeads;

	private List<String[]> excelRows;

	public SystemExcel(IEntityDefinition system, IAction action, File excel) throws FileNotFoundException, IOException, DemsyException {
		this.entityDefManager = Demsy.entityDefManager;
		systemClass = entityDefManager.getType(system);

		// 处理自定义系统相关信息
		List<IEntityField> datas = (List<IEntityField>) entityDefManager.getFieldsOfEnabled(system);
		for (IEntityField data : datas) {
			String name = data.getName();
			boolean valid = !data.isDisabled();
			String mode = entityDefManager.getMode(data, action, false, "E");
			valid = valid && (mode.equals("E") || mode.equals("M"));

			if (valid)
				fields.put(name, data);
		}
		if (log.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("系统(" + system.getName() + ")字段：\n");
			for (IEntityField fld : datas) {
				if (fields.get(fld.getName()) == null) {
					continue;
				}
				sb.append(fld.getName()).append("：").append(entityDefManager.getPropName(fld));
				sb.append("\n");
			}
			log.info(sb.toString());
		}

		// 处理excel相关信息
		excelRows = ExcelUtil.parseExcel(excel);
		if (excelRows != null) {
			if (log.isInfoEnabled()) {
				StringBuffer sb = new StringBuffer();
				sb.append("Excel数据:\n");
				for (String[] row : excelRows) {
					for (String cell : row) {
						sb.append(cell).append(", ");
					}
					sb.append("\n");
				}
				log.info(sb.toString());
			}
			if (excelRows.size() > 0) {
				excelHeads = excelRows.get(0);
				excelRows.remove(0);
			}
		}
		// 校验： 验证表头是否重复？是否为系统字段？
		if (excelHeads != null) {
			List<String> listHeads = new ArrayList();
			for (String str : excelHeads) {
				if (listHeads.contains(str)) {
					throw new DemsyException(" Excel数据校验出错：数据表表头不能重复！【" + str + "】列非法");
				}
				if (fields.get(str) == null) {
					throw new DemsyException(" Excel数据校验出错：数据表表头在系统字段中必须存在并在新增时允许编辑！【" + str + "】列非法");
				}
				listHeads.add(str);
			}
			List<String> excelPKs = new ArrayList();
			int len = excelRows.size();
			for (int i = 0; i < len; i++) {
				String[] row = excelRows.get(i);
				if (row == null || row.length <= 1) {
					throw new DemsyException(" Excel数据校验出错：数据表至少需要 2 列！但第 " + (i + 2) + " 行只有 " + (row == null ? 0 : row.length) + " 列");
				}
				if (row[0] == null || row[0].trim().length() == 0) {
					throw new DemsyException(" Excel数据校验出错：数据表的第一列不允许为空！第 " + (i + 2) + " 行第一列非法");
				}
				if (excelPKs.contains(row[0])) {
					throw new DemsyException(" Excel数据校验出错：数据表的第一列不允许重复！第 " + (i + 2) + " 行第一列非法");
				}
				excelPKs.add(row[0]);
			}
		}
	}

	public SystemExcel(IEntityDefinition system, File excel) throws FileNotFoundException, IOException, DemsyException {
		this.entityDefManager = Demsy.entityDefManager;
		systemClass = entityDefManager.getType(system);

		// 处理自定义系统相关信息
		List<IEntityField> datas = (List<IEntityField>) entityDefManager.getFieldsOfEnabled(system);
		for (IEntityField data : datas) {
			String name = data.getName();

			fields.put(name, data);
		}
		if (log.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("系统(" + system.getName() + ")字段：\n");
			for (IEntityField fld : datas) {
				if (fields.get(fld.getName()) == null) {
					continue;
				}
				sb.append(fld.getName()).append("：").append(entityDefManager.getPropName(fld));
				sb.append("\n");
			}
			log.info(sb.toString());
		}

		// 处理excel相关信息
		excelRows = ExcelUtil.parseExcel(excel);
		if (excelRows != null) {
			if (log.isInfoEnabled()) {
				StringBuffer sb = new StringBuffer();
				sb.append("Excel数据:\n");
				for (String[] row : excelRows) {
					for (String cell : row) {
						sb.append(cell).append(", ");
					}
					sb.append("\n");
				}
				log.info(sb.toString());
			}
			if (excelRows.size() > 0) {
				excelHeads = excelRows.get(0);
				excelRows.remove(0);
			}
		}
	}

	public List getRows() throws InstantiationException, IllegalAccessException {
		IBizSession session = Demsy.bizSession;
		String softID = Demsy.me().getSoft().getDataGuid();

		final List<IDataEntity> retList = new ArrayList();
		int excelRowIndex = 1;
		int excelColIndex = 0;
		for (String[] row : excelRows) {
			excelRowIndex++;
			BaseEntity data = (BaseEntity) systemClass.newInstance();
			for (int i = 0; i < row.length; i++) {
				excelColIndex = i + 1;
				if (i >= excelHeads.length) {
					break;
				}
				try {
					IEntityField fld = fields.get(excelHeads[i]);
					String propName = entityDefManager.getPropName(fld);
					String propValue = row[i];

					IEntityDefinition fkSystem = fld.getRefrenceSystem();
					if (fkSystem != null) {
						int dot = propName.indexOf(".");
						if (dot < 0) {
							propName = propName + ".name";
						}
					}
					int dot = propName.indexOf(".");

					/*
					 * 解析外键字段
					 */
					if (dot > 0) {
						log.debug(propName);

						String nextProp = propName.substring(dot + 1);
						propName = propName.substring(0, dot);

						if (fkSystem == null && fld.getRefrenceField() != null) {
							fkSystem = fld.getRefrenceField().getSystem();
						}
						if (fkSystem == null) {
							throw new DemsyException("Excel表中的第 " + excelColIndex + " 列【" + fld.getName() + "】是外键字段，但引用的系统不存在！");
						}
						Object fkValue = null;
						if (!Str.isEmpty(propValue)) {
							Class fkClass = entityDefManager.getType(fkSystem);
							fkValue = session.load(fkClass, Expr.eq(nextProp, propValue));
							if (fkValue == null) {
								// throw new DemsyException("Excel表中的第 " + colIndex + " 列【" + fld.getName() + "】是外键字段，但第 " + rowIndex + " 行【" + propValue + "】在【" + fkSystem.getName() + "】模块中不存在！");
								fkValue = fkClass.newInstance();
								Obj.setValue(fkValue, nextProp, propValue);
							}
						}
						Obj.setValue(data, propName, fkValue);
					} else {
						/*
						 * 解析字典字段
						 */
						Option[] options = entityDefManager.getOptions(fld);
						if (options != null && options.length > 0) {
							for (Option option : options) {
								if (option.getText().equals(propValue)) {
									propValue = option.getValue();
									break;
								}
							}
						}

						//
						if (propValue != null && propValue.trim().length() > 0)
							Obj.setValue(data, propName, propValue);
					}
				} catch (Throwable e) {
					throw new DemsyException("导入Excel中的第 " + excelRowIndex + " 行第 " + excelColIndex + " 列时出错！" + e.getMessage());
				}
			}

			//
			data.setTenantGuid(softID);

			retList.add(data);
		}

		return retList;
	}

	private List getDataRows() throws InstantiationException, IllegalAccessException {
		IBizSession session = Demsy.bizSession;
		String softID = Demsy.me().getSoft().getDataGuid();

		IEntityField pkfld = fields.get(excelHeads[0]);
		final List<IDataEntity> list = new ArrayList();
		int rowIndex = 1;
		int colIndex = 0;
		for (String[] row : excelRows) {
			rowIndex++;
			BaseEntity data = (BaseEntity) session.load(systemClass, Expr.eq(entityDefManager.getPropName(pkfld), row[0]));
			if (data == null) {
				data = (BaseEntity) systemClass.newInstance();
			}
			for (int i = 0; i < row.length; i++) {
				colIndex = i + 1;
				try {
					IEntityField fld = fields.get(excelHeads[i]);
					String propname = entityDefManager.getPropName(fld);
					String propvalue = row[i];

					IEntityDefinition fksystem = fld.getRefrenceSystem();
					if (fksystem != null) {
						int dot = propname.indexOf(".");
						if (dot < 0) {
							propname = propname + ".name";
						}
					}

					int dot = propname.indexOf(".");
					// 设置外键值
					if (dot > 0) {
						log.debug(propname);

						String nextprop = propname.substring(dot + 1);
						propname = propname.substring(0, dot);

						if (fksystem == null && fld.getRefrenceField() != null) {
							fksystem = fld.getRefrenceField().getSystem();
						}
						if (fksystem == null) {
							throw new DemsyException("Excel表中的第 " + colIndex + " 列【" + fld.getName() + "】是外键字段，但引用的系统不存在！");
						}
						Object fkvalue = null;
						if (!Str.isEmpty(propvalue)) {
							fkvalue = session.load(entityDefManager.getType(fksystem), Expr.eq(nextprop, propvalue));
							if (fkvalue == null) {
								throw new DemsyException("Excel表中的第 " + colIndex + " 列【" + fld.getName() + "】是外键字段，但第 " + rowIndex + " 行【" + propvalue + "】在【" + fksystem.getName() + "】模块中不存在！");
							}
						}
						Obj.setValue(data, propname, fkvalue);
					} else {
						Obj.setValue(data, propname, propvalue);
					}
				} catch (Throwable e) {
					throw new DemsyException("导入Excel中的第 " + rowIndex + " 行第 " + colIndex + " 列时出错！" + e.getMessage());
				}
			}

			//
			data.setTenantGuid(softID);

			list.add(data);
		}

		return list;
	}

	public int save() throws InstantiationException, IllegalAccessException, DemsyException {
		IBizSession session = Demsy.bizSession;
		try {
			session.save(this.getDataRows());
		} catch (Throwable e) {
			log.error(Ex.msg(e));
			throw new DemsyException(e);
		}
		return excelRows.size();
	}
}
