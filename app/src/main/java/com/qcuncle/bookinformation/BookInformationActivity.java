package com.qcuncle.bookinformation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.dbHelper;

public class BookInformationActivity extends Activity {
    private static String DB_NAME = "mydb";
    private EditText et_no;
    private EditText et_name;
    private EditText et_ar;
    private EditText et_pr;
    private ArrayList<Map<String, Object>> data;
    private data.dbHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleAdapter listAdapter;
    private View view;
    private ListView listview;
    private Button selBtn, addBtn, updBtn, delBtn;
    private Map<String, Object> item;
    private String selId;
    private ContentValues selCV;

    /**
     * Called when the activity is first created.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_informatin);
        listview = (ListView) findViewById(R.id.list_book);
        et_no = (EditText) findViewById(R.id.et_no);
        et_name = (EditText) findViewById(R.id.et_name);
        et_ar = (EditText) findViewById(R.id.et_ar);
        et_pr = (EditText) findViewById(R.id.et_pr);
        //selBtn = (Button) findViewById(R.id.searchBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        updBtn = (Button) findViewById(R.id.updateBtn);
        delBtn = (Button) findViewById(R.id.deleteBtn);
        init();

        dbHelper = new dbHelper(this, DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库
        data = new ArrayList<Map<String, Object>>();
        dbFindAll();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO Auto-generated method stub
                Map<String, Object> listItem = (Map<String, Object>) listview.getItemAtPosition(position);
                et_no.setText((String) listItem.get("bno"));
                et_name.setText((String) listItem.get("bname"));
                et_ar.setText((String) listItem.get("bar"));
                et_pr.setText((String) listItem.get("bpr"));
                selId = (String) listItem.get("_id");
                Log.i("mydbDemo", "_id=" + selId);
            }
        });
    }
    private void init() {
/*
        selBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(BookInformationActivity.this, "完善中！" , Toast.LENGTH_SHORT).show();
            }
        });
 */

        addBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dbAdd();
                dbFindAll();
            }
        });
        updBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dbUpdate();
                dbFindAll();
            }
        });
        delBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dbDel();
                dbFindAll();
            }
        });
    }

    //数据删除
    protected void dbDel() {
        // TODO Auto-generated method stub
        String where = "_id=" + selId;
        int i = db.delete(dbHelper.TB_NAME, where, null);
        if (i > 0)
            Log.i("myDbDemo", "数据删除成功!");
        else
            Log.e("myDbDemo", "数据未删除!");
    }
    private void showList() {
        // TODO Auto-generated method stub
        listAdapter = new SimpleAdapter(this, data,
                R.layout.list_item, new String[]{"bno","bname","bar", "bpr"}, new int[]{R.id.tvNo, R.id.tvName, R.id.tvAr,R.id.tvPr,});
        listview.setAdapter(listAdapter);
    }
    //更新列表中的数据
    protected void dbUpdate() {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put("bno", et_no.getText().toString().trim());
        values.put("bname", et_name.getText().toString().trim());
        values.put("bar", et_ar.getText().toString().trim());
        values.put("bpr", et_pr.getText().toString().trim());
        String where = "_id=" + selId;
        int i = db.update(dbHelper.TB_NAME, values, where, null);
        if (i > 0)
            Log.i("myDbDemo", "数据更新成功！");
        else
            Log.e("myDbDemo", "数据未更新");
    }
    //插入数据
    protected void dbAdd() {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put("bno", et_no.getText().toString().trim());
        values.put("bname", et_name.getText().toString().trim());
        values.put("bar", et_ar.getText().toString().trim());
        values.put("bpr", et_pr.getText().toString().trim());
        long rowid = db.insert(dbHelper.TB_NAME, null, values);
        if (rowid == -1)
            Log.e("myDbDemo", "数据插入失败！");
        else
            Log.i("myDbDemo", "数据插入成功!" + rowid);
    }

    //查询数据
    protected void dbFindAll() {
        // TODO Auto-generated method stub
        data.clear();
        cursor = db.query(dbHelper.TB_NAME, null, null, null, null, null, "_id ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(0);
            String bno = cursor.getString(1);
            String bname = cursor.getString(2);
            String bar = cursor.getString(3);
            String bpr = cursor.getString(4);
            item = new HashMap<String, Object>();
            item.put("_id", id);
            item.put("bno", bno);
            item.put("bname", bname);
            item.put("bar", bar);
            item.put("bpr", bpr);
            data.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        showList();
    }
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(BookInformationActivity.this,MainActivity.class);
        startActivity(intent);
        //关闭原来activity
        BookInformationActivity.this.finish();
    }
}
