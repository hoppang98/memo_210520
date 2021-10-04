package com.memo.post.bo;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
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
}
