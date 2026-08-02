package io.github.wxtkm.smartroute.user.service;

import io.github.wxtkm.smartroute.user.dto.request.RegisterRequest;
import io.github.wxtkm.smartroute.user.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);
}