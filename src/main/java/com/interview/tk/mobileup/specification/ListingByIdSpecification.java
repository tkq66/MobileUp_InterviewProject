package com.interview.tk.mobileup.specification;

import com.interview.tk.mobileup.specification.AbstractSpecification;
import com.interview.tk.mobileup.domain.Listing;

/**
 * Specification for querying Listing table by 'id'
 * @author teekay
 *
 */
public class ListingByIdSpecification extends AbstractSpecification<Listing>
{
	private int id;
	
	public ListingByIdSpecification(int id)
	{
		this.id = id;
	}
	
	@Override
	public boolean isSatisfiedBy(Listing listing)
	{
		return this.id == listing.getListingId();
	}

	@Override
	public String toSQLQuery() 
	{
		return String.format("%2s = %3s",
				"ListingId",
				this.id);
	}
}
