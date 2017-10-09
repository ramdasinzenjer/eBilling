package srt.inz.ebilling;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connectors.Connectivity;
import srt.inz.connectors.Constants;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Updatereading extends Activity{
	
	Spinner sp_custom;
	ArrayAdapter<String> s1;
	List<String> lables = new ArrayList<String>(); String scust,hs,Consnumber,sdate,response,respo; 
	
	String scmr,snmr; EditText pmr,cmr,edat; Button ok;
	
	ArrayList<HashMap<String, String>> oslist=new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readupdate);
		pmr=(EditText)findViewById(R.id.pmread01);
		cmr=(EditText)findViewById(R.id.currread01);
		edat=(EditText)findViewById(R.id.bldate01);
		ok=(Button)findViewById(R.id.bill01);
		sp_custom=(Spinner)findViewById(R.id.spinner_customers);
		edat.setEnabled(false);
		
		new CustomerlistApi().execute();
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 snmr=cmr.getText().toString();
				 scmr=pmr.getText().toString();
				 new UpdateReadingApi().execute();
				 
			}
		});
		
	}
	
	
	public class CustomerlistApi extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
				String urlParameters = null;
	    		try
				{	
					
	    			 urlParameters ="" ;
	    			 
				}
				
				catch(Exception e)
				{
					System.out.println("Error:"+e);
				}
				
	    		hs=Connectivity.excutePost(Constants.GETCUSTOMERLIST_URL,
	                    urlParameters);
	            Log.e("You are at", "" + hs);
			return hs;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(hs.contains("success"))
			{
				parsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), hs, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	public void parsingmethod()
	{
		try
		{
			JSONObject jobject=new JSONObject(hs);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();
			/*oslist.add(null);
			sp_custom.setAdapter(null);*/
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				Consnumber=data1.getString("Consnumber");
				
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("Consnumber", Consnumber);
	            	oslist.add(map);
	            
	            lables.add(oslist.get(i).get("Consnumber"));
	            
	            s1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lables);
		        s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    sp_custom.setAdapter(s1);
	            
			    sp_custom.setOnItemSelectedListener(new OnItemSelectedListener()
		        {

			    	
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						scust=arg0.getItemAtPosition(arg2).toString();
						((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
						
						Toast.makeText(getApplicationContext(), ""+scust, Toast.LENGTH_SHORT).show();
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Calendar calobj = Calendar.getInstance();
						//System.out.println(df.format(calobj.getTime()));
						sdate=df.format(calobj.getTime()); 
						edat.setText(sdate);
						new BillReadApi().execute();
					
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub	
					}
		        	
		        });
			    
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	protected class UpdateReadingApi extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters = null;
    		try
			{	
				
    			 urlParameters ="Consnumber="+URLEncoder.encode(scust,"UTF-8")
    					 +"&&"+"Previousreading="+URLEncoder.encode(scmr,"UTF-8")
    					 +"&&"+"Currentreading="+URLEncoder.encode(snmr,"UTF-8")
    					 +"&&"+"timedate="+URLEncoder.encode(sdate,"UTF-8");
    			 
			}
			
			catch(Exception e)
			{
				System.out.println("Error:"+e);
			}
			
    		response=Connectivity.excutePost(Constants.UPDATEREADING_URL,
                    urlParameters);
            Log.e("You are at", "" +response);
		return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
		}
	}
	
	public class BillReadApi extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try
			{			
    			 urlParameters =  "Consnumber=" + URLEncoder.encode(scust, "UTF-8");	
			} 
			catch(Exception e)
			{
				System.out.println("Error:"+e);
			}
			respo=Connectivity.excutePost(Constants.BILLGEN_URL,
                    urlParameters);
            Log.e("You are at", "" + respo);
			return respo;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			parsingmethodread();
		}
	}
	public void parsingmethodread()
	{
		try
		{
			JSONObject jobject=new JSONObject(respo);
			JSONObject jobject1=jobject.getJSONObject("event");
			JSONArray ja=jobject1.getJSONArray("details");
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				scmr=data1.getString("Currentreading");
				
				pmr.setText(scmr);
				pmr.setEnabled(false);
				
		
			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
}
