package com.danzz.dao;

import com.danzz.entity.User;

public interface UserQueryDao {

    User queryUserById(String id);

    void deleteUserById(String id);
}
