package Inferno;

import Blocks.Grass;
import Inferno.Input.KeyListener;
import Inferno.audio.Al;
import Renderer.Shader;
import World.Chunk;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene{

    // 3 coordinates per vertex now (x, y and z)
    private float fov = 70f; // Field of view
    Chunk chunk;
    Vector3f cameraPos = new Vector3f(0.0f, 0.0f, 3.0f); // Positioning the camera
    Vector3f cameraTarget = new Vector3f(0.0f, 0.0f, 0.0f); // Camera looks at origin
    Vector3f upVector = new Vector3f(0.0f, 1.0f, 0.0f); // up vector pointing up in the y
    Matrix4f model; // Matrix4f model
    Matrix4f view; // Matrix view
    Matrix4f projection; // Matrix4f projection
    public static Shader MainShader = new Shader("Assets/Shaders/default.glsl");
    private float pitch = 0.0f;
    private float yaw = 0.0f;
    private float playerSpeed = 5.0f;
    private List<Chunk> chunkList = new ArrayList<>();
    private boolean wireframe = false;

    public LevelEditorScene(){
        MainShader.compile();
        System.out.println("Inside Level Editor scene");
        Window.get().r = 0f;
        Window.get().g = 0;
        Window.get().b = 0;
    }

    @Override
    public void Init() {
        // ========== INITIALIZING AUDIO ============
        int sound = Al.createBuffer("Assets/Sounds/gaza.mp3");
        Al.playSound(sound);

        // ========= SETTING UP SHADERS =============
        // Model Matrix
        model = new Matrix4f();
        model.translate(new Vector3f(0.0f, 0.0f, 0.0f));
        model.rotate(0, new Vector3f(0, 0, 0));
        model.scale(1.0f, 1.0f, 1.0f);

        // View Matrix
        view = new Matrix4f();
        view.setLookAt(new Vector3f(cameraPos),
                    new Vector3f(cameraTarget),
                    new Vector3f(upVector));

        // Projection Matrix
        projection = new Matrix4f();
        projection.perspective((float) Math.toRadians(fov),
                /* Aspect ratio */ 1280f / 720f,
                0.1f,
                100.0f);

        // Pass matrices to the shader

        // ======= CONFIGURING OPENGL CONTEXT ========
        glViewport(0, 0, 1280, 720);

        glShadeModel(GL_FLAT);
        // ======= Creating Chunks ========
        chunkList.add(new Chunk());
    }
    @Override
    public void update(float dt) {

        if(KeyListener.isKeyPressed(KeyEvent.VK_F)){
            wireframe = !wireframe;
            if(wireframe){
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            }else{
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            }
        }

        float dz = (float) (-Math.sin(yaw) * playerSpeed * dt);
        float dx = (float) (Math.cos(yaw) * playerSpeed * dt);

        float rightDz = (float) (-Math.sin(yaw + Math.PI / 2) * playerSpeed * dt);
        float rightDx = (float) (Math.cos(yaw + Math.PI / 2) * playerSpeed * dt);

        //KeyListener.isKeyPressed(GLFW_SPACE)
        if (KeyListener.isKeyPressed(KeyEvent.VK_W)) {
            cameraPos.z -= dz;
            cameraPos.x += dx;
        }

        if (KeyListener.isKeyPressed(KeyEvent.VK_S)) {
            cameraPos.z += dz;
            cameraPos.x -= dx;
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_D)) {
            cameraPos.z -= rightDz;
            cameraPos.x += rightDx;
        }

        if (KeyListener.isKeyPressed(KeyEvent.VK_A)) {
            cameraPos.z += rightDz;
            cameraPos.x -= rightDx;
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_E)){
            cameraPos.y += playerSpeed * dt;
            cameraTarget.y += playerSpeed * dt;
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_Q)){
            cameraPos.y -= playerSpeed * dt;
            cameraTarget.y -= playerSpeed * dt;
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_I)) {
            pitch += 5 * dt; // pitch up
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_K)) {
            pitch -= 5 * dt; // pitch down
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_J)) {
            yaw -= 5 * dt; // yaw left
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_L)) {
            yaw += 5 * dt; // yaw right
        }
        cameraTarget.x = cameraPos.x + (float)Math.cos(yaw);
        cameraTarget.y = cameraPos.y + (float)Math.tan(pitch);
        cameraTarget.z = cameraPos.z + (float)Math.sin(yaw);
        view.identity().lookAt(cameraPos, cameraTarget, upVector);
        render();
        //System.out.println("X: " + cameraX + " " + "Y: " + cameraY + " " + "Z: " + cameraZ);
    }
    public void render() {
        // Create the model matrix and apply transformations
        MainShader.use();
        MainShader.setUniform("model", model);
        MainShader.setUniform("view", view);
        MainShader.setUniform("projection", projection);
        for(Chunk chunk : chunkList){
            chunk.drawChunk();
        }
        MainShader.detach();
    }

}
