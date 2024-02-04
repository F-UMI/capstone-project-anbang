/*
package com.example.anbang_.temp;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.anbang_.BuildConfig;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    Button btn_selectimage;
    ImageView iv_userimage;
    Button btn_sendtoserver;

    ImageView iv_testimage;
    TextView tv_testuserfilename;

    String imgName="userimage.png";

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddhhmm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btn_selectimage=findViewById(R.id.btnSelectimage);
        iv_userimage=findViewById(R.id.ivUserimage);
        btn_sendtoserver=findViewById(R.id.btnSendtoserver);

        iv_testimage=findViewById(R.id.ivTestimage);
        tv_testuserfilename=findViewById(R.id.tvTestuserfilename);


        //---------------- 사진 불러오기 클릭 ---------------
        btn_selectimage.setOnClickListener(view -> startCropActivity());


        //---------------- 서버로 사진 전송 클릭 ---------------
        btn_sendtoserver.setOnClickListener(view -> {
            try {
                String imgpath = getCacheDir() + "/" + imgName;   // 내부 저장소에 저장되어 있는 이미지 경로
                Bitmap bm = BitmapFactory.decodeFile(imgpath);
                iv_testimage.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋

                File cachefile = new File(imgpath);
                String time = getTime();
                String devicemodel = getDeviceModel();
                String cachefilename= devicemodel + "_" + time + imgName;
                tv_testuserfilename.setText(cachefilename);

                uploadWithTransferUtilty(cachefilename, cachefile);

                Toast.makeText(getApplicationContext(), "캐시파일 접근 성공", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "캐시파일 접근 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }//onCreate end

    //---------------- 사진 불러오기 ---------------
    private void startCropActivity() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(resultUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    saveBitmapToJpeg(imgBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                iv_userimage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    //이미지뷰 사진 캐시에 저장
    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getCacheDir(), imgName);    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
            Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
        }
    }

    //현재시간 가져오기
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    //모델명 가져오기
    public static String getDeviceModel() {
        return Build.MODEL;
    }


    //---------------- S3 업로드 ---------------
    public void uploadWithTransferUtilty(String fileName, File file) {

        AWSCredentials awsCredentials = new BasicAWSCredentials(
                BuildConfig.ACCESS_KEY, BuildConfig.SECRET_ACCESS_KEY);
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));

        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(this).build();
        TransferNetworkLossHandler.getInstance(this);

        TransferObserver uploadObserver = transferUtility.upload(BuildConfig.BUCKET_NAME, fileName, file);

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    // Handle a completed upload
                }
            }

            @Override
            public void onProgressChanged(int id, long current, long total) {
                int done = (int) (((double) current / total) * 100.0);
                Toast.makeText(getApplicationContext(), "S3 업로드 성공", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(getApplicationContext(), "S3 업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
*/
