package com.nationality.LocationUtils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nationality.LoginActivity;
import com.nationality.SignupActivity;
import com.nationality.model.GetPrivacySettingsRequest;
import com.nationality.model.UpdateLocationRequest;
import com.nationality.retrofit.RestHandler;
import com.nationality.retrofit.RetrofitListener;
import com.nationality.utils.Constants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class LocationHandlerService  extends IntentService implements RetrofitListener
{    
    SharedPreferences pref;
    Editor editor;
	private RestHandler rest;

	public LocationHandlerService()
    {
        super("LocationHandlerService");
    }
    public static final String TAG = "LocationHandlerService";
 
    @Override
    protected void onHandleIntent(Intent intent) 
    {
    	pref= getSharedPreferences("Location", Context.MODE_PRIVATE);
    	Location location = (Location)intent.getParcelableExtra("location");
    	if(location!=null)
    	{
			String lat = String.valueOf(location.getLatitude());
			String lng = String.valueOf(location.getLongitude());

			String addr = getAddr(location.getLatitude(), location.getLongitude());

    		editor = pref.edit();
    		editor.putString("Lat", location.getLatitude()+"");
    		editor.putString("Long", location.getLongitude() + "");
    		editor.putString("Time", getDate(location.getTime(), "yyyy-MM-dd HH:mm:ss"));
    		editor.commit();

			if (!Constants.isLocationUpdated) {
				rest = new RestHandler(this, this);
				rest.makeHttpRequest(rest.retrofit.create(RestHandler.RestInterface.class).
						setLocation(Constants.getUserId(getApplicationContext()), addr, lat, lng), "setLocation");
			}

		}
		else {
			//Toast.makeText(getApplicationContext(), "location not found", Toast.LENGTH_LONG).show();
		}
    }

	private String getAddr(double latitude, double longitude) {
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = geoCoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String address = "";
		if (addresses != null && addresses.size() > 0) {
			String movedLocality = addresses.get(0).getSubLocality();
			int addlineIndex = addresses.get(0).getMaxAddressLineIndex();
			String strFullAddr = addresses.get(0).getAddressLine(0);
			for (int i = 1; i < addlineIndex; i++) {
				strFullAddr = strFullAddr + ", " + addresses.get(0).getAddressLine(i);
				address=strFullAddr;
			}
		}
		return address;
	}

	public String getDate(long milliSeconds, String dateFormat)
	{	  
	    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);	 
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(milliSeconds);
	    return formatter.format(calendar.getTime());
	}

	@Override
	public void onSuccess(Call call, Response response, String method) {
		if(response!=null && response.code()==200) {

			if (method.equalsIgnoreCase("setLocation")) {
				UpdateLocationRequest req_Obj = (UpdateLocationRequest) response.body();
				if (!req_Obj.getResult().getError()) {
					SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).edit();
					editor.putString(Constants.strShPrefUserLocationSet,"Yes");
					editor.putString(Constants.strShPrefAddress,req_Obj.getResult().getData().getAddress());
                    editor.commit();
					Constants.isLocationUpdated = true;
					/*if (SignupActivity.mGoogleApiClient.isConnected()) {
						SignupActivity.mGoogleApiClient.disconnect();
						//LocationServices.FusedLocationApi.removeLocationUpdates(LoginActivity.mGoogleApiClient, this);
						//LocationServices.FusedLocationApi.removeLocationUpdates(LoginActivity.mGoogleApiClient, this);
					}*/

				} else {
					//Toast.makeText(getActivity(), "Server Response Error.. ", Toast.LENGTH_LONG).show();
				}

			}
		}
	}

	@Override
	public void onFailure(String errorMessage) {
		Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
	}

	private class SaveLocationOnRegistration extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected Boolean doInBackground(Void... params) {
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result){

			}
		}

	}
}
