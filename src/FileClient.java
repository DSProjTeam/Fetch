
import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileClient {

    private Socket s;

    public FileClient(String host, int port, String file) {
        try {
            s = new Socket(host, port);
            sendFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String file) throws IOException {
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        JSONObject response = new JSONObject();
        File filef = new File(file);
        response.put("response", "success");
        response.put("filename", "testpic");
        response.put("filetype", file.substring(file.indexOf(".") + 1));
        response.put("filelength", filef.length());
        out.writeUTF(response.toString());
        out.flush();
        while (fis.read(buffer) > 0) {
            out.write(buffer);
        }
        out.flush();
        fis.close();
        out.close();
    }

    public static void main(String[] args) {
        FileClient fc = new FileClient("localhost", 2345, "pic.jpg");//you can test txt file as well.
    }

}