package com.example.link;
import static io.realm.Realm.getApplicationContext;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import io.realm.Realm;

public class BinAdp extends BaseAdapter {

    ArrayList<BinModel> modelList;
    Context context;
    Realm realm = Realm.getDefaultInstance();

    public BinAdp(ArrayList<BinModel> modelList, Context context) {
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

        view= LayoutInflater.from(context).inflate(R.layout.bin_list_item,viewGroup,false);
        TextView name=view.findViewById(R.id.itemLinkNameBin);
        TextView link=view.findViewById(R.id.itemLinkLinkBin);
        ImageButton delete=view.findViewById(R.id.deleteForeverBin);
        ImageButton undo=view.findViewById(R.id.undoBin);

        name.setText(modelList.get(i).getLinkname());
        link.setText(modelList.get(i).getLink());

        final String linkdate=modelList.get(i).getLinkdate();
        final String linkname=modelList.get(i).getLinkname();
        final String linktopic=modelList.get(i).getLinktopic();
        final String linkdef=modelList.get(i).getLinkdefinition();
        final String status=modelList.get(i).getFavstatus();
        final String linkx=modelList.get(i).getLink();

        //delete from binmodel database ------------------------------------------------------------
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLinkFunc(linkdate);
                Intent intent=new Intent(getApplicationContext(),BinAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        //delete from binmodel database ------------------------------------------------------------

        //inset to linkmodel database then  delete from binmodel database---------------------------
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(linkname,linktopic,linkx,linkdate,linkdef,status);
                deleteLinkFunc(linkdate);

                Intent intent=new Intent(getApplicationContext(),BinAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                Toast.makeText(getApplicationContext(),"Link Was Undone Successfully",Toast.LENGTH_SHORT).show();
            }
        });
        //inset to linkmodel database then  delete from binmodel database---------------------------

        return view;
    }

    // delete from binmodel func--------------------------------------------------------------------
    public void deleteLinkFunc(final String datePrimary){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //date is primary key
                //user cannot add note more than one at same time

                BinModel model=realm.where(BinModel.class).equalTo("linkdate",datePrimary).findFirst();
                model.deleteFromRealm();
            }
        });
    }
    // delete from binmodel func--------------------------------------------------------------------

    //insert to linkmodel database -----------------------------------------------------------------
    public void insert(final String name, final String topic ,final String link , final String date, final String def, final String status){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                LinkModel model=realm.createObject(LinkModel.class);
                model.setFavstatus(status);
                model.setLinkdefinition(def);
                model.setLinktopic(topic);
                model.setLinkname(name);
                model.setLinkdate(date);
                model.setLink(link);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"Link Was Moved To Link List Successfully",Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //insert to linkmodel database -----------------------------------------------------------------
}
