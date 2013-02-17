package bitmaputils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.Display;

public class BitmapUtils 
{
	public static byte[] bitmapToByteArray(Bitmap bitmap)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
	
	public static Bitmap byteArrayToBitmap(byte[] byteArray)
	{
		Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		return bitmap;
	}
	
	public static Bitmap loadImageFromUri(Activity activity, Uri uriImageFile)
	{
		Bitmap bmp = null;
		
		//: 풀스크린을 가정하고 현재 디바이스의 화면 크기를 가져온다
		Display currentDisplay = activity.getWindowManager().getDefaultDisplay();
		int dw = currentDisplay.getWidth();
		int dh = currentDisplay.getHeight();
		
		try
		{
			//: 캡쳐된 이미지의 너비와 높이를 구한다.
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			bmp = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uriImageFile), null, bmpFactoryOptions);
			
			int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth / (float)dw);
			int heightRatio= (int)Math.ceil(bmpFactoryOptions.outHeight / (float)dh);
			
			//: 두 비율 다 1보다 크면 이미지의 가로, 세로 중 한쪽이 화면보다 크다.
			if(widthRatio > 1 && heightRatio > 1)
			{
				if(heightRatio > widthRatio)
				{
					//: 높이 비율이 더 커서 그에 따라 맞춘다
					bmpFactoryOptions.inSampleSize = heightRatio;
				}
				else
				{
					//: 너비 비율이 더 커서 그에 따라 맞춘다
					bmpFactoryOptions.inSampleSize = widthRatio;
				}
			}
			
			//: 실제로 디코딩해서 비트맵을 가져온다.
			bmpFactoryOptions.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uriImageFile), null, bmpFactoryOptions);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			//: 이미지를 반환한다.
			return bmp;
		}
	}
}
