package com.adaptive.cloud.config;

/**
 * Exception that may be thrown by <em>any</em> configuration service method if it's determined that the calling context should not be permitted to perform the requested operation.
 * <p>
 * This would typically be expected to occur either 
 * </p>
 * @author Kevin Seal
 */
public class NotAuthorizedException extends RuntimeException {

}
