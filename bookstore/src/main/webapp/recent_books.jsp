<%@ page import="com.DB.DynamoDBClientProvider" %>
<%@ page import="com.DAO.userDAOImpl" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.entity.User" %>
<%@ page import="java.util.*" %>
<%@ page import="software.amazon.awssdk.services.dynamodb.DynamoDbClient" %>



<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Recent Books</title>
<link rel="icon" type="image/png"
	href="<%=request.getContextPath()%>/assets/images/icon.png" />
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
	<h3 class="text-center mt-3">Books recently viewed by you</h3>
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
		
		

<%
    User u = (User) session.getAttribute("userobj");
    List<BookDetails> recentBooks = new ArrayList<>();
    if (u != null) {
        DynamoDbClient client = DynamoDBClientProvider.getClient();
        userDAOImpl ud = new userDAOImpl(client);
        List<String> ids = ud.getRecentViewed(u.getEmail());
        BookDAOImpl bd = new BookDAOImpl(client);
        for (String id : ids) {
            BookDetails b = bd.getBookById(id);
            if (b != null) recentBooks.add(b);
        }
    }
%>

<!-- render recentBooks list -->
<div class="container mt-4">

  <div class="row">
    <%
      for (BookDetails b : recentBooks) {
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
  </div>
</div>


</body>
</html>




