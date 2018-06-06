package com.example.ether.openglesdemo.objects;

import com.example.ether.openglesdemo.data.VertexArray;
import com.example.ether.openglesdemo.programs.ColorShaderProgram;
import com.example.ether.openglesdemo.utils.Geometry;

import java.util.List;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.example.ether.openglesdemo.Constants.BYTES_PER_FLOAT;

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;
    public final float radius;
    public final float height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommend> drawList;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(new Geometry.Point(0f, 0f, 0f), radius, height, numPointsAroundMallet);

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bingData(ColorShaderProgram colorProgram) {
        //绑定坐标
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);

    }

    public void onDraw() {
        for (ObjectBuilder.DrawCommend drawCommend : drawList) {
            drawCommend.draw();
        }
    }
}
