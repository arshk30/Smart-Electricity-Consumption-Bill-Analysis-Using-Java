
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Electricity Readings</title>
</head>
<body>
    <h1>Electricity Consumption</h1>

    <a href="dashboard">Dashboard</a> |
    <a href="customers">Customers</a> |
    <a href="readings?page=add">Add Reading</a> |
    <a href="bills">Bills</a>

    <h2>Monthly Reading Records</h2>

    <table border="1" cellpadding="10">
        <tr>
            <th>Record ID</th><th>Customer</th><th>Month</th>
            <th>Units</th><th>Bill Amount (₹)</th>
        </tr>
        <%
        List<Map<String, Object>> records =
            (List<Map<String, Object>>) request.getAttribute("records");

        if (records != null) {
            for (Map<String, Object> r : records) {
        %>
        <tr>
            <td><%= r.get("id") %></td>
            <td><%= r.get("name") %></td>
            <td><%= r.get("month") %></td>
            <td><%= r.get("units") %></td>
            <td><%= String.format("%.2f",
                    ((Number) r.get("amount")).doubleValue()) %></td>
        </tr>
        <%
            }
        }
        %>
    </table>
</body>
</html>
