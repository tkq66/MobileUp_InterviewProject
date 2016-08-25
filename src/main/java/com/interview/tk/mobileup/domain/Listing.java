package com.interview.tk.mobileup.domain;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Listing
{
	private int ListingId;
	private String Address;
	private String Description;
	private String Price;
	private String DateSubmitted;
	
	public int getListingId()
	{
		return this.ListingId;
	}
	public void setListingId(int listingId)
	{
		this.ListingId = listingId;
	}
	public String getAddress()
	{
		return this.Address;
	}
	public void setAddress(String address)
	{
		this.Address = address;
	}
	public String getDescription()
	{
		return this.Description;
	}
	public void setDescription(String description)
	{
		this.Description = description;
	}
	public String getPrice()
	{
		return this.Price;
	}
	public void setPrice(String price)
	{
		this.Price = price;
	}
	public String getDateSubmitted()
	{
		return this.DateSubmitted;
	}
	public void setDateSubmitted(Date DateSubmitted)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		this.DateSubmitted  = dateFormat.format(DateSubmitted);
	}
}
