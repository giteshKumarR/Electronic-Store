package com.lcwd.electronic.store.ElectronicStore;

import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.repositories.RoleRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
//		if(roleAdmin == null) {
//			roleAdmin = new Role();
//			roleAdmin.setRoleId(UUID.randomUUID().toString());
//			roleAdmin.setName("ROLE_ADMIN");
//			roleRepository.save(roleAdmin);
//		}
//
		Role roleNormal = roleRepository.findByName("ROLE_NORMAL").orElse(null);
//		if(roleNormal == null) {
//			roleNormal = new Role();
//			roleNormal.setRoleId(UUID.randomUUID().toString());
//			roleNormal.setName("ROLE_NORMAL");
//			roleRepository.save(roleNormal);
//		}
		// Make a ADMIN user
//		User user = new User();
//		user.setUserId(UUID.randomUUID().toString());
//		user.setUserStatus("Active");
//		user.setName("Gitesh");
//		user.setUserEmail("gitesh@gmail.com");
//		user.setUserPassword(passwordEncoder.encode("gitesh@123"));
//		user.setUserGender("M");
//		user.setUserAbout("I am an ADMIN for this project");
//		user.setRoles(List.of(roleAdmin));
//		user.setUserProfileImage("gitesh_profile.png");
////		System.out.println("user = " + user);
//		userRepository.save(user);
	}
}
