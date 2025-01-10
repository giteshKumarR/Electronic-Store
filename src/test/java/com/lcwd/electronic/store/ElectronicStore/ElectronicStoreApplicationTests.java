package com.lcwd.electronic.store.ElectronicStore;

import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void contextLoads() {
	}

	@Test
	void testJwtToken() {
		User user = userRepository.findByUserEmail("gitesh@gmail.com").get();
		String token = jwtHelper.generateToken(user);
		System.out.println("token = " + token);

		System.out.println("Username = " + jwtHelper.getUsernameFromToken(token));
		System.out.println("Is token expired = " + jwtHelper.isTokenExpired(token));
		//token = eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaXRlc2hAZ21haWwuY29tIiwiaWF0IjoxNzM2MDY3ODc4LCJleHAiOjE3MzYwODU4Nzh9.-k8r3WLbBcDkjtf2B0On2cZHFwMdTJsjHx6sazXKVucOemKBnNKM8X9NEHcAqMi-8kDWNB-pfMUXepDCfeRikA
		//token = eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaXRlc2hAZ21haWwuY29tIiwiaWF0IjoxNzM2MDY3ODc4LCJleHAiOjE3MzYwODU4Nzh9.-k8r3WLbBcDkjtf2B0On2cZHFwMdTJsjHx6sazXKVucOemKBnNKM8X9NEHcAqMi-8kDWNB-pfMUXepDCfeRikA
	}

}
