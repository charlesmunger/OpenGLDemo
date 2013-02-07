package edu.ucsb.ece251.charlesmunger.opengldemo.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import edu.ucsb.ece251.charlesmunger.opengldemo.DieModel;
import edu.ucsb.ece251.charlesmunger.opengldemo.GLDrawable;

public class DieModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GLDrawable.class).to(DieModel.class);
		bindConstant().annotatedWith(Names.named("VertexShaderCode")).to(
				 "uniform mat4 uMVPMatrix;" +
		            "attribute vec4 vPosition;" +
		            "void main() {" +
		            "  gl_Position = vPosition * uMVPMatrix;" +
		            "}");
	}

}
