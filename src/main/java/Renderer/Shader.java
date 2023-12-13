package Renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {
    private String vertexSource;
    private String fragmentSource;
    private String filepath;
    private int vertexID, fragmentID, shaderProgramID;
    public Shader(String filepath) {
        this.filepath = filepath;

        try {
            // Read the entire shader file
            String shaderSource = new String(Files.readAllBytes(Paths.get(filepath)));

            // Split the source string into parts at each "#type" keyword
            String[] shaderParts = shaderSource.split("(#type)( )+([a-zA-Z]+)");

            // Find and trim shader type identifiers
            String firstType = parseType(shaderSource, 0);
            String secondType = parseType(shaderSource, shaderSource.indexOf("#type", firstType.length()));

            // Assign vertex/fragment sources based on identifiers found
            assignShaderSource(firstType, shaderParts[1]);
            assignShaderSource(secondType, shaderParts[2]);

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }

        System.out.println(vertexSource);
        System.out.println(fragmentSource);
    }
    // Extracts shader type (e.g., "vertex" or "fragment") from shader file
    private String parseType(String shaderSource, int startFromIndex) {
        int index = shaderSource.indexOf("#type", startFromIndex) + 6;
        int eol = shaderSource.indexOf("\r\n", index);
        return shaderSource.substring(index, eol).trim();
    }
    private void assignShaderSource(String shaderType, String shaderSource) throws IOException {
        if (shaderType.equals("vertex")) {
            vertexSource = shaderSource;
        } else if (shaderType.equals("fragment")) {
            fragmentSource = shaderSource;
        } else {
            throw new IOException("Unexpected shader type: '" + shaderType + "'");
        }
    }
    public void compile() {
        vertexID = createShader(GL_VERTEX_SHADER, vertexSource);
        fragmentID = createShader(GL_FRAGMENT_SHADER, fragmentSource);

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);

        linkProgram(shaderProgramID);

        glShadeModel(GL_FLAT); // consider moving this to a place where you set up your other OpenGL states.
    }
    private int createShader(int shaderType, String source) {
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);

        checkCompileErrors(shaderID, shaderType);

        return shaderID;
    }
    private void checkCompileErrors(int shaderID, int shaderType) {
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);

        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            String shaderTypeStr = shaderType == GL_VERTEX_SHADER ? "Vertex" : "Fragment";
            System.out.printf("ERROR: %s shader compilation failed.\n%s\n", shaderTypeStr, glGetShaderInfoLog(shaderID, len));

            // Consider throwing an exception instead of using assert false : "", it's more standard in Java and can help debugging as it gives a full stack trace
            throw new RuntimeException("Shader compilation failed");
        }
    }
    private void linkProgram(int programID) {
        glLinkProgram(programID);

        int success = glGetProgrami(programID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(programID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: shader Program linking failed.");
            System.out.println(glGetProgramInfoLog(programID, len));
            // Similarly, consider exception handling here
            throw new RuntimeException("Shader program linking failed");
        }
    }
    public void use(){
        glUseProgram(shaderProgramID);
    }
    public void detach(){
        glUseProgram(0);
    }
    public void setUniform(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Dump the matrix into a FloatBuffer
            FloatBuffer fb = matrix.get(stack.mallocFloat(16));
            glUniformMatrix4fv(glGetUniformLocation(shaderProgramID, name), false, fb);
        }
    }
}
