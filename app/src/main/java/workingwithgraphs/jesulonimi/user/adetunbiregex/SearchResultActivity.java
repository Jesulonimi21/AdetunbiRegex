package workingwithgraphs.jesulonimi.user.adetunbiregex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static workingwithgraphs.jesulonimi.user.adetunbiregex.MainActivity.bibleList;

public class SearchResultActivity extends AppCompatActivity {
    int count = 0;
    TextView countTextView;
    String state = "allText";
    TextView searchResultTextView;
    Button onlyOneType;
    String searchWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchResultTextView = findViewById(R.id.search_result);
        countTextView = findViewById(R.id.total_count);
        onlyOneType=findViewById(R.id.only_chapters);
        searchWord=getIntent().getStringExtra("searchWord");
        String text = "";
        for (BibleModel bibleModel1 : bibleList) {
            text += bibleModel1.text;
            text += "\n";
        }
        searchResultTextView.setText(text);
        count = getIntent().getIntExtra("count", 0);
        countTextView.setText("The total number of search results for "+searchWord +" " + count);
    }

    public void seeOnlyChapters(View v) {
        String text = "";
        if (state.equals("allText")) {
            for (BibleModel bibleModel1 : bibleList) {
                text += bibleModel1.bibleChapter;
                text += "\n";
            }
            searchResultTextView.setText(text);
            state = "allChapters";
            onlyOneType.setText("See only bible verses");
        } else {
            for (BibleModel bibleModel1 : bibleList) {
                text += bibleModel1.text;
                text += "\n";
            }
            searchResultTextView.setText(text);
            state = "allText";
            onlyOneType.setText("See only bible Chapters");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bibleList.clear();
    }
}

