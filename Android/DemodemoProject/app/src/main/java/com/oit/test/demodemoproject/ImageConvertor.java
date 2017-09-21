package com.oit.test.demodemoproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;



public class ImageConvertor {
    String imageString;
    byte [] imageBytes;
     Context context;
    String imagePath;
    public String imageToBase64String(Context context, String img_Decodable_Str){
        this.context = context;
        imagePath = img_Decodable_Str;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        imageBytes = byteArrayOutputStream.toByteArray();
        imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return imageString;
    }
    public Bitmap base64StringTImage(String encoded){
        imageBytes = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
       return decodedImage;
    }
}
