package com.example.randomwordfromdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WordsTable mWordsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWordsTable = new WordsTable(this);

        Button engToThaiButton = (Button) findViewById(R.id.eng_to_thai_button);
        engToThaiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView engWordTextView = (TextView) findViewById(R.id.eng_word_textview);
                String engWord = engWordTextView.getText().toString();

                String thaiWord = mWordsTable.getThaiWordFromEngWord(engWord);
                TextView thaiWordTextView = (TextView) findViewById(R.id.thai_word_textview);
                thaiWordTextView.setText(thaiWord);
            }
        });
    }

}
