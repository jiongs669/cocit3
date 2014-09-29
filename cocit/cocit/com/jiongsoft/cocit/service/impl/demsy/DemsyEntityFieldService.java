// $codepro.audit.disable unnecessaryCast
package com.jiongsoft.cocit.service.impl.demsy;

import static com.kmetop.demsy.Demsy.moduleEngine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.nutz.json.Json;

import com.jiongsoft.cocit.service.FieldService;
import com.jiongsoft.cocit.service.TableService;
import com.jiongsoft.cocit.util.KeyValue;
import com.jiongsoft.cocit.util.ObjectUtil;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.entity.ISoftConfig;
import com.kmetop.demsy.comlib.impl.sft.dic.Dic;
import com.kmetop.demsy.comlib.impl.sft.dic.DicCategory;
import com.kmetop.demsy.comlib.impl.sft.system.AbstractSystemData;
import com.kmetop.demsy.comlib.impl.sft.system.SFTSystem;
import com.kmetop.demsy.engine.BizEngine;

public class DemsyEntityFieldService implements FieldService {
	private AbstractSystemData entity;

	private KeyValue[] dicOptions;

	DemsyEntityFieldService(AbstractSystemData entity) {
		this.entity = entity;
	}

	@Override
	public Long getID() {
		return entity.getId();
	}

	@Override
	public String getName() {
		return entity.getName();
	}

	@Override
	public boolean isDisabled() {
		return entity.isDisabled();
	}

	@Override
	public String getInfo() {
		return entity.getDesc();
	}

	@Override
	public Date getCreatedDate() {
		return entity.getCreated();
	}

	@Override
	public String getCreatedUser() {
		return entity.getCreatedBy();
	}

	@Override
	public Date getLatestModifiedDate() {
		return entity.getUpdated();
	}

	@Override
	public String getLatestModifiedUser() {
		return entity.getUpdatedBy();
	}

