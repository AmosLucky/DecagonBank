package com.example.security;

import java.awt.print.Pageable;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.management.Query;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//import com.example.Customers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minidev.json.JSONObject;


@Controller
//@RestController
public class CustomerController {
	
	@Autowired
	 CustomersRepo repo;
	@Autowired
	TransactionRepo transactionRepo;
	
	
	@RequestMapping("/")
	public String home() {
		return "index";
		
	}
	
	@RequestMapping("/index")
	public String index() {
		return "index";
		
	}
	
	public String generateAccountNumber() {
		Random rand = new Random();
	    String number = "";
	    for (int i = 0; i < 10; i++)
	    {
	        int n = rand.nextInt(10) + 0;
	        number += Integer.toString(n);
	    }
	   
	    return number;
		
	}
	
	@PostMapping("/index")
	public ModelAndView register(HttpServletResponse response,Customers customer) {
		
		String status = "faild to register";
		String alert = "danger";
		ModelAndView mv = new ModelAndView();
		
		////////?CHECK IF EMAIL EXISTS///
		List <Customers> findByEmailAddress = repo.findByEmailAddress(customer.getEmail());
		if(findByEmailAddress.isEmpty()) {
		
		String account_number = generateAccountNumber();
		double baance = 0;
		 //customer = new Customers();
		customer.setAccount_number(account_number);
		customer.setBalance(baance);
		Customers savedCustomer = repo.save(customer);
		if(repo.findById(savedCustomer.getId()) != null) {
			 status = "success";
			 alert = "success";
			 
			 Cookie cookie = new Cookie("id", String.valueOf(customer.getId()));
				cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
				cookie.setSecure(true);
				cookie.setHttpOnly(true);
				cookie.setPath("/");
				 //add cookie to response
			    response.addCookie(cookie);
				
				//System.out.println(new Gson().toJson(customerToLogin));
				
				mv.setViewName("redirect:dashboard");
				return mv;
			
		}else {
			status = "Something went wrong";
			 alert = "danger";
			
		}
		
		}else {
			status = "A user with this email already exist";
			 alert = "danger";
			
		}
		

		
		mv.addObject("status",status);
		mv.addObject("type",alert);
		mv.setViewName("index");
		return mv;
		
		
		
		
	}
	
	@RequestMapping("/login")
	public String login() {
		
		return "login";
		
	}
	
	@PostMapping("/login")
	public ModelAndView loginRequest(HttpServletResponse response,Customers customer) {
		
		ModelAndView mv = new ModelAndView();
		List<Customers> requestedUser = repo.findByEmailAndPassword(customer.getEmail(),customer.getPassword());
		//System.out.print(customer.toString());
		String status = "An error occoured";
		String alert = "danger";
		if(requestedUser.size() == 1) {
			////To do login///
			
			 status = "Success";
			 alert = "success";
			Customers customerToLogin = requestedUser.get(0);
			//System.out.print(c.toString());
			String jsonUser = new Gson().toJson(customerToLogin);
			Cookie cookie = new Cookie("id", String.valueOf(customerToLogin.getId()));
			cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
			cookie.setSecure(true);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			 //add cookie to response
		    response.addCookie(cookie);
			
			System.out.println(new Gson().toJson(customerToLogin));
			
			mv.setViewName("redirect:dashboard");
			return mv;

			
			
			
		}else if(requestedUser.size()>1) {
			status = "Multiple user detected";
			
		
		}else if(requestedUser.isEmpty()) {
			status = "Username or password not correct";
			
		}
		
		
		mv.addObject("status",status);
		mv.addObject("type",alert);
		mv.setViewName("login");
		return mv;
		
		
	} 
	
