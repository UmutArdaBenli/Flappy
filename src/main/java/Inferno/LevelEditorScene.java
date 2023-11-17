package Inferno;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene{

    private String vertexShaderSrc =
            "#version 330 core\n" +
            "layout(location=0) in vec3 aPos;\n" +
            "layout(location=1) in vec4 aColor;\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos,1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

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
        System.out.println("Inside Level Editor scene");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }

    @Override
    public void Init() {
        //First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the Shader source to the GPU
        glShaderSource(vertexID,vertexShaderSrc);
        glCompileShader(vertexID);

        // Check for errors during compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == 0){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false : "";
        }

        //First load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the Shader source to the GPU
        glShaderSource(fragmentID,fragmentShaderSrc);
        glCompileShader(fragmentID);

        // Check for errors during compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == 0){
            int len = glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success= glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgram,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: shader programm linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram,len));
            assert false : "";
        }

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
        glUseProgram(shaderProgram);
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
