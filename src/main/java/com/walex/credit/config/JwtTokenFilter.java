/**
 * 
 */
package com.walex.credit.config;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import com.walex.credit.model.Privilege;
import com.walex.credit.model.Role;
import com.walex.credit.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * @author waliu.faleye
 *
 */
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {
	private String secret;

	private static final String BEARER = "Bearer";

	private CustomAuthenticationProvider customAuthenticationProvider;

	public JwtTokenFilter(CustomAuthenticationProvider customAuthenticationProvider, String secret) {
		this.customAuthenticationProvider = customAuthenticationProvider;
		this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {

		try {
			String headerValue = ((HttpServletRequest) req).getHeader("Authorization");
			getBearerToken(headerValue).ifPresent(token -> {
				String username = getClaimFromToken(token, Claims::getSubject);
				User userDetails = customAuthenticationProvider.loadUserByUsername(username);
				List<Role> roles = new ArrayList<Role>();
				log.info("username===" + username);
				try {
					if (username.equals(userDetails.getUserName()) && !invalidToken(userDetails, token)
							&& !isJwtExpired(token)) {
						roles.add(userDetails.getUserRoleData());
						log.info("roles.size()===" + roles.size());
						List<GrantedAuthority> authorities = (List<GrantedAuthority>) this.getAuthorities(roles);
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, userDetails.getPassword(), authorities);
						usernamePasswordAuthenticationToken.setDetails(
								new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) req));
						log.info("authorities.size()===" + authorities.size());
						// log.info("authorities.size()==="+usernamePasswordAuthenticationToken.get);
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (ExpiredJwtException e) {
			// most likely an ExpiredJwtException, but this will handle any
			req.setAttribute("exception", e);
			((HttpServletResponse) res).setStatus(HttpServletResponse.SC_FORBIDDEN);
			// ((HttpServletResponse) res).set
		}

		filterChain.doFilter(req, res);
	}

	private Optional<String> getBearerToken(String headerVal) {
		if (headerVal != null && headerVal.startsWith(BEARER)) {
			return Optional.of(headerVal.replace(BEARER, "").trim());
		}
		return Optional.empty();
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}

	private Boolean isJwtExpired(String token) {
		Date expirationDate = getClaimFromToken(token, Claims::getExpiration);
		return expirationDate.before(new Date());
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

		log.info("privileges.size()" + privileges.size());
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

	private boolean invalidToken(User user, String token) throws ParseException {
		Date expirationDate = getClaimFromToken(token, Claims::getExpiration);
		SimpleDateFormat formatter6 = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
		log.info("" + expirationDate);
		log.info("" + user.getExpiration());
		log.info("" + expirationDate.before(formatter6.parse(user.getExpiration())));
		return expirationDate.before(formatter6.parse(user.getExpiration()));
	}
}
