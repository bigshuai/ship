package org.ship.shipservice.service.account;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class JavaHttpPost {
	private final String USER_AGENT = "Mozilla/5.0";
	public static void main(String[] args) throws Exception {

		JavaHttpPost http = new JavaHttpPost();

        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();

    }
	// HTTP POST request
    private void sendPost() throws Exception {

        String url = "http://localhost/shipService/login";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "username=admin&password=admin";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        String responseCode = (String)con.getContent();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

    }
}
