package edu.ucsb.ece251.charlesmunger.opengldemo;

import roboguice.RoboGuice;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.google.inject.Inject;

public class DieView extends GLSurfaceView {

	@Inject GLSurfaceView.Renderer mRenderer;
	public DieView(Context context) {
		this(context,null);
	}
	
	public DieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setEGLContextClientVersion(2);
		RoboGuice.injectMembers(context, this);
		setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

}
