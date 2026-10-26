
package com.electricity.servlet;

import com.electricity.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT * FROM customer ORDER BY customer_id DESC")) {

            java.util.List<java.util.Map<String, Object>> list =
                new java.util.ArrayList<>();

            while (rs.next()) {
                java.util.Map<String, Object> c =
                    new java.util.HashMap<>();

                c.put("id", rs.getInt("customer_id"));
                c.put("name", rs.getString("name"));
                c.put("address", rs.getString("address"));
                c.put("meter", rs.getString("meter_no"));
                c.put("phone", rs.getString("phone"));

                list.add(c);
            }

            request.setAttribute("customers", list);

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        request.getRequestDispatcher("/customers.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try (Connection con = DBConnection.getConnection()) {

            if ("add".equals(action)) {
                String sql = "INSERT INTO customer " +
                    "(name, address, meter_no, phone) " +
                    "VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps =
                         con.prepareStatement(sql)) {

                    ps.setString(1, request.getParameter("name"));
                    ps.setString(2, request.getParameter("address"));
                    ps.setString(3, request.getParameter("meter"));
                    ps.setString(4, request.getParameter("phone"));

                    ps.executeUpdate();
                }

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(
                    request.getParameter("id"));

                String sql =
                    "DELETE FROM customer WHERE customer_id = ?";

                try (PreparedStatement ps =
                         con.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
            }

            response.sendRedirect(
                request.getContextPath() + "/customers");

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to save or delete customer. " +
                "Check the meter number and existing records.", e);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid customer ID.", e);
        }
    }
}
