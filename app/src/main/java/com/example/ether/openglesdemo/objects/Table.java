package com.example.ether.openglesdemo.objects;

import android.opengl.GLES20;

import com.example.ether.openglesdemo.Constants;
import com.example.ether.openglesdemo.data.VertexArray;
import com.example.ether.openglesdemo.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static com.example.ether.openglesdemo.Constants.BYTES_PER_FLOAT;

public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COUNT) * BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATE = {
//              X,     Y,    S,    T
            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f,
    };
    private final VertexArray vertexArray;

    public Table() {
        vertexArray = new VertexArray(VERTEX_DATE);

    }

    public void bingData(TextureShaderProgram textureProgram) {
        //绑定坐标
        vertexArray.setVertexAttribPointer(0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        //绑定纹理
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COUNT,
                STRIDE);
    }

    public void draw() {
        GLES20.glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
