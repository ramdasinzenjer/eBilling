package srt.inz.ebilling;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Options extends Activity{
	Button o1,o2,o3,o4;
	TextView t;	String s,respo;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		o1=(Button)findViewById(R.id.opt1);
		o2=(Button)findViewById(R.id.opt2);
		o3=(Button)findViewById(R.id.opt3);
		
		
		t=(TextView)findViewById(R.id.num);
		
		SharedPreferences share=this.getSharedPreferences("key", MODE_WORLD_READABLE);
		s=share.getString("srt","");
		t.setText(s);
				
		
		o1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(getApplicationContext(),Billgen.class);
				startActivity(i);
			}
		});
		
		o2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(getApplicationContext(),Userdetails.class);
				startActivity(i);
			}
		});
		
		o3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(getApplicationContext(),Aboutus.class);
				startActivity(i);
			}
		});
	}
	
	
}
