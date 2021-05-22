package ua.kpi.comsys.iv8107;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import org.json.*;
import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ThirdActivity extends AppCompatActivity {

    String jsonBooksLoadedString;
    ArrayList<Book> booksFilter;
    TableRow[] tblRows;
    ImageView[] imgOfBooks;
    TextView[] infOfBooks;
    Drawable[] drawables;
    TableLayout table;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.getTabAt(2).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 1) {
                    Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 3) {
                    Intent intent = new Intent(ThirdActivity.this, FourthActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        table = findViewById(R.id.table);
        progressBar = findViewById(R.id.progressBar);
        SearchView search = findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                booksFilter = new ArrayList<Book>();
                if (newText.length() >= 3){
                    for (char i : newText.toCharArray()){
                        if (!Character.isLetterOrDigit(i)){
                            drawInfoAboutIllegalCharacter(i);
                            return false;
                        }
                    }
                    JsonTask task = new JsonTask();
                    task.execute(newText);
                } else {
                    table.removeAllViews();
                }
                return false;
            }
        });
    }

    private void drawInfoAboutIllegalCharacter(char i){
        table.removeAllViews();
        TableRow tblRowNoBooks = new TableRow(this);
        TextView txtViewNoBooks = new TextView(this);
        txtViewNoBooks.setText("Illegal character (" + i + "). Use only latin letters and digits.");
        tblRowNoBooks.addView(txtViewNoBooks);
        table.addView(tblRowNoBooks);
    }

    private class JsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://api.itbook.store/1.0/search/" + params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                }
                connection.disconnect();
                try {
                    JSONObject jsonBooksLoadedObject = new JSONObject(buffer.toString());
                    JSONArray jsonBooksLoadedArray = jsonBooksLoadedObject.getJSONArray("books");
                    drawables = new Drawable[jsonBooksLoadedArray.length()];
                    for (int i = 0; i < jsonBooksLoadedArray.length(); i++){
                        JSONObject book = jsonBooksLoadedArray.getJSONObject(i);
                        booksFilter.add(new Book(
                                book.getString("title"),
                                book.getString("subtitle"),
                                book.getString("isbn13"),
                                book.getString("price"),
                                book.getString("image")
                        ));
                        url = new URL(book.getString("image"));
                        connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        drawables[i] = Drawable.createFromStream(connection.getInputStream(), null);
                        connection.disconnect();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            jsonBooksLoadedString = result;
            tblRows = new TableRow[booksFilter.size()];
            infOfBooks = new TextView[booksFilter.size()];
            imgOfBooks = new ImageView[booksFilter.size()];
            redrawTable(booksFilter, tblRows, imgOfBooks, infOfBooks, table, booksFilter);
            progressBar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void redrawTable(
            ArrayList<Book> booksArrayFiltered, TableRow[] tblRows, ImageView[] imgOfBooks, TextView[] infOfBooks,
            TableLayout table, ArrayList<Book> booksArrayUnfiltered
    ){
        table.removeAllViews();
        tblRows = new TableRow[booksArrayFiltered.size()];
        infOfBooks = new TextView[booksArrayFiltered.size()];
        imgOfBooks = new ImageView[booksArrayFiltered.size()];
        Drawable[] drawables = new Drawable[booksArrayFiltered.size()];
        if (this.drawables != null) {
            for (int i = 0; i < Math.min(this.drawables.length, drawables.length); i++) {
                drawables[i] = this.drawables[i];
            }
        }
        for (int i = 0; i < booksArrayFiltered.size(); i++) {
            tblRows[i] = new TableRow(this);
            tblRows[i].setPadding(10, 10, 10, 10);
            imgOfBooks[i] = this.createBookImage(booksArrayFiltered.get(i), drawables[i]);
            tblRows[i].addView(imgOfBooks[i]);
            infOfBooks[i] = new TextView(this);
            infOfBooks[i].setText(
                    booksArrayFiltered.get(i).getTtl() + "\n" + booksArrayFiltered.get(i).getSbttl() + "\n" +
                            booksArrayFiltered.get(i).getIsbn13() + "\n" + booksArrayFiltered.get(i).getPrice()
            );
            infOfBooks[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            infOfBooks[i].setPadding(10, 10, 10, 10);
            tblRows[i].addView(infOfBooks[i]);
            String isbn13 = booksArrayFiltered.get(i).getIsbn13();
            TableRow tblRow = tblRows[i];
            Book books = booksArrayFiltered.get(i);
            tblRows[i].setOnTouchListener(new SwipeTouchDetect(ThirdActivity.this) {
                @Override
                public void onClick() {
                    Intent intent = new Intent(ThirdActivity.this, InfoOfBook.class);
                    intent.putExtra("id", isbn13);
                    startActivity(intent);
                }

                @Override
                public void onSwipeLeft() {
                    swipeLeftMethod(table, tblRow, booksArrayUnfiltered, booksArrayFiltered, books);
                }
            });
            table.addView(tblRows[i]);
        }
    }

    private Book[] loadBookArray() {
        String str = "";
        JSONObject booksJson = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(openFileInput("BooksListUser.txt"));
            booksJson = new JSONObject((String) inputStream.readObject());
        }
        catch (IOException e) {
            try {
                InputStream inputStream = getAssets().open("BooksList.txt");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);

                str = new String(buffer);
                booksJson = new JSONObject(str);
                ObjectOutputStream stream = new ObjectOutputStream(openFileOutput("BooksListUser.txt", MODE_PRIVATE));
                stream.writeObject(booksJson.toString());
                stream.close();
            } catch (IOException | JSONException e2) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Book[] books = new Book[0];
        try {
            JSONArray booksJsonArray = booksJson.getJSONArray("books");
            books = new Book[booksJsonArray.length()];
            JSONObject book;
            for (int i = 0; i < booksJsonArray.length(); i++) {
                book = booksJsonArray.getJSONObject(i);
                books[i] = new Book(
                        book.getString("title"),
                        book.getString("subtitle"),
                        book.getString("isbn13"),
                        book.getString("price"),
                        book.getString("image")
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    private ImageView createBookImage(Book book, Drawable drawable) {
    ImageView bookImage = new ImageView(this);
        bookImage.setImageDrawable(drawable);
        bookImage.setContentDescription(book.getTtl());
        bookImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bookImage.setMinimumWidth(200);
        bookImage.setMinimumHeight(200);
        bookImage.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        return bookImage;
    }

    private void swipeLeftMethod(TableLayout table,
                                 TableRow tableRow,
                                 ArrayList<Book> booksArrayUnfiltered,
                                 ArrayList<Book> booksArrayFiltered,
                                 Book book){
        table.removeView(tableRow);
        booksArrayUnfiltered.remove(book);
        booksArrayFiltered.remove(book);
        JSONArray booksJsonArray = new JSONArray();
        for (int j = 0; j < booksArrayUnfiltered.size(); j++) {
            try {
                booksJsonArray.put(new JSONObject().
                        put("title", booksArrayUnfiltered.get(j).getTtl()).
                        put("subtitle", booksArrayUnfiltered.get(j).getSbttl()).
                        put("isbn13", booksArrayUnfiltered.get(j).getIsbn13()).
                        put("price", booksArrayUnfiltered.get(j).getPrice()).
                        put("image", booksArrayUnfiltered.get(j).getImage()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            JSONObject booksJson = new JSONObject().put("books", booksJsonArray);
            ObjectOutputStream stream = new ObjectOutputStream(openFileOutput("BooksListUser.txt", MODE_PRIVATE));
            stream.writeObject(booksJson.toString());
            stream.close();
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

