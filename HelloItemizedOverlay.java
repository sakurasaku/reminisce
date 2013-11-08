package com.ams.reminisce;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public HelloItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}
	
	public void addOverlay(OverlayItem overlay) {
		
		mOverlays.add(overlay);
		populate();
	}
	

	@Override
	protected OverlayItem createItem(int i) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return mOverlays.size();
	}

}
