
package com.electricity.servlet;

import com.electricity.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/bills")
public class BillServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, Object>> bills = new ArrayList<>();

        String sql =
            "SELECT b.bill_id, c.name, c.meter_no, r.month, " +
            "r.units, b.amount, b.status, b.bill_date " +
            "FROM bill b " +
            "JOIN electricity_record r ON b.record_id = r.record_id " +
            "JOIN customer c ON r.customer_id = c.customer_id " +
            "ORDER BY b.bill_id DESC";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> b = new HashMap<>();

                b.put("id", rs.getInt("bill_id"));
                b.put("name", rs.getString("name"));
                b.put("meter", rs.getString("meter_no"));
                b.put("month", rs.getString("month"));
                b.put("units", rs.getDouble("units"));
                b.put("amount", rs.getDouble("amount"));
                b.put("status", rs.getString("status"));
                b.put("date", rs.getTimestamp("bill_date"));

                bills.add(b);
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        request.setAttribute("bills", bills);

        request.getRequestDispatcher("/bills.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(
                request.getParameter("bill_id"));

            String sql =
                "UPDATE bill SET status = 'Paid' WHERE bill_id = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

            response.sendRedirect(
                request.getContextPath() + "/bills");

        } catch (NumberFormatException e) {
            response.sendError(400, "Invalid bill ID.");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
