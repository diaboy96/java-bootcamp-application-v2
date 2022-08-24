package cz.martindavidik.accountingservice.repositories;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExpenseItemRepository extends CrudRepository<ExpenseItem, Integer> {

    List<ExpenseItem> findByExpense(Expense expense);
}
