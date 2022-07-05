package com.example.link;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    ArrayList<LinkModel> array=new ArrayList<>();
    ListView listView;
    ArrayList<LinkModel> listModel=new ArrayList<>();
    Realm realm;
    MainAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm=Realm.getDefaultInstance();
        getSupportActionBar().setTitle("All Links");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showAllLink();
        createNav();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_act_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.searchAll);
        MenuItem backupItem=menu.findItem(R.id.backupMenu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adp=new MainAdapter(listModel,getApplicationContext());
                listView.setAdapter((ListAdapter) adp);

                filter(newText.toLowerCase());
                return false;
            }
        });

        backupItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},100);
                }
                else {
                    RealmResults<LinkModel> models = realm.where(LinkModel.class).findAll();
                    String fileName = "LinkApp.txt";
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);

                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        for (LinkModel m : models) {
                            String convert = "Linkname: " + m.getLinkname() + "\n"
                                    + "Linktopic: " + m.getLinktopic() + "\n"
                                    + "Link: " + m.getLink() + "\n"
                                    + "Linkdate: " + m.getLinkdate() + "\n"
                                    + "Definition: " + m.getLinkdefinition() + "\n"
                                    + "***************************************\n";
                            fileOutputStream.write(convert.getBytes());
                        }
                        fileOutputStream.close();
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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


    private void filter(String text) {

        ArrayList<LinkModel> filteredlist = new ArrayList<>();
        for (LinkModel item : listModel) {
            if (item.getLinkname().toLowerCase().contains(text.toLowerCase()) || item.getLinktopic().toLowerCase().contains(text.toLowerCase()) ) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "Link Not Found", Toast.LENGTH_SHORT).show();
        } else {
            // mainadapter search func
            adp.filterList(filteredlist);
        }
    }


    //set bottom nav -------------------------------------------------------------------------------
    public void createNav(){

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.homeNavigation);

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
                        return true;

                    case R.id.addNewNavigation:
                        Intent inten=new Intent(getApplicationContext(),AddNewAct.class);
                        startActivity(inten);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    //set bottom nav -------------------------------------------------------------------------------

    // show linkmodel database ---------------------------------------------------------------------
    public void showAllLink(){

        listView=findViewById(R.id.listView);
        RealmResults<LinkModel> results=realm.where(LinkModel.class).findAll();

        for(LinkModel m:results){
            listModel.add(m);
        }

        if(results.size()>0){
            MainAdapter adapter=new MainAdapter(listModel,getApplicationContext());
            listView.setAdapter(adapter);
        }
    }
    // show linkmodel database ---------------------------------------------------------------------

}