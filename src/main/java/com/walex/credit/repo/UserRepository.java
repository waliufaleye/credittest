/**
 * 
 */
package com.walex.credit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walex.credit.model.User;

/**
 * @author waliu.faleye
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

	public User findUserByUserName(String userName);

}
