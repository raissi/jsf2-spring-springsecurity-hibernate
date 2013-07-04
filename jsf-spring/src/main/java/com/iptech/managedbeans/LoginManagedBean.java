package com.iptech.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.iptech.security.Authority;
import com.iptech.security.RememberMeService;

@Component
@Scope("session")
public class LoginManagedBean implements Serializable{
	private static final long serialVersionUID = 8687174499215715900L;
	
	private String username;
	private String password;
	private Boolean rememberMe = false;
	
	@Inject
	@Named("userService")
	private UserDetailsService userService;
	
	@Inject
	@Named("rememberMeServices")
	private RememberMeService rememberMeService;

	public String loginUser(){
		System.out.println("Username: "+username+" password: "+password);
		FacesContext context = FacesContext.getCurrentInstance();
		if("admin".equals(username) && "admin".equals(password)){
			//In your login method:
			List<Authority> auths = new ArrayList<Authority>();
			auths.add(new Authority("ROLE_USER")); //Role here, like "admin"
			Authentication authentication =  new UsernamePasswordAuthenticationToken(userService.loadUserByUsername(username), null, auths);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			if(rememberMe){
				HttpServletRequest request =  (HttpServletRequest) context.getExternalContext().getRequest();
				HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
				    @Override public String getParameter(String name) { return "true"; }            
				};
				HttpServletResponse response =(HttpServletResponse) context.getExternalContext().getResponse();
				rememberMeService.onLoginSuccess(wrapper, response, authentication);  
			}
			return "pretty:page3";
		}else{
			 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Login Error", "Invalid credentials"));
		}
		return null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	
}
