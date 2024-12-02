package com.zerobase.cms.user.client.mailgun;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Builder
@Data // 📍 이 어노테이션 중복되는건데 굳이 왜 쓰는지 모르겠음
public class SendMailForm {
	private String from;
	private String to;
	private String subject;
	private String text;
}
