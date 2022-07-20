/**
 * 
 */
package com.walex.credit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author waliu.faleye
 *
 */
@Entity
@JsonInclude(value = Include.NON_NULL)
@Table(name = "user", schema = "creditdb")
public class User {
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "api_user_generator")
	@SequenceGenerator(name = "api_user_generator", allocationSize = 1, sequenceName = "apiuser_gen")
	private Long id;

	@Column(unique = true, name = "userName")
	private String userName;

	@Column(name = "password", nullable = true)
	private String password;

	// @Column(name = "apiKey")
	// private String apiKey;

	// @Column(name = "apiIVKey")
	// private String apiIVKey;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userrole_id", nullable = false)
	private Role userRoleData;

	@JsonIgnore
	@Column(name = "status", length = 1, nullable = false)
	private String status = "I";

	@JsonIgnore
	@Column(name = "delFlg", length = 1, nullable = false)
	private String delFlg = "N";

	@Transient
	private String responseStatus;

	@Transient
	private String responseMessage;

	// @JsonIgnore
	// private boolean includeVAT;

	@JsonIgnore
	private String expiration;

	@JsonIgnore
	@Column(nullable = false)
	private String channel;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userRoleData
	 */
	public Role getUserRoleData() {
		return userRoleData;
	}

	/**
	 * @param userRoleData the userRoleData to set
	 */
	public void setUserRoleData(Role userRoleData) {
		this.userRoleData = userRoleData;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the delFlg
	 */
	public String getDelFlg() {
		return delFlg;
	}

	/**
	 * @param delFlg the delFlg to set
	 */
	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	/**
	 * @return the responseStatus
	 */
	@JsonProperty(value = "status", index = 0)
	public String getResponseStatus() {
		return responseStatus;
	}

	/**
	 * @param responseStatus the responseStatus to set
	 */
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * @return the responseMessage
	 */
	@JsonProperty(value = "message", index = 1)
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * @return the expiration
	 */
	public String getExpiration() {
		return expiration;
	}

	/**
	 * @param expiration the expiration to set
	 */
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
}
