//package com.example.william.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class UserTestController {
//	@Autowired
//	private JdbcTemplate jdbc;
//	
//	@RequestMapping(value="/allUser",method=RequestMethod.GET,produces="application/json")
//	public String getAllUser() {
//		List<Map<String,Object>> m = jdbc.queryForList("select * from user");
//		return m.toString();
//	}
//	
//	@RequestMapping(value="/getUser/{id}",method=RequestMethod.GET,produces="application/json")
//	public String addUser(@PathVariable String id) {
//		Map m = jdbc.queryForMap("select * from user where id='"+id+"'");
//		return m.toString();
//	}
//}
