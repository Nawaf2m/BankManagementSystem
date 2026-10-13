
package com.example.bankmanagementsystem.Controller;

import com.example.bankmanagementsystem.Api.ApiResponse;
import com.example.bankmanagementsystem.Model.Account;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/get")
    public ResponseEntity<?> getAccounts(){
        return ResponseEntity.status(200).body(accountService.getAccounts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAccount(@AuthenticationPrincipal User user, @RequestBody @Valid Account account){
        accountService.addAccount(user.getId(), account);
        return ResponseEntity.status(200).body(new ApiResponse("Account added successfully"));
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<?> updateAccount(@AuthenticationPrincipal User user, @PathVariable Integer accountId, @RequestBody @Valid Account account){
        accountService.updateAccount(user.getId(), accountId, account);
        return ResponseEntity.status(200).body(new ApiResponse("Account updated successfully"));
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal User user, @PathVariable Integer accountId){
        accountService.deleteAccount(user.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account deleted successfully"));
    }

    @PutMapping("/activate/{accountId}")
    public ResponseEntity<?> activateAccount(@PathVariable Integer accountId){
        accountService.activateAccount(accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account activated successfully"));
    }

    @GetMapping("/get/my/accounts")
    public ResponseEntity<?> getMyAccounts(@AuthenticationPrincipal User user){
        return ResponseEntity.status(200).body(accountService.getMyAccounts(user.getId()));
    }

    @GetMapping("/get/details/{accountId}")
    public ResponseEntity<?> getAccountById(@AuthenticationPrincipal User user, @PathVariable Integer accountId){
        return ResponseEntity.status(200).body(accountService.getAccountById(user.getId(), accountId));
    }

    @PutMapping("/deposit/{accountId}")
    public ResponseEntity<?> deposit(@AuthenticationPrincipal User user, @PathVariable Integer accountId, @RequestParam BigDecimal amount){
        accountService.deposit(user.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Amount deposited successfully"));
    }

    @PutMapping("/withdraw/{accountId}")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal User user, @PathVariable Integer accountId, @RequestParam BigDecimal amount){
        accountService.withdraw(user.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Amount withdrawn successfully"));
    }

    @PutMapping("/transfer/{fromAccountId}/{toAccountId}")
    public ResponseEntity<?> transfer(@AuthenticationPrincipal User user, @PathVariable Integer fromAccountId, @PathVariable Integer toAccountId, @RequestParam BigDecimal amount){
        accountService.transfer(user.getId(), fromAccountId, toAccountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Amount transferred successfully"));
    }

    @PutMapping("/block/{accountId}")
    public ResponseEntity<?> blockAccount(@PathVariable Integer accountId){
        accountService.blockAccount(accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account blocked successfully"));
    }
}
