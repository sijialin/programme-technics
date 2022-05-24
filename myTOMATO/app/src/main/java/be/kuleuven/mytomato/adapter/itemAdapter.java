package be.kuleuven.mytomato.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.kuleuven.mytomato.R;
import be.kuleuven.mytomato.database.ToDo;

public class itemAdapter extends BaseAdapter{
    Context context;
    List<ToDo> mItems;
    LayoutInflater inflater;
    Resources r;

    int[] colors = {0xffF8F2FD,0xffd8c3e1,0xff6f5f90,0xff511f66,0xff270849,0xff210c2b,0xff000000};
    public itemAdapter(Context context, List<ToDo> mItems, Resources r) {
        this.context = context;
        this.mItems = mItems;
        this.r = r;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       viewHolder holder = null;
        if(view==null){
            view = inflater.inflate(R.layout.main_item,viewGroup,false);

            holder=new viewHolder(view);
            view.setTag(holder);
        }
        else{
            holder = (viewHolder)view.getTag();
        }
        ToDo item = mItems.get(i);
        holder.image.setImageResource(item.getImageID());
        holder.name.setText(item.getName());
        holder.note.setText(item.getNote());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.name.setTextColor(colors[item.getColor()-1]);//colors[item.getColor()]);
        //改颜色
        return view;
    }

    class viewHolder{
        ImageView image;
        TextView name,note,time,date;
        public viewHolder(View view){
            image = view.findViewById(R.id.item_image);
            name= view.findViewById(R.id.item_name);
            note = view.findViewById(R.id.item_note);
            date = view.findViewById(R.id.item_ddl);
            time = view.findViewById(R.id.item_ddl_time);
        }
    }
}
