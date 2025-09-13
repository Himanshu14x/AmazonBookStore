
<%@ page import="software.amazon.awssdk.services.dynamodb.DynamoDbClient, com.DB.DynamoDBClientProvider" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Amazon</title>

    <!-- Favicon -->
    <link rel="icon" type="image/png" href="<%=request.getContextPath()%>/assets/images/icon.png" />

    <%@include file="all_component/allCss.jsp" %>
    
  <style type="text/css">
.back-img {
 
  background-image: url('<%=request.getContextPath()%>/assets/images/slider1.jpg');
  height: 40vh;
  width: 100%;
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
}
</style>

</head>

<body>
	<%@include file="all_component/navbar.jsp" %>
	<div class="container-fluid back-img">
</div>

<%
software.amazon.awssdk.services.dynamodb.DynamoDbClient connect = com.DB.DynamoDBClientProvider.getClient();
%>

<!--  Start Recent Books -->
<div class="container-fluid mt-4">
  <h3 class="text-center mb-4">Recent Books</h3>

  <div class="row">
    <!-- Book 1 -->
    <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
      <div class="card h-100 book-card">
        <img src="book/book1.jpg" alt="Book 1" class="card-img-top book-cover img-thumbnail">
        <div class="card-body d-flex flex-column">
          <h5 class="card-title text-truncate">Programming Skills For Data Science</h5>
          <p class="card-subtitle text-muted mb-2 small">Michael Freeman</p>

          <div class="mt-auto d-flex justify-content-between align-items-center">
            <div class="price">&#8377; 543.00</div>

            <div class="btn-group btn-group-sm" role="group">
              <form action="cart/add" method="post" style="display:inline;">
                <input type="hidden" name="productId" value="1">
                <input type="hidden" name="qty" value="1">
                <button type="submit" class="btn btn-success btn-sm">
                  <i class="fa fa-shopping-cart"></i> Add
                </button>
              </form>
              <a class="btn btn-outline-secondary btn-sm" href="book?id=1">View</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Book 2 -->
    <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
      <div class="card h-100 book-card">
        <img src="book/book2.jpg" alt="Book 2" class="card-img-top book-cover img-thumbnail">
        <div class="card-body d-flex flex-column">
          <h5 class="card-title text-truncate">Dune</h5>
          <p class="card-subtitle text-muted mb-2 small">Frank Herbert</p>

          <div class="mt-auto d-flex justify-content-between align-items-center">
            <div class="price">&#8377; 849.00</div>

            <div class="btn-group btn-group-sm" role="group">
              <form action="cart/add" method="post" style="display:inline;">
                <input type="hidden" name="productId" value="2">
                <input type="hidden" name="qty" value="1">
                <button type="submit" class="btn btn-success btn-sm">
                  <i class="fa fa-shopping-cart"></i> Add
                </button>
              </form>
              <a class="btn btn-outline-secondary btn-sm" href="book?id=2">View</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Book 3 -->
    <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
      <div class="card h-100 book-card">
        <img src="book/book3.jpg" alt="Book 3" class="card-img-top book-cover img-thumbnail">
        <div class="card-body d-flex flex-column">
          <h5 class="card-title text-truncate">Project Hail Mary</h5>
          <p class="card-subtitle text-muted mb-2 small">Andy Weir</p>

          <div class="mt-auto d-flex justify-content-between align-items-center">
            <div class="price">&#8377; 1175.00</div>

            <div class="btn-group btn-group-sm" role="group">
              <form action="cart/add" method="post" style="display:inline;">
                <input type="hidden" name="productId" value="3">
                <input type="hidden" name="qty" value="1">
                <button type="submit" class="btn btn-success btn-sm">
                  <i class="fa fa-shopping-cart"></i> Add
                </button>
              </form>
              <a class="btn btn-outline-secondary btn-sm" href="book?id=3">View</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Book 4 -->
    <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
      <div class="card h-100 book-card">
        <img src="book/book4.jpg" alt="Book 4" class="card-img-top book-cover img-thumbnail">
        <div class="card-body d-flex flex-column">
          <h5 class="card-title text-truncate">Where the Crawdads Sing</h5>
          <p class="card-subtitle text-muted mb-2 small">Delia Owens</p>

          <div class="mt-auto d-flex justify-content-between align-items-center">
            <div class="price">&#8377; 1395.00</div>

            <div class="btn-group btn-group-sm" role="group">
              <form action="cart/add" method="post" style="display:inline;">
                <input type="hidden" name="productId" value="4">
                <input type="hidden" name="qty" value="1">
                <button type="submit" class="btn btn-success btn-sm">
                  <i class="fa fa-shopping-cart"></i> Add
                </button>
              </form>
              <a class="btn btn-outline-secondary btn-sm" href="book?id=4">View</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Book 5 -->
    <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
      <div class="card h-100 book-card">
        <img src="book/book5.jpg" alt="Book 5" class="card-img-top book-cover img-thumbnail">
        <div class="card-body d-flex flex-column">
          <h5 class="card-title text-truncate">The Midnight Library</h5>
          <p class="card-subtitle text-muted mb-2 small">Matt Haig</p>

          <div class="mt-auto d-flex justify-content-between align-items-center">
            <div class="price">&#8377; 1699.00</div>

            <div class="btn-group btn-group-sm" role="group">
              <form action="cart/add" method="post" style="display:inline;">
                <input type="hidden" name="productId" value="5">
                <input type="hidden" name="qty" value="1">
                <button type="submit" class="btn btn-success btn-sm">
                  <i class="fa fa-shopping-cart"></i> Add
                </button>
              </form>
              <a class="btn btn-outline-secondary btn-sm" href="book?id=5">View</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    
    <!-- Book 6 -->
    <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
      <div class="card h-100 book-card">
        <img src="book/book6.jpg" alt="Atomic Habits" class="book-cover">
        <div class="card-body">
          <h5 class="card-title text-truncate">Atomic Habits</h5>
          <p class="card-subtitle">James Clear</p>
          <div class="card-actions">
            <div class="price">&#8377; 599.00</div>
            <div class="btn-group" role="group">
              <form action="cart/add" method="post" style="display:inline;">
                <input type="hidden" name="productId" value="6">
                <input type="hidden" name="qty" value="1">
                <button type="submit" class="btn btn-sm btn-add"><i class="fa fa-shopping-cart"></i> Add</button>
              </form>
              <a href="book?id=6" class="btn btn-sm btn-outline-secondary btn-view">View</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Book 7 -->
    <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
      <div class="card h-100 book-card">
        <img src="book/book7.jpg" alt="Ikigai" class="book-cover">
        <div class="card-body">
          <h5 class="card-title text-truncate">Ikigai: The Japanese Secret</h5>
          <p class="card-subtitle">Héctor García</p>
          <div class="card-actions">
            <div class="price">&#8377; 399.00</div>
            <div class="btn-group" role="group">
              <form action="cart/add" method="post" style="display:inline;">
                <input type="hidden" name="productId" value="7">
                <input type="hidden" name="qty" value="1">
                <button type="submit" class="btn btn-sm btn-add"><i class="fa fa-shopping-cart"></i> Add</button>
              </form>
              <a href="book?id=7" class="btn btn-sm btn-outline-secondary btn-view">View</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    

  </div> <!-- row -->
  <div class="text-center mt-1">
  
  <a href="" class="btn btn-danger btn-sm text-white"> View All</a>
  </div>
  
</div> <!-- container -->

<!--  End Recent Books -->

<br></br>

<%@include file="all_component/footer.jsp" %>
</body>
</html>