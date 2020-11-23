package com.mshakir.javaapi.ws.ui.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mshakir.javaapi.ws.ui.model.request.UpdateUserDetialsRequestModel;
import com.mshakir.javaapi.ws.ui.model.request.UserDetailsRequestModel;
import com.mshakir.javaapi.ws.ui.model.response.UserRest;
@RestController
@RequestMapping("users")

public class UserController {
	Map<String, UserRest>users;
	@GetMapping
	public String getUsers(@RequestParam (value="page", defaultValue="1", required=false) int page , 
						   @RequestParam (value="limit", defaultValue="20") int limit , 
						   @RequestParam (value="sort", defaultValue="Ascending Order", required=false) String sort)  
	{
		System.out.println("Get Mapping Method Been Called... ");
		System.out.println("Page Number : "+ page);
		System.out.println("Page Limit  : "+ limit);
		System.out.println("Sorting     : "+ sort);
		
		return "Get Mapping Method Been Called for all the Users... \n" +
				"Page Number : " + 
				"\nPage Limit  : "+ 
				"\nSorting     : "+ sort;
//		return "Get USERS called with certain limit::: " + limit + " and certain page number:::  " + page + "  \nSORTING :::" + sort;
	}
	
	@GetMapping(path="/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity <UserRest> getUser(@PathVariable String userId) 
	{
		if (users.containsKey(userId))
		{
			return new ResponseEntity<UserRest> (users.get(userId), HttpStatus.OK);			
		}
		else
		{
			return new ResponseEntity<UserRest>(HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE } ,
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public ResponseEntity <UserRest> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails) {		
		UserRest returnValue = new UserRest();
		returnValue.setFirstName(userDetails.getFirstName());
		returnValue.setLastName(userDetails.getLastName());
		returnValue.setEmail(userDetails.getEmail());

		String userId = UUID.randomUUID().toString();
		returnValue.setUserId(userId);
		
		if(users == null) users = new HashMap<>();
		users.put(userId, returnValue);

		return new ResponseEntity<UserRest> (returnValue, HttpStatus.OK);		
	}
	
	@PutMapping(path="/{userId}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE } ,
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserDetialsRequestModel userDetails) {
		UserRest storedUserDetails = users.get(userId);
		storedUserDetails.setFirstName(userDetails.getFirstName());
		storedUserDetails.setLastName(userDetails.getLastName());
		users.put(userId, storedUserDetails);		
		return storedUserDetails;
	}
	
	@DeleteMapping(path="/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
		users.remove(userId);
		System.out.println("The record has been deleted by using the :" + userId);
		return  ResponseEntity.noContent().build();
	}
}
