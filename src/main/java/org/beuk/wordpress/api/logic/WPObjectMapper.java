package org.beuk.wordpress.api.logic;

import java.io.*;
import java.util.*;

import org.beuk.wordpress.api.entity.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class WPObjectMapper {

	ObjectMapper mapper;

	public WPObjectMapper() {

		mapper = new ObjectMapper();
	}

	public String createPageContentInsert(Page page) throws IOException {

		final JsonFactory jfactory = new JsonFactory();
		final Writer out = new StringWriter();
		try (final JsonGenerator generator = jfactory.createGenerator(out)) {
			generator.writeStartObject();
			generator.writeObjectField("title", page.title);
			generator.writeObjectField("parent", 0);
			// generator.writeObjectField("author", page.author);
			generator.writeObjectField("type", page.type);
			generator.writeObjectField("status", page.status);
			generator.writeObjectField("slug", page.slug);
			generator.writeObjectField("content_raw", page.content);
			generator.writeObjectField("excerpt", page.excerpt);
			generator.writeObjectField("comment_status", "closed");

			generator.writeEndObject();
		}
		return out.toString();
	}

	public String createPageContentUpdate(Page page) throws IOException {

		final JsonFactory jfactory = new JsonFactory();
		final Writer out = new StringWriter();
		try (final JsonGenerator generator = jfactory.createGenerator(out)) {
			generator.writeStartObject();
			generator.writeObjectField("content_raw", page.content);
			generator.writeEndObject();
		}
		return out.toString();
	}

	public String fromPost(Post post) throws JsonGenerationException, JsonMappingException, IOException {

		final JsonFactory jfactory = new JsonFactory();

		final Writer out = new StringWriter();

		final JsonGenerator generator = jfactory.createGenerator(out);
		// mapper.writeValue(new File("hol.json"), post);
		mapper.writeValue(generator, post);
		return out.toString();
	}

	public Category toCategory(String response) throws JsonParseException, JsonMappingException, IOException {

		return mapper.readValue(response, Category.class);
	}

	public Page toPage(String response) throws JsonParseException, JsonMappingException, IOException {

		return mapper.readValue(response, Page.class);
	}

	public Post toPost(String response) throws JsonParseException, JsonMappingException, IOException {

		return mapper.readValue(response, Post.class);
	}

	public List<Post> toPosts(String response) throws JsonParseException, IOException {

		final JsonFactory f = new JsonFactory();
		final JsonParser jp = f.createParser(response);
		jp.nextToken();
		final List<Post> posts = new ArrayList<>();
		while (jp.nextToken() == JsonToken.START_OBJECT) {
			posts.add(mapper.readValue(jp, Post.class));
		}
		return posts;
	}

	public User toUser(String response) throws JsonParseException, JsonMappingException, IOException {

		return mapper.readValue(response, User.class);
	}
}
