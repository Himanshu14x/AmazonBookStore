

<!-- Top bar with logo/search/login/register -->
<div class="container-fluid py-3 border-bottom">
	<div class="row align-items-center">
		<!-- Left: Logo -->
		<div class="col-md-3 d-flex align-items-center">
			<a href="<%= request.getContextPath() %>/"> <img
				src="<%= request.getContextPath() %>/assets/images/logo.png"
				alt="Ebook Logo"
				style="height: 50px; width: auto; margin-right: 10px;">
			</a>
		</div>

		
<!-- Middle: Search -->
<div class="col-md-6">
      <form class="d-flex">
        <input class="form-control search-input" type="search"
               placeholder="Search books, authors..." aria-label="Search">
        <button class="btn search-btn" type="submit">
          <i class="fas fa-search"></i>
        </button>
      </form>
    </div>



		<!-- Right: Login & Register -->
		<div class="col-md-3 text-md-right mt-2 mt-md-0">
			<a href="login.jsp" class="btn btn-primary mr-2"> <i
				class="fas fa-user-plus"></i> Login
			</a> <a href="register.jsp" class="btn btn-success"> <i
				class="fas fa-sign-in-alt"></i> Register
			</a>

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



