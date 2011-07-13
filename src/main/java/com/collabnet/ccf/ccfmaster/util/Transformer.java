package com.collabnet.ccf.ccfmaster.util;

public interface Transformer<T,U> {
	public U transform(T t);
}