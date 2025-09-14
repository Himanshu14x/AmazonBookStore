<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="all_component/allCss.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/assets/images/icon.png" />
    <meta charset="UTF-8">
    <title>Register</title>

    <style>
        body {
            background-color: #f8f9fa;
        }
        .register-container {
            max-width: 400px;
            margin: 50px auto;
            padding: 30px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0px 6px 18px rgba(0,0,0,0.1);
        }
        .register-container h2 {
            font-weight: bold;
            margin-bottom: 10px;
        }
        .register-container p {
            color: #6c757d;
            margin-bottom: 20px;
        }
        .btn-register {
            background-color: #ff9100;
            border: none;
            color: white;
            font-weight: bold;
            padding: 10px;
            border-radius: 8px;
            width: 100%;
        }
        .btn-register:hover {
            background-color: #e57f00; 
        }
        .form-control {
            border-radius: 8px;
        }
    </style>
</head>
<body>
    <%@ include file="all_component/navbar.jsp" %>

    <div class="register-container">
        <h2>Create your account</h2>
        <p>Join Amazon Books to discover great books</p>

        <%
            // Use implicit session object. Read messages and then remove them so they appear only once.
            String success = (String) session.getAttribute("successMessage");
            String error = (String) session.getAttribute("errorMessage");

            if (success != null) {
                session.removeAttribute("successMessage");
            }
            if (error != null) {
                session.removeAttribute("errorMessage");
            }
        %>

        <% if (success != null) { %>
            <div class="alert alert-success" role="alert">
                <%= success %>
            </div>
        <% } %>

        <% if (error != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= error %>
            </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/register" method="post">
            <div class="form-group">
                <label for="name">Name</label>
                <input type="text" class="form-control" id="name" name="name" placeholder="Tony Stark" required>
            </div>
            <div class="form-group mt-3">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="IronMan@example.com" required>
            </div>
            <div class="form-group mt-3">
                <label for="password">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            
            
            <button type="submit" class="btn-register mt-4">Create account</button>
        </form>

        <p class="mt-3 text-center">
            Already have an account? <a href="login.jsp">Log in</a>
        </p>
    </div>

    <br/>
    <%@ include file="all_component/footer.jsp" %>
</body>
</html>
