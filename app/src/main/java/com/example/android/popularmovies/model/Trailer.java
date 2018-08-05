package com.example.android.popularmovies.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Trailer{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<TrailerItem> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<TrailerItem> results){
		this.results = results;
	}

	public List<TrailerItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"Trailer{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}