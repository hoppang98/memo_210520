package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // 스프링 빈(contoller, bo, dao 다 아닐때)
public class FileManagerService {
	
	// WebMvcConfig도 같이 볼 것 -> 실제 저장된 파일과 이미지 패스를 매핑해줌 - addResourceHanderlers
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 실제 이미지가 저장될 경로
	// C:\손지승\6_spring_project\ex\memo_workspace\Memo\images - 새폴더 생성
	public final static String FILE_UPLOAD_PATH = "C:\\손지승\\6_spring_project\\ex\\memo_workspace\\Memo\\images/"; // final static - 다시는 수정 못하는, 대문자로 쓰기
	
	public String saveFile(String loginId, MultipartFile file) throws IOException {
		// 파일 디렉토리 경로 - 예) miga2400_1620995857/apple.png  = 아이디_현재시간변환값/파일명
		// 파일명이 겹치지 않게 하려고 현재시간을 경로에 붙여준다.
		String directoryName = loginId + "_" + System.currentTimeMillis() + "/"; // 경로만들기 문자열 이어붙이기
		String filePath = FILE_UPLOAD_PATH + directoryName; // 이 경로에 만들거야
		
		File directory = new File(filePath);
		if (directory.mkdir() == false) { // 새로운 폴더 생성
			logger.error("[파일업로드] 디렉토리 생성 실패" + directoryName + ", filePath" + filePath);
			return null;
		}
		
		//파일 업로드 : byte 단위로 업로드 한다.
		byte[] bytes = file.getBytes();
		Path path = Paths.get(filePath + file.getOriginalFilename()); // getOriginalFilename = input에 올린 파일 명이다.
		Files.write(path, bytes);
	
		// 이지미 URL path를 리턴한다.
		// 예) http://localhost/images/miga2400_1645412312/apple.png - 이런 주소를 브라우저에 치면 그림이 나오게
		return "/images/" + directoryName + file.getOriginalFilename();
		
	}
}
