package srt.inz.ebilling;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connectors.Connectivity;
import srt.inz.connectors.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

 @SuppressWarnings("deprecation")
public class Billgen extends Activity{
	
	EditText date; String sdate;		String respo,scn;
	
	EditText pmr,cmr;
	String spmr,scmr;
	
	Button ok;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.billgen);
		date=(EditText)findViewById(R.id.bldate);
		pmr=(EditText)findViewById(R.id.pmread);
		cmr=(EditText)findViewById(R.id.currread);
		spmr=pmr.getText().toString();
		scmr=cmr.getText().toString();
		
		/*Calendar cal=Calendar.getInstance();
		Date d=cal.getTime();
		sdate=d.toString(); date.setText(sdate.substring(0, 10) +"-"+sdate.substring(29));*/
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calobj = Calendar.getInstance();
		//System.out.println(df.format(calobj.getTime()));
		sdate=df.format(calobj.getTime()); date.setText(sdate);
		
		SharedPreferences share=this.getSharedPreferences("key", MODE_WORLD_READABLE);
		scn=share.getString("srt","");
		
		
		ok=(Button)findViewById(R.id.bill);
		ok.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi") @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spmr=pmr.getText().toString();
				scmr=cmr.getText().toString();
				
				if(spmr.isEmpty() && scmr.isEmpty())
				{	
					Toast.makeText(getApplicationContext(), "Field empty", Toast.LENGTH_LONG).show();
				}
				else
				{
					Intent i= new Intent(getApplicationContext(),Billview.class);
					startActivity(i);
					
				}
				
			}
		});
		
		billg bv=new billg();
		bv.execute();
		parsingmethod();
		
			
		pmr.setEnabled(false);
		date.setEnabled(false);
		cmr.setEnabled(false);
	}
	
	
	
	
	class billg extends AsyncTask<String, String, String>
	{

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
			respo=Connectivity.excutePost(Constants.BILLGEN_URL,
                    urlParameters);
            Log.e("You are at", "" + respo);
			return respo;
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
			JSONObject jobject=new JSONObject(respo);
			JSONObject jobject1=jobject.getJSONObject("event");
			JSONArray ja=jobject1.getJSONArray("details");
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				spmr=data1.getString("Previousreading");
				scmr=data1.getString("Currentreading");
				
				//System.out.println(name+email+phone);
				pmr.setText(spmr);
				cmr.setText(scmr);
				
				SharedPreferences share=getSharedPreferences("rd", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("rd0",spmr);
				ed.commit();
				
				SharedPreferences share1=getSharedPreferences("rd1", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed1=share.edit();
				ed.putString("rd2",scmr);
				ed.commit();
				
				
		
			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
}
