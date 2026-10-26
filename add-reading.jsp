
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Electricity Reading</title>
</head>
<body>
    <h1>Add Monthly Electricity Reading</h1>

    <form action="readings" method="post">

        <p>Select Customer:</p>
        <select name="customer_id" required>
            <%
            List<Map<String, Object>> customers =
                (List<Map<String, Object>>) request.getAttribute("customers");

            if (customers != null) {
                for (Map<String, Object> c : customers) {
            %>
            <option value="<%= c.get("id") %>">
                <%= c.get("name") %> - <%= c.get("meter") %>
            </option>
            <%
                }
            }
            %>
        </select>

        <p>Month:
            <input type="month" name="month" required>
        </p>

        <p>Previous Meter Reading:
            <input type="number" name="previous"
                   min="0" step="0.01" required>
        </p>

        <p>Current Meter Reading:
            <input type="number" name="current"
                   min="0" step="0.01" required>
        </p>

        <button type="submit">Calculate and Generate Bill</button>
    </form>

    <p><a href="readings">Back to Readings</a></p>
</body>
</html>
