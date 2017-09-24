package josh.logme;

/**
 * Created by Josh PC on 8/31/2017.
 */


import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public static final String FIRST_COLUMN="Time";
    public static final String SECOND_COLUMN="B.G.";
    public static final String THIRD_COLUMN="Dose";
    public static final String FOURTH_COLUMN="Carbs";
    public static final String FIFTH_COLUMN="Notes";

    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
        TextView txtFifth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.log_list, null);
            holder=new ViewHolder();

            holder.txtFirst = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.txtGlucose);
            holder.txtThird = (TextView) convertView.findViewById(R.id.txtDose);
            holder.txtFourth = (TextView) convertView.findViewById(R.id.txtCarbs);
            holder.txtFifth = (TextView) convertView.findViewById(R.id.txtNotes);

            convertView.setTag(holder);
        }else{

            holder=(ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map=list.get(list.size() - position - 1);
        holder.txtFirst.setText(map.get(FIRST_COLUMN));
        holder.txtSecond.setText(map.get(SECOND_COLUMN));
        holder.txtThird.setText(map.get(THIRD_COLUMN));
        holder.txtFourth.setText(map.get(FOURTH_COLUMN));
        holder.txtFifth.setText(map.get(FIFTH_COLUMN));

        return convertView;
    }

}