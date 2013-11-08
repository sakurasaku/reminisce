package com.ams.reminisce;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	// データベース名
	private static final String DB_NAME = "REMINISCE";

	// テーブル名
	public static final String TABLE_NAME = "reminisce";
	public static final String TABLE_PASS = "password";

	// カラム名
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_MEMO = "memo";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PASS = "number";

	private boolean f = false;
	
	//初期投入サンプルデータ
	private String[][] datas = new String[][]{
			{"Relativelayout", "カレンダー上部のボタン等の配置を相対レイアウトに変更した。様々なプラットフォームで統一性を保てる。", "2011/12/18 18:48"},
			{"ドナルドダッグ","今日は野球の試合。４打数４安打２ホームランの大活躍！！試合にも勝って決勝進出。", "2011/12/20 15:33"},
			{"代々木体育館前", "イッパイ人が居るなあ。今日は何のイベントだろう？", "2011/12/23 11:12"},
			{"タロちゃんとお散歩", "愛犬のタロちゃんと代々木公園に来ました。リードを外してあげると大喜び！！他のワンちゃんと追いかけっこしてスゴイ楽しそう。。。", "2011/12/23 11:48"},
			{"メリークリスマス", "イブの今日はスカイツリーも点燈。幻想的な写真が撮れたよ。", "2011/12/24 19:57"},
			{"謹賀新年", "2012年 新年明けましておめでとうございます。昨年は皆様のご協力の元、様々な困難を乗り越える事が出来ました。有難うございます！今年も更に大きく成長できるよう精一杯頑張っていきたいと思います。どうぞ皆様、今年も宜しくお願い致します。", "2012/01/01 10:19"},
	};
	
	private String[][] ids = new String[][]{
			
			{"maffi", "golgo", "2012/01/10 23:59"}
	};

	/**
	 * コンストラクタ
	 */
	public DatabaseOpenHelper(Context context) {

		// 指定したデータベース名が存在しない場合は、新たに作成されonCreate()が呼ばれる
		// バージョンを変更するとonUpgrade()が呼ばれる
		super(context, DB_NAME, null, 1);
	}

	/**
	 *  データベースの生成に呼び出されるので、 スキーマの生成を行う
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.beginTransaction();
		
		try{
			// テーブルの生成
			StringBuilder createSql = new StringBuilder();
			createSql.append("create table " + TABLE_NAME + " (");
			createSql.append(COLUMN_ID + " integer primary key,");
			createSql.append(COLUMN_TITLE + " text,");
			createSql.append(COLUMN_MEMO + " text,");
			createSql.append(COLUMN_TIME + " text");
			createSql.append(")");
			
			db.execSQL( createSql.toString());
			db.setTransactionSuccessful();
				
		} finally {
			db.endTransaction();
		}

		// サンプルデータの投入
	    db.beginTransaction();
	    
	    try{
	    	for( String[] data: datas){
	    		ContentValues values = new ContentValues();
	    		values.put(COLUMN_TITLE, data[0]);
	    		values.put(COLUMN_MEMO, data[1]);
	    		values.put(COLUMN_TIME, data[2]);
	    		db.insert(TABLE_NAME, null, values);
	    	}
	    	db.setTransactionSuccessful();
	    	
	    } finally {
	    	db.endTransaction();
	    }
	    
	    db.beginTransaction();
	    
	    try{
	    	// テーブルの生成
	    	StringBuilder createSql = new StringBuilder();
	    	createSql.append("create table " + TABLE_PASS + " (");
	    	createSql.append(COLUMN_ID + " integer primary key,");
	    	createSql.append(COLUMN_NAME + " text,");
	    	createSql.append(COLUMN_PASS + " text,");
	    	createSql.append(COLUMN_TIME + " text");
	    	createSql.append(")");

	    	db.execSQL( createSql.toString());
	    	db.setTransactionSuccessful();
	    }finally{
	    	db.endTransaction();	    	
	    }
	    // サンプルデータの投入
	    db.beginTransaction();
	    
	    try{
	    	for( String[] data: ids){
	    		ContentValues values = new ContentValues();
	    		values.put(COLUMN_NAME, data[0]);
	    		values.put(COLUMN_PASS, data[1]);
	    		values.put(COLUMN_TIME, data[2]);
	    		db.insert(TABLE_PASS, null, values);
	    	}
	    	db.setTransactionSuccessful();
	    }finally{
	    	db.endTransaction();
	    }
	    
	    
	}

	//データベースの更新
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
