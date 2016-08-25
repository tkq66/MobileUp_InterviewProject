package com.interview.tk.mobileup.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.StringBuffer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonParseException;

import com.interview.tk.mobileup.domain.Listing;
import com.interview.tk.mobileup.repository.ListingRepository;
import com.interview.tk.mobileup.specification.*;

@Controller
public class ServerController 
{
	@Autowired
	private ListingRepository listingRepository;
	
	@Autowired
	private InitialDataHelper initialDataHelper;
	
	private ObjectMapper mapper;
	
	/**
	 * Initialize all in-class variables.
	 */
	public ServerController()
	{
		this.mapper = new ObjectMapper();
	}
	
	/**
	 * After all the beans had been created and wired, clear existing table
	 * then initialize the initial data to the database.
	 */
	@PostConstruct
	public void prepareDatabase()
	{
		this.clearExistingDatabase();
		this.initializeDatabaseFromFile();
	}
	
	/**
	 * Renders the "index" template and populate it with the listings that were given.
	 * 
	 * @param model	- Model object that is to be used prepare data that Thymeleaf templating engine can use.
	 * @return		- String name of the template that is to be rendered.
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(Model model)
	{
		// Query all listings, ordered by descending date
		List<Listing> listings = this.listingRepository.queryOrder(new AllListingByDescendingDateSpecification());
		
		// Prep the container to bind forms with
		ListingsJsonString listingJsonString = new ListingsJsonString();
		SearchString searchString = new SearchString();
		
		model.addAttribute("searchString", searchString);
		model.addAttribute("listingJsonString", listingJsonString);
		model.addAttribute("listings", listings);
		return "index";
	}
	
	/**
	 * 
	 * @param searchString	- Form data that was sent in by the previous template, containing object containing keywords to be searched.
	 * @param model			- Model object that is to be used prepare data that Thymeleaf templating engine can use.
	 * @return				- String name of the template that is to be rendered.
	 */
	@RequestMapping(value="/details", method=RequestMethod.POST, params={"search"}, headers = "content-type=application/x-www-form-urlencoded")
	public String searchResult(@ModelAttribute SearchString searchString, Model model)
	{
		// Search for a list of elements with the given keyword
		SearchRepository<Listing> searchRepository = new SearchRepository<Listing>(this.listingRepository, this.mapper);
		List<String> result = searchRepository.getListOfObjectStringFoundFromKeywordArray(searchString.getSearchStringArray());

		model.addAttribute("processedListings", result);
		return "details";
	}
	
	/**
	 * Renders the "details" page template and populate it with listings that were given.
	 * 
	 * @param listingJsonString	- Form data that was sent in by the previous template, containing list of id of objects to be displayed.
	 * @param model				- Model object that is to be used prepare data that Thymeleaf templating engine can use.
	 * @return					- String name of the template that is to be rendered.
	 */
	@RequestMapping(value="/details", method=RequestMethod.POST, params={"select"}, headers = "content-type=application/x-www-form-urlencoded")
	public String selectResult(@ModelAttribute ListingsJsonString listingJsonString, Model model)
	{
		model.addAttribute("processedListings", listingJsonString.getListingArray());
		return "details";
	}
	
