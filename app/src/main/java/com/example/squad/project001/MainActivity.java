package com.example.squad.project001;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String[] name, email, balance, tts;
    ListView ls;

    @Override
    protected void onStart() {
        super.onStart();
        if (db.isOpen()) {
            Cursor result = db.rawQuery("SELECT * FROM account", null);
            int i = 0;
            name = new String[result.getCount()];
            email = new String[result.getCount()];
            balance = new String[result.getCount()];

            while (result.moveToNext()) {
                name[i] = result.getString(1);
                email[i] = result.getString(0);
                balance[i] = String.valueOf(result.getInt(2));
//                Toast.makeText(this,String.valueOf(result.getInt(2)),Toast.LENGTH_SHORT).show();
                i++;
            }
            result.close();
        }
        MyAdapter adapter = new MyAdapter(this, name, email, balance);
        ls.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ls = findViewById(R.id.listview);
        db = openOrCreateDatabase("Student", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS account(Email TEXT PRIMARY KEY , name TEXT , balance int);");
        db.execSQL("CREATE TABLE IF NOT EXISTS tt(detail VARCHAR);");

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, Transaction.class);
                i.putExtra("id", email[position]);
                i.putExtra("name", name[position]);
                i.putExtra("bal", balance[position]);
                i.putExtra("emails", email);
                i.putExtra("names", name);
                startActivity(i);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            helo();
            // run your one time code
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

    }

    public void helo() {
        db.execSQL("INSERT INTO account  VALUES('temp0@test.com','temp0',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp1@test.com','temp1',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp2@test.com','temp2',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp3@test.com','temp3',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp4@test.com','temp4',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp5@test.com','temp5',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp6@test.com','temp6',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp7@test.com','temp7',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp8@test.com','temp8',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('temp9@test.com','temp9',100000.00)");


    }

    public void rem(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.transaction_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Transactions");
        ListView lv = convertView.findViewById(R.id.lv1);


        if (db.isOpen()) {
            Cursor result = db.rawQuery("SELECT * FROM tt", null);
            tts = new String[result.getCount()];
            int x = 0;
            while (result.moveToNext()) {
                tts[x] = "\n"+result.getString(0)+"\n";
//                Toast.makeText(this,String.valueOf(result.getInt(2)),Toast.LENGTH_SHORT).show();
                x++;
            }
            result.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tts);
        lv.setAdapter(adapter);
        alertDialog.show();

    }
}
