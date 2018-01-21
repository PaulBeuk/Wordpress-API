package org.beuk.wordpress.api.client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientController {

	public String username;
	public String password;

	public ClientController() {

	}

	public ClientController(String username, String password) {

		super();
		this.username = username;
		this.password = password;
	}

	public String getResponse(String url) throws MalformedURLException, IOException {

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		final String encoded = encodeString(username + ":" + password);

		connection.setRequestProperty("Authorization", "Basic " + encoded);
		connection.setReadTimeout(5000);
		connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
		connection.addRequestProperty("User-Agent", "Mozilla");
		connection.addRequestProperty("Referer", "google.com");
		boolean redirect = false;

		final int status = connection.getResponseCode();
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
		}

		if (redirect) {
			final String newUrl = connection.getHeaderField("Location");
			final String cookies = connection.getHeaderField("Set-Cookie");
			connection = (HttpURLConnection) new URL(newUrl).openConnection();
			connection.setRequestProperty("Cookie", cookies);
			connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			connection.addRequestProperty("User-Agent", "Mozilla");
			connection.addRequestProperty("Referer", "google.com");
		}

		final StringBuffer html = new StringBuffer();
		try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				html.append(inputLine).append('\n');
			}
		}
		connection.disconnect();
		return html.toString();

	}

	public String saveContent(String url, String pageContent) throws IOException {

		System.out.println("saving page: " + url);
		System.out.println("user: " + username);
		System.out.println("password: " + password);
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		// final String encoded = new String(Base64.encode(username + ":" +
		// password));

		final String encoded = new String(encodeString(username + ":" + password));

		connection.setRequestProperty("Authorization", "Basic " + encoded);
		connection.setReadTimeout(5000);
		connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
		connection.addRequestProperty("User-Agent", "Mozilla");
		connection.addRequestProperty("Referer", "google.com");

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestMethod("POST");

		boolean redirect = false;
		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
		wr.write(pageContent);
		wr.flush();

		final int status = connection.getResponseCode();
		System.out.println("status: " + status);
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
		}

		if (redirect) {
			final String newUrl = connection.getHeaderField("Location");
			final String cookies = connection.getHeaderField("Set-Cookie");
			connection = (HttpURLConnection) new URL(newUrl).openConnection();
			connection.setRequestProperty("Cookie", cookies);
			connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			connection.addRequestProperty("User-Agent", "Mozilla");
			connection.addRequestProperty("Referer", "google.com");

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestMethod("POST");
			redirect = false;
			wr = new OutputStreamWriter(connection.getOutputStream());
			wr.write(pageContent);
			wr.flush();

		}

		final StringBuffer html = new StringBuffer();
		try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				html.append(inputLine).append('\n');
			}
		}
		connection.disconnect();
		return html.toString();
	}

	private String encodeString(String value) {

		return new String(Base64.getEncoder().encode(value.getBytes()));
	}
}
