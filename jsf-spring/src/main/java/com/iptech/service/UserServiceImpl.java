package com.iptech.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {
System.out.println("Getting access details from employee dao !!");
        
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
				return null;
			}
		};
		return user;
	}

}
