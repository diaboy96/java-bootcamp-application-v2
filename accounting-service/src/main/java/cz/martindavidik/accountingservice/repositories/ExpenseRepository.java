package cz.martindavidik.accountingservice.repositories;

import cz.martindavidik.accountingservice.domain.Expense;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {
}
