package io.github.izzyleung;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

final class Network {

  private Network() {

  }

  static InputStream openInputStream(String address) throws IOException {
    URL url = new URL(address);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("User-Agent", "Mozilla/5.0");

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      return connection.getInputStream();
    } else {
      throw new IOException("Network Error - response code: " + connection.getResponseCode());
    }
  }
}
