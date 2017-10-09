package srt.inz.ebilling;

import java.net.URLEncoder;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Home extends Activity {
TextView wel;
	
	EditText cnum,code; String scnum,scode;
	
	Button k;
	Spinner sect;
	ArrayAdapter<String> assect;
	String ssect; String sh1;
	String[] sc={"Select Section ","Trivandrum","Attingal","Nemom","Kadakavur"};
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
				
				if(scnum.equals("admin")||scode.equals("admin"))
				{
					Intent i=new Intent(Home.this,AdminMain.class);
					startActivity(i);
				}
				else
				{
				conslog cl=new conslog();
				cl.execute();
				}
			}
		});
        
    }
    
    public class conslog extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String urlParameters = null;
    		try
			{	
				
    			 urlParameters =  "Section=" + URLEncoder.encode(ssect, "UTF-8") +"&&"
    					 +"Consnumber=" + URLEncoder.encode(scnum, "UTF-8") +"&&"
    					 +"Secretcode=" + URLEncoder.encode(scode, "UTF-8");
				
			}
			
			catch(Exception e)
			{
				System.out.println("Error:"+e);
			}
			
    		sh1=Connectivity.excutePost(Constants.LOGIN_URL,
                    urlParameters);
            Log.e("You are at", "" + sh1);
			return sh1; 
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			if(sh1.contains("success"))
			{
				Toast.makeText(getApplicationContext(), "Succesfully Logged in...", Toast.LENGTH_SHORT).show();
				Intent i=new Intent(getApplicationContext(),Options.class);
				startActivity(i);
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
			
			
			
		}
		
	}
    
    
    
    
    
    
}
