package srt.inz.ebilling;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Testclass extends Activity{
TextView wel;
	
	EditText cnum,code; String scnum,scode;
	
	Button k;
	Spinner sect;
	ArrayAdapter<String> assect;
	String ssect; String sh1;
	String[] sc={"Select Section ","Thiruvananthapuram","Attingal","Varkala","Kadakavur",
			"Chirayinkeezhu","Balaramapuram","Neyyattinkara","Kattakada","Vizhinjam",
			"Parassala","Nedumangad","Kilimanoor","Vithura","Neyyardam",
			"Kallada","Alamcode"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        cnum=(EditText)findViewById(R.id.Consumernum);
        code=(EditText)findViewById(R.id.code);
        
        
        sect=(Spinner)findViewById(R.id.section_spinner);
        assect=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sc);
	    assect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sect.setAdapter(assect);
        sect.setOnItemSelectedListener(new OnItemSelectedListener()
        {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				ssect=arg0.getItemAtPosition(arg2).toString();
				Toast.makeText(getApplicationContext(), ""+ssect,Toast.LENGTH_LONG).show();
				SharedPreferences share1=getSharedPreferences("key1", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed1=share1.edit();
				ed1.putString("srt1",ssect);
				ed1.commit();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        
        wel=(TextView)findViewById(R.id.textView1);
        cnum=(EditText)findViewById(R.id.Consumernum);
        code=(EditText)findViewById(R.id.code);
        
        k=(Button)findViewById(R.id.ok);
        k.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Intent i= new Intent(getApplicationContext(),Options.class);
				startActivity(i);*/
				
				scnum=cnum.getText().toString();
				scode=code.getText().toString();
				SharedPreferences share=getSharedPreferences("key", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("srt",scnum);
				ed.commit();
				
				
				conslog cl=new conslog();
				cl.execute();
				
			}
		});
        
    }
    
    public class conslog extends AsyncTask<Void, Void, Void>
	{
		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try
			{	String serv = getResources().getString(R.string.server);
				DefaultHttpClient h=new DefaultHttpClient();
				ResponseHandler<String> respp=new BasicResponseHandler();
				HttpPost postMethod=new HttpPost(serv+"conslogin.php");
				List<NameValuePair>namevaluepairs=new ArrayList<NameValuePair>(2);
				namevaluepairs.add(new BasicNameValuePair("Consnumber", scnum));
				namevaluepairs.add(new BasicNameValuePair("Secretcode", scode));
				postMethod.setEntity(new UrlEncodedFormEntity(namevaluepairs));
				sh1=h.execute(postMethod,respp);
				
			}
			
			catch(Exception e)
			{
				System.out.println("Error:"+e);
			}
			
			
			return null; 
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			if(sh1.contains("success"))
			{
				Toast.makeText(getApplicationContext(), sh1, Toast.LENGTH_LONG).show();
				Intent i=new Intent(getApplicationContext(),Options.class);
				startActivity(i);
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), sh1, Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
			
			
			
		}
		
	}
    
    


}
