package cz.martindavidik.accountingservice.repository;

import cz.martindavidik.accountingservice.domain.Expense;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {

    Optional<Expense> findExpenseByExpenseNumber(String expenseNumber);

    List<Expense> findByDateBetween(LocalDate from, LocalDate to);

    List<Expense> findExpenseByDateAndPaid(LocalDate date, boolean paid);
}
