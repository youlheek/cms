package com.zerobase.cms.user.service.seller;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.zerobase.cms.user.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SellerService {

	private final SellerRepository sellerRepository;

	public Optional<Seller> findByIdAndEmail(Long id, String email) {
		return sellerRepository.findByIdAndEmail(id, email);
	}

	// [셀러] - 로그인 처리
	public Optional<Seller> findValidSeller(String email, String password) {
		return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, password);
	}

	/*
	 * SignUp Method - TODO : 추후 리팩토링 필요
	 */
	// [셀러] 회원가입
	public Seller signUp(SignUpForm signUpForm) {
		return sellerRepository.save(Seller.from(signUpForm));
	}

	// [셀러] 회원가입 - 이메일 중복 확인
	public boolean isEmailExist(String email) {
		return sellerRepository.findByEmail(email).isPresent();
	}

	// [셀러] 회원가입 - 이메일 인증
	@Transactional
	public void verifyEmail(String email, String code) {
		Seller seller = sellerRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		if (seller.isVerify()) { // 이미 인증된 경우
			throw new CustomException(ALREADY_VERIFY);
		} else if (!seller.getVerificationCode().equals(code)) {
			throw new CustomException(WRONG_VERIFICATION);
		} else if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(EXPIRE_CODE);
		}

		seller.setVerify(true);
	}

	// [셀러] 회원가입 - 인증코드 & expired Set
	// 📍 근데 왜 굳이 LocalDateTime 으로 return시킬까?
	// TODO : 리팩토링시 Application에서 인증만료일 return 시켜주면 되겠다
	@Transactional
	public LocalDateTime changeSellerValidationEmail(Long id, String verificationCode)	{
		Optional<Seller> optionalSeller = sellerRepository.findById(id);

		if (optionalSeller.isPresent()) {
			Seller seller = optionalSeller.get();
			seller.setVerificationCode(verificationCode);
			seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));

			return seller.getVerifyExpiredAt();
		}

		throw new CustomException(NOT_FOUND_USER);
	}
}
