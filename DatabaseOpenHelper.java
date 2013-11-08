package com.ams.reminisce;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	// �f�[�^�x�[�X��
	private static final String DB_NAME = "REMINISCE";

	// �e�[�u����
	public static final String TABLE_NAME = "reminisce";
	public static final String TABLE_PASS = "password";

	// �J������
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_MEMO = "memo";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PASS = "number";

	private boolean f = false;
	
	//���������T���v���f�[�^
	private String[][] datas = new String[][]{
			{"Relativelayout", "�J�����_�[�㕔�̃{�^�����̔z�u�𑊑΃��C�A�E�g�ɕύX�����B�l�X�ȃv���b�g�t�H�[���œ��ꐫ��ۂĂ�B", "2011/12/18 18:48"},
			{"�h�i���h�_�b�O","�����͖싅�̎����B�S�Ő��S���łQ�z�[�������̑劈��I�I�����ɂ������Č����i�o�B", "2011/12/20 15:33"},
			{"��X�ؑ̈�ّO", "�C�b�p�C�l������Ȃ��B�����͉��̃C�x���g���낤�H", "2011/12/23 11:12"},
			{"�^�������Ƃ��U��", "�����̃^�������Ƒ�X�،����ɗ��܂����B���[�h���O���Ă�����Ƒ��сI�I���̃��������ƒǂ������������ăX�S�C�y�������B�B�B", "2011/12/23 11:48"},
			{"�����[�N���X�}�X", "�C�u�̍����̓X�J�C�c���[���_���B���z�I�Ȏʐ^���B�ꂽ��B", "2011/12/24 19:57"},
			{"�މ�V�N", "2012�N �V�N�����܂��Ă��߂łƂ��������܂��B��N�͊F�l�̂����͂̌��A�l�X�ȍ�������z���鎖���o���܂����B�L��������܂��I���N���X�ɑ傫�������ł���悤����t�撣���Ă��������Ǝv���܂��B�ǂ����F�l�A���N���X�������肢�v���܂��B", "2012/01/01 10:19"},
	};
	
	private String[][] ids = new String[][]{
			
			{"maffi", "golgo", "2012/01/10 23:59"}
	};

	/**
	 * �R���X�g���N�^
	 */
	public DatabaseOpenHelper(Context context) {

		// �w�肵���f�[�^�x�[�X�������݂��Ȃ��ꍇ�́A�V���ɍ쐬����onCreate()���Ă΂��
		// �o�[�W������ύX�����onUpgrade()���Ă΂��
		super(context, DB_NAME, null, 1);
	}

	/**
	 *  �f�[�^�x�[�X�̐����ɌĂяo�����̂ŁA �X�L�[�}�̐������s��
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.beginTransaction();
		
		try{
			// �e�[�u���̐���
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

		// �T���v���f�[�^�̓���
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
	    	// �e�[�u���̐���
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
	    // �T���v���f�[�^�̓���
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

	//�f�[�^�x�[�X�̍X�V
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
