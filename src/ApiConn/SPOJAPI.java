package ApiConn;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class SPOJAPI {

    public static void main(String[] args) throws IOException {
        getJSONSPOJ("edanv");
    }

    public static void getJSONSPOJ(String spojuser) throws MalformedURLException, IOException {
        String sURL = "https://competitive-coding-api.herokuapp.com/api/spoj/" + spojuser;
        // Connect to the URL using java's native library
        URL url;
        url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
        SpojUserStats spojinfo = new Gson().fromJson(rootobj, SpojUserStats.class);
        System.out.println(spojinfo.toString());

    }

    public class SpojUserStats {

        public String status;
        public String username;
        public String platform;
        public double points;
        public int rank;
        public List<String> solved;
        public List<String> todo;
        public String joindata;
        public String institute;

        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();
            sb.append("Codechef stats \n\n");
            sb.append("___User info___\n\n");
            sb.append("Name: " + username + "\n");
            sb.append("Institute: " + institute + "\n");
            sb.append("Join date: " + joindata + "\n");
            sb.append("Points: " + points + "\n");
            sb.append("Rank: " + rank + "\n\n");
            sb.append("Problems resolved:\n");
            for (String s : solved) {
                sb.append(s+"\t: https://www.spoj.com/problems/"+s+"\n");
            }
            sb.append("\nTo do:\n");
            for (String s : todo) {
                sb.append(s+"\t: https://www.spoj.com/problems/"+s+"\n");
            }
            return sb.toString();
        }
    }
}
