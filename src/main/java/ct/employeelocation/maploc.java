package ct.employeelocation;



import java.security.PublicKey;

import ct.employeelocation.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class maploc extends BroadcastReceiver {

	String l;
	Context c;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		c=context;
	Bundle bundle = intent.getExtras();
	SmsMessage[] msgs = null;
	String str="";
	
	if (bundle != null)
	{
	//---retrieve the SMS message received---
	Object[] pdus = (Object[]) bundle.get("pdus");
	msgs = new SmsMessage[pdus.length];
	for (int i=0; i<msgs.length; i++){
	msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

	//---get the message body---
	str += msgs[i].getMessageBody().toString();
   
 	
	if(str.startsWith("http://maps.google.com/maps?q="))
   {
	   Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	  
	   Intent smstxt=new Intent(context,smstext.class);
	   smstxt.putExtra("sms", str);
	   smstxt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   context.startActivity(smstxt);
	   
	   
	 //custom.show();
   
   }
	
	} 

	}
	}
	public Dialog onCreateDialog(int value)
	{
		final Dialog custom=new Dialog(c);
		  custom.setContentView(R.layout.loc);
		  TextView text=(TextView)custom.findViewById(R.id.smsloctext);
		  text.setText("dfghjk");
		  Button back2=(Button)custom.findViewById(R.id.back2);
		  back2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//custom.dismiss();
			}
		});
		  
		
		return custom;
	}
 

}

	
	
	
