package com.example.link;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

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

    // set search item -----------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_all_links, menu);
        MenuItem searchViewItem = menu.findItem(R.id.searchAll);

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

        return super.onCreateOptionsMenu(menu);
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
    // set search item -----------------------------------------------------------------------------


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