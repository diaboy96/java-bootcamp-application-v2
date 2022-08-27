package cz.martindavidik.accountingservice.repositories;

import cz.martindavidik.accountingservice.domain.Expense;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {

    Optional<Expense> findExpenseByExpenseNumber(String expenseNumber);

    List<Expense> findByDateBetween(Date from, Date to);

    List<Expense> findExpenseByDateAndPaid(Date date, boolean paid);
}
