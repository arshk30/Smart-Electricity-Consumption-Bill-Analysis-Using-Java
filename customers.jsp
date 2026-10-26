
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Management</title>
    <style>
        body { font-family: Arial; margin: 30px; background: #f4f7fb; }
        a, button { padding: 8px 12px; cursor: pointer; }
        table { width: 100%; background: white; border-collapse: collapse; }
        th, td { padding: 12px; border: 1px solid #ddd; text-align: left; }
        th { background: #176b87; color: white; }
    </style>
</head>
<body>
    <h1>Customer Management</h1>
    <a href="dashboard">Dashboard</a>
    <a href="add-customer.jsp">Add Customer</a>
    <a href="readings">Electricity Readings</a>
    <a href="bills">Bills</a>

    <br><br>
    <table>
        <tr>
            <th>ID</th><th>Name</th><th>Address</th>
            <th>Meter Number</th><th>Phone</th><th>Action</th>
        </tr>
        <%
        List<Map<String, Object>> list =
            (List<Map<String, Object>>) request.getAttribute("customers");

        if (list != null) {
            for (Map<String, Object> c : list) {
        %>
        <tr>
            <td><%= c.get("id") %></td>
            <td><%= c.get("name") %></td>
            <td><%= c.get("address") %></td>
            <td><%= c.get("meter") %></td>
            <td><%= c.get("phone") %></td>
            <td>
                <form action="customers" method="post"
                      onsubmit="return confirm('Delete this customer?')">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="<%= c.get("id") %>">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <%
            }
        }
        %>
    </table>
</body>
</html>
