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
    var numbers = /^[0-9]+$/;
    if (operation.match(numbers)) {
        if (operation > 0) {
            return true;
        } else{
            alert("Amount can't be negative");
            return false;
        }
    }
        alert("Amount must be a number");
        return false;
}
