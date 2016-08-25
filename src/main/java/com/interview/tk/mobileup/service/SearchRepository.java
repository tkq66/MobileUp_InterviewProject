package com.interview.tk.mobileup.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interview.tk.mobileup.repository.RepositoryInterface;

/**
 * Class used to search through a given repository.
 * @author teekay
 *
 * @param <T> Entity to be used to apply searching to.
 */
public class SearchRepository<T>
{
	private RepositoryInterface repository;
	private ObjectMapper mapper;
	
	/**
	 * Inject mandatory dependencies: ObjectMapper and Repository.
	 * 
	 * @param repository	- Repository to be searching from.
	 * @param mapper		- Parses object to JSON string.
	 */
	public SearchRepository(RepositoryInterface repository, ObjectMapper mapper)
	{
		this.repository = repository;
		this.mapper = mapper;
	}
	
	/**
	 * Return list of found object JSON string for a given keyword.
	 * 
	 * @param keyword	- String to be found in the repository.
	 * @return			- List of objects JSON string that are found. If there are errors
	 * reading from source database or converting queried data to string,
	 * then the return value would be an empty array.
	 */
	public List<String> getListOfObjectStringFoundFromKeyword(String keyword)
	{
		List<String> foundObjects = new ArrayList<String>();

		// If fetching source fails, return empty object array
		try
		{
			List<String> sourceList = this.queryAllToString();
			
			// If any element have words that matches keyword, add to found array
			for(int i = 0; i < sourceList.size(); i++)
			{
				String sourceString = sourceList.get(i);
				
				// If the source string does contain keyword and doesn't already exist
				// inside the foundObjects list, then add object to found list
				if(sourceString.contains(keyword) &&
						!(foundObjects.contains(sourceString)))
				{
					foundObjects.add(sourceString);
				}
			}
		}
		catch(JsonProcessingException exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			return foundObjects;	
		}
	}
	
	/**
	 * Return list of found objects JSON string for a set of keywords.
	 * 
	 * @param keywords	- Array of strings to be found in the repository.
	 * @return			- List of objects JSON string that are found. If there are errors
	 * reading from source database or converting queried data to string,
	 * then the return value would be an empty array.
	 */
	public List<String> getListOfObjectStringFoundFromKeywordArray(String[] keywords)
	{
		List<String> foundObjects = new ArrayList<String>();

		// If fetching source fails, return empty object array
		try
		{
			List<String> sourceList = this.queryAllToString();
			
			// Find objects for all keywords
			for(int i = 0; i < keywords.length; i++)
			{
				// If any element have words that matches keyword, add to found array
				for(int j = 0; j < sourceList.size(); j++)
				{
					String sourceString = sourceList.get(j);
					
					// If the source string does contain keyword and doesn't already exist
					// inside the foundObjects, then add object to found list
					if(sourceString.contains(keywords[i]) &&
							!(foundObjects.contains(sourceString)))
					{
						foundObjects.add(sourceString);
					}
				}
			}
		}
		catch(JsonProcessingException exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			return foundObjects;	
		}
	}
	
	/**
	 * Get all data from database as a list of string, each element being object of the row.
	 * 
	 * @return							- List of string of row entities.
	 * @throws JsonProcessingException	- When converting Object to JSON string failed.
	 */
	private List<String> queryAllToString() throws JsonProcessingException
	{
		List<T> queryList = this.repository.query();
		List<String> queryStringList  = new ArrayList<String>();
		
		for(int i = 0; i < queryList.size(); i++)
		{
			String objectString = this.mapper.writeValueAsString(queryList.get(i));
			queryStringList.add(objectString);
		}
		
		return queryStringList;
	}
}
