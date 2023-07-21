package com.danzz.reflection.invoker;

import java.lang.reflect.Field;

public class SetterInvoker implements Invoker {

    private Field field;

    public SetterInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object obj, Object[] args) throws Exception {
        field.set(obj, args[0]);
        return null;
    }

    @Override
    public Class<?> type() {
        return field.getType();
    }
}
