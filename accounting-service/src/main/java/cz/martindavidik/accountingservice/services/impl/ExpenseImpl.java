package cz.martindavidik.accountingservice.services.impl;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.repositories.ExpenseRepository;
import cz.martindavidik.accountingservice.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public Optional<Expense> findExpenseByExpenseNumber(String expenseNumber) {
        return expenseRepository.findExpenseByExpenseNumber(expenseNumber);
    }

    @Override
    @Transactional
    public List<Expense> findByDateBetween(Date from, Date to) {
        return expenseRepository.findByDateBetween(from, to);
    }

    @Override
    @Transactional
    public Expense attachExpenseDocument(String expenseNumber, String base64encodedPDFInvoice) {
        Optional<Expense> expense = this.findExpenseByExpenseNumber(expenseNumber);

        if (expense.isPresent()) {
            Expense expense1 = expense.get();
            expense1.setPDFinvoiceBase64Encoded(base64encodedPDFInvoice);

            return this.save(expense1);
        }

        return null;
    }

    @Override
    @Transactional
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }
}
