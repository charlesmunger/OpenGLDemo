package edu.ucsb.ece251.charlesmunger.opengldemo.modules;

import android.opengl.GLSurfaceView;

import com.google.inject.AbstractModule;

import edu.ucsb.ece251.charlesmunger.opengldemo.DieRenderer;

public class CustomRenderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GLSurfaceView.Renderer.class).to(DieRenderer.class);
	}
}
