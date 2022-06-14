package com.example.link;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

public class BinAct extends AppCompatActivity {

    ListView listView;
    ArrayList<BinModel> listModel=new ArrayList<>();
    Realm realm;
    BinAdp adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin);

        realm=Realm.getDefaultInstance();
        getSupportActionBar().setTitle("Bin");
        showAllDeletedLink();

        FloatingActionButton btn=findViewById(R.id.goEntranceButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),EntranceAct.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //show BinModel database start------------------------------------------------------------------

    public void showAllDeletedLink(){

        listView=findViewById(R.id.listViewBin);
        RealmResults<BinModel> results=realm.where(BinModel.class).findAll();

        for(BinModel m:results){
            listModel.add(m);
        }

        if(results.size()>0){
            adapter=new BinAdp(listModel,getApplicationContext());
            listView.setAdapter(adapter);
        }
    }
    //show BinModel database start------------------------------------------------------------------
}