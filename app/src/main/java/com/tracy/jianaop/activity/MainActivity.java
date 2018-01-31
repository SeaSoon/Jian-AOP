package com.tracy.jianaop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tracy.jianaop.R;

import java.util.Arrays;


public class MainActivity extends Activity {

    final Item[] items = new Item[]{
            new Item("fragments ", FragmentActivity.class),
            new Item("ListView", ListViewActivity.class),
            new Item("RecyclerView", RecyclerViewActivity.class),
            new Item("ViewPager", ViewPagerActivity.class),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView itemListView = findViewById(R.id.itemList);
        ArrayAdapter<Item> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arrays.asList(items));
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener((parent, view, position, id) -> {
            startActivity(new Intent(MainActivity.this, items[position].Activity));
        });
    }

    class Item {
        public String name;
        public Class Activity;

        public Item(String name, Class activity) {
            this.name = name;
            Activity = activity;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}