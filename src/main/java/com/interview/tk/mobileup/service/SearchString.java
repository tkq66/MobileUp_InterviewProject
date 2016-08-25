package com.interview.tk.mobileup.service;

/**
 * Object to bind with edit-text search form component of the template.
 * @author teekay
 *
 */
public class SearchString
{
	private String searchString;

	public String getSearchString()
	{
		return searchString;
	}

	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}
	
	public String[] getSearchStringArray()
	{
		return this.searchString.split(" ");
	}
}
