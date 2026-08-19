package io.github.wxtkm.smartroute.user.service;
import io.github.wxtkm.smartroute.common.security.JwtService;
import io.github.wxtkm.smartroute.user.dto.request.LoginRequest;
import io.github.wxtkm.smartroute.user.dto.request.RegisterRequest;
import io.github.wxtkm.smartroute.user.dto.response.AuthResponse;
import io.github.wxtkm.smartroute.user.dto.response.UserResponse;
import io.github.wxtkm.smartroute.user.repository.RoleRepository;
import io.github.wxtkm.smartroute.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}