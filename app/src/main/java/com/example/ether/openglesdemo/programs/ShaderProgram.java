package com.example.ether.openglesdemo.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.example.ether.openglesdemo.utils.ShaderHelper;
import com.example.ether.openglesdemo.utils.TextRecourseReader;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {
    public static final String U_COLOR="u_Color";
    public static final String U_MATRIX="u_Matrix";
    public static final String U_TEXTURE_UNIT="u_TextureUnit";

    public static final String A_POSITION="a_Position";
    public static final String A_TEXTURE_COORDINATES="a_TextureCoordinates";
    public static final String A_COLOR="a_Color";
    public  final int program;

    public ShaderProgram(Context context,int vertexShaderRecourseId,int fragmentShaderRecourseId) {
        program= ShaderHelper.buildProgram(TextRecourseReader.readTextFileFromResource(context,vertexShaderRecourseId),
                TextRecourseReader.readTextFileFromResource(context,fragmentShaderRecourseId));

    }
    public void useProgram(){
        glUseProgram(program);
    }
}
