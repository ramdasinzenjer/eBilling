package srt.inz.ebilling;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connectors.Connectivity;
import srt.inz.connectors.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Userdetails extends Activity{
	
	TextView n,cn,ad,ph;
	String sn,scn,sad,sph; String hos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdetails);
		
		n=(TextView)findViewById(R.id.name);
		ad=(TextView)findViewById(R.id.address);
		ph=(TextView)findViewById(R.id.phone);
		
		
		cn=(TextView)findViewById(R.id.consumer);
		SharedPreferences share=this.getSharedPreferences("key", MODE_WORLD_READABLE);
		scn=share.getString("srt","");
		cn.setText("Consumer number: \t"+scn);
		
		Button bk=(Button)findViewById(R.id.back);
		bk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i =new Intent(getApplicationContext(),Options.class);
				startActivity(i);
			}
		});
		
		uview uv=new uview();
		uv.execute();
		parsingmethod();
		
	}
	
	
	class uview extends AsyncTask<String, String, String>
	{
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try
			{			
    			 urlParameters =  "Consnumber=" + URLEncoder.encode(scn, "UTF-8");
			} 
			catch(Exception e)
			{
				System.out.println("Error:"+e);
			}
			hos=Connectivity.excutePost(Constants.BILLVIEW_URL,
                    urlParameters);
            Log.e("You are at", "" + hos);
			return hos;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			parsingmethod();
		}
	}
	public void parsingmethod()
	{
		try
		{
			JSONObject jobject=new JSONObject(hos);
			JSONObject jobject1=jobject.getJSONObject("event");
			JSONArray ja=jobject1.getJSONArray("details");
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				sn=data1.getString("Name");
				sad=data1.getString("Address");
				sph=data1.getString("Phone");
				
				//System.out.println(name+email+phone);
				n.setText("Name: \t"+sn);
				ad.setText("Address: \t"+sad);
				ph.setText("Phone: \t"+sph);
		
			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}

}
