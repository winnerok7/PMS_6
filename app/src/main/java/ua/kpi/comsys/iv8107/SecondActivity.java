package ua.kpi.comsys.iv8107;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        TabLayout tabsDrawing = findViewById(R.id.tabsDraw);
        outState.putInt("tabsDrawingState", tabsDrawing.getSelectedTabPosition());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TabLayout tabs = findViewById(R.id.tabs);
        TabLayout tabsDrawing = findViewById(R.id.tabsDraw);
        DrawingView drawingView = findViewById(R.id.drawing);
        tabs.getTabAt(1).select();
        if (savedInstanceState != null){
            int tabDrawingId = savedInstanceState.getInt("tabsDrawingState");
            tabsDrawing.getTabAt(tabDrawingId).select();
            drawingView.setNumberOfDrawing(tabDrawingId);
            drawingView.invalidate();
        }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 2) {
                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 3) {
                    Intent intent = new Intent(SecondActivity.this, FourthActivity.class);
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
        tabsDrawing.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    drawingView.setNumberOfDrawing(0);
                    drawingView.invalidate();
                } else {
                    drawingView.setNumberOfDrawing(1);
                    drawingView.invalidate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}