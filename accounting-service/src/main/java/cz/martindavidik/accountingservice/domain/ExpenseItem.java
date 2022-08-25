package cz.martindavidik.accountingservice.domain;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ExpenseItem {

    @Id
    @Column(unique = true)
    private int code;

    private String description;

    @Range(min = 1, max = 9999)
    private int numberOfPieces;

    private double pricePerPiece;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    public ExpenseItem() {
    }

    public ExpenseItem(int code, String description, int numberOfPieces, double pricePerPiece) {
        this.code = code;
        this.description = description;
        this.numberOfPieces = numberOfPieces;
        this.pricePerPiece = pricePerPiece;
    }

    public ExpenseItem(int code, String description, int numberOfPieces, double pricePerPiece, Expense expense) {
        this.code = code;
        this.description = description;
        this.numberOfPieces = numberOfPieces;
        this.pricePerPiece = pricePerPiece;
        this.expense = expense;
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
}
