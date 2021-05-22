package ua.kpi.comsys.iv8107;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FourthActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceType")
    ArrayList<ImageView> IMGs = new ArrayList<ImageView>();
    ArrayList<String> URIs = new ArrayList<String>();
    Drawable[] drawables;
    ConstraintLayout constraintLayout;
    ConstraintSet set = new ConstraintSet();

    ProgressBar progressBar;

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        String[] URIsArray = new String[URIs.size()];
        for (int i = 0; i < URIs.size(); i++){
            URIsArray[i] = URIs.get(i);
        }
        outState.putStringArray("URIs", URIsArray);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.getTabAt(3).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    Intent intent = new Intent(FourthActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 1) {
                    Intent intent = new Intent(FourthActivity.this, SecondActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 2) {
                    Intent intent = new Intent(FourthActivity.this, ThirdActivity.class);
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

        constraintLayout = findViewById(R.id.constraint);
        progressBar = findViewById(R.id.progressBar);

        new JsonTask().execute();
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
                URL url = new URL(
                        "https://pixabay.com/api/?key=19193969-87191e5db266905fe8936d565&q=hot+summer&image_type=photo&per_page=24"
                );

                connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                }
                connection.disconnect();
                try {
                    JSONObject jsonReslt = new JSONObject(buffer.toString());
                    JSONArray jsonImgArray = jsonReslt.getJSONArray("hits");
                    drawables = new Drawable[jsonImgArray.length()];
                    for (int i = 0; i < jsonImgArray.length(); i++){
                        URL urlImage = new URL(jsonImgArray.getJSONObject(i).getString("previewURL"));
                        connection = (HttpURLConnection) urlImage.openConnection();
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

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            for (Drawable i : drawables){
                drawFromDrawable(i);
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                Uri slctdImage = data.getData();
                URIs.add(slctdImage.toString());
                drawFromUri(slctdImage);
            }
        }
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void drawFromDrawable(Drawable drawable){
        IMGs.add(new ImageView(this));
        int size = IMGs.size();
        ImageView img = IMGs.get(size - 1);
        img.setImageDrawable(drawable);
        img.setId(View.generateViewId());
        if (size % 8 == 1){
            constraintLayout.addView(img, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            if (size > 8){
                set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 2).getId(), ConstraintSet.BOTTOM);}
        }
        if (size % 8 == 2){
            constraintLayout.addView(img, getDisplay().getWidth() * 3 / 4, getDisplay().getWidth() * 3 / 4);
            set.clone(constraintLayout);
            set.connect(img.getId(), ConstraintSet.LEFT, IMGs.get(size - 2).getId(), ConstraintSet.RIGHT);
            set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 2).getId(), ConstraintSet.TOP);
        }
        if (size % 8 == 3){
            constraintLayout.addView(img, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 3).getId(), ConstraintSet.BOTTOM);
        }
        if (size % 8 == 4 || size % 8 == 5){
            constraintLayout.addView(img, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 2).getId(), ConstraintSet.BOTTOM);
        }
        if (size % 8 == 0 || size % 8 >= 6){
            constraintLayout.addView(img, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 2).getId(), ConstraintSet.TOP);
            set.connect(img.getId(), ConstraintSet.LEFT, IMGs.get(size - 2).getId(), ConstraintSet.RIGHT);
        }
        set.applyTo(constraintLayout);
    }
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void drawFromUri(Uri selectedImage){
        IMGs.add(new ImageView(this));
        int size = IMGs.size();
        ImageView img = IMGs.get(size - 1);
        img.setImageURI(selectedImage);
        img.setId(View.generateViewId());
        if (size % 8 == 1){
            constraintLayout.addView(img, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            if (size > 8){
                set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 2).getId(), ConstraintSet.BOTTOM);}
        }
        if (size % 8 == 2){
            constraintLayout.addView(img, getDisplay().getWidth() * 3 / 4, getDisplay().getWidth() * 3 / 4);
            set.clone(constraintLayout);
            set.connect(img.getId(), ConstraintSet.LEFT, IMGs.get(size - 2).getId(), ConstraintSet.RIGHT);
            set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 2).getId(), ConstraintSet.TOP);
        }
        if (size % 8 == 3){
            constraintLayout.addView(img, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 3).getId(), ConstraintSet.BOTTOM);
        }
        if (size % 8 == 4 || size % 8 == 5){
            constraintLayout.addView(img, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 2).getId(), ConstraintSet.BOTTOM);
        }
        if (size % 8 == 0 || size % 8 >= 6){
            constraintLayout.addView(img, getDisplay().getWidth() / 4, getDisplay().getWidth() / 4);
            set.clone(constraintLayout);
            set.connect(img.getId(), ConstraintSet.TOP, IMGs.get(size - 2).getId(), ConstraintSet.TOP);
            set.connect(img.getId(), ConstraintSet.LEFT, IMGs.get(size - 2).getId(), ConstraintSet.RIGHT);
        }
        set.applyTo(constraintLayout);
    }
}
