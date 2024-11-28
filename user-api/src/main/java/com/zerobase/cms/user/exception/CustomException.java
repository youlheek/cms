package com.zerobase.cms.user.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
// 📍 RuntimeException 과 그냥 Exception 의 차이

	private final ErrorCode errorCode;

	// 생성자
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getDetail()); // RuntimeException의 생성자 호출
		// RuntimeException (String message) : 예외 발생 시 오류 메시지를 설정
		this.errorCode = errorCode;
	}
}

/**
 * RuntimeException은 언체크 예외(Unchecked Exception)로,
 * 컴파일러가 예외 처리를 강제하지 않음
 * -> 보통 프로그래밍 논리 오류나 비정상적인 실행 흐름을 나타내기 위해 사용
 * -> RuntimeException을 상속하면 예외가 발생했을 때 이를 반드시 처리하지 않아도 됨
 *
 * Exception 은 체크 예외(Checked Exception)로,
 * 컴파일러가 예외 처리를 강제하는 예외
 * -> 반드시 try-catch로 처리하거나 throws를 사용해야 함
 * -> 외부 환경에서 발생할 수 있는 오류(e.g. 파일 읽기 실패, 네트워크 오류 등)를 나타낼 때 사용됨
 */