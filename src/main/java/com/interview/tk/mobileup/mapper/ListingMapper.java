package com.interview.tk.mobileup.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.interview.tk.mobileup.domain.Listing;

/**
 * Object to map queried data to Listing entity.
 * @author teekay
 *
 */
public class ListingMapper implements RowMapper<Listing> 
{
	public Listing mapRow(ResultSet rs, int rowNum) throws SQLException 
	{
		Listing listing = new Listing();
		listing.setListingId(rs.getInt("ListingId"));
		listing.setAddress(rs.getString("Address"));
		listing.setDescription(rs.getString("Description"));
		listing.setPrice(rs.getString("Price"));
		listing.setDateSubmitted(rs.getTimestamp("DateSubmitted"));
		return listing;
	}
}
