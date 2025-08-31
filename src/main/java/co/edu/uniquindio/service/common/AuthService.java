package co.edu.uniquindio.service.common;

import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.LoginDto;

public interface AuthService {



    TokenDto login(LoginDto loginDto);







}
