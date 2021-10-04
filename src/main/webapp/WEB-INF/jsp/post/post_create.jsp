<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="w-50 post-create">
		<div class="m-5">
			<h1>글쓰기</h1>
			<input type="text" id="subject" class="form-control mb-2"
				placeholder="제목을 입력해주세요.">
			<textarea id="content" class="form-control mb-2" rows="10" cols="100"
				placeholder="내용을 입력해주세요."></textarea>

			<div class="d-flex justify-content-end">
				<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif">
			</div>

			<%-- float를 사용한 정렬을 사용할 때는 1차 상위부모에 clearfix를 해주어야 float를 사용한 아래 태그들에 영향을 주지 않는다. --%>
			<div class="claerfix">
				<a href="/post/post_list_view" class="btn btn-dark float_left">목록</a>

				<div class="float-right">
					<button type="button" id="clearBtn" class="btn btn-secondary">모두지우기</button>
					<button type="button" id="saveBtn" class="btn btn-primary ml-2">저장</button>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// 모두지우기 버튼 클릭
		$('#clearBtn').on('click', function() {
			// 제목 input, 내용 textarea 영역을 빈칸으로 만든다.
			if (confirm("내용을 지우시겠습니까?")) {
				$('#subject').val('');
				$('#content').val('');
			}
		});

		// 글 내용 저장버튼 클릭
		$('#saveBtn').on('click', function() {
			let subject = $('#subject').val().trim();
			console.log(subject); // 값이 잘 들어왔는지 확인
			if (subject == '') {
				alert("제목을 입력해주세요.");
				return;
			}
			
			let content = $('#content').val();
			console.log(content);
			if (content == '') {
				alert("내용을 입력해주세요.");
				return;
			}

			// 파일이 업로드 된 경우에 확장자 검사
			let file = $('#file').val();
			console.log("file" + file);
			if (file != '') {
				// file.split('.'); // 파일명을 .기준으로 자른다(배열에 저장)
				let ext = file.split('.').pop().toLowerCase(); 		// toLowerCase - 모두 소문자로 바꾼다(PNG 확장자명을 소문자로 바꾸기 위해 사용)
				if ($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) {		// inArray -  배열 안에 괄호 안에 것들이 있는가
					alert("jpg, jpeg, png, gif만 사용이 가능합니다");
					$('#file').val(''); // 잘못 올린 파일을 비운다.
					return;
				}
			}
			
			// 폼태그를 자바스크립트에서 만든다. 
			let formData = new FormData(); // 변수가 아닌 객체를 만든다.
			formData.append("subject", subject); // append = 아무것도 없는 폼태그에 데이터를 추가한다. ('만들 내용', 위에서 만든 변수 subject)
			formData.append("content", content); // append = 아무것도 없는 폼태그에 데이터를 추가한다. ('만들 내용', 위에서 만든 변수 content)
			formData.append("file", $('#file')[0].files[0]); // (request parameter명(만들 내용), 첫번째 files id태그에 있는 첫번째 files - id로 위에 있는 파일 중 첫번째(0) 파일을 가져온다.)
			
			//ajax로 보내기
			$.ajax({
				type:'post' // 사진같은 파일은 무조건 post - 그래야 requestbody안에 담겨서 넘어간다.
				, url:'/post/create'
				, data: formData // 위에 있는 formData 통채로 보낸다.
				, enctype: 'multipart/form-data' // 파일 업로드와 관련된 필수 설정
				, processData: false  			// 파일 업로드와 관련된 필수 설정 (true면 파일 자체로 안넘어가고 querry string으로 넘어감 따라서 꼭 true)
				, contentType: false			// 파일 업로드와 관련된 필수 설정
				, success: function(data) {
					if(data.result == 'success') {
						alert("메모가 저장되었습니다.");
						location.href = "/post/post_list_view";
					}
				}, error: function(e) {
					alert("메모 저장에 실패했습니다" + e);
				}
			});
			
		});
	});
</script>