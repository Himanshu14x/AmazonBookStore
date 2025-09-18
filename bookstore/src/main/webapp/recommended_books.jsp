<%@ page import="com.DB.DynamoDBClientProvider" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.entity.User" %>
<%@ page import="software.amazon.awssdk.services.dynamodb.DynamoDbClient" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>All Recommended Books</title>
 <%@include file="all_component/allCss.jsp" %>
 
 
 <div class="mt-4">
  <h5>Recommended for you</h5>
  <div class="row">
  
  
    <%
    // Get the book id from request param (try "bid" then "id")
    String id = request.getParameter("bid");
    if (id == null || id.trim().isEmpty()) {
        id = request.getParameter("id");
    }

    BookDetails b = null;
    if (id != null && !id.trim().isEmpty()) {
        try {
            DynamoDbClient client = DynamoDBClientProvider.getClient();
            BookDAOImpl dao = new BookDAOImpl(client);
            b = dao.getBookById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
  %>
  
  
  <%
    // record recent view for logged-in user
    User sessionUser = (User) session.getAttribute("userobj");
    if (sessionUser != null && b != null) {
        try {
            DynamoDbClient client = DynamoDBClientProvider.getClient();
            com.DAO.userDAOImpl udao = new com.DAO.userDAOImpl(client);
            udao.addRecentViewed(sessionUser.getEmail(), b.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get recommendations by same genre rating >= 4
    List<BookDetails> recs = new ArrayList<>();
    try {
        DynamoDbClient client = DynamoDBClientProvider.getClient();
        com.DAO.BookDAOImpl bookDao = new com.DAO.BookDAOImpl(client);
        if (b != null && b.getGenre() != null && !b.getGenre().trim().isEmpty()) {
            recs = bookDao.getRecommendedByGenre(b.getGenre(), 4.0, 5, b.getId());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
    <%
      for (BookDetails rb : recs) {
    %>
      <div class="col-lg-2 col-md-3 col-6 text-center mb-3">
        <a href="view_books.jsp?bid=<%= rb.getId() %>">
          <img src="<%= request.getContextPath() %>/book/<%= rb.getPhoto() %>" style="width:100%;height:150px;object-fit:cover;" onerror="this.src='<%= request.getContextPath() %>/assets/images/icon.png'"/>
          <div class="small"><%= rb.getTitle() %></div>
          <div class="small-muted">₹<%= rb.getPrice() %></div>
        </a>
      </div>
    <%
      }
    %>
  </div>
</div>
 
</head>
<body>

</body>
</html>