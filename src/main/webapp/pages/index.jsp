<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome</title>
<script src=https://code.jquery.com/jquery-3.6.0.min.js></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
    rel="stylesheet"
    integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
    crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
    integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
    crossorigin="anonymous"></script>
</head>
<style>

</style>
<body>

<div class="container p-5">
<div class="row">
<div class="col-md-3"></div>
<div class="col-md-6">
<h1>Registration form</h1>
<div class="alert alert-${type} }">
${status}
</div>
<form action="index" method="post" class="p-5">
<input type="text" name="name" class="form-control" placeholder="Name">
<br>
<input type="email" name="email" class="form-control" placeholder="email">
<br>
<input type="password" name="password" class="form-control" placeholder="Password">
<br>
<br>
<input type="submit" name="submit" value="Register"  class="btn btn-primary mr-5">
<a href="/login" class="link-primary ml-5">Login</a>
<br>

</form>
</div>
<div class="col-md-3"></div>
</div>

</div>

</body>
</html>