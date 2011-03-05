package com.androing.memo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MemoEdit extends Activity {
	RadioGroup radioGroup;
	EditText editText;
	MemoItem memoList;
	
	private static final int MENU_ID_DELETE = (Menu.FIRST + 1);
	private static final int MENU_ID_SAVE = (Menu.FIRST + 2);
	private static final int MENU_ID_CANCEL = (Menu.FIRST + 3);

	private static final int TYPE_PRIVATE = 0;	//	プライベート
	private static final int TYPE_JOB = 1;		//	仕事
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        //	パラメータ取得
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	memoList = new MemoItem();
        	memoList.setIndex(extras.getInt("index"));
        	memoList.setType(extras.getInt("type"));
        	memoList.setText(extras.getString("memo"));

            radioGroup = (RadioGroup)findViewById(R.id.type_group);
            if(memoList.getType() == TYPE_PRIVATE) {
            	radioGroup.check(R.id.type_private);
            } else {
            	radioGroup.check(R.id.type_job);
            }

            editText = (EditText)findViewById(R.id.memo_edit);
            editText.setText(memoList.getText());
            editText.setSelection(memoList.getText().length());

        } else {
        	setResult(RESULT_CANCELED);
        	finish();
        }
    }

    //	オプションメニュー追加
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if(memoList.getText().length() > 0) {
    		menu.add(Menu.NONE, MENU_ID_DELETE, Menu.NONE, getText(R.string.menu_delete)).setIcon(android.R.drawable.ic_menu_delete);
    	}
   		menu.add(Menu.NONE, MENU_ID_SAVE, Menu.NONE, getText(R.string.menu_save)).setIcon(android.R.drawable.ic_menu_save);
    	menu.add(Menu.NONE, MENU_ID_CANCEL, Menu.NONE, getText(R.string.menu_cancel)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    	return	super.onCreateOptionsMenu(menu);
    }

    //	オプションメニュー押下時
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences sharedPref = getSharedPreferences("MEMO_DATA", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

    	switch(item.getItemId()) {
    	case MENU_ID_DELETE:	//	削除
    		editor.putString("mode", "delete");
    		editor.putInt("index", memoList.getIndex());
    		editor.commit();
    		setResult(RESULT_OK);
    		finish();
    		break;
    		
    	case MENU_ID_SAVE:		//	保存
    		if(editText.getText().length() == 0) {
    			String message = getText(R.string.memo_warning).toString();
    			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    			break;
    		}
    		
    		editor.putString("mode", "save");
    		editor.putInt("index", memoList.getIndex());
    		if(radioGroup.getCheckedRadioButtonId() == R.id.type_private) {
        		editor.putInt("type", TYPE_PRIVATE);
    		} else {
        		editor.putInt("type", TYPE_JOB);
    		}
    		editor.putString("memo", editText.getText().toString());
    		editor.commit();
    		setResult(RESULT_OK);
    		finish();
    		break;

    	case MENU_ID_CANCEL:	//	破棄
			setResult(RESULT_CANCELED);
			finish();
    		break;
    	}
    	return	super.onOptionsItemSelected(item);
    }
}
