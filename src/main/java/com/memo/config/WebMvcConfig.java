package com.memo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.interceptor.PermissionInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private PermissionInterceptor interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
		.addPathPatterns("/**") // 어떤 주소로 들어왔을 때 인터셉터 태울 것인가, /** = 손주를 포함한 모든 디렉토리 확인, 어떤 주소든 모두 다
		.excludePathPatterns("/user/sign_out", "/static/**", "/error"); // 인터셉터를 안태울 path 설정
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**") // http://localhost/images/miga2400_1633473142/apple.png 이렇게 들어오게 설정
		.addResourceLocations("file:///C:\\손지승\\6_spring_project\\ex\\memo_workspace\\Memo\\images/"); // 실제파일 저장위치
		//.addResourceLocations(FileManagerService.FILE_UPLOAD_PATH); 위랑 같음
	}
}
