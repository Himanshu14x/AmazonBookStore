
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin: add books</title>
<link rel="icon" type="image/png"
	href="<%=request.getContextPath()%>/assets/images/icon.png" />
<%@include file="allCss.jsp"%>
</head>
<body style="background-color: #f0f2f2">
	<%@include file="navbar.jsp"%>

	<div class="container">
		<div class="row">
			<div class="col-md-4 offset-md-4">
				<div class="card">
					<div class="card-body">
						<h4 class="text-center">Add Books</h4>
						<c:if test="${not empty SuccessMsg }">
							<p class="text-center text-primary">${SuccessMsg }</p>
							<c:remove var="SuccessMsg" scope="session" />

						</c:if>
						<c:if test="${not empty error }">
							<p class="text-center text-danger">${error}</p>
							<c:remove var="error" scope="session" />

						</c:if>
				<form action="<%=request.getContextPath()%>/add_books" method="post" enctype="multipart/form-data">


							<div class="form-group">
								<label for="exampleInputEmail1">Book ID*</label> <input
									name="bid" type="text" class="form-control"
									id="exampleInputEmail1" aria-describedby="emailHelp" required>
							</div>

							<div class="form-group">
								<label for="exampleInputEmail1">Book Name*</label> <input
									name="bname" type="text" class="form-control"
									id="exampleInputEmail1" aria-describedby="emailHelp" required>
							</div>

							<div class="form-group">
								<label for="exampleInputEmail1">Author Name*</label> <input
									name="author" type="text" class="form-control"
									id="exampleInputEmail1" aria-describedby="emailHelp" required>
							</div>

							<div class="form-group">
								<label for="exampleInputPassword1">Rating*</label> <input
									name="rating" type="number" step="0.1" class="form-control"
									id="exampleInputPassword1" required>
							</div>

							<div class="form-group">
								<label for="exampleInputPassword1">Genre*</label> <input
									name="genre" type="text" class="form-control"
									id="exampleInputPassword1" required>
							</div>

							<div class="form-group">
								<label for="exampleInputPassword1">Price*</label> <input
									name="price" type="number" step="0.1" class="form-control"
									id="exampleInputPassword1" required>
							</div>

							<div class="form-group">
								<label for="exampleFormControlFile1">Upload Photo</label> <input
									name="bimg" type="file" class="form-control-file"
									id="exampleFormControlFile1" required>
							</div>

							<button type="submit" class="btn btn-primary">Add</button>





						</form>


					</div>


				</div>


			</div>


		</div>

	</div>
	<%@include file="footer.jsp"%>
</body>
</html>