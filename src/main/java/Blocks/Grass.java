package Blocks;

import Inferno.LevelEditorScene;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.EnumSet;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


/**
 * This class represents a Grass block.
 */
public class Grass extends Block{
    private Vector3f position;
    float[] vertices = new float[] {
            -SIZE / 2, -SIZE / 2, -SIZE / 2, // 0
            SIZE / 2, -SIZE / 2, -SIZE / 2, // 1
            SIZE / 2,  SIZE / 2, -SIZE / 2, // 2
            -SIZE / 2,  SIZE / 2, -SIZE / 2, // 3
            -SIZE / 2, -SIZE / 2,  SIZE / 2, // 4
            SIZE / 2, -SIZE / 2,  SIZE / 2, // 5
            SIZE / 2,  SIZE / 2,  SIZE / 2, // 6
            -SIZE / 2,  SIZE / 2,  SIZE / 2, // 7
    };
    int[] indices = new int[]{
            // Front face
            0, 1, 2,
            2, 3, 0,
            // Back face
            5, 4, 7,
            7, 6, 5,
            // Left face
            4, 0, 3,
            3, 7, 4,
            // Right face
            1, 5, 6,
            6, 2, 1,
            // Bottom face
            4, 5, 1,
            1, 0, 4,
            // Top face
            3, 2, 6,
            6, 7, 3
    };
    float[] textureCoords = {
            // Bottom-left,       bottom-right,      top-right,        top-left    (same for all faces)
            0.0f, 0.0f,        1.0f, 0.0f,        1.0f, 1.0f,        0.0f, 1.0f,  // Front face
            0.0f, 0.0f,        1.0f, 0.0f,        1.0f, 1.0f,        0.0f, 1.0f,  // Back face
            0.0f, 0.0f,        1.0f, 0.0f,        1.0f, 1.0f,        0.0f, 1.0f,  // Left face
            0.0f, 0.0f,        1.0f, 0.0f,        1.0f, 1.0f,        0.0f, 1.0f,  // Right face
            0.0f, 0.0f,        1.0f, 0.0f,        1.0f, 1.0f,        0.0f, 1.0f,  // Top face
            0.0f, 0.0f,        1.0f, 0.0f,        1.0f, 1.0f,        0.0f, 1.0f,  // Bottom face
    };
    /**
     * The variable visibleFaces represents the set of faces that are currently visible for a Grass object.
     * It is an EnumSet containing instances of the Face enum, which represents the possible faces of a block.
     * By default, all faces are set to be visible.
     */
    // Add these instance variables
    private static final float SIZE = 1f; //
    private Vector3f color;
    /**
     * Constructs a Grass object at the specified position.
     *
     * @param position The position of the Grass object.
     */
    public Grass(Vector3f position) {
        super(position);
        this.position = position;
        float r = (float)Math.random();
        float g = (float)Math.random();
        float b = (float)Math.random();
        this.color = new Vector3f(r,g,b);
    }
    public float[] getVertices() {
        return vertices;
    }
    public int[] getIndices() {
        return indices;
    }
    @Override
    public Vector3f getPosition() {
        return position;
    }
}
