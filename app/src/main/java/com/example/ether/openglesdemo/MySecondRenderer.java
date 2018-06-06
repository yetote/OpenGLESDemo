package com.example.ether.openglesdemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.ether.openglesdemo.utils.MatrixHelper;
import com.example.ether.openglesdemo.utils.ShaderHelper;
import com.example.ether.openglesdemo.utils.TextRecourseReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static com.example.ether.openglesdemo.Constants.BYTES_PER_FLOAT;

public class MySecondRenderer implements GLSurfaceView.Renderer {
    private final float[] modelMatrix = new float[16];
    private static final String TAG = "MyRenderer";
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectMatrix = new float[16];
    private int uMatrixLocation;
    private int aColorLocation;
    private int aPositionLocation;
    private final Context context;
    private int program;
    /**
     * 一个float类型占4个字节
     */
    private FloatBuffer vertexData;
    /**
     * 每个顶点有两个坐标点想，x，y
     */
    public static final int POSITION_COMPONENT_COUNT = 2;
    public static final int COLOR_COMPONENT_COUNT = 3;
    public static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    public MySecondRenderer(Context context) {
        this.context = context;
        float[] tableVertices = {
                //三角形,   x,     y,    z,    w,    r,    g,    b,
                   0f,    0f,  1.0f, 1.0f, 1.0f,
                -0.5f, -0.8f,  0.7f, 0.7f, 0.7f,
                 0.5f, -0.8f,  0.7f, 0.7f, 0.7f,
                 0.5f,  0.8f,  0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f,  0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f,  0.7f, 0.7f, 0.7f,

                //中间的直线
                -0.5f, 0.0f,  1.0f, 0.0f, 0.0f,
                 0.5f, 0.0f,  1.0f, 0.0f, 0.0f,

                //木槌
                0.0f, -0.4f,  0.0f, 0.0f, 1.0f,
                0.0f,  0.4f,  1.0f, 0.0f, 0.0f
        };
        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)//分配了一块本地内存，不会被gc影响，大小为顶点数组的长度*所占的字节数
                .order(ByteOrder.nativeOrder())//使字节缓冲区（byteBuffer）按照本地字节序（nativeOrder）进行排序
                .asFloatBuffer();
        /*
         * 当进程结束后占用的内存会被释放掉
         * 但是如果有很多的字节缓冲区，就需要进行手动管理了
         * */
        vertexData.put(tableVertices);//将数据从虚拟机中复制到本地内存中
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 0.0f, 1.0f, 0.0f);

        String vertexShaderSource = TextRecourseReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);

        String fragmentShaderSource = TextRecourseReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);


        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);


        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);


        ShaderHelper.validateProgram(program);


        glUseProgram(program);

        aColorLocation = glGetAttribLocation(program, A_COLOR);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        vertexData.position(0);
        /**
         * 在缓冲区找到对应数据的位置
         * @param indx 属性位置
         * @param size 属性所包含的分量的个数
         * @param type 数据的类型
         * @param normalized 整形数据才有意义，忽略掉
         * @param stride 跨距，告诉openGL每个位置或颜色的间隔，只有一个数组存储多个属性的时候才有意义
         * @param ptr 从哪里读取数据
         */
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
        /*在缓冲区找到颜色的对应代码*/
        //从第一个颜色代码的位置开始读取
        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);


    }

    /**
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectMatrix, 45, (float) width / (float) height, 1f, 10f);
        /*设置单位矩阵，
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1,
         */
        setIdentityM(modelMatrix, 0);
        //Z轴方向平移-2个单位
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectMatrix, 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        glDrawArrays(GL_LINES, 6, 2);
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
