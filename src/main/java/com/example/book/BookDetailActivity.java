package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private Book book;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        String bookId = getIntent().getStringExtra("bookId");
        book = BookData.getBookById(bookId);

        if (book == null) {
            Toast.makeText(this, "图书不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadBorrowHistory();
    }

    private void initViews() {
        ImageView ivCover = findViewById(R.id.iv_cover);
        loadBookCover(ivCover, book.getImageUrl());

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(book.getTitle());

        TextView tvAuthor = findViewById(R.id.tv_author);
        tvAuthor.setText("作者: " + book.getAuthor());

        TextView tvPublisher = findViewById(R.id.tv_publisher);
        tvPublisher.setText("出版社: " + book.getPublisher());

        TextView tvPublishDate = findViewById(R.id.tv_publish_date);
        tvPublishDate.setText("出版日期: " + book.getPublishDate());

        TextView tvRating = findViewById(R.id.tv_rating);
        tvRating.setText("★★★★★ " + book.getReviewCount() + "评论 " + book.getRating() + "推荐");

        TextView tvStock = findViewById(R.id.tv_stock);
        tvStock.setText("库存: " + book.getAvailableStock() + "/" + book.getStock());

        TextView tvSummary = findViewById(R.id.tv_summary);
        tvSummary.setText(book.getSummary());

        Button btnDate = findViewById(R.id.btn_date);
        Button btnBorrow = findViewById(R.id.btn_borrow);

        btnDate.setOnClickListener(v -> showDatePicker());

        btnBorrow.setOnClickListener(v -> borrowBook());

        if (book.getAvailableStock() <= 0) {
            btnBorrow.setEnabled(false);
            btnBorrow.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    private void loadBorrowHistory() {
        LinearLayout llBorrowHistory = findViewById(R.id.ll_borrow_history);
        List<BorrowRecord> allRecords = BookData.getBorrowRecords();

        boolean hasRecords = false;
        for (BorrowRecord record : allRecords) {
            if (record.getBookId().equals(book.getId())) {
                hasRecords = true;

                LinearLayout recordItem = new LinearLayout(this);
                recordItem.setOrientation(LinearLayout.HORIZONTAL);
                recordItem.setPadding(10, 10, 10, 10);

                LinearLayout infoLayout = new LinearLayout(this);
                infoLayout.setOrientation(LinearLayout.VERTICAL);

                TextView tvUser = new TextView(this);
                tvUser.setText("借阅人: " + record.getUserName());
                tvUser.setTextSize(14);
                tvUser.setTextColor(getResources().getColor(R.color.black));

                TextView tvDates = new TextView(this);
                tvDates.setText("借阅: " + record.getBorrowDate() + " | 应还: " + record.getReturnDate());
                tvDates.setTextSize(12);
                tvDates.setTextColor(getResources().getColor(R.color.gray_text));

                TextView tvStatus = new TextView(this);
                tvStatus.setText(record.isReturned() ? "已归还" : "借阅中");
                tvStatus.setTextSize(12);
                tvStatus.setTextColor(record.isReturned() ?
                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));

                infoLayout.addView(tvUser);
                infoLayout.addView(tvDates);
                infoLayout.addView(tvStatus);

                recordItem.addView(infoLayout);
                llBorrowHistory.addView(recordItem);

                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                divider.setBackgroundColor(getResources().getColor(R.color.light_gray));
                llBorrowHistory.addView(divider);
            }
        }

        if (!hasRecords) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText("暂无借阅记录");
            tvEmpty.setTextSize(14);
            tvEmpty.setTextColor(getResources().getColor(R.color.gray_text));
            llBorrowHistory.addView(tvEmpty);
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                    Toast.makeText(this, "已选择还书日期: " + selectedDate, Toast.LENGTH_SHORT).show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void borrowBook() {
        if (selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(this, "请先选择还书日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if (BookData.borrowBook(book.getId(), selectedDate, this)) {
            Toast.makeText(this, "借阅成功，请到借阅管理查看记录", Toast.LENGTH_LONG).show();
            loadBorrowHistory();
            TextView tvStock = findViewById(R.id.tv_stock);
            tvStock.setText("库存: " + book.getAvailableStock() + "/" + book.getStock());
            
            if (book.getAvailableStock() <= 0) {
                Button btnBorrow = findViewById(R.id.btn_borrow);
                btnBorrow.setEnabled(false);
                btnBorrow.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        } else {
            Toast.makeText(this, "借阅失败，该书库存不足", Toast.LENGTH_LONG).show();
        }
    }

    private void loadBookCover(ImageView ivCover, String imageUrl) {
        if (imageUrl != null && imageUrl.startsWith("drawable://")) {
            String drawableName = imageUrl.substring(11);
            int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
            if (resId != 0) {
                ivCover.setImageResource(resId);
            }
        } else {
            Glide.with(this).load(imageUrl).into(ivCover);
        }
    }
}