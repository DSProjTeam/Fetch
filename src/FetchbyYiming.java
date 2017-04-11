import org.json.simple.JSONObject;

import java.io.*;
import java.util.HashMap;

public class FetchbyYiming {
    public static void main(String[] args) {

    }
    public static void fetch(JSONObject json, KeyList keyList, DataOutputStream out) {
        JSONObject response = new JSONObject();
        if (!json.containsKey("resourceTemplate")) {
            response.put("response", "error");
            response.put("errorMessage", "missing resourceTemplate");
        } else {
            JSONObject resourceTemplate = (JSONObject) json.get("resourceTemplate");
            if (!resourceTemplate.containsKey("uri") || !resourceTemplate.containsKey("channal")) {
                response.put("response", "error");
                response.put("errorMessage", "missing resourceTemplate");
            } else {
                String uri = (String) resourceTemplate.get("uri");
                if (!uri.startsWith("file")) {
                    response.put("response", "error");
                    response.put("errorMessage", "invalid resourceTemplate");
                } else {
                    String channal = (String) resourceTemplate.get("channal");
                    HashMap<String, HashMap<String, HashMap<String, Integer>>> keys = keyList.getKeys();
                    if (!keys.containsKey(channal)) {
                        response.put("response", "error");
                        response.put("errorMessage", "invalid resourceTemplate");
                    } else {
                        //send file
                        if (!keys.get(channal).containsKey(uri)) {
                            response.put("response", "error");
                            response.put("errorMessage", "invalid resourceTemplate");
                        } else {
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(uri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            byte[] buffer = new byte[4096];
                            File file = new File(uri);
                            response.put("response", "success");
                            response.put("filename", "testcat");//filename depends on real filename
                            response.put("filetype", uri.substring(uri.indexOf(".") + 1));//jpg or txt
                            response.put("filelength", file.length());
                            try {
                                out.writeUTF(response.toString());
                                out.flush();
                                while (fis.read(buffer) > 0) {
                                    out.write(buffer);
                                }
                                out.flush();
                                fis.close();
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
