package com.example.book;

public class BorrowRecord {
    private String id;
    private String bookId;
    private String bookTitle;
    private String userId;
    private String userName;
    private String borrowDate;
    private String returnDate;
    private boolean returned;

    public BorrowRecord() {}

    public BorrowRecord(String id, String bookId, String bookTitle, String userId, 
                       String userName, String borrowDate, String returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.userId = userId;
        this.userName = userName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getBorrowDate() { return borrowDate; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }

    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}