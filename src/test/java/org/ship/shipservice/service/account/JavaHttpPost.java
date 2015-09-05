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
        String url = "http://localhost/shipService/api/v1/user/code";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");

        String urlParameters = "phone=18516293301";
        URLEncoder.encode(urlParameters,"UTF-8");
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
            result += "" + line;  
        }  
        System.out.println("测试个人中心功能 : " + result);

    }
}
