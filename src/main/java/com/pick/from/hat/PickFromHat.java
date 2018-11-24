package com.pick.from.hat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class PickFromHat {
    private static Map<String, String> email2name = new HashMap<String, String>();
    private static List<String> names = new ArrayList<String>();
    private static String namesJsonFile = "/settings.json";
    private static String fromEmail;
    private static String fromPassword;

    public static void main(String[] args) {
        loadNamesWithMailAddress();
        suffleNames();
        sendMails();
    }

    private static void loadNamesWithMailAddress() {
        JSONParser parser = new JSONParser();
        try {
            URL fileUrl = PickFromHat.class.getResource(namesJsonFile);
            File file = new File(fileUrl.toURI());
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
            fromEmail = (String) jsonObject.get("fromEmail");
            fromPassword = (String) jsonObject.get("fromPassword");
            JSONArray mail2names = (JSONArray) jsonObject.get("names");
            for (Object obj : mail2names) {
                JSONObject mail2name = (JSONObject) obj;
                String email = (String) mail2name.get("email");
                String name = (String) mail2name.get("name");

                email2name.put(email, name);
                names.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void suffleNames() {
        Collections.shuffle(names);

        boolean noSelfPicking;
        do {
            int i = 0;
            noSelfPicking = true;
            for (String email : email2name.keySet()) {
                String name = email2name.get(email);
                String pickedName = names.get(i);
                if (name.equals(pickedName)) {
                    noSelfPicking = false;
                    Collections.shuffle(names);
                    break;
                }
                i++;
            }
        } while (!noSelfPicking);
    }

    private static void sendMails() {
        int i = 0;
        for (String email : email2name.keySet()) {
            String name = email2name.get(email);
            String title = "You picked a name from a hat";
            String htmlBody = "Hi my friend " + name + "!<br />Congratulation!!! Your chosen one is <b>" + names.get(i)
                    + "</b>!!!<br />Don't spend too much money for others, except Viktor! ;)<br />Bye";
            SendMail.sendMail(fromEmail, fromPassword, email, title, htmlBody);
            i++;
        }
    }
}
