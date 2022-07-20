/**
 * 
 */
package com.walex.credit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author waliu.faleye
 *
 */
@Data
@ConfigurationProperties(value = "credit")
public class CreditProperties {

	private String successCode;

	private String failedCode;

	private String createSuccessMsg;

	private String createFailedMsg;

	private String updateSuccessMsg;

	private String updateFailedMsg;

	private String deleteSuccessMsg;

	private String deleteFailedMsg;

	private String fetchSuccessMsg;

	private String fetchFailedMsg;

	private String activeStatus;

	private String inactiveStatus;

	private String userNotExist;

	private String activeUser;

	private String deletedUser;

	private String successReset;

	private String jwtSecret;

	private long jwtExpiration;
}
