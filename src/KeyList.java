import java.util.HashMap;

/**
 * A class that represent the list of primary keys
 * created by Jiacheng Chen
 */
public class KeyList {
    private HashMap<String, HashMap<String, HashMap<String, Integer>>> keys;
    private Integer index;

    public KeyList() {
        keys = new HashMap<>();
        index = 0;
    }

    /**
     * put a key set into the list
     *
     * @param res
     * resource
     */

    public int put(Resource res) {
        String channel = res.getChannel();
        String owner = res.getOwner();
        String uri = res.getUri();

        if (keys.containsKey(channel)) {
            if (keys.get(channel).containsKey(uri)) {
                if (!keys.get(channel).get(uri).containsKey(owner)) {
                    return -2;
                } else {
                    return keys.get(channel).get(uri).get(owner);
                }
            } else {
                keys.get(channel).put(uri, new HashMap<String, Integer>());
                keys.get(channel).get(uri).put(owner, index);
            }
        } else {
            keys.put(channel, new HashMap<String, HashMap<String, Integer>>());
            keys.get(channel).put(uri, new HashMap<String, Integer>());
            keys.get(channel).get(uri).put(owner, index);
        }
        //System.out.println(index);
        index += 1;
        return -1;
    }

    /**
     * remove a key pair in the list using keys
     *
     * @param channel
     * @param uri
     * @param owner
     * @return if remove is successful;
     */
    public boolean remove(String channel, String uri, String owner) {
        if(keys.containsKey(channel)) {
            if (keys.get(channel).containsKey(uri)) {
                if (keys.get(channel).get(uri).containsKey(owner)) {
                    keys.get(channel).get(uri).remove(owner);
                    if (keys.get(channel).get(uri).isEmpty()) {
                        keys.get(channel).remove(uri);
                        if (keys.get(channel).isEmpty()) {
                            keys.remove(channel);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public HashMap<String, HashMap<String, HashMap<String, Integer>>> getKeys() {
        return keys;
    }
}
