package io.github.wxtkm.smartroute.user.service;

import io.github.wxtkm.smartroute.user.dto.request.*;
import io.github.wxtkm.smartroute.user.dto.response.*;

public interface UserService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse updateProfile(
            String email,
            UpdateProfileRequest request
    );

    void changePassword(
            String email,
            ChangePasswordRequest request
    );
}
