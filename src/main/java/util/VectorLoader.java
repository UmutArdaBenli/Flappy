package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class VectorLoader {

    public VectorLoader(String path){
        try {
            FileReader fr = new FileReader(path);
            System.out.println(fr.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
