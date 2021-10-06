<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> <%-- JSTL core library --%>

<%-- layout에 다 있는 내용이라 html내용 지운다 --%>
<div class="d-flex justify-content-center">
	<div class="w-50 post-create">
		<div class="m-5">
			<h1>글 상세/수정</h1>
			<input type="text" id="subject" class="form-control mb-2" placeholder="제목을 입력해주세요." value="${post.subject}">
			<textarea id="content" class="form-control mb-2" rows="10" cols="100" placeholder="내용을 입력해주세요.">${post.content}</textarea>

			<div class="d-flex justify-content-end">
				<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif">
			</div>
			
			<%-- 이미지가 있을 때만 이미지 영역 추가 --%>
			<c:if test="${not empty post.imagePath }">
				<div>
					<img src="${post.imagePath}" alt="업로드 이미지" width="300">
				</div>
			</c:if>

			<%-- float를 사용한 정렬을 사용할 때는 1차 상위부모에 clearfix를 해주어야 float를 사용한 아래 태그들에 영향을 주지 않는다. --%>
			<div class="claerfix">
				<a href="#" id="deleteBtn" class="btn btn-secondary float_left" data-post-id="${post.id}">삭제</a>
				<%-- 삭제버튼 안에 postId 정보를 심어놓고 script에서 뽑아서 사용, camel케이스 사용 못함(postId(x) post-id(o)) --%>
				
				<div class="float-right">
					<button type="button" id="listBtn" class="btn btn-dark">목록으로</button>
					<button type="button" id="saveBtn" class="btn btn-primary ml-2" data-post-id="${post.id}">수정</button>
					<%-- 수정버튼 안에 postId 정보를 심어놓고 script에서 뽑아서 사용, camel케이스 사용 못함(postId(x) post-id(o)) --%>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
	// 목록으로 버튼 클릭 => 목록으로 이동
	$('#listBtn').on('click', function() {
		location.href = "/post/post_list_view"; // 그냥 위에 listBtn에 a href걸어도 상관없다
	});
	
	// 글 수정
	$('#saveBtn').on('click', function(){
		let subject = $('#subject').val().trim();
		if (subject == '') {
			alert("제목을 입력해주세요");
			return;
		}
		
		let content = $('#content').val();
		if (content == '') {
			alert("내용을 입력해주세요");
			return;
		}
		
		let fileName = $('#file').val(); 
		if (fileName != '') { // 파일은 공백가능
			let fileArr = fileName.split('.');
			let ext = fileArr.pop().toLowerCase(); // .뒤에 있는 확장자명을 소문자로 전부 변환
			if ($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) { // 확장자명 확인
				alert("이미지 파일만 업로드 할 수 있습니다.");
				$('#file').val(''); // 잘못된 파일을 비운다.
				return;
			}
		}
		
		let postId = $(this).data('post-id');
		console.log("postId:" + postId);
		
		// 폼태그를 자바스트립트에서 만든다.
		let formData = new FormData();
		formData.append('postId', postId);
		formData.append('subject', subject);
		formData.append('content', content);
		formData.append('file', $('#file')[0].files[0]); // file은 특이한 방식으로 가져온다.
		//4개의 파라미터 구성 완료
		
		// ajax 통신으로 서버에 전송한다
		// ajax는 뷰에 보낼 수 없다. restcontroller에만 보낸다.
		$.ajax({
			type:'put' // post도 상관없는데 put으로 하면 컨트롤러에서 putMapping해야함
			, url: '/post/update' // ajax url은 뷰로 보내는게 아니다
			, data: formData
			, enctype: 'multipart/form-data' // 파일 업로드와 관련된 필수 설정
			, processData: false  			// 파일 업로드와 관련된 필수 설정 (true면 파일 자체로 안넘어가고 querry string으로 넘어감 따라서 꼭 true)
			, contentType: false			// 파일 업로드와 관련된 필수 설정
			, success: function(data) {
				if (data.result == 'success') {
					alert("메모가 수정되었습니다.");
					location.reload(true); // reload는 새로고침
				}
			}, error: function(e) {
				alert("메모 수정에 실패했습니다. 관리자에게 문의해주세요." + e);
			}
		});
	});
	// 삭제
	$('#deleteBtn').on('click', function(e) {
		e.preventDefault(); // 화면 상단으로 올라가는 것 방지
			
		let postId = $(this).data('post-id');
			
		// ajax 통신으로 삭제 요청
		$.ajax({
			type: 'delete'
			, url: '/post/delete' // ajax url은 뷰로 보내는게 아니다
			, data: {"postId" : postId} // json형식으로 보낸다.
			, success: function(data) {
				if (data.result == 'success') {
					alert("삭제가 성공했습니다.");
					location.href="/post/post_list_view";
				}
			}, error: function(e) {
				alert("메모 삭제 실패" + e);
			}
		});
	});
});
</script>