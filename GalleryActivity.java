package com.ams.reminisce;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_view);

        //SDカードが存在しないとき開くダイアログ
        if(!sdcardReadReady()){
        	new AlertDialog.Builder(this).setMessage(R.string.message2).setNeutralButton("OK", this).show();
        	return;
        }
        //SDカードのディレクトリを取得
        String rootDir = Environment.getExternalStorageDirectory().getPath();

        //画像の指定に必要な情報を取得
        File picturesDir = new File(rootDir + "/Pictures");
        picturesPath = picturesDir.getAbsolutePath() + "/";
        picturesList = picturesDir.list();

        //JPG形式に限定したファイル名の一覧を作り直す
        List<String> tmp = new ArrayList<String>();
        for(String s : picturesList){

        	if(s.endsWith("JPG") || s.endsWith("jpg")){

        		tmp.add(s);
        	}
        }
        tmp.toArray(picturesList);

        //サムネールの作成中に開くダイアログ
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getText(R.string.message1));
        dialog.show();

        //サムネールの作成
        thumbnail = new Bitmap[picturesList.length];
        new Thread(this).start();

        //ギャラリーの作成
        gallery = (Gallery)findViewById(R.id.gallery1);
        gallery.setSpacing(2);
        gallery.setAdapter(new GalleryAdapter());
        gallery.setOnItemClickListener(this);

        //大きなイメージビューに初期値の画像を設定
        imageView = (ImageView)findViewById(R.id.imageView1);
        Bitmap picture = BitmapFactory.decodeFile(picturesPath + picturesList[0]);
        imageView.setImageBitmap(picture);

    }
	/**
	 * 追加したスレッドでサムネールを作成する処理
	 */
	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

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
	//SDカードが存在するかどうか調べるメソッド
	private boolean sdcardReadReady(){

		String state = Environment.getExternalStorageState();
		return(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
	}

    /**
     * ダイアログのボタンが押されたとき呼び出されるメソッド
     */
	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ

		switch(arg1){

		case DialogInterface.BUTTON_NEUTRAL:
			arg0.dismiss();
			finish();
		}
	}


	/**
	 * ギャラリーの選択肢に画像を設定するアダプタ
	 */
	public class GalleryAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO 自動生成されたメソッド・スタブ
			return picturesList.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO 自動生成されたメソッド・スタブ
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO 自動生成されたメソッド・スタブ

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
	 * ギャラリーの選択肢が押されたとき呼び出されるメソッド
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自動生成されたメソッド・スタブ

		if(arg0 == gallery){

			Bitmap picture = BitmapFactory.decodeFile(picturesPath + picturesList[arg2]);
			imageView.setImageBitmap(picture);

			String free = "Free:" + (Debug.getNativeHeapFreeSize() / 1024) + "kbytes";
			Toast.makeText(this, free, Toast.LENGTH_LONG).show();
		}
	}
}
