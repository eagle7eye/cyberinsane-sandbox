package com.cyberinsane.pocollections;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cyberinsane.pocollections.customview.BasicCustomView;
import com.cyberinsane.pocollections.customview.BobbleHeadActivity;

public class MainActivity extends Activity {

    ListView mListView;
    Object[] mModules = { new BasicCustomView(), new BobbleHeadActivity() };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ClassAdapter(this, android.R.layout.simple_list_item_1));

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent();
                String defaultPackage = this.getClass().getPackage().getName();
                String activity = mModules[arg2].getClass().getCanonicalName();
                intent.setComponent(new ComponentName(defaultPackage, activity));
                startActivity(intent);

            }
        });
    }

    private class ClassAdapter extends ArrayAdapter<Object> {

        public ClassAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return mModules.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView rowView = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            rowView.setText(mModules[position].getClass().getSimpleName());
            return rowView;
        }
    }
}
