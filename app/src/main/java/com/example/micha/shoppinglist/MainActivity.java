package com.example.micha.shoppinglist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.Image;
import android.os.AsyncTask;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> elements;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        elements = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.MyRecylerView);
        mAdapter = new MyAdapter(elements);
        mRecyclerView.setAdapter(mAdapter);

        ImageButton add_button = (ImageButton) findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = elements.size();
                elements.add("");
                mAdapter.notifyItemInserted(position);
            }
        });

        Button save_btn = (Button) findViewById(R.id.save_button);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncSave asyncSave = new AsyncSave();
                asyncSave.execute();
            }
        });

        Button load_btn = (Button) findViewById(R.id.load_button);
        load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncLoad asyncLoad = new AsyncLoad();
                asyncLoad.execute();
            }
        });

        ImageButton settings_btn = (ImageButton) findViewById(R.id.settings_button);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_layout, new SettingsFragment()).addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RecyclerView rv = (RecyclerView) findViewById(R.id.MyRecylerView);
        rv.setVisibility(View.VISIBLE);
        Toolbar tr = (Toolbar) findViewById(R.id.my_toolbar);
        tr.setVisibility(View.VISIBLE);
    }

    private class AsyncSave extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            StringBuilder buffer = new StringBuilder();
            String file_name = "items_file";
            for (String item:
                    elements) {
                buffer.append(item + "\n");
            }
            try{
                FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                fileOutputStream.write(buffer.toString().getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private  class AsyncLoad extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try{
                String Message;
                FileInputStream fileInputStream = openFileInput("items_file");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                elements.clear();
                while((Message = bufferedReader.readLine()) != null)
                {
                    elements.add(Message);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
