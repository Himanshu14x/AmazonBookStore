<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin: Orders</title>
<link rel="icon" type="image/png"
	href="<%=request.getContextPath()%>/assets/images/icon.png" />
<%@include file="allCss.jsp"%>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<h3 class="text-center">Orders</h3>
	<table class="table table-striped">
		<thead class="bg-primary text-white">
			<tr>
				<th scope="col">Order ID</th>
				<th scope="col">User ID</th>
				<th scope="col">Name</th>
				<th scope="col">Book Title</th>
				<th scope="col">Author</th>
				<th scope="col">Price</th>
				<th scope="col">Payment type</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th scope="row">1</th>
				<td>Mark</td>
				<td>Otto</td>
				<td>@mdo</td>
				<td>Mark</td>
				<td>Otto</td>
				<td>Otto</td>
			<tr>
				<th scope="row">1</th>
				<td>Mark</td>
				<td>Otto</td>
				<td>@mdo</td>
				<td>Mark</td>
				<td>Otto</td>
				<td>Otto</td>
			<tr>
				<th scope="row">1</th>
				<td>Mark</td>
				<td>Otto</td>
				<td>@mdo</td>
				<td>Mark</td>
				<td>Otto</td>
				<td>Otto</td>
			<tr>
				<th scope="row">1</th>
				<td>Mark</td>
				<td>Otto</td>
				<td>@mdo</td>
				<td>Mark</td>
				<td>Otto</td>
				<td>Otto</td>

			</tr>
		</tbody>
	</table>
	
	<%@include file="footer.jsp" %>
</body>
</html>