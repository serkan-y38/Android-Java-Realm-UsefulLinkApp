package com.example.link;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class EntranceAct extends AppCompatActivity {

    NavigationView navigationView;
    TextView addressText;
    ImageButton copyButton;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        if(ContextCompat.checkSelfPermission(EntranceAct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(EntranceAct.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},100);
        }

        startActWithClickableLayout();
        getSupportActionBar().setTitle("LinkApp");
        setDrawerNav();
    }

    //drawer menu start
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // set drawermenu and items --------------------------------------------------------------------
    public void setDrawerNav() {

        navigationView=findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.close_menu);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                // close drawer when item is tapped
                switch (item.getItemId()){
                    case R.id.shareDrawer:{
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT,"Check Out This Useful App 'App Link Here'");
                        startActivity(Intent.createChooser(intent,"Share Now"));
                        break;
                    }
                    case R.id.HelpDrawer:{
                        Toast.makeText(getApplicationContext(), "Please Describe Your Issue", Toast.LENGTH_LONG).show();

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"yilmzsrkn@gmail.com" ));
                        String subject="Please describe your issue here";
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                        break;
                    }
                    case R.id.contactUsDrawer:{
                        Toast.makeText(getApplicationContext(),"Send an email to us",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.binDrawer:{
                        Intent intent=new Intent(getApplicationContext(),BinAct.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case R.id.guideDrawer:{
                        Intent intent=new Intent(getApplicationContext(),GuideAct.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();}
    }
    // set drawermenu and items --------------------------------------------------------------------


     public void startActWithClickableLayout(){

         RelativeLayout golinkList=findViewById(R.id.goAllLinkLayout);
         RelativeLayout gofavList=findViewById(R.id.goFavLinkLayout);
         RelativeLayout buy=findViewById(R.id.buyCoffeeLayout);
         RelativeLayout goadd=findViewById(R.id.goAddLinkLayout);

         buy.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 LayoutInflater inflater=getLayoutInflater();
                 View v=inflater.inflate(R.layout.buy_coffe_alert,null);

                 addressText= v.findViewById(R.id.addressCoffeeEt);
                 copyButton=v.findViewById(R.id.copyCoffeeButton);

                 AlertDialog.Builder alert=new AlertDialog.Builder(EntranceAct.this);
                 alert.setView(v);
                 alert.setCancelable(true);
                 AlertDialog dialog=alert.create();

                 copyButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {

                         ClipboardManager clipboardManager=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                         ClipData clipData=ClipData.newPlainText("Text",addressText.getText().toString());
                         clipboardManager.setPrimaryClip(clipData);

                         Toast.makeText(getApplicationContext(),"Copied",Toast.LENGTH_SHORT).show();

                     }
                 });
                 dialog.show();
             }
         });

         golinkList.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                 startActivity(intent);
             }
         });

         gofavList.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(getApplicationContext(),FavoriteAct.class);
                 startActivity(intent);
             }
         });

         goadd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(getApplicationContext(),AddNewAct.class);
                 startActivity(intent);
             }
         });
     }

}