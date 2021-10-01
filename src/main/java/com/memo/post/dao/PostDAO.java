package com.memo.post.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	
	public List<Post> selectPostList(int userId); // selectPostList = postMapper의 id값
	
}
