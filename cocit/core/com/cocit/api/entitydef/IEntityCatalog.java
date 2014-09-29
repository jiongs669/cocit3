package com.cocit.api.entitydef;

import com.cocit.api.entity.INamedEntity;
import com.cocit.api.entity.ITreeEntity;

public interface IEntityCatalog<T> extends INamedEntity, ITreeEntity<T> {

	void setCode(String code);

}
