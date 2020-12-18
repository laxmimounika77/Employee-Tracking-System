package ct.employeelocation;

import java.io.IOException;

import org.apache.http.HttpResponse;

import ct.employeelocation.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class smstext extends Activity
{
@Override
protected void onCreate(Bundle savedInstanceState)
{
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.smstextlink);
	//DefaultHttpClient client=new DefaultHttpClient();
	
	
	
	TextView text=(TextView)findViewById(R.id.smst);
	Intent in=getIntent();
	final String smstt=in.getStringExtra("sms");
	
	text.setText(smstt);
	

	
}
}
