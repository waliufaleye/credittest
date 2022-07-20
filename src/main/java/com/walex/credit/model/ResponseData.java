/**
 * 
 */
package com.walex.credit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author waliu.faleye
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {

	private String status;

	private String message;
}