	/**
	 * Create an entity inside the database, as advised by the calling front-end.
	 * 
	 * @param request		- An HttpServletRequest object, containing JSON string of the listing information to be created.
	 * @param response		- An HttpServletResponse object.
	 * @return				- Listing object of the newly created Listing, to be converted into JSON String upon returning.
	 * @throws IOException	- From getting the request data string.
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public @ResponseBody Listing create(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String requestString = this.getRequestString(request);
		
		// Parse request string into JSON object
		JSONObject requestObject = new JSONObject(requestString);
	
		Listing newListing = new Listing();
		newListing.setAddress(requestObject.getString("address"));
		newListing.setPrice(requestObject.getString("price"));
		newListing.setDescription(requestObject.getString("description"));
		
		this.listingRepository.add(newListing);
		
		// Query all listings, ordered by descending date, then get the latest listing
		List<Listing> updatedListingList = this.listingRepository.queryOrder(new AllListingByDescendingDateSpecification());
		Listing updatedListing = updatedListingList.get(0);
		
		return updatedListing;
	}
	
	/**
	 * Update entities inside the database, as advised by the calling front-end.
	 * 
	 * @param request		- An HttpServletRequest object, containing JSONArray string of the listings to be updated.
	 * @param response		- An HttpServletResponse object.
	 * @return				- List of Listing object of the updated Listings, to be converted into JSON String upon returning.
	 * @throws IOException	- From getting the request data string.
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public @ResponseBody List<Listing> update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String requestString = this.getRequestString(request);
		
		// Parse request string into a JSON array
		JSONArray requestObjectArray = new JSONArray(requestString);
		
		// For each JSON object in the array, update the database with them
		for(int i=0; i < requestObjectArray.length(); i++)
		{
			JSONObject listingJson = requestObjectArray.getJSONObject(i);
			
			Listing listingElement = new Listing();
			listingElement.setListingId(listingJson.getInt("listingId"));
			listingElement.setAddress(listingJson.getString("address"));
			listingElement.setDescription(listingJson.getString("description"));
			listingElement.setPrice(listingJson.getString("price"));
			
			this.listingRepository.update(listingElement);
		}
		
		// Fetch updated listing table for rendering
		List<Listing> updatedListing = this.listingRepository.query();
		
		return updatedListing;
	}
	
	/**
	 * Deletes Listing entity from the database, as advised by the calling front-end.
	 * 
	 * @param request		- An HttpServletRequest object, containing JSONArray string of the listings to be deleted.
	 * @param response		- An HttpServletResponse object.
	 * @return				- List of Listing object of the updated Listings, to be converted into JSON String upon returning.
	 * @throws IOException	- From getting the request data string.
	 */
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public @ResponseBody List<Listing> delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String requestString = this.getRequestString(request);

		// Transform string to list
		List<String> idList = new ArrayList<String>(Arrays.asList(requestString.split("\\[|\\]|\\s*,\\s*")));
		
		// Delete listings with id inside list (starting at 1 because element 0 is white space)
		for(int i = 1; i < idList.size(); i++)
		{
			int id = Integer.parseInt(idList.get(i));
			this.listingRepository.remove(new ListingByIdSpecification(id));
		}
		
		// Fetch updated listing table for rendering
		List<Listing> updatedListing = this.listingRepository.query();
		
		return updatedListing;
	}
	
	/**
	 * Take the "request" object and stream the data that was sent into a String
	 * 
	 * @param request		- An HttpServletRequest object, containing the desired data and convenience reader methods.
	 * @return				- String containing the entirety of the data associated with the request object.
	 * @throws IOException	- From reading in the request stream
	 */
	private String getRequestString(HttpServletRequest request) throws IOException
	{
		// Read in data send front front-end
		String bufferString;
		StringBuffer requestString = new StringBuffer();
		BufferedReader reader = request.getReader();
		while ((bufferString = reader.readLine()) != null)
		{
			requestString.append(bufferString);
		}
		
		return requestString.toString();
	}
	
	/**
	 * Remove all elements from the database table if there are stuff initially there.
	 */
	private void clearExistingDatabase()
	{
		// Clear existing table		
		int enrtyInTable = this.listingRepository.query().size();
		if(enrtyInTable != 0)
		{
			this.listingRepository.remove();
		}
	}
	
	/**
	 * Read data from client's CSV file and populate the database table with it.
	 */
	private void initializeDatabaseFromFile()
	{
		// Initialize data from client's CSV file
		// Even if initialization failed, proceed to allow the site to work because the site's
		// functionality is independent from the CSV file.
		String fileName = "src/main/resources/mobileupInterviewProject_realEstateWebApp.csv";
		try
		{
			initialDataHelper.setTargetFile(fileName);
			initialDataHelper.initializeData();
		}
		catch(FileNotFoundException exception)
		{
			exception.printStackTrace();
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
}
