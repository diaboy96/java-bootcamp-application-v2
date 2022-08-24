package cz.martindavidik.accountingservice.services.impl;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.repositories.ExpenseRepository;
import cz.martindavidik.accountingservice.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }
}
