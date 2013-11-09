package com.ams.reminisce;

import java.util.List;

import com.google.android.maps.*;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class GMapActivity extends MapActivity 
implements OnClickListener{
	
	LinearLayout linearLayout;
	MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable;
	HelloItemizedOverlay itemizedOverlay;
	Button btn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmap);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.foot);
        itemizedOverlay = new HelloItemizedOverlay(drawable);
        btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(this);
        
        GeoPoint point = new GeoPoint(35669831,139698844);
        OverlayItem overlayitem = new OverlayItem(point, "", "");
        
        GeoPoint point2 = new GeoPoint(35410000, 139460000);
        OverlayItem overlayitem2 = new OverlayItem(point2, "", "");
        
        
        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		if (v == btn){
            GeoPoint savePoint = mapView.getMapCenter();

            StringBuilder latlng = new StringBuilder();
            latlng.append("Lat:");
            latlng.append(savePoint.getLatitudeE6());
            latlng.append("\nLng:");
            latlng.append(savePoint.getLongitudeE6());
            
            
            
            
            
            
            
            
 		}
	}
}
