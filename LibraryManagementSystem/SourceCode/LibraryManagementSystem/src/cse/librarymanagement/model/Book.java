package cse.librarymanagement.model;

public class Book {

    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private int categoryId;
    private int shelfId;
    private int totalCopies;
    private int availableCopies;

    public Book() {
    }

    public Book(int bookId, String isbn, String title,
                String author, String publisher,
                int publicationYear, int categoryId,
                int shelfId, int totalCopies,
                int availableCopies) {

        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.categoryId = categoryId;
        this.shelfId = shelfId;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public int getBookId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getShelfId() {
        return shelfId;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setShelfId(int shelfId) {
        this.shelfId = shelfId;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}