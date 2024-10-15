package de.axa.robin.vertragsverwaltung.storage.Checker;

import de.axa.robin.vertragsverwaltung.user_interaction.Output;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class AddressValidator {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?format=json&q=";

    public static boolean validateAddress(String street, String houseNumber, String plz, String place) {
        try {
            String query = URLEncoder.encode(street + " " + houseNumber + ", " + plz + " " + place, StandardCharsets.UTF_8);
            String url = NOMINATIM_URL + query;

            System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");

            System.setProperty("http.proxyHost", "localhost");
            System.setProperty("http.proxyPort", "3128");
            System.setProperty("https.proxyHost", "localhost");
            System.setProperty("https.proxyPort", "3128");

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP) {
                String newUrl = conn.getHeaderField("Location");
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                status = conn.getResponseCode();
            }

            if (status != HttpURLConnection.HTTP_OK) {
                Output.errorvalidate("Received HTTP status code " + status);
                return true;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JsonReader jsonReader = Json.createReader(new StringReader(content.toString()));
            JsonArray jsonArray = jsonReader.readArray();
            jsonReader.close();

            if (!jsonArray.isEmpty()) {
                JsonObject address = jsonArray.getJsonObject(0);
                String displayName = address.getString("display_name").toLowerCase();

                return displayName.contains(street.toLowerCase()) &&
                        displayName.contains(houseNumber.toLowerCase()) &&
                        displayName.contains(plz.toLowerCase()) &&
                        displayName.contains(place.toLowerCase());
            }
            else {
                Output.errorvalidate("Adresse existiert nicht!");
            }

        } catch (ConnectException | SocketTimeoutException e) {
            Output.connection(e.getMessage());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}