package org.devefx.sso.validation;

/**
 * ticket验证接口
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:38:00
 */
public interface TicketValidator {
	
	public abstract Assertion validate(String ticket, String service) throws TicketValidationException;
}
