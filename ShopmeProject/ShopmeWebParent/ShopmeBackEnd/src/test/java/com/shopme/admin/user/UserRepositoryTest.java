package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userMario = new User("medurazn@gmail.com", "m39862212", "Mario", "Durazno");
		userMario.addRole(roleAdmin);
		
		User savedUser = repo.save(userMario);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userWilmer = new User("wido@gmail.com", "wido2025", "Wilmer", "Durazno");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		userWilmer.addRole(roleEditor);
		userWilmer.addRole(roleAssistant);

		
		User savedUser = repo.save(userWilmer);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserById() {
		User userMario = repo.findById(1).get();
		System.out.println(userMario);
		assertThat(userMario).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userMario = repo.findById(1).get();
		userMario.setEnabled(true);
		//userMario.setEmail("medurazn@gmail.com");
		
		repo.save(userMario);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userWido = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userWido.getRoles().remove(roleEditor);
		userWido.addRole(roleSalesperson);
		
		repo.save(userWido);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "medurazn@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countByid = repo.countById(id);
		
		assertThat(countByid).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableuser() {
		Integer id = 16;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableuser() {
		Integer id = 16;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
		
	}
	
	
	
	
	
}
