package ct.employeelocation;

import java.util.ArrayList;

import ct.employeelocation.R;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class view extends Activity{
	
	Intent in;
    ListView vendor_list;
    EditText vendor_search;
    //String[] vendor_names2={"vendor1","vendor2","vendor3","vendor4","vendor5","vendor6","vendor7","vendor8"};
   ArrayList<String> vendor_names=new ArrayList<String>();
   RecipentDataBase db=new RecipentDataBase(this);
   Cursor c;
@Override
protected void onCreate(Bundle savedInstanceState) 
{
	// TODO Auto-generated method stub
 
super.onCreate(savedInstanceState);
setContentView(R.layout.view);
Button b1=(Button)findViewById(R.id.backv);
b1.setOnClickListener(new OnClickListener() 
{
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	Intent i=new Intent(getApplicationContext(),MainActivity.class);
	startActivity(i);
	}
});


db.open();
c=db.getVendorDetails();
for(int i=0;i<c.getCount();i++)
{
c.moveToPosition(i);
vendor_names.add(c.getString(0));
}
//Toast.makeText(getApplicationContext(),"vendor from database"+vendor_names2.get(0),100).show();
//db.close();

final ArrayAdapter<String> vendor_adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1,
        vendor_names);
vendor_list=(ListView)findViewById(R.id.vendorlistView);
vendor_list.setAdapter(vendor_adapter);



vendor_list.setOnItemClickListener(new OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
    {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(),(CharSequence) vendor_list.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
       // in=new Intent(getApplicationContext(),maploc.class);
        //in.putExtra("selected vendor",(CharSequence) vendor_list.getItemAtPosition(position));
    //    startActivity(in);
        String number = null;
   
        //db.open();
        //c=db.getVendorDetails();
        c.moveToFirst();
        for(int i=0;i<vendor_names.size();i++)
        {
        	
        	if(vendor_names.get(i).equals(vendor_list.getItemAtPosition(position)))
        	{
        		number=c.getString(1);
        		Toast.makeText(getApplicationContext(),"number:"+number, Toast.LENGTH_SHORT).show();
        		
        	}
        	c.moveToNext();
        }
        
        c.moveToPrevious();
        
       // Toast.makeText(getApplicationContext(),"number"+c.getString(0), 100).show();
        SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage(number, null, "VR SIDDHARTHA ENGG COLLEGE,KANURU VJA.", null, null);
     
    
    }
});



}

}
