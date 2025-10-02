package emt.emtlab.services.application.service;

import emt.emtlab.services.application.dto.auth.DisplayUserDto;
import emt.emtlab.services.application.dto.auth.LoginRequest;
import emt.emtlab.services.application.dto.auth.RegisterRequest;

import java.util.Optional;

public interface AuthApplicationService {

    Optional<DisplayUserDto> register(RegisterRequest createUserDto);

    Optional<DisplayUserDto> login(LoginRequest loginUserDto);

    Optional<DisplayUserDto> findByUsername(String username);

}