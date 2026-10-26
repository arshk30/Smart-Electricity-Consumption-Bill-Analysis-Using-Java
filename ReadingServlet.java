
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

@WebServlet("/readings")
public class ReadingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            List<Map<String, Object>> customers = new ArrayList<>();

            ResultSet rs = st.executeQuery(
                "SELECT customer_id, name, meter_no FROM customer ORDER BY name"
            );

            while (rs.next()) {
                Map<String, Object> c = new HashMap<>();
                c.put("id", rs.getInt("customer_id"));
                c.put("name", rs.getString("name"));
                c.put("meter", rs.getString("meter_no"));
                customers.add(c);
            }
            rs.close();

            List<Map<String, Object>> records = new ArrayList<>();

            rs = st.executeQuery(
                "SELECT r.record_id, c.name, r.month, r.units, b.amount " +
                "FROM electricity_record r " +
                "JOIN customer c ON r.customer_id = c.customer_id " +
                "JOIN bill b ON r.record_id = b.record_id " +
                "ORDER BY r.record_id DESC"
            );

            while (rs.next()) {
                Map<String, Object> r = new HashMap<>();
                r.put("id", rs.getInt("record_id"));
                r.put("name", rs.getString("name"));
                r.put("month", rs.getString("month"));
                r.put("units", rs.getDouble("units"));
                r.put("amount", rs.getDouble("amount"));
                records.add(r);
            }
            rs.close();

            request.setAttribute("customers", customers);
            request.setAttribute("records", records);

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if ("add".equals(request.getParameter("page"))) {
            request.getRequestDispatcher("/add-reading.jsp")
                   .forward(request, response);
        } else {
            request.getRequestDispatcher("/readings.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int customerId = Integer.parseInt(
                request.getParameter("customer_id"));

            String month = request.getParameter("month");

            double previous = Double.parseDouble(
                request.getParameter("previous"));

            double current = Double.parseDouble(
                request.getParameter("current"));

            if (month == null || month.trim().isEmpty()
                    || previous < 0 || current < previous) {
                response.sendError(400, "Invalid month or meter readings.");
                return;
            }

            double units = current - previous;
            double amount = units * 7.0 + 50.0;

            try (Connection con = DBConnection.getConnection()) {
                con.setAutoCommit(false);

                try {
                    int recordId;

                    String sql = "INSERT INTO electricity_record " +
                        "(customer_id, month, previous_reading, " +
                        "current_reading, units) VALUES (?, ?, ?, ?, ?)";

                    try (PreparedStatement ps = con.prepareStatement(
                            sql, Statement.RETURN_GENERATED_KEYS)) {

                        ps.setInt(1, customerId);
                        ps.setString(2, month);
                        ps.setDouble(3, previous);
                        ps.setDouble(4, current);
                        ps.setDouble(5, units);
                        ps.executeUpdate();

                        try (ResultSet keys = ps.getGeneratedKeys()) {
                            if (!keys.next()) {
                                throw new SQLException("Record ID not generated.");
                            }
                            recordId = keys.getInt(1);
                        }
                    }

                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO bill (record_id, amount) VALUES (?, ?)")) {

                        ps.setInt(1, recordId);
                        ps.setDouble(2, amount);
                        ps.executeUpdate();
                    }

                    con.commit();

                } catch (SQLException e) {
                    con.rollback();
                    throw e;
                } finally {
                    con.setAutoCommit(true);
                }
            }

            response.sendRedirect(
                request.getContextPath() + "/readings");

        } catch (NumberFormatException e) {
            response.sendError(400, "Please enter valid numeric readings.");
        } catch (SQLException e) {
            throw new ServletException(
                "Unable to save reading or generate bill.", e);
        }
    }
}
