package com.example.book;

public class Book {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String publishDate;
    private String rating;
    private String reviewCount;
    private String imageUrl;
    private String description;
    private String summary; // 简介
    private int stock; // 库存
    private int borrowedCount; // 已借阅数量

    public Book() {}

    public Book(String id, String title, String author, String publisher, String publishDate,
                String rating, String reviewCount, String imageUrl, String description, 
                String summary, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.description = description;
        this.summary = summary;
        this.stock = stock;
        this.borrowedCount = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getPublishDate() { return publishDate; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getReviewCount() { return reviewCount; }
    public void setReviewCount(String reviewCount) { this.reviewCount = reviewCount; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getBorrowedCount() { return borrowedCount; }
    public void setBorrowedCount(int borrowedCount) { this.borrowedCount = borrowedCount; }

    public int getAvailableStock() {
        return stock - borrowedCount;
    }
}