<%@ page import="com.entity.User"%>


<%
// Get logged-in user from session (your code stores user in "userobj")
User user1 = null;
Object uobj = session.getAttribute("userobj");
if (uobj instanceof User)
	user1 = (User) uobj;
%>

<!-- Top bar with logo/search/login/register -->
<div class="container-fluid py-3 border-bottom">
	<div class="row align-items-center">
		<!-- Left: Logo -->
		<div class="col-md-3 d-flex align-items-center">
			<a href="<%=request.getContextPath()%>/home.jsp"> <img
				src="<%=request.getContextPath()%>/assets/images/logo.png"
				alt="Ebook Logo"
				style="height: 50px; width: auto; margin-right: 10px;">
			</a>
		</div>

		<!-- Middle: Search -->
		<div class="col-md-6">
			<form class="d-flex" action="<%=request.getContextPath()%>/search"
				method="get">
				<input class="form-control search-input" name="q" type="search"
					placeholder="Search books, authors..." aria-label="Search"
					value="<%=request.getParameter("q") != null ? request.getParameter("q") : ""%>">
				<button class="btn search-btn" type="submit">
					<i class="fas fa-search"></i>
				</button>
			</form>
		</div>

		<!-- Right: Logins / User -->
		<div class="col-md-3 text-md-right mt-2 mt-md-0">
			<%
			if (user1 == null) {
			%>
			<!-- Not logged in: show Login / Register -->
			<a href="<%=request.getContextPath()%>/login.jsp"
				class="btn btn-primary mr-2"> <i class="fas fa-user"></i> Login
			</a> <a href="<%=request.getContextPath()%>/register.jsp"
				class="btn btn-success"> <i class="fas fa-user-plus"></i>
				Register
			</a>
			<%
			} else {
			// Logged in: show user name + dropdown with logout
			%>
			<div class="btn-group">
				<button type="button"
					class="btn btn-outline-primary dropdown-toggle"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<i class="fas fa-user-circle"></i> &nbsp;<%=user1.getName() != null ? user1.getName() : "User"%>
				</button>
				<div class="dropdown-menu dropdown-menu-right p-2">

					<a class="dropdown-item"
						href="<%=request.getContextPath()%>/orders.jsp">My Orders</a>
					<div class="dropdown-divider"></div>
					<a class="dropdown-item text-danger"
						href="<%=request.getContextPath()%>/logout">Logout</a>
				</div>
			</div>
			<%
			}
			%>
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
			<li class="nav-item active"><a class="nav-link"
				href="<%=request.getContextPath()%>/<%=(user1 != null ? "home.jsp" : "index.jsp")%>">
					<i class="fas fa-home"></i> Home
			</a></li>

			<%
			if (user1 != null) {
			%>
			<li class="nav-item"><a class="nav-link"
				href="<%=request.getContextPath()%>/recent_books.jsp">Recent
					Books</a></li>

			<%
			}
			%>
		</ul>

		<div class="d-flex align-items-center">
			<%
			if (user1 != null) {
			%>
			<!-- Orders button (visible only when logged in) -->
			<div class="nav-item mr-2">
				<a class="btn btn-outline-primary position-relative"
					href="<%=request.getContextPath()%>/orders.jsp"> <i
					class="fa fa-box"></i> <span class="ms-1 d-none d-md-inline">Orders</span>
				</a>
			</div>

			<!-- Cart dropdown (visible only when logged in) -->
			<div class="nav-item dropdown">
				<a class="btn btn-outline-primary" href="cart.jsp" id="cartDropdown"
					role="button" aria-haspopup="true" aria-expanded="false"> <i
					class="fa fa-shopping-cart"></i> <span
					class="ms-1 d-none d-md-inline">Cart</span>

				</a>


			</div>
			<%
			}
			%>
		</div>
	</div>
</nav>

<script>
	// optional: close other dropdowns when opening cart/user menu (bootstrap handles most)
</script>
