/**
 * Created by Kir Kolesnikov on 10.02.2015.
 */
function validateLoginForm() {
    var fieldLogin = $("#login").val();
    var fieldPassword = $("#password").val();

    if (fieldLogin.length < 2) {
        alert("Login cannot be empty or less then 2 chars! ");
        return false;
    }
    if (fieldPassword.length < 2) {
        alert("Password cannot be empty or less then 2 chars!");
        return false;
    }
    return true;
}
function validateFunds() {
    var operation = $(".operation").val();
    var numbers = /^[0-9]+([,.][0-9]{1,2})?$/;
    if (operation.match(numbers)) {
        if (operation > 0) {
            return true;
        } else {
            alert("Amount can't be negative");
            return false;
        }
    }
    alert("Amount must be a number");
    return false;
}

function validateNewClient() {
    $(".error").html("");

    var isName = /^[A-Za-z]+( [A-Za-z]+)*$/;
    var isEmail = /^[A-Za-z\.-0-9]{2,}@[A-Za-z\.-0-9]{2,}\.[A-Za-z]{2,3}$/;
    var isPhone = /[+0-9]{4,15}/;
    var isFunds = /^[0-9]{1,7}([,.][0-9]{1,2})?$/;

    var name = $("#fullname").val();
    var city = $("#city").val();
    var phone = $("#phone").val();
    var email = $("#email").val();
    var balance = $("#startBalance").val();

    if (!name.match(isName)) {
        $("#wrongName").html("Wrong name or surname");
        return false;
    }
    if (!city.match(isName)) {
        $("#wrongCity").html("Wrong city");
        return false;
    }
    if (!phone.match(isPhone)) {
        $("#wrongPhone").html("Wrong phone number");
        return false;
    }
    if (!email.match(isEmail)) {
        $("#wrongEmail").html("Wrong email");
        return false;
    }
    if (!balance.match(isFunds)) {
        $("#wrongBalance").html("Wrong start balance");
        return false;
    }
    return true;

    function validateSearchClients() {
        $(".error").html("");
        var isName = /^[A-Za-z]+( [A-Za-z]+)*$/;

        if (!name.match(isName)) {
            $("#name").html("Wrong name or surname");
            return false;
        }
        if (!city.match(isName)) {
            $("#city").html("Wrong city");
            return false;
        }
        return true;
    }
}
