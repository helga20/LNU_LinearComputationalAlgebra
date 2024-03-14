package Form.Interfaces;

import java.io.File;
import java.io.FileNotFoundException;

public interface ImportFileInterface {
    void call(File file) throws FileNotFoundException;
}
