package com.company.ledgerapi.dao;

import com.company.ledgerapi.dto.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    //Custom update method for partial update
    @Modifying
    @Query("update Transaction t set t.transaction_value = :transaction_value where t.id = :id")
    void updateTransactionValue(@Param(value="id") int id, @Param(value="transaction_value") double transaction_value);
}
