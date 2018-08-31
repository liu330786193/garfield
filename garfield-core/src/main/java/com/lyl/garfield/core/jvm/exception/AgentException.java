package com.lyl.garfield.core.jvm.exception;

public class AgentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AgentException(String message){
        super(message);
    }

    public AgentException(String message, Throwable e){
        super(message, e);
    }

}
