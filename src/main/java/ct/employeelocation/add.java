package ct.employeelocation;

import java.util.ArrayList;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add extends Activity{
	
	 EditText en1,em1;
	  
	    //String[] vendor_names2={"vendor1","vendor2","vendor3","vendor4","vendor5","vendor6","vendor7","vendor8"};
	    ArrayList<String> vendor_names=new ArrayList<String>();
	   RecipentDataBase db=new RecipentDataBase(this);
	   
	    Cursor chek_vendor;
	    public static int ref_id;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add);
	 en1=(EditText)findViewById(R.id.nfield);
	  em1=(EditText)findViewById(R.id.mfield);
	Button b1=(Button)findViewById(R.id.submit);
	Button b2=(Button)findViewById(R.id.Backa);
	b1.setOnClickListener(new OnClickListener() 
	{
		
		@Override
		public void onClick(View v) {
		
			// TODO Auto-generated method stub
          db.open(); 
          //getting vendor details to check previously there or not in database
          chek_vendor=db.getVendorDetails();
          chek_vendor.moveToFirst();
      	if(em1.getText().toString().length()<10){

	        // out of range
	        Toast.makeText(add.this, "Please Enter Correct Phone Number", Toast.LENGTH_LONG).show();
      	}
      	else
      	{
      		
      	
      	
           if(chek_vendor.getCount()==0)
          {
        	  ref_id=01;
        	  
          }
          else
          {
        	  chek_vendor.moveToPosition(chek_vendor.getCount());
        	  ref_id=chek_vendor.getCount()+1;
          }
            
			
          	// New VENDOR DATA inserting into database.......
            long inserted=db.insertVendorDetails(en1.getText().toString(),em1.getText().toString());
          
            Toast.makeText(getApplicationContext(),"Successfully Inserted  "+inserted,Toast.LENGTH_SHORT).show();
            
            en1.setText("");
            em1.setText("");
            db.close();
      	}
           }
	});
	
	
	b2.setOnClickListener(new OnClickListener() 
	{
	    @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		Intent i=new Intent(getApplicationContext(),MainActivity.class);
		startActivity(i);
	
	    }
	});
	
	
	}

}
