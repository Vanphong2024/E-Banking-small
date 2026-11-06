package com.phong.mini_banking.service;

import com.phong.mini_banking.entity.*;
import com.phong.mini_banking.repository.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffAccountService {
    private static final BigDecimal MAX_BALANCE = new BigDecimal("9999999999999.99");
    private final BankAccountRepository accounts;
    private final TransactionRepository transactions;
    private final AccountStatusChangeRepository statusChanges;

    public StaffAccountService(BankAccountRepository accounts, TransactionRepository transactions,
                               AccountStatusChangeRepository statusChanges) {
        this.accounts = accounts;
        this.transactions = transactions;
        this.statusChanges = statusChanges;
    }

    @Transactional
    public void deposit(Long accountId, BigDecimal amount, String requestId, String staffEmail) {
        if (amount == null || amount.signum() <= 0 || amount.stripTrailingZeros().scale() > 0
                || amount.compareTo(MAX_BALANCE) > 0) {
            throw new IllegalArgumentException("Số tiền nạp phải là số nguyên VNĐ lớn hơn 0 và trong hạn mức số dư.");
        }
        if (requestId == null || !requestId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException("Yêu cầu nạp tiền không hợp lệ. Vui lòng tải lại trang.");
        }
        requireActor(staffEmail);
        BankAccount account = lockedCustomerAccount(accountId);
        var previous = transactions.findByDepositRequestId(requestId);
        if (previous.isPresent()) {
            transaction recorded = previous.get();
            if (!recorded.getReceiverAccount().getAccountId().equals(accountId)
                    || recorded.getAmount().compareTo(amount) != 0
                    || !staffEmail.equals(recorded.getPerformedBy())) {
                throw new IllegalArgumentException("Mã yêu cầu đã được sử dụng cho giao dịch khác.");
            }
            return;
        }
        if (!"ACTIVE".equals(account.getStatus())) {
            throw new IllegalArgumentException("Tài khoản đang bị khóa hoặc chưa hoạt động, không thể nạp tiền.");
        }
        BigDecimal newBalance = account.getBalance().add(amount);
        if (newBalance.compareTo(MAX_BALANCE) > 0) {
            throw new IllegalArgumentException("Số dư sau khi nạp vượt hạn mức lưu trữ.");
        }
        account.setBalance(newBalance);
        accounts.save(account);
        transaction deposit = new transaction();
        deposit.setAmount(amount);
        deposit.setReceiverAccount(account);
        deposit.setCreatedAt(LocalDateTime.now());
        deposit.setDescription("Nạp tiền tại quầy");
        deposit.setPerformedBy(staffEmail);
        deposit.setDepositRequestId(requestId);
        transactions.save(deposit);
    }

    @Transactional
    public void setStatus(Long accountId, String status, String staffEmail) {
        if (!"ACTIVE".equals(status) && !"LOCKED".equals(status)) {
            throw new IllegalArgumentException("Trạng thái tài khoản không hợp lệ.");
        }
        requireActor(staffEmail);
        BankAccount account = lockedCustomerAccount(accountId);
        if (status.equals(account.getStatus())) { return; }
        statusChanges.save(new AccountStatusChange(account, account.getStatus(), status, staffEmail));
        account.setStatus(status);
        accounts.save(account);
    }

    private BankAccount lockedCustomerAccount(Long id) {
        BankAccount account = accounts.findLockedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản ngân hàng."));
        if (account.getUser().getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("Chỉ được thao tác trên tài khoản khách hàng.");
        }
        return account;
    }

    private void requireActor(String actor) {
        if (actor == null || actor.isBlank()) {
            throw new IllegalArgumentException("Không xác định được nhân viên thực hiện.");
        }
    }
}
