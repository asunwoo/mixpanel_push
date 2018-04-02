package com.mixpanel.events;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

import java.util.Base64;

/**
 * Simple class used to construct a predefined event payload to Mixpanel using a given token
 */
public class PushEvent {

    //URL to send events to
    private static String URL_STRING = "http://api.mixpanel.com/track/?data=";


    /**
     * Method that constructs and uses an http connection to deliver
     * the specified JSON payload to mixpanel
     * @param token
     * Mixpanel token indicating the Mixpanel project to associate the event to
     * @return
     * String indicating success or failyer
     */
    public String postEvent(String token){

        HttpURLConnection http = null;
        String response = null;

        String jsonEvent = String.format(PAYLOAD, token);

        //Base64 encoding per mixpanel requirements
        byte[] encodedBytes = Base64.getEncoder().encode(jsonEvent.getBytes());

        String dataString = new String(encodedBytes);
        String requestString = URL_STRING + dataString;

        try {
            URL url = new URL(requestString);
            URLConnection con = url.openConnection();

            http = (HttpURLConnection)con;
            http.setRequestMethod("GET");
            http.connect();

            BufferedReader br = null;
            //Check response for success
            //if successful read for 0 or 1 response code
            //if not read the error stream
            if (http.getResponseCode() >= 200  && http.getResponseCode() <= 299) {
                br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            response = sb.toString();

        } catch(MalformedURLException mue){
            mue.printStackTrace();
        } catch(ProtocolException pe){
            pe.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {
            http.disconnect();
        }

        //Construct the response
        if(response.equals("1")){
            return "Event sent to Mixpanel successfully: " + response;
        } else {
            return "Event send failure: " + response;
        }

    }

    public static void main(String[] args) {
        PushEvent pe = new PushEvent();
        String response = pe.postEvent(args[0]);

        System.out.println(response);
    }

    private static String PAYLOAD =
             "{\n" +
             "  \"userId\": \"user1\",\n" +
             "  \"event\": \"Product Viewed\",\n" +
             "  \"properties\": {\n" +
             "    \"product_id\": \"pr_507f1f77bcf86cd799439011\",\n" +
             "    \"sku\": \"G-32\",\n" +
             "    \"category\": \"Games\",\n" +
             "    \"name\": \"Monopoly: 3rd Edition\",\n" +
             "    \"brand\": \"Hasbro\",\n" +
             "    \"variant\": \"200 pieces\",\n" +
             "    \"price\": 18.99,\n" +
             "    \"quantity\": 1,\n" +
             "    \"token\": \"%s\"\n" +
             "  }\n" +
             "}";
}
