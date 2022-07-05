package com.example.link;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import io.realm.Realm;

public class MainAdapter extends BaseAdapter {

    ArrayList<LinkModel> modelList;
    Context context;
    Realm realm = Realm.getDefaultInstance();


    public MainAdapter(ArrayList<LinkModel> modelList, Context context) {
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

        view= LayoutInflater.from(context).inflate(R.layout.mainlist_view_item,viewGroup,false);

        TextView name=view.findViewById(R.id.itemLinkName);
        TextView topic=view.findViewById(R.id.itemLinkTopic);
        TextView date=view.findViewById(R.id.itemLinkDate);
        LinearLayout layout=view.findViewById(R.id.itemClikibleLayout);
        ImageButton addfavBtn=view.findViewById(R.id.addFavoriteButton);

        name.setText(modelList.get(i).getLinkname());
        topic.setText(modelList.get(i).getLinktopic());
        date.setText(modelList.get(i).getLinkdate());

        final String linkName=modelList.get(i).getLinkname();
        final String linkDate=modelList.get(i).getLinkdate();
        final String linkTopic=modelList.get(i).getLinktopic();
        final String def=modelList.get(i).getLinkdefinition();
        final String link=modelList.get(i).getLink();
        final String status=modelList.get(i).getFavstatus();

        // data transfer to detailpage -------------------------------------------------------------
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DetailPageAct.class);
                intent.putExtra("name",linkName);
                intent.putExtra("date",linkDate);
                intent.putExtra("def",def);
                intent.putExtra("topic",linkTopic);
                intent.putExtra("status",status);
                intent.putExtra("link",link);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        // data transfer to detailpage -------------------------------------------------------------

        // click fav button then insert to favmodel database ---------------------------------------
        addfavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String linkDate=modelList.get(i).getLinkdate();
                final String status=modelList.get(i).getFavstatus();

                if (status.equals("0")){
                    updateLinkStatus(linkDate,"1");
                    addfavBtn.setBackgroundResource(R.drawable.ic_baseline_star);
                    insertToFavDb(linkName,linkTopic,def,link,linkDate,"1");
                }
                else if(status.equals("1")) {
                    updateLinkStatus(linkDate,"0");
                    deleteFavDb(linkDate);
                    addfavBtn.setBackgroundResource(R.drawable.ic_baseline_star_24);
                    Toast.makeText(context.getApplicationContext(),"Link Was deleted From Favorite list",Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;
    }
    // click fav button then insert to favmodel database -------------------------------------------

    public void updateLinkStatus(String date, String newStatus){
        LinkModel modelUp=realm.where(LinkModel.class).equalTo("linkdate",date).findFirst();
        realm.beginTransaction();
        modelUp.setFavstatus(newStatus);
        realm.commitTransaction();
    }

    public void insertToFavDb(final String name,final String topic, final String def, final String link , final String date, final String status){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                FavModel model=realm.createObject(FavModel.class);
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
                Toast.makeText(context.getApplicationContext(),"Link Was Marked As Favorite Successfully",Toast.LENGTH_LONG).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(context.getApplicationContext(), "Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteFavDb(String dates){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //date is primary key

                FavModel model=realm.where(FavModel.class).equalTo("linkdate",dates).findFirst();
                model.deleteFromRealm();
            }
        });
    }

    // search func ---------------------------------------------------------------------------------
    public void filterList(ArrayList<LinkModel> filteredlist) {
        modelList = filteredlist;
        notifyDataSetChanged();
    }
    // search func ---------------------------------------------------------------------------------

}
