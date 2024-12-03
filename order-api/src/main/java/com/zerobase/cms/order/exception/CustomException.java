package com.zerobase.cms.order.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
	private final int status;

	private static final ObjectMapper mapper = new ObjectMapper();

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getDetail());
		// TODO : 📍 getDetail이 String으로 임의 작성하는건데 그게 어떻게 RuntimeException에서 먹히지..?
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
	}
}
