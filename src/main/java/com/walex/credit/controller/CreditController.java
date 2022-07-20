/**
 * 
 */
package com.walex.credit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.walex.credit.config.JwtProvider;
import com.walex.credit.interfaces.ServiceInterface;
import com.walex.credit.model.CreditDetail;
import com.walex.credit.model.ResetUser;
import com.walex.credit.model.ResponseData;
import com.walex.credit.model.SigninUser;
import com.walex.credit.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author waliu.faleye
 *
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1")
public class CreditController {

	private final ServiceInterface serviceInte;

	private final JwtProvider jwtProvider;

	private final AuthenticationManager authenticationManager;

	@PostMapping("/reset")
	public User reset(@RequestBody ResetUser apiUser) {
		User loginUser = null;
		loginUser = serviceInte.reset(apiUser.getUserName());
		return loginUser;
	}

	@PostMapping("/signin")
	public String signIn(@RequestBody SigninUser apiUser) {
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(apiUser.getUserName(), apiUser.getPassword()));
			log.info("auth for user, " + auth);
			return jwtProvider.createToken(apiUser.getUserName(), apiUser.getPassword());
		} catch (AuthenticationException e) {
			e.printStackTrace();
			log.info("Log in failed for user, " + apiUser.getUserName());
		}

		return "";
	}

	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	@PostMapping("/createLoan")
	@PreAuthorize("hasAuthority('createLoan')")
	public ResponseEntity<ResponseData> createLoan(@RequestBody @Valid CreditDetail creditDetail) {
		return serviceInte.createLoan(creditDetail);
	}

	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	@PostMapping("/updateLoan")
	@PreAuthorize("hasAuthority('updateLoan')")
	public ResponseEntity<ResponseData> updateLoan(@RequestBody CreditDetail creditDetail) {
		return serviceInte.updateLoan(creditDetail);
	}

	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	@DeleteMapping("/deleteLoan")
	@PreAuthorize("hasAuthority('deleteLoan')")
	public ResponseEntity<ResponseData> deleteLoan(@RequestBody CreditDetail creditDetail) {
		return serviceInte.deleteLoan(creditDetail);
	}

	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	@GetMapping("/getLoans")
	@PreAuthorize("hasAuthority('getLoans')")
	public ResponseEntity<Page<CreditDetail>> getLoans(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return serviceInte.getLoans(pageable);
	}

	@Operation(security = { @SecurityRequirement(name = "bearer-key") })
	@GetMapping("/getLoanDetailsById/{loanDetailsId}")
	@PreAuthorize("hasAuthority('getLoanDetailsById')")
	public ResponseEntity<CreditDetail> getLoanDetailsById(
			@PathVariable(name = "loanDetailsId", required = true) Long loanDetailsId) {
		return serviceInte.getLoanDetailsById(loanDetailsId);
	}

	@ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
