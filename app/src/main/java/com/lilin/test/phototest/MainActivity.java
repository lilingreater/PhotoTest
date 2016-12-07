package com.lilin.test.phototest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private GridAdapter adapter;
    //呈现图片的gridview
    protected GridView gv_pushimage;
    private EditText et_quality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        BitmapManager.tempSelectBitmap = new ArrayList<ImageItem>();
        adapter = new GridAdapter(this, BitmapManager.tempSelectBitmap);
        gv_pushimage.setAdapter(adapter);
        CommonUtils.setGideViewHeightBasedOnChildren(gv_pushimage, 4);

        gv_pushimage.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == BitmapManager.tempSelectBitmap.size()) {
                    showChoosePicDialog();
                }
            }
        });
        adapter.setOnItemDeleteClick(new GridAdapter.onItemDeleteClick() {
            @Override
            public void onItemDelete(int position) {
                BitmapManager.tempSelectBitmap.remove(position);
                adapter.update(BitmapManager.tempSelectBitmap);
                CommonUtils.setGideViewHeightBasedOnChildren(gv_pushimage, 4);
            }
        });
    }

    private void initView() {
        gv_pushimage = (GridView) findViewById(R.id.gv_pushimage);
        et_quality = (EditText) findViewById(R.id.et_quality);
    }

    private String imageFilePath;

    /**
     * 选择图片
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择照片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        // 拍照
                        //设置图片的保存路径,作为全局变量
                        String fileName = String.valueOf(System.currentTimeMillis());
                        try {
                            imageFilePath = FileUtils.createSDDir("temp").getPath() + "/" + fileName + ".JPEG";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        File temp = new File(imageFilePath);
                        Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
                        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
                        it.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
                        startActivityForResult(it, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) { // 如果返回码是可以用的
            switch (requestCode) {
                case CHOOSE_PICTURE://图库返回
                    if (data != null) {
                        setImageToView(data);
                    }

                    break;
                case TAKE_PICTURE:
                    Bitmap bmp = BitmapFactory.decodeFile(imageFilePath);
                    comp(bmp);
                    break;
            }
        }
    }


    /**
     * 保存的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        try {
            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);  //获取照片路径
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

            Log.i("wechat", "压缩前图片的大小" + (bitmap.getByteCount() / 1024 / 1024)
                    + "M宽度为" + bitmap.getWidth() + "高度为" + bitmap.getHeight());//压缩前图片的大小37M宽度为2336高度为4160

            comp(bitmap);//comp压缩bitmap图片的大小4M宽度为1168高度为2080//慢
//            Bitmap comp = ImageUtils.compQuality(bitmap, et_quality.getText().toString());//compQuality压缩后图片的大小37M宽度为2336高度为4160bytes.length=  661KB 50 //compQuality压缩后图片的大小37M宽度为2336高度为4160bytes.length=  234KB  10
//            Bitmap comp = ImageUtils.compInSampleSize(picturePath);//compInSampleSize压缩后图片的大小9M宽度为1168高度为2080
//            Bitmap comp = ImageUtils.compRGB565(picturePath);// 压缩后图片的大小18M宽度为2336高度为4160
//            Bitmap comp = ImageUtils.compMartix(bitmap);//压缩后图片的大小9M宽度为1168高度为2080
        } catch (Exception e) {
            // TODO Auto-generatedcatch block
            e.printStackTrace();
        }
    }

    /**
     * @param image
     * @return
     */
    private void comp(final Bitmap image) {
        new AsyncTask<String, String, Bitmap>() {
            private ImageItem takePhoto;
            private String imagePath;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                takePhoto = new ImageItem();
                takePhoto.setBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.icon_image_default)).getBitmap());
                BitmapManager.tempSelectBitmap.add(takePhoto);//占位
                adapter.update(BitmapManager.tempSelectBitmap);
                CommonUtils.setGideViewHeightBasedOnChildren(gv_pushimage, 4);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap comp = ImageUtils.comp(image);
                imagePath = FileUtils.saveBitmapFile(comp);//保存到本地临时文件
                return comp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                ImageItem takePhoto1 = new ImageItem();
                takePhoto1.setBitmap(result);
                BitmapManager.tempSelectBitmap.remove(takePhoto);//移除占位
                BitmapManager.tempSelectBitmap.add(takePhoto1);
                adapter.update(BitmapManager.tempSelectBitmap);
                CommonUtils.setGideViewHeightBasedOnChildren(gv_pushimage, 4);
            }
        }.execute();
    }

}
