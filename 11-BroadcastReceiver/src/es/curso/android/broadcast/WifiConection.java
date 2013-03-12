package es.curso.android.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;




public class WifiConection extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
			if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
				
				Log.d("WifConnection", "true");
				Toast.makeText(context, "wifi activada", Toast.LENGTH_SHORT).show();
			} else {
				Log.d("WifConnection", "false");
				Toast.makeText(context, "wifi desactivada", Toast.LENGTH_SHORT).show();
			}
									
		}
	}
}