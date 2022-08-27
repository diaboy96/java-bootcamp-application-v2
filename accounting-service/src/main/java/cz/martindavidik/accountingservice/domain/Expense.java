package cz.martindavidik.accountingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Expense {

    @Id
    @Column(unique = true)
    private String expenseNumber;

    @Column(length = 8)
    private int supplier;

    private Date date;

    @ManyToMany(mappedBy = "expenses", targetEntity = ExpenseItem.class)
    @JsonIgnoreProperties("expenses")
    private Set<ExpenseItem> expenseItems = new HashSet<>();

    private String PDFinvoicePath = null;

    private boolean paid = false;

    public Expense() {
    }

    public Expense(String expenseNumber, int supplier, Date date) {
        this.expenseNumber = expenseNumber;
        this.supplier = supplier;
        this.date = date;
    }

    public Expense(String expenseNumber, int supplier, Date date, String PDFinvoicePath, boolean paid) {
        this.expenseNumber = expenseNumber;
        this.supplier = supplier;
        this.date = date;
        this.PDFinvoicePath = PDFinvoicePath;
        this.paid = paid;
    }

    public String getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(String expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPDFinvoicePath() {
        return PDFinvoicePath;
    }

    public void setPDFinvoicePath(String PDFinvoicePath) {
        this.PDFinvoicePath = PDFinvoicePath;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Set<ExpenseItem> getExpenseItems() {
        return expenseItems;
    }

    public void removeAllExpenseItems() {
        for (ExpenseItem expenseItem : expenseItems) {
            expenseItem.getExpenses().remove(this);
        }
        expenseItems.clear();
    }
}
