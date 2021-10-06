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
import org.springframework.util.CollectionUtils;
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
	public String postListView(
			@RequestParam(value = "prevId", required= false) Integer prevIdParam, //이전버튼값 - 컨트롤러가 받아서 bo에 패스
			@RequestParam(value = "nextId", required= false) Integer nextIdParam, // 다음버튼값 - 컨트롤러가 받아서 bo에 패스
			Model model, HttpServletRequest request) {
		
		// 글 목록들을 가져온다. (로그인된 사용자만)
		HttpSession session = request.getSession(); // 세션 가져오기
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) { // - 혹시라도 userId가 비어있으면 오류 띄우기
			logger.info("[post_list_view] userId is null. " + userId); // 로그 확인용 필수는 아님(에러 위치 확인용) 
			return "redirect:/user/sign_in_view";
		}
		
		List<Post> postList = postBO.getPostList(userId, prevIdParam, nextIdParam); // userId에 해당하는 list만 가져온다
		
		int prevId = 0;
		int nextId = 0;
		if(CollectionUtils.isEmpty(postList) == false) { // CollectionUtils
			prevId = postList.get(0).getId();
			nextId = postList.get(postList.size() - 1).getId();
			
			// 이전이나 다음이 없는 경우 0으로 세팅한다. (jsp에서 0인지 검사)
			
			// 마지막페이지(다음 기준)인 경우 0으로 세팅
			if (postBO.isLastPage(userId, nextId)) {
				nextId = 0;
			}
			
			// 첫번째페이지(이전 기준)인 경우 0으로 세팅
			if (postBO.isFirstPage(userId, prevId)) {
				prevId = 0;
			}
		}
		
		// 모델에 담는다
		model.addAttribute("prevId", prevId); // 처음 값
		model.addAttribute("nextId", nextId); // 마지막 값
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
