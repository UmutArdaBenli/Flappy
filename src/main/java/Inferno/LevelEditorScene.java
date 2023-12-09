package Inferno;

import Renderer.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene{

    private float[] vertexArray = {
            // position          // color
             1f,-1f, 0.0f,    1.0f,1.0f,0.0f,1.0f,
            -1f, 1f, 0.0f,    1.0f,0.0f,1.0f,1.0f,
             1f, 1f, 0.0f,    1.0f,0.0f,0.0f,1.0f,
            -1f,-1f, 0.0f,    1.0f,1.0f,1.0f,1.0f
    };
    // IMPORTANT: MUST BE COUNTERCLOCKWISE ORDER
    private int[] elementArray = {
            // position          // color
            2,1,0, // top right triangle
            0,1,3
    };

    private int vaoID,vboID,eboID;

    public LevelEditorScene(){
        Shader testShader = new Shader("Assets/Shaders/default.glsl");
        testShader.compile();
        System.out.println("Inside Level Editor scene");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }

    @Override
    public void Init() {
        // ====================================
        // INITIALIZE VAO, VBO, and EBO Buffer objects and hand over to the GPU
        // ====================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0,positionsSize,GL_FLOAT,false,vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        // bind Shader program

        // bind the VAO that we're using
        glBindVertexArray(vaoID);
        // Enable the vertex attribute pointer
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT,0);
        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glUseProgram(0);


    }
}
