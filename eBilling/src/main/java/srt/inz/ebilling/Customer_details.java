package srt.inz.ebilling;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connectors.Connectivity;
import srt.inz.connectors.Constants;
import android.app.Activity;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Customer_details extends Activity{
	
	ListView mylist; ListAdapter adapter; 	String hs,Consnumber,Name,Address,Secretcode,Phone,Connectiontype,Section;
	ArrayList<HashMap<String, String>> oslist=new ArrayList<HashMap<String,String>>();
	
	Spinner sp_sect; ArrayAdapter<String> sectlist; String sect;
	
	EditText etn,etcn,etsk,etph,etctyp,etad;
	String sn,scn,ssk,sphn,styp,sad,result; Button brg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_details);
		mylist=(ListView)findViewById(R.id.mlist);
		sp_sect=(Spinner)findViewById(R.id.spinnerSection);
		
		etn=(EditText)findViewById(R.id.editName);
		etcn=(EditText)findViewById(R.id.editConsnumber);
		etsk=(EditText)findViewById(R.id.editSecretcode);
		etad=(EditText)findViewById(R.id.editAddress);
		etph=(EditText)findViewById(R.id.editPhone);
		etctyp=(EditText)findViewById(R.id.editConnectiontype);

		brg=(Button)findViewById(R.id.but_ok);
		
		brg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				sn=etn.getText().toString();
				scn=etcn.getText().toString();
				ssk=etsk.getText().toString();
				sad=etad.getText().toString();
				sphn=etph.getText().toString();
				styp=etctyp.getText().toString();
				
				if(getValidate())
				{
					new CustomRegisterApiTask().execute();
				}
				
			}
		});
		
		String[] sct=getResources().getStringArray(R.array.ele_sect);
		sectlist=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sct);
		
		sectlist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_sect.setAdapter(sectlist);
		sp_sect.setOnItemSelectedListener(new OnItemSelectedListener()
        {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				sect=arg0.getItemAtPosition(arg2).toString();
				Toast.makeText(getApplicationContext(), ""+sect,Toast.LENGTH_LONG).show();
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
		
		
		new CustomerlistApi().execute();
	}

	
	protected class CustomerlistApi extends AsyncTask<String, String, String>
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
				Name=data1.getString("Name");
				Address=data1.getString("Address");
				Secretcode=data1.getString("Secretcode");
				Phone=data1.getString("Phone");
				Connectiontype=data1.getString("Connectiontype");
				Section=data1.getString("Section");
				
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("Consnumber", Consnumber);
	            map.put("Name", Name);
	            map.put("Address", Address);
	            map.put("Secretcode", Secretcode);
	            map.put("Phone", Phone);
	            map.put("Connectiontype", Connectiontype);
	            map.put("Section", Section);
	            
	            map.put("myview", "Name : "+Name+"\n Consumer Number : "+Consnumber+"\n Address : "+Address
	            		+"Connection Type : "+Connectiontype+"\t Section : "+Section);
	            
	            	oslist.add(map);
	            
	            	 adapter = new SimpleAdapter(getApplicationContext(), oslist,
	     	                R.layout.layout_single,
	     	                new String[] {"myview"}, new int[] {R.id.mtext_single});
	     	            mylist.setAdapter(adapter);
	     	            
	     	            mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	     	               
	     				@Override
	     	               public void onItemClick(AdapterView<?> parent, View view,
	     	                                            int position, long id) {               
	     	               Toast.makeText(getApplicationContext(), 
	     	            		   " "+oslist.get(+position).get("Phone"), Toast.LENGTH_SHORT).show();	
	     	               
	     	               }
	     	                });
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	protected class CustomRegisterApiTask extends AsyncTask<String,String,String> {
	    
	    @Override
	    protected String doInBackground(String... params) {


	            String urlParameters = null;
	            try {
	                urlParameters =  "Name=" + URLEncoder.encode(sn, "UTF-8") + "&&"
	                        + "Consnumber=" + URLEncoder.encode(scn, "UTF-8")+ "&&" 
	                        + "Secretcode=" + URLEncoder.encode(ssk, "UTF-8")+ "&&" 
	                        + "Address=" + URLEncoder.encode(sad, "UTF-8")+ "&&" 
	                        + "Phone=" + URLEncoder.encode(sphn, "UTF-8")+ "&&" 
	                        + "Connectiontype=" + URLEncoder.encode(styp, "UTF-8")+"&&"
	                        + "Section=" + URLEncoder.encode(sect, "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }		

	            result = Connectivity.excutePost(Constants.REGISTER_URL,
	                    urlParameters);
	            Log.e("You are at", "" + result);

	       return result;
	    }

	    @Override
	    protected void onPostExecute(String s) {
	        super.onPostExecute(s);
	        
	      //  linlaHeaderProgress.setVisibility(View.GONE);
	        if(result.contains("success"))
	        {
	        	
	        Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_SHORT).show();
	        
	        }
	        else
	        {
	        	Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_SHORT).show();
	        }
	        
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	    }
	}
	
	public boolean getValidate() {
	      
		
		if (sn.length()==0)
        {
            etn.setError("Please enter Name");
            return false;
        }
        
        if (sphn.length()!=10)
        {
            etph.setError("Invalid phone No:");
            return false;
        }
        if (scn.length()==0)
        {
            etcn.setError("Enter the Customer id");
            return false;
        }
        if(styp.length()==0)
        {
        	etctyp.setError("Enter Connection Type");
            return false;
        }
        if(sad.length()==0)
        {
        	etad.setError("Enter Address");
            return false;
        }

        return true;
    }
}
