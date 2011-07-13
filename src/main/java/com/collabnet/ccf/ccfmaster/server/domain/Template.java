package com.collabnet.ccf.ccfmaster.server.domain;


public interface Template<T> extends Mapping<T> {
	public String getName();
	public void setName(String name);
	public Directions getDirection();
	public void setDirection(Directions direction);
}
