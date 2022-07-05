package com.example.link.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.link.R;

public class GuideAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quick Start");

        // play video ------------------------------------------------------------------------------
        VideoView view=findViewById(R.id.videoView);
        String path="android.resource://"+getPackageName()+"/"+R.raw.video;
        Uri uri=Uri.parse(path);
        view.setVideoURI(uri);

        MediaController mediaController=new MediaController(this);
        view.setMediaController(mediaController);
        mediaController.setAnchorView(view);
    }
    // play video ------------------------------------------------------------------------------

}