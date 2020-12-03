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
import java.util.HashSet;
import java.util.List;

public class CodeforcesAPI {

    public static void main(String[] args) throws IOException {
        
        getJSONCodeforces("DmitriyH");
        getJSONCodeforces("BryanttV");
    }

    public static void getJSONCodeforces(String codeforcesuser) throws MalformedURLException, IOException {
        String sURL = "https://codeforces.com/api/user.info?handles=" + codeforcesuser;
        // Connect to the URL using java's native library
        URL url;
        url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
        UserInfo codeforcesinfo = new Gson().fromJson(rootobj, UserInfo.class);
        Submissions codeforcesinfosubmissions = getJSONCodeforcesSub(codeforcesuser);
        System.out.println(codeforcesuser+" "+codeforcesinfo.toString());
        System.out.println(codeforcesinfosubmissions.toString());

    }

    public static Submissions getJSONCodeforcesSub(String codeforcesuser) throws MalformedURLException, IOException {
        String sURL = "https://codeforces.com/api/user.status?handle=" + codeforcesuser;
        // Connect to the URL using java's native library
        URL url;
        url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
        Submissions s = new Gson().fromJson(rootobj, Submissions.class);
        return s;
    }

    public class UserInfoResult {

        public String lastName;
        public String country;
        public int lastOnlineTimeSeconds;
        public String city;
        public int rating;
        public int friendOfCount;
        public String titlePhoto;
        public String handle;
        public String avatar;
        public String firstName;
        public int contribution;
        public String organization;
        public String rank;
        public int maxRating;
        public int registrationTimeSeconds;
        public String maxRank;
    }

    public class UserInfo {

        public String status;
        public List<UserInfoResult> result;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Codeforces stats \n");
            sb.append("\n _______User info_______ \n\n");
            sb.append("Name: " + result.get(0).firstName + " " + result.get(0).lastName + "\n");
            sb.append("Country: " + result.get(0).country + "\n");
            sb.append("City: " + result.get(0).city + "\n");
            sb.append("Institution: " + result.get(0).organization + "\n");
            sb.append("Avatar: " + result.get(0).avatar + "\n");
            sb.append("Title photo: " + result.get(0).titlePhoto + "\n");
            sb.append("rank: " + result.get(0).rank + "\n");
            sb.append("max rank: " + result.get(0).maxRank + "\n");
            
            return sb.toString();
        }

    }

    public class Problem {

        public int contestId;
        public String index;
        public String name;
        public String type;
        public int points;
        public int rating;
        public List<String> tags;
    }

    public class Member {

        public String handle;
    }

    public class Author {

        public int contestId;
        public List<Member> members;
        public String participantType;
        public boolean ghost;
        public int startTimeSeconds;
        public int room;
    }

    public class SubmissionsResult {

        public int id;
        public int contestId;
        public int creationTimeSeconds;
        public long relativeTimeSeconds;
        public Problem problem;
        public Author author;
        public String programmingLanguage;
        public String verdict;
        public String testset;
        public int passedTestCount;
        public int timeConsumedMillis;
        public int memoryConsumedBytes;
    }

    public class Submissions {

        public String status;
        public List<SubmissionsResult> result;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n\n___Problems___\n\n");
            sb.append("----------------------------------- \n");
            int accepted = 0;
            HashSet<String> set= new HashSet<>();
            for (SubmissionsResult i : result) {
                if (i.verdict.equals("OK")&&set.add(i.problem.name)) {
                    sb.append("Problem name: " + i.problem.name + "\n");
                    sb.append("Problems tags:");
                    for (String s : i.problem.tags) {
                        sb.append(s + ",");
                    }
                    sb.append("\n");
                    sb.append("Language: " + i.programmingLanguage + "\n");
                    sb.append("\n");
                    accepted++;
                }
            }
            sb.append("Problems solved: " + accepted + "\n\n");
            sb.append("Total submits: " + result.size() + "\n\n");
            sb.append("Accuracy: "+(((double)accepted/result.size())*100)+"%");
            sb.append("\n\n");
            return sb.toString();
        }
    }
}
