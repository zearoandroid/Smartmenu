package com.zearoconsulting.smartmenu.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by saravanan on 04-09-2016.
 */
public class FileUtils {

    // Find the SD Card path
    public static  final File filepath = Environment.getExternalStorageDirectory();

    // Create a new folder in SD Card
    public static  final File mImgDir = new File(filepath.getAbsolutePath()+ "/SmartMenu Images/");
    public static  final File mVideoDir = new File(filepath.getAbsolutePath()+ "/SmartMenu Videos/");

    public static Bitmap bitmap = null;
    public static OutputStream output = null;

    public static String storeImage(String image, long prodId, Bitmap defaultMap){

        if(!mImgDir.exists())
            mImgDir.mkdirs();

        //set the image name as productid
        String imageName = prodId+".png";

        // Create a name for the saved image
        File file = new File(mImgDir, imageName);

        if(file.exists())
            file.delete();
        else
            file = new File(mImgDir, imageName);

        if(image.equals(""))
            bitmap = defaultMap;
        else
            bitmap = Common.decodeBase64(image);

        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String getDirectoryPath = file.getAbsolutePath();
        return getDirectoryPath;
    }

    public static String storeVideo(long prodId, InputStream in){

        if(!mVideoDir.exists())
            mVideoDir.mkdirs();

        //set the image name as productid
        String videoName = prodId+".mp4";

        // Create a name for the saved image
        File file = new File(mVideoDir, videoName);

        if(file.exists())
            file.delete();
        else
            file = new File(mVideoDir, videoName);

        try {

            output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                output.write(buffer, 0, len1);
            }

            output.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        String getDirectoryPath = file.getAbsolutePath();
        return getDirectoryPath;
    }

    public static String storeMultiImage(String image, long prodId, Bitmap defaultMap,int val){

        if(!mImgDir.exists())
            mImgDir.mkdirs();

        //set the image name as productid
        String imageName = prodId+"_"+val+".png";

        // Create a name for the saved image
        File file = new File(mImgDir, imageName);

        if(file.exists())
            file.delete();
        else
            file = new File(mImgDir, imageName);

        if(image.equals(""))
            bitmap = defaultMap;
        else
            bitmap = Common.decodeBase64(image);

        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String getDirectoryPath = file.getAbsolutePath();
        return getDirectoryPath;
    }

    public static String readImage(long prodId){

        return null;
    }
    public static void deleteImages(){
        deleteDir(mImgDir);
    }

    public static boolean deleteDir(File dir) {
        try {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }

            return dir.delete();
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public static void deleteVideos(){
        deleteVideoDir(mVideoDir);
    }

    public static boolean deleteVideoDir(File dir) {
        try {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }

            return dir.delete();
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }
}

