package com.memo.post.bo;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	private Logger logger = LoggerFactory.getLogger(PostBO.class);
	
	// bo가 dao한테 요청
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	public List<Post> getPostList(int userId) {
		return postDAO.selectPostList(userId);
	}
	
	public int createPost(int userId, String userLoginId, String subject, String content, MultipartFile file) { // 파일을 줄테니 경로를 달라
		String imagePath = null;
		if (file != null) {
			// dao요청하는게 아닌 새로운 클래스를 만든다, 이미지는 여러 곳에서 쓸 수 있도록 common 패키지에 만든다(FileManagerService)
			try {
				imagePath = fileManagerService.saveFile(userLoginId, file); // 여기서 책임진다 try catch
			} catch (IOException e) {
				imagePath = null;
			}
		}
		// db에 저장
		return postDAO.insertPost(userId, subject, content, imagePath); // db에는 userLoginId는 저장안한다
	}
	
	public Post getPost(int postId) {
		return postDAO.selectPost(postId);
	}
	
	public void updatePost(int postId, String loginId, String subject, String content, MultipartFile file) { //void는 리턴값이 없다.
		
		// postId로 게시물이 있는지 확인 - 검증
		// 위에 있는 getPost 사용
		Post post = getPost(postId);
		if (post == null) {
			logger.error("[글 수정] post is null. postId:{}", postId); // postId가 {}안으로 들어가는 형식
			return;
		}
		
		// file이 있으면 업로드 후 imagePath를 얻어온다.
		String imagePath = null;
		if (file != null) {
			// 파일 업로드
			try {
				imagePath = fileManagerService.saveFile(loginId, file);
				
				// 기존에 있던 파일 제거 - imagePath가 존재(업로드 성공) && 기존에 파일이 있으면!! 
				if (imagePath != null && post.getImagePath() != null) {
					// 업로드 실패를 대비해서 파일 업로드 성공 후 기존 파일 제거하는게 안전
					fileManagerService.deleteFile(post.getImagePath());
				}
				
			} catch (IOException e) {

			}
		}
		
		// db update
		postDAO.updatePost(postId, subject, content, imagePath);
		
	}
}
