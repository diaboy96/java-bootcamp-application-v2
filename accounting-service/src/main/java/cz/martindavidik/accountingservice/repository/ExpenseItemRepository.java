package cz.martindavidik.accountingservice.repository;

import cz.martindavidik.accountingservice.domain.ExpenseItem;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseItemRepository extends CrudRepository<ExpenseItem, Integer> {

}
