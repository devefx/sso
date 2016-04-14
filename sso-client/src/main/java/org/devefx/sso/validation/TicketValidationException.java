package org.devefx.sso.validation;

/**
 * ticket验证异常
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:37:39
 */
public class TicketValidationException extends Exception {
	
	private static final long serialVersionUID = 4211224404339123411L;

	public TicketValidationException(String message) {
		super(message);
	}
	
	public TicketValidationException(Throwable throwable) {
		super(throwable);
	}
	
	public TicketValidationException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
