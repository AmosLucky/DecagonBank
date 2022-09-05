package com.example.security;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomersRepo extends JpaRepository<Customers,Integer> {
  // Customers findByEmailAndPassword(String email,String name, Pageable pageable);
	
	 @Query("SELECT t FROM Customers t WHERE t.email = ?1 AND t.password = ?2")
	   List<Customers> findByEmailAndPassword(String email, String password);
	 
	 @Query("select u from Customers u where u.email = ?1")
List<Customers> findByEmailAddress(String emil);


}