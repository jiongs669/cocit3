package com.kmjsoft.cocit.entity.config;

import java.util.List;

import com.kmjsoft.cocit.entity.INamedEntity;
import com.kmjsoft.cocit.entity.ITreeEntity;

public interface IDic extends INamedEntity, ITreeEntity {
	IDic getParent();
	
	List<? extends IDic> getDics();
}
