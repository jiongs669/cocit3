package com.jiongsoft.cocit.ctx;

public interface IIoc {

	<T> T get(String name);

	IIoc init(String... paths);

	<T> T getIoc();
}
