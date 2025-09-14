

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
			<li class="nav-item active"><a class="nav-link" href="#"><i
					class="fas fa-home"></i>Home<span class="sr-only">(current)</span></a>
			</li>
			
			<li class="nav-item active"><a class="nav-link" href="recent_books.jsp">Recent
					Books</a></li>
			<li class="nav-item active"><a class="nav-link disabled"
				href="recommended_books.jsp">Recommended Books</a></li>
		</ul>


		<!-- Orders button  -->
		<div class="nav-item">
			<a class="btn btn-outline-primary position-relative mr-2"
				href="<%= request.getContextPath() %>/orders"> <i
				class="fa fa-box"></i> <span class="ms-1 d-none d-md-inline">Orders</span>
			</a>
		</div>



		<div class="nav-item dropdown">
			<a class="btn btn-outline-primary dropdown-toggle position-relative"
				href="#" id="cartDropdown" role="button" data-toggle="dropdown"
				aria-haspopup="true" aria-expanded="false"> <i
				class="fa fa-shopping-cart"></i> <span
				class="ms-1 d-none d-md-inline">Cart</span> <%-- Badge with count --%>
				<%
           int cartCount = 0;
           Object cartObj = session.getAttribute("cart");
           if (cartObj instanceof java.util.List) {
               cartCount = ((java.util.List<?>) cartObj).size();
           }
        %> <span
				class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
					<%= cartCount %>
			</span>
			</a>

			<ul class="dropdown-menu dropdown-menu-right p-3"
				aria-labelledby="cartDropdown" style="min-width: 250px;">
				<% 
            java.util.List<?> cart = (java.util.List<?>) session.getAttribute("cart");
            if (cart == null || cart.isEmpty()) {
        %>
				<li class="text-center text-muted">Your cart is empty</li>
				<% } else { 
             for (Object itemObj : cart) {
               String title = itemObj.toString(); 
        %>
				<li><%= title %></li>
				<%   } } %>
				<li><hr class="dropdown-divider"></li>
				<li class="d-flex justify-content-between"><a
					class="btn btn-sm btn-secondary"
					href="<%= request.getContextPath() %>/cart">View Cart</a> <a
					class="btn btn-sm btn-success"
					href="<%= request.getContextPath() %>/checkout">Checkout</a></li>
			</ul>
		</div>
	</div>
</nav>



