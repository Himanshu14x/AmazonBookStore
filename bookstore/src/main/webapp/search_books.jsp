<%@ page import="java.util.*" %>
<%@ page import="com.entity.BookDetails" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Search results - Bookstore</title>
  <link rel="icon" type="image/png" href="<%=request.getContextPath()%>/assets/images/icon.png" />
  <%@ include file="all_component/allCss.jsp" %>
  <style>
    /* match your All Books table styling */
    .results-heading {
      text-align: center;
      margin: 24px 0;
      font-size: 28px;
      font-weight: 600;
    }
    .table-header-blue {
      background: #0d6efd; /* bootstrap primary / close to screenshot */
      color: #fff;
    }
    .book-thumb {
      width: 56px;
      height: 80px;
      object-fit: cover;
      border-radius: 4px;
      box-shadow: 0 1px 3px rgba(0,0,0,0.06);
    }
    .actions-col .btn { margin-left: 6px; }
    /* you can adjust these paddings if you want truly edge-to-edge */
    .full-width-no-gutter { padding-left: 0; padding-right: 0; }
  </style>
</head>
<body>
  <%@ include file="all_component/navbar.jsp" %>

  <div class="container-fluid mt-3 mb-5 px-0">
    <div class="row no-gutters">
      <div class="col-12 px-0">
        <h2 class="results-heading">Search results for: "<%= request.getAttribute("query") != null ? request.getAttribute("query") : "" %>"</h2>
      </div>
    </div>

    <div class="row no-gutters">
      <div class="col-12 px-0">
        <div class="table-responsive px-0">
          <table class="table table-borderless table-hover w-100">
            <thead class="table-header-blue">
              <tr>
                <th style="width:6%;">ID</th>
                <th style="width:8%;">Image</th>
                <th style="width:34%;">Title</th>
                <th style="width:18%;">Author</th>
                <th style="width:10%;">Genre</th>
                <th style="width:6%;">Rating</th>
                <th style="width:8%;">Price</th>
                <th style="width:10%;">Actions</th>
              </tr>
            </thead>
            <tbody>
              <%
                List<BookDetails> results = (List<BookDetails>) request.getAttribute("results");
                if (results == null) results = new ArrayList<>();
                if (results.isEmpty()) {
              %>
                <tr>
                  <td colspan="8" class="text-center py-4">No results found.</td>
                </tr>
              <%
                } else {
                  for (BookDetails b : results) {
                    String id = b.getId() != null ? b.getId() : "";
                    String title = b.getTitle() != null ? b.getTitle() : "";
                    String author = b.getAuthor() != null ? b.getAuthor() : "";
                    String genre = b.getGenre() != null ? b.getGenre() : "";
                    String ratingVal = b.getRating();
                    String priceVal = b.getPrice();
                    String photo = b.getPhoto() != null ? b.getPhoto() : "icon.png";
              %>
                <tr>
                  <td class="align-middle"><%= id %></td>
                  <td class="align-middle">
                    <img src="<%= request.getContextPath() %>/book/<%= photo %>" alt="cover" class="book-thumb"
                         onerror="this.src='<%= request.getContextPath() %>/assets/images/icon.png'"/>
                  </td>
                  <td class="align-middle"><%= title %></td>
                  <td class="align-middle"><%= author %></td>
                  <td class="align-middle"><%= genre %></td>
                  <td class="align-middle"><%= ratingVal != null ? ratingVal : "0.0" %></td>
                  <td class="align-middle">₹<%= priceVal != null ? priceVal : "0.0" %></td>
                  <td class="align-middle actions-col">
                    <form action="<%= request.getContextPath() %>/cart/add" method="post" style="display:inline-block;">
                      <input type="hidden" name="bookId" value="<%= id %>" />
                      <input type="hidden" name="title" value="<%= title %>" />
                      <input type="hidden" name="price" value="<%= priceVal != null ? priceVal : 0.0 %>" />
                      <input type="hidden" name="photo" value="<%= photo %>" />
                      <input type="hidden" name="qty" value="1" />
                      <button type="submit" class="btn btn-success btn-sm"><i class="fas fa-cart-plus"></i> Add</button>
                    </form>

                    <a href="<%= request.getContextPath() %>/view_books.jsp?bid=<%= id %>" class="btn btn-outline-secondary btn-sm">View</a>
                  </td>
                </tr>
              <%
                  } // end for
                } // end else
              %>
            </tbody>
          </table>
        </div> <!-- /.table-responsive -->
      </div> <!-- /.col-12 -->
    </div> <!-- /.row -->
  </div> <!-- /.container-fluid -->

  <%@ include file="all_component/footer.jsp" %>

  <script>
    // disable submit buttons to prevent double-clicks (same UX as other pages)
    (function(){
      document.querySelectorAll('form').forEach(function(f){
        f.addEventListener('submit', function(e){
          var btn = this.querySelector('button[type="submit"]');
          if (btn) btn.disabled = true;
        });
      });
    })();
  </script>
</body>
</html>
