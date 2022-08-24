package cz.martindavidik.accountingservice.repositories;

import cz.martindavidik.accountingservice.domain.Expense;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {

    Optional<Expense> findExpenseByExpenseNumber(String expenseNumber);
}
