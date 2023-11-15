package Inferno.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;

import static org.lwjgl.openal.ALC10.*;

public class Al {
    long device = alcOpenDevice((ByteBuffer)null);
    ALCCapabilities deviceCaps = ALC.createCapabilities(device);

    long context = alcCreateContext(device, (ByteBuffer)null);



}
