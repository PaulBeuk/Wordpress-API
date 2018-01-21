package org.beuk.wordpress.api.entity;

public class Category {

	public class ParentCategory {

		public int ID;
		public String name;
		public String slug;
		public String description;
		public int parent;
		public int count;
		public String link;
		public CategoryMeta meta;
	}

	public int ID;
	public String name;
	public String slug;
	public String description;
	public ParentCategory parent;
	public int count;
	public String link;
	public CategoryMeta meta;
}
