package edu.ucsb.ece251.charlesmunger.opengldemo.modules;

import com.google.inject.AbstractModule;

import edu.ucsb.ece251.charlesmunger.opengldemo.GLDrawable;
import edu.ucsb.ece251.charlesmunger.opengldemo.TextureDieModel;

public class TexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GLDrawable.class).to(TextureDieModel.class);
	}

}
