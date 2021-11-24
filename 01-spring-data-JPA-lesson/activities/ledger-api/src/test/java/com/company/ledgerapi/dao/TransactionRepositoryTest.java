package com.company.ledgerapi.dao;

import com.company.ledgerapi.dto.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepo;

    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @Before
    public void setUp() {
        transactionRepo.deleteAll();

        transaction1 = new Transaction();

        transaction1.setRecipient("Bob");
        transaction1.setSender("bank");
        transaction1.setTransaction_value(BigDecimal.valueOf(-39.37));

        transaction2 = new Transaction();

        transaction2.setRecipient("bank");
        transaction2.setSender("Rick");
        transaction2.setTransaction_value(BigDecimal.valueOf(73.20));

        transaction3 = new Transaction();

        transaction3.setRecipient("bank");
        transaction3.setSender("Jimmy");
        transaction3.setTransaction_value(BigDecimal.valueOf(373.63));

    }

    @After
    public void tearDown() {
        transactionRepo.deleteAll();
    }

    @Test
    public void shouldAddAndGetTransactionFromDatabase() {
        transaction1 = transactionRepo.save(transaction1);

        Transaction fromRepo = transactionRepo.findById(transaction1.getId()).get();
        assertEquals(transaction1, fromRepo);
    }

    @Test
    public void shouldDeleteTransactionFromDatabase() {
        transaction2 = transactionRepo.save(transaction2);
        transactionRepo.deleteById(transaction2.getId());
        Optional<Transaction> fromRepo = transactionRepo.findById(transaction2.getId());
        assertFalse(fromRepo.isPresent());
    }

    @Test
    public void shouldFindTransactionById() {
        transaction3 = transactionRepo.save(transaction3);
        Optional<Transaction> fromRepo = transactionRepo.findById(transaction3.getId());
        assertTrue(fromRepo.isPresent());
    }


}