package com.danzz.reflection.invoker;

import java.lang.reflect.Field;

public class GetterInvoker implements Invoker {

    private Field field;

    public GetterInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object obj, Object[] args) throws Exception {
        return field.get(obj);
    }

    @Override
    public Class<?> type() {
        return field.getType();
    }
}
