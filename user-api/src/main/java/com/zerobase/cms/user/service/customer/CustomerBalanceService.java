package com.zerobase.cms.user.service.customer;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.repository.CustomerBalanceHistoryRepository;
import com.zerobase.cms.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.cms.user.exception.ErrorCode.NOT_ENOUGH_BALANCE;
import static com.zerobase.cms.user.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {

	private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
	private final CustomerRepository customerRepository;

	@Transactional(noRollbackFor = CustomException.class)
	//CustomException이 발생하더라도 트랜잭션이 롤백하지 않고 정상적으로 커밋됨
	public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form) throws CustomException{

		// 가장 최근 입출금기록 받아오기
		CustomerBalanceHistory customerBalanceHistory =
				customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
				.orElse(CustomerBalanceHistory.builder()
						.changeMoney(0)
						.currentMoney(0)
						.customer(customerRepository.findById(customerId).orElseThrow(() -> new CustomException(NOT_FOUND_USER)))
						.build()
				);

		if (customerBalanceHistory.getCurrentMoney() + form.getMoney() < 0) {
			throw new CustomException(NOT_ENOUGH_BALANCE);
		}

		customerBalanceHistory = CustomerBalanceHistory.builder()
				.customer(customerBalanceHistory.getCustomer())
				.changeMoney(form.getMoney())
				.currentMoney(customerBalanceHistory.getCurrentMoney() + form.getMoney())
				.fromMessage(form.getFrom())
				.description(form.getMessage())
				.build();

		// Customer 객체에 balance 적용
		customerBalanceHistory.getCustomer().setBalance(customerBalanceHistory.getCurrentMoney());

		return customerBalanceHistoryRepository.save(customerBalanceHistory);
	}
}
