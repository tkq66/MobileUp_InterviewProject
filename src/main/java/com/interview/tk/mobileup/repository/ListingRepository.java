package com.interview.tk.mobileup.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interview.tk.mobileup.domain.Listing;
import com.interview.tk.mobileup.mapper.ListingMapper;
import com.interview.tk.mobileup.specification.Specification;

/**
 * Repository implementation in Listing entity.
 * @author teekay
 *
 */
@Repository
public class ListingRepository implements RepositoryInterface<Listing>
{
	@Autowired
	private JdbcTemplate jdbcTemplateObject;
	
	public void add(Listing listing)
	{
		String SQLString = "INSERT INTO Listing (ListingId, Address, Description, Price, DateSubmitted) " + 
				"VALUES (?, ?, ?, ?, NOW())";
		this.jdbcTemplateObject.update(SQLString, listing.getListingId(), listing.getAddress(),
				listing.getDescription(), listing.getPrice());
	}

	public void add(Iterable<Listing> listings)
	{
		for(Listing listing : listings)
		{
			String SQLString = "INSERT INTO Listing (ListingId, Address, Description, Price, DateSubmitted) " + 
					"VALUES (?, ?, ?, ?, NOW())";
			this.jdbcTemplateObject.update(SQLString, listing.getListingId(), listing.getAddress(),
					listing.getDescription(), listing.getPrice());
		}
	}

	public void update(Listing listing)
	{
		String SQLString = "UPDATE Listing SET Address=?,Description=?,Price=?, DateSubmitted=NOW() WHERE ListingId=?;";
		this.jdbcTemplateObject.update(SQLString, listing.getAddress(), listing.getDescription(), 
				listing.getPrice(), listing.getListingId());
	}
	
	public void remove()
	{
		String SQLQueryString = "DELETE FROM Listing;";

		this.jdbcTemplateObject.update(SQLQueryString);
	}


	public void remove(Specification specification)
	{
		String SQLQueryPrefix = "DELETE FROM Listing WHERE";
		String SQLQueryString = String.format("%1s %2s;",
									SQLQueryPrefix,
									specification.toSQLQuery());
		
		this.jdbcTemplateObject.update(SQLQueryString);
	}
	
	public List<Listing> query()
	{
		String SQLQueryString = "SELECT * FROM Listing;";

		List<Listing> queriedListing = this.jdbcTemplateObject.query(SQLQueryString, 
																new ListingMapper());
		return queriedListing;
	}

	public List<Listing> query(Specification specification)
	{
		String SQLQueryPrefix = "SELECT * FROM Listing WHERE";
		String SQLQueryString = String.format("%1s %2s;",
									SQLQueryPrefix,
									specification.toSQLQuery());
		
		List<Listing> queriedListing = this.jdbcTemplateObject.query(SQLQueryString, 
																new ListingMapper());
		return queriedListing;
	}
	
	public List<Listing> queryOrder(Specification specification)
	{
		String SQLQueryPrefix = "SELECT * FROM Listing ORDER BY";
		String SQLQueryString = String.format("%1s %2s;",
									SQLQueryPrefix,
									specification.toSQLQuery());
		
		List<Listing> queriedListing = this.jdbcTemplateObject.query(SQLQueryString, 
																new ListingMapper());
		return queriedListing;
	}
}