package com.example.william.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.william.common.Result;
import com.example.william.dao.UserDao;
import com.example.william.entity.User;

@RestController
public class UserController {
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value="/getUser/{uid}",method=RequestMethod.GET,produces="application/json")
	public Result<User> getUser(@PathVariable Integer uid) {
		Result<User> r = new Result<User>();
		User u = userDao.findByUid(uid);
		if (u == null) {
			r.setMessage("No user found");
			r.setStatus(200);
		} else {
			r.setStatus(200);
			r.setData(u);
		}
		
		return r;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST,produces="application/json")
	public Result<User> login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
		Result<User> r = new Result<User>();
		User u = userDao.findByNameAndPassword(username, password);
		if (u == null) {
			r.setMessage("Username or password incorrect");
			r.setStatus(400);
		} else {
			r.setMessage("login success");
			r.setStatus(200);
			r.setData(u);
			request.getSession().setAttribute("user", u);
		}
		return r;
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET,produces="application/json")
	public Result logout(HttpServletRequest request) {
		Result r = new Result();
		request.getSession().invalidate();
		r.setMessage("Logout success");
		r.setStatus(200);
		return r;
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST,produces="application/json")
	public Result<User> signup(@RequestBody User me, HttpServletRequest request) {
		Result<User> res = new Result<User>();
		String n = me.getName();
		User u = userDao.findByName(n);
		if (u != null) {
			res.setMessage("Username already taken");
			res.setStatus(400);
		} else {
			String p = me.getPassword();
			try {
				String hash = DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(p.getBytes("UTF-8")));
				me.setPassword(hash);
				me.setAvatorUrl("/img/default_avatar.jpg");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userDao.save(me);
			request.getSession().setAttribute("user", me);
			res.setMessage("Sign up Successful");
			res.setStatus(200);
			
		}
		return res;
	}
	
	
	@RequestMapping(value="/myProfile",method=RequestMethod.GET,produces="application/json")
	public Result<User> myProfile(HttpServletRequest request) {
		Result<User> r = new Result<User>();
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			r.setMessage("Not logged in");
			r.setStatus(10000);
		} else {
			r.setMessage("My profile");
			r.setStatus(200);
			r.setData(u);
		}
		
		return r;
	}
	

}
