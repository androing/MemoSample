package com.androing.memo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MemoListAdapter extends ArrayAdapter<MemoItem> {
	private LayoutInflater inflater;
	private int textViewResourceId;
	private List<MemoItem> items;

	static class ViewStore {
		ImageView imageView;
		TextView textView;
	}
	
	public MemoListAdapter(Context context, int textViewResourceId, List<MemoItem> items) {
		super(context, textViewResourceId, items);
		
		this.textViewResourceId = textViewResourceId;
		this.items = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewStore viewStore;
		
		if(convertView != null) {
			view = convertView;
			//	2回目以降はTAGからview情報取得
			viewStore = (ViewStore)view.getTag();
		} else {
			view = inflater.inflate(textViewResourceId, null);

			//	リスト表示高速化の為、TAGに保持
			ImageView imageView = (ImageView)view.findViewWithTag("icon");
			TextView textView = (TextView)view.findViewWithTag("text");

			viewStore = new ViewStore();
			viewStore.imageView = imageView;
			viewStore.textView = textView;
			view.setTag(viewStore);
		}

		MemoItem item = items.get(position);
		
		if(item.getType() == 0) {
			viewStore.imageView.setImageResource(R.drawable.ic_menu_home);
		} else {
			viewStore.imageView.setImageResource(R.drawable.ic_menu_agenda);
		}
		viewStore.textView.setText(item.getText());
		
		return	view;
	}
}
