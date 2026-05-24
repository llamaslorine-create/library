package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private LinearLayout llContent;
    private boolean showAddBook = true;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        llContent = findViewById(R.id.ll_content);

        Button btnAddBook = findViewById(R.id.btn_add_book);
        Button btnBookManage = findViewById(R.id.btn_book_manage);
        Button btnUserManage = findViewById(R.id.btn_user_manage);

        btnAddBook.setOnClickListener(v -> {
            showAddBook = true;
            btnAddBook.setBackgroundColor(getResources().getColor(R.color.blue));
            btnAddBook.setTextColor(getResources().getColor(R.color.white));
            btnBookManage.setBackgroundColor(getResources().getColor(R.color.gray));
            btnBookManage.setTextColor(getResources().getColor(R.color.black));
            btnUserManage.setBackgroundColor(getResources().getColor(R.color.gray));
            btnUserManage.setTextColor(getResources().getColor(R.color.black));
            showAddBookForm();
        });

        btnBookManage.setOnClickListener(v -> {
            showAddBook = false;
            btnAddBook.setBackgroundColor(getResources().getColor(R.color.gray));
            btnAddBook.setTextColor(getResources().getColor(R.color.black));
            btnBookManage.setBackgroundColor(getResources().getColor(R.color.blue));
            btnBookManage.setTextColor(getResources().getColor(R.color.white));
            btnUserManage.setBackgroundColor(getResources().getColor(R.color.gray));
            btnUserManage.setTextColor(getResources().getColor(R.color.black));
            showBookList();
        });

        btnUserManage.setOnClickListener(v -> {
            showAddBook = false;
            btnAddBook.setBackgroundColor(getResources().getColor(R.color.gray));
            btnAddBook.setTextColor(getResources().getColor(R.color.black));
            btnBookManage.setBackgroundColor(getResources().getColor(R.color.gray));
            btnBookManage.setTextColor(getResources().getColor(R.color.black));
            btnUserManage.setBackgroundColor(getResources().getColor(R.color.blue));
            btnUserManage.setTextColor(getResources().getColor(R.color.white));
            showAddUserForm();
        });

        showAddBookForm();
    }

    private void showAddBookForm() {
        llContent.removeAllViews();
        selectedImagePath = null;

        TextView title = new TextView(this);
        title.setText("添加新图书");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        llContent.addView(title);

        EditText etId = createEditText("图书ID");
        EditText etTitle = createEditText("书名");
        EditText etAuthor = createEditText("作者");
        EditText etPublisher = createEditText("出版社");
        EditText etPublishDate = createEditText("出版日期 (yyyy-MM-dd)");
        EditText etRating = createEditText("推荐率 (如: 99.9%)");
        EditText etReviewCount = createEditText("评论数");
        EditText etDescription = createEditText("描述");
        EditText etSummary = createEditText("简介");
        EditText etStock = createEditText("库存数量");

        LinearLayout imageLayout = new LinearLayout(this);
        imageLayout.setOrientation(LinearLayout.VERTICAL);

        Button btnSelectImage = new Button(this);
        btnSelectImage.setText("选择封面图片");
        btnSelectImage.setBackgroundColor(getResources().getColor(R.color.blue));
        btnSelectImage.setTextColor(getResources().getColor(R.color.white));
        btnSelectImage.setTextSize(16);
        btnSelectImage.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 50));
        btnSelectImage.setPadding(0, 10, 0, 10);

        ImageView ivPreview = new ImageView(this);
        ivPreview.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 200));
        ivPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivPreview.setBackgroundColor(getResources().getColor(R.color.light_gray));

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        imageLayout.addView(btnSelectImage);
        imageLayout.addView(ivPreview);

        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageLayoutParams.setMargins(0, 0, 0, 15);
        imageLayout.setLayoutParams(imageLayoutParams);

        Button btnSubmit = new Button(this);
        btnSubmit.setText("添加图书");
        btnSubmit.setBackgroundColor(getResources().getColor(R.color.blue));
        btnSubmit.setTextColor(getResources().getColor(R.color.white));
        btnSubmit.setTextSize(18);
        btnSubmit.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 60));
        btnSubmit.setPadding(0, 15, 0, 15);

        btnSubmit.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();
            String titleStr = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String publisher = etPublisher.getText().toString().trim();
            String publishDate = etPublishDate.getText().toString().trim();
            String rating = etRating.getText().toString().trim();
            String reviewCount = etReviewCount.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String summary = etSummary.getText().toString().trim();

            int stock = 0;
            try {
                stock = Integer.parseInt(etStock.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "库存数量必须是数字", Toast.LENGTH_SHORT).show();
                return;
            }

            if (id.isEmpty() || titleStr.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "请填写必填项", Toast.LENGTH_SHORT).show();
                return;
            }

            Book book = new Book(id, titleStr, author, publisher, publishDate,
                    rating, reviewCount, selectedImagePath, description, summary, stock);

            if (BookData.addBook(book)) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                clearFields(etId, etTitle, etAuthor, etPublisher, etPublishDate,
                        etRating, etReviewCount, etDescription, etSummary, etStock);
                selectedImagePath = null;
                ivPreview.setImageURI(null);
            } else {
                Toast.makeText(this, "图书ID已存在", Toast.LENGTH_SHORT).show();
            }
        });

        llContent.addView(etId);
        llContent.addView(etTitle);
        llContent.addView(etAuthor);
        llContent.addView(etPublisher);
        llContent.addView(etPublishDate);
        llContent.addView(etRating);
        llContent.addView(etReviewCount);
        llContent.addView(imageLayout);
        llContent.addView(etDescription);
        llContent.addView(etSummary);
        llContent.addView(etStock);
        llContent.addView(btnSubmit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            selectedImagePath = selectedImage.toString();
            Toast.makeText(this, "图片已选择", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBookList() {
        llContent.removeAllViews();

        TextView title = new TextView(this);
        title.setText("图书管理");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        llContent.addView(title);

        List<Book> books = BookData.getBookList();

        for (Book book : books) {
            LinearLayout bookItem = new LinearLayout(this);
            bookItem.setOrientation(LinearLayout.VERTICAL);
            bookItem.setPadding(15, 15, 15, 15);
            bookItem.setBackgroundColor(getResources().getColor(R.color.white));

            LinearLayout infoLayout = new LinearLayout(this);
            infoLayout.setOrientation(LinearLayout.VERTICAL);

            TextView tvTitle = new TextView(this);
            tvTitle.setText("书名: " + book.getTitle());
            tvTitle.setTextSize(16);
            tvTitle.setTextColor(getResources().getColor(R.color.black));
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView tvAuthor = new TextView(this);
            tvAuthor.setText("作者: " + book.getAuthor());
            tvAuthor.setTextSize(14);
            tvAuthor.setTextColor(getResources().getColor(R.color.gray_text));

            TextView tvStock = new TextView(this);
            tvStock.setText("库存: " + book.getAvailableStock() + "/" + book.getStock());
            tvStock.setTextSize(14);
            tvStock.setTextColor(getResources().getColor(R.color.blue));

            TextView tvStatus = new TextView(this);
            boolean isBorrowed = BookData.isBookBorrowed(book.getId());
            tvStatus.setText(isBorrowed ? "状态: 有借阅中" : "状态: 可删除");
            tvStatus.setTextSize(14);
            tvStatus.setTextColor(isBorrowed ? getResources().getColor(R.color.red) : getResources().getColor(R.color.green));

            infoLayout.addView(tvTitle);
            infoLayout.addView(tvAuthor);
            infoLayout.addView(tvStock);
            infoLayout.addView(tvStatus);

            LinearLayout buttonLayout = new LinearLayout(this);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setPadding(0, 10, 0, 0);

            Button btnEdit = new Button(this);
            btnEdit.setText("编辑");
            btnEdit.setBackgroundColor(getResources().getColor(R.color.blue));
            btnEdit.setTextColor(getResources().getColor(R.color.white));
            btnEdit.setTextSize(14);
            btnEdit.setPadding(15, 8, 15, 8);
            LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            editParams.setMarginEnd(10);
            btnEdit.setLayoutParams(editParams);

            Button btnDelete = new Button(this);
            btnDelete.setText("删除");
            btnDelete.setTextColor(getResources().getColor(R.color.white));
            btnDelete.setTextSize(14);
            btnDelete.setPadding(15, 8, 15, 8);
            if (isBorrowed) {
                btnDelete.setBackgroundColor(getResources().getColor(R.color.gray));
                btnDelete.setEnabled(false);
            } else {
                btnDelete.setBackgroundColor(getResources().getColor(R.color.red));
                btnDelete.setEnabled(true);
            }
            LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            deleteParams.setMarginStart(10);
            btnDelete.setLayoutParams(deleteParams);

            String bookId = book.getId();
            btnEdit.setOnClickListener(v -> showEditBookForm(book));
            btnDelete.setOnClickListener(v -> deleteBook(bookId));

            buttonLayout.addView(btnEdit);
            buttonLayout.addView(btnDelete);

            bookItem.addView(infoLayout);
            bookItem.addView(buttonLayout);

            llContent.addView(bookItem);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            divider.setBackgroundColor(getResources().getColor(R.color.light_gray));
            llContent.addView(divider);
        }
    }

    private void showEditBookForm(Book book) {
        llContent.removeAllViews();

        TextView title = new TextView(this);
        title.setText("编辑图书");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        llContent.addView(title);

        EditText etId = createEditText("图书ID (不可修改)");
        etId.setText(book.getId());
        etId.setEnabled(false);

        EditText etTitle = createEditText("书名");
        etTitle.setText(book.getTitle());

        EditText etAuthor = createEditText("作者");
        etAuthor.setText(book.getAuthor());

        EditText etPublisher = createEditText("出版社");
        etPublisher.setText(book.getPublisher());

        EditText etPublishDate = createEditText("出版日期");
        etPublishDate.setText(book.getPublishDate());

        EditText etRating = createEditText("推荐率");
        etRating.setText(book.getRating());

        EditText etReviewCount = createEditText("评论数");
        etReviewCount.setText(book.getReviewCount());

        EditText etImageUrl = createEditText("封面图片URL");
        etImageUrl.setText(book.getImageUrl());

        EditText etDescription = createEditText("描述");
        etDescription.setText(book.getDescription());

        EditText etSummary = createEditText("简介");
        etSummary.setText(book.getSummary());

        EditText etStock = createEditText("库存数量");
        etStock.setText(String.valueOf(book.getStock()));

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button btnSave = new Button(this);
        btnSave.setText("保存修改");
        btnSave.setBackgroundColor(getResources().getColor(R.color.blue));
        btnSave.setTextColor(getResources().getColor(R.color.white));
        btnSave.setTextSize(16);
        btnSave.setLayoutParams(new LinearLayout.LayoutParams(0, 50, 1));
        btnSave.setPadding(0, 10, 0, 10);

        Button btnCancel = new Button(this);
        btnCancel.setText("取消");
        btnCancel.setBackgroundColor(getResources().getColor(R.color.gray));
        btnCancel.setTextColor(getResources().getColor(R.color.black));
        btnCancel.setTextSize(16);
        btnCancel.setLayoutParams(new LinearLayout.LayoutParams(0, 50, 1));
        btnCancel.setPadding(0, 10, 0, 10);

        btnSave.setOnClickListener(v -> {
            String titleStr = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String publisher = etPublisher.getText().toString().trim();
            String publishDate = etPublishDate.getText().toString().trim();
            String rating = etRating.getText().toString().trim();
            String reviewCount = etReviewCount.getText().toString().trim();
            String imageUrl = etImageUrl.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String summary = etSummary.getText().toString().trim();

            int stock = 0;
            try {
                stock = Integer.parseInt(etStock.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "库存数量必须是数字", Toast.LENGTH_SHORT).show();
                return;
            }

            if (titleStr.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "请填写必填项", Toast.LENGTH_SHORT).show();
                return;
            }

            Book updatedBook = new Book(book.getId(), titleStr, author, publisher, publishDate,
                    rating, reviewCount, imageUrl, description, summary, stock);
            updatedBook.setBorrowedCount(book.getBorrowedCount());

            if (BookData.updateBook(updatedBook)) {
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                showBookList();
            } else {
                Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> showBookList());

        buttonLayout.addView(btnSave);
        buttonLayout.addView(btnCancel);

        llContent.addView(etId);
        llContent.addView(etTitle);
        llContent.addView(etAuthor);
        llContent.addView(etPublisher);
        llContent.addView(etPublishDate);
        llContent.addView(etRating);
        llContent.addView(etReviewCount);
        llContent.addView(etImageUrl);
        llContent.addView(etDescription);
        llContent.addView(etSummary);
        llContent.addView(etStock);
        llContent.addView(buttonLayout);
    }

    private void deleteBook(String bookId) {
        if (BookData.deleteBook(bookId)) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            showBookList();
        } else {
            Toast.makeText(this, "该书有借阅中，无法删除", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddUserForm() {
        llContent.removeAllViews();

        TextView title = new TextView(this);
        title.setText("添加用户");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        llContent.addView(title);

        EditText etUsername = createEditText("用户名");
        EditText etName = createEditText("姓名");
        EditText etPassword = createEditText("密码");
        EditText etRole = createEditText("角色 (admin/user)");

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button btnSave = new Button(this);
        btnSave.setText("添加用户");
        btnSave.setBackgroundColor(getResources().getColor(R.color.blue));
        btnSave.setTextColor(getResources().getColor(R.color.white));
        btnSave.setTextSize(18);
        btnSave.setLayoutParams(new LinearLayout.LayoutParams(0, 60, 1));
        btnSave.setPadding(0, 15, 0, 15);

        Button btnCancel = new Button(this);
        btnCancel.setText("查看用户列表");
        btnCancel.setBackgroundColor(getResources().getColor(R.color.gray));
        btnCancel.setTextColor(getResources().getColor(R.color.black));
        btnCancel.setTextSize(18);
        btnCancel.setLayoutParams(new LinearLayout.LayoutParams(0, 60, 1));
        btnCancel.setPadding(0, 15, 0, 15);

        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = etRole.getText().toString().trim();

            if (username.isEmpty() || name.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!role.equals("admin") && !role.equals("user")) {
                Toast.makeText(this, "角色只能是 admin 或 user", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = String.valueOf(System.currentTimeMillis());
            User user = new User(userId, username, password, role, name);

            if (UserData.addUser(user)) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                clearFields(etUsername, etName, etPassword, etRole);
            } else {
                Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> showUserList());

        buttonLayout.addView(btnSave);
        buttonLayout.addView(btnCancel);

        llContent.addView(etUsername);
        llContent.addView(etName);
        llContent.addView(etPassword);
        llContent.addView(etRole);
        llContent.addView(buttonLayout);
    }

    private void showUserList() {
        llContent.removeAllViews();

        TextView title = new TextView(this);
        title.setText("用户列表");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        llContent.addView(title);

        Button btnAddUser = new Button(this);
        btnAddUser.setText("添加新用户");
        btnAddUser.setBackgroundColor(getResources().getColor(R.color.blue));
        btnAddUser.setTextColor(getResources().getColor(R.color.white));
        btnAddUser.setTextSize(16);
        btnAddUser.setPadding(0, 10, 0, 10);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 50);
        btnParams.setMargins(0, 0, 0, 20);
        btnAddUser.setLayoutParams(btnParams);
        btnAddUser.setOnClickListener(v -> showAddUserForm());
        llContent.addView(btnAddUser);

        List<User> users = UserData.getAllUsers();

        for (User user : users) {
            LinearLayout userItem = new LinearLayout(this);
            userItem.setOrientation(LinearLayout.VERTICAL);
            userItem.setPadding(15, 15, 15, 15);
            userItem.setBackgroundColor(getResources().getColor(R.color.white));

            LinearLayout infoLayout = new LinearLayout(this);
            infoLayout.setOrientation(LinearLayout.VERTICAL);

            TextView tvUsername = new TextView(this);
            tvUsername.setText("用户名: " + user.getUsername());
            tvUsername.setTextSize(16);
            tvUsername.setTextColor(getResources().getColor(R.color.black));

            TextView tvName = new TextView(this);
            tvName.setText("姓名: " + user.getName());
            tvName.setTextSize(14);
            tvName.setTextColor(getResources().getColor(R.color.gray_text));

            TextView tvRole = new TextView(this);
            tvRole.setText("角色: " + (user.isAdmin() ? "管理员" : "普通用户"));
            tvRole.setTextSize(14);
            tvRole.setTextColor(user.isAdmin() ?
                    getResources().getColor(R.color.red) : getResources().getColor(R.color.blue));

            infoLayout.addView(tvUsername);
            infoLayout.addView(tvName);
            infoLayout.addView(tvRole);

            LinearLayout buttonLayout = new LinearLayout(this);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setPadding(0, 10, 0, 0);

            Button btnEdit = new Button(this);
            btnEdit.setText("编辑");
            btnEdit.setBackgroundColor(getResources().getColor(R.color.blue));
            btnEdit.setTextColor(getResources().getColor(R.color.white));
            btnEdit.setTextSize(14);
            btnEdit.setPadding(15, 8, 15, 8);
            LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            editParams.setMarginEnd(10);
            btnEdit.setLayoutParams(editParams);

            Button btnDelete = new Button(this);
            btnDelete.setText("删除");
            btnDelete.setTextColor(getResources().getColor(R.color.white));
            btnDelete.setTextSize(14);
            btnDelete.setPadding(15, 8, 15, 8);
            if (user.isAdmin()) {
                btnDelete.setBackgroundColor(getResources().getColor(R.color.gray));
                btnDelete.setEnabled(false);
            } else {
                btnDelete.setBackgroundColor(getResources().getColor(R.color.red));
                btnDelete.setEnabled(true);
            }
            LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            deleteParams.setMarginStart(10);
            btnDelete.setLayoutParams(deleteParams);

            btnEdit.setOnClickListener(v -> showEditUserForm(user));
            btnDelete.setOnClickListener(v -> deleteUser(user.getId()));

            buttonLayout.addView(btnEdit);
            buttonLayout.addView(btnDelete);

            userItem.addView(infoLayout);
            userItem.addView(buttonLayout);

            llContent.addView(userItem);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            divider.setBackgroundColor(getResources().getColor(R.color.light_gray));
            llContent.addView(divider);
        }
    }

    private void showEditUserForm(User user) {
        llContent.removeAllViews();

        TextView title = new TextView(this);
        title.setText("编辑用户");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        llContent.addView(title);

        EditText etId = createEditText("用户ID (不可修改)");
        etId.setText(user.getId());
        etId.setEnabled(false);

        EditText etUsername = createEditText("用户名");
        etUsername.setText(user.getUsername());

        EditText etName = createEditText("姓名");
        etName.setText(user.getName());

        EditText etPassword = createEditText("密码");
        etPassword.setText(user.getPassword());

        EditText etRole = createEditText("角色 (admin/user)");
        etRole.setText(user.getRole());

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button btnSave = new Button(this);
        btnSave.setText("保存修改");
        btnSave.setBackgroundColor(getResources().getColor(R.color.blue));
        btnSave.setTextColor(getResources().getColor(R.color.white));
        btnSave.setTextSize(16);
        btnSave.setLayoutParams(new LinearLayout.LayoutParams(0, 50, 1));
        btnSave.setPadding(0, 10, 0, 10);

        Button btnCancel = new Button(this);
        btnCancel.setText("取消");
        btnCancel.setBackgroundColor(getResources().getColor(R.color.gray));
        btnCancel.setTextColor(getResources().getColor(R.color.black));
        btnCancel.setTextSize(16);
        btnCancel.setLayoutParams(new LinearLayout.LayoutParams(0, 50, 1));
        btnCancel.setPadding(0, 10, 0, 10);

        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = etRole.getText().toString().trim();

            if (username.isEmpty() || name.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!role.equals("admin") && !role.equals("user")) {
                Toast.makeText(this, "角色只能是 admin 或 user", Toast.LENGTH_SHORT).show();
                return;
            }

            User updatedUser = new User(user.getId(), username, password, role, name);

            if (UserData.updateUser(updatedUser)) {
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                showUserList();
            } else {
                Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> showUserList());

        buttonLayout.addView(btnSave);
        buttonLayout.addView(btnCancel);

        llContent.addView(etId);
        llContent.addView(etUsername);
        llContent.addView(etName);
        llContent.addView(etPassword);
        llContent.addView(etRole);
        llContent.addView(buttonLayout);
    }

    private void deleteUser(String userId) {
        if (UserData.deleteUser(userId)) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            showUserList();
        } else {
            Toast.makeText(this, "管理员无法删除", Toast.LENGTH_SHORT).show();
        }
    }

    private EditText createEditText(String hint) {
        EditText et = new EditText(this);
        et.setHint(hint);
        et.setBackgroundResource(R.drawable.edit_text_bg);
        et.setTextSize(16);
        et.setPadding(15, 15, 15, 15);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 15);
        et.setLayoutParams(params);
        return et;
    }

    private void clearFields(EditText... fields) {
        for (EditText field : fields) {
            field.setText("");
        }
    }
}