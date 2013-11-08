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
	private Cursor c;		//SQL�̎��s���ʂ̃��X�g���i�[//

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		// �J�ڌ�����f�[�^���擾
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");

		// SQLiteDatabase�I�u�W�F�N�g���擾����
		db = (new DatabaseOpenHelper(this)).getWritableDatabase();

		// �w���ID�̓��L�f�[�^���擾����B
		String sql = "SELECT * FROM "+DatabaseOpenHelper.TABLE_NAME+ " WHERE "+DatabaseOpenHelper.COLUMN_ID+" = "+id;
		c = db.rawQuery(sql, null);		//List��127�s�ڂƔ�r�@execSQL�Ƃ̈Ⴂ�@�����̓Z���N�g���Ɏg���Ȃ��H�@�����̓u�[���[���^�H
		//SQL�͑��ɂ�����������̂Ń`�[���̕��j�ɏ]���B�l�b�g�ɂ��������ς�����//
		c.moveToFirst();
		//�擾�����f�[�^��ݒ�//
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		// �X�V�{�^�����N���b�N���ꂽ�ꍇ
		if(v == updateButton){

			String id = idTextView.getText().toString();
			String title = nameEditText.getText().toString();
			String memo = numberEditText.getText().toString();

			// �w���ID�̃����o�[�����X�V����B		�X�V�ΏۂɂȂ���̂��w��//
			ContentValues cv = new ContentValues();
            cv.put(DatabaseOpenHelper.COLUMN_TITLE, title);
            cv.put(DatabaseOpenHelper.COLUMN_MEMO, memo);
            db.update(DatabaseOpenHelper.TABLE_NAME, cv, DatabaseOpenHelper.COLUMN_ID+" = " + id, null);		//���s//

            Toast.makeText(this, R.string.update_message, Toast.LENGTH_LONG).show();
		}

		// �L�����Z���{�^�����N���b�N���ꂽ�ꍇ
		if(v == cancelButton){

		}

		// ���X�g�A�N�e�B�r�e�B�[�ɑJ�ڂ���B		���X�g�ɖ߂�//
		Intent intent = new Intent(UpdateActivity.this,ReminisceActivity.class);
		startActivity(intent);
	}
}
