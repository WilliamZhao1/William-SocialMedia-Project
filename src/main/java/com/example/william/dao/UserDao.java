package com.example.william.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.william.entity.User;

public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
	User findByUid(Integer uid);
	
	@Query(value="select * from user where name=?1 and password=MD5(?2)", nativeQuery=true)
	User findByNameAndPassword(String name, String password);
	
	User findByName(String name);
}
