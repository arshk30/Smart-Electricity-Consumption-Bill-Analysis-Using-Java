
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Electricity Bills</title>
</head>
<body>
    <h1>Electricity Bill Management</h1>

    <a href="dashboard">Dashboard</a> |
    <a href="customers">Customers</a> |
    <a href="readings">Readings</a>

    <br><br>

    <table border="1" cellpadding="8">
        <tr>
            <th>Bill ID</th><th>Customer</th><th>Meter</th>
            <th>Month</th><th>Units</th><th>Amount (₹)</th>
            <th>Status</th><th>Date</th><th>Action</th>
        </tr>

        <%
        List<Map<String, Object>> bills =
            (List<Map<String, Object>>) request.getAttribute("bills");

        if (bills != null) {
            for (Map<String, Object> b : bills) {
        %>
        <tr>
            <td><%= b.get("id") %></td>
            <td><%= b.get("name") %></td>
            <td><%= b.get("meter") %></td>
            <td><%= b.get("month") %></td>
            <td><%= b.get("units") %></td>
            <td><%= String.format("%.2f",
                    ((Number) b.get("amount")).doubleValue()) %></td>
            <td><%= b.get("status") %></td>
            <td><%= b.get("date") %></td>
            <td>
                <% if ("Unpaid".equals(b.get("status"))) { %>
                <form action="bills" method="post">
                    <input type="hidden" name="bill_id"
                           value="<%= b.get("id") %>">
                    <button type="submit">Mark Paid</button>
                </form>
                <% } else { %>
                    Completed
                <% } %>
            </td>
        </tr>
        <%
            }
        }
        %>
    </table>
</body>
</html>
