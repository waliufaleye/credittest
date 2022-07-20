/**
 * 
 */
package com.walex.credit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walex.credit.model.CreditDetail;

/**
 * @author waliu.faleye
 *
 */
public interface CreditDetailRepository extends JpaRepository<CreditDetail, Long> {

}
