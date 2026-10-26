
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Smart Electricity System</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            margin: 30px;
        }
        h1 { color: #176b87; }
        .cards {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
        }
        .card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            min-width: 180px;
            box-shadow: 0 2px 8px #ccc;
        }
        .value { font-size: 28px; font-weight: bold; }
        a { margin-right: 15px; }
    </style>
</head>
<body>

    <h1>Smart Electricity Consumption and Bill Analysis System</h1>
    <p>Monitor customers, electricity consumption and billing records.</p>

    <p>
        <a href="customers">Customer Management</a>
        <a href="readings">Electricity Readings</a>
        <a href="readings?page=add">Generate New Bill</a>
        <a href="bills">View Bills</a>
    </p>

    <hr>

    <div class="cards">
        <div class="card">
            <h3>Total Customers</h3>
            <div class="value">${customers}</div>
        </div>

        <div class="card">
            <h3>Reading Records</h3>
            <div class="value">${records}</div>
        </div>

        <div class="card">
            <h3>Total Units Consumed</h3>
            <div class="value">${units}</div>
        </div>

        <div class="card">
            <h3>Total Bills Generated</h3>
            <div class="value">₹${amount}</div>
        </div>
    </div>

</body>
</html>
