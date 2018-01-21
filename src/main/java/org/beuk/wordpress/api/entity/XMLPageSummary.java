package org.beuk.wordpress.api.entity;

public class XMLPageSummary {

	// public Date date_created_gmt;
	// public Date dateCreated;
	public int pageId;

	public int pageParentId;

	public String pageTitle;

	public String pageSlug;

	public int getPageId() {

		return pageId;
	}

	public int getPageParentId() {

		return pageParentId;
	}

	public String getPageSlug() {

		return pageSlug;
	}

	public String getPageTitle() {

		return pageTitle;
	}
}
