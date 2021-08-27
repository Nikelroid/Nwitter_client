package profiles;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class twitteSaver {
    public twitteSaver(int serial , String picture) {
        try {
            Path simple = Paths.get("simple.png");
            Path path = Paths.get(simple.toAbsolutePath().getParent()+
                    "\\main\\src\\resources\\twittes\\"+serial+".png");

            File outputFile = new File(String.valueOf(path));

            byte[] decodedBytes = Base64.getDecoder().decode(picture);
            FileUtils.writeByteArrayToFile(outputFile, decodedBytes);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
