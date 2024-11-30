package com.zerobase.cms.user.exception;

import jakarta.servlet.ServletException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 전역 예외 처리 & 전역 설정을 위해 사용되는 어노테이션
@Slf4j
public class ExceptionController {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponse> customRequestException (
			final CustomException c) {

		log.warn("api Exception : {} ", c.getErrorCode());
		return ResponseEntity.badRequest().body(new ExceptionResponse(c.getMessage(), c.getErrorCode()));
		// 사용자가 잘못된 요청을 보냈을 때, 그 요청이 잘못됐다는 걸 알려주는 응답을 만들어 보냄
		// ResponseEntity : HTTP 응답을 생성하기 위해 사용하는 객체
		// -> 클라이언트에게 보낼 응답 상태 코드와 본문(body)를 정의할 수 있음

	}

	@Getter
	@ToString
	@AllArgsConstructor
	public static class ExceptionResponse {
		private String message;
		private ErrorCode errorCode;
	}
}

/**
 * 굳이 ExceptionResponse 클래스를 만들어서 ResponseEntity 안에 넣는 이유 ?
 *
 * 응답 구조가 복잡해질 경우에는 단순히 문자열로 처리하는 것이 어려워져.
 * 나중에 응답에 새로운 필드(예: timestamp, details 등)를 추가해야 한다면,
 * 문자열을 기반으로 추가하는 것보다는 객체를 확장하는 방식이 훨씬 간편하고 깔끔해.
 *
 * 예를 들어, 현재는 message와 code만 있지만 나중에 에러 발생 시간이나 디버그 정보 등을 추가해야 한다고 상상해보자.
 * 이런 경우 Response 클래스를 수정하면 일관되게 새로운 정보를 추가할 수 있어.
 */