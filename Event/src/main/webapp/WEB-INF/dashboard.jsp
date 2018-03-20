<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Index</title>
		<link rel="stylesheet" type="text/css" href="/css/style.css">	
		<script src="/js/main.js"></script>
	</head>

	<body>
		<form action="/logout" method="get">
			<input type="submit" value="logout">
		</form>
		<h1>Welcome! ${currentUser.firstName}</h1>
		<h3>Events in your State</h3>
		<div>
			<table>
			<tr>
				<th>Name</th>
				<th>Date</th>
				<th>Location</th>
				<th>Join</th>
			</tr>
			<c:forEach items="${notJoinedEvents}" var="new">
				<tr>
				<td><a href="/events/${new.id}">${new.name}</a></td>
				<td>${new.date}</td>
				<td>${new.city}, ${new.state}</td>
				<td><form action="/events/${new.id}/join" method="post"><input type="submit" value="Join"></form></td>
				</tr>
			</c:forEach>
		</table>
		</div>
		<h3>All Events</h3>
				<div>
			<table>
			<tr>
				<th>Name</th>
				<th>Date</th>
				<th>Location</th>
			</tr>
			<c:forEach items="${notUserStates}" var="event">
				<tr>
				<td><a href="/events/${event.id}">${event.name}</a></td>
				<td>${event.date}</td>
				<td>${event.city}, ${event.state}</td>
				<c:choose>
						<c:when test="${isActivated}">
							<td> Already Joined</td>
						</c:when>
						<c:otherwise>
							<td><form action="/events/${event.id}/join" method="post"><input type="submit" value="Join"></form></td>

						</c:otherwise>
					</c:choose>	
				</tr>
			</c:forEach>
		</table>
		</div>	
		
		<h3>Events You are Going to</h3>
				<div>
			<table>
			<tr>
				<th>Name</th>
				<th>Date</th>
				<th>Location</th>
			</tr>

			<c:forEach items="${joinedEvents}" var="join">
				<tr>
				<td><a href="/events/${join.id}">${join.name}</a></td>
				<td>${join.date}</td>
				<td>${join.city}, ${join.state}</td>
				</tr>
			</c:forEach>
		</table>
		</div>
		
		<form:form method="post" action="/events/new" modelAttribute="event">
				<form:label path="name"> Name:
					<form:input path="name"></form:input>
					<form:errors path="name"></form:errors>
				</form:label>
				
				<form:label path="date"> Date:
					<form:input type="date" path="date"></form:input>
					<form:errors path="date"></form:errors>
				</form:label>
				
				<form:label path="city"> City:
					<form:input path="city"></form:input>
					<form:errors path="city"></form:errors>
				</form:label>
				<form:label path="state"> State:
					<form:input path="state"></form:input>
					<form:errors path="state"></form:errors>
				</form:label>
				<input type= "submit" value="Create Event">
		</form:form>
	</body>
</html>