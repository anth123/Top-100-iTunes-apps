package anth123.top100itunesapps;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppEntryListAdapter extends BaseAdapter {
	private Activity context;
	private List<AppEntry> list;

	public AppEntryListAdapter(Activity context, List<AppEntry> entryList) {
		this.context = context;
		this.list = entryList;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderItem viewHolder;
		
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_row_appentry, null);
			
			viewHolder = new ViewHolderItem();
			viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.ivLogo);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            viewHolder.tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderItem) convertView.getTag();
		}
		
		AppEntry entry = list.get(position);
		viewHolder.tvName.setText(entry.getName());
		viewHolder.tvPrice.setText(entry.getPrice());
		viewHolder.ivLogo.setImageBitmap(entry.getImage());
        viewHolder.tvPosition.setText(position + 1 + "");

		return convertView;
	}

    static class ViewHolderItem {
		ImageView ivLogo;
		TextView tvName;
		TextView tvPrice;
        TextView tvPosition;
	}
}
