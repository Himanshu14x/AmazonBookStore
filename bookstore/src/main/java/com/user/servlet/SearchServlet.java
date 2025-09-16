package com.user.servlet;

import com.DAO.BookDAOImpl;
import com.DB.DynamoDBClientProvider;
import com.entity.BookDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        if (q == null || q.trim().isEmpty()) {
            // optionally: redirect to all books or show empty results
            req.setAttribute("results", java.util.Collections.emptyList());
            req.setAttribute("query", "");
            req.getRequestDispatcher("/search_books.jsp").forward(req, resp);
            return;
        }

        try {
            BookDAOImpl dao = new BookDAOImpl(DynamoDBClientProvider.getClient());
            List<BookDetails> results = dao.searchBooks(q);
            req.setAttribute("results", results);
            req.setAttribute("query", q);
            req.getRequestDispatcher("/search_books.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("results", java.util.Collections.emptyList());
            req.setAttribute("query", q);
            req.getRequestDispatcher("/search_books.jsp").forward(req, resp);
        }
    }
}
