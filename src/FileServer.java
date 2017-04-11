
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread {

    private ServerSocket ss;

    public FileServer(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket clientSock = ss.accept();
                saveFile(clientSock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile(Socket clientSock) throws IOException {
        DataInputStream in = new DataInputStream(clientSock.getInputStream());
        String firstLine = in.readUTF();
        JSONParser parser = new JSONParser();
        JSONObject cmd=null;
        try {
            cmd = (JSONObject) parser.parse(firstLine);
            if (cmd.get("response").equals("success")) {
                long filesize = (long) cmd.get("filelength");
                String fileName = (String) cmd.get("filename");
                String fileType = cmd.get("filetype").toString();
                FileOutputStream fos = new FileOutputStream(fileName+"1."+fileType);
                byte[] buffer = new byte[4096];
                int read;
                int totalRead = 0;
                long remaining = filesize;
                while ((read = in.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    fos.write(buffer, 0, read);
                }
                fos.close();

                in.close();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        FileServer fs = new FileServer(2345);
        fs.start();
    }

}