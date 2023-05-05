package com.danzz.typealias;

import java.util.HashMap;
import java.util.Locale;

public class TypeAliasRegister {

    private static HashMap<String, Class> typeAliasMap = new HashMap<String, Class>() {{
        put("string", String.class);
        put("int", Integer.class);
        put("long", Long.class);
        put("double", Double.class);
        put("byte", Byte.class);
        put("float", Float.class);
        put("char", Character.class);
        put("boolean", Boolean.class);
        put("short", Short.class);
    }};

    public static void typeAliasRegistry(String typeName, Class value) {
        typeAliasMap.put(typeName.toLowerCase(Locale.ENGLISH), value);
    }

    public static Class resolveTypeAlias(String typeName) {
        return typeAliasMap.getOrDefault(typeName.toLowerCase(Locale.ENGLISH), null);
    }

}
