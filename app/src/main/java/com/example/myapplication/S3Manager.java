/*
package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.regions.Regions;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class S3Manager {

    private final Context context;
    private final TransferUtility transferUtility;
    private final AmazonS3 s3Client;

    public S3Manager(Context context) {
        this.context = context;

        // Replace "YOUR_ACCESS_KEY" and "YOUR_SECRET_KEY" with your actual AWS credentials
        AWSCredentials credentials = new BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_ACCESS_KEY);

        // Initialize TransferUtility
        this.transferUtility = TransferUtility.builder()
                .context(context)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .s3Client(new AmazonS3Client(credentials, Region.getRegion(Regions.AP_NORTHEAST_2))) // Replace with your AWS region
                .build();

        // Initialize AmazonS3 client
        this.s3Client = new AmazonS3Client(credentials, Region.getRegion(Regions.AP_NORTHEAST_2)); // Replace with your AWS region
    }

    public List<S3ObjectSummary> listObjectsInFolder(String bucketName, String folderPath) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            Callable<List<S3ObjectSummary>> listObjectsCallable = () -> {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                        .withBucketName(bucketName)
                        .withPrefix(folderPath);

                ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
                return objectListing.getObjectSummaries();
            };

            Future<List<S3ObjectSummary>> future = executorService.submit(listObjectsCallable);

            // Wait for the result
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Shut down the executor service
            executorService.shutdown();
        }
    }

    public void downloadAndDisplayImagesInFolder(String bucketName, String folderPath, ImageView imageView) {
        List<S3ObjectSummary> objectSummaries = listObjectsInFolder(bucketName, folderPath);

        for (S3ObjectSummary objectSummary : objectSummaries) {
            String objectKey = objectSummary.getKey();
            new DownloadAndDisplayImageTask(this, bucketName, objectKey, imageView).execute();
        }
    }

    private static class DownloadAndDisplayImageTask extends AsyncTask<Void, Void, Bitmap> {

        private final WeakReference<S3Manager> s3ManagerRef;
        private final String bucketName;
        private final String objectKey;
        private final WeakReference<ImageView> imageViewRef;

        DownloadAndDisplayImageTask(S3Manager s3Manager, String bucketName, String objectKey, ImageView imageView) {
            this.s3ManagerRef = new WeakReference<>(s3Manager);
            this.bucketName = bucketName;
            this.objectKey = objectKey;
            this.imageViewRef = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            S3Manager s3Manager = s3ManagerRef.get();
            if (s3Manager != null) {
                File file = new File(s3Manager.context.getCacheDir(), objectKey);

                TransferObserver downloadObserver = s3Manager.transferUtility.download(
                        bucketName,
                        objectKey,
                        file
                );

                try {
                    // Block until the download completes
                    downloadObserver.setTransferListener(new TransferListener() {
                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            if (state == TransferState.COMPLETED) {
                                // Decode and return the downloaded bitmap
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                onPostExecute(bitmap);
                            }
                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            // Update progress if needed
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            // Handle error
                        }
                    });

                    // Return null here as onPostExecute will handle setting the bitmap
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = imageViewRef.get();
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}*/
