package com.memo.post.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	
	public List<Post> selectPostList( // selectPostList = postMapper의 id값
			@Param("userId") int userId,
			@Param("direction") String direction,
			@Param("standardId") Integer standardId,
			@Param("limit") int limit); 
	
	
	public int selectIdByUserIdAndSort( // 페이징
			@Param("userId") int userId, 
			@Param("sort") String sort);
	
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
	
	public void deletePost(int id); // 하나만 넘기면 @Param("postId") param어노테이션 생략 가능
	

}
