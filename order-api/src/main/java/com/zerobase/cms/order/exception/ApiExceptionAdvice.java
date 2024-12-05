package com.zerobase.cms.order.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {
	@ExceptionHandler(value = CustomException.class)
	public ResponseEntity<CustomException.CustomExceptionResponse> exceptionHandler(
			HttpServletRequest req,
			final CustomException e) {

		return ResponseEntity
				.status(e.getStatus())
				.body(
						CustomException.CustomExceptionResponse.builder()
								.status(e.getStatus())
								.code(e.getErrorCode().name())
								.message(e.getMessage())
								.build());

	}

}

// TODO : 왜 user-api처럼 이름을 ExceptionController로 하지 않고 Advice를 붙였는지?