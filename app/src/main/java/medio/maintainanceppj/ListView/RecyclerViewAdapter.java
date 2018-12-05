package medio.maintainanceppj.ListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import medio.maintainanceppj.Database.FireModel;
import medio.maintainanceppj.R;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<FireModel> list;
    Context context;

    public RecyclerViewAdapter(List<FireModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item,parent,false);
        ViewHolder myHoder = new ViewHolder(view);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FireModel mylist = list.get(position);
        holder.keg.setText(mylist.getKegiatan());
        holder.tgl.setText(mylist.getTanggal());
        holder.jam.setText(mylist.getJam());
        holder.ruang.setText(mylist.getRuangan());
    }

    @Override
    public int getItemCount() {
        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;

            }
            else{

                arr=list.size();
            }



        }catch (Exception e){



        }

        return arr;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView keg,tgl,jam,ruang;


        public ViewHolder(View itemView) {
            super(itemView);
            keg = (TextView) itemView.findViewById(R.id.vKeg);
            tgl= (TextView) itemView.findViewById(R.id.vTgl);
            jam= (TextView) itemView.findViewById(R.id.vJam);
            ruang= (TextView) itemView.findViewById(R.id.vRuangan);

        }
    }
}
