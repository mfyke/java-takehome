package com.company.ledgerapi.controller;

import com.company.ledgerapi.dao.TransactionRepository;
import com.company.ledgerapi.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@RestController
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepo;

    // GET endpoint that returns a specific transaction by Id
    @GetMapping(value = "/transactions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction getTransactionById(@PathVariable int id) {
        Transaction transaction = transactionRepo.getById(id);

        if(transaction != null) {
            return transaction;
        }
        throw new IllegalArgumentException();
    }

    // POST endpoint that creates a single transaction
    @PostMapping(value = "/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@RequestBody @Valid Transaction transaction) {
        transactionRepo.save(transaction);
        return transaction;
    }

    // PUT endpoint that updates the transaction_value of a specific transaction determined by transaction Id
    @PutMapping(value = "/transactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTransaction(@PathVariable("id") int id, @RequestBody Transaction transaction) {

        Transaction transactionFromRepo = transactionRepo.getById(id);

        if(transactionFromRepo != null) {
            transactionFromRepo.setTransaction_value(transaction.getTransaction_value());
            transactionRepo.save(transactionFromRepo);
            return;
        }
        throw new IllegalArgumentException();

    }

    // DELETE endpoint that “soft-deletes” a transaction
    @DeleteMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable("id") int id) {

        Transaction transaction = transactionRepo.getById(id);

        if(transaction != null) {
            transactionRepo.deleteById(id);
            return;
        }
        throw new IllegalArgumentException();

    }

    // GET endpoint that returns all transactions from the database that are not soft-deleted
    @GetMapping("/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }

    // GET endpoint that returns the sum of all transaction values for transactions that are not soft-deleted
    @GetMapping("/transactions/sum")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal calculateSum() {
        List <Transaction> transactionList = transactionRepo.findAll();
        BigDecimal sum = transactionList.stream().map(t -> t.getTransaction_value()).reduce(BigDecimal.ZERO,BigDecimal::add);

        return sum.setScale(2, RoundingMode.UNNECESSARY);
    }

}
