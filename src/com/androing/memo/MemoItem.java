package com.androing.memo;

public class MemoItem {

	//	index
	private int index;
	
	//	���(�v���C�x�[�g=0, �d��=1)
	private int type;

	//	�e�L�X�g
	private String text;

	public MemoItem() {
		index = -1;
		type = 0;
		text = "";
	}
	
	public MemoItem(int index, int type, String text) {
		this.index = index;
		this.type = type;
		this.text = text;
	}

	public int getIndex() {
		return	index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getType() {
		return	type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getText() {
		return	text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
