package com.dblp.communities.start;

import java.util.ArrayList;
import java.util.List;

import com.dblp.communities.io.FileDownloader;
import com.dblp.communities.parser.Coauthor;
import com.dblp.communities.parser.Hit;

public class Main {

	public static void main(String[] args) {
		
		// Coauthor search
		/*
		List<Coauthor> coauthors = FileDownloader.searchForCoauthorsOf("f/Fomin:Fedor_V=");
		for (Coauthor author : coauthors) {
			System.out.println(author.getName());
			System.out.println(author.getUrlpt());
			System.out.println();
		}
		*/
		
		// Person search
		/*
		List<String> keywords = new ArrayList<String>();
		keywords.add("Dregi");
		keywords.add("Markus");
		List<Hit> hits = FileDownloader.searchForPerson(keywords);
		for (Hit hit : hits) {
			System.out.println(hit.getId());
			System.out.println(hit.getAuthor());
			System.out.println(hit.getUrl());
			System.out.println(hit.getUrlPt());
			System.out.println();
		}
		*/
	}
	
}
