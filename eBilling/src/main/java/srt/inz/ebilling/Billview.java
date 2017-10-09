package srt.inz.ebilling;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connectors.Connectivity;
import srt.inz.connectors.Constants;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Billview extends Activity{
	TextView cn,sect,dt,Nam,Phn,Ads,Prd,Crd,Usg,Mr,Ed,Fc,Ta;
	String ssect,sdt,sNam,sPhn,sAds,sPrd,sCrd,sUsg,sEd,sTa;
	
	String dbsect,type;
	//private static final String DBL_FMT = "##.####";
	
	Integer fc=40, cfc=360; Integer mr=12; // for commercial: fixed charge=360rs , meter rent=12rs
	
	String sFc,sMr,hos;	String spr,scr;
	
	String bvscn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.billvw);
		cn=(TextView)findViewById(R.id.cnsno);
		
			sect=(TextView)findViewById(R.id.elsection);		//electrical section shared from first page (Home)
			
			dt=(TextView)findViewById(R.id.dat);			//date using inbuilt method should be entered to db table billdet
			
			Nam=(TextView)findViewById(R.id.nam);			//name	should be retrieved from db table consdet
			Phn=(TextView)findViewById(R.id.phn);			//phone	should be retrieved from db table consdet
			Ads=(TextView)findViewById(R.id.add);			//address	should be retrieved from db table consdet
			
			Prd=(TextView)findViewById(R.id.prerdng);		//previous reading should be retrieved from db table billdet
			Crd=(TextView)findViewById(R.id.currdng);		//current reading	entered through device
			
			Usg=(TextView)findViewById(R.id.usag);			//usage should be calculated using method
			Mr=(TextView)findViewById(R.id.mrent);			//meter rent fixed
			Ed=(TextView)findViewById(R.id.eduty);			// duty calculation required
			Fc=(TextView)findViewById(R.id.fcharge);		//fixed charge
			Ta=(TextView)findViewById(R.id.tamt);			//total amount calculation required
			
			SharedPreferences share1=this.getSharedPreferences("key1", MODE_WORLD_READABLE);
			ssect=share1.getString("srt1","");
			sect.setText(ssect);
		
		
		SharedPreferences share=this.getSharedPreferences("key", MODE_WORLD_READABLE);
		bvscn=share.getString("srt","null");
		cn.setText("Consumer No: "+bvscn);
		
		sFc=fc.toString(); sMr=mr.toString();
		Fc.setText("Fixed Charge: "+sFc+" Rs"); Mr.setText("Meter Rent: "+sMr+" Rs");
		
		/*Calendar cal=Calendar.getInstance();
		Date d=cal.getTime();
		sdt=d.toString(); 
		
		dt.setText("Date: "+sdt.substring(0, 10) +""+sdt.substring(29));
				*/
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calobj = Calendar.getInstance();
		System.out.println(df.format(calobj.getTime()));
		sdt=df.format(calobj.getTime()); dt.setText(sdt);
		
		Calendar calobj1 = Calendar.getInstance();
		calobj1.setTime(new Date());
		calobj1.add(Calendar.DATE, 10);
		TextView pdat=(TextView)findViewById(R.id.paydat);
		String spdat=df.format(calobj1.getTime());
		pdat.setText("Pay Before "+spdat);

				/*String s=df.format(calobj.getTime());
				String i=String.valueOf(s)+5;	// try converting string to in and get substring of date
				TextView pdat=(TextView)findViewById(R.id.paydat);
				pdat.setText("Pay Before "+i);*/
		
		billv bv=new billv();
		bv.execute();
		parsingmethod();
		
		

	}
	public void sheduleAlarm(View v)
	{

		Long time=new GregorianCalendar().getTimeInMillis()+24*60*60*1000*8;
		Intent intentAlarm= new Intent(this, AlarmReceiver.class);
		AlarmManager alarmmanager=(AlarmManager)getSystemService(ALARM_SERVICE);
		
		alarmmanager.set(AlarmManager.RTC_WAKEUP,time,PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
		Toast.makeText(this, "Alarm shecduled " , Toast.LENGTH_LONG).show();
		
		
	}
	
	class billv extends AsyncTask<String, String, String>
	{
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try
			{			
    			 urlParameters =  "Consnumber=" + URLEncoder.encode(bvscn, "UTF-8");
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
				sNam=data1.getString("Name");
				sAds=data1.getString("Address");
				sPhn=data1.getString("Phone");
				type=data1.getString("Connectiontype");
				
				//System.out.println(name+email+phone);
				Nam.setText("Name:\t"+sNam);
				Ads.setText("Address:\t"+sAds);
				Phn.setText("Phone:\t"+sPhn);
				
				
				if(type.contains("Normal"))
				{
				amountcalc();
				sFc=fc.toString(); sMr=mr.toString();
				Fc.setText("Fixed Charge: "+sFc+" Rs"); Mr.setText("Meter Rent: "+sMr+" Rs");
				}
				else 
				{
					comm_amountcalc();
					sFc=cfc.toString(); sMr=mr.toString();
					Fc.setText("Fixed Charge: "+sFc+" Rs"); Mr.setText("Meter Rent: "+sMr+" Rs");
				}
		
			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	public void amountcalc()
	{
		SharedPreferences share=this.getSharedPreferences("rd", MODE_WORLD_READABLE);
		spr=share.getString("rd0","");
		
		
		SharedPreferences share1=this.getSharedPreferences("rd1", MODE_WORLD_READABLE);
		scr=share.getString("rd2","");
		
		int pr= Integer.parseInt(spr); int cr= Integer.parseInt(scr);
		
		
		
		int usage= cr-pr;
		double duty= 0.26*usage; //for commercial .648
		double echarg;
		if (usage<200)
		{
			echarg= 2.9*usage;
		}
		else if(usage>300) {
			echarg=7*usage;
		}
		else {
			echarg=3.5*usage;
		}
		
		
		double total= echarg+duty+fc+mr;
		
		
				Ed.setText("Ele. Duty: "+String.valueOf(duty).substring(0, 4)+" Rs"); //String.valueof(variable) used to get value from string
		Usg.setText("Usage: "+String.valueOf(usage)+" unit");
		Ta.setText("Total: "+String.valueOf(Math.round(total))+"/- Rs");	//math function used for rounding doudle value
		Prd.setText("Pre Reading: "+spr);
		Crd.setText("Curr. Reading: "+scr);
	}
	public void comm_amountcalc()
	{
		SharedPreferences share=this.getSharedPreferences("rd", MODE_WORLD_READABLE);
		spr=share.getString("rd0","");
		
		
		SharedPreferences share1=this.getSharedPreferences("rd1", MODE_WORLD_READABLE);
		scr=share.getString("rd2","");
		
		int pr= Integer.parseInt(spr); int cr= Integer.parseInt(scr);
		
		
		
		int usage= cr-pr;
		double duty= 0.648*usage; //for commercial .648
		double echarg;
		/*if (usage<200)
		{
			echarg= 2.9*usage;
		}
		else if(usage>300) {*/
			echarg=7*usage;
		/*}
		else {
			echarg=3.5*usage;
		}*/
		
		
		double total= echarg+duty+cfc+mr;
		
		
				Ed.setText("Ele. Duty: "+String.valueOf(duty).substring(0, 4)+" Rs"); //String.valueof(variable) used to get value from string
		Usg.setText("Usage: "+String.valueOf(usage)+" unit");
		Ta.setText("Total: "+String.valueOf(Math.round(total))+"/- Rs");	//math function used for rounding doudle value
		Prd.setText("Pre Reading: "+spr);
		Crd.setText("Curr. Reading: "+scr);
	}
}
