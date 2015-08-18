package clio.project.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import clio.project.matters.Matters;

/**
 * Created by Daniel Marantz on 17/08/15.
 *
 * ListAdapter is a custom ArrayAdapter that handles Matters Objects
 * for the listview.
 */
public class ListAdapter extends ArrayAdapter<Matters> {

    private List<Matters> itemList;
    private Context context;
    private LayoutInflater  inflater;

    /**
     * Constructor to initialize the listview data.
     *
     * @param itemList List of all Matters.
     * @param ctx      Context of the Activity.
     */
    public ListAdapter(List<Matters> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = ctx;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Getter of the Matters list total.
     *
     * @return The total of the list.
     */
    public int getCount() {
        if (itemList != null) {
            return itemList.size();
        }
        return 0;
    }

    /**
     * Getter of a Matter given its position.
     *
     * @param position Location of the list item.
     * @return         Matters object.
     */
    public Matters getItem(int position) {
        if (itemList != null) {
            return itemList.get(position);
        }
        return null;
    }

    /**
     * Getter of a list item id.
     *
     * @param position Location of the list item.
     * @return         Id of a list item.
     */
    public long getItemId(int position) {
        if (itemList != null) {
            return itemList.get(position).hashCode();
        }
        return 0;
    }

    /**
     * Getter of the view to update list data.
     *
     * @param position    Location of the list item.
     * @param convertView The view of the list.
     * @param parent      The viewgroup of the list.
     * @return            The updated view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        //inflate row layout
        if (row == null) {
            row = inflater.inflate(R.layout.list_item, null);
            holder =new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }
        // Sets the the given Matter to the listview tab
        Matters m = itemList.get(position);
        //show data
        holder.displayName.setText(m.getDisplayName());
        holder.clientName.setText(m.getClientName());

        return row;
    }

    /**
     * Getter of the list of Matters.
     *
     * @return List of Matters.
     */
    public List<Matters> getItemList() {
        return itemList;
    }

    /**
     * Setter of the list of Matters.
     *
     * @param itemList List of Matters.
     */
    public void setItemList(List<Matters> itemList) {
        this.itemList = itemList;
    }

    /**
     * ViewHolder is an inner anonymous class for listview optimization
     */
    class ViewHolder {

        TextView displayName;
        TextView clientName ;

        public ViewHolder(View v){
            displayName =(TextView) v.findViewById(R.id.displayName);
            clientName =(TextView) v.findViewById(R.id.clientName);
        }
    }
}