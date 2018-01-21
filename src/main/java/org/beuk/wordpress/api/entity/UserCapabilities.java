package org.beuk.wordpress.api.entity;

import com.fasterxml.jackson.annotation.*;

public class UserCapabilities {

	public boolean switch_themes;
	public boolean edit_themes;
	public boolean activate_plugins;
	public boolean edit_plugins;
	public boolean edit_users;
	public boolean edit_files;
	public boolean manage_options;
	public boolean moderate_comments;
	public boolean manage_categories;
	public boolean manage_links;
	public boolean upload_files;

	@JsonProperty(value = "import")
	public boolean importer;

	public boolean unfiltered_html;
	public boolean edit_posts;
	public boolean edit_others_posts;
	public boolean edit_published_posts;
	public boolean publish_posts;
	public boolean edit_pages;
	public boolean read;
	public boolean level_10;
	public boolean level_9;
	public boolean level_8;
	public boolean level_7;
	public boolean level_6;
	public boolean level_5;
	public boolean level_4;
	public boolean level_3;
	public boolean level_2;
	public boolean level_1;
	public boolean level_0;
	public boolean edit_others_pages;
	public boolean edit_published_pages;
	public boolean publish_pages;
	public boolean delete_pages;
	public boolean delete_others_pages;
	public boolean delete_published_pages;
	public boolean delete_posts;
	public boolean delete_others_posts;
	public boolean delete_published_posts;
	public boolean delete_private_posts;
	public boolean edit_private_posts;
	public boolean read_private_posts;
	public boolean delete_private_pages;
	public boolean edit_private_pages;
	public boolean read_private_pages;
	public boolean delete_users;
	public boolean create_users;
	public boolean unfiltered_upload;
	public boolean edit_dashboard;
	public boolean update_plugins;
	public boolean delete_plugins;
	public boolean install_plugins;
	public boolean update_themes;
	public boolean install_themes;
	public boolean update_core;
	public boolean list_users;
	public boolean remove_users;
	public boolean add_users;
	public boolean promote_users;
	public boolean edit_theme_options;
	public boolean delete_themes;
	public boolean export;
	public boolean administrator;
}
