/**
 * 
 */
package com.walex.credit.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author waliu.faleye
 *
 */
@Entity
@Data
@EqualsAndHashCode
@Table(name = "credit_detail", schema = "creditdb")
public class CreditDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "cd_gen")
	@javax.persistence.TableGenerator(name = "cd_gen", table = "GENERATOR_CD_TABLE", pkColumnName = "ID", initialValue = 1, allocationSize = 1)
	private Long id;

	@NotBlank(message = "Customer ID cannot be blank")
	@NotNull(message = "Customer ID cannot be null")
	@NotEmpty(message = "Customer ID cannot be empty")
	@Column(name = "customer_id")
	private String customerId;

	@NotBlank(message = "Loan Account Scheme cannot be blank")
	@NotNull(message = "Loan Account Scheme cannot be null")
	@NotEmpty(message = "Loan Account Scheme cannot be empty")
	@Column(name = "loan_account_scheme")
	private String loanAccountScheme;

	@NotBlank(message = "Branch ID cannot be blank")
	@NotNull(message = "Branch ID cannot be null")
	@NotEmpty(message = "Branch ID cannot be empty")
	@Column(name = "branch_id")
	private String branchId;

	@NotBlank(message = "Loan Period Days cannot be blank")
	@NotNull(message = "Loan Period Days cannot be null")
	@NotEmpty(message = "Loan Period Days cannot be empty")
	@Column(name = "loan_period_days")
	private Long loanPeriodDays;

	@NotBlank(message = "Loan Period Months cannot be blank")
	@NotNull(message = "Loan Period Months cannot be null")
	@NotEmpty(message = "Loan Period Months cannot be empty")
	@Column(name = "loan_period_months")
	private Long loanPeriodMonths;

	@NotBlank(message = "Repayment Method cannot be blank")
	@NotNull(message = "Repayment Method cannot be null")
	@NotEmpty(message = "Repayment Method cannot be empty")
	@Column(name = "re_pmt_method")
	private String rePmtMethod;

	@NotBlank(message = "Loan Amount cannot be blank")
	@NotNull(message = "Loan Amount cannot be null")
	@NotEmpty(message = "Loan Amount cannot be empty")
	@Column(name = "amount_value")
	private BigDecimal amountValue;

	@NotBlank(message = "Currency Code cannot be blank")
	@NotNull(message = "Currency Code cannot be null")
	@NotEmpty(message = "Currency Code cannot be empty")
	@Column(name = "currency_code")
	private String currencyCode;

	@NotBlank(message = "Number of Installmental cannot be blank")
	@NotNull(message = "Number of Installmental cannot be null")
	@NotEmpty(message = "Number of Installmental cannot be empty")
	@Column(name = "no_of_installmental")
	private Long noOfInstallmental;

	@Column(name = "status")
	private String status;

	@SuppressWarnings("unchecked")
	public static <T> T mergeObjects(T first, T second) {
		Class<?> clas = first.getClass();
		Field[] fields = clas.getDeclaredFields();
		Object result = null;
		try {
			result = clas.getDeclaredConstructor().newInstance();
			for (Field field : fields) {
				field.setAccessible(true);
				Object value1 = field.get(first);
				Object value2 = field.get(second);
				Object value = (value2 != null) ? value2 : value1;
				field.set(result, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) result;
	}
}
