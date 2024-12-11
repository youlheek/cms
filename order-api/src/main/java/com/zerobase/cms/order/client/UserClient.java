package com.zerobase.cms.order.client;

import com.zerobase.cms.order.client.customer.ChangeBalanceForm;
import com.zerobase.cms.order.client.customer.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-api", url = "${feign.client.url.user-api}")
public interface UserClient {
// TODO : 왜 interface로 쓴걸까?

	@GetMapping("/customer/getInfo")
	ResponseEntity<CustomerDto> getCustomerInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token);

	@PostMapping("/customer/balance")
	ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token,
										  @RequestBody ChangeBalanceForm form);

}
