<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>DummyPay</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>

<body>

<div class="container" style="margin-top:1rem;">
    <h1 style="margin-bottom:0;">DummyPay</h1>

    <p class="text-muted">
        A <a href="https://hichris.com">Chris Smith</a> project.
    </p>

    <p>DummyPay is a tool that makes it easy to test your checkout system.</p>

    <h3>How does it work?</h3>

    <p>Send a pay request to the server, then query the card's balance:<br>
        <code>
            $ curl -X POST https://dummypay.io/pay -H "Content-Type: application/json" --data '{<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"type": "card",<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardNumber": <card></card>,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardCvv": 1234,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardExpMonth": 11,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardExpYear": 45,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"amount": 10 }'<br>
            $ curl https://dummypay.io/card/<card></card><br>
            {"balance":10.00}
        </code>
    </p>

    <p>
        You can also do the same for banks:<br>
        <code>
            $ curl -X POST https://dummypay.io/pay -H "Content-Type: application/json" --data '{<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"type": "bank",<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"bankRouting": <bankR></bankR>,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"bankAccount": <bankA></bankA>,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"amount": 10 }'<br>
            $ curl https://dummypay.io/bank/<bankR></bankR>/<bankA></bankA><br>
            {"balance":10.00}
        </code>
    </p>

    <h3>Overriding details</h3>

    <p>
        Normally, you may pass any CVV or expiration date (provided the date is in the future) and the payment will go through. But if you'd like to test scenarios such as invalid CVV code, incorrect expiration date, or the card not existing at all, you'll have to override those details beforehand. You can also overwrite the balance as well:<br>
        <code>
            $ curl -X PUT https://dummypay.io/card/<card></card> -H "Content-Type: application/json" --data '{<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"exists": "false",<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cvv": 1234,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"expMonth": 11,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"expYear": 45,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"balance": 60 }'<br>
            $ curl -X POST https://dummypay.io/pay -H "Content-Type: application/json" --data '{<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"type": "card",<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardNumber": <card></card>,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardCvv": 1234,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardExpMonth": 11,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardExpYear": 45,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"amount": 10 }'<br>
            {"status":470, "message":"invalidCard"}
        </code>
    </p>
    
    <h3>Storing information (e.g. saved payment method)</h3>
    
    <p>
        <code>
            $ curl -X POST https://dummypay.io/save -H "Content-Type: application/json" --data '{<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"type": "card",<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardNumber": <card></card>,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardCvv": 1234,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardExpMonth": 11,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardExpYear": 45 }'<br>
            {"savedId":"53ce3555-d530-4843-8f04-80c138fe3f5b"}<br>
            $ curl -X POST https://dummypay.io/pay -H "Content-Type: application/json" --data '{<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"type": "saved",<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"savedId": "53ce3555-d530-4843-8f04-80c138fe3f5b",<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"amount": 10 }'<br>
            $ curl https://dummypay.io/card/<card></card><br>
            {"balance":10.00}
        </code>
    </p>

    <h3>Namespacing</h3>

    <p>
        If you feel that the number of available card numbers isn't enough, or you'd like to test a certain card number in particular you fear might cause collisions with other users, you have two options. The first option is to run your own DummyPay server, the second option is to provide an <code>Authorization</code> header:<br>
        <code>
            $ curl -X POST -u root:<pass></pass> https://dummypay.io/pay -H "Content-Type: application/json" --data '{<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"type": "card",<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardNumber": <card></card>,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardCvv": 1234,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardExpMonth": 11,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"cardExpYear": 45,<br>
            &nbsp;&nbsp;&nbsp;&nbsp;"amount": 10 }'<br>
            $ curl -u root:<pass></pass> https://dummypay.io/card/<card></card><br>
            {"balance":10.00}
        </code>
    </p>

    <h3>Isn't this insecure?</h3>

    <p>
        Yes. <b>Do not</b> send real card numbers (or bank numbers) to this server. Everything on here is stored in plain text. Instead, generate a random credit card number on the fly.
    </p>

    <h3>Technicalities</h3>

    <ul>
        <li>All cards, bank routing/accounts, and saved payment methods will be deleted after 1 hour after being created.</li>
        <li>For more information about the API, please checkout the source code over on <a href="https://github.com/chris13524/dummypay">GitHub</a>.</li>
    </ul>
</div>

<script>
    function genPass() {
        var array = [
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
        var result = "";
        for (var i = 0; i < 8; i++) {
            result += array[Math.floor(Math.random() * array.length)];
        }
        return result;
    }

    function genCard() {
        var array = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
        var result = "";
        for (var i = 0; i < 16; i++) {
            result += array[Math.floor(Math.random() * array.length)];
        }
        return result;
    }

    function genBank() {
        var array = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
        var result = "";
        for (var i = 0; i < 10; i++) {
            result += array[Math.floor(Math.random() * array.length)];
        }
        return result;
    }

    var pass = genPass();
    var passElements = document.getElementsByTagName("pass");
    for (var i = 0; i < passElements.length; i++) {
        passElements[i].innerHTML = pass;
    }

    var card = genCard();
    var cardElements = document.getElementsByTagName("card");
    for (var j = 0; j < cardElements.length; j++) {
        cardElements[j].innerHTML = card;
    }

    var bankR = genBank();
    var bankRElements = document.getElementsByTagName("bankR");
    for (var l = 0; l < bankRElements.length; l++) {
        bankRElements[l].innerHTML = bankR;
    }

    var bankA = genBank();
    var bankAElements = document.getElementsByTagName("bankA");
    for (var m = 0; m < bankAElements.length; m++) {
        bankAElements[m].innerHTML = bankA;
    }
</script>

</body>
</html>