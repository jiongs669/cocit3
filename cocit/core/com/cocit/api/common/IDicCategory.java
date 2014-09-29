package com.cocit.api.common;

import java.util.List;

import com.cocit.api.entity.INamedEntity;
import com.cocit.api.entity.ITreeEntity;

public interface IDicCategory extends INamedEntity, ITreeEntity {
	List<? extends IDic> getDics();
}
