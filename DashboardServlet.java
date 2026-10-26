
package com.electricity.servlet;

import com.electricity.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int customers = 0;
        int records = 0;
        double units = 0;
        double amount = 0;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(
                "SELECT COUNT(*) FROM customer"
            );
            if (rs.next()) customers = rs.getInt(1);
            rs.close();

            rs = st.executeQuery(
                "SELECT COUNT(*), COALESCE(SUM(units),0) " +
                "FROM electricity_record"
            );
            if (rs.next()) {
                records = rs.getInt(1);
                units = rs.getDouble(2);
            }
            rs.close();

            rs = st.executeQuery(
                "SELECT COALESCE(SUM(amount),0) FROM bill"
            );
            if (rs.next()) amount = rs.getDouble(1);
            rs.close();

        } catch (Exception e) {
            throw new ServletException(e);
        }

        request.setAttribute("customers", customers);
        request.setAttribute("records", records);
        request.setAttribute("units", units);
        request.setAttribute("amount", amount);

        request.getRequestDispatcher("/dashboard.jsp")
               .forward(request, response);
    }
}
