package World;

import Blocks.Grass;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import util.Batch;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    static Batch batch;
    private final Matrix4f model;
    private static final int SIZE_X = 32;
    private static final int SIZE_Z = 32;
    private static List<Grass> blocks = new ArrayList<>();
    Grass[][][] grid = new Grass[SIZE_X][SIZE_X][SIZE_Z];
    private static final float BLOCK_SIZE = 1f; // Block size to match the size in Grass class

    public Chunk() {
        model = new Matrix4f();
        for (int x = 0; x < SIZE_X; x++) {
            for (int z = 0; z < SIZE_X; z++) {
                for (int y = 0; y < SIZE_Z; y++) {
                    Vector3f pos = new Vector3f(x * BLOCK_SIZE, y * BLOCK_SIZE, -z * BLOCK_SIZE);
                    grid[x][y][z] = new Grass(pos);
                    blocks.add(new Grass(pos));
                }
            }
        }
        batch = new Batch(blocks);
    }
    public void drawChunk(){
        batch.draw();
    }
}
