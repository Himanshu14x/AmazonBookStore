
<%@page import="com.entity.BookDetails"%>
<%@page import="com.DAO.BookDAOImpl"%>
<%@ page
	import="software.amazon.awssdk.services.dynamodb.DynamoDbClient, com.DB.DynamoDBClientProvider"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Amazon : Home</title>

<!-- Favicon -->
<link rel="icon" type="image/png"
	href="<%=request.getContextPath()%>/assets/images/icon.png" />

<%@include file="all_component/allCss.jsp"%>

<style type="text/css">
.back-img {
	background-image:
		url('<%=request.getContextPath()%>/assets/images/slider1.jpg');
	height: 40vh;
	width: 100%;
	background-repeat: no-repeat;
	background-size: cover;
	background-position: center;
}


#toast {
	min-width: 300px;
	position: fixed;
	bottom: 30px;
	left: 50%;
	margin-left: -125px;
	background: #333;
	padding: 10px;
	color: white;
	text-align: center;
	z-index: 1;
	font-size: 18px;
	visibility: hidden;
	box-shadow: 0px 0px 100px #000;
}

#toast.display {
	visibility: visible;
	animation: fadeIn 0.5, fadeOut 0.5s 2.5s;
}

@keyframes fadeIn {from { bottom:0;
	opacity: 0;
}

to {
	bottom: 60px;
	opacity: 1;
}

}
@keyframes fadeOut {form { bottom:60px;
	opacity: 1;
}

to {
	bottom: 0;
	opacity: 0;
}
}


</style>

</head>

<body>
<c:if test="${not empty successMsg}">

<div id="toast">${successMsg}</div>

<script type="text/javascript">
		showToast();
		function showToast(content)
		{
		    $('#toast').addClass("display");
		    $('#toast').html(content);
		    setTimeout(()=>{
		        $("#toast").removeClass("display");
		    },2000)
		}	
</script>

</c:if>

	<%@include file="all_component/navbar.jsp"%>
	<div class="container-fluid back-img"></div>

	<%
	software.amazon.awssdk.services.dynamodb.DynamoDbClient connect = com.DB.DynamoDBClientProvider.getClient();
	%>

	<!--  Start Recent Books -->
	<div class="container-fluid mt-4">
		<h3 class="text-center mb-4">Featured Books</h3>

		<div class="row">
			<!-- Book 1 -->
			<%
			User u = (User) session.getAttribute("userobj");
			%>

			<%
			BookDAOImpl dao = new BookDAOImpl(DynamoDBClientProvider.getClient());
			List<BookDetails> list = dao.getNewBook();
			for (BookDetails b : list) {
			%>
			<div class="col-sm-6 col-md-4 col-lg-3 mb-4">
				<div class="card h-100 book-card">
					<img src="book/<%=b.getPhoto()%>" alt="Book 1"
						class="card-img-top book-cover img-thumbnail">
					<div class="card-body d-flex flex-column">
						<h5 class="card-title text-truncate"><%=b.getTitle()%></h5>
						<p class="card-subtitle text-muted mb-2 small"><%=b.getAuthor()%></p>

						<%

						%>
						<div
							class="mt-auto d-flex justify-content-between align-items-center">
							<div class="price">
								&#8377;
								<%=b.getPrice()%></div>

							<div class="btn-group btn-group-sm" role="group">
								<form action="<%=request.getContextPath()%>/cart/add"
									method="post" style="display: inline;">
									<input type="hidden" name="bookId" value="<%=b.getId()%>">
									<input type="hidden" name="qty" value="1">
									<button type="submit" class="btn btn-success btn-sm">
										<i class="fa fa-shopping-cart"></i> Add
									</button>
								</form>


								<a class="btn btn-outline-secondary btn-sm"
									href="view_books.jsp?bid=<%=b.getId()%>">View</a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<%
			}
			%>





		</div>
		<!-- row -->
		<div class="text-center mt-1">

			<a href="all_books.jsp" class="btn btn-danger btn-sm text-white">
				View All</a>
		</div>

	</div>
	<!-- container -->

	<!--  End Recent Books -->

	<br></br>

	<%@include file="all_component/footer.jsp"%>
</body>
</html>