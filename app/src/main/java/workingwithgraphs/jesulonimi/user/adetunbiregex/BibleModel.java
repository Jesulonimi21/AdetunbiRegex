package workingwithgraphs.jesulonimi.user.adetunbiregex;

public class BibleModel {
    String bibleChapter;
    int lineCount;
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BibleModel(String bibleChapter, int lineCount,String text) {
        this.bibleChapter = bibleChapter;
        this.lineCount = lineCount;
        this.text=text;
    }

    public String getBibleChapter() {
        return bibleChapter;
    }

    public void setBibleChapter(String bibleChapter) {
        this.bibleChapter = bibleChapter;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
        }

        @Override
    public String toString(){
        return this.text;
        }
}
