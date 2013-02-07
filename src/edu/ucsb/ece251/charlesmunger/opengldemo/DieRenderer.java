package edu.ucsb.ece251.charlesmunger.opengldemo;


import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import roboguice.RoboGuice;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DieRenderer implements GLSurfaceView.Renderer {
	private final Provider<GLDrawable> mModel;
	private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    
	@Inject
	public DieRenderer(Provider<GLDrawable> mModel) {
		this.mModel = mModel;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

     // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, -5f, 0, 0f, 0, 0, 0f, 1f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        // Create a rotation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
//        Matrix.setRotateM(mRotationMatrix, 0, angle, 1, 1, 0.5f);

        // Combine the rotation matrix with the projection and camera view
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0.0f, -1.0f, -1.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, Arrays.copyOf(mMVPMatrix, mMVPMatrix.length), 0, mRotationMatrix, 0);
//        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);
        // Draw triangle
        mModel.get().draw(mMVPMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	}
	
}
