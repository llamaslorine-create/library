package com.example.book;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookData {
    private static List<Book> bookList = new ArrayList<>();
    private static List<BorrowRecord> borrowRecords = new ArrayList<>();
    private static boolean isInitialized = false;

    static {
        bookList.add(new Book("1", "我与地坛", "史铁生", "人民文学出版社", 
                "2011-06-01", "99.9%", "727130条", 
                "drawable://woyuditan",
                "史铁生经典作品",
                "《我与地坛》是史铁生的散文代表作，讲述了作者在地坛公园的思考与感悟，探讨生命的意义与存在的价值。",
                10));
        
        bookList.add(new Book("2", "活着", "余华", "北京十月文艺出版社", 
                "2021-10-01", "100%", "1179782条", 
                "drawable://huozhe",
                "余华著，新经典出品",
                "《活着》讲述了一个人一生的故事，这是一个历尽世间沧桑和磨难老人的人生感言。",
                15));
        
        bookList.add(new Book("3", "被讨厌的勇气", "岸见一郎, 古贺史健", "机械工业出版社", 
                "2020-03-05", "100%", "1172642条", 
                "drawable://yongqi",
                "阿德勒心理学",
                "本书以青年与哲人的对话形式，阐述阿德勒心理学的核心观点，帮助读者获得真正的自由与幸福。",
                8));
        
        bookList.add(new Book("4", "瓦尔登湖", "(美) 梭罗", "中央编译出版社", 
                "2019-04-01", "98%", "154665条", 
                "drawable://waerdenghu",
                "王光林 译 万亭文化 出品",
                "《瓦尔登湖》记录了梭罗在瓦尔登湖畔的独居生活，探讨自然、生活与精神追求。",
                5));
        
        bookList.add(new Book("5", "长安的荔枝", "马伯庸", "湖南文艺出版社", 
                "2022-10-31", "99%", "310403条", 
                "drawable://lizhi",
                "博集天卷 出品",
                "天宝十四年，长安城小吏李善德突然接到一个任务：要在贵妃诞日之前，从岭南运来新鲜荔枝。",
                12));
    }

    public static void initialize(Context context) {
        if (!isInitialized) {
            List<BorrowRecord> savedRecords = StorageUtil.loadBorrowRecords(context);
            if (!savedRecords.isEmpty()) {
                borrowRecords.addAll(savedRecords);
                for (BorrowRecord record : savedRecords) {
                    if (!record.isReturned()) {
                        Book book = getBookById(record.getBookId());
                        if (book != null) {
                            book.setBorrowedCount(book.getBorrowedCount() + 1);
                        }
                    }
                }
            }
            isInitialized = true;
        }
    }

    public static List<Book> getBookList() {
        return bookList;
    }

    public static Book getBookById(String id) {
        for (Book book : bookList) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    public static List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Book book : bookList) {
            if (book.getTitle().toLowerCase().contains(lowerKeyword) ||
                book.getAuthor().toLowerCase().contains(lowerKeyword) ||
                book.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(book);
            }
        }
        return results;
    }

    public static boolean addBook(Book book) {
        for (Book b : bookList) {
            if (b.getId().equals(book.getId())) {
                return false;
            }
        }
        bookList.add(book);
        return true;
    }

    public static boolean borrowBook(String bookId, String returnDate, Context context) {
        Book book = getBookById(bookId);
        if (book != null && book.getAvailableStock() > 0) {
            book.setBorrowedCount(book.getBorrowedCount() + 1);
            
            User user = UserData.getCurrentUser();
            Calendar calendar = Calendar.getInstance();
            String borrowDate = calendar.get(Calendar.YEAR) + "-" + 
                    String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + 
                    String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
            BorrowRecord record = new BorrowRecord(
                String.valueOf(System.currentTimeMillis()),
                bookId,
                book.getTitle(),
                user.getId(),
                user.getName(),
                borrowDate,
                returnDate
            );
            borrowRecords.add(record);
            StorageUtil.saveBorrowRecords(context, borrowRecords);
            return true;
        }
        return false;
    }

    public static boolean returnBook(String recordId, Context context) {
        for (BorrowRecord record : borrowRecords) {
            if (record.getId().equals(recordId) && !record.isReturned()) {
                record.setReturned(true);
                
                Book book = getBookById(record.getBookId());
                if (book != null && book.getBorrowedCount() > 0) {
                    book.setBorrowedCount(book.getBorrowedCount() - 1);
                }
                StorageUtil.saveBorrowRecords(context, borrowRecords);
                return true;
            }
        }
        return false;
    }

    public static List<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }

    public static List<BorrowRecord> getBorrowRecordsByUser(String userId) {
        List<BorrowRecord> userRecords = new ArrayList<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getUserId().equals(userId)) {
                userRecords.add(record);
            }
        }
        return userRecords;
    }

    public static boolean isBookBorrowed(String bookId) {
        for (BorrowRecord record : borrowRecords) {
            if (record.getBookId().equals(bookId) && !record.isReturned()) {
                return true;
            }
        }
        return false;
    }

    public static boolean deleteBook(String bookId) {
        if (isBookBorrowed(bookId)) {
            return false;
        }
        Book book = getBookById(bookId);
        if (book != null) {
            bookList.remove(book);
            return true;
        }
        return false;
    }

    public static boolean updateBook(Book updatedBook) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getId().equals(updatedBook.getId())) {
                bookList.set(i, updatedBook);
                return true;
            }
        }
        return false;
    }
}