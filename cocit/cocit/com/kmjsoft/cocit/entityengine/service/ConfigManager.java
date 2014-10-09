package com.kmjsoft.cocit.entityengine.service;

/**
 * 软件配置助理：用于辅助Cocit软件{@link SoftService}完成配置项管理工作。
 * 
 * @author jiongs753
 * 
 */
public interface ConfigManager {

	public <T> T get(String configKey, T defaultReturn);
}
