package util;

import Blocks.Grass;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * The Batch class represents a batch of blocks to render.
 */
public class Batch {
    private List<Grass> grassList;
    private FloatBuffer vertexBuffer;
    private IntBuffer indicesBuffer;
    private float[] vertices;
    private int[] indices;
    private Matrix4f modelMatrix;
    private int vaoID, vboID, eboID;

    public Batch(List<Grass> grassList){
        this.grassList = grassList;
        prepareBatching();
    }
    private void prepareBatching(){
        this.modelMatrix = new Matrix4f().identity(); // create a model matrix and translate it by the position

        List<Float> verticesList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();

        for (Grass grass : grassList) {
            Vector3f position = grass.getPosition();
            float[] cubeVertices = grass.getVertices();
            for (int i = 0; i < cubeVertices.length / 3; i++) {
                verticesList.add(cubeVertices[i * 3] + position.x);
                verticesList.add(cubeVertices[i * 3 + 1] + position.y);
                verticesList.add(cubeVertices[i * 3 + 2] + position.z);
            }
            int[] cubeIndices = grass.getIndices();
            int vertexOffset = (verticesList.size() / 3) - (cubeVertices.length / 3); // Calculates the offset for the indices
            for (int index : cubeIndices) {
                indicesList.add(index + vertexOffset);
            }
        }

        this.vertices = new float[verticesList.size()];

        for (int i = 0; i < verticesList.size(); i++) {
            this.vertices[i] = verticesList.get(i);
        }
        this.indices = indicesList.stream().mapToInt(i -> i).toArray();
        // -------------------------//
        // Initialize VAO, VBO, EBO
        // -------------------------//
        vaoID = glGenVertexArrays();
        vboID = glGenBuffers();
        eboID = glGenBuffers();
        // -------------------------//
        // Bind VAO
        // -------------------------//
        glBindVertexArray(vaoID);
        // -------------------------//
        // Create FloatBuffer of vertices
        // -------------------------//
        vertexBuffer = BufferUtils.createFloatBuffer(vertices.length + 3);
        vertexBuffer.put(vertices).flip();
        // -------------------------//
        // Create IntBuffer of indices
        // -------------------------//
        indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();
        // -------------------------//
        // Bind to VBO, upload vertices
        // -------------------------//
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        // -------------------------//
        // Bind to EBO, upload indices
        // -------------------------//
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        // -------------------------//
        // Textures
        // -------------------------//
        Texture blockTexture = new Texture("Assets/Textures/grass.png");
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glBindTexture(GL_TEXTURE_2D, blockTexture.getTextureId());
        // -------------------------//
        // Position attribute
        // -------------------------//
        int positionsSize = 3;
        int vertexSizeBytes = positionsSize * Float.BYTES; // 3 elements (position) * 4 bytes each
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
    }

    public void draw(){
        // Draw the Grass objects
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
    }
}