function validForm(e){
let pass=document.getElementById('password').value;
let confirmPassword=document.getElementById('confirmPassword').value;
if(pass!=confirmPassword){
e.preventDefault();
alert("The password and the confirm password must contain the same value !");
}
document.getElementById("demo");
}
//th:onclick="hello(event)"
//<script type="text/javascript" th:src="@{/js/login_validation.js}"></script>