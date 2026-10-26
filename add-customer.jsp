
<!DOCTYPE html>
<html>
<head>
    <title>Add Customer</title>
</head>
<body>
    <h1>Register Electricity Customer</h1>

    <form action="customers" method="post">
        <input type="hidden" name="action" value="add">

        <p>Customer Name:
            <input type="text" name="name" required>
        </p>

        <p>Address:
            <input type="text" name="address">
        </p>

        <p>Meter Number:
            <input type="text" name="meter" required>
        </p>

        <p>Phone:
            <input type="text" name="phone">
        </p>

        <button type="submit">Save Customer</button>
    </form>

    <p><a href="customers">Back to Customers</a></p>
</body>
</html>
