<%@ page import="com.DB.DynamoDBClientProvider" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.entity.BookDetails" %>
<%@ page import="software.amazon.awssdk.services.dynamodb.DynamoDbClient" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Book - Bookstore</title>
  <%@ include file="all_component/allCss.jsp" %>
  <style>
    /* (same CSS as before, omitted for brevity) */
    .book-cover { width:100%; max-width:360px; height:auto; object-fit:cover; border-radius:6px; box-shadow:0 2px 6px rgba(0,0,0,.08); }
    .price-badge { font-size:1.6rem; font-weight:700; color:#1370e2; }
    .small-muted { color:#6c757d; font-size:0.9rem; }
    .product-panel { background:#fff; border-radius:8px; padding:18px; box-shadow:0 2px 6px rgba(0,0,0,.03); }
    .add-cart { background:#ffd400; border:none; width:100%; padding:12px 14px; border-radius:6px; font-weight:600; }
    .buy-now { background:#fb641b; border:none; width:100%; padding:12px 14px; border-radius:6px; color:#fff; margin-top:8px; font-weight:600; }
  </style>
</head>
<body>
  <%@ include file="all_component/navbar.jsp" %>

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

  <div class="container mt-4 mb-5">
    <%
      if (b == null) {
    %>
      <div class="alert alert-warning">Book not found.</div>
    <%
      } else {
    %>
      <div class="row gx-4">
        <!-- Left: cover -->
        <div class="col-lg-3 col-md-4 col-sm-12 text-center">
          <div class="product-panel">
            <img class="book-cover" src="<%= request.getContextPath() %>/book/<%= b.getPhoto() %>" alt="<%= b.getTitle() %>"
                 onerror="this.src='<%= request.getContextPath() %>/assets/images/icon.png'"/>
            <div class="mt-3 small-muted">by <strong><%= b.getAuthor() %></strong></div>
            <div class="mt-2 small-muted">Rating: <strong><%= b.getRating() != null ? b.getRating() : "0.0" %></strong></div>
          </div>
        </div>

        <!-- Middle: details -->
        <div class="col-lg-6 col-md-8 col-sm-12">
          <div class="product-panel">
            <h2 style="margin-bottom:6px;"><%= b.getTitle() %></h2>
            <div class="small-muted mb-3">by <strong><%= b.getAuthor() %></strong></div>

            <div class="mb-3">
              <span class="price-badge">₹<%= b.getPrice() %></span>
              <div class="small-muted">Inclusive of all taxes</div>
            </div>

            <hr/>

            <h5>Description</h5>
            <p class="small-muted">
              <!-- Static placeholder description -->
              A crisp edition for your reading. Genre: <strong><%= b.getGenre() %></strong>.
            </p>

            <div class="row specs text-center mt-4">
              <div class="col-2"><div class="small-muted">Free Delivery</div></div>
              <div class="col-2"><div class="small-muted">10 days Replacement</div></div>
              <div class="col-2"><div class="small-muted">Amazon Delivered</div></div>
              <div class="col-3"><div class="small-muted">Pay on Delivery</div></div>
              <div class="col-3"><div class="small-muted">Secure transaction</div></div>
            </div>

            <hr/>

            <h6>Product details</h6>
            <ul class="small-muted">
              <li>Title: <strong><%= b.getTitle() %></strong></li>
              <li>Author: <strong><%= b.getAuthor() %></strong></li>
              <li>Genre: <strong><%= b.getGenre() %></strong></li>
              <li>Rating: <strong><%= b.getRating() %></strong></li>
            </ul>
          </div>
        </div>

        <!-- Right: purchase panel -->
        <div class="col-lg-3 col-md-12">
          <div class="product-panel">
            <div class="mb-2">
              <div class="small-muted">Price</div>
              <div class="price-badge">₹<%= b.getPrice() %></div>
            </div>

            <div class="mb-3 small-muted">Inclusive of all taxes</div>
            <div class="mb-3"><div class="small-muted">In stock</div><div class="small-muted">Ships from <strong>Amazon</strong></div></div>

            <!-- Add to cart form -->
            <form action="<%= request.getContextPath() %>/cart/add" method="post" class="mb-2">
              <input type="hidden" name="productId" value="<%= b.getId() %>" />
              <input type="hidden" name="title" value="<%= b.getTitle() %>" />
              <input type="hidden" name="price" value="<%= b.getPrice() %>" />
              <input type="hidden" name="photo" value="<%= b.getPhoto() %>" />

              <div class="form-group mb-2">
                <label for="qty">Quantity</label>
                <select name="qty" id="qty" class="form-control">
                  <option>1</option>
                  <option>2</option>
                  <option>3</option>
                  <option>4</option>
                  <option>5</option>
                </select>
              </div>

              <button type="submit" class="btn add-cart">Add to Cart</button>
            </form>

            <!-- Buy Now form (simple) -->
            <form action="<%= request.getContextPath() %>/buy" method="post">
              <input type="hidden" name="productId" value="<%= b.getId() %>" />
              <input type="hidden" name="qty" value="1" />
              <button type="submit" class="btn buy-now">Buy Now</button>
            </form>

            <div class="mt-3 small-muted">Payment: Credit/Debit cards, UPI &amp; more</div>
          </div>
        </div>
      </div>

   

    <%
      } // end else b != null
    %>
  </div>

  <%@ include file="all_component/footer.jsp" %>

  <script>
    (function(){
      document.querySelectorAll('form').forEach(f=>{
        f.addEventListener('submit', function(e){
          const btn = this.querySelector('button[type="submit"]');
          if (btn) { btn.disabled = true; }
        });
      });
    })();
  </script>
</body>
</html>
