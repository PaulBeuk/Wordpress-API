package org.beuk.wordpress.api.client;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.configuration.*;
import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.*;
import org.beuk.wordpress.api.entity.*;
import org.slf4j.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class XMLRPCController {

	static ObjectMapper mapper = new ObjectMapper();
	static String username = "notset";

	static String password = "notset";

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

		final XMLRPCController controller = new XMLRPCController();
		try {
			// controller.test();
			// controller.getPages();
			// final int pageId = controller.getPageByTitle(testPageTitle);
			// if (pageId > 0) {
			// controller.editPage(pageId);
			// }
			final String[] cats = new String[1];
			cats[0] = "Clubblad";
			controller.savePage("hello xmlrpc world", "hello xmlrpc world".replace(' ', '-'), "en dan is dit de content", -1, -1, cats);

			// controller.getPage(4);

			// controller.savePage();

		} catch (final XmlRpcException | MalformedURLException e) {
			e.printStackTrace();
		}

	}

	Logger log = LoggerFactory.getLogger(XMLRPCController.class);

	XmlRpcClient xmlRpcClient;
	String serverURL = "http://localhost/wordpress/xmlrpc.php";
	List<XMLPageSummary> allPages;
	List<XMLUserSummary> allUsers;

	public XMLRPCController() {

	}

	public XMLRPCController(Configuration config) throws XmlRpcException, JsonParseException, JsonMappingException, IOException {
		serverURL = config.getString("wp.xmlrpc.host");
		username = config.getString("wp.username");
		password = config.getString("wp.password");
		getXmlRpcClient();
		getPages();
		retrieveAllUsers();
	}

	public List<XMLPageSummary> getAllPages() {

		return allPages;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPage(int pageId) throws XmlRpcException, MalformedURLException {

		// final XmlRpcClient xmlRpcClient = createXmlRpcClient();
		getXmlRpcClient();
		final List<String> pParams = new ArrayList<>();
		pParams.add("1");
		pParams.add(pageId + "");
		pParams.add(username);
		pParams.add(password);
		// pParams.add(pageId + "");
		final String pMethodName = "wp.getPage";

		return (Map<String, Object>) xmlRpcClient.execute(pMethodName, pParams);
	}

	public XMLPageSummary getPageByTitle(String title) {

		final Optional<XMLPageSummary> optional = allPages.stream().filter(p -> p.pageTitle.equals(title)).findFirst();
		if (optional.isPresent())
			return optional.get();
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPost(int pageId) throws XmlRpcException, MalformedURLException {

		// final XmlRpcClient xmlRpcClient = createXmlRpcClient();
		getXmlRpcClient();
		final List<String> pParams = new ArrayList<>();
		pParams.add("1");
		pParams.add(username);
		pParams.add(password);
		pParams.add(pageId + "");
		final String pMethodName = "wp.getPost";

		return (Map<String, Object>) xmlRpcClient.execute(pMethodName, pParams);
	}

	public int getUserByName(String name) throws JsonParseException, JsonMappingException, XmlRpcException, IOException {

		for (final XMLUserSummary userSummary : allUsers) {
			log.info(userSummary.username + " " + name);
			if (userSummary.username.equalsIgnoreCase(name)) {
				return userSummary.userId;
			}
		}
		log.info("no user found by name: " + name);
		return -1;
	}

	@SuppressWarnings("unchecked")
	public void retrieveAllPages() throws XmlRpcException {

		final List<Object> pParams = new ArrayList<>();
		pParams.add("1");
		pParams.add(username);
		pParams.add(password);
		final Map<String, String> mm = new HashMap<>();
		mm.put("post_type", "page");
		mm.put("number", "5");
		mm.put("number", "5");
		pParams.add(mm);
		pParams.add("post_id");
		pParams.add("post_title");
		// pParams.add("post_slug");
		final Object[] o = (Object[]) xmlRpcClient.execute("wp.getPosts", pParams);
		log.info("o size: " + o.length);
		allPages = new ArrayList<>(o.length);
		for (final Object element : o) {
			log.info(element.toString());
			allPages.add(parsePageSummary((Map<String, Object>) element));
		}
		/*
		 * for (final XMLPageSummary page : allPages) { log.info(page.page_id + " pt: " + page.page_title + " ps: " +
		 * page.page_slug); }
		 */
		log.info("retrieved pages found: " + allPages.size());
	}

	public void savePage(String title, String slug, String content, int userId, int parentId, String[] categories) throws XmlRpcException, JsonParseException, JsonMappingException, IOException {

		final XMLPageSummary pageSummary = getPageByTitle(title);
		if (pageSummary != null) {
			editPage(pageSummary.pageId, title, content, userId, parentId, categories);
		} else {
			newPage(title, slug, content, userId, parentId, categories);
		}
	}

	XmlRpcClient createXmlRpcClient() throws MalformedURLException {

		xmlRpcClient = new XmlRpcClient();
		final XmlRpcClientConfigImpl pConfig = new XmlRpcClientConfigImpl();
		log.info("connecting to: " + serverURL);
		final URL pURL = new URL(serverURL);
		pConfig.setServerURL(pURL);
		xmlRpcClient.setConfig(pConfig);
		return xmlRpcClient;
	}

	void editPage(int pageId, String title, String content, int userId, int parentId, String[] categories) throws MalformedURLException, XmlRpcException {

		final List<Object> pParams = new ArrayList<>();
		pParams.add("1");
		pParams.add(pageId);
		pParams.add(username);
		pParams.add(password);
		final Map<String, Object> pageOut = new HashMap<>();
		pageOut.put("description", content);
		if (userId > -1) {
			pageOut.put("wp_author_id", userId + "");
		}
		if (parentId > -1) {
			pageOut.put("wp_page_parent_id", parentId + "");
		}
		if (categories != null && categories.length > 0) {
			// log.info("add cats");
			pageOut.put("categories", categories);
		}
		// final List<String> cats = Arrays.asList("Interne");
		// pageOut.put("categories", cats);
		pParams.add(pageOut);
		pParams.add(true);
		final String pMethodName = "wp.editPage";
		xmlRpcClient.execute(pMethodName, pParams);
		log.info("editPage: " + pageId + " title: " + title);
	}

	@SuppressWarnings("unchecked")
	void getPages() throws XmlRpcException, JsonParseException, JsonMappingException, IOException {

		getXmlRpcClient();
		final List<String> pParams = new ArrayList<>();
		pParams.add("1");
		pParams.add(username);
		pParams.add(password);
		final String pMethodName = "wp.getPageList";
		final Object[] o = (Object[]) xmlRpcClient.execute(pMethodName, pParams);
		log.info("page summaries found: " + o.length);
		allPages = new ArrayList<>(o.length);
		for (final Object element : o) {
			allPages.add(parsePageSummary((Map<String, Object>) element));
		}
	}

	void newPage(String title, String slug, String content, int userId, int parentId, String[] categories) throws XmlRpcException, JsonParseException, JsonMappingException, IOException {

		// final XmlRpcClient xmlRpcClient = createXmlRpcClient();
		getXmlRpcClient();
		final List<Object> pParams = new ArrayList<>();
		pParams.add("1");
		pParams.add(username);
		pParams.add(password);

		final Map<String, Object> pageOut = new HashMap<>();
		pageOut.put("title", title);
		pageOut.put("wp_slug", slug);
		if (userId > -1) {
			pageOut.put("wp_author_id", userId + "");
		}
		if (parentId > -1) {
			pageOut.put("wp_page_parent_id", parentId + "");
		}
		if (categories != null && categories.length > 0) {
			pageOut.put("categories", categories);
		}
		pageOut.put("description", content);

		pParams.add(pageOut);
		pParams.add(true);
		final String pMethodName = "wp.newPage";
		xmlRpcClient.execute(pMethodName, pParams);
		log.info(pMethodName + " title: " + title);
	}

	XMLPageSummary parsePageSummary(Map<String, Object> m) {

		final XMLPageSummary x = new XMLPageSummary();
		// x.date_created_gmt = (Date) m.get("date_created_gmt");
		// x.dateCreated = (Date) m.get("dateCreated");

		x.pageId = Integer.parseInt((String) m.get("page_id"));
		x.pageTitle = (String) m.get("page_title");
		x.pageParentId = Integer.parseInt((String) m.get("page_parent_id"));
		// log.info("--- " + x.page_title);
		return x;
	}

	XMLUserSummary parseUserSummary(Map<String, Object> m) {

		final XMLUserSummary x = new XMLUserSummary();
		x.userId = Integer.parseInt((String) m.get("user_id"));
		x.username = (String) m.get("username");
		return x;
	}

	@SuppressWarnings("unchecked")
	void retrieveAllUsers() throws XmlRpcException {

		final List<Object> pParams = new ArrayList<>();
		pParams.add("1");
		pParams.add(username);
		pParams.add(password);
		final Map<String, String> mm = new HashMap<>();
		mm.put("number", "500");

		pParams.add(mm);
		final Object[] o = (Object[]) xmlRpcClient.execute("wp.getUsers", pParams);
		allUsers = new ArrayList<>(o.length);
		for (final Object element : o) {
			allUsers.add(parseUserSummary((Map<String, Object>) element));
		}
		log.info("users found: " + allUsers.size());

	}

	void test() throws XmlRpcException, MalformedURLException {

		// final XmlRpcClient xmlRpcClient = createXmlRpcClient();
		getXmlRpcClient();
		final List<String> pParams = new ArrayList<>();
		pParams.add("1");
		pParams.add(username);
		pParams.add(password);
		pParams.add("7");
		final String pMethodName = "wp.getUser";
		log.info("username: " + username);
		log.info("password: " + password);
		final Object execute = xmlRpcClient.execute(pMethodName, pParams);
		log.info("executed: " + execute);
	}

	private void getXmlRpcClient() throws MalformedURLException {

		if (xmlRpcClient == null) {
			createXmlRpcClient();
		}
	}

}
