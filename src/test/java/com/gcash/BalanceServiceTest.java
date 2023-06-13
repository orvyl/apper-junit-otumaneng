package com.gcash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BalanceServiceTest {

    @Test
    void testGetBalance() throws AccountNotFoundException {
        AccountRepository accountRepository = new AccountRepository();
        BalanceService balanceService = new BalanceService(accountRepository);

        double initialBalance = 1000.0;
        String id = accountRepository.createAccount("Orvyl", initialBalance);

        Double balance = balanceService.getBalance(id);

        Assertions.assertEquals(initialBalance, balance);
    }

    @Test
    void testGetBalanceAccountNotFound() {
        AccountRepository accountRepository = new AccountRepository();
        BalanceService balanceService = new BalanceService(accountRepository);

        double initialBalance = 1000.0;
        accountRepository.createAccount("Orvyl", initialBalance);

        Assertions.assertThrows(AccountNotFoundException.class, () -> balanceService.getBalance("random id"));
    }

    @Test
    void testDebit() throws InsufficientBalanceException {
        AccountRepository accountRepository = new AccountRepository();
        BalanceService balanceService = new BalanceService(accountRepository);

        double initialBalance = 1000.0;
        String id = accountRepository.createAccount("Orvyl", initialBalance);

        double debitedAmount = 50.0;
        balanceService.debit(id, debitedAmount);

        Double currentBalance = accountRepository.getAccount(id).getBalance();

        Assertions.assertEquals(initialBalance - debitedAmount, currentBalance);
    }
}
