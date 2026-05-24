package com.example.book;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorageUtil {
    private static final String FILE_NAME = "borrow_records.json";
    private static Gson gson = new Gson();

    public static void saveBorrowRecords(Context context, List<BorrowRecord> records) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            FileWriter writer = new FileWriter(file);
            gson.toJson(records, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<BorrowRecord> loadBorrowRecords(Context context) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                Type type = new TypeToken<List<BorrowRecord>>() {}.getType();
                List<BorrowRecord> records = gson.fromJson(reader, type);
                reader.close();
                return records != null ? records : new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}