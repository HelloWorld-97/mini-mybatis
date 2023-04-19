package com.danzz.dynamicProxy;

public interface UserQueryDao {

    User queryUserById(String id);

    void deleteUserById(String id);
}
