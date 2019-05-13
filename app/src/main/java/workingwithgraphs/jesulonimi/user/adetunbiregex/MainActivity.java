package workingwithgraphs.jesulonimi.user.adetunbiregex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    TextView bibleText;
    public static List<BibleModel> bibleList=new ArrayList<>();
    String globalReadText;
    TextView searchEditText;
    int openBibleClicked =0;
    int searchClicked=0;
    NestedScrollView nestedScrollView;
    int moveCount = 0;
    int count = 0;
    int lineCount = 0;
    ProgressBar progress;
    List<Integer> lineNumber;
    //    Pattern p=Pattern.compile("\\d+:\\d+");
    Pattern p = Pattern.compile("\\.");
    Pattern beginPattern=Pattern.compile("\\w+\\s*\\d+\\s*:\\s*\\d+");


    Pattern newP=Pattern.compile(".");
    Matcher matcher;
    Matcher searchMatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = findViewById(R.id.progressBar);
        searchEditText = findViewById(R.id.edittext_search);
        nestedScrollView = findViewById(R.id.nested_scrollview);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        bibleText = findViewById(R.id.bibleText);
    }

    public void readBibleText(View v) {
        if(openBibleClicked ==1){
            Toast.makeText(this, "Loading,please be patient ", Toast.LENGTH_LONG).show();
            return;
        }
        openBibleClicked =1;

        final FileInputStream[] fileInputStream = new FileInputStream[1];
        final String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ylt.txt";
        progress.setVisibility(View.VISIBLE);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                bibleText.setText(globalReadText);
                progress.setVisibility(View.INVISIBLE);
            }
        };


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        File file = null;
                        try {
                            file = new File(baseDir);
                            fileInputStream[0] = new FileInputStream(file);
                            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream[0]);

                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            StringBuilder stringBuilder = new StringBuilder();
                            String text;
                            while ((text = bufferedReader.readLine()) != null) {
                                StringBuilder buer = new StringBuilder();
                                matcher = p.matcher(text);
                                buer.append(text);
                                Log.d("nimiDebug", text);
                                Boolean checkRegex = matcher.find();
                                Log.d("nimiBoolean", checkRegex + "");
                                if (checkRegex) {
                                    Log.d("nimiDebug", matcher.start() + "nbj");
                                    int value = matcher.start()+1;
                                    buer.insert(value, "\n\n");
                                    text = buer.toString();
                                }
                                stringBuilder.append(text);
                            }
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("key", stringBuilder.toString());
                            msg.setData(bundle);
                            globalReadText = stringBuilder.toString();
                            handler.sendMessage(msg);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.d("nimiDebug", e.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("nimiDebug", e.toString());
                        } finally {

                            try {
                                fileInputStream[0].close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }



    public void searchForWord(View v){
                    if(searchClicked==1){
                        Toast.makeText(this, "Your results are loading, please be patient", Toast.LENGTH_LONG).show();
                        return;
                    }
                    searchClicked=1;

        bibleList.clear();
        progress.setVisibility(View.VISIBLE);
        final String searchWord=searchEditText.getText().toString();
        final String unknownSearch=searchWord.trim();
        if(unknownSearch.equals("")){
            Toast.makeText(this, "put a value in the edittext", Toast.LENGTH_SHORT).show();
            return;
        }
        final FileInputStream[] fileInputStream = new FileInputStream[1];
        final String baseDir=Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"ylt.txt";

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.setVisibility(View.INVISIBLE);
            }
        };


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        lineNumber=new ArrayList<>();
                        File file=null;
                        try {
                            file=new File(baseDir);
                            fileInputStream[0] =new FileInputStream(file);
                            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream[0]);

                            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                            StringBuilder stringBuilder=new StringBuilder();
                            String text;
                            while((text=bufferedReader.readLine())!=null){
                                lineCount++;
                                Log.d("nimiDebug",lineCount+"normal"+"\n");
                                StringBuilder buer=new StringBuilder();
                                matcher=newP.matcher(text);
                                searchMatcher=beginPattern.matcher(text);
                                buer.append(text);
                                Boolean checkRegex=matcher.find();
                                if(checkRegex){

                                    int value=matcher.start();
                                    buer.insert(value,"\n");
                                    text=buer.toString();
                                }

                                String[] word=text.split(" ");

//                                for(String words:word){
//                                    Log.d("nimiDebug",words);
//                                    if(words.equals(unknownSearch)){
//                                        count++;
//                                        lineNumber.add(lineCount);
//                                        Log.d("searchFound",lineCount+ words);
//                                        String beginVerse="";
//                                        String backUpText="";
//                                        if(searchMatcher.find()){
//                                            int beginVerseNum=searchMatcher.start();
//                                            int beginVerseEnd=searchMatcher.end();
//                                             beginVerse=text.substring(beginVerseNum,++beginVerseEnd);
//                                            Log.d("beginVerse",beginVerse);}
//                                        }else{
//                                            searchMatcher=beginPattern.matcher(text);
//                                            if(searchMatcher.find()){
//                                                int beginVerseNum=searchMatcher.start();
//                                                int beginVerseEnd=searchMatcher.end();
//                                                beginVerse=text.substring(beginVerseNum,++beginVerseEnd);
//                                                Log.d("beginVerse",beginVerse);
//                                            }
//                                        }
//                                        BibleModel bibleModel=new BibleModel(beginVerse,lineCount,text);
//                                        bibleList.add(bibleModel);
//                                        for(BibleModel bibleModel1:bibleList){
//                                            Log.d("beginVerse",bibleModel1.toString());
//                                        }
//                                        backUpText=text;
//
//                                    }

                                //}
                                if(returnMatchingPaattern(text,unknownSearch)){
                                    count++;
                                    lineNumber.add(lineCount);
//                                    Log.d("searchFound",lineCount+ words);
                                    String beginVerse="";
                                    String backUpText="";
                                    if(searchMatcher.find()){
                                        int beginVerseNum=searchMatcher.start();
                                        int beginVerseEnd=searchMatcher.end();
                                        beginVerse=text.substring(beginVerseNum,++beginVerseEnd);
                                        Log.d("beginVerse",beginVerse);}
//                                        }else{
//                                            searchMatcher=beginPattern.matcher(text);
//                                            if(searchMatcher.find()){
//                                                int beginVerseNum=searchMatcher.start();
//                                                int beginVerseEnd=searchMatcher.end();
//                                                beginVerse=text.substring(beginVerseNum,++beginVerseEnd);
//                                                Log.d("beginVerse",beginVerse);
//                                            }
//                                        }
                                    BibleModel bibleModel=new BibleModel(beginVerse,lineCount,text);
                                    bibleList.add(bibleModel);
                                }
                                stringBuilder.append(text);
                            }
                          for(int i:lineNumber){
                                Log.d("myDebug",i+"vsljijij");
                          }
                            globalReadText=stringBuilder.toString();


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.d("nimiDebug",e.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("nimiDebug",e.toString());
                        }finally{

                            try {
                                Message msg=new Message();
                                handler.sendMessage(msg);
                                fileInputStream[0].close();
                                Intent intent=new Intent(MainActivity.this,SearchResultActivity.class);
                                intent.putExtra("count",count);
                                count=0;
                                searchClicked =0;
                                intent.putExtra("searchWord",unknownSearch);

                                startActivity(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("errorDebug",e.toString());
                            }
                        }
                    }
                }
        ).start();
    }

