package com.example.link;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import io.realm.Realm;

public class FavAdapter extends BaseAdapter {

    ArrayList<FavModel> modelList;
    Context context;
    Realm realm = Realm.getDefaultInstance();

    public FavAdapter(ArrayList<FavModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).inflate(R.layout.favorite_view,viewGroup,false);

        TextView nameT=view.findViewById(R.id.favoriteLinkName);
        TextView topicT=view.findViewById(R.id.favoriteLinkTopic);
        ImageButton removeFavBtn=view.findViewById(R.id.favoriteButton);
        Button golink=view.findViewById(R.id.goLinkFavListBtn);

        nameT.setText(modelList.get(i).getLinkname());
        topicT.setText(modelList.get(i).getLinktopic());

        String date=modelList.get(i).getLinkdate();
        String link=modelList.get(i).getLink();

        // delete from favmodel database -----------------------------------------------------------
        removeFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //date is primary key

                        FavModel model=realm.where(FavModel.class).equalTo("linkdate",date).findFirst();
                        model.deleteFromRealm();
                        Toast.makeText(context.getApplicationContext(),"Link Was Marked As Unfavorite Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // delete from favmodel database -----------------------------------------------------------

        golink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWebPage(link);
            }
        });
        return view;
    }

    public void goWebPage(String url){
        Uri uri=Uri.parse(url);
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //search item func -----------------------------------------------------------------------------
    public void filterList(ArrayList<FavModel> filteredlist) {
        modelList = filteredlist;
        notifyDataSetChanged();
    }
    //search item func -----------------------------------------------------------------------------

}
