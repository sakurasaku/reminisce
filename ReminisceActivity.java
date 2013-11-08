package com.ams.reminisce;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
//import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ReminisceActivity extends Activity
implements
OnClickListener,
DialogInterface.OnClickListener,
OnItemClickListener,
OnEditorActionListener
{

	private SQLiteDatabase db;
	private Cursor c;
	private ListView cigaretteList;
	private Button insertButton, button2 ;
//	private TextView memberCountTextView;
	private AlertDialog deleteButtonDialog;
	private EditText searchText;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

//		memberCountTextView = (TextView)findViewById(R.id.searchText);
		insertButton = (Button)findViewById(R.id.insertButton);
		button2 = (Button)findViewById(R.id.button2);
		insertButton.setOnClickListener(this);
		button2.setOnClickListener(this);
		searchText = (EditText)findViewById(R.id.searchText);
		searchText.setWidth(searchText.getWidth());		//サイズをここで固定する。画面サイズが変わる事があるので。

		searchText.setOnEditorActionListener(this);

		// SQLiteDatabaseオブジェクトを取得する
		db = (new DatabaseOpenHelper(this)).getWritableDatabase();
		
		cigaretteList = (ListView)findViewById(R.id.list);
		
		//トリガを設定
		cigaretteList.setOnItemClickListener(this);

		// コンテキストメニュー登録
		registerForContextMenu(cigaretteList);

		getDiaryList();		
	}

	/** 
	 * 登録されている全日記を取得しリストアダプタを作成するメソッド
	 */
	public void getDiaryList(){

		// queryメソッドでカーソルを取得
		c = db.query("reminisce", null, null, null, null, null, "_id DESC");

		// moveToFirstメソッドでカーソルを先頭に移動しています
		c.moveToFirst();
		
		// 取得したカーソルをリスト表示させる
		cigaretteList.setAdapter(
				new SimpleCursorAdapter(
						this,R.layout.list_item,
						c,
						new String[] {"title", "memo", "time"},
						new int[] {R.id.title, R.id.memo, R.id.time}
				)
		);

//		memberCountTextView.setText(Integer.toString(c.getCount())+"件の想いでが記録されています");
	}
	
    /**
     * コンテキストメニュー生成時処理			（押し続けたとき）
     */
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo info) {

    	super.onCreateContextMenu(menu, view, info);
    	AdapterContextMenuInfo adapterInfo = (AdapterContextMenuInfo) info;
    	ListView listView = (ListView)view;		//リストビュー型にキャスト

    	final Cursor cc = (Cursor)listView.getItemAtPosition(adapterInfo.position);	//リストビュー内はカーソル型なので

    	// コンテキストメニュータイトル
    	menu.setHeaderTitle(cc.getString(1)+"の"+getString(R.string.edit));		//押し続けた時のメニューを作成（）内の１はDBHelperの話　０はID　１は名前

    	// 編集メニュー部分
    	menu.add(getString(R.string.update)).setOnMenuItemClickListener(new OnMenuItemClickListener(){		//

			public boolean onMenuItemClick(MenuItem item) {
				// TODO 自動生成されたメソッド・スタブ

				// 更新アクティビティーに遷移
				Intent intent = new Intent(ReminisceActivity.this,UpdateActivity.class);		//インテントで遷移する
				intent.putExtra("id", cc.getString(0));				//選択されたメンバのID（変数）を渡す

				startActivity(intent);

				return false;
			}
    	});

    	// 削除メニュー部分
    	menu.add(getString(R.string.delete)).setOnMenuItemClickListener(new OnMenuItemClickListener() {	//コールバック

			public boolean onMenuItemClick(MenuItem item) {
				// TODO 自動生成されたメソッド・スタブ

				// 削除確認ダイアログ生成
				AlertDialog.Builder deleteButtonBuilder  = new AlertDialog.Builder(ReminisceActivity.this)
				.setTitle(R.string.confirm)
				.setMessage(cc.getString(1)+"の"+getText(R.string.delete_confirm_message))
				.setCancelable(false)
				.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener(){	//コールバックのコールバック

					public void onClick(DialogInterface dialog, int which) {

						// 削除SQL実行
						db.execSQL("DELETE FROM "+DatabaseOpenHelper.TABLE_NAME+ " WHERE "+DatabaseOpenHelper.COLUMN_ID+ " = " +cc.getString(0));
						//（本来成功時と失敗時の処理がここに入る）

						// 登録されている全日記を取得しリストアダプタを作成する。
						getDiaryList();		//メソッド化してある

						// 削除完了メッセージ表示
						Toast.makeText(ReminisceActivity.this, cc.getString(1)+"を"+getText(R.string.delete_message), Toast.LENGTH_LONG).show();

					}
				})
				.setNegativeButton(R.string.no, ReminisceActivity.this);

				deleteButtonDialog = deleteButtonBuilder.create();
				deleteButtonDialog.show();

				return false;
			}
    	});

    	// キャンセルメニュー部分
    	menu.add(getString(R.string.cancel));

    }
    /**
     * ボタン用のオンクリックメソッド
     */
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ

		// 日記ボタンがクリックされた場合
		if(v == insertButton){

			// 追加アクティビティーに遷移（移動する）
			Intent intent = new Intent();
			intent.setClassName("com.ams.reminisce","com.ams.reminisce.LogIn");
			startActivity(intent);
		}
		
		// 暦ボタンがクリックされた場合
		if(v == button2){

			// 追加アクティビティーに遷移（移動する）
			Intent intent = new Intent();
			intent.setClassName("com.ams.reminisce","com.ams.reminisce.SimpleCalendarViewActivity");
			startActivity(intent);
		}
	}

	public void onClick(DialogInterface dialog, int which) {
		// TODO 自動生成されたメソッド・スタブ
	}
	
    /**
     * メニューが開く時初回だけ呼び出されるメソッド
     */
    public boolean onCreateOptionsMenu(Menu menu){
    	
    	super.onCreateOptionsMenu(menu);
    	
    	getMenuInflater().inflate(R.menu.menu, menu);
    	return true;
    }
    //メニューが開く時初回だけ呼び出されるメソッド
    public boolean onPrepareOptionsMenu(Menu menu){
    	
    	super.onPrepareOptionsMenu(menu);
    	
/*    	boolean condition = (editText.getText().length()!=0);
    	menu.findItem(R.id.item1).setEnabled(condition);
    	menu.findItem(R.id.item3).setEnabled(condition);
    	menu.findItem(R.id.item4).setEnabled(condition);
*/    	
    	return true;
    }
    /**
     * メニューで選択されたアイテムの処理をするメソッド
     */
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	if(item.isCheckable()){
    		
    		item.setChecked(!item.isChecked());
    	}
    	switch(item.getItemId()){
    	
    	case R.id.item4:
//    		editText.setTextSize(editText.getTextSize() + 2);
    		break;
    		
    	case R.id.item5:
    		finish();
    		break;
    		
    	case R.id.item1:
    		// 画像ボタンがクリックされた場合
    		// 追加アクティビティーに遷移（移動する）
    		Intent intent2 = new Intent();
    		intent2.setClassName("com.ams.reminisce","com.ams.reminisce.GalleryActivity");
    		startActivity(intent2);
    		break;
    		
    		
    	
    	case R.id.item3:
    		// 画像ボタンがクリックされた場合
    		// 追加アクティビティーに遷移（移動する）
    		Intent intent1 = new Intent();
    		intent1.setClassName("com.ams.reminisce","com.ams.reminisce.GMapActivity");
    		startActivity(intent1);
    		break;
    		
    	case R.id.item2:	
    	case R.id.item6:
    	case R.id.item7:
    	case R.id.item8:
    		break;
    	default:
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自動生成されたメソッド・スタブ
		
		if(arg0 == cigaretteList){
			
			
		}		
	}
	/**
	 * 詳細をクリックすると発動するメソッド。　
	 * @param arg1
	 */
	public void onClickIntent(View arg1){
		
		
		
		//テキストビューのプロパティonClickとOnclickableに設定
		// 更新アクティビティーに遷移
		Intent intent = new Intent(this,individual.class);		//インテントで遷移する
		intent.putExtra("selected", c.getString(0));				//選択されたメンバのID（変数）を渡す
		
		startActivity(intent);
	}

	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO 自動生成されたメソッド・スタブ
		
		if(arg0 == searchText){
			
			Editable search = searchText.getText();
			
			// 個別表示アクティビティーに遷移
            Intent intent = new Intent(this,individual.class);
			intent.putExtra("letter", search.toString());				//検索ワードを渡す

			startActivity(intent);
		}
		
		return true;
	}

}




