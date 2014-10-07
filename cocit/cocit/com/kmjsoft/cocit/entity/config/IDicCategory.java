package com.kmjsoft.cocit.entity.config;

import java.util.List;

import com.kmjsoft.cocit.entity.INamedEntity;
import com.kmjsoft.cocit.entity.ITreeEntity;

public interface IDicCategory extends INamedEntity, ITreeEntity {
	List<? extends IDic> getDics();
}
