package com.ams.reminisce;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.sql.Timestamp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class InsertActivity extends Activity
implements
OnClickListener
{

	private SQLiteDatabase db;
	private EditText numberEditText;
	private EditText nameEditText;
	private Button insertButton,cancelButton;
//	private TextView textView1, textView2;
	



	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert);

		// SQLiteDatabase�I�u�W�F�N�g���擾����
		db = (new DatabaseOpenHelper(this)).getWritableDatabase();

		numberEditText = (EditText)findViewById(R.id.editText2);
		nameEditText = (EditText)findViewById(R.id.editText1);
		insertButton = (Button)findViewById(R.id.insertButton);
		cancelButton = (Button)findViewById(R.id.button1);
//		textView1 = (TextView)findViewById(R.id.textView1);
//		textView2 = (TextView)findViewById(R.id.textView2);

		insertButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

	}
	/**
	 * �^�C���X�^���v�^���X�g�����O�ɕϊ�����
	 * @param timestamparg
	 * @param timeFormat
	 * @return
	 */
	public String formattedTimestamp(Timestamp timestamparg, String timeFormat) {
		
		return new SimpleDateFormat(timeFormat).format(timestamparg);
	}
	//�\�����鏑���̌^
	public final String TIME_FORMAT = "yyyy/MM/dd HH:mm";
	

	public void onClick(View v) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		// �ǉ��{�^�����N���b�N���ꂽ�ꍇ
		if(v == insertButton){
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//			long timestamp = System.currentTimeMillis();
//			String time = timestamp.toString();
			
			//�Y�񂾖��Ɏ����Œǉ������J�����̒l�B�������t�H�[�}�b�g	
			String time = formattedTimestamp(timestamp, TIME_FORMAT);	

			String memo = numberEditText.getText().toString();
			String title = nameEditText.getText().toString();

			// �V�K�����o�[��ǉ�����B
			ContentValues cv = new ContentValues();
            cv.put(DatabaseOpenHelper.COLUMN_TITLE, title);
            cv.put(DatabaseOpenHelper.COLUMN_MEMO, memo);
            cv.put(DatabaseOpenHelper.COLUMN_TIME, time);
            db.insert(DatabaseOpenHelper.TABLE_NAME, null, cv);

            Toast.makeText(this, R.string.insert_message, Toast.LENGTH_LONG).show();
		}

		// �L�����Z���{�^�����N���b�N���ꂽ�ꍇ
		if(v == cancelButton){

		}

		// ���X�g�A�N�e�B�r�e�B�[�ɑJ�ڂ���B
		Intent intent = new Intent(InsertActivity.this,ReminisceActivity.class);
		startActivity(intent);
	}
	
	
//=======================	Inner class    ==========================//
	
	public class GalleryActivity extends Activity
	implements
	DialogInterface.OnClickListener,
	OnItemClickListener,
	Runnable{

	private ProgressDialog dialog;
	private ImageView imageView;
	private Gallery gallery;
	private String[] picturesList;
	private Bitmap[] thumbnail;
	private String picturesPath;
	private LinearLayout layout;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.id.linearLayout2);

        //SD�J�[�h�����݂��Ȃ��Ƃ��J���_�C�A���O
        if(!sdcardReadReady()){
        	new AlertDialog.Builder(this).setMessage(R.string.message2).setNeutralButton("OK", this).show();
        	return;
        }
        //SD�J�[�h�̃f�B���N�g�����擾
        String rootDir = Environment.getExternalStorageDirectory().getPath();

        //�摜�̎w��ɕK�v�ȏ����擾
        File picturesDir = new File(rootDir + "/Pictures");
        picturesPath = picturesDir.getAbsolutePath() + "/";
        picturesList = picturesDir.list();

        //JPG�`���Ɍ��肵���t�@�C�����̈ꗗ����蒼��
        List<String> tmp = new ArrayList<String>();
        for(String s : picturesList){

        	if(s.endsWith("JPG") || s.endsWith("jpg")){

        		tmp.add(s);
        	}
        }
        tmp.toArray(picturesList);

        //�T���l�[���̍쐬���ɊJ���_�C�A���O
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getText(R.string.message1));
        dialog.show();

        //�T���l�[���̍쐬
        thumbnail = new Bitmap[picturesList.length];
        new Thread(this).start();

        //�M�������[�̍쐬
        gallery = (Gallery)findViewById(R.id.gallery1);
        gallery.setSpacing(2);
        gallery.setAdapter(new GalleryAdapter());
        gallery.setOnItemClickListener(this);

        //�傫�ȃC���[�W�r���[�ɏ����l�̉摜��ݒ�
        imageView = (ImageView)findViewById(R.id.imageView1);
        Bitmap picture = BitmapFactory.decodeFile(picturesPath + picturesList[0]);
        imageView.setImageBitmap(picture);
        
        //���j�A���C�A�E�g�ɃC���[�W�r���[���Z�b�g
        layout = (LinearLayout)findViewById(R.id.linearLayout2);
        layout.addView(imageView);
        setContentView(layout);



    }
	/**
	 * �ǉ������X���b�h�ŃT���l�[�����쐬���鏈��
	 */
	@Override
	public void run() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		for(int i = 0; i < picturesList.length; i++){

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(picturesPath + picturesList[i], options);
			int width = options.outWidth;
			int height = options.outHeight;
			int scale = Math.max(width / 160 + 1, height / 120 + 1);
			options.inSampleSize = scale;

			options.inJustDecodeBounds = false;
			thumbnail[i] = BitmapFactory.decodeFile(picturesPath + picturesList[i], options);
		}
		dialog.dismiss();
	}
	//SD�J�[�h�����݂��邩�ǂ������ׂ郁�\�b�h
	private boolean sdcardReadReady(){

		String state = Environment.getExternalStorageState();
		return(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
	}

    /**
     * �_�C�A���O�̃{�^���������ꂽ�Ƃ��Ăяo����郁�\�b�h
     */
	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		switch(arg1){

		case DialogInterface.BUTTON_NEUTRAL:
			arg0.dismiss();
			finish();
		}
	}


	/**
	 * �M�������[�̑I�����ɉ摜��ݒ肷��A�_�v�^
	 */
	public class GalleryAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO �����������ꂽ���\�b�h�E�X�^�u
			return picturesList.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO �����������ꂽ���\�b�h�E�X�^�u
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO �����������ꂽ���\�b�h�E�X�^�u
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO �����������ꂽ���\�b�h�E�X�^�u

			ImageView view;
			if(arg1 == null){

				view = new ImageView(GalleryActivity.this);
				view.setImageBitmap(thumbnail[arg0]);
			}
			else{
				view = (ImageView)arg1;
			}
			return view;
		}
	}

	/**
	 * �M�������[�̑I�����������ꂽ�Ƃ��Ăяo����郁�\�b�h
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		if(arg0 == gallery){

			Bitmap picture = BitmapFactory.decodeFile(picturesPath + picturesList[arg2]);
			imageView.setImageBitmap(picture);

			String free = "Free:" + (Debug.getNativeHeapFreeSize() / 1024) + "kbytes";
			Toast.makeText(this, free, Toast.LENGTH_LONG).show();
		}
	}
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}