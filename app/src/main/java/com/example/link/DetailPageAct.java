package com.example.link;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmResults;

public class DetailPageAct extends AppCompatActivity {

    EditText nameE,topicE,linkE,defE, telNumberE;
    FloatingActionButton goLinkBtn, deleteBtn,updateBtn;
    WebView wb;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        realm = Realm.getDefaultInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSetData();
        updateLink();
        deleteLink();
    }

    // set options menu items ----------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.detail_menu_items,menu);
        MenuItem help=menu.findItem(R.id.detailHelp);
        MenuItem share=menu.findItem(R.id.detailShare);

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Toast.makeText(getApplicationContext(), "Please Describe Your Issue", Toast.LENGTH_LONG).show();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"yilmzsrkn@gmail.com" ));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Please describe your issue here");
                startActivity(Intent.createChooser(emailIntent, "Chooser Title"));

                return false;
            }
        });

        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent i=getIntent();
                String link=i.getStringExtra("link");
                String combine="you can use this link "+ link;

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,combine);
                startActivity(Intent.createChooser(intent,"Share Now"));

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    // set options menu items ----------------------------------------------------------------------

    // fill the detail edittext fields from linkmodel database -------------------------------------
    public void getSetData(){

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String def=intent.getStringExtra("def");
        String topic= intent.getStringExtra("topic");
        String link= intent.getStringExtra("link");

        getSupportActionBar().setTitle(name);

        nameE=findViewById(R.id.linkNameDetail);
        topicE=findViewById(R.id.linkTopicDetail);
        defE=findViewById(R.id.linkDefDetail);
        linkE=findViewById(R.id.linkDetail);
        goLinkBtn =findViewById(R.id.goLinkButton);

        nameE.setText(name);
        topicE.setText(topic);
        defE.setText(def);
        linkE.setText(link);

        wb=findViewById(R.id.webView);
        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl(link);

        goLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWebPage(link);
            }
        });
    }
    // fill the detail edittext fields from linkmodel database -------------------------------------

    public void goWebPage(String url){
        Uri uri=Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    // update link from linkmodel database ---------------------------------------------------------
    public void updateLink(){

        updateBtn=findViewById(R.id.updateFloating);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLinkFunc();
                goBackMainAct();
            }
        });
    }

    public void updateLinkFunc(){

        Intent intent=getIntent();
        String primaryDate=intent.getStringExtra("date");

        String newLink=linkE.getText().toString();
        String newName=nameE.getText().toString();
        String newDef=defE.getText().toString();
        String newTopic=topicE.getText().toString();

        LinkModel model=realm.where(LinkModel.class).equalTo("linkdate",primaryDate).findFirst();

        realm.beginTransaction();
        model.setLink(newLink);
        model.setLinkdefinition(newDef);
        model.setLinktopic(newTopic);
        model.setLinkname(newName);
        realm.commitTransaction();

        Toast.makeText(getApplicationContext(), "Link Was Updated", Toast.LENGTH_SHORT).show();
    }
    // update link from linkmodel database ---------------------------------------------------------

    // delete link from linkmodel database ---------------------------------------------------------
    public void deleteLink(){
        deleteBtn=findViewById(R.id.deleteFloating);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLinkFunc();
                goBackMainAct();
            }
        });
    }

    public void deleteLinkFunc(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //date is primary key
                //user cannot add note more than one at same time

                Intent intent=getIntent();
                final String def=intent.getStringExtra("def");
                final String topic= intent.getStringExtra("topic");
                final String name=intent.getStringExtra("name");
                final String link= intent.getStringExtra("link");
                final String datePrimary=intent.getStringExtra("date");
                final String status=intent.getStringExtra("status");

                LinkModel model=realm.where(LinkModel.class).equalTo("linkdate",datePrimary).findFirst();
                model.deleteFromRealm();

                insert(name,topic,link,datePrimary,def,status);
            }
        });
    }
    // delete link from linkmodel database ---------------------------------------------------------

    //insert to linkmodel database -----------------------------------------------------------------
    public void insert(final String name, final String topic ,final String link , final String date, final String def, final String status){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                BinModel model=realm.createObject(BinModel.class);
                model.setFavstatus(status);
                model.setLinkdefinition(def);
                model.setLinktopic(topic);
                model.setLinkname(name);
                model.setLinkdate(date);
                model.setLink(link);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"Link Was Moved To Bin Successfully",Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }
    //insert to linkmodel database -----------------------------------------------------------------

    public void goBackMainAct(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

}