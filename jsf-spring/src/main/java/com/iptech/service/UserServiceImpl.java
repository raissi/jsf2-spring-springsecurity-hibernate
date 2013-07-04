package com.iptech.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iptech.security.Authority;

@Service("userService")
public class UserServiceImpl implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {
System.out.println("Getting access details from employee dao !!: "+username);
        
		// Ideally it should be fetched from database and populated instance of
		// #org.springframework.security.core.userdetails.User should be returned from this method
		UserDetails user = new UserDetails() {
			private static final long serialVersionUID = -4810208159912762095L;

			@Override
			public boolean isEnabled() {
				return true;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				return true;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				return true;
			}
			
			@Override
			public String getUsername() {
				return username;
			}
			
			@Override
			public String getPassword() {
				return username;
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				List<Authority> auths = new ArrayList<Authority>();
				auths.add(new Authority("ROLE_USER"));
				return auths;
			}
		};
		Authentication authentication =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return user;
	}

}
