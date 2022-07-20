/**
 * 
 */
package com.walex.credit.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.walex.credit.model.CreditDetail;
import com.walex.credit.model.ResponseData;
import com.walex.credit.model.User;

/**
 * @author waliu.faleye
 *
 */
public interface ServiceInterface {
	public User reset(String username);

	public ResponseEntity<ResponseData> createLoan(CreditDetail creditDetail);

	public ResponseEntity<ResponseData> updateLoan(CreditDetail creditDetail);

	public ResponseEntity<ResponseData> deleteLoan(CreditDetail creditDetail);

	public ResponseEntity<Page<CreditDetail>> getLoans(Pageable pageable);

	public ResponseEntity<CreditDetail> getLoanDetailsById(Long id);

}
