package com.danzz.dao;

import com.danzz.entity.User;

public interface UserMapper {

    User queryUserById(Long id);

    void deleteUserById(String id);
}
