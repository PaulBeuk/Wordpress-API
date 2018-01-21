package org.beuk.wordpress.api.entity;

import com.fasterxml.jackson.annotation.*;

public class PostMeta {

	public class PostMetaLinks {

		public String self;
		public String author;
		public String collection;
		public String replies;
		@JsonProperty(value = "version-history")
		public String versionHistory;
	}

	public PostMetaLinks links;

}
