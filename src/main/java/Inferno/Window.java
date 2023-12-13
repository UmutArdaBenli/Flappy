package Inferno;

import Inferno.Input.KeyListener;
import Inferno.Input.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import util.Time;
import util.imageParser;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    int width,height;
    String title;
    private long glfwWindow;
    private static Window window = null;

    private static Scene currentScene;

    public float r,g,b,a = 1;
    private final imageParser appIcon = imageParser.load_image("Assets/icon.png");
    public float dt;

    private Window(){
        this.width = 1280;
        this.height = 720;
        this.title = "MysticForge: Minecraft";
    }



    public static void changeScene(int newScene){
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.Init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.Init();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return window;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();
        //free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        //terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }


    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        //initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_FALSE);

        //create the window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }
        // ======= CONFIGURING GLFW CONTEXT ========
        glfwSetFramebufferSizeCallback(glfwWindow, (window, newWidth, newHeight) -> {
            // update your viewport with the new dimensions
            glViewport(0, 0, newWidth, newHeight);
            // update width and height variables with new values
            this.width = newWidth;
            this.height = newHeight;
        });

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::KeyCallback);
        // Make the OpenGL context current.
        glfwMakeContextCurrent(glfwWindow);
        // Enable VSync
        glfwSwapInterval(0);
        // set flat faces

        GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(appIcon.get_width(), appIcon.get_heigh(), appIcon.get_image());
        imagebf.put(0, image);

        glfwSetWindowIcon(glfwWindow, imagebf);
        // Make the window visible
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        glEnable(GL_LIGHTING);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_NORMALIZE);
        Window.changeScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll events
            glfwPollEvents();
            glClearColor(r,g,b,1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if(dt >= 0) {
                currentScene.update(dt);
            }
            glfwSwapBuffers(glfwWindow);
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

}
