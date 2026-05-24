package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private LinearLayout llResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        llResults = findViewById(R.id.ll_results);

        Button btnSearch = findViewById(R.id.btn_search);
        EditText etSearch = findViewById(R.id.et_search);

        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            if (keyword.isEmpty()) {
                Toast.makeText(this, "请输入搜索关键词", Toast.LENGTH_SHORT).show();
                return;
            }
            searchBooks(keyword);
        });

        loadAllBooks();
    }

    private void loadAllBooks() {
        List<Book> books = BookData.getBookList();
        displayBooks(books);
    }

    private void searchBooks(String keyword) {
        List<Book> results = BookData.searchBooks(keyword);
        displayBooks(results);
    }

    private void displayBooks(List<Book> books) {
        llResults.removeAllViews();

        if (books.isEmpty()) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText("未找到相关图书");
            tvEmpty.setTextSize(16);
            tvEmpty.setTextColor(getResources().getColor(R.color.gray_text));
            tvEmpty.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tvEmpty.setPadding(0, 50, 0, 0);
            llResults.addView(tvEmpty);
            return;
        }

        for (Book book : books) {
            LinearLayout bookItem = new LinearLayout(this);
            bookItem.setOrientation(LinearLayout.HORIZONTAL);
            bookItem.setPadding(10, 10, 10, 10);
            bookItem.setBackgroundColor(getResources().getColor(R.color.white));
            bookItem.setClickable(true);

            ImageView ivCover = new ImageView(this);
            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(100, 140);
            ivParams.setMarginEnd(15);
            ivCover.setLayoutParams(ivParams);
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
            LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            arrowParams.setMarginStart(10);
            arrowIcon.setLayoutParams(arrowParams);

            bookItem.addView(ivCover);
            bookItem.addView(infoLayout);
            bookItem.addView(arrowIcon);

            String bookId = book.getId();
            bookItem.setOnClickListener(v -> goToDetail(bookId));

            llResults.addView(bookItem);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8));
            divider.setBackgroundColor(getResources().getColor(R.color.light_gray));
            llResults.addView(divider);
        }
    }

    private void goToDetail(String bookId) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("bookId", bookId);
        startActivity(intent);
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