package com.pick.from.hat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;

public class PickFromHat {
    private List<Person> persons = new ArrayList<Person>();
    private List<Person> choosedPersons = new ArrayList<Person>();
    private String namesJsonFile = "/settings.json";
    private String fromEmail;
    private String fromPassword;

    public static void main(String[] args) {
        PickFromHat pickFromHat = new PickFromHat();
        pickFromHat.startPickFromHat();
    }

    public void startPickFromHat() {
        loadNamesWithMailAddress();
        suffleNames();
        sendMails();
    }

    private void loadNamesWithMailAddress() {
        JSONParser parser = new JSONParser();
        try {
            InputStream is = PickFromHat.class.getResourceAsStream(namesJsonFile);
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(is, "UTF-8"));
            fromEmail = (String) jsonObject.get("fromEmail");
            fromPassword = (String) jsonObject.get("fromPassword");
            JSONArray mail2names = (JSONArray) jsonObject.get("names");
            for (Object obj : mail2names) {
                JSONObject mail2name = (JSONObject) obj;
                String email = (String) mail2name.get("email");
                String name = (String) mail2name.get("name");

                Person person = new Person(name, email);
                persons.add(person);
                choosedPersons.add(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void suffleNames() {
        Collections.shuffle(choosedPersons);

        boolean noSelfPicking;
        do {
            int i = 0;
            noSelfPicking = true;
            for (Person person : persons) {
                Person choosedPerson = choosedPersons.get(i);
                if (person.equals(choosedPerson)) {
                    noSelfPicking = false;
                    Collections.shuffle(choosedPersons);
                    break;
                }
                i++;
            }
        } while (!noSelfPicking);
    }

    private void sendMails() {
        int i = 0;
        for (Person person : persons) {
            String title = "You picked a name from a hat";
            String htmlBody = "Hi my friend " + person.getName() + "!<br />Congratulation!!! Your chosen one is <b>" + choosedPersons.get(i).getName()
                    + "</b>, his/her mail address is " + choosedPersons.get(i).getEmail() + "!!! <br />Don't spend too much money for others, except Viktor! ;)<br />Bye";
            SendMail.sendMail(fromEmail, fromPassword, person.getEmail(), title, htmlBody);
            i++;
        }
    }
}
