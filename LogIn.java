package com.ams.reminisce;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class LogIn extends Activity
implements OnClickListener{
	
	private Cursor c;
	private SQLiteDatabase db;
	private Button button1, button2;
	private EditText editText1;
	private CharSequence[] list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		button1 = (Button)findViewById(R.id.button1);
		button2 = (Button)findViewById(R.id.button2);
		editText1 = (EditText)findViewById(R.id.editText1);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);

		
		db = (new DatabaseOpenHelper(this)).getWritableDatabase();
		
		// 登録されている全日記を取得しリストアダプタを作成する。
		getPassword();
		
		
		
	}
	
	/** 
	 * 登録されているパスワードを取得するメソッド
	 */
	public void getPassword(){

		// queryメソッドでカーソルを取得
		c = db.query("password", null, null, null, null, null, null);
		
		
		c.moveToFirst();

		list = new CharSequence[c.getCount()];
		for (int i = 0; i < list.length; i++) {		//リストのレングスはこの場合１しかない。
		    list[i] = c.getString(2);				//パスワードは３列目のカラムだから引数が２になる
		    c.moveToNext();
		}
		c.close();
		
//		editText1.setText(list[0]);
		
/*		
		if(c.moveToFirst()){
			do{
				long id = c.getLong(c.getColumnIndex(“_id”));
				value = c.getString(c.getColumnIndex(“number”));
//			　　　　System.out.println(id + “:” + bookmark);
			}while(c.moveToNext());
		}
*/

			
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		
		if(v == button1){
			
			//入力した文字をトースト出力する
			String answer = editText1.getText().toString();
			
		
			if(list[0].equals(answer)){
				Toast.makeText(LogIn.this, 
				"ログインします", 
				Toast.LENGTH_LONG).show();
				
				// メインアクティビティーに遷移（移動する）
				Intent intent = new Intent();
				intent.setClassName("com.ams.reminisce","com.ams.reminisce.ReminisceActivity");
				startActivity(intent);

			}
			else{
				Toast.makeText(LogIn.this, 
				"passwordが違います", 
				Toast.LENGTH_LONG).show();
			}
		}
		if(v == button2){
			
			finish();
		}
	}
}
