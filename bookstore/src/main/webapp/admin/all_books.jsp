

<%@page import="com.entity.BookDetails"%>
<%@page import="java.util.List"%>
<%@page import="com.DB.DynamoDBClientProvider"%>
<%@page import="software.amazon.awssdk.services.dynamodb.DynamoDbClient"%>
<%@page import="com.DAO.BookDAOImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin: All Books</title>
<link rel="icon" type="image/png"
	href="<%=request.getContextPath()%>/assets/images/icon.png" />
<%@include file="allCss.jsp"%>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<h3 class="text-center">All Books</h3>
	<table class="table table-striped">
		<thead class="bg-primary text-white">
			<tr>
				<th scope="col">ID</th>
				<th scope="col">Image</th>
				<th scope="col">Title</th>
				<th scope="col">Author</th>
				<th scope="col">Genre</th>
				<th scope="col">Rating</th>
				<th scope="col">Price</th>
				<th scope="col">Action</th>
			</tr>
		</thead>
		<tbody>
		<%BookDAOImpl dao = new BookDAOImpl(DynamoDBClientProvider.getClient());
	
		List<BookDetails> list = dao.getAllBooks();
		for(BookDetails b : list){
			%>
			
			<tr>
				<td><%=b.getId() %></td>
				<td><img src="../book/<%=b.getPhoto()%>" style="width: 50px; height:50px;" ></td>
				<td><%=b.getTitle() %></td>
				<td><%=b.getAuthor() %></td>
				<td><%=b.getGenre() %></td>
				<td><%=b.getRating() %></td>
			    <td><%=b.getPrice() %></td>
			
				<td>
				<a href="#" class="btn btn-sm btn-primary">Edit</a>
				<a href="#" class="btn btn-sm btn-danger">Delete</a>
				</td>
			</tr>
			
			<%
			
		}
		
		  %>
			
						
		</tbody>
	</table>
	<%@include file="footer.jsp" %>
</body>
</html>