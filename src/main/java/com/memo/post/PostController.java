package com.memo.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.mybatis.logging.LoggerFactory; - 이거 아니다
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

@RequestMapping("/post")
@Controller
public class PostController {
	
	private Logger logger = LoggerFactory.getLogger(PostController.class); // 로그 확인용 필수는 아님(에러 위치 확인용)
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글 목록 화면
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/post_list_view")
	public String postListView(Model model, HttpServletRequest request) {
		
		// 글 목록들을 가져온다. (로그인된 사용자만)
		HttpSession session = request.getSession(); // 세션 가져오기
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) { // - 혹시라도 userId가 비어있으면 오류 띄우기
			logger.info("[post_list_view] userId is null. " + userId); // 로그 확인용 필수는 아님(에러 위치 확인용) 
			return "redirect:/user/sign_in_view";
		}
		
		List<Post> postList = postBO.getPostList(userId); // userId에 해당하는 list만 가져온다
		
		// 모델에 담는다
		
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		return "template/layout"; // layout에서 ${viewName}.이 계속 바뀌기 때문에 여기서 model을 내려줘야함
	}
	
	
	/**
	 * 글쓰기 화면
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/post_create_view")
	public String postCreateView(Model model, HttpServletRequest request) { // session에 userId가 있는 경우에만 접근 가능(=로그인 상태에서만)
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			// 세션에 id가 없으면 로그인 하는 페이지로 이동(redirect)
			return "redirect:/user/sign_in_view";
		}
		model.addAttribute("viewName", "post/post_create");
		return "template/layout"; // layout에서 ${viewName}.이 계속 바뀌기 때문에 여기서 model을 내려줘야함
	}
	
	/**
	 * 메모 수정 화면
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/post_detail_view")
	public String postDeatilView(
			@RequestParam("postId") int postId, // detail_view는 create_view와 달리 내가 뭘 눌렀는지 정보를 뿌려야한다. (postId값이 파라미터로 필요)
			Model model,
			HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			// 세션에 id가 없으면 로그인 하는 페이지로 이동(redirect)
			return "redirect:/user/sign_in_view";
		}
		
		// postId에 해당하는 게시물을 가져와서 model에 담는다
		Post post = postBO.getPost(postId);
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/post_detail");
		return "template/layout"; // layout에서 ${viewName}.이 계속 바뀌기 때문에 여기서 model을 내려줘야함 jsp로 보낸다.
		
	}
}
