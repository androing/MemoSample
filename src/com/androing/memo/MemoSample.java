package com.androing.memo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MemoSample extends Activity implements OnItemClickListener {

	private ArrayList<MemoItem> items = null;		//	�������X�g
	private MemoListAdapter memoAdapter = null;
	private ListView listView;
	private MenuItem menuItem_add;
	private MenuItem menuItem_alldel;
	
	private static final int MENU_ID_ADD = (Menu.FIRST + 1);
	private static final int MENU_ID_ALLDEL = (Menu.FIRST + 2);
	private static final int TYPE_PRIVATE = 0;	//	�v���C�x�[�g
	private static final int TYPE_JOB = 1;		//	�d��
	private static final int MEMO_MAX_NUM = 20;	//	�����ő吔
	
	private static final int REQUEST_MEMO_EDIT = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listView = (ListView)findViewById(R.id.memoList);

        items = new ArrayList<MemoItem>();
        memoAdapter = new MemoListAdapter(this, R.layout.list, items);
        listView.setAdapter(memoAdapter);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(this);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//	�����ҏW����̖߂�
		if(requestCode == REQUEST_MEMO_EDIT && resultCode == RESULT_OK) {
			SharedPreferences sharedPref = getSharedPreferences("MEMO_DATA", MODE_PRIVATE);
			String mode = sharedPref.getString("mode", "");
			int index = sharedPref.getInt("index", -1);
			int type = sharedPref.getInt("type", TYPE_PRIVATE);
			String memo = sharedPref.getString("memo", "");
			
			if(index < 0 || MEMO_MAX_NUM <= index) {
				//	�O�̈�
				return;
			}

			Boolean hit = false;
			for(int i=0; i<items.size(); i++) {
				MemoItem item = items.get(i);
				if(item.getIndex() == index) {
					if(mode.equals("save")) {
						//	�㏑���ۑ�
						item.setType(type);
						item.setText(memo);
						items.set(i, item);
						hit = true;
					} else if(mode.equals("delete")) {
						//�폜
						items.remove(i);
						hit = true;
					}
				}
			}
			if(hit == false && mode.equals("save")) {
				//	�ǉ�
				items.add(new MemoItem(index, type, memo));
			}
	        memoAdapter.notifyDataSetChanged();
			setMenu();
		}
	}
    
    //	���X�g�I��
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(parent == listView) {
    		//	�����ҏW�Ɉڍs
			Intent intent = new Intent(MemoSample.this, MemoEdit.class);
			MemoItem item = items.get(position);
			intent.putExtra("index", item.getIndex());
			intent.putExtra("type", item.getType());
			intent.putExtra("memo", item.getText());
			startActivityForResult(intent, REQUEST_MEMO_EDIT);
		}
	}

    //	�I�v�V�������j���[�ǉ�
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menuItem_add = menu.add(Menu.NONE, MENU_ID_ADD, Menu.NONE, getText(R.string.menu_add)).setIcon(android.R.drawable.ic_menu_add);
    	menuItem_alldel = menu.add(Menu.NONE, MENU_ID_ALLDEL, Menu.NONE, getText(R.string.menu_alldel)).setIcon(android.R.drawable.ic_menu_delete);
    	setMenu();
    	return	super.onCreateOptionsMenu(menu);
    }

    //	�I�v�V�������j���[������
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case MENU_ID_ADD:
    		//	�ǉ�
    		int	index = getFreeIndex();
    		if(index == -1) {
    			break;
    		}
    		
    		//	�����ҏW�Ɉڍs
			Intent intent = new Intent(MemoSample.this, MemoEdit.class);
			intent.putExtra("index", index);
			intent.putExtra("type", TYPE_PRIVATE);
			intent.putExtra("memo", "");
			startActivityForResult(intent, REQUEST_MEMO_EDIT);
    		break;
    		
    	case MENU_ID_ALLDEL:
    		//	�S�č폜
    		String title = getText(R.string.confirm_message).toString();
    		String message = getText(R.string.all_delete_confirm).toString();
    		
    		Builder builder = new AlertDialog.Builder(this);
        	builder.setTitle(title);
        	builder.setMessage(message);
        	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int whichButton) {
        			items.clear();
        	        memoAdapter.notifyDataSetChanged();
        			setMenu();
        		}
        	});
        	builder.setNegativeButton("Canbel", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int whichButton) {
        			//	��������
        		}
        	});
        	builder.create();
        	builder.show();
    		break;
    	}
    	return	super.onOptionsItemSelected(item);
    }

    //	���j���[�ݒ�
    private void setMenu() {
    	if(items.size() >= MEMO_MAX_NUM) {
    		menuItem_add.setEnabled(false);
    	} else {
    		menuItem_add.setEnabled(true);
    	}
    	
    	if(items.isEmpty()) {
    		menuItem_alldel.setEnabled(false);
    	} else {
    		menuItem_alldel.setEnabled(true);
    	}
    }

    //	�󂫃C���f�b�N�X�擾
    private int getFreeIndex() {
    	boolean[] index_list = new boolean[MEMO_MAX_NUM];
    	for(int i=0; i<MEMO_MAX_NUM; i++) {
    		index_list[i] = false;
    	}
    	for(int i=0; i<items.size(); i++) {
    		index_list[items.get(i).getIndex()] = true;
    	}
    	for(int i=0; i<MEMO_MAX_NUM; i++) {
    		if(index_list[i] == false) {
    			return	i;
    		}
    	}
    	return	-1;
    }
}
