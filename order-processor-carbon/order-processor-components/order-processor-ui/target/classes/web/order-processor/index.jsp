<html>
<head>
    <style>
        /* Basic Grey */
        .basic-grey {
            width: 400px;
            margin-right: auto;
            margin-left: auto;
            background: #EEE;
            padding: 20px 30px 20px 30px;
            font: 12px Georgia, "Times New Roman", Times, serif;
            color: #888;
            text-shadow: 1px 1px 1px #FFF;
            border:1px solid #DADADA;
        }
        .basic-grey h1 {
            font: 25px Georgia, "Times New Roman", Times, serif;
            padding: 0px 0px 10px 40px;
            display: block;
            border-bottom: 1px solid #DADADA;
            margin: -10px -30px 30px -30px;
            color: #888;
        }
        .basic-grey h1>span {
            display: block;
            font-size: 11px;
        }
        .basic-grey label {
            display: block;
            margin: 0px 0px 5px;
        }
        .basic-grey label>span {
            float: left;
            width: 80px;
            text-align: right;
            padding-right: 10px;
            margin-top: 10px;
            color: #888;
        }
        .basic-grey input[type="text"], .basic-grey input[type="email"], .basic-grey textarea,.basic-grey select{
            border: 1px solid #DADADA;
            color: #888;
            height: 24px;
            margin-bottom: 16px;
            margin-right: 6px;
            margin-top: 2px;
            outline: 0 none;
            padding: 3px 3px 3px 5px;
            width: 70%;
            font: normal 12px/12px Georgia, "Times New Roman", Times, serif;
        }
        .basic-grey textarea{
            height:100px;
        }
        .basic-grey .button {
            background: #E48F8F;
            border: none;
            padding: 10px 25px 10px 25px;
            color: #FFF;
        }
        .basic-grey .button:hover {
            background: #CF7A7A
        }
    </style>
</head>
<body>
<div id="middle">
<form action="register_customer.jsp" method="post" class="basic-grey">
    <h1>Register as a Customer</h1>
    <span>Please fill all the fields.</span>
    <label>
        <span>Your Name :</span>
        <input id="name" type="text" name="name" placeholder="Your Full Name" />
    </label>

    <label>
        <span>Your Email :</span>
        <input id="email" type="email" name="email" placeholder="Valid Email Address" />
    </label>

    <label>
        <span>Phone :</span>
        <input id="phone" type="text" name="phone" placeholder="Valid Contact Number" />
    </label>

    <label>
        <span>Shipping Address :</span>
        <textarea id="address" name="address" placeholder="Your Shipping Address"></textarea>
    </label>

    <label>
        <span>City :</span>
        <input id="city" type="text" name="city" placeholder="Shipping City" />
    </label>

    <label>
        <span>Postal Code :</span>
        <input id="postalCode" type="text" name="postalCode" placeholder="Postal Code" />
    </label>

    <label>
        <span>&nbsp;</span>
        <input type="submit" class="button" value="Register" />
    </label>
</form>
</div>
</body>
</html>
