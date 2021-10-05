package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

@RequestMapping("/post")
@RestController
public class PostRestController {
	// RestController는 뷰 화면으로 보내는게 아니다
	
	@Autowired
	private PostBO postBO;
	
	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request){
		
		// session에서 유저 id를 가져온다,
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		// DB에 내용들을 BO에서 insert (userid, userLoginId, subject, content, file) -> 받은 파라미터에 userid가 없다 -> session에서 유저 id를 가져온다
		Map<String, Object> result = new HashMap<>();
		result.put("result", "error");
		
		int row = postBO.createPost(userId, userLoginId, subject, content, file);
		if (row > 0) {
			result.put("result", "success");
		}
		
		// 결과값 response
		return result;
	}
	
	
	@PutMapping("/update") // put - 수정을 의미
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file,  // file은 비필수 파라미터
			HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String loginId = (String) session.getAttribute("userLoginId");
		
		// db update - bo에서 처리
		postBO.updatePost(postId, loginId, subject, content, file);
		
		// 응답값 세팅
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");
		
		return result;
	}

}
