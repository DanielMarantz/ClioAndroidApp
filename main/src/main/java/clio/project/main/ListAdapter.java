package clio.project.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import clio.project.matters.Matters;

public class ListAdapter extends ArrayAdapter<Matters> {

    private List<Matters> itemList;
    private Context context;

    public ListAdapter(List<Matters> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Matters getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Matters m = itemList.get(position);
        TextView mainListText1 = (TextView) v.findViewById(R.id.displayName);
        mainListText1.setText(m.getDisplayName());

        TextView mainListText2 = (TextView) v.findViewById(R.id.clientName);
        mainListText2.setText(m.getClientName());

        return v;
    }

    public List<Matters> getItemList() {
        return itemList;
    }

    public void setItemList(List<Matters> itemList) {
        this.itemList = itemList;
    }

}