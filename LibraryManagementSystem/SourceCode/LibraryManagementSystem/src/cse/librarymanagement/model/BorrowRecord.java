package cse.librarymanagement.model;

import java.time.LocalDate;

public class BorrowRecord {

    private int borrowId;
    private int studentId;
    private String studentIdNumber;
    private int bookId;
    private String studentName;
    private String membershipId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fineAmount;
    private String status;

    public BorrowRecord() {
    }

    public BorrowRecord(int borrowId, int studentId,
                        int bookId, String studentName,
                        String membershipId, String bookTitle,
                        LocalDate borrowDate, LocalDate dueDate,
                        LocalDate returnDate, double fineAmount,
                        String status) {

        this.borrowId = borrowId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.studentName = studentName;
        this.membershipId = membershipId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = status;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public int getStudentId() {
        return studentId;
    }

    // Returns the student's actual ID number.
    public String getStudentIdNumber() {
        return studentIdNumber;
    }

    public int getBookId() {
        return bookId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    // Sets the student's actual ID number.
    public void setStudentIdNumber(String studentIdNumber) {
        this.studentIdNumber = studentIdNumber;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}