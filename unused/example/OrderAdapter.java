package example;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.james.skiller.R;

public class OrderAdapter extends ArrayAdapter<Order> {

	private List<Order> items;
	private Context context;

	public OrderAdapter(Context context, int textViewResourceId, List<Order> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row, null);
		}
		Order o = items.get(position);
		if (o != null) {
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			TextView bt = (TextView) v.findViewById(R.id.bottomtext);
			if (tt != null) {
				tt.setText("Name: " + o.getOrderName());
			}
			if (bt != null) {
				bt.setText("Status: " + o.getOrderStatus());
			}
		}
		return v;
	}
}