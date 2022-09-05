package com.example.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface TransactionRepo extends JpaRepository<Transactions,Integer> {
	 @Query("SELECT t FROM Transactions t WHERE t.accountNumber = ?1")
	   List<Transactions> findByAccountNumber(String accountNumber);

}
