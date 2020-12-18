package ct.employeelocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipentDataBase {
	
	 private static final String RP_DATABASE="Recipent_DB";
	    private static final int RP_DB_VERSION=1;
	    
	    private static final String RP_DB_VENDOR_TABLE="vendor_table";
	    
	    private static final String RP_DB_VENDOR_NAME="vendor_name";
	    private static final String RP_DB_VENDOR_NUMBER="vendor_num";
	    
	    private static final String RP_DB_VENDOR_TABLE_CREATE="create table vendor_table(vendor_name text not null, vendor_num text not null)";
	    
	    private SQLiteDatabase db;
	    private RpDBhelper helper;
	    public RecipentDataBase(Context context)
	    {
	        helper=new RpDBhelper(context);
	        // TODO Auto-generated constructor stub
	    }
	    
	    class RpDBhelper extends SQLiteOpenHelper
	    {

	        public RpDBhelper(Context context)
	        {
	            super(context, RP_DATABASE, null, RP_DB_VERSION);
	            // TODO Auto-generated constructor stub
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db)
	        {
	            // TODO Auto-generated method stub
	            db.execSQL(RP_DB_VENDOR_TABLE_CREATE);
	           
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	        {
	            // TODO Auto-generated method stub
	            
	        }
	        
	    }
	    public  RecipentDataBase open() throws SQLException
	    {
	        db=helper.getWritableDatabase();
	        return this;
	    }
	    public void close()
	    {
	        helper.close();
	    }
	    public long insertVendorDetails(String vendor_namee, String string)
	    {
	        ContentValues cv=new ContentValues();
	        cv.put( RP_DB_VENDOR_NAME, vendor_namee);
	        cv.put(RP_DB_VENDOR_NUMBER, string);
	        
	        return db.insert(RP_DB_VENDOR_TABLE, null,cv);
	    }
	   
	    public Cursor getVendorDetails()
	    {
	        return db.query(RP_DB_VENDOR_TABLE, 
	                new String[]{RP_DB_VENDOR_NAME,RP_DB_VENDOR_NUMBER},
	                null, null, null, null, null);
	    }
	    
	    public Cursor getVendorRefId(String vendor_name)
	    {
	        
	    	String queryforRefId="select vendor_ref_id from vendor_table where vendor_name='"+vendor_name+"'";
	    	
	    	
	    	return db.rawQuery(queryforRefId, null);
	    	/*return db.query(BM_DB_VENDOR_TABLE, 
	                new String[]{BM_DB_VENDOR_NUMBER,BM_DB_VENDOR_NAME,BM_DB_VENDOR_ADDRESS,BM_DB_VENDOR_REF_ID},
	                null, null, null, null, null);*/
	    }
	    
	 
	    
}
