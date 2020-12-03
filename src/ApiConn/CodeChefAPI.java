package ApiConn;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CodeChefAPI {

    public static void main(String[] args) throws IOException {
        CodeChefAPI c = new CodeChefAPI();
        c.getJSONCodechef("bryanttv");
    }

    public void getJSONCodechef(String codechefuser) throws MalformedURLException, IOException {
        String sURL = "https://competitive-coding-api.herokuapp.com/api/codechef/" + codechefuser;
        // Connect to the URL using java's native library
        URL url;
        url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
        //get all solved 
        List<Practice> arr = new ArrayList<>();
        try {
            JsonObject fully = rootobj.getAsJsonObject("fully_solved");
            JsonArray fullyarr = fully.getAsJsonArray("Practice");
            Iterator<JsonElement> it = fullyarr.iterator();

            while (it.hasNext()) {
                JsonElement next = it.next();
                JsonObject aux = next.getAsJsonObject();
                JsonPrimitive name = aux.getAsJsonPrimitive("name");
                JsonPrimitive link = aux.getAsJsonPrimitive("link");
                arr.add(new Practice(name.getAsString(), link.getAsString()));
            }
        } catch (NullPointerException e) {
            arr.add(new Practice("No excercises done yet", " begin your training"));
        }

        //get parcial solved
        List<Practice> arr2 = new ArrayList<>();
        try {
            JsonObject parcial = rootobj.getAsJsonObject("partially_solved");
            JsonArray parcialarr = parcial.getAsJsonArray("Practice");
            Iterator<JsonElement> it2 = parcialarr.iterator();

            while (it2.hasNext()) {
                JsonElement next = it2.next();
                JsonObject aux = next.getAsJsonObject();
                JsonPrimitive name = aux.getAsJsonPrimitive("name");
                JsonPrimitive link = aux.getAsJsonPrimitive("link");
                arr2.add(new Practice(name.getAsString(), link.getAsString()));
            }
        } catch (NullPointerException e) {
            arr2.add(new Practice("No excercises done yet", " begin your training"));
        }

        Root codechefinfo = new Gson().fromJson(rootobj, Root.class);
        codechefinfo.fully_solved.practice = arr;
        codechefinfo.partially_solved.practice = arr2;
        System.out.println(codechefinfo.toString());
    }

    public class UserDetails {

        public String name;
        public String username;
        public String country;
        public String state;
        public String city;
        public String studentProfessional;
        public String institution;
    }

    public class Contest {

        public String name;
        public int rating;
        public String global_rank;
        public String country_rank;
    }

    public class Practice {

        public String name;
        public String link;

        public Practice(String name, String link) {
            this.name = name;
            this.link = link;
        }

    }

    public class FullySolved {

        public int count;
        public List<Practice> practice = new ArrayList<>();
    }

    public class PartiallySolved {

        public int count;
        public List<Practice> practice = new ArrayList<>();
    }

    public class ContestRating {

        public String code;
        public String getyear;
        public String getmonth;
        public String getday;
        public String reason;
        public String penalised_in;
        public String rating;
        public String rank;
        public String name;
        public String end_date;
    }

    public class Root {
        public String status;
        public int rating;
        public String stars;
        public int highest_rating;
        public String global_rank;
        public String country_rank;
        public UserDetails user_details;
        public List<Contest> contests = new ArrayList<>();
        public List<ContestRating> contest_ratings = new ArrayList<>();
        public FullySolved fully_solved;
        public PartiallySolved partially_solved;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Codechef stats \n\n");
            sb.append("___User info___\n\n");
            sb.append("Name: " + user_details.name + "\n");
            sb.append("Country: " + user_details.country + "\n");
            sb.append("City: " + user_details.city + "\n");
            sb.append("Institution: " + user_details.institution + "\n");
            sb.append("Stars: " + stars + "\n");
            sb.append("Highest rating: " + highest_rating + "\n");
            sb.append("Global rank: " + global_rank + "\n");
            sb.append(user_details.country + " rank: " + country_rank + "\n");
            sb.append("\n\n___Problems___\n\n");
            sb.append("Fully resolved:" + fully_solved.count + " \n");
            sb.append("----------------------------------- \n");
            sb.append("Name : link \n");
            for (int i = 0; i < fully_solved.practice.size(); i++) {
                sb.append(fully_solved.practice.get(i).name + "\t : " + fully_solved.practice.get(i).link + "\n");
            }

            sb.append("\nPartial resolved:" + partially_solved.count + " \n");
            sb.append("----------------------------------- \n");
            for (int i = 0; i < partially_solved.practice.size(); i++) {
                sb.append(partially_solved.practice.get(i).name + " : " + partially_solved.practice.get(i).link + "\n");
            }
            sb.append("\n\n___Contests___\n\n");
            for (int i = 0; i < contests.size(); i++) {
                sb.append("Name: " + contests.get(i).name + "\n");
                sb.append("Rating: " + contests.get(i).rating + "\n");
                sb.append("Global rank: " + contests.get(i).global_rank + "\n");
                sb.append(user_details.country + " rank: " + contests.get(i).country_rank + "\n\n");
            }
            sb.append("\n\n___Contests details___\n\n");
            if (contest_ratings.size() == 0) {
                sb.append("No participation in CodeChef contest");
            } else {
                for (int i = 0; i < contest_ratings.size(); i++) {
                    sb.append("Code: " + contest_ratings.get(i).code + "\n");
                    sb.append("Date: " + contest_ratings.get(i).getday + "/" + contest_ratings.get(i).getmonth + "/" + contest_ratings.get(i).getyear + "\n");
                    sb.append("Contest Name: " + contest_ratings.get(i).name + "\n");
                    sb.append("End date: " + contest_ratings.get(i).end_date + "\n");
                    sb.append("Rank: " + contest_ratings.get(i).rank + "\n");
                    sb.append("Rating: " + contest_ratings.get(i).rating + "\n");
                }
            }
            return sb.toString();
        }

    }
}
