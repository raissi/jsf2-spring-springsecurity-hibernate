package com.iptech.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

public interface RememberMeService {

	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication);
}
