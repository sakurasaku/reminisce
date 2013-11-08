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
		searchText.setWidth(searchText.getWidth());		//�T�C�Y�������ŌŒ肷��B��ʃT�C�Y���ς�鎖������̂ŁB

		searchText.setOnEditorActionListener(this);

		// SQLiteDatabase�I�u�W�F�N�g���擾����
		db = (new DatabaseOpenHelper(this)).getWritableDatabase();
		
		cigaretteList = (ListView)findViewById(R.id.list);
		
		//�g���K��ݒ�
		cigaretteList.setOnItemClickListener(this);

		// �R���e�L�X�g���j���[�o�^
		registerForContextMenu(cigaretteList);

		getDiaryList();		
	}

	/** 
	 * �o�^����Ă���S���L���擾�����X�g�A�_�v�^���쐬���郁�\�b�h
	 */
	public void getDiaryList(){

		// query���\�b�h�ŃJ�[�\�����擾
		c = db.query("reminisce", null, null, null, null, null, "_id DESC");

		// moveToFirst���\�b�h�ŃJ�[�\����擪�Ɉړ����Ă��܂�
		c.moveToFirst();
		
		// �擾�����J�[�\�������X�g�\��������
		cigaretteList.setAdapter(
				new SimpleCursorAdapter(
						this,R.layout.list_item,
						c,
						new String[] {"title", "memo", "time"},
						new int[] {R.id.title, R.id.memo, R.id.time}
				)
		);

//		memberCountTextView.setText(Integer.toString(c.getCount())+"���̑z���ł��L�^����Ă��܂�");
	}
	
    /**
     * �R���e�L�X�g���j���[����������			�i�����������Ƃ��j
     */
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo info) {

    	super.onCreateContextMenu(menu, view, info);
    	AdapterContextMenuInfo adapterInfo = (AdapterContextMenuInfo) info;
    	ListView listView = (ListView)view;		//���X�g�r���[�^�ɃL���X�g

    	final Cursor cc = (Cursor)listView.getItemAtPosition(adapterInfo.position);	//���X�g�r���[���̓J�[�\���^�Ȃ̂�

    	// �R���e�L�X�g���j���[�^�C�g��
    	menu.setHeaderTitle(cc.getString(1)+"��"+getString(R.string.edit));		//�������������̃��j���[���쐬�i�j���̂P��DBHelper�̘b�@�O��ID�@�P�͖��O

    	// �ҏW���j���[����
    	menu.add(getString(R.string.update)).setOnMenuItemClickListener(new OnMenuItemClickListener(){		//

			public boolean onMenuItemClick(MenuItem item) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u

				// �X�V�A�N�e�B�r�e�B�[�ɑJ��
				Intent intent = new Intent(ReminisceActivity.this,UpdateActivity.class);		//�C���e���g�őJ�ڂ���
				intent.putExtra("id", cc.getString(0));				//�I�����ꂽ�����o��ID�i�ϐ��j��n��

				startActivity(intent);

				return false;
			}
    	});

    	// �폜���j���[����
    	menu.add(getString(R.string.delete)).setOnMenuItemClickListener(new OnMenuItemClickListener() {	//�R�[���o�b�N

			public boolean onMenuItemClick(MenuItem item) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u

				// �폜�m�F�_�C�A���O����
				AlertDialog.Builder deleteButtonBuilder  = new AlertDialog.Builder(ReminisceActivity.this)
				.setTitle(R.string.confirm)
				.setMessage(cc.getString(1)+"��"+getText(R.string.delete_confirm_message))
				.setCancelable(false)
				.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener(){	//�R�[���o�b�N�̃R�[���o�b�N

					public void onClick(DialogInterface dialog, int which) {

						// �폜SQL���s
						db.execSQL("DELETE FROM "+DatabaseOpenHelper.TABLE_NAME+ " WHERE "+DatabaseOpenHelper.COLUMN_ID+ " = " +cc.getString(0));
						//�i�{���������Ǝ��s���̏����������ɓ���j

						// �o�^����Ă���S���L���擾�����X�g�A�_�v�^���쐬����B
						getDiaryList();		//���\�b�h�����Ă���

						// �폜�������b�Z�[�W�\��
						Toast.makeText(ReminisceActivity.this, cc.getString(1)+"��"+getText(R.string.delete_message), Toast.LENGTH_LONG).show();

					}
				})
				.setNegativeButton(R.string.no, ReminisceActivity.this);

				deleteButtonDialog = deleteButtonBuilder.create();
				deleteButtonDialog.show();

				return false;
			}
    	});

    	// �L�����Z�����j���[����
    	menu.add(getString(R.string.cancel));

    }
    /**
     * �{�^���p�̃I���N���b�N���\�b�h
     */
	public void onClick(View v) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		// ���L�{�^�����N���b�N���ꂽ�ꍇ
		if(v == insertButton){

			// �ǉ��A�N�e�B�r�e�B�[�ɑJ�ځi�ړ�����j
			Intent intent = new Intent();
			intent.setClassName("com.ams.reminisce","com.ams.reminisce.LogIn");
			startActivity(intent);
		}
		
		// ��{�^�����N���b�N���ꂽ�ꍇ
		if(v == button2){

			// �ǉ��A�N�e�B�r�e�B�[�ɑJ�ځi�ړ�����j
			Intent intent = new Intent();
			intent.setClassName("com.ams.reminisce","com.ams.reminisce.SimpleCalendarViewActivity");
			startActivity(intent);
		}
	}

	public void onClick(DialogInterface dialog, int which) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
	}
	
    /**
     * ���j���[���J�������񂾂��Ăяo����郁�\�b�h
     */
    public boolean onCreateOptionsMenu(Menu menu){
    	
    	super.onCreateOptionsMenu(menu);
    	
    	getMenuInflater().inflate(R.menu.menu, menu);
    	return true;
    }
    //���j���[���J�������񂾂��Ăяo����郁�\�b�h
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
     * ���j���[�őI�����ꂽ�A�C�e���̏��������郁�\�b�h
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
    		// �摜�{�^�����N���b�N���ꂽ�ꍇ
    		// �ǉ��A�N�e�B�r�e�B�[�ɑJ�ځi�ړ�����j
    		Intent intent2 = new Intent();
    		intent2.setClassName("com.ams.reminisce","com.ams.reminisce.GalleryActivity");
    		startActivity(intent2);
    		break;
    		
    		
    	
    	case R.id.item3:
    		// �摜�{�^�����N���b�N���ꂽ�ꍇ
    		// �ǉ��A�N�e�B�r�e�B�[�ɑJ�ځi�ړ�����j
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		if(arg0 == cigaretteList){
			
			
		}		
	}
	/**
	 * �ڍׂ��N���b�N����Ɣ������郁�\�b�h�B�@
	 * @param arg1
	 */
	public void onClickIntent(View arg1){
		
		
		
		//�e�L�X�g�r���[�̃v���p�e�BonClick��Onclickable�ɐݒ�
		// �X�V�A�N�e�B�r�e�B�[�ɑJ��
		Intent intent = new Intent(this,individual.class);		//�C���e���g�őJ�ڂ���
		intent.putExtra("selected", c.getString(0));				//�I�����ꂽ�����o��ID�i�ϐ��j��n��
		
		startActivity(intent);
	}

	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		if(arg0 == searchText){
			
			Editable search = searchText.getText();
			
			// �ʕ\���A�N�e�B�r�e�B�[�ɑJ��
            Intent intent = new Intent(this,individual.class);
			intent.putExtra("letter", search.toString());				//�������[�h��n��

			startActivity(intent);
		}
		
		return true;
	}

}




