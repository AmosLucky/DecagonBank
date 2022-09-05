<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard</title>
<script src=https://code.jquery.com/jquery-3.6.0.min.js></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
    rel="stylesheet"
    integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
    crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
    integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
    crossorigin="anonymous"></script>
</head>
<body>

<div class="jumbotron text-center bg-light mb-3 p-5">
  <h3><small>Welcome:</small> ${ customer.getName() }</h3>
 <h1><small>Balance:</small> <span id="balance">${customer.getBalance() }</span></h1>
 <h6>Account number: <b>${customer.getAccount_number() }</b> </h6>
 <div>
 <a href="/logout" class="btn btn-primary">Logout</a>
 </div>
</div>
  
<div class="container">
  <div class="row justify-content-center">
    <div class="col-sm-4">
    <h5>Deposit</h5>
      <form mathod="post" action="/dashboard" id="depositform">
      <input type="number" required class="form-control" id="depo_amount"         placeholder="Enter Amount"/>
      <br>
      <input type="submit" class="form-control" name="submit"
      value="Deposit"/>
      </form>
     
    </div>
     <div class="col-sm-1"></div>
    <div class="col-sm-4">
     <h5>Withdraw</h5>
      <form mathod="post" action="/withdraw" id="withdrawform">
      <input type="number" required class="form-control" id="withdrawal_amount" placeholder="Enter Amount"/>
      <br>
      <input type="submit" class="form-control" name="submit"
      value="Deposit"/>
      </form>
     
    </div>
    
  </div>
</div>
<script>
$(document).ready(function(){
	$('#depositform').submit(
		    function(event) {
		    	event.preventDefault();
		    	 var amount_str = $('#depo_amount').val();
		    	// alert(amount_str);
		    	var amount = parseInt(amount_str);
		    	 if(amount > 0){
		    		// alert(amount);
		    		 $.ajax({
		    			
		    	            type : "POST",
		    	            dataType: "json",
		    	            url : '/deposit',
		    				data : JSON.stringify({"amount":amount}),
		    	            contentType: "application/json; charset=utf-8",
		    	            //data : {"amount":amount},
		    	            success : function(response) {
		    	            	const data = JSON.parse(JSON.stringify(response));
		    	            	console.log(data);
		    	            	
		    	            	
		    	            	if(data["status"] == "success"){
		    	            		
		    	            		$("#balance").html(data["balance"])
		    	            		
		    	            		alert("Successfully deposited "+amount );
		    	            		$('#depo_amount').val("");
		    	            		
		    	            		
		    	            	}else{
		    	            		alert("An error occored" );
		    	            	}
		    	                
		    	            },
		    	            error : function(err) {
		    	                alert("not working "+JSON.stringify(err));
		    	            }
		    	        });
		    		 
		    		 
		    	 }else{
		    		 alert("Amount must be greater than 0");
		    		 
		    	 } 
		    	
		    });
	
	////////////////????WITHDRAWAllll///////////////////
	$('#withdrawform').submit(
		    function(event) {
		    	event.preventDefault();
		    	 var amount_str = $('#withdrawal_amount').val();
		    	// alert(amount_str);
		    	var amount = parseInt(amount_str);
		    	 if(amount > 0){
		    		// alert(amount);
		    		 $.ajax({
		    			
		    	            type : "POST",
		    	            dataType: "json",
		    	            url : '/withdraw',
		    				data : JSON.stringify({"amount":amount}),
		    	            contentType: "application/json; charset=utf-8",
		    	            //data : {"amount":amount},
		    	            success : function(response) {
		    	            	const data = JSON.parse(JSON.stringify(response));
		    	            	console.log(data);
		    	            	
		    	            	
		    	            	if(data["status"] == "success"){
		    	            		
		    	            		$("#balance").html(data["balance"])
		    	            		
		    	            		alert("Successfully witdrawn "+amount );
		    	            		$('#withdrawal_amount').val("");
		    	            		
		    	            		
		    	            	}else if(data["status"] == "2"){
		    	            		alert(data["msg"] );
		    	            		
		    	            	}else{
		    	            		alert("An error occored" );
		    	            	}
		    	                
		    	            },
		    	            error : function(err) {
		    	                alert("not working "+JSON.stringify(err));
		    	            }
		    	        });
		    		 
		    		 
		    	 }else{
		    		 alert("Amount must be greater than 0");
		    		 
		    	 } 
		    	
		    });
	
	
	
	.ajax({
		
        type : "POST",
        dataType: "json",
        url : '/withdraw',
		data : JSON.stringify({"amount":amount}),
        contentType: "application/json; charset=utf-8",
        //data : {"amount":amount},
        success : function(response) {
        	const data = JSON.parse(JSON.stringify(response));
        	console.log(data);
        	
        	
        	if(data["status"] == "success"){
        		
        		$("#balance").html(data["balance"])
        		
        		alert("Successfully witdrawn "+amount );
        		$('#withdrawal_amount').val("");
        		
        		
        	}else if(data["status"] == "2"){
        		alert(data["msg"] );
        		
        	}else{
        		alert("An error occored" );
        	}
            
        },
        error : function(err) {
            alert("not working "+JSON.stringify(err));
        }
    });
 
});



</script>

</body>
</html>