package com.walex.credit;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.walex.credit.config.CreditProperties;
import com.walex.credit.interfaces.ServiceInterface;
import com.walex.credit.model.CreditDetail;
import com.walex.credit.model.ResponseData;

@SpringBootTest
class CredittestApplicationTests {

	// @Test
	void contextLoads() {
	}

	@Autowired
	ServiceInterface serviceInterface;

	@Autowired
	CreditProperties creditProp;

	@Test
	public void createCreditTest() {
		CreditDetail creditDetail = new CreditDetail();
		creditDetail.setAmountValue(new BigDecimal("10"));
		creditDetail.setBranchId("1234");
		creditDetail.setCurrencyCode("USD");
		creditDetail.setCustomerId("1234");
		creditDetail.setLoanAccountScheme("SBA");
		creditDetail.setLoanPeriodDays(90L);
		creditDetail.setLoanPeriodMonths(3L);
		creditDetail.setNoOfInstallmental(3L);
		creditDetail.setRePmtMethod("Transfer");

		ResponseEntity<ResponseData> response = serviceInterface.createLoan(creditDetail);
		if (response != null && response.hasBody())
			Assertions.assertEquals(creditProp.getSuccessCode(), response.getBody().getStatus());
	}

	@Test
	public void updateCreditTest() {
		CreditDetail creditDetail = new CreditDetail();
		creditDetail.setRePmtMethod("Cash");
		creditDetail.setId(2L);

		ResponseEntity<ResponseData> response = serviceInterface.updateLoan(creditDetail);
		if (response != null && response.hasBody())
			Assertions.assertEquals(creditProp.getSuccessCode(), response.getBody().getStatus());

	}

	@Test
	public void deleteCreditTest() {
		CreditDetail creditDetail = new CreditDetail();
		creditDetail.setId(3L);

		ResponseEntity<ResponseData> response = serviceInterface.deleteLoan(creditDetail);
		if (response != null && response.hasBody())
			Assertions.assertEquals(creditProp.getSuccessCode(), response.getBody().getStatus());

	}

	@Test
	public void fetchCreditsTest() {
		Pageable pageable = Pageable.ofSize(10);

		ResponseEntity<Page<CreditDetail>> response = serviceInterface.getLoans(pageable);
		if (response != null && response.hasBody())
			Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	public void getLoanDetailsByIdTest() {

		ResponseEntity<CreditDetail> response = serviceInterface.getLoanDetailsById(2L);
		if (response != null && response.hasBody())
			Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

	}
}
