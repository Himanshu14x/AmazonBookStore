package com.admin.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import com.DAO.BookDAOImpl;
import com.DB.DynamoDBClientProvider;
import com.entity.BookDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@WebServlet("/add_books")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
                 maxFileSize = 5 * 1024 * 1024,    // 5MB
                 maxRequestSize = 10 * 1024 * 1024) // 10MB
public class BooksAdd extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads"; // inside webapp or absolute path

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ensure correct encoding
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            String id = req.getParameter("bid");
            String title = req.getParameter("bname");
            String author = req.getParameter("author");
            String ratingStr = req.getParameter("rating");
            String genre = req.getParameter("genre");
            String priceStr = req.getParameter("price");

            // basic validation
            if (id == null || id.isBlank() || title == null || title.isBlank()) {
                req.setAttribute("error", "Book id and title are required");
                req.getRequestDispatcher("/admin/add_book.jsp").forward(req, resp);
                return;
            }

            double rating = 0.0;
            double price = 0.0;
            try {
                if (ratingStr != null && !ratingStr.isBlank()) rating = Double.parseDouble(ratingStr);
                if (priceStr != null && !priceStr.isBlank()) price = Double.parseDouble(priceStr);
            } catch (NumberFormatException nfe) {
                req.setAttribute("error", "Invalid numeric value for rating or price");
                req.getRequestDispatcher("/admin/add_book.jsp").forward(req, resp);
                return;
            }

            Part part = req.getPart("bimg");
            String storedFileName = part.getSubmittedFileName();
            
           
               
                String uploadsPath = getServletContext().getRealPath("")+"book";
               
                System.out.println(uploadsPath);
                
                File uploadsDir = new File(uploadsPath);
                part.write(uploadsPath+File.separator+storedFileName);
               
            
            BookDetails b = new BookDetails(id, title, author, genre, String.valueOf(rating), String.valueOf(price), storedFileName, "admin");

            // Save to DynamoDB via DAO
            DynamoDbClient client = DynamoDBClientProvider.getClient();
            BookDAOImpl daoImpl = new BookDAOImpl(client);

            boolean saved = daoImpl.addBooks(b); // <-- replace with your actual method signature returning boolean/void
            if (saved) {
                // redirect to list or success page
            	 req.getSession().setAttribute("SuccessMsg", "Book Added Successfully!");
                resp.sendRedirect(req.getContextPath() + "/admin/add_books.jsp?Book has been added!");
               
            } else {
                req.getSession().setAttribute("error", "Failed to save book");
                req.getRequestDispatcher("/admin/add_books.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            // log properly (use a logger); show friendly message
            e.printStackTrace();
            req.getSession().setAttribute("error", "Server error: " + e.getMessage());
            req.getRequestDispatcher("/admin/add_books.jsp").forward(req, resp);
        }
    }
}
