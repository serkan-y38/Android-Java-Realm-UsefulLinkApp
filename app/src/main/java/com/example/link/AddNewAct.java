package com.example.link;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddNewAct extends AppCompatActivity {

    Realm realm;
    EditText name,topic,link,def;
    FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        realm= Realm.getDefaultInstance();

        getSupportActionBar().setTitle("Add New Link");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addToLinkDb();
        createNav();
    }

    //set bottom nav -------------------------------------------------------------------------------

    public void createNav(){

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.addNewNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favoriteNavigation:
                        Intent intent=new Intent(getApplicationContext(),FavoriteAct.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case (R.id.homeNavigation):
                        Intent inten=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(inten);
                        overridePendingTransition(0,0);

                        case R.id.addNewNavigation:
                        return true;
                }
                return false;
            }
        });
    }
    //set bottom nav -------------------------------------------------------------------------------

    //insert linkmodel database --------------------------------------------------------------------
    public void addToLinkDb(){

        def = findViewById(R.id.linkDefAdd);
        name= findViewById(R.id.linkNameAdd);
        topic= findViewById(R.id.linkTopicAdd);
        link = findViewById(R.id.linkAdd);
        btn = findViewById(R.id.addLinkButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String defaultsatus="0";
                final String definition=def.getText().toString();
                final String nameLink=name.getText().toString();
                final String topicLink=topic.getText().toString();
                final String Link=link.getText().toString();
                final String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

                if(nameLink.isEmpty() || definition.isEmpty() || Link.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Please Fill The Required Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    insert(nameLink,topicLink,definition,Link,timeStamp,defaultsatus);
                }
            }
        });
    }

    public void insert(final String name,final String topic, final String def, final String link , final String date, final String status){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                LinkModel model=realm.createObject(LinkModel.class);
                model.setLinkname(name);
                model.setLinktopic(topic);
                model.setLinkdefinition(def);
                model.setLinkdate(date);
                model.setLink(link);
                model.setFavstatus(status);

                goBackMainAct();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"Link Was Added Successfully",Toast.LENGTH_SHORT).show();
                LogDb();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void LogDb(){
        RealmResults<LinkModel> models=realm.where(LinkModel.class).findAll();
        for(LinkModel m:models){
            Log.i("link models",m.toString());
        }
    }

    //insert linkmodel database   ------------------------------------------------------------------

    public void goBackMainAct(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

}