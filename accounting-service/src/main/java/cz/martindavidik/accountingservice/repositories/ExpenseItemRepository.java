package cz.martindavidik.accountingservice.repositories;

import cz.martindavidik.accountingservice.domain.ExpenseItem;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseItemRepository extends CrudRepository<ExpenseItem, Integer> {

}
