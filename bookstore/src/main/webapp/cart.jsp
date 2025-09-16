<%@ page import="com.DB.DynamoDBClientProvider" %>
<%@ page import="com.DAO.CartDAOImpl" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.entity.User" %>
<%@ page import="software.amazon.awssdk.services.dynamodb.DynamoDbClient" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="all_component/allCss.jsp" %> <%-- keep your project's global CSS & bootstrap --%>

<%
    // Get logged-in user
    User user = null;
    if (session != null) user = (User) session.getAttribute("userobj");
    if (user == null) {
        // if not logged in, redirect to login (or you can show a message)
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    DynamoDbClient client = DynamoDBClientProvider.getClient();
    CartDAOImpl cartDao = new CartDAOImpl(client);
    Map<String, String> cart = cartDao.getCart(user.getEmail()); // bookId -> qty (string)

    BookDAOImpl bookDao = new BookDAOImpl(client);

    int totalItems = 0;
    double grandTotal = 0.0;

    // prepare list of display entries
    class Line { public BookDetails book; public int qty; public double price; public double subtotal; }
    List<Line> lines = new ArrayList<>();
    if (cart != null) {
        for (Map.Entry<String, String> e : cart.entrySet()) {
            String bookId = e.getKey();
            int qty = 1;
            try { qty = Integer.parseInt(e.getValue()); } catch (Exception ignored) {}
            BookDetails b = bookDao.getBookById(bookId);
            if (b == null) continue; // skip missing
            double price = 0.0;
            try { price = Double.parseDouble(b.getPrice()); } catch (Exception ignored) {}
            double subtotal = price * qty;
            Line L = new Line();
            L.book = b; L.qty = qty; L.price = price; L.subtotal = subtotal;
            lines.add(L);
            totalItems += qty;
            grandTotal += subtotal;
        }
    }
%>

<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Your Cart</title>
<link rel="icon" type="image/png"
	href="<%=request.getContextPath()%>/assets/images/icon.png" />
  <style>
    /* Theme colors */
    :root {
      --primary-blue: #1370e2;
      --accent-yellow: #ffd400;
      --muted: #6c757d;
      --panel-bg: #fff;
      --page-bg: #f6f6f8;
      --card-border: rgba(19,112,226,0.08);
    }

    body { background: var(--page-bg); }

    .cart-container { max-width:1200px; margin: 28px auto; padding: 0 14px; }
    .cart-header { font-size:28px; font-weight:600; margin-bottom:18px; color: #111; }

    .cart-left { background: var(--panel-bg); padding:18px; border-radius:6px; box-shadow: 0 2px 8px rgba(0,0,0,0.03); }
    .cart-item { border-bottom: 1px solid #eee; padding:18px 0; display:flex; gap:18px; align-items:flex-start; }
    .cart-item:last-child { border-bottom:0; padding-bottom:12px; }

    .item-thumb { width:140px; height:140px; flex: 0 0 140px; border-radius:6px; overflow:hidden; background:#fafafa; display:flex; align-items:center; justify-content:center; }
    .item-thumb img { width:100%; height:100%; object-fit:cover; }

    .item-body { flex: 1; }
    .item-title { font-size:18px; font-weight:600; color:#111; text-decoration:none; display:block; margin-bottom:6px; }
    .item-author { color:var(--muted); font-size:13px; margin-bottom:6px; display:block; }
    .item-price { font-weight:700; color:var(--primary-blue); font-size:16px; }

    .qty-control { display:flex; align-items:center; gap:10px; margin-top:14px; }
    .qty-button { border:2px solid var(--primary-blue); background:transparent; color:var(--primary-blue); width:34px; height:34px; border-radius:6px; font-weight:700; display:inline-flex; align-items:center; justify-content:center; cursor:pointer; }
    .qty-display { min-width:36px; text-align:center; font-weight:600; }

    .remove-link { margin-left:12px; color:#c82333; text-decoration:none; font-size:14px; }

    /* right summary */
    .cart-right { margin-left:18px; width:320px; position:sticky; top:20px; align-self:flex-start; }
    .summary-card { background:var(--panel-bg); padding:18px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.04); }
    .summary-line { display:flex; justify-content:space-between; align-items:center; margin-bottom:10px; font-size:15px; color:#222; }
    .summary-total { font-size:18px; font-weight:700; color:#111; }
    .proceed-btn { display:block; width:100%; text-align:center; background:var(--accent-yellow); color:#111; padding:12px; border-radius:28px; font-weight:700; border:none; cursor:pointer; box-shadow:0 2px 6px rgba(0,0,0,0.06); }

    /* smaller screens */
    @media (max-width: 991px) {
      .layout { flex-direction:column; }
      .cart-right { width:100%; position:static; margin-top:14px; }
    }

    /* tidy small text */
    .muted { color:var(--muted); font-size:13px; }
    .price-right { text-align:right; min-width:100px; font-weight:700; }

    /* small delete icon */
    .trash { color:#666; margin-right:8px; }
  </style>
</head>
<body>
<!-- navbar content -->  





<%@include file="all_component/navbar.jsp"%>



<!-- Navbar content -->
  <div class="cart-container">
    <div class="cart-header">Shopping Cart</div>

    <div style="display:flex; gap:18px;" class="layout">
      <!-- LEFT: items -->
      <div style="flex:1" class="cart-left">
        <%
          if (lines.isEmpty()) {
        %>
          <div style="padding:22px">
            <div class="muted">Your cart is currently empty.</div>
            <div style="margin-top:12px">
              <a href="<%=request.getContextPath()%>/home.jsp" class="btn" style="background:var(--primary-blue); color:#fff; padding:10px 14px; border-radius:6px; text-decoration:none;">Continue shopping</a>
            </div>
          </div>
        <%
          } else {
            for (Line L : lines) {
        %>
          <div class="cart-item">
            <div class="item-thumb">
              <img src="<%=request.getContextPath()%>/book/<%=L.book.getPhoto()%>" alt="<%=L.book.getTitle()%>" onerror="this.src='<%=request.getContextPath()%>/assets/images/icon.png'"/>
            </div>

            <div class="item-body">
              <a class="item-title" href="<%=request.getContextPath()%>/view_books.jsp?bid=<%=L.book.getId()%>"><%=L.book.getTitle()%></a>
              <div class="item-author"><%=L.book.getAuthor()%></div>
              <div class="muted">In stock</div>

              <div style="display:flex; justify-content:space-between; align-items:center; margin-top:8px;">
                <div>
                  <div class="item-price">₹ <%= String.format("%.2f", L.price) %></div>
                  <div class="muted" style="margin-top:4px">Subtotal: ₹ <%= String.format("%.2f", L.subtotal) %></div>
                </div>

                <div style="text-align:right;">
                  <!-- Quantity controls: each form posts to /cart/update with desired qty -->
                  <div class="qty-control">
                    <form action="<%=request.getContextPath()%>/cart/update" method="post" style="display:inline;">
                      <input type="hidden" name="bookId" value="<%=L.book.getId()%>" />
                      <input type="hidden" name="qty" value="<%= Math.max(0, L.qty - 1) %>" />
                      <button type="submit" class="qty-button" title="Decrease">−</button>
                    </form>

                    <div class="qty-display"><%=L.qty%></div>

                    <form action="<%=request.getContextPath()%>/cart/update" method="post" style="display:inline;">
                      <input type="hidden" name="bookId" value="<%=L.book.getId()%>" />
                      <input type="hidden" name="qty" value="<%= (L.qty + 1) %>" />
                      <button type="submit" class="qty-button" title="Increase">+</button>
                    </form>

                    <form action="<%=request.getContextPath()%>/cart/update" method="post" style="display:inline; margin-left:12px;">
                      <input type="hidden" name="bookId" value="<%=L.book.getId()%>" />
                      <input type="hidden" name="qty" value="0" />
                      <button type="submit" class="remove-link" style="background:none;border:none;padding:0;font-size:14px;color:#c82333;cursor:pointer;">Delete</button>
                    </form>
                  </div>

                </div>
              </div>
            </div>
          </div>
        <%
            } // for
          } // else
        %>
      </div>

     <!-- RIGHT: summary & order form -->
<div class="cart-right">
  <div class="summary-card">
    <div style="margin-bottom:8px; font-size:14px; color:var(--muted)">Subtotal (<%= totalItems %> items)</div>
    <div class="summary-line summary-total" style="margin-bottom:14px;">
      <div style="font-size:20px; font-weight:800">₹ <%= String.format("%.2f", grandTotal) %></div>
    </div>

    <!-- Order / Checkout form -->
    <form action="<%=request.getContextPath()%>/order/place" method="post">
      <div style="margin-bottom:10px;">
        <label style="font-weight:600; display:block; margin-bottom:6px;">Contact phone</label>
        <input type="text" name="phoneNumber" value="<%= (user.getPhoneNumber() != null ? user.getPhoneNumber() : "") %>" class="form-control" placeholder="Phone number" required />
      </div>

      <div style="margin-bottom:10px;">
        <label style="font-weight:600; display:block; margin-bottom:6px;">Address</label>
        <textarea name="address" class="form-control" rows="2" placeholder="House no., street, area" required><%= (user.getAddress()!=null ? user.getAddress() : "") %></textarea>
      </div>

      <div style="display:flex; gap:8px; margin-bottom:10px;">
        <div style="flex:1">
          <label style="font-weight:600; display:block; margin-bottom:6px;">Landmark</label>
          <input type="text" name="landmark" value="<%= (user.getLandmark()!=null ? user.getLandmark() : "") %>" class="form-control" placeholder="Landmark (optional)" />
        </div>
        <div style="flex:1">
          <label style="font-weight:600; display:block; margin-bottom:6px;">City</label>
          <input type="text" name="city" value="<%= (user.getCity()!=null ? user.getCity() : "") %>" class="form-control" placeholder="City" required />
        </div>
      </div>

      <div style="margin-bottom:12px;">
        <label style="font-weight:600; display:block; margin-bottom:6px;">Pincode</label>
        <input type="text" name="pincode" value="<%= (user.getPincode()!=null ? user.getPincode() : "") %>" class="form-control" placeholder="PIN code" required />
      </div>

      <div style="margin-bottom:12px;">
        <label style="display:block; font-weight:600; margin-bottom:6px;">Payment method</label>
        <div>
          <label style="margin-right:8px;"><input type="radio" name="paymentMethod" value="Credit/Debit Card" checked /> Credit/Debit Card</label><br/>
          <label style="margin-right:8px;"><input type="radio" name="paymentMethod" value="UPI" /> UPI</label><br/>
          <label style="margin-right:8px;"><input type="radio" name="paymentMethod" value="COD" /> Cash on Delivery (COD)</label><br/>
          <label style="margin-right:8px;"><input type="radio" name="paymentMethod" value="Net Banking" /> Net Banking</label>
        </div>
      </div>

      <div style="margin-top:6px;">
        <button type="submit" class="proceed-btn">Proceed to Pay</button>
      </div>
    </form>

    <div style="margin-top:10px; color:var(--muted); font-size:13px;">
      Secure transaction.
    </div>
  </div>

  <!-- promotional card below -->
  <div style="margin-top:12px;">
    <div style="background: linear-gradient(180deg, rgba(19,112,226,0.06), rgba(19,112,226,0.02)); border-radius:8px; padding:12px; color:var(--primary-blue); font-weight:600;">
      Free delivery on eligible orders
    </div>
  </div>
</div>

    </div>
  </div>

  <%@ include file="all_component/footer.jsp" %>
</body>
</html>
