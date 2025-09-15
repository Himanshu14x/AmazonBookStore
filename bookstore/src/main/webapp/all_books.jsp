

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
<%@include file="all_component/allCss.jsp"%>

</head>
<body>


<style>
.table td, .table th {
    vertical-align: middle !important;
}
</style>
	<%@include file="all_component/navbar.jsp"%>
		<c:if test="${empty userobj }">
	
	<c:redirect url="../login.jsp"/>
	
	</c:if>
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
				<th scope="col"></th>
			</tr>
		</thead>
		<tbody>
		<%BookDAOImpl dao = new BookDAOImpl(DynamoDBClientProvider.getClient());
	
		List<BookDetails> list = dao.getAllBooks();
		for(BookDetails b : list){
			%>
			
			<tr>
				<td><%=b.getId() %></td>
				<td><img src="book/<%=b.getPhoto()%>" style="width: 60px; height:90px;" ></td>
				<td><%=b.getTitle() %></td>
				<td><%=b.getAuthor() %></td>
				<td><%=b.getGenre() %></td>
				<td><%=b.getRating() %></td>
			    <td><%=b.getPrice() %></td>
				<td>

<div class="btn-group btn-group-sm" role="group">
              <form action="cart/add" method="post" style="display:inline;">
                <input type="hidden" name="productId" value="1">
                <input type="hidden" name="qty" value="1">
                <button type="submit" class="btn btn-success btn-sm">
                  <i class="fa fa-shopping-cart"></i> Add
                </button>
              </form>
              <a class="btn btn-outline-secondary btn-sm" href="view_books.jsp?bid=<%=b.getId()%>">View</a>
            </div>


</td>
			
			</tr>
			
			<%
			
		}
		
		  %>
			
						
		</tbody>
	</table>
	<%@include file="all_component/footer.jsp" %>
</body>
</html>