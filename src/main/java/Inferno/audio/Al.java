package Inferno.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.net.URL;

import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.ALC10.*;

public class Al {
    // Initialize Open AL
    public static void init(){
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        long device = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        long context = alcCreateContext(device,attributes);
        alcMakeContextCurrent(context);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        AL.createCapabilities(alcCapabilities);
    }
    // Create buffer class
    public static int createBuffer(String path){
        int buffer = alGenBuffers();
        WaveData waveData = WaveData.create(path);
        if (waveData == null) {
            return -1;
        }

        alBufferData(buffer,waveData.format,waveData.data,waveData.samplerate);
        waveData.dispose();
        return buffer;
    }
    // play sound method
    public static int playSound(int buffer){
        int source = AL10.alGenSources();
        AL10.alSourcei(source,AL10.AL_BUFFER,buffer);
        AL10.alSourcePlay(source);
        return source;
    }

    // Wave data class
    public static class WaveData{
        public final int format;
        public final int samplerate;
        public final ByteBuffer data;
        private WaveData(byte[] data, int format, int samplerate){
            this.data = ByteBuffer.wrap(data);
            this.format = format;
            this.samplerate = samplerate;
        }
        public void dispose(){
            data.clear();
        }
        public static WaveData create(String path){
            URL url = WaveData.class.getResource(path);

            if (url == null) {
                return null;
            }

            byte[] data = null;
            int format = -1;
            int samplerate = -1;
            try{
                data = WaveData.class.getResourceAsStream(path).readAllBytes();
                format = AL10.AL_FORMAT_MONO16;
                samplerate = 44100;
            }catch (Exception e){
                e.printStackTrace();
            }
            return new WaveData(data,format,samplerate);
        }

    }
}