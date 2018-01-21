package org.beuk.wordpress.api.entity;

import java.util.*;

public class Page {

	public long ID;
	public String title;
	public String status;
	public String type;
	public User author;

	public String content;
	public long parent;
	public String link;
	public Date date;
	public Date modified;
	public String format;
	public String slug;
	public String guid;
	public String excerpt;
	public long menu_order;
	public String comment_status;
	public String ping_status;
	public boolean sticky;
	public String date_tz;
	public Date date_gmt;
	public String modified_tz;
	public Date modified_gmt;
	public String password;
	public PostMeta meta;
	public String featured_image;
	public List<String> terms;
}