//    public void moveToText(View v){
//        moveCount++;
//        if(moveCount<lineNumber.size()){
//            bibleText.getLayout().getLineStart(lineNumber.get(moveCount));
//                nestedScrollView.post(
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                  nestedScrollView.scrollTo(moveCount,lineNumber.get(moveCount));
//                                Toast.makeText(MainActivity.this, "movd", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                );
//        }else{
//            Toast.makeText(this, "end of search length reached", Toast.LENGTH_SHORT).show();
//        }
//    }

    public boolean returnMatchingPaattern(String sentence,String searchWord){
        Pattern searchPattern=Pattern.compile("\\w*"+searchWord+"\\w*");
        Pattern newPattern=Pattern.compile("\\w+\\s*\\d+\\s*:\\s*\\d+");

//            Matcher newMatcher=newPattern.matcher(sentence);
//            String newSentence=sentence;
//                 if(newMatcher.find()){
//                     int beginVerseNum=searchMatcher.start();
//                     int beginVerseEnd=searchMatcher.end();
        String newSentence=sentence;
        Log.d("newSentence",newSentence);
        newSentence=newSentence.replaceAll("\\w+\\s*\\d+\\s*:\\s*\\d+","");


        Log.d("newSentence",newSentence);
        Matcher searchingMatcher=searchPattern.matcher(newSentence);
        if(searchingMatcher.find()){
//            int beCount=searchingMatcher.end()+2;
//            Character stringAt= sentence.charAt(beCount);
//                if(searchingMatcher.groupCount()>1&&stringAt.toString().equals(":")){
//                    return false;
//                }
            return true;
        }

        return false;
    }
}
