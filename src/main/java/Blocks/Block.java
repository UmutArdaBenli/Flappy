package Blocks;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Block {
    private Vector3f position;
    public Block (Vector3f position){
        this.position = position;
    }
    public void update(float dt){

    }
    public void draw(){

    }

    public Vector3fc getPosition() {
        return position;
    }
}
