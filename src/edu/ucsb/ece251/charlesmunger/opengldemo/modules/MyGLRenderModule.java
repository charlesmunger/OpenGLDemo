package edu.ucsb.ece251.charlesmunger.opengldemo.modules;

import android.opengl.GLSurfaceView;

import com.example.android.opengl.MyGLRenderer;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import edu.ucsb.ece251.charlesmunger.opengldemo.DieRenderer;

public class MyGLRenderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GLSurfaceView.Renderer.class).to(MyGLRenderer.class);
		bindConstant().annotatedWith(Names.named("VertexShaderCode")).to(
				"attribute vec4 vPosition;" + "void main() {"
						+ "  gl_Position = vPosition;" + "}");
		bindConstant().annotatedWith(Names.named("fragmentShaderCode")).to(
				"precision mediump float;" + "uniform vec4 vColor;"
						+ "void main() {" + "  gl_FragColor = vColor;" + "}");
	}
}
