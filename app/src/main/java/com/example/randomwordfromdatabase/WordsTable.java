package com.example.randomwordfromdatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Promlert on 10/3/2015.
 */
public class WordsTable {

    private static final String DATABASE_NAME = "eng_thai.db";
    private static final int DATABASE_VERSION = 1;

    protected static final String TABLE_NAME = "words";
    protected static final String COL_ID = "_id";
    protected static final String COL_ENG_WORD = "eng_word";
    protected static final String COL_THAI_WORD = "thai_word";

    private static final String SQL_CREATE_TABLE_WORDS =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_ENG_WORD + " TEXT, "
                    + COL_THAI_WORD + " TEXT)";

    private Context mContext;
    private static DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    public WordsTable(Context context) {
        this.mContext = context;

        if (mHelper == null) {
            mHelper = new DatabaseHelper(context);
        }
        mDatabase = mHelper.getWritableDatabase();
    }

    // returns Thai word if exists, otherwise returns English word passed in
    public String getThaiWordFromEngWord(String englishWord) {
        Cursor cursor = mDatabase.query(
                TABLE_NAME,
                new String[]{COL_THAI_WORD},
                COL_ENG_WORD + "=?",
                new String[]{englishWord},
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COL_THAI_WORD));
        } else {
            return englishWord;
        }
    }

    // returns English word if exists, otherwise returns Thai word passed in
    public String getEngWordFromThaiWord(String thaiWord) {
        Cursor cursor = mDatabase.query(
                TABLE_NAME,
                new String[]{COL_ENG_WORD},
                COL_THAI_WORD + "=?",
                new String[]{thaiWord},
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COL_ENG_WORD));
        } else {
            return thaiWord;
        }
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE_WORDS);

            insertInitialData(db);
        }

        private void insertInitialData(SQLiteDatabase db) {
            Map<String, String> wordMap = new HashMap<>();

            wordMap.put("cat", "แมว");
            wordMap.put("dog", "หมา");
            wordMap.put("dolphin", "โลมา");
            wordMap.put("koala", "โคอาล่า");
            wordMap.put("lion", "สิงโต");
            wordMap.put("owl", "นกฮูก");
            wordMap.put("penguin", "เพนกวิน");
            wordMap.put("pig", "หมู");
            wordMap.put("rabbit", "กระต่าย");
            wordMap.put("tiger", "เสือ");

            wordMap.put("arm", "แขน");
            wordMap.put("ear", "หู");
            wordMap.put("eye", "ตา");
            wordMap.put("foot", "เท้า");
            wordMap.put("hair", "ผม");
            wordMap.put("hand", "มือ");
            wordMap.put("head", "หัว");
            wordMap.put("mouth", "ปาก");
            wordMap.put("nose", "จมูก");
            wordMap.put("thumb", "นิ้วโป้ง");



            /**************************************************
             * เพิ่มคำศัพท์ในหมวดอื่นๆให้ครบ
             **************************************************/



            StringBuilder sb = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(TABLE_NAME)
                    .append("\n");

            boolean firstEntry = true;

            // loops through wordMap to create 'INSERT INTO' string that inserts multiple rows
            // on one execSQL() call.
            for (Map.Entry<String, String> entry : wordMap.entrySet()) {
                String engWord = entry.getKey();
                String thaiWord = entry.getValue();

                if (firstEntry) {
                    sb.append("SELECT ")
                            .append("null")
                            .append(" AS ")
                            .append(COL_ID)
                            .append(", '")
                            .append(engWord)
                            .append("' AS ")
                            .append(COL_ENG_WORD)
                            .append(", '")
                            .append(thaiWord)
                            .append("' AS ")
                            .append(COL_THAI_WORD)
                            .append("\n");
                    firstEntry = false;
                } else {
                    sb.append("UNION ALL SELECT null, '")
                            .append(engWord)
                            .append("'")
                            .append(", '")
                            .append(thaiWord)
                            .append("'")
                            .append("\n");
                }
            }
            db.execSQL(sb.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            return;
        }
    }
}
