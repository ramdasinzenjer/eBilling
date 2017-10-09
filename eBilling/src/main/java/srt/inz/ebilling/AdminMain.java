package srt.inz.ebilling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdminMain extends Activity{
	
	Button bcustomer,bmeterreading; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminmain);
		bcustomer=(Button)findViewById(R.id.bt_addConsumer);
		bmeterreading=(Button)findViewById(R.id.bt_addBill);
		
		bcustomer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Customer_details.class);
				startActivity(i);
			}
		});
		
		bmeterreading.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),Updatereading.class);
				startActivity(i);
				
			}
		});
	}

}
