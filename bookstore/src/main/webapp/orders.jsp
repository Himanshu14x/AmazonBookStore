<%@ page import="com.DB.DynamoDBClientProvider" %>
<%@ page import="com.DAO.OrderDAOImpl" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.entity.Order" %>
<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.entity.User" %>
<%@ page import="software.amazon.awssdk.services.dynamodb.DynamoDbClient" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="all_component/allCss.jsp" %>


<%
    // require login
    User user = null;
    if (session != null) user = (User) session.getAttribute("userobj");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    DynamoDbClient client = DynamoDBClientProvider.getClient();
    OrderDAOImpl orderDao = new OrderDAOImpl(client);
    BookDAOImpl bookDao = new BookDAOImpl(client);

    // fetch orders for this user
    List<Order> orders = orderDao.getOrdersByEmail(user.getEmail());
    // sort latest first by createdAt if possible
    orders.sort((o1, o2) -> {
        try { return o2.getCreatedAt().compareTo(o1.getCreatedAt()); } catch (Exception ex) { return 0; }
    });
%>

<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>My Orders</title>
  <link rel="icon" type="image/png"
	href="<%=request.getContextPath()%>/assets/images/icon.png" />

  <style>
    :root {
      --primary-blue: #1370e2;
      --accent-yellow: #ffd400;
      --muted: #6c757d;
      --panel-bg: #fff;
      --page-bg: #f6f6f8;
    }
    body { background: var(--page-bg); }
    .container { max-width:1200px; margin:28px auto; padding: 0 14px; }
    .orders-header { font-size:26px; font-weight:700; margin-bottom:16px; color:#111; }
    .order-card { background:var(--panel-bg); border-radius:8px; padding:14px; margin-bottom:16px; box-shadow:0 2px 8px rgba(0,0,0,0.03); }
    .order-meta { display:flex; justify-content:space-between; align-items:center; gap:12px; margin-bottom:12px; }
    .order-id { font-weight:700; color:var(--primary-blue); }
    .order-date { color:var(--muted); font-size:13px; }

    table.order-items { width:100%; border-collapse:collapse; margin-top:8px; }
    table.order-items th, table.order-items td { padding:10px 8px; border-bottom:1px solid #eee; text-align:left; vertical-align:middle; }
    table.order-items th { font-size:13px; color:var(--muted); font-weight:600; }
    .item-thumb { width:72px; height:72px; object-fit:cover; border-radius:6px; }

    .price-right { text-align:right; white-space:nowrap; font-weight:700; color:var(--primary-blue); }
    .subtotal-line { display:flex; justify-content:flex-end; margin-top:8px; font-weight:700; font-size:16px; }

    .no-orders { padding:18px; color:var(--muted); }

    .small-muted { color:var(--muted); font-size:13px; }
    .pay-method { font-weight:600; color:#333; }

    /* responsive */
    @media (max-width: 768px) {
      .order-meta { flex-direction:column; align-items:flex-start; gap:6px; }
      table.order-items th:nth-child(1), table.order-items td:nth-child(1) { display:none; }
    }
  </style>
</head>
<body>
<%@include file="all_component/navbar.jsp"%>
  <div class="container">
    <div class="orders-header">Your Orders</div>

    <%
      if (orders == null || orders.isEmpty()) {
    %>
      <div class="order-card no-orders">
        You have not placed any orders yet. <a href="<%=request.getContextPath()%>/">Continue shopping</a>.
      </div>
    <%
      } else {
          for (Order o : orders) {
    %>
      <div class="order-card">
        <div class="order-meta">
          <div>
            <div class="order-id">Order: <%= o.getOrderId() %></div>
            <div class="order-date small-muted">Placed: <%= (o.getCreatedAt() != null ? o.getCreatedAt() : "") %></div>
            <div class="small-muted">Payment: <span class="pay-method"><%= (o.getPaymentMethod()!=null ? o.getPaymentMethod() : "N/A") %></span></div>
          </div>
          <div style="text-align:right;">
            <div class="small-muted">Status</div>
            <div style="font-weight:700; color:#222;"><%= (o.getStatus()!=null ? o.getStatus() : "") %></div>
          </div>
        </div>

        <table class="order-items">
          <thead>
            <tr>
              <th style="width:90px;"></th>
              <th>Product</th>
              <th style="width:110px;">Unit price</th>
              <th style="width:80px;">Qty</th>
              <th style="width:140px;" class="price-right">Subtotal</th>
            </tr>
          </thead>
          <tbody>
            <%
              List<Map<String, String>> items = o.getItems();
              if (items != null) {
                for (Map<String,String> it : items) {
                  String bookId = it.get("bookId");
                  String qtyStr = it.get("qty");
                  String priceStr = it.get("price");
                  int qty = 1;
                  double price = 0.0;
                  try { qty = Integer.parseInt(qtyStr); } catch (Exception ignored) {}
                  try { price = Double.parseDouble(priceStr); } catch (Exception ignored) {}

                  // fetch book details to show title & author (optional; may be null if book deleted)
                  BookDetails bd = null;
                  if (bookId != null) {
                    try { bd = bookDao.getBookById(bookId); } catch (Exception ignored) {}
                  }

                  String title = (bd != null && bd.getTitle() != null) ? bd.getTitle() : (it.get("title") != null ? it.get("title") : bookId);
                  String author = (bd != null && bd.getAuthor() != null) ? bd.getAuthor() : (it.get("author") != null ? it.get("author") : "");
                  String thumb = (bd != null && bd.getPhoto() != null) ? bd.getPhoto() : "assets/images/icon.png";

                  double subtotal = price * qty;
            %>
            <tr>
              <td>
                <img class="item-thumb" src="<%= request.getContextPath() %>/book/<%= thumb %>" onerror="this.src='<%=request.getContextPath()%>/assets/images/icon.png'"/>
              </td>
              <td>
                <div style="font-weight:600;"><%= title %></div>
                <div class="small-muted"><%= author %></div>
              </td>
              <td>₹ <%= String.format("%.2f", price) %></td>
              <td><%= qty %></td>
              <td class="price-right">₹ <%= String.format("%.2f", subtotal) %></td>
            </tr>
            <%
                } // end items loop
              } // end if items
            %>
          </tbody>
        </table>

        <div class="subtotal-line">
          <div style="width:320px; max-width:70%;">Order total:</div>
          <div style="min-width:140px; text-align:right;">₹ <%= String.format("%.2f", o.getTotal()) %></div>
        </div>
      </div>
    <%
          } // end orders loop
      } // end else
    %>
  </div>

  <%@ include file="all_component/footer.jsp" %>
</body>
</html>