	@Override
	public <T> T get(String propName, T defaultReturn) {
		String value = entity.get(propName);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) value;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value, valueType);
		} catch (Throwable e) {
			Log.warn("", e);
		}

		return defaultReturn;
	}

	@Override
	public String getPropName() {
		return entity.getPropName();
	}

	@Override
	public Integer getScale() {
		return entity.getScale();
	}

	@Override
	public Integer getPrecision() {
		Integer prec = entity.getPrecision();

		return prec == null ? 0 : prec;
	}

	@Override
	public byte getType() {
		BizEngine bizEngine = (BizEngine) Demsy.bizEngine;
		if (bizEngine.isBoolean(entity))
			return TYPE_BOOL;
		if (bizEngine.isDate(entity))
			return TYPE_DATE;
		if (bizEngine.isNumber(entity))
			return TYPE_NUMBER;
		if (bizEngine.isUpload(entity))
			return TYPE_UPLOAD;
		if (bizEngine.isSystemFK(entity))
			return TYPE_FK;
		if (bizEngine.isString(entity) && this.getPrecision() > 255)
			return TYPE_TEXT;
		if (bizEngine.isRichText(entity))
			return TYPE_RICH_TEXT;
		if (bizEngine.isString(entity))
			return TYPE_STRING;

		return TYPE_STRING;
	}

	@Override
	public String getPattern() {
		return entity.getPattern();
	}

	@Override
	public boolean isPassword() {
		return entity.isPassword();
	}

	@Override
	public boolean isToString() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public KeyValue getDicOption(Object fieldValue) {
		if (fieldValue == null)
			return null;

		String value = fieldValue.toString();
		if (fieldValue instanceof Boolean) {
			value = ((Boolean) fieldValue) ? "1" : "0";
		}

		return getDicOptionsMap().get(value);
	}

	@Override
	public Map<Object, KeyValue> getDicOptionsMap() {
		Map<Object, KeyValue> ret = new HashMap();
		KeyValue[] options = this.getDicOptions();
		if (options != null) {
			for (KeyValue kv : options) {
				ret.put(kv.getValue(), kv);
			}
		}

		return ret;
	}

	@Override
	public KeyValue[] getDicOptions() {
		if (dicOptions != null)
			return dicOptions;

		String str = entity.getOptions();

		if (!StringUtil.isNil(str)) {

			// 转换字符串为 KeyValue[]。
			dicOptions = convertStringToOptions(str);

		} else if (getType() == TYPE_BOOL) {

			// 转换Bool值为 KeyValue[]。
			dicOptions = new KeyValue[] { KeyValue.make("是", "1"), KeyValue.make("否", "0") };

		} else if (entity.getType().isV1Dic()) {

			// 转换 SFT V1版本的字典数据为 KeyValue[]
			DicCategory dicc = entity.getDicCategory();

			List<KeyValue> oplist = new ArrayList();
			if (dicc != null && !dicc.isDisabled()) {
				List<Dic> list = dicc.getDics();
				for (Dic dic : list) {
					if (!dic.isDisabled() && !dic.isDisabled())
						oplist.add(KeyValue.make(dic.getName(), "" + dic.getId()));
				}
			}

			KeyValue[] dicOptions = new KeyValue[oplist.size()];
			dicOptions = oplist.toArray(dicOptions);
		} else
			dicOptions = new KeyValue[0];

		return dicOptions;
	}

	private static KeyValue[] convertStringToOptions(String str) {
		str = str.trim();
		if (str.charAt(0) == '[') {
			try {
				return Json.fromJson(KeyValue[].class, str);
			} catch (Throwable e) {
				Log.error("解析字段字典选项列表时出错：非法字典选项表达式！(%s)", str, e);
			}
		} else if (str.charAt(0) == '{') {
			String key = str.substring(1, str.length() - 1);
			ISoftConfig config = moduleEngine.getSoftConfig(key);
			if (config != null && !StringUtil.isNil(config.getValue())) {
				return convertStringToOptions(config.getValue());
			}
		} else {
			String[] strs = StringUtil.toArray(str, ",;，；\r\t\n");
			KeyValue[] options = new KeyValue[strs.length];
			int i = 0;
			for (String item : strs) {
				item = item.trim();
				int idx = item.indexOf(":");
				if (idx < 0) {
					idx = item.indexOf("：");
				}
				if (idx > -1) {
					options[i++] = KeyValue.make(item.substring(idx + 1).trim(), item.substring(0, idx).trim());
				} else {
					options[i++] = KeyValue.make(item, item);
				}
			}
			return options;
		}

		return new KeyValue[0];
	}

	@Override
	public boolean isGridField() {
		return entity.isGridField();
	}

	@Override
	public int getGridOrder() {
		return entity.getGridOrder();
	}

	@Override
	public int getGridWidth() {
		return entity.getGridWidth();
	}

	@Override
	public TableService getFkEntityTable() {
		SFTSystem sys = entity.getRefrenceSystem();
		if (sys == null)
			return null;

		return new DemsyEntityTableService(sys);
	}

	@Override
	public boolean isChildEntity() {
		return entity.isMappingToMaster();
	}

	@Override
	public boolean isDisabledNavi() {
		return !entity.isDisabledNavi();
	}

	@Override
	public boolean isManyToMany() {
		return entity.isSysMultiple();
	}

	@Override
	public boolean isCascading() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getUploadType() {
		String types = entity.getUploadType();
		return StringUtil.toArray(types, "|,; ");
	}

	@Override
	public int getSequence() {
		return entity.getOrderby();
	}

	@Override
	public Properties getExtProps() {
		return entity.getDynaProp();
	}

	public AbstractSystemData getEntity() {
		return entity;
	}

	public boolean isFK() {
		if (entity.getRefrenceSystem() != null && !this.isSubTable()) {
			return this.getPropName() != null && entity.getRefrenceField() == null;
		}

		return false;
	}

	private boolean isSubTable() {
		String type = entity.getType().getCode();
		return "SubSystem".equals(type) || "FakeSubSystem".equals(type);
	}

	public boolean isV1Dic() {
		return entity.getType().isV1Dic();
	}

	public boolean isBoolean() {
		return entity.getType().isBoolean();
	}

	@Override
	public String format(Object value) {
		if (value == null)
			return "";

		KeyValue item = this.getDicOption(value);
		if (item != null) {
			return item.getKey();
		}
		if (this.getType() == TYPE_FK) {
			return value.toString();
		}

		return ObjectUtil.format(value, getPattern());
	}

	private String parseMode(String actionMode) {
		String mode = entity.getMode();
		if (actionMode == null || actionMode.trim().length() == 0) {
			actionMode = "v";
		}
		if (mode == null || mode.trim().length() == 0) {
			return "";
		}
		mode = mode.trim();
		String[] dataModes = StringUtil.toArray(mode, " ");
		String defaultActionMode = "*";
		String defaultMode = "";
		for (String dataMode : dataModes) {
			if (dataMode == null) {
				continue;
			}
			dataMode = dataMode.trim();
			int index = dataMode.indexOf(":");
			if (index < 0) {
				continue;
			}
			String actMode = dataMode.substring(0, index);
			if (actMode.equals(actionMode)) {
				return dataMode.substring(index + 1);
			}
			if (defaultActionMode.equals(actMode)) {
				defaultMode = dataMode.substring(index + 1);
			}
		}
		return defaultMode;
	}

	@Override
	public String getMode(String actionMode) {
		return getMode(actionMode, true, null);
	}

	public String getMode(String actionMode, boolean mustPriority, String defalutMode) {
		String ret = parseMode(actionMode);
		if (mustPriority && ret.indexOf("M") > -1) {
			return "M";
		}
		if (!ret.equals("M")) {
			ret = ret.replace("M", "");
		}
		if (ret.length() > 0) {
			return ret;
		}
		// 创建或编辑时：默认可编辑
		if (actionMode.charAt(0) == 'c') {
			if (StringUtil.isNil(defalutMode))
				return "E";
			else
				return defalutMode;
		}
		// 批量修改时：默认不显示
		if (actionMode.startsWith("bu")) {
			if (StringUtil.isNil(defalutMode))
				return "N";
			else
				return defalutMode;
		}
		// 浏览数据时：默认检查模式显示
		if (actionMode.charAt(0) == 'v') {
			if (StringUtil.isNil(defalutMode))
				return "S";
			else
				return defalutMode;
		}

		// 检查模式显示
		return "S";
	}

	@Override
	public String getUiTemplate() {
		return entity.getUiTemplate();
	}
}
