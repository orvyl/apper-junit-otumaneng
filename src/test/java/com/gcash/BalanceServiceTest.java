package com.gcash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

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
    void testDebit() throws InsufficientBalanceException, AccountNotFoundException {
        AccountRepository accountRepository = new AccountRepository();
        BalanceService balanceService = new BalanceService(accountRepository);

        double initialBalance = 1000.0;
        String id = accountRepository.createAccount("Orvyl", initialBalance);

        double debitedAmount = 50.0;
        balanceService.debit(id, debitedAmount);

        Double currentBalance = accountRepository.getAccount(id).getBalance();

        Assertions.assertEquals(initialBalance - debitedAmount, currentBalance);
    }

    @Test
    void testDebitInsufficientBalance() {
        AccountRepository accountRepository = new AccountRepository();
        BalanceService balanceService = new BalanceService(accountRepository);

        double initialBalance = 1000.0;
        String id = accountRepository.createAccount("Orvyl", initialBalance);

        double debitedAmount = 50_000.0;
        Assertions.assertThrows(InsufficientBalanceException.class, () -> balanceService.debit(id, debitedAmount));
    }

    @Test
    void testDebitAccountNotFound() {
        AccountRepository accountRepository = new AccountRepository();
        BalanceService balanceService = new BalanceService(accountRepository);

        double initialBalance = 1000.0;
        String id = accountRepository.createAccount("Orvyl", initialBalance);

        double debitedAmount = 50_000.0;
        Assertions.assertThrows(AccountNotFoundException.class, () -> balanceService.debit("random id", debitedAmount));
    }

    @Test
    void testTransfer() throws InsufficientBalanceException, AccountNotFoundException {
        AccountRepository accountRepository = new AccountRepository();
        BalanceService balanceService = new BalanceService(accountRepository);

        double initialBalance = 1000.0;
        String id0 = accountRepository.createAccount("Orvyl", initialBalance);
        String id1 = accountRepository.createAccount("Eishi", initialBalance);

        double transferAmount = 50.0;
        balanceService.transfer(id0, id1, transferAmount);

        Assertions.assertAll(() -> Assertions.assertEquals(
                initialBalance - transferAmount,
                accountRepository.getAccount(id0).getBalance()),
                () -> Assertions.assertEquals(
                        initialBalance + transferAmount,
                        accountRepository.getAccount(id1).getBalance()));
    }
}
