package com.example.william.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.william.common.Result;
import com.example.william.dao.RelationshipDao;
import com.example.william.entity.Relationship;
import com.example.william.entity.User;

@RestController
public class RelationshipController {

	@Autowired
	private RelationshipDao relationshipDao;

	@RequestMapping(value = "/sendRequest", method = RequestMethod.POST, produces = "application/json")
	public Result<User> sendRequest(@RequestParam Integer receiverId, HttpServletRequest request) {
		Result<User> res = new Result<User>();
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			res.setMessage("Not logged in");
			res.setStatus(400);
		} else {
			Relationship rela = relationshipDao.findBySponsorAndAccepter(u.getUid(), receiverId);
			
			if (rela != null) {
				res.setMessage("Friend request already sent or already friended");
				res.setStatus(400);
			} else {
				rela = new Relationship();
				rela.setSponsor(u.getUid());
				rela.setAccepter(receiverId);
				rela.setStatus("Pending");
				rela.setIntimacyLevel(0);
				relationshipDao.save(rela);
				res.setStatus(200);
				res.setMessage("Request sent");
			}

		}
		return res;
	}

	@RequestMapping(value = "/acceptRequest", method = RequestMethod.GET, produces = "application/json")
	public Result<User> acceptRequest(@RequestParam Integer senderId, HttpServletRequest request) {
		Result<User> res = new Result<User>();
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			res.setMessage("Not logged in");
			res.setStatus(400);
		} else {
			Relationship rela = relationshipDao.findBySponsorAndAccepterAndStatus(senderId, u.getUid(), "Pending");
			if (rela != null) {
				rela.setStatus("Accepted");
				relationshipDao.save(rela);
				res.setMessage("Accepted");
				res.setStatus(200);
			} else {
				res.setMessage("No friend request");
				res.setStatus(400);
			}
		}
		return res;
	}

}
