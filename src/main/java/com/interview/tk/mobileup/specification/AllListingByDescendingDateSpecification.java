package com.interview.tk.mobileup.specification;

import java.util.List;

import com.interview.tk.mobileup.specification.AbstractSpecification;
import com.interview.tk.mobileup.domain.Listing;

/**
 * Specification for querying Listing table by descending date
 * @author teekay
 *
 */
public class AllListingByDescendingDateSpecification extends AbstractSpecification<List<Listing>>
{
	public AllListingByDescendingDateSpecification()
	{
	}
	
	@Override
	public boolean isSatisfiedBy(List<Listing> listings)
	{
		Listing firstListing;
		Listing secondListing;
		
		// Return true is the list is empty
		if(!listings.iterator().hasNext())
		{
			return true;
		}
		
		// Return true if the list has 1 element
		// or more than 1 elements and satisfy the descending criteria
		firstListing = listings.iterator().next();
		while(listings.iterator().hasNext())
		{
			secondListing = listings.iterator().next();
			
			// If second date came AFTER the first date, this list is no longer
			// descending. Therefore, return false.
			if(firstListing.getDateSubmitted().compareTo(secondListing.getDateSubmitted()) == -1)
			{
				return false;
			}
			
			// After checking for descending criteria, update variables
			firstListing = secondListing;
		}
		
		return true;
	}

	@Override
	public String toSQLQuery() 
	{
		return String.format("%1s DESC",
				"DateSubmitted");
	}
}

