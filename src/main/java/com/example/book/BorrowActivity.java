package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BorrowActivity extends AppCompatActivity {

    private LinearLayout llBorrowList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        llBorrowList = findViewById(R.id.ll_borrow_list);

        loadBorrowRecords();
        checkOverdueRecords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBorrowRecords();
        checkOverdueRecords();
    }

    private void checkOverdueRecords() {
        User user = UserData.getCurrentUser();
        if (user == null) {
            return;
        }

        List<BorrowRecord> records;
        if (user.isAdmin()) {
            records = BookData.getBorrowRecords();
        } else {
            records = BookData.getBorrowRecordsByUser(user.getId());
        }

        int overdueCount = 0;
        for (BorrowRecord record : records) {
            if (!record.isReturned() && isOverdue(record.getReturnDate())) {
                overdueCount++;
            }
        }

        if (overdueCount > 0) {
            Toast.makeText(this, "有 " + overdueCount + " 本书已到期，请及时归还！", 
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isOverdue(String returnDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(returnDate);
            Date today = new Date();
            return date != null && date.before(today);
        } catch (ParseException e) {
            return false;
        }
    }

    private void handleReturn(String recordId) {
        if (BookData.returnBook(recordId, this)) {
            Toast.makeText(this, "归还成功，图书已放回书架", Toast.LENGTH_LONG).show();
            loadBorrowRecords();
        } else {
            Toast.makeText(this, "归还失败，请稍后重试", Toast.LENGTH_LONG).show();
        }
    }

    private void loadBorrowRecords() {
        llBorrowList.removeAllViews();

        User user = UserData.getCurrentUser();
        if (user == null) {
            return;
        }

        List<BorrowRecord> records;
        if (user.isAdmin()) {
            records = BookData.getBorrowRecords();
        } else {
            records = BookData.getBorrowRecordsByUser(user.getId());
        }

        if (records.isEmpty()) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText(user.isAdmin() ? "暂无借阅记录" : "您暂无借阅记录");
            tvEmpty.setTextSize(16);
            tvEmpty.setTextColor(getResources().getColor(R.color.gray_text));
            tvEmpty.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tvEmpty.setPadding(0, 50, 0, 0);
            llBorrowList.addView(tvEmpty);
            return;
        }

        for (BorrowRecord record : records) {
            Book book = BookData.getBookById(record.getBookId());

            LinearLayout recordItem = new LinearLayout(this);
            recordItem.setOrientation(LinearLayout.HORIZONTAL);
            recordItem.setPadding(10, 10, 10, 10);
            recordItem.setBackgroundColor(getResources().getColor(R.color.white));

            ImageView ivCover = new ImageView(this);
            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(80, 110);
            ivParams.setMarginEnd(15);
            ivCover.setLayoutParams(ivParams);
            if (book != null) {
                Glide.with(this).load(book.getImageUrl()).into(ivCover);
            }

            LinearLayout infoLayout = new LinearLayout(this);
            infoLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            infoLayout.setLayoutParams(infoParams);

            TextView tvTitle = new TextView(this);
            tvTitle.setText(record.getBookTitle());
            tvTitle.setTextSize(16);
            tvTitle.setTextColor(getResources().getColor(R.color.black));
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

            if (user.isAdmin()) {
                TextView tvUser = new TextView(this);
                tvUser.setText("借阅人: " + record.getUserName());
                tvUser.setTextSize(12);
                tvUser.setTextColor(getResources().getColor(R.color.blue));
                infoLayout.addView(tvUser);
            }

            TextView tvBorrowDate = new TextView(this);
            tvBorrowDate.setText("借阅日期: " + record.getBorrowDate());
            tvBorrowDate.setTextSize(12);
            tvBorrowDate.setTextColor(getResources().getColor(R.color.gray_text));

            TextView tvReturnDate = new TextView(this);
            tvReturnDate.setText("预约还书: " + record.getReturnDate());
            tvReturnDate.setTextSize(12);
            
            boolean isOverdue = !record.isReturned() && isOverdue(record.getReturnDate());
            tvReturnDate.setTextColor(isOverdue ? 
                    getResources().getColor(R.color.red) : getResources().getColor(R.color.orange));

            TextView tvStatus = new TextView(this);
            if (record.isReturned()) {
                tvStatus.setText("已归还");
                tvStatus.setTextColor(getResources().getColor(R.color.green));
            } else if (isOverdue) {
                tvStatus.setText("已到期");
                tvStatus.setTextColor(getResources().getColor(R.color.red));
            } else {
                tvStatus.setText("借阅中");
                tvStatus.setTextColor(getResources().getColor(R.color.orange));
            }
            tvStatus.setTextSize(12);

            infoLayout.addView(tvTitle);
            infoLayout.addView(tvBorrowDate);
            infoLayout.addView(tvReturnDate);
            infoLayout.addView(tvStatus);

            recordItem.addView(ivCover);
            recordItem.addView(infoLayout);

            if (!record.isReturned()) {
                Button btnReturn = new Button(this);
                btnReturn.setText("归还");
                btnReturn.setTextSize(12);
                btnReturn.setBackgroundColor(getResources().getColor(R.color.blue));
                btnReturn.setTextColor(getResources().getColor(R.color.white));
                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                btnParams.setMarginStart(10);
                btnReturn.setLayoutParams(btnParams);
                
                btnReturn.setOnClickListener(v -> handleReturn(record.getId()));
                recordItem.addView(btnReturn);
            }

            llBorrowList.addView(recordItem);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            divider.setBackgroundColor(getResources().getColor(R.color.light_gray));
            llBorrowList.addView(divider);
        }
    }
}