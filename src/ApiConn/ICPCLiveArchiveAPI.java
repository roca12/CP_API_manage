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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ICPCLiveArchiveAPI {

    public static void main(String[] args) throws IOException {
        EstadisticasCompletas res = getJSONICPC("694550");
        System.out.println(res.toString());
    }

    public static EstadisticasCompletas getJSONICPC(String uvaid) throws MalformedURLException, IOException {
        String sURL = "https://icpcarchive.ecs.baylor.edu/uhunt/api/subs-user/" + uvaid;
        // Connect to the URL using java's native library
        URL url;
        url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
        EstadisticasICPC icpcinfo = new Gson().fromJson(rootobj, EstadisticasICPC.class);
        EstadisticasCompletas artemisinfo = calcAll(icpcinfo);
        return artemisinfo;

    }

    /*
    10 : Submission error
    15 : Can't be judged
    20 : In queue
    30 : Compile error
    35 : Restricted function
    40 : Runtime error
    45 : Output limit
    50 : Time limit
    60 : Memory limit
    70 : Wrong answer
    80 : PresentationE
    90 : Accepted
     */
    public static EstadisticasCompletas calcAll(EstadisticasICPC e) {
        /*
        0 Submission ID
        1 Problem ID
        2 Verdict ID
        3 Runtime
        4 Submission Time (unix timestamp)
        5 Language ID (1=ANSI C, 2=Java, 3=C++, 4=Pascal, 5=C++11)
        6 Submission Rank
        */
        EstadisticasCompletas aux = new EstadisticasCompletas();
        HashSet<Integer> a = new HashSet<>();
        HashSet<Integer> b = new HashSet<>();
        aux.name = e.name;
        aux.uname = e.uname;
        aux.submissions = e.subs.size();
        for (int i = 0; i < e.subs.size(); i++) {
            if (a.add(e.subs.get(i).get(1))) {
                aux.problemsTried.add((e.subs.get(i).get(1)));
            }
            if (e.subs.get(i).get(2) == 90) {
                if (b.add(e.subs.get(i).get(1))) {
                    aux.problemsCorrects.add((e.subs.get(i).get(1)));
                }
            }
            switch (e.subs.get(i).get(2)) {
                case 10:
                    aux.submissionerror++;
                    break;
                case 15:
                    aux.cantbejudged++;
                    break;
                case 20:
                    aux.inqueue++;
                    break;
                case 30:
                    aux.compileerror++;
                    break;
                case 35:
                    aux.restrictedfunction++;
                    break;
                case 40:
                    aux.runtimeerror++;
                    break;
                case 45:
                    aux.outputlimit++;
                    break;
                case 50:
                    aux.timelimt++;
                    break;
                case 60:
                    aux.memorylimit++;
                    break;
                case 70:
                    aux.wronganswer++;
                    break;
                case 80:
                    aux.presentationerror++;
                    break;
                case 90:
                    aux.accepted++;
                    break;
            }
            switch (e.subs.get(i).get(5)) {
                case 1:
                    aux.ANSIC++;
                    break;
                case 2:
                    aux.JAVA++;
                    break;
                case 3:
                    aux.CPP++;
                    break;
                case 4:
                    aux.PASCAL++;
                    break;
                case 5:
                    aux.CPP11++;
                    break;
            }
        }
        return aux;
    }

    public static class EstadisticasICPC {

        public String name;
        public String uname;
        public List<List<Integer>> subs;
    }

    public static class EstadisticasCompletas {

        public String name;
        public String uname;
        public List<Integer> problemsTried = new ArrayList<>();
        public List<Integer> problemsCorrects = new ArrayList<>();
        public int submissions = 0;
        public int accepted = 0;
        public int wronganswer = 0;
        public int timelimt = 0;
        public int runtimeerror = 0;
        public int presentationerror = 0;
        public int submissionerror = 0;
        public int cantbejudged = 0;
        public int inqueue = 0;
        public int compileerror = 0;
        public int restrictedfunction = 0;
        public int outputlimit = 0;
        public int memorylimit = 0;
        public int ANSIC = 0;
        public int JAVA = 0;
        public int CPP = 0;
        public int PASCAL = 0;
        public int CPP11 = 0;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append("\n").append(uname).append("\n\nProblems tried:\n");
            problemsTried.forEach((p) -> {
                sb.append(p).append(" ");
            });
            sb.append("\nProblems Solved:");
            problemsCorrects.forEach((p) -> {
                sb.append(p).append(" ");
            });
            sb.append("\n");
            sb.append("Accepted:").append(accepted).append("\n");
            sb.append("Presentation Error:").append(presentationerror).append("\n");
            sb.append("Runtime Error:").append(runtimeerror).append("\n");
            sb.append("Submmision error:").append(submissionerror).append("\n");
            sb.append("Can´t be judged:").append(cantbejudged).append("\n");
            sb.append("In queue:").append(inqueue).append("\n");
            sb.append("Compile Error:").append(compileerror).append("\n");
            sb.append("Restricted Function:").append(restrictedfunction).append("\n");
            sb.append("Output Limit:").append(outputlimit).append("\n");
            sb.append("Time limit:").append(timelimt).append("\n");
            sb.append("Memory Limit:" + memorylimit + "\n");
            sb.append("Wrong Answer:").append(accepted).append("\n");
            sb.append("\n___________________\n");
            sb.append("Acuracy: " + ((((double) accepted / (double) submissions) * 100)) + "%");
            sb.append("\n___________________\n");
            sb.append("Submissions per Language \n");
            sb.append("ANSI C: " + ANSIC + "\n");
            sb.append("JAVA: " + JAVA + "\n");
            sb.append("C++: " + CPP + "\n");
            sb.append("PASCAL: " + PASCAL + "\n");
            sb.append("C++11: " + CPP11 + "\n");

            return sb.toString();
        }
    }
}