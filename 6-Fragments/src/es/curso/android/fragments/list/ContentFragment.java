package es.curso.android.fragments.list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import es.curso.android.fragments.R;

public class ContentFragment extends Fragment 
{
	private Integer mResImage;
	private View myView;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);	
	}

	
	public void setImage (Integer res)
	{		
		mResImage = res; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		myView = inflater.inflate(R.layout.content, container, false);
	
		updateImage();
		
		return myView;
	}

	public void updateImage ()
	{
		ImageView image = (ImageView) myView.findViewById(R.id.image);
		if (image != null && mResImage != null)
			image.setImageDrawable(getResources().getDrawable(mResImage)); 
	}
}
