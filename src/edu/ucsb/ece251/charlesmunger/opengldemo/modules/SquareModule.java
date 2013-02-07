package edu.ucsb.ece251.charlesmunger.opengldemo.modules;

import com.example.android.opengl.MyGLRenderer;
import com.google.inject.AbstractModule;

import edu.ucsb.ece251.charlesmunger.opengldemo.GLDrawable;

public class SquareModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GLDrawable.class).to(MyGLRenderer.Square.class);
	}

}
