<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-between">
	<div class="logo d-flex align-items-center">
		<h1 class="font-weight-bold p-4">메모 게시판</h1>
	</div>

	<div class="login-info d-flex align-items-center mr-4">
		<%-- session 정보가 있는 경우에만 출력 --%>
		<c:if test="${not empty userName}">
			<span class="font-weight-bold">${userName}님 안녕하세요.</span>
			<a href="/user/sign_out" class="ml-2 text-white font-weight-bold">로그아웃</a>
			
	</div>
	</c:if>
</div>