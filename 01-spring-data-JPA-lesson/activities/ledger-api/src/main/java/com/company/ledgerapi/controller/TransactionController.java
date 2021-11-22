package com.company.ledgerapi.controller;

import com.company.ledgerapi.dao.TransactionRepository;
import com.company.ledgerapi.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepo;

    // GET endpoint that returns a specific transaction by Id
    @GetMapping(value = "/transactions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction getTransactionById(@PathVariable int id) {
        Optional<Transaction> transaction = Optional.of(transactionRepo.getById(id));

        if(!transaction.isPresent()) {
            throw new IllegalArgumentException("Transaction ID does not match any transactions");
        }

        return transaction.get();
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
    public void updateTransaction(@PathVariable("id") int id, @RequestBody double transaction_value) {

        Optional<Transaction> transaction = Optional.of(transactionRepo.getById(id));

        if(!transaction.isPresent()) {
            throw new IllegalArgumentException("Transaction ID does not match any transactions");
        }

        transactionRepo.updateTransactionValue(id, transaction_value);
    }

    // DELETE endpoint that “soft-deletes” a transaction
    @DeleteMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable("id") int id) {
        Optional<Transaction> transaction = Optional.of(transactionRepo.getById(id));

        if(!transaction.isPresent()) {
            throw new IllegalArgumentException("Transaction ID does not match any transactions");
        }

        transactionRepo.deleteById(id);
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
    public double calculateSum() {
        List <Transaction> transactionList = transactionRepo.findAll();
        double sum = transactionList.stream().mapToDouble(t -> t.getTransaction_value()).sum();
        return sum;
    }

}
