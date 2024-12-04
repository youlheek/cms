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
		// super 을 타고 올라가보면 String을 출력하게끔 정의되어있기 때문에
		// 우리가 임의로 작성한 에러코드가 사용자에게 보여지거나 로그에 남게 되는것
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
	}
}
