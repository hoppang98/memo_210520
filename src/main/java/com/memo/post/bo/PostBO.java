package com.memo.post.bo;

import java.io.IOException;
import java.util.Collections;
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
	
	private static final int POST_MAX_SIZE = 3;
	
	// bo가 dao한테 요청
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// 페이징이 포함된 코드
	public List<Post> getPostList(int userId, Integer prevId, Integer nextId) {
		// 10 9 8 / 7 6 5 / 4 3 2 / 1
		// 이전 : 7보다 큰 3개 오름차순(8,9,10)으로 가져와서 코드에서 역순(10,9,8)으로 변경, 다음 : 5보다 작은 3개 내림차순으로 가져온다(3,2,1).
		String direction = null; // null || next || prev
		Integer standardId  = null;
		
		if (prevId != null) {
			// 이전 클릭
			direction = "prev";
			standardId = prevId;
			
			// 7보다 큰 3개 => 8 9 10 reverse 시켜야 한다. => 10, 9 ,8
			List<Post> postList = postDAO.selectPostList(userId, direction, standardId, POST_MAX_SIZE);
			Collections.reverse(postList); //Collections = list나 set을 조작할 수 있는 클래스
			return postList;
			
		} else if (nextId != null) {
			// 다음 클릭
			direction = "next";
			standardId = nextId;
		}
		
		return postDAO.selectPostList(userId, direction, standardId, POST_MAX_SIZE);
	}
	
	// 페이징 - 다음 기준으로 마지막 페이지인가?
	public boolean isLastPage(int userId, int nextId) {
		// 오름차순 limit 1 제일 작은값과 nextId를 비교, 같다면 마지막 페이지
		return nextId == postDAO.selectIdByUserIdAndSort(userId, "ASC");
	}
	
	// 이전 기준으로 첫번째 페이지인가?
	public boolean isFirstPage(int userId, int prevId) {
		// 내림차순 limit 1 제일 큰값과 prevId를 비교, 같다면 첫번째 페이지
		return prevId == postDAO.selectIdByUserIdAndSort(userId, "DESC");
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
	
	public void deletePost(int postId) { // postId = 필수파라미터
		// postId로 post를 가져온다. imagePath를 보고 그림이 있는지 확인
		// 같은 bo에 있는 getPost 사용하기
		Post post = getPost(postId);
		if(post == null) {
			logger.error("[delete post] 삭제할 게시물이 없습니다. {}", postId);
			return;
		}
		
		// 그림이 있으면 삭제한다.
		String imagePath = post.getImagePath();
		if (imagePath != null ) {
			try {
				fileManagerService.deleteFile(imagePath);
			} catch (IOException e) {
				logger.error("[delete post] 그림 삭제 실패 postId: {}, path: {}", postId, imagePath);
			}
		}
		//포스트를 삭제한다.
		postDAO.deletePost(postId);
		
	}
}
