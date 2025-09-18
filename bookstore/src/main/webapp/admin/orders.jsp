<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="com.DB.DynamoDBClientProvider, com.DAO.OrderDAOImpl, com.entity.Order, software.amazon.awssdk.services.dynamodb.DynamoDbClient, java.util.*" %>

<jsp:include page="/admin/allCss.jsp" />
<jsp:include page="navbar.jsp" />

<%
    DynamoDbClient client = DynamoDBClientProvider.getClient();
    OrderDAOImpl od = new OrderDAOImpl(client);
    List<Order> orders = od.getAllOrders();
    if (orders == null) orders = new ArrayList<>();
%>

<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <title>Admin: Orders</title>
</head>
<body>
  <div class="table table-stripped">
    <h3 class="mb-3">Orders (total: <%= orders.size() %>)</h3>

    <table class="table table-striped">
      <thead class="bg-primary text-white">
        <tr>
          <th>Order ID</th>
          <th>User Email</th>
          <th>Titles</th>
          <th>Total (₹)</th>
          <th>Payment</th>
        </tr>
      </thead>
      <tbody>
      <%
        if (orders.isEmpty()) {
      %>
        <tr><td colspan="5" class="text-center">No orders found.</td></tr>
      <%
        } else {
            for (Order o : orders) {
                String oid = o.getOrderId() != null ? o.getOrderId() : "";
                String email = o.getEmail() != null ? o.getEmail() : "";
                String pm = o.getPaymentMethod() != null ? o.getPaymentMethod() : "";
                double total = o.getTotal();
                // build a comma-separated titles list from items
                String titles = "";
                List<Map<String,String>> items = o.getItems();
                if (items != null && !items.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (Map<String,String> it : items) {
                        String t = it.get("title");
                        if (t == null) t = it.get("bookId");
                        if (sb.length()>0) sb.append(", ");
                        sb.append(t);
                    }
                    titles = sb.toString();
                }
      %>
        <tr>
          <td><strong><%= oid %></strong></td>
          <td><%= email %></td>
          <td style="max-width:400px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;"><%= titles %></td>
          <td>₹ <%= String.format("%.2f", total) %></td>
          <td><%= pm %></td>
        </tr>
      <%
            } // end for
        } // end else
      %>
      </tbody>
    </table>
  </div>

  <jsp:include page="footer.jsp" />
</body>
</html>
