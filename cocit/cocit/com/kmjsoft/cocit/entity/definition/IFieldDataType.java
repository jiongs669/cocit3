package com.kmjsoft.cocit.entity.definition;

import com.kmjsoft.cocit.entity.INamedEntity;

public interface IFieldDataType extends INamedEntity {
	public boolean isBoolean();

	public boolean isV1Dic();

	public boolean isV1GEO();

	public boolean isNumber();

	public boolean isInteger();

	public boolean isRichText();

	public boolean isDate();

	public boolean isString();

	public boolean isUpload();

	public boolean isSystem();

	public boolean isManyToMany();

	public String getType();

	public String getUiTemplate();

}
