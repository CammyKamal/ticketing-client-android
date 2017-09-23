package com.chandigarhadmin.service;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.chandigarhadmin.utils.User;
import com.chandigarhadmin.utils.WsseToken;

public class JSONParser {
    // Method type
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    public final static int DELETE = 4;

    Context context;

    public JSONParser(Context context) {
        super();
        this.context = context;
    }

    /**
     * Get response string
     *
     * @param url
     * @param method
     * @return
     */
    public String getResponseString(String url, int method, String basicAuth,User user) {
        return makeServiceCall(url, method, null, basicAuth,user);
    }

    /**
     * Get response string
     *
     * @param url
     * @param method
     * @param params
     * @return
     */
    public String getResponseString(String url, int method,
                                    JSONObject params, String basicAuth,User user) {
        return makeServiceCall(url, method, params, basicAuth,user);
    }

    /**
     * Get JSON
     *
     * @param url
     * @param method
     * @return
     */
    public JSONObject getJSONFromUrl(String url, int method, String basicAuth,User user) {
        return getJSONFromUrl(url, method, null, basicAuth,user);
    }

    /**
     * Get JSON
     *
     * @param url
     * @param method
     * @return
     */
    public JSONObject getJSONFromUrl(String url, int method,
                                     JSONObject params, String basicAuth,User user) {

        JSONObject json = null;
        // try parse the string to a JSON object
        try {
            String jsonString = makeServiceCall(url, method, params, basicAuth,user);
            if (jsonString != null) {
                json = new JSONObject(jsonString);
            }
            return json;
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            return json;
        }

    }

    /**
     * Get JSONArray
     *
     * @param url
     * @param method
     * @return
     */
    public JSONArray getJSONArrayFromUrl(String url, int method,
                                         String basicAuth) {
        return getJSONArrayFromUrl(url, method, null, basicAuth);
    }

    /**
     * Get JSONArray
     *
     * @param url
     * @param method
     * @return
     */
    public JSONArray getJSONArrayFromUrl(String url, int method,
                                         JSONObject params, String basicAuth) {

        JSONArray jsonArray = null;
        // try parse the string to a JSON object
        try {
            String jsonString = makeServiceCall(url, method, params, basicAuth,null);
            if (jsonString != null) {
                Log.e("JsonString", jsonString);
                jsonArray = new JSONArray(jsonString);
            }
            return jsonArray;
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            return jsonArray;
        }

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    private String makeServiceCall(String address, int method,JSONObject params, String
            basicAuth,User user) {

        String result = null;


        // http client

        URL url = null;
        HttpURLConnection urlConnection = null;
        OutputStream out;
        InputStream in;
        try {

            url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setInstanceFollowRedirects(true);


            // Checking http request method type
            if (method == POST) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
            } else if (method == GET) {
                urlConnection.setRequestMethod("GET");
            } else if (method == PUT) {
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
            } else if (method == DELETE) {
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setDoOutput(true);
            }
            //urlConnection.setDoOutput(true);
            if(null!=user) {
                WsseToken token = new WsseToken(user);
                urlConnection.setRequestProperty(WsseToken.HEADER_AUTHORIZATION, token .getAuthorizationHeader());
                urlConnection.setRequestProperty(WsseToken.HEADER_WSSE, token.getWsseHeader());
            }



            urlConnection.setRequestProperty( "Content-Type", "application/json");
            //urlConnection.setRequestProperty( "Content-Type", "x-www-form-urlencoded");
            urlConnection.setRequestProperty( "charset", "utf-8");
            //urlConnection.setRequestProperty("Content-Type","application/json");
            if (params != null) {
               /* Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("SUBJECT", params.get("SUBJECT").toString())
                        .appendQueryParameter("subject",params.get("subject").toString())
                        .appendQueryParameter("description", params.get("description").toString())
                        .appendQueryParameter("status", params.get("status").toString())
                        .appendQueryParameter("priority", params.get("priority").toString())
                        .appendQueryParameter("source", params.get("source").toString())
                        .appendQueryParameter("reporter", params.get("reporter").toString());
                String query = builder.build().getEncodedQuery();
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();*/
                OutputStreamWriter os = new   OutputStreamWriter(urlConnection.getOutputStream());
                os.write(params.toString());
                os.close();
               /* byte[] outData = params.toString().getBytes(Charset.forName("UTF-8"));
                urlConnection.setRequestProperty( "Content-Length", Integer.toString( outData.length));
                out = new BufferedOutputStream(
                        urlConnection.getOutputStream());
                out.write(outData);
                out.close();*/

            }
            StringBuilder builder = new StringBuilder();
            try {
                BufferedReader in1 = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                String line;
                while ((line = in1.readLine()) != null) {
                    builder.append(line); // + "\r\n"(no need, json has no line breaks!)
                }
                in1.close();
            } catch(Exception e) {

            }
            System.out.println("JSON: " + builder.toString());

            in = new BufferedInputStream(urlConnection.getInputStream());
            result = inputStreamToString(in);
            Log.e("ServerResponse", result);
        } catch (IOException e) {
            Log.e("MakeServiceCall", "Error : " + e.getMessage());
        } finally {
            urlConnection.disconnect();
        }

        return result;
    }

    private static String inputStreamToString(InputStream in) {
        String result = "";
        if (in == null) {
            return result;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            result = out.toString();
            reader.close();

            return result;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("InputStream", "Error : " + e.toString());
            return result;
        }

    }
}
