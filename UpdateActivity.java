package com.ams.reminisce;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends Activity
implements
OnClickListener
{

	private SQLiteDatabase db;
	private TextView idTextView;
	private EditText numberEditText;
	private EditText nameEditText;
	private Button updateButton,cancelButton;
	private Cursor c;		//SQLの実行結果のリストを格納//

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		// 遷移元からデータを取得
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");

		// SQLiteDatabaseオブジェクトを取得する
		db = (new DatabaseOpenHelper(this)).getWritableDatabase();

		// 指定のIDの日記データを取得する。
		String sql = "SELECT * FROM "+DatabaseOpenHelper.TABLE_NAME+ " WHERE "+DatabaseOpenHelper.COLUMN_ID+" = "+id;
		c = db.rawQuery(sql, null);		//Listの127行目と比較　execSQLとの違い　無効はセレクト文に使えない？　無効はブーリーン型？
		//SQLは他にもやり方があるのでチームの方針に従う。ネットにやり方いっぱいある//
		c.moveToFirst();
		//取得したデータを設定//
		idTextView = (TextView)findViewById(R.id.textView1);
		numberEditText = (EditText)findViewById(R.id.memoText);
		nameEditText = (EditText)findViewById(R.id.titleText);
		updateButton = (Button)findViewById(R.id.updateButton);
		cancelButton = (Button)findViewById(R.id.weatherButton);

		updateButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		idTextView.setText(id);
		numberEditText.setText(c.getString(2));
		nameEditText.setText(c.getString(1));
	}

	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ

		// 更新ボタンがクリックされた場合
		if(v == updateButton){

			String id = idTextView.getText().toString();
			String title = nameEditText.getText().toString();
			String memo = numberEditText.getText().toString();

			// 指定のIDのメンバー情報を更新する。		更新対象になるものを指定//
			ContentValues cv = new ContentValues();
            cv.put(DatabaseOpenHelper.COLUMN_TITLE, title);
            cv.put(DatabaseOpenHelper.COLUMN_MEMO, memo);
            db.update(DatabaseOpenHelper.TABLE_NAME, cv, DatabaseOpenHelper.COLUMN_ID+" = " + id, null);		//実行//

            Toast.makeText(this, R.string.update_message, Toast.LENGTH_LONG).show();
		}

		// キャンセルボタンがクリックされた場合
		if(v == cancelButton){

		}

		// リストアクティビティーに遷移する。		リストに戻る//
		Intent intent = new Intent(UpdateActivity.this,ReminisceActivity.class);
		startActivity(intent);
	}
}
