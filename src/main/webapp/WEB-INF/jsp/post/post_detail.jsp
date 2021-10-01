<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<div class="d-flex justify-content-center">
	<div class="w-50">
		<div class="m-5">
			<h1>글 상세/수정</h1>
			<input name="subject" type="text" class="form-control mb-2" placeholder="제목을 입력해주세요." value="${post.subject}">
			<textarea name="content" class="form-control mb-2" rows="15" cols="100" placeholder="내용을 입력해주세요.">${post.content}</textarea>
			<%-- float를 사용한 정렬을 사용할 때는 1차 상위부모에 clearfix를 해주어야 float를 사용한 아래 태그들에 영향을 주지 않는다. --%>
			<div class="file-upload-btn clearfix">
				<input id="file" type="file" class="float-right" accept=".jpg,.jpeg,.png,.gif">
			</div>
			
			<%-- 이미지가 있을 때만 이미지 영역 추가 --%>
			<c:if test="${not empty post.imagePath}">
			<div class="image-area">
				<img src="${post.imagePath}" alt="업로드 이미지" width="300">
			</div>
			</c:if>
			
			<div class="btn-area clearfix mt-5">
				<button id="postDeleteBtn" data-post-id="${post.id}" class="btn btn-secondary float-left">삭제</button>
					
				<div class="float-right">
					<button id="postListBtn" class="btn btn-dark">목록</button>
					<%-- postId를 자바스크립트에서 사용하기 위해 data 속성을 추가한다. data-속성명 --%>
					<button id="saveBtn" data-post-id="${post.id}" class="btn btn-primary ml-2">저장</button>
				</div>
			</div>
		</div>
	</div>
</div>