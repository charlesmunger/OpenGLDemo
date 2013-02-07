package edu.ucsb.ece251.charlesmunger.opengldemo;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;

public class DieActivity extends RoboActivity {
	//@InjectView(R.id.dieView1) private GLSurfaceView dieView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_die_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_die_view, menu);
		return true;
	}

}
