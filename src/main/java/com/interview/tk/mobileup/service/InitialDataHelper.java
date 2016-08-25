package com.interview.tk.mobileup.service;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interview.tk.mobileup.repository.ListingRepository;
import com.interview.tk.mobileup.domain.Listing;

/**
 * Object used to initialize data to database from file.
 * @author teekay
 *
 */
@Component
public class InitialDataHelper
{
	@Autowired
	private ListingRepository listingRepository;
	
	private FileReader fileReader;
	private CSVReader csvReader;
	private boolean initialized = false;
		
	public void setFileReader(FileReader fileReader)
	{
		this.fileReader = fileReader;
	}
		
	public void setCSVReader(CSVReader csvReader)
	{
		this.csvReader = csvReader;
	}
	
	/**
	 * With the given file name, prepare all the reader objects to read in the file.
	 * @param fileName				 - Name of the file to be reading in from.
	 * @throws FileNotFoundException - File did not exist with the given fileName.
	 */
	public void setTargetFile(String fileName) throws FileNotFoundException
	{
		this.setFileReader(new FileReader(fileName));
		this.setCSVReader(new CSVReader(this.fileReader));
	}
	
	/**
	 * Read in data from the file then add them to the given repository.
	 * @throws IOException	- Failed to read from the file.
	 */
	public void initializeData() throws IOException
	{
		String[] readLine;
		Listing listing = new Listing();
		// Skip the first line, which is the label
		readLine = this.csvReader.readNext();
		while((readLine = this.csvReader.readNext()) != null)
		{
			listing.setListingId(Integer.parseInt(readLine[0]));
			listing.setAddress(readLine[1]);
			listing.setDescription(readLine[2]);
			listing.setPrice(readLine[3]);
			listingRepository.add(listing);
		}
		
		this.initialized = true;
	}
}
