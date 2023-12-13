package util;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int textureId;

    public Texture(String filePath) {
        // Load the texture file
        ByteBuffer imageData;
        int width, height;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            IntBuffer pChannels = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);  // If you're working with OpenGL, make sure to set this to true.
            imageData = stbi_load(filePath, pWidth, pHeight, pChannels, 4);
            if (imageData == null) {
                throw new RuntimeException("Failed to load texture file '" + filePath + "' : " + stbi_failure_reason());
            }

            width = pWidth.get();
            height = pHeight.get();
        }

        // Create a new OpenGL texture
        textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(imageData);
    }

    public int getTextureId() {
        return textureId;
    }
}