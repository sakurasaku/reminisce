package com.ams.reminisce;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class individual extends Activity
implements OnItemClickListener{

	private SQLiteDatabase db;
	private Cursor c;		//SQLの実行結果のリストを格納//
	private ListView cigaretteList;
	private String theDate, letter;
	private TextView view1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual);
		
		// SQLiteDatabaseオブジェクトを取得する
		db = (new DatabaseOpenHelper(this)).getWritableDatabase();

		cigaretteList = (ListView)findViewById(R.id.list);
		view1 = (TextView)findViewById(R.id.textView1);
		
		//トリガを設定
		cigaretteList.setOnItemClickListener(this);

		
		// コンテキストメニュー登録
		registerForContextMenu(cigaretteList);
		
		// 遷移元からデータを取得
		Intent intent = getIntent();
		theDate = intent.getStringExtra("selected");
		letter = intent.getStringExtra("letter");
		
		// 登録されている全日記を取得しリストアダプタを作成する。
		getDiaryList();
		
		view1.setText(letter);
		
		
	}
	
	/** 
	 * 登録されている全日記を取得しリストアダプタを作成するメソッド
	 */
	public void getDiaryList(){
		
		String where;
		
		if(theDate == null){
			
			where = "memo LIKE " + "'%" + letter + "%'";
		}
		else{
			
			where = "time LIKE " + "'" + theDate + "%'";
		}
		
		// queryメソッドでカーソルを取得
		c = db.query("reminisce", null, where, null, null, null, "_id DESC");
		

		// moveToFirstメソッドでカーソルを先頭に移動しています
		c.moveToFirst();
		
		
		// 取得したカーソルをリスト表示させる
		cigaretteList.setAdapter(
				new SimpleCursorAdapter(
						this,R.layout.list_individual,
						c,
						new String[] {"title", "memo", "time"},
						new int[] {R.id.title, R.id.memo, R.id.time}
				)
		);

//		memberCountTextView.setText(Integer.toString(c.getCount())+"件の想いでが記録されています");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自動生成されたメソッド・スタブ
		
		if(arg0 == cigaretteList){
			
			view1.setText(" "+arg2);
				
				
			
		}
		
	}
	/**
	 * 詳細をクリックすると発動するメソッド。　
	 * @param arg1
	 */
	public void onClickIntent(View arg1){
		
		
		//テキストビューのプロパティonClickとOnclickableに設定
		// 更新アクティビティーに遷移
		Intent intent = new Intent(this,UpdateActivity.class);		//インテントで遷移する
		intent.putExtra("id", c.getString(0));				//選択されたメンバのID（変数）を渡す
		
		view1.setText(c.getString(0));

		startActivity(intent);
	}

	
}
