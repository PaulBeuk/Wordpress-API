package org.beuk.wordpress.api.logic;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.configuration.*;
import org.beuk.wordpress.api.client.*;
import org.beuk.wordpress.api.entity.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class WordpressController {

	public static void main(String[] args) {

		final WordpressController userController = new WordpressController();
		try {
			// final User user = userController.getUser(userId);
			// System.out.println("user: " + user.username);
			// userController.getPostByCat(45);
			// System.out.println("post: " + posts.size());
			// for (final Post post : posts) {
			// System.out.println(post.terms.category.get(0).ID + ": " +
			// post.terms.category.get(0).name + " title: " + post.title);
			// }
			// final Post post = posts.get(0);
			// post.content = "iets anders";
			// final String postToJson = userController.postToJson(post);
			// System.out.println("postToJson: " + postToJson);
			// final Category category = userController.getCategory(45);
			// System.out.println(category.ID + " cat: " + category.name);
			// final Page page =
			// userController.getPage("2014-a-paulvanbeukering");
			// System.out.println(page.ID + " cat: " + page.slug + " content: "
			// + page.content);
			//
			// userController.updatePage(page, "zo dat " + new
			// Date().toString());
			userController.createPage("2014 a Helmer Wieringa", "2014-a-Helmer-Wieringa", "result of Helmer");

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public ClientController clientController;

	public WPObjectMapper wpObjectMapper;
	String wpURL = "http://localhost/wordpress/wp-json/";
	String username = "notset";
	String password = "notset";
	String userURL;
	String postURL;
	String taxonomyURL;
	String categoryURL;
	String pageURL;

	public WordpressController() {

		clientController = new ClientController(username, password);
		wpObjectMapper = new WPObjectMapper();
	}

	public WordpressController(Configuration config) {

		wpURL = config.getString("wp.wp-json.host");
		username = config.getString("wp.username");
		password = config.getString("wp.password");
		userURL = wpURL + "users";
		postURL = wpURL + "posts";
		taxonomyURL = wpURL + "taxonomies";
		categoryURL = taxonomyURL + "/category/terms";
		pageURL = wpURL + "pages";
		clientController = new ClientController(username, password);

		wpObjectMapper = new WPObjectMapper();
	}

	public void savePage(String title, String slug, String content) throws IOException {

		Page page = null;
		try {
			page = getPage(slug);
		} catch (final IOException e) {
			// System.out.println("getPage exception: " + e.getMessage());
		}

		if (page == null) {
			System.out.println("page not found, create new: " + slug);
			createPage(title, slug, content);
		} else {
			System.out.println("page found, update: " + slug);
			page.content = content;
			updatePage(page);
		}
	}

	void createPage(String title, String slug, String content) throws IOException {

		final Page page = new Page();
		page.title = title;
		page.slug = slug;
		page.content = content;
		page.type = "page";
		page.status = "publish";

		final String createPageContentInsert = wpObjectMapper.createPageContentInsert(page);
		// System.out.println("new page: " + createPageContentInsert);
		clientController.saveContent(pageURL, createPageContentInsert);
	}

	Category getCategory(int catId) throws MalformedURLException, IOException {

		final String response = clientController.getResponse(categoryURL + "/" + catId);

		return wpObjectMapper.toCategory(response);

	}

	Page getPage(String slug) throws IOException {

		final String response = clientController.getResponse(pageURL + "/" + slug);
		// System.out.println("page: " + response);
		return wpObjectMapper.toPage(response);
	}

	Post getPost(int postId) throws MalformedURLException, IOException {

		final String response = clientController.getResponse(postURL + "/" + postId);
		return wpObjectMapper.toPost(response);
	}

	List<Post> getPostByCat(int catId) throws MalformedURLException, IOException {

		final String response = clientController.getResponse(postURL + "?filter[cat]=" + catId);
		return wpObjectMapper.toPosts(response);
	}

	String getPosts() throws MalformedURLException, IOException {

		return clientController.getResponse(postURL);
	}

	User getUser(int userId) throws MalformedURLException, IOException {

		final String response = clientController.getResponse(userURL + "/" + userId);
		return wpObjectMapper.toUser(response);
	}

	String postToJson(Post post) throws JsonGenerationException, JsonMappingException, IOException {

		return wpObjectMapper.fromPost(post);
	}

	void updatePage(Page page) throws IOException {

		final String pageContent = wpObjectMapper.createPageContentUpdate(page);
		// System.out.println("pageCotent: " + pageContent);
		clientController.saveContent(page.meta.links.self, pageContent);

	}
}
