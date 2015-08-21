package org.ship.shipservice.service.account;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class JavaHttpPost {
	private final String USER_AGENT = "Mozilla/5.0";
	public static void main(String[] args) throws Exception {

		JavaHttpPost http = new JavaHttpPost();

        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();

    }
	// HTTP POST request
    private void sendPost() throws Exception {
    	String result = "";  
        String url = "http://43.254.52.16/shipService/api/v1/register";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");

        String urlParameters = "phone=12375&password=132245&username=12&shipname=32&shipno=87";
        URLEncoder.encode(urlParameters,"UTF-8");
        System.out.println(urlParameters);
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(  
                new InputStreamReader(con.getInputStream()));  
        String line;  
        while ((line = in.readLine()) != null) {  
            result += "/n" + line;  
        }  
        System.out.println("Response Code : " + result);

    }
}
