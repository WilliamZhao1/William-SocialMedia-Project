package com.example.william.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.william.common.Result;
import com.example.william.dao.PostDao;
import com.example.william.dao.RelationshipDao;
import com.example.william.dao.UserDao;
import com.example.william.entity.Post;
import com.example.william.entity.Relationship;
import com.example.william.entity.User;
import com.example.william.vo.PostVo;

@RestController
public class PostController {

	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private RelationshipDao relationshipDao;
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value = "/postContent", method = RequestMethod.POST, produces = "application/json")
	public Result<User> postContent(@RequestBody Post p, HttpServletRequest request) {
		Result<User> res = new Result<User>();
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			res.setMessage("Not logged in");
			res.setStatus(400);
		} else {
			p.setAuthorId(u.getUid());
			p.setPostTime(new Date());
			postDao.save(p);
			res.setMessage("Posted");
			res.setStatus(200);
		}
		return res;
	}
	
	@RequestMapping(value = "/myContent", method = RequestMethod.GET, produces = "application/json")
	public Result<List<Post>> myContent(HttpServletRequest request) {
		Result<List<Post>> res = new Result<List<Post>>();
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			res.setMessage("Not logged in");
			res.setStatus(400);
		} else {
			Pageable p = PageRequest.of(0, 10, Sort.by("postTime").descending());
			Page<Post> contents = postDao.findByAuthorId(u.getUid(), p);
			List<Post> posts = contents.getContent();
			res.setData(posts);
			res.setStatus(200);
			res.setMessage("My content");
		}
		return res;
	}
	
	@RequestMapping(value = "/findContent", method = RequestMethod.GET, produces = "application/json")
	public Result<List<Post>> findContent(@RequestParam int searchId, HttpServletRequest request) {
		Result<List<Post>> res = new Result<List<Post>>();
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			res.setMessage("Not logged in");
			res.setStatus(400);
		} else {
			Pageable p = PageRequest.of(0, 10, Sort.by("postTime").descending());
			Page<Post> contents = postDao.findByAuthorId(searchId, p);
			List<Post> posts = contents.getContent();
			Relationship rela = relationshipDao.findBySponsorAndAccepterAndStatus(u.getUid(), searchId, "Accepted");
			if (rela != null) {
				rela.setIntimacyLevel(rela.getIntimacyLevel() + 1);
				relationshipDao.save(rela);
			}
			res.setData(posts);
			res.setStatus(200);
			res.setMessage("My content");
		}
		return res;
	}
	
	
	@RequestMapping(value = "/timeline", method = RequestMethod.GET, produces = "application/json")
	public Result<List<PostVo>> timeline(HttpServletRequest request) {
		Result<List<PostVo>> res = new Result<List<PostVo>>();
		List<PostVo> data = new ArrayList<PostVo>();
		
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			Pageable pageableOfAllPost = PageRequest.of(0, 10, Sort.by("postTime").descending());
			Page<Post> allPageOfPost = postDao.findAll(pageableOfAllPost);
			List<Post> allOfPost = allPageOfPost.getContent();
			for(Post p:allOfPost) {
				PostVo pv = new PostVo();
				pv.setPid(p.getPid());
				pv.setTitle(p.getTitle());
				pv.setContent(p.getContent());
				pv.setPictureUrl(p.getPictureUrl());
				pv.setPostTime(p.getPostTime());
				pv.setStrPostTime(p.getPostTime().toString());
				int authorId = p.getAuthorId();
				pv.setAuthorId(authorId);
				User author = userDao.findByUid(authorId);
				if(author!=null) {
					pv.setAuthorName(author.getName());
				}
				data.add(pv);
			}
		} else {
			HashSet<String> keys = new HashSet<String>();
			
			Pageable pageableOfRela = PageRequest.of(0, 10, Sort.by("intimacyLevel").descending());
			Page<Relationship> pageOfRela = relationshipDao.findBySponsorAndStatus(u.getUid(), "Accepted", pageableOfRela);
			List<Relationship> friends = pageOfRela.getContent();
			if (!friends.isEmpty()) {
				int size = friends.size();
				Pageable pageableOfPost = PageRequest.of(0, 1, Sort.by("postTime").descending());
				for (int i = 0; i < size; i++) {
					Relationship r = friends.get(i);
					int accepterId = r.getAccepter();
					Page<Post> pageOfPost = postDao.findByAuthorId(accepterId, pageableOfPost);
					List<Post> newPosts = pageOfPost.getContent();
					if(!newPosts.isEmpty()) {
						Post newPost = newPosts.get(0);
						String key = newPost.getAuthorId().toString()+newPost.getPostTime();
						keys.add(key);
						PostVo pv = new PostVo();
						pv.setPid(newPost.getPid());
						pv.setTitle(newPost.getTitle());
						pv.setContent(newPost.getContent());
						pv.setPictureUrl(newPost.getPictureUrl());
						pv.setPostTime(newPost.getPostTime());
						pv.setStrPostTime(newPost.getPostTime().toString());
						int authorId = newPost.getAuthorId();
						pv.setAuthorId(authorId);
						User author = userDao.findByUid(authorId);
						if(author!=null) {
							pv.setAuthorName(author.getName());
						}
						data.add(pv);
					}
				}
			}
			
			if (data.size() < 10 ) {
				Pageable pageableOfAllPost = PageRequest.of(0, 10, Sort.by("postTime").descending());
				Page<Post> allPageOfPost = postDao.findAll(pageableOfAllPost);
				List<Post> allOfPost = allPageOfPost.getContent();
				int size  = allOfPost.size();
				for(int i=0 ; i<size; i++) {
					Post post = allOfPost.get(i);
					String key = post.getAuthorId().toString()+post.getPostTime();
					if(!keys.contains(key)) {
						PostVo pv = new PostVo();
						pv.setPid(post.getPid());
						pv.setTitle(post.getTitle());
						pv.setContent(post.getContent());
						pv.setPictureUrl(post.getPictureUrl());
						pv.setPostTime(post.getPostTime());
						pv.setStrPostTime(post.getPostTime().toString());
						int authorId = post.getAuthorId();
						pv.setAuthorId(authorId);
						User author = userDao.findByUid(authorId);
						if(author!=null) {
							pv.setAuthorName(author.getName());
						}
						data.add(pv);
						keys.add(key);
					}
					if(data.size()>=10) {
						break;
					}
				}
			}
		}
		res.setStatus(200);
		res.setData(data);
		return res;
	}
	
}









