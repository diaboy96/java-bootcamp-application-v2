package cz.martindavidik.accountingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ExpenseItem {

    @Id
    @Column(unique = true)
    private int code;

    private String description;

    @Range(min = 1, max = 9999)
    private int numberOfPieces;

    private double pricePerPiece;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Expense.class)
    @JoinTable(
            name = "expense_expense_items",
            joinColumns = {@JoinColumn(name = "code")},
            inverseJoinColumns = {@JoinColumn(name = "expense_number")}
    )
    @JsonIgnoreProperties("expenseItems")
    private Set<Expense> expenses = new HashSet<>();

    public ExpenseItem() {
    }

    public ExpenseItem(int code, String description, int numberOfPieces, double pricePerPiece) {
        this.code = code;
        this.description = description;
        this.numberOfPieces = numberOfPieces;
        this.pricePerPiece = pricePerPiece;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(int numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        expense.getExpenseItems().add(this);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
        expense.getExpenseItems().remove(this);
    }

    public void removeAllExpenses() {
        for (Expense expense : expenses) {
            expense.getExpenseItems().remove(this);
        }
        expenses.clear();
    }
}
