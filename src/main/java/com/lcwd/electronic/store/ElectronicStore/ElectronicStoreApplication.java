package com.lcwd.electronic.store.ElectronicStore;

import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {
	@Autowired
	private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Role roleAdmin = roleRepository.findByName("ADMIN").orElse(null);
//		if(roleAdmin == null) {
//			roleAdmin = new Role();
//			roleAdmin.setRoleId(UUID.randomUUID().toString());
//			roleAdmin.setName("ROLE_ADMIN");
//			roleRepository.save(roleAdmin);
//		}
//
//		Role roleNormal = roleRepository.findByName("NORMAL").orElse(null);
//		if(roleNormal == null) {
//			roleNormal = new Role();
//			roleNormal.setRoleId(UUID.randomUUID().toString());
//			roleNormal.setName("ROLE_NORMAL");
//			roleRepository.save(roleNormal);
//		}
	}
}
