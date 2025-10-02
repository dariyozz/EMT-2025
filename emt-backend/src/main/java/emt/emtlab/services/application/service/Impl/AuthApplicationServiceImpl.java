package emt.emtlab.services.application.service.Impl;

import emt.emtlab.services.application.dto.auth.DisplayUserDto;
import emt.emtlab.services.application.dto.auth.LoginRequest;
import emt.emtlab.services.application.dto.auth.RegisterRequest;
import emt.emtlab.services.application.service.AuthApplicationService;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.service.UserDomainService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthApplicationServiceImpl implements AuthApplicationService {

    private final UserDomainService userDomainService;

    public AuthApplicationServiceImpl(UserDomainService userService) {
        this.userDomainService = userService;
    }

    @Override
    public Optional<DisplayUserDto> register(RegisterRequest createUserDto) {
        User user = userDomainService.register(
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                createUserDto.confirmPassword(),
                createUserDto.firstName(),
                createUserDto.lastName(),
                createUserDto.roles()
        );
        return Optional.of(DisplayUserDto.from(user));
    }

    @Override
    public Optional<DisplayUserDto> login(LoginRequest loginUserDto) {
        User user = userDomainService.login(loginUserDto.username(), loginUserDto.password());
        return Optional.of(DisplayUserDto.from(user));
    }

    @Override
    public Optional<DisplayUserDto> findByUsername(String username) {
        return Optional.of(DisplayUserDto.from(userDomainService.findByUsername(username)));
    }

}