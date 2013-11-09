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
		
		// �o�^�����Ă����S���L���擾�����X�g�A�_�v�^���쐬�����B
		getPassword();
		
		
		
	}
	
	/** 
	 * �o�^�����Ă����p�X���[�h���擾���郁�\�b�h
	 */
	public void getPassword(){

		// query���\�b�h�ŃJ�[�\�����擾
		c = db.query("password", null, null, null, null, null, null);
		
		
		c.moveToFirst();

		list = new CharSequence[c.getCount()];
		for (int i = 0; i < list.length; i++) {	
		    list[i] = c.getString(2);		
		    c.moveToNext();
		}
		c.close();
			
	}

	@Override
	public void onClick(View v) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		if(v == button1){
			
			//���͂����������g�[�X�g�o�͂���
			String answer = editText1.getText().toString();
			
		
			if(list[0].equals(answer)){
				Toast.makeText(LogIn.this, 
				"���O�C�����܂�", 
				Toast.LENGTH_LONG).show();
				
				// ���C���A�N�e�B�r�e�B�[�ɑJ�ځi�ړ������j
				Intent intent = new Intent();
				intent.setClassName("com.ams.reminisce","com.ams.reminisce.ReminisceActivity");
				startActivity(intent);

			}
			else{
				Toast.makeText(LogIn.this, 
				"password���Ⴂ�܂�", 
				Toast.LENGTH_LONG).show();
			}
		}
		if(v == button2){
			
			finish();
		}
	}
}
