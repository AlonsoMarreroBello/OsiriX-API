package com.osirix.api.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.osirix.api.entity.Role;
import com.osirix.api.entity.Staff;
import com.osirix.api.repository.RoleRepository;
import com.osirix.api.repository.StaffRepository;

//@Configuration
public class LoadDatabase {


	@Bean
	CommandLineRunner initDatabase(StaffRepository userRepository, RoleRepository roleRepository) {
		return arg -> {
			/** ROLES **/
			Role roleAdmin = Role.builder()
								 .roleName("ADMIN")
								 .roleDescription("Role for the maximum administrator of the system with all grants")
								 .build();
			
			roleRepository.save(roleAdmin);
			
			Staff userAlonso = new Staff("Alonso", "alonso@correo.com", "$2a$10$WuJ7nzkzSHzZJZhNwOL2/OSYrX5PJStblUrnWl2QrKI2LyV6L1i/S", LocalDate.now(), LocalDate.now(), true, true, new ArrayList<>(), Set.of(roleAdmin));			
			
			
			userRepository.save(userAlonso);
			
		};
	}
	
}
