package com.memo.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.mybatis.logging.LoggerFactory; - 이거 제거해야함
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
	// 권한 제어
	
	// private Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class); -> 아래와 같은 방식
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private HttpServletResponse response;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("[###preHandle]" + request.getRequestURI()); // request.getRequestURI() - 어떤 url에서 받아왔는지 알 수 있다.
		
		// 세션을 가져온다
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId"); // 로그인 정도 3개중에 아무거나 가져와도 괜찮다 하나만 있어도 로그인 정보 확인 가능
		
		// URL path를 가져온다.
		String uri = request.getRequestURI();
		
		
		if (userId != null && uri.startsWith("/user")) {
			// 만약 로그인이 되어 있으면 + /user에 있으면 => post 쪽으로 보낸다.
			response.sendRedirect("/post/post_list_view");
			return false;
		} else if (userId == null && uri.startsWith("/post")) {
			// 로그인이 안되어 있으면(세션이 없다는 것) + /post에 있으면 => user 쪽으로 보낸다
			response.sendRedirect("/post/sign_in_view");
			return false;
		}
		
		
		
		return true;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse reponse, Object handler, ModelAndView modelAndView) {
		logger.warn("[###postHandle]" + request.getRequestURI());
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse reponse, Object handler, Exception exception) {
		logger.error("[###afterCompletion]" + request.getRequestURI());
	}
	// config 패키지 안에 이걸 설정했다는걸 알려줘야함 (WebMvcConfig)
	// info, warn, error은 경고 나오는거 차이, 내가 원하는 레벨로 설정
}
