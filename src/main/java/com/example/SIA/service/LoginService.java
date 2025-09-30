package com.example.SIA.service;

import com.example.SIA.dto.LoginRequest;
import com.example.SIA.dto.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);
    void logout();
}
