package com.memo.post.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	
	public List<Post> selectPostList(int userId); // selectPostList = postMapper의 id값
	
	public int insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	public Post selectPost(int id); //여기 id를 postMapper의 WHERE로 보낸다
	
	public void updatePost(
			@Param("id") int id,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
}
