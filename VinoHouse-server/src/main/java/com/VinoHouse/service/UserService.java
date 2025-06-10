package com.VinoHouse.service;

import com.VinoHouse.dto.UserLoginDTO;
import com.VinoHouse.entity.User;

public interface UserService {

    /**
     * 微信登录
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
