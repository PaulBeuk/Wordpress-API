package org.beuk.wordpress.api.entity;

import java.util.*;

public class User {

	public long ID;

	public String username;
	public String name;
	public String first_name;
	public String last_name;
	public String nickname;
	public String slug;
	public String URL;
	public String avatar;
	public String description;
	public Date registered;
	public List<String> roles;
	public UserCapabilities capabilities;
	public boolean email;
	public UserMeta meta;
}
