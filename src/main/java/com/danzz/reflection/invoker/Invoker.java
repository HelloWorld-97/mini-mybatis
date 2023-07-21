package com.danzz.reflection.invoker;

public interface Invoker {

    Object invoke(Object obj, Object[] args) throws Exception;

    Class<?> type();
}
