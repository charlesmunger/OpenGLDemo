package edu.ucsb.ece251.charlesmunger.opengldemo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.android.opengl.MyGLRenderer;
import com.google.inject.Inject;

public final class TextureDieModel implements GLDrawable {
	
	private final String vertexShaderCode =
		// This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +

        "attribute vec4 vPosition;" +
        "attribute vec4 a_color;" +
        "attribute vec2 tCoordinate;" +
        "varying vec2 v_tCoordinate;" +
        "varying vec4 v_Color;" +
        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        "  gl_Position = uMVPMatrix * vPosition;" +
        "	v_tCoordinate = tCoordinate;" +
        "	v_Color = a_color;" +
        "}";

    private final String fragmentShaderCode =
    	"precision mediump float;" +
        "varying vec4 v_Color;" +
        "varying vec2 v_tCoordinate;" +
        "uniform sampler2D s_texture;" +
        "void main() {" +
        // texture2D() is a build-in function to fetch from the texture map
        "	vec4 texColor = texture2D(s_texture, v_tCoordinate); " + 
        "  gl_FragColor = v_Color*0.5 + texColor*0.5;" +
        "}";
	
 // Set color with red, green, blue and alpha (opacity) values
	private static final int COORDS_PER_VERTEX = 3;
	private int mPositionHandle, mTexCoordHandle;
	private int mProgram;

	private FloatBuffer vertexBuffer, texCoordBuffer, colorBuffer;
//    private final ShortBuffer drawListBuffer;
	private int mColorHandle, mTextureUniformHandle;
	private int mMVPMatrixHandle;
    private int mTextureDataHandle;
	private final int vertexCount = vertices.length / COORDS_PER_VERTEX;
	private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex
	static final int COORDS_PER_TEX = 2;
    static float texCoord[] = {
    	
		0.0f, 0.0f,
    	1.0f, 0.0f, 				
		1.0f, 1.0f

    };
    static float vertices[] = { // in counterclockwise order:
        0.0f,  0.622008459f, 0.0f,   // top
       -0.5f, -0.311004243f, 0.0f,   // bottom left
        0.5f, -0.311004243f, 0.0f    // bottom right
   };
    private final int texCoordStride = COORDS_PER_TEX * 4; // 4 bytes per float

    //===================================
    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    // Set another color
    static final int COLORB_PER_VER = 4;
    static float colorBlend[] = {
    	1.0f, 0.0f, 0.0f, 1.0f,
    	0.0f, 1.0f, 0.0f, 1.0f,
    	0.0f, 0.0f, 1.0f, 1.0f
    };
    //private final int colorBlendCount = colorBlend.length / COLORB_PER_VER;
    private final int colorBlendStride = COLORB_PER_VER * 4;
	
//	private static final float[] vertices = {
//		    -1.0f,-1.0f,-1.0f, // triangle 1 : begin
//		    -1.0f,-1.0f, 1.0f,
//		    -1.0f, 1.0f, 1.0f, // triangle 1 : end
////		    1.0f, 1.0f,-1.0f, // triangle 2 : begin
////		    -1.0f,-1.0f,-1.0f,
////		    -1.0f, 1.0f,-1.0f, // triangle 2 : end
////		    1.0f,-1.0f, 1.0f,
////		    -1.0f,-1.0f,-1.0f,
////		    1.0f,-1.0f,-1.0f,
////		    1.0f, 1.0f,-1.0f,
////		    1.0f,-1.0f,-1.0f,
////		    -1.0f,-1.0f,-1.0f,
////		    -1.0f,-1.0f,-1.0f,
////		    -1.0f, 1.0f, 1.0f,
////		    -1.0f, 1.0f,-1.0f,
////		    1.0f,-1.0f, 1.0f,
////		    -1.0f,-1.0f, 1.0f,
////		    -1.0f,-1.0f,-1.0f,
////		    -1.0f, 1.0f, 1.0f,
////		    -1.0f,-1.0f, 1.0f,
////		    1.0f,-1.0f, 1.0f,
////		    1.0f, 1.0f, 1.0f,
////		    1.0f,-1.0f,-1.0f,
////		    1.0f, 1.0f,-1.0f,
////		    1.0f,-1.0f,-1.0f,
////		    1.0f, 1.0f, 1.0f,
////		    1.0f,-1.0f, 1.0f,
////		    1.0f, 1.0f, 1.0f,
////		    1.0f, 1.0f,-1.0f,
////		    -1.0f, 1.0f,-1.0f,
////		    1.0f, 1.0f, 1.0f,
////		    -1.0f, 1.0f,-1.0f,
////		    -1.0f, 1.0f, 1.0f,
////		    1.0f, 1.0f, 1.0f,
////		    -1.0f, 1.0f, 1.0f,
////		    1.0f,-1.0f, 1.0f
//		};

	
	@Inject
	public TextureDieModel(Resources r) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertices);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        
        ByteBuffer texbb = ByteBuffer.allocateDirect(
        		texCoord.length * 4);
        // use the device hardware's native byte order
        texbb.order(ByteOrder.nativeOrder());
        
        // create a floating point buffer from the ByteBuffer
        texCoordBuffer = texbb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        texCoordBuffer.put(texCoord);
        // set the buffer to read the first coordinate
        texCoordBuffer.position(0);
        
        //===================================
        // color
        //===================================
        ByteBuffer cbb = ByteBuffer.allocateDirect(
        		colorBlend.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colorBlend);
        colorBuffer.position(0);
        
        //===================================
        // loading an image into texture
        //===================================
        mTextureDataHandle = loadTexture(r, R.drawable.myicon);
        Log.i("TDM", "texter handle is " + mTextureDataHandle);
	    final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
	    final int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

	    mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables
    }
	
	public void draw(float[] mvpMatrix) {
		// Add program to OpenGL ES environment
	    GLES20.glUseProgram(mProgram);

	    // get handle to vertex shader's vPosition member
	    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

	    // Enable a handle to the triangle vertices
	    GLES20.glEnableVertexAttribArray(mPositionHandle);

	    // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
	                                 GLES20.GL_FLOAT, false,
	                                 vertexStride, vertexBuffer);

	    mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_color");

	    GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, COLORB_PER_VER,
                                     GLES20.GL_FLOAT, false,
                                     colorBlendStride, colorBuffer);
        MyGLRenderer.checkGlError("glVertexAttribPointer...color");
        
        // setting texture coordinate to vertex shader
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "tCoordinate");
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mTexCoordHandle, COORDS_PER_TEX,
                					GLES20.GL_FLOAT, false,
                					texCoordStride, texCoordBuffer);   
        MyGLRenderer.checkGlError("glVertexAttribPointer...texCoord");
        
	    
	    // Set color for drawing the triangle
//	    GLES20.glUniform4fv(mColorHandle, 1, color, 0);

	 // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");
        

        // texture
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);        
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        
	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

	public static int loadShader(int type, String shaderCode){
	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
	
	public static int loadTexture(final Resources r, final int resourceId)
    {
        final int[] textureHandle = new int[1];
     
        GLES20.glGenTextures(1, textureHandle, 0);
     
        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling
            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(r, resourceId, options);
     
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
     
            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
     
            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
     
            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }
     
        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }
     
        return textureHandle[0];
    }
}