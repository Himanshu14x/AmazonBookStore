
    <%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/assets/images/icon.png" />
<meta charset="UTF-8">
<title>Login</title>
<%@include file="all_component/allCss.jsp" %>
</head>
<body>
<%@include file="all_component/navbar.jsp" %>

<style>
    /* page background */
    body {
      background-color: #f6f8fa;
    }

    /* centered card */
    .auth-card {
      max-width: 420px;
      margin: 60px auto;
      padding: 30px;
      background: #ffffff;
      border-radius: 10px;
      box-shadow: 0 8px 24px rgba(0,0,0,0.06);
    }

    .auth-card h2 {
      font-weight: 700;
      margin-bottom: 6px;
      font-size: 1.6rem;
    }
    .auth-card p.lead {
      color: #6c757d;
      margin-bottom: 20px;
    }

    .form-control {
      border-radius: 8px;
      height: 44px;
      padding: 0.5rem 12px;
    }

    /* themed login button */
    .btn-login {
      background-color: #ff9100;   /* your orange theme */
      border: 2px solid #ff9100;
      color: #fff;
      font-weight: 600;
      height: 44px;
      width: 100%;
      border-radius: 8px;
    }
    .btn-login:hover {
      background-color: #e07e00;
      border-color: #e07e00;
    }

    /* small helper text and links */
    .auth-footer {
      margin-top: 14px;
      text-align: center;
      color: #6c757d;
      font-size: 0.95rem;
    }

    /* small responsiveness */
    @media (max-width: 576px) {
      .auth-card { margin: 30px 12px; padding: 22px; }
    }
  </style>
</head>
<body>



  <!-- login card -->
  <div class="auth-card">
    <h2>Log in</h2>
    <p class="lead">Welcome back to Amazon Books</p>

    <!-- show server-side message if present -->
<c:if test="${not empty sessionScope.successMsg1}">
    <h5 class="text-center text-success">${sessionScope.successMsg1}</h5>
    <c:remove var="successMsg1" scope="session" />
</c:if>


    <c:if test="${not empty successMsg1 }">
    <h5 class="text-center text-success">${successMsg1}</h5>
    <c:remove var="failedMsg" scope="session" />
</c:if>
    
    
    

    <form action="login" method="post">
      <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input id="email" name="email" type="email" class="form-control" placeholder="IronMan@example.com" required name="email">
      </div>

      <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input id="password" name="password" type="password" class="form-control" required name="password">
      </div>

      <div class="d-grid">
        <button type="submit" class="btn btn-login">Log in</button>
      </div>
    </form>

    <div class="auth-footer">
      <p>Don't have an account? <a href="<%= request.getContextPath() %>/register.jsp">Sign up</a></p>
      
    </div>
  </div>

<br></br>

<br></br>
<br></br>

<%@include file="all_component/footer.jsp" %>
</body>

</html>