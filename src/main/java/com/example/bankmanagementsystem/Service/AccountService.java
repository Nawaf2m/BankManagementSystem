package com.example.bankmanagementsystem.Service;

import com.example.bankmanagementsystem.Api.ApiException;
import com.example.bankmanagementsystem.Model.Account;
import com.example.bankmanagementsystem.Model.Customer;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Repository.AccountRepository;
import com.example.bankmanagementsystem.Repository.AuthRepository;
import com.example.bankmanagementsystem.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;

    public List<Account> getAccounts(){
        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()){
            throw new ApiException("The list is empty");
        }
        return accounts;
    }

    public void addAccount(Integer userId, Account account){
        User user = authRepository.findUserById(userId);

        if (user == null || user.getCustomer() == null){
            throw new ApiException("Customer not found");
        }

        Account oldAccount = accountRepository.findAccountByAccountNumber(account.getAccountNumber());

        if (oldAccount != null){
            throw new ApiException("Account number already exists");
        }

        account.setId(null);
        account.setBalance(BigDecimal.ZERO);
        account.setIsActive(false);
        account.setCustomer(user.getCustomer());

        accountRepository.save(account);
    }

    public void updateAccount(Integer userId, Integer accountId, Account account){
        Account oldAccount = accountRepository.findAccountById(accountId);

        if (oldAccount == null){
            throw new ApiException("Account not found");
        }

        if (!oldAccount.getCustomer().getUser().getId().equals(userId)){
            throw new ApiException("You cannot update this account");
        }

        Account existingAccount = accountRepository.findAccountByAccountNumber(account.getAccountNumber());

        if (existingAccount != null && !existingAccount.getId().equals(accountId)){
            throw new ApiException("Account number already exists");
        }

        oldAccount.setAccountNumber(account.getAccountNumber());

        accountRepository.save(oldAccount);
    }

    public void deleteAccount(Integer userId, Integer accountId){
        Account account = accountRepository.findAccountById(accountId);

        if (account == null){
            throw new ApiException("Account not found");
        }

        if (!account.getCustomer().getUser().getId().equals(userId)){
            throw new ApiException("You cannot delete this account");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0){
            throw new ApiException("Cannot delete account with remaining balance");
        }

        accountRepository.delete(account);
    }

    public List<Account> getMyAccounts(Integer userId){
        List<Account> accounts = accountRepository.findAccountsByCustomerUserId(userId);

        if (accounts.isEmpty()){
            throw new ApiException("You don't have any accounts");
        }

        return accounts;
    }

    public void activateAccount(Integer accountId){
        Account account = accountRepository.findAccountById(accountId);

        if (account == null){
            throw new ApiException("Account not found");
        }

        if (account.getIsActive()){
            throw new ApiException("Account is already active");
        }

        account.setIsActive(true);
        accountRepository.save(account);
    }

    public Account getAccountById(Integer userId, Integer accountId){
        Account account = accountRepository.findAccountById(accountId);

        if (account == null){
            throw new ApiException("Account not found");
        }

        if (!account.getCustomer().getUser().getId().equals(userId)){
            throw new ApiException("You cannot access this account");
        }

        return account;
    }

    public void deposit(Integer userId, Integer accountId, BigDecimal amount){
        Account account = accountRepository.findAccountById(accountId);

        if (account == null){
            throw new ApiException("Account not found");
        }

        if (!account.getCustomer().getUser().getId().equals(userId)){
            throw new ApiException("You cannot deposit into this account");
        }

        if (!account.getIsActive()){
            throw new ApiException("Account is not active");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new ApiException("Deposit amount must be greater than zero");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public void withdraw(Integer userId, Integer accountId, BigDecimal amount){
        Account account = accountRepository.findAccountById(accountId);

        if (account == null){
            throw new ApiException("Account not found");
        }

        if (!account.getCustomer().getUser().getId().equals(userId)){
            throw new ApiException("You cannot withdraw from this account");
        }

        if (!account.getIsActive()){
            throw new ApiException("Account is not active");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new ApiException("Withdraw amount must be greater than zero");
        }

        if (account.getBalance().compareTo(amount) < 0){
            throw new ApiException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    @Transactional
    public void transfer(Integer userId, Integer fromAccountId, Integer toAccountId, BigDecimal amount){
        Account fromAccount = accountRepository.findAccountById(fromAccountId);
        Account toAccount = accountRepository.findAccountById(toAccountId);

        if (fromAccount == null || toAccount == null){
            throw new ApiException("Account not found");
        }

        if (!fromAccount.getCustomer().getUser().getId().equals(userId)){
            throw new ApiException("You cannot transfer from this account");
        }

        if (fromAccount.getId().equals(toAccount.getId())){
            throw new ApiException("Cannot transfer to the same account");
        }

        if (!fromAccount.getIsActive() || !toAccount.getIsActive()){
            throw new ApiException("Both accounts must be active");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new ApiException("Transfer amount must be greater than zero");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0){
            throw new ApiException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    public void blockAccount(Integer accountId){
        Account account = accountRepository.findAccountById(accountId);

        if (account == null){
            throw new ApiException("Account not found");
        }

        if (!account.getIsActive()){
            throw new ApiException("Account is already blocked");
        }

        account.setIsActive(false);
        accountRepository.save(account);
    }
}