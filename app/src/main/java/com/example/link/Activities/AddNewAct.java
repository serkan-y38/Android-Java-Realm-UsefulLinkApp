package com.example.link;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddNewAct extends AppCompatActivity {

    Realm realm;
    EditText name,topic,link,def;
    FloatingActionButton btn;
    ArrayList<LinkModel> arrayList=new ArrayList<>();

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

                checkPermission();
                RealmResults<LinkModel> models=realm.where(LinkModel.class).findAll();

                final String defaultsatus="0";
                final String definition=def.getText().toString();
                final String nameLink=name.getText().toString();
                final String topicLink=topic.getText().toString();
                final String Link=link.getText().toString();
                final String timeStamp = new SimpleDateFormat("ss.mm.HH.dd.MM.yyyy").format(new java.util.Date());

                if(nameLink.isEmpty() || topicLink.isEmpty() || Link.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Please Fill The Required Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    insert(nameLink,topicLink,definition,Link,timeStamp,defaultsatus);
                    backUp(models);

                    def.setText("");
                    name.setText("");
                    topic.setText("");
                    link.setText("");
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
            arrayList.add(m);
        }
    }

    public void backUp(RealmResults<LinkModel> models){

        String fileName= "LinkApp.txt";
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileName);

        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(file);
            for(LinkModel m:models){
                String convert="Linkname: "+m.getLinkname()+"\n"
                        +"Linktopic: "+m.getLinktopic()+"\n"
                        +"Link: "+m.getLink()+"\n"
                        +"Linkdate: "+m.getLinkdate()+"\n"
                        +"Definition: "+m.getLinkdefinition()+"\n"
                        +"***************************************\n";
                fileOutputStream.write(convert.getBytes());
            }
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(AddNewAct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(AddNewAct.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 100:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Permission Allowed, data will back up your device",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Permission Denied, Permission is necessary to back up data, please allow the permission",Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //insert linkmodel database   ------------------------------------------------------------------

}