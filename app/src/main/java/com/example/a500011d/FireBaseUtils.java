package com.example.a500011d;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class FireBaseUtils {
    final static long FIVE_MEGABYTE = 1024 * 1024 * 5;
    public static Task<byte[]> downloadToImageView(final Context context, StorageReference storageRef, final ImageView imageView) {

        return storageRef.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for image reference is returns, use this as needed
                Toast.makeText(context, "Download successful", Toast.LENGTH_SHORT).show();
                ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                Drawable drw = Drawable.createFromStream(is, "articleImage");
                imageView.setImageDrawable(drw);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("ERROR", exception.toString());
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static StorageTask<UploadTask.TaskSnapshot> uploadImageToStorage(final Context context, StorageReference storageRef, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        UploadTask uploadTask = storageRef.putBytes(byteArray);
        return uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(context, "Image upload successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getStringFormattedDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // NOTE: this method requires min SDK to be 26
            LocalDateTime t = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
            return t.format(formatter);
        } else {
            /* NOTE: java.util.Date is apparently considered bad (I don't really understand and too
            lazy to look into it now)
            (https://stackoverflow.com/questions/1969442/whats-wrong-with-java-date-time-api)
             but the improved java.time package requires min SDK to be 26. If we ok with setting min
             SDK to 26 then we can remove this one and the if wrapper
             */
            String pattern = "dd MMMM yyyy HH:mm";
            DateFormat df = new SimpleDateFormat(pattern);
            Date today = Calendar.getInstance().getTime();
            String todayAsString = df.format(today);
            return todayAsString;
        }

    }
}
