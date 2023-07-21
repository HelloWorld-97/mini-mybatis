package com.danzz.reflection.invoker;

import java.lang.reflect.Method;

public class MethodInvoker implements Invoker {

    private Method method;
    private Class<?> type;

    public MethodInvoker(Method method) {
        this.method = method;
        if (method.getParameterCount() == 1) {
            this.type = method.getParameterTypes()[0];
        } else {
            this.type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object obj, Object[] args) throws Exception {
        return null;
    }

    @Override
    public Class<?> type() {
        return null;
    }
}
