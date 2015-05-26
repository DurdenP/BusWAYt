package app.durdenp.com.buswayt;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Marco on 25/05/2015.
 */
public class Temp {
    public static void main(String args[]){

        String url = "http://www.amt.ct.it/MappaLinee/leggifermate.php?linee=BRT1%3B";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", "Mozilla/5.0");

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = null;
        try {
            rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));


        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
