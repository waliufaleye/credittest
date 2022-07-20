/**
 * 
 */
package com.walex.credit.services;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walex.credit.config.CreditProperties;
import com.walex.credit.interfaces.ServiceInterface;
import com.walex.credit.model.CreditDetail;
import com.walex.credit.model.ResponseData;
import com.walex.credit.model.User;
import com.walex.credit.repo.CreditDetailRepository;
import com.walex.credit.repo.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author waliu.faleye
 *
 */
@Service
@RequiredArgsConstructor
public class ServiceImpl implements ServiceInterface {
	private final UserRepository apiUserRepo;

	private final PasswordEncoder pwdEncoder;
	private final CreditDetailRepository cdRepo;
	private final CreditProperties creditProp;

	@Override
	public ResponseEntity<ResponseData> createLoan(CreditDetail creditDetail) {
		// TODO Auto-generated method stub
		ResponseEntity<ResponseData> respEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ResponseData response = new ResponseData(creditProp.getFailedCode(), creditProp.getCreateFailedMsg());
		try {
			creditDetail.setStatus(creditProp.getActiveStatus());
			creditDetail = cdRepo.save(creditDetail);
			if (creditDetail != null) {
				httpStatus = HttpStatus.OK;
				response = new ResponseData(creditProp.getSuccessCode(), creditProp.getCreateSuccessMsg());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		respEntity = new ResponseEntity<ResponseData>(response, httpStatus);
		return respEntity;
	}

	@Override
	public ResponseEntity<ResponseData> updateLoan(CreditDetail creditDetail) {
		// TODO Auto-generated method stub
		ResponseEntity<ResponseData> respEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ResponseData response = new ResponseData(creditProp.getFailedCode(), creditProp.getUpdateFailedMsg());
		try {
			Optional<CreditDetail> creditDetailOpt = cdRepo.findById(creditDetail.getId());
			if (creditDetailOpt.isPresent()) {
				CreditDetail originalData = creditDetailOpt.get();
				CreditDetail mergedData = CreditDetail.mergeObjects(originalData, creditDetail);
				creditDetail = cdRepo.save(mergedData);
				if (creditDetail != null) {
					httpStatus = HttpStatus.OK;
					response = new ResponseData(creditProp.getSuccessCode(), creditProp.getUpdateSuccessMsg());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		respEntity = new ResponseEntity<ResponseData>(response, httpStatus);
		return respEntity;
	}

	@Override
	public ResponseEntity<ResponseData> deleteLoan(CreditDetail creditDetail) {
		// TODO Auto-generated method stub
		ResponseEntity<ResponseData> respEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ResponseData response = new ResponseData(creditProp.getFailedCode(), creditProp.getDeleteFailedMsg());
		try {
			Optional<CreditDetail> creditDetailOpt = cdRepo.findById(creditDetail.getId());
			if (creditDetailOpt.isPresent()) {
				CreditDetail originalData = creditDetailOpt.get();
				originalData.setStatus(creditProp.getInactiveStatus());
				creditDetail = cdRepo.save(originalData);
				if (creditDetail != null) {
					httpStatus = HttpStatus.OK;
					response = new ResponseData(creditProp.getSuccessCode(), creditProp.getDeleteSuccessMsg());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		respEntity = new ResponseEntity<ResponseData>(response, httpStatus);
		return respEntity;
	}

	@Override
	public ResponseEntity<Page<CreditDetail>> getLoans(Pageable pageable) {
		// TODO Auto-generated method stub
		ResponseEntity<Page<CreditDetail>> respEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		Page<CreditDetail> response = null;
		try {
			response = cdRepo.findAll(pageable);
			if (response != null && !response.isEmpty()) {
				httpStatus = HttpStatus.OK;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		respEntity = new ResponseEntity<Page<CreditDetail>>(response, httpStatus);
		return respEntity;
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public User reset(String username) {
		// TODO Auto-generated method stub
		User returnUser = new User();
		User user = apiUserRepo.findUserByUserName(username);
		if (user != null) {
			if ("I".equalsIgnoreCase(user.getStatus()) && "N".equalsIgnoreCase(user.getDelFlg())) {
				String pwd = RandomStringUtils.randomAlphanumeric(12);
				returnUser.setUserName(username);
				returnUser.setPassword(pwd);
				returnUser.setResponseMessage(creditProp.getSuccessReset());
				returnUser.setResponseStatus(creditProp.getSuccessCode());
				user.setPassword(pwdEncoder.encode(pwd));
				user.setStatus("A");
				apiUserRepo.save(user);
			} else {
				if ("A".equalsIgnoreCase(user.getStatus())) {
					returnUser.setResponseMessage(creditProp.getActiveUser());
					returnUser.setResponseStatus(creditProp.getFailedCode());
				} else {
					if ("Y".equalsIgnoreCase(user.getDelFlg())) {
						returnUser.setResponseMessage(creditProp.getDeletedUser());
						returnUser.setResponseStatus(creditProp.getFailedCode());
					}
				}
			}
		} else {

			returnUser.setResponseMessage(creditProp.getUserNotExist());
			returnUser.setResponseStatus(creditProp.getFailedCode());
		}
		return returnUser;

	}

	@Override
	public ResponseEntity<CreditDetail> getLoanDetailsById(Long id) {
		// TODO Auto-generated method stub
		ResponseEntity<CreditDetail> respEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		CreditDetail response = null;
		try {
			Optional<CreditDetail> optResponse = cdRepo.findById(id);
			if (optResponse.isPresent()) {
				response = optResponse.get();
				if (response != null) {
					httpStatus = HttpStatus.OK;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		respEntity = new ResponseEntity<CreditDetail>(response, httpStatus);
		return respEntity;
	}
}
