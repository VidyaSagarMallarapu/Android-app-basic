package com.example.vidyasagarnaidum.otpsend;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

class Android_Contact {
    //----------------< fritzbox_Contacts() >----------------
    public String android_contact_Name = "";
    public String android_contact_TelefonNr = "";
    public int android_contact_ID = 0;

    public String getName() {
        return android_contact_Name;
    }

    public String getNumber() {
        return android_contact_TelefonNr;
    }
//----------------</ fritzbox_Contacts() >----------------
}

public class Verified extends AppCompatActivity {


    public String android_contact_Name = "";
    public String android_contact_TelefonNr = "";
    public int android_contact_ID = 0;
    ListView listView;

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    EditText e1;
    ImageButton b2;
    ArrayList<Android_Contact> arrayList_Android_Contacts;
    Cursor cursor_Android_Contacts;
    ClipboardManager clipboardManager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);
        arrayList_Android_Contacts = new ArrayList<>();
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        e1 = (EditText) findViewById(R.id.editText);
        b2 = (ImageButton) findViewById(R.id.search);

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                DataSetting(e1.getText().toString());
            }
        });

        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {


            }
            ContentResolver contentResolver = getContentResolver();

            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

//--</ get all Contacts >--

//----< Check: hasContacts >----
            if (cursor_Android_Contacts.getCount() > 0) {
//----< has Contacts >----
//----< @Loop: all Contacts >----
                while (cursor_Android_Contacts.moveToNext()) {
//< init >
                    Android_Contact android_contact = new Android_Contact();
                    String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                    String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//</ init >

//----< set >----
                    if (contact_display_name.equals("")) continue;
                    android_contact.android_contact_Name = contact_display_name;


//----< get phone number >----
                    int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {

                        Cursor phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                , null
                                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                                , new String[]{contact_id}
                                , null);

                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//< set >
                            android_contact.android_contact_TelefonNr = phoneNumber;
//</ set >
                        }
                        if (android_contact.android_contact_TelefonNr.equals("") || android_contact.android_contact_TelefonNr == NULL) {
                            Toast.makeText(this, "phone has null number", Toast.LENGTH_LONG).show();
                            continue;
                        }
                        phoneCursor.close();
                    }

// Add the contact to the ArrayList
                    arrayList_Android_Contacts.add(android_contact);
                }


            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "" + ex, Toast.LENGTH_LONG).show();
        }
        DataSetting("");


    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        menu.setHeaderTitle("Select The Action");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        // TextView t1=(TextView)findViewById(R.id.listview_Android_Contacts.)
        Android_Contact ob = (Android_Contact) listView.getAdapter().getItem(index);
        String name = ob.getName();
        String number = ob.getNumber();
        if (item.getItemId() == R.id.call) {
            Toast.makeText(getApplicationContext(), "calling contact ", Toast.LENGTH_LONG).show();

            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel: "+number));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(this, "No permission to call!", Toast.LENGTH_SHORT).show();
            }
            startActivity(phoneIntent);
        }
        else if(item.getItemId()==R.id.sms){
            //Toast.makeText(getApplicationContext(),"sending sms code",Toast.LENGTH_LONG).show();
            Intent i=new Intent(getApplicationContext(),SmsActivity.class);
            i.putExtra("phone",number);
            startActivity(i);

        }
        else if(item.getItemId()==R.id.phoneCopy){


            //copying text by clicking it
            if(number.indexOf("+91")!=-1)
            {
                number=number.substring(3);
            }
            clipboardManager.setText(number);
            Toast.makeText(getApplicationContext(),number +" Copied",Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId()==R.id.NameCopy){
            Toast.makeText(getApplicationContext(),name+ " Copied",Toast.LENGTH_LONG).show();
            //copying text by clicking it
            clipboardManager.setText(name);
        }


        else{
            return false;
        }
        return true;
    }

    public void DataSetting(final String temp) {
        try {


        final ArrayList<Android_Contact> arrayList_Android_Contacts2 = new ArrayList<Android_Contact>();
        Adapter_for_Android_Contacts adapter;

        if (temp.equals("")) {
            adapter = new Adapter_for_Android_Contacts(this, arrayList_Android_Contacts);
            Toast.makeText(getApplicationContext(),  arrayList_Android_Contacts.size()+" results found", Toast.LENGTH_LONG).show();
        } else {
            Android_Contact ac2 ;
            int i = 0;



            while (i < arrayList_Android_Contacts.size()) {
                ac2=arrayList_Android_Contacts.get(i);
                String te=(ac2.android_contact_Name).toLowerCase();
                if (((ac2).android_contact_TelefonNr).indexOf(temp)!=-1||((te).indexOf(temp.toLowerCase())!=-1)) {
                    arrayList_Android_Contacts2.add(arrayList_Android_Contacts.get(i));

                }

                ++i;
            }
            Toast.makeText(getApplicationContext(),  arrayList_Android_Contacts2.size()+" results found", Toast.LENGTH_LONG).show();


            adapter = new Adapter_for_Android_Contacts(this, arrayList_Android_Contacts2);
        }
         listView = (ListView) findViewById(R.id.listview_Android_Contacts);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    Android_Contact ac;
                    if(temp.equals("")) {
                         ac = arrayList_Android_Contacts.get(i);
                    }
                    else
                    {
                        ac = arrayList_Android_Contacts2.get(i);
                    }



                }}
        );
        } catch (Exception e) {
                    e.printStackTrace();
                }



    }




    public void Llaunc(View view) {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        Toast.makeText(getApplicationContext(),"Logout completed",Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
class Adapter_for_Android_Contacts extends BaseAdapter {
    //----------------< Adapter_for_Android_Contacts() >----------------
//< Variables >
    Context mContext;
    List<Android_Contact> mList_Android_Contacts;
//</ Variables >

    //< constructor with ListArray >
    public Adapter_for_Android_Contacts(Context mContext, List<Android_Contact> mContact) {
        this.mContext = mContext;
        this.mList_Android_Contacts = mContact;
    }
//</ constructor with ListArray >

    @Override
    public int getCount() {
        return mList_Android_Contacts.size();
    }


    public Object getItem(int position) {
        return mList_Android_Contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //----< show items >----
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(mContext,R.layout.contactlist_android_items,null);
//< get controls >
        TextView textview_contact_Name= (TextView) view.findViewById(R.id.textview_android_contact_name);
        TextView textview_contact_TelefonNr= (TextView) view.findViewById(R.id.textview_android_contact_phoneNr);
//</ get controls >

//< show values >
        textview_contact_Name.setText(mList_Android_Contacts.get(position).android_contact_Name);
        textview_contact_TelefonNr.setText(mList_Android_Contacts.get(position).android_contact_TelefonNr);
//</ show values >


        view.setTag(mList_Android_Contacts.get(position).android_contact_Name);
        return view;
    }
//----</ show items >----
//----------------</ Adapter_for_Android_Contacts() >----------------
}
