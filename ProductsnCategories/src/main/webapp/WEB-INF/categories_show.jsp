<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Show Category</title>
</head>
<body>
	<h1> <c:out value="${category.name}"/> </h1>

	<h2>Products:</h2>

	<c:forEach items="${category.products}" var="product">
		<h3> <c:out value="${product.name}"/> </h3>
	</c:forEach>

	<h3>Add Product</h3>
	
	<form method="post" action="/categories/${category.id}">
		<select name="product">
			<c:forEach items="${products}" var="product">
				<option value="${product.id}"> ${product.name}</option>
			</c:forEach>
		</select>
		
		<input type="submit" value="Add"></input>
	</form>
</body>
</html>