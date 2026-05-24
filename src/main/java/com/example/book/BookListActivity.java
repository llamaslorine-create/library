package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
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

public class BookListActivity extends AppCompatActivity {

    private LinearLayout llBookList;
    private TextView tvToast;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        llBookList = findViewById(R.id.ll_book_list);
        tvToast = findViewById(R.id.tv_toast);

        loadBookList();
    }

    private void loadBookList() {
        llBookList.removeAllViews();
        List<Book> books = BookData.getBookList();

        for (Book book : books) {
            LinearLayout bookContainer = new LinearLayout(this);
            bookContainer.setOrientation(LinearLayout.VERTICAL);
            bookContainer.setPadding(10, 10, 10, 0);
            bookContainer.setBackgroundColor(getResources().getColor(R.color.white));

            LinearLayout bookItem = new LinearLayout(this);
            bookItem.setOrientation(LinearLayout.HORIZONTAL);
            bookItem.setPadding(10, 10, 10, 10);
            bookItem.setClickable(true);

            ImageView ivCover = new ImageView(this);
            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(100, 140);
            ivParams.setMarginEnd(15);
            ivCover.setLayoutParams(ivParams);
            ivCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            loadBookCover(ivCover, book.getImageUrl());

            LinearLayout infoLayout = new LinearLayout(this);
            infoLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            infoLayout.setLayoutParams(infoParams);

            TextView tvTitle = new TextView(this);
            tvTitle.setText(book.getTitle());
            tvTitle.setTextSize(18);
            tvTitle.setTextColor(getResources().getColor(R.color.black));
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView tvRating = new TextView(this);
            tvRating.setText("★★★★★ " + book.getReviewCount() + "评论 " + book.getRating() + "推荐");
            tvRating.setTextSize(12);
            tvRating.setTextColor(getResources().getColor(R.color.orange));

            TextView tvAuthor = new TextView(this);
            tvAuthor.setText(book.getAuthor());
            tvAuthor.setTextSize(14);
            tvAuthor.setTextColor(getResources().getColor(R.color.black));

            TextView tvDetail = new TextView(this);
            tvDetail.setText(book.getPublishDate() + " " + book.getPublisher());
            tvDetail.setTextSize(12);
            tvDetail.setTextColor(getResources().getColor(R.color.gray_text));

            TextView tvStock = new TextView(this);
            tvStock.setText("库存: " + book.getAvailableStock() + "/" + book.getStock());
            tvStock.setTextSize(12);
            tvStock.setTextColor(getResources().getColor(R.color.blue));

            TextView tvSummary = new TextView(this);
            tvSummary.setText("简介: " + book.getSummary());
            tvSummary.setTextSize(12);
            tvSummary.setTextColor(getResources().getColor(R.color.gray_text));
            tvSummary.setMaxLines(2);

            infoLayout.addView(tvTitle);
            infoLayout.addView(tvRating);
            infoLayout.addView(tvAuthor);
            infoLayout.addView(tvDetail);
            infoLayout.addView(tvStock);
            infoLayout.addView(tvSummary);

            TextView arrowIcon = new TextView(this);
            arrowIcon.setText(">");
            arrowIcon.setTextSize(24);
            arrowIcon.setTextColor(getResources().getColor(R.color.gray));
            arrowIcon.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            arrowParams.setMarginStart(10);
            arrowIcon.setLayoutParams(arrowParams);

            bookItem.addView(ivCover);
            bookItem.addView(infoLayout);
            bookItem.addView(arrowIcon);

            String bookId = book.getId();
            bookItem.setOnClickListener(v -> goToDetail(bookId));

            LinearLayout buttonLayout = new LinearLayout(this);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setPadding(10, 10, 10, 15);

            Button btnDate = new Button(this);
            btnDate.setText("选择时间");
            btnDate.setTextColor(getResources().getColor(R.color.white));
            btnDate.setTextSize(14);
            btnDate.setGravity(Gravity.CENTER);
            btnDate.setPadding(15, 10, 15, 10);
            GradientDrawable dateBg = new GradientDrawable();
            dateBg.setColor(getResources().getColor(R.color.blue));
            dateBg.setCornerRadius(8);
            btnDate.setBackground(dateBg);
            LinearLayout.LayoutParams btnDateParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            btnDateParams.setMarginEnd(15);
            btnDate.setLayoutParams(btnDateParams);

            Button btnBorrow = new Button(this);
            btnBorrow.setText("借阅");
            btnBorrow.setTextColor(getResources().getColor(R.color.white));
            btnBorrow.setTextSize(14);
            btnBorrow.setGravity(Gravity.CENTER);
            btnBorrow.setPadding(15, 10, 15, 10);
            GradientDrawable borrowBg = new GradientDrawable();
            borrowBg.setColor(book.getAvailableStock() > 0 ? getResources().getColor(R.color.blue) : getResources().getColor(R.color.gray));
            borrowBg.setCornerRadius(8);
            btnBorrow.setBackground(borrowBg);
            btnBorrow.setEnabled(book.getAvailableStock() > 0);
            LinearLayout.LayoutParams btnBorrowParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            btnBorrowParams.setMarginStart(15);
            btnBorrow.setLayoutParams(btnBorrowParams);

            btnDate.setOnClickListener(v -> showDatePicker(bookId));
            btnBorrow.setOnClickListener(v -> borrowBook(bookId));

            buttonLayout.addView(btnDate);
            buttonLayout.addView(btnBorrow);

            bookContainer.addView(bookItem);
            bookContainer.addView(buttonLayout);

            llBookList.addView(bookContainer);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            divider.setBackgroundColor(getResources().getColor(R.color.light_gray));
            llBookList.addView(divider);
        }
    }

    private void goToDetail(String bookId) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("bookId", bookId);
        startActivity(intent);
    }

    private void showDatePicker(String bookId) {
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

    private void borrowBook(String bookId) {
        if (selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(this, "请先选择还书日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if (BookData.borrowBook(bookId, selectedDate, this)) {
            Toast.makeText(this, "借阅成功，可在借阅管理中查看", Toast.LENGTH_LONG).show();
            loadBookList();
        } else {
            Toast.makeText(this, "借阅失败，该书库存不足", Toast.LENGTH_LONG).show();
        }
    }

    private void showToast(String message) {
        tvToast.setText(message);
        tvToast.setVisibility(View.VISIBLE);
        tvToast.postDelayed(() -> tvToast.setVisibility(View.GONE), 2000);
    }

    private void loadBookCover(ImageView ivCover, String imageUrl) {
        if (imageUrl != null && imageUrl.startsWith("drawable://")) {
            String drawableName = imageUrl.substring(11);
            int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
            if (resId != 0) {
                ivCover.setImageResource(resId);
            }
        } else {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_book).into(ivCover);
        }
    }
}