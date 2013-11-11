package com.collabnet.ccf.ccfmaster.server.domain;

public interface Template<T> extends Mapping<T> {
    public Directions getDirection();

    public String getName();

    public void setDirection(Directions direction);

    public void setName(String name);
}
