package com.cocit.api.common;

import java.util.List;

import com.cocit.api.entity.INamedEntity;
import com.cocit.api.entity.ITreeEntity;

public interface IDic extends INamedEntity, ITreeEntity {
	IDic getParent();
	
	List<? extends IDic> getDics();
}
