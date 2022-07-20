/**
 * 
 */
package com.walex.credit.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.walex.credit.model.Privilege;
import com.walex.credit.model.Role;
import com.walex.credit.model.User;
import com.walex.credit.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author waliu.faleye
 *
 */
@Service
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {// , UserDetailsService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	PasswordEncoder pwdEncoder;

	// @Override
	public User loadUserByUsername(String userName) throws UsernameNotFoundException {
		// List<APIUser> users = new ArrayList<APIUser>();
		User user = null;
		user = userRepo.findUserByUserName(userName);
		UserBuilder builder = null;
		if (user != null && "A".equalsIgnoreCase(user.getStatus()) && "N".equalsIgnoreCase(user.getDelFlg())) {
			builder = org.springframework.security.core.userdetails.User.withUsername(userName);
			builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
			builder.roles(user.getUserRoleData().getRoleDescription());
		} else {
			throw new UsernameNotFoundException(
					user.getUserName().toUpperCase().concat(" User not found. User Inactive or Deleted"));
		}

		// return builder.build();
		return user;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		String name = authentication.getName();
		String password = "";
		// List<APIUser> users = new ArrayList<APIUser>();
		List<Role> roles = new ArrayList<Role>();
		User user = null;
		// log.info("authentication.getCredentials()===" +
		// authentication.getCredentials());
		if (authentication.getCredentials() != null) {
			password = authentication.getCredentials().toString();

			log.info("name===" + name);
			// log.info("password===" + password);
			user = userRepo.findUserByUserName(name);
			if (user == null) {
				throw new UsernameNotFoundException("Not a valid User credential");
			} else {
				if (pwdEncoder.matches(password, user.getPassword())) {
					if ("A".equalsIgnoreCase(user.getStatus()) && "N".equalsIgnoreCase(user.getDelFlg())) {
						// roles.add(user.getUserRoleData());
					} else {
						if ("I".equalsIgnoreCase(user.getStatus())) {
							throw new UsernameNotFoundException(
									user.getUserName().toUpperCase().concat(" status is Inactive. Reset Credentials."));
						} else {
							if ("Y".equalsIgnoreCase(user.getDelFlg())) {
								throw new UsernameNotFoundException(
										user.getUserName().toUpperCase().concat(" Deleted. Contact System Admin."));
							}
						}
					}
				} else {
					throw new UsernameNotFoundException(
							user.getUserName().toUpperCase().concat(" uses wrong password"));
				}
			}
		}
		Authentication auth = new UsernamePasswordAuthenticationToken(user, password, this.getAuthorities(roles));// ,this.getAuthorities(roles));
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(Collection<Role> roles) {

		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

}
