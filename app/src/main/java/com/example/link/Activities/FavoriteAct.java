package com.example.link.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.link.Adapters.FavAdapter;
import com.example.link.Models.FavModel;
import com.example.link.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteAct extends AppCompatActivity {

    ListView listView;
    ArrayList<FavModel> listModel=new ArrayList<>();
    Realm realm;
    FavAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorite Links");
        realm=Realm.getDefaultInstance();

        createNav();
        showAllFavDb();
    }

    // set search item -----------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_act_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.searchFav);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adp=new FavAdapter(listModel,getApplicationContext());
                listView.setAdapter((ListAdapter) adp);

                filter(newText.toLowerCase());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {

        ArrayList<FavModel> filteredlist = new ArrayList<>();
        for (FavModel item : listModel) {
            if (item.getLinkname().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "Link Not Found", Toast.LENGTH_SHORT).show();
        } else {
            //fav adapter --> filterList
            adp.filterList(filteredlist);
        }
    }
    // set search item -----------------------------------------------------------------------------

    //set bottom nav -------------------------------------------------------------------------------
    public void createNav(){

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.favoriteNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case (R.id.homeNavigation):
                        Intent inten=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(inten);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.addNewNavigation:
                        Intent intent=new Intent(getApplicationContext(),AddNewAct.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        break;

                }

                return false;
            }
        });
    }
    //set bottom nav -------------------------------------------------------------------------------

    // show FavModel database ----------------------------------------------------------------------
    public void showAllFavDb(){

        listView=findViewById(R.id.listViewFav);
        RealmResults<FavModel> results=realm.where(FavModel.class).findAll();

        for(FavModel m:results){
            listModel.add(m);
        }

        if(results.size()>0){
            FavAdapter adapter=new FavAdapter(listModel,getApplicationContext());
            listView.setAdapter(adapter);
        }
    }
    // show FavModel database ----------------------------------------------------------------------

}