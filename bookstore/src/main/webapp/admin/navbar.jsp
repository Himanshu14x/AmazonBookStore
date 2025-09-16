
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Top bar with logo/search/login/register -->
<div class="container-fluid py-3 border-bottom">
	<div class="row align-items-center">
		<!-- Left: Logo -->
		<div class="col-md-3 d-flex align-items-center">
			<a href=""> <img
				src="<%=request.getContextPath()%>/assets/images/logo.png"
				alt="Ebook Logo"
				style="height: 50px; width: auto; margin-right: 10px;">
			</a>
		</div>


	


		<div class="col-md-3 ml-md-auto mt-2 mt-md-0 text-right">


			<c:if test="${not empty userobj}">
				<a class="btn btn-primary text-white mr-2"> <i
					class="fas fa-user"></i> ${userobj.name }
				</a>
					<a data-toggle="modal" data-target="#exampleModal" class="btn btn-success"> <i
					class="fas fa-sign-in-alt"></i> Logout
				</a>
			</c:if>


			<c:if test="${empty userobj }">
				<a href="../login.jsp" class="btn btn-primary mr-2"> <i
					class="fas fa-user-plus"></i> Login
				</a>
				<a href="../register.jsp" class="btn btn-success"> <i
					class="fas fa-sign-in-alt"></i> Register
				</a>
			</c:if>
		</div>

	</div>
</div>


<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      <div class="text-center">
        <h4>Do you want to logout?</h4>
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
       <a href = "../logout" type="button" class="btn btn-primary text-white">Logout</a>
      
      </div>
        
      </div>
      <div class="modal-footer">
      
      </div>
    </div>
  </div>
</div>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-custom">


	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#navbarSupportedContent"
		aria-controls="navbarSupportedContent" aria-expanded="false"
		aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>

	<div class="collapse navbar-collapse" id="navbarSupportedContent">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item active"><a class="nav-link" href="home.jsp"><i
					class="fas fa-home"></i>Home<span class="sr-only">(current)</span></a>
			</li>


		</ul>






	</div>
</nav>



