package com.printz.guano.shoppingassistant;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RestClient {

    private static final String LOG_TAG = RestClient.class.getSimpleName();

    /**
     * REST API routes
     */
    public static final String WARES = "wares";
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String INVITATIONS = "invitations";
    private String route;

    /**
     * Server definition
     */
    private static final String server = "http://192.168.57.1";

    /**
     * Request body, url params and reponse body
     */
    private JSONObject requestBody;
    private HashMap<String, String> requestParams;

    private JSONObject responseBody;

    public RestClient(String route) {
        this();
        this.route = route;
    }

    public RestClient() {
        requestBody = new JSONObject();
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void addBodyParam(String key, String value) {
        try {
            requestBody.put(key, value);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Error parsing post data to send.");
            e.printStackTrace();
        }
    }

    public void addRequestParam(String key, String value) {
        requestParams.put(key, value);
    }

    public void resetRequest() {
        requestParams = new HashMap<>();
        requestBody = new JSONObject();
    }

    public JSONObject getResponseBody() {
        return responseBody;
    }

    /**
     * Performs Http Post method call
     *
     * @return Http reponse status code
     * @throws IOException
     */
    public int executePost() throws IOException {
        HttpURLConnection connection = null;

        try {
            try {
                URL url = new URL(server + "/" + route);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();

                OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                streamWriter.write(requestBody.toString());
                streamWriter.flush();
                streamWriter.close();

                String data = readResponseBody(connection.getInputStream());

                responseBody = new JSONObject(data);

            } finally {
                if (connection != null) {
                    int responseCode = connection.getResponseCode();
                    connection.disconnect();
                    return responseCode;
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Error parsing json");
        }

        return 0;
    }

    /**
     * Performs Http Get method call
     *
     * @return Http response status code
     * @throws IOException
     */
    public int executeGet() throws IOException {
        HttpURLConnection connection = null;

        try {
            try {
                URL url = new URL(server + "/" + route);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();

                OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                streamWriter.write(getRequestParamString(requestParams));
                streamWriter.flush();
                streamWriter.close();

                String data = readResponseBody(connection.getInputStream());

                responseBody = new JSONObject(data);

            } finally {
                if (connection != null) {
                    int responseCode = connection.getResponseCode();
                    connection.disconnect();
                    return responseCode;
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Error parsing json");
        }

        return 0;
    }

    public int executePut() throws IOException {
        HttpURLConnection connection = null;

        try {
            try {
                URL url = new URL(server + "/" + route);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();

                OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                streamWriter.write(requestBody.toString());
                streamWriter.flush();
                streamWriter.close();

                String data = readResponseBody(connection.getInputStream());

                responseBody = new JSONObject(data);

            } finally {
                if (connection != null) {
                    int responseCode = connection.getResponseCode();
                    connection.disconnect();
                    return responseCode;
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Error parsing json");
        }

        return 0;
    }

    public int executeDelete(String id) throws IOException {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(server + "/" + route + "/" + id);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");

            connection.connect();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return connection.getResponseCode();
    }

    private String getRequestParamString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /**
     * Reads the http body data
     *
     * @param inputStream Connection input stream to read from
     * @return String the data read
     * @throws IOException
     */
    private String readResponseBody(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(streamReader);

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        bufferedReader.close();
        return stringBuilder.toString();
    }
}
