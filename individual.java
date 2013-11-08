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
	private Cursor c;		//SQL�̎��s���ʂ̃��X�g���i�[//
	private ListView cigaretteList;
	private String theDate, letter;
	private TextView view1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual);
		
		// SQLiteDatabase�I�u�W�F�N�g���擾����
		db = (new DatabaseOpenHelper(this)).getWritableDatabase();

		cigaretteList = (ListView)findViewById(R.id.list);
		view1 = (TextView)findViewById(R.id.textView1);
		
		//�g���K��ݒ�
		cigaretteList.setOnItemClickListener(this);

		
		// �R���e�L�X�g���j���[�o�^
		registerForContextMenu(cigaretteList);
		
		// �J�ڌ�����f�[�^���擾
		Intent intent = getIntent();
		theDate = intent.getStringExtra("selected");
		letter = intent.getStringExtra("letter");
		
		// �o�^����Ă���S���L���擾�����X�g�A�_�v�^���쐬����B
		getDiaryList();
		
		view1.setText(letter);
		
		
	}
	
	/** 
	 * �o�^����Ă���S���L���擾�����X�g�A�_�v�^���쐬���郁�\�b�h
	 */
	public void getDiaryList(){
		
		String where;
		
		if(theDate == null){
			
			where = "memo LIKE " + "'%" + letter + "%'";
		}
		else{
			
			where = "time LIKE " + "'" + theDate + "%'";
		}
		
		// query���\�b�h�ŃJ�[�\�����擾
		c = db.query("reminisce", null, where, null, null, null, "_id DESC");
		

		// moveToFirst���\�b�h�ŃJ�[�\����擪�Ɉړ����Ă��܂�
		c.moveToFirst();
		
		
		// �擾�����J�[�\�������X�g�\��������
		cigaretteList.setAdapter(
				new SimpleCursorAdapter(
						this,R.layout.list_individual,
						c,
						new String[] {"title", "memo", "time"},
						new int[] {R.id.title, R.id.memo, R.id.time}
				)
		);

//		memberCountTextView.setText(Integer.toString(c.getCount())+"���̑z���ł��L�^����Ă��܂�");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		if(arg0 == cigaretteList){
			
			view1.setText(" "+arg2);
				
				
			
		}
		
	}
	/**
	 * �ڍׂ��N���b�N����Ɣ������郁�\�b�h�B�@
	 * @param arg1
	 */
	public void onClickIntent(View arg1){
		
		
		//�e�L�X�g�r���[�̃v���p�e�BonClick��Onclickable�ɐݒ�
		// �X�V�A�N�e�B�r�e�B�[�ɑJ��
		Intent intent = new Intent(this,UpdateActivity.class);		//�C���e���g�őJ�ڂ���
		intent.putExtra("id", c.getString(0));				//�I�����ꂽ�����o��ID�i�ϐ��j��n��
		
		view1.setText(c.getString(0));

		startActivity(intent);
	}

	
}
