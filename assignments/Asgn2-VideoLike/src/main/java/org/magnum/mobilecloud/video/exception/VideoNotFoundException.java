package org.magnum.mobilecloud.video.exception;

import org.springframework.http.HttpStatus;

/**
 * @author dceschmidt
 * @since 31/08/14
 */
public class VideoNotFoundException extends RuntimeException {

	public HttpStatus getHttpStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
