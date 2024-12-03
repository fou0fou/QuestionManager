package com.foufou.service;


import com.foufou.dto.UserLoginDTO;
import com.foufou.entity.User;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
