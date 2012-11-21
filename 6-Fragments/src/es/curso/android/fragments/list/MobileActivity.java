package es.curso.android.fragments.list;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import es.curso.android.fragments.list.MyListFragment.IListFragment;

public class MobileActivity extends FragmentActivity implements IListFragment
{
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        // Create the list fragment and add it as our sole content.
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
        	MyListFragment list = new MyListFragment();       
        	getSupportFragmentManager().beginTransaction().add(android.R.id.content, list).commit();
        }	        
	}

	@Override
	public void itemClick(Integer imageResource) 
	{
		
		Intent i = new Intent (this,MobileActivityContent.class);
		i.putExtra("IMAGE", imageResource);		
		
		startActivity(i);
	}
}
