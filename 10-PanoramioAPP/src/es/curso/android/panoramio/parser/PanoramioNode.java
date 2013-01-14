package es.curso.android.panoramio.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class PanoramioNode implements Parcelable
{				
	
	public static final Parcelable.Creator<PanoramioNode> CREATOR = 
			new Parcelable.Creator<PanoramioNode>() {
		
		public PanoramioNode createFromParcel(Parcel in) {
			return new PanoramioNode(in);
		}

		public PanoramioNode[] newArray(int size) {
			return new PanoramioNode[size];
		}
	};
	
	
	public String name;
	public String info_url;
	public String photo_url;
	public String thumb_url;
	public String date;
	
	public Double latitude;
	public Double longitude;
	
	private Bitmap mPhotoThumb = null; 
	
	public PanoramioNode ()
	{		
	}
	
	
	public PanoramioNode (Parcel in)
	{		
		name = (String) in.readString();
		info_url = (String) in.readString();
		photo_url = (String) in.readString();
		thumb_url = (String) in.readString();
		date = (String) in.readString();
	    		
		// Read Double
		latitude = (Double) in.readDouble();
		longitude = (Double) in.readDouble();
		
		
		
		
		//Read bitmaps
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int b;

		while((b = in.readByte()) != -1)
			byteStream.write(b);
		
		if (byteStream.size() > 0)
		{
			byte bitmapBytes[] = byteStream.toByteArray();
			mPhotoThumb = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
		}
		
	}
	
	
	public Bitmap getPhoto ()
	{
		return getBitmapFromURL(photo_url);
	}
	
	public Bitmap getPhotoThumb ()
	{
		if (mPhotoThumb == null)
		{
			mPhotoThumb = getBitmapFromURL(thumb_url);
		}
		
		return mPhotoThumb;
	}
	
	private Bitmap getBitmapFromURL(String urlBitmap) {
	    try {
	        URL url = new URL(urlBitmap);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);	        
	        return myBitmap;
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		
		// Write String
		out.writeString(name);
		out.writeString(info_url);
		out.writeString(photo_url);
		out.writeString(thumb_url);
		out.writeString(date);


		// Write Double
		out.writeDouble(latitude);
		out.writeDouble(longitude);	


		// Write Bitmap		
		if (mPhotoThumb != null)
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();		
			mPhotoThumb.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
			byte bitmapBytes[] = byteStream.toByteArray();
			out.writeByteArray (bitmapBytes, 0, bitmapBytes.length);
		}
		
	}
	
	
	
	
}