<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- JSTL core library --%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- formatting core library --%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 목록</h1>
		
		<table class="table table-hover">
			<thead>
				<tr>
					<th>No.</th>
					<th>제목</th>
					<th>작성날짜</th>
					<th>수정날짜</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${postList}" var="post">
				<tr>
					<td>${post.id}</td>
					<td>${post.subject}</td>
					<td>
						<%-- Date객체로 내려온 값을 String Format(pattern)으로 변경해서 출력 --%>
						<fmt:formatDate value="${post.createdAt}" var="createdAt" pattern="yyyy-MM-dd HH:mm:ss" />
						${createdAt}
					</td>
					<td>
						<fmt:formatDate value="${post.updatedAt}" var="updatedAt" pattern="yyyy-MM-dd HH:mm:ss" />
						${updatedAt}
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div class="d-flex justify-content-end">
			<a href="/post/post_create_view" class="btn btn-primary">글쓰기</a>
		</div>
		
	</div>
</div>