	@GetMapping("/dashboard")
	public ModelAndView dashboard(HttpServletRequest request) {
	
		
		ModelAndView mv = new ModelAndView();
		
		Cookie[] cookies = request.getCookies();
		String id =Arrays.stream(request.getCookies())
        .filter(c -> c.getName().equals("id"))
        .findFirst()
        .map(Cookie::getValue)
        .orElse(null);
		Customers customer  = repo.findById(Integer.parseInt(id)).orElse(null);
		if(id != null && customer != null) {
			mv.addObject("customer",customer);
		


			mv.setViewName("dashboard");
			return mv;
		}
			mv.setViewName("redirect:login");
			return mv;
		
		
//		mv.addObject("status",status);
//		mv.addObject("type",alert);
//		mv.setViewName("dashboard");
//		return mv;
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("id", null); // Not necessary, but saves bandwidth.
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
		response.addCookie(cookie);
		return "login";
	}
	
	
	@CrossOrigin(origins = "http://localhost:2023")
	//@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@PostMapping("/deposit")
	public String deposit(@RequestBody String j, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		//JSONObject jsonObject= requestParamsToJSON(request);
		
//		System.out.print(jsonObject);
		//JSONObject json3 = new JSONObject(request.getParameterMap());
//		System.out.print(json3);
		//String amount_str = (String) json3.get("amount");
		 JsonObject data = new Gson().fromJson(j, JsonObject.class);
		 String amount_str = data.get("amount").getAsString();
		//double amount = Double.parseDouble(amount_str);
		System.out.print(j);
		System.out.print(amount_str);
		double amount =  Double.parseDouble(amount_str);
		JsonObject res = new JsonObject();
		
		
		
		
		String id =Arrays.stream(request.getCookies())
		        .filter(c -> c.getName().equals("id"))
		        .findFirst()
		        .map(Cookie::getValue)
		        .orElse(null);
				Customers customer  = repo.findById(Integer.parseInt(id)).orElse(null);
				double new_balance = amount+customer.getBalance();
				if(id != null && customer != null) {
					customer.setBalance(new_balance);
					repo.save(customer);
					//System.out.print(customer.toString());
					
					String balance_res = String.valueOf(customer.getBalance());
					
					res.addProperty("status", "success");
					res.addProperty("balance", balance_res);
					
					Transactions tr =  new Transactions();
					tr.setAccountNumber(customer.getAccount_number());
					tr.setName(customer.getName());
					tr.setAmount(amount_str);
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					   LocalDateTime now = LocalDateTime.now();
					//String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
					tr.setCreatedAt(dtf.format(now)) ;

					System.out.print(tr.toString());
					transactionRepo.save(tr);
					
					
					return res.toString();
					
				}
				res.addProperty("status", "failed");
				
		return res.toString();
		
	}
	
	
	@CrossOrigin(origins = "http://localhost:2023")
	//@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@PostMapping("/withdraw")
	public String withdraw(@RequestBody String j, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		
		 JsonObject data = new Gson().fromJson(j, JsonObject.class);
		 String amount_str = data.get("amount").getAsString();
		//double amount = Double.parseDouble(amount_str);
		System.out.print(j);
		System.out.print(amount_str);
		double amount =  Double.parseDouble(amount_str);
		JsonObject res = new JsonObject();
		
		
		
		
		String id =Arrays.stream(request.getCookies())
		        .filter(c -> c.getName().equals("id"))
		        .findFirst()
		        .map(Cookie::getValue)
		        .orElse(null);
				Customers customer  = repo.findById(Integer.parseInt(id)).orElse(null);
				
				if(id != null && customer != null) {
					if(amount > customer.getBalance()) {
						res.addProperty("status", "2");
						res.addProperty("msg", "insufficient balance");
						
						return res.toString();
						
					}
					
					double new_balance = customer.getBalance() - amount;
					customer.setBalance(new_balance);
					repo.save(customer);
					//System.out.print(customer.toString());
					
					
					String balance_res = String.valueOf(customer.getBalance());
					
					res.addProperty("status", "success");
					res.addProperty("balance", balance_res);
					
					Transactions tr =  new Transactions();
					tr.setAccountNumber(customer.getAccount_number());
					tr.setName(customer.getName());
					tr.setAmount(amount_str);
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					   LocalDateTime now = LocalDateTime.now();
					//String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
					tr.setCreatedAt(dtf.format(now)) ;
					System.out.print(tr.toString());
					transactionRepo.save(tr);
					
					
					
					
					
					return res.toString();
					
				}
				res.addProperty("status", "failed");
				
		return res.toString();
		
	}
	
	@CrossOrigin(origins = "http://localhost:2023")
	//@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@PostMapping("/fetchTransantions")
	public String fetchTransantions(@RequestBody String j, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		 JsonObject data = new Gson().fromJson(j, JsonObject.class);
		 String account_number = data.get("account_number").getAsString();
		
		 JsonObject res = new JsonObject();
		 
		 
		 List<Transactions> listTransactions =  transactionRepo.findByAccountNumber(account_number);
		 res.addProperty("customers",new Gson().toJson(listTransactions));
		 
		
		return res.toString();
		
	}
	
	
	

}
