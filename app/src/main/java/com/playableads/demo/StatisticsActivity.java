package com.playableads.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/9/26.
 */

public class StatisticsActivity extends ToolBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        listView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(listView);
        showUpAction();

        SharedPreferences sharedPreferences = getSharedPreferences("zplay.com.a", Context.MODE_PRIVATE);

        Set<String> tracksName = sharedPreferences.getStringSet("trakings", new HashSet<String>());
        ArrayList<String> items = new ArrayList<>(10);
        for (String name : tracksName) {
            items.add(String.format(Locale.CHINA, "%s : %d", name, sharedPreferences.getInt(name, 0)));
        }
        Collections.sort(items);
        Collections.reverse(items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.string_array_item, items);

        listView.setAdapter(adapter);
    }

    public static void launch(Context context) {
        Intent i = new Intent(context, StatisticsActivity.class);
        context.startActivity(i);
    }
}
