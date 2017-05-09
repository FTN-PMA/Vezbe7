package rs.reviewer.tools;

import rs.reviewer.R;
import rs.reviewer.model.Comment;
import rs.reviewer.model.NavItem;
import rs.reviewer.model.Review;
import rs.reviewer.model.Tag;
import rs.reviewer.model.User;

import java.util.ArrayList;

public class Mokap {

	
	/*MOKAP*/
    public static ArrayList<NavItem> getList(){
    	ArrayList<NavItem> items = new ArrayList<NavItem>();
    	
    	items.add(new NavItem("Home", "Meetup review objects", R.drawable.ic_action_map));
    	items.add(new NavItem("Groups", "Meetup groups", R.drawable.ic_action_select_all));
    	items.add(new NavItem("Reviews", "Meetup destination", R.drawable.ic_action_labels));
    	items.add(new NavItem("Preferences", "Change your preferences", R.drawable.ic_action_settings));
    	items.add(new NavItem("About", "Get to know about us", R.drawable.ic_action_about));
    	
    	items.add(new NavItem("Home", "Meetup review objects", R.drawable.ic_action_map));
    	items.add(new NavItem("Groups", "Meetup groups", R.drawable.ic_action_select_all));
    	items.add(new NavItem("Reviews", "Meetup destination", R.drawable.ic_action_labels));
    	items.add(new NavItem("Preferences", "Change your preferences", R.drawable.ic_action_settings));
    	items.add(new NavItem("About", "Get to know about us", R.drawable.ic_action_about));
    	
    	items.add(new NavItem("Home", "Meetup review objects", R.drawable.ic_action_map));
    	items.add(new NavItem("Groups", "Meetup groups", R.drawable.ic_action_select_all));
    	items.add(new NavItem("Reviews", "Meetup destination", R.drawable.ic_action_labels));
    	items.add(new NavItem("Preferences", "Change your preferences", R.drawable.ic_action_settings));
    	items.add(new NavItem("About", "Get to know about us", R.drawable.ic_action_about));
    	
    	return items;
    }
    
    public static ArrayList<Tag> getTags() {
    	
    	ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag("Name1"));
		tags.add(new Tag("Name2"));
		tags.add(new Tag("Name3"));
		tags.add(new Tag("Name4"));
		tags.add(new Tag("Name5"));
		tags.add(new Tag("Name6"));
		tags.add(new Tag("Name1"));
		tags.add(new Tag("Name2"));
		tags.add(new Tag("Name3"));
		tags.add(new Tag("Name4"));
		tags.add(new Tag("Name5"));
		tags.add(new Tag("Name6"));
		
		return tags;
	}
    
    public static ArrayList<Comment> getCommentsList(){
		ArrayList<Comment> items = new ArrayList<Comment>();
		items.add(new Comment("bla bla truc", new User(), new Review()));
		items.add(new Comment("bla bla truc", new User(), new Review()));
		items.add(new Comment("bla bla truc", new User(), new Review()));
		items.add(new Comment("bla bla truc", new User(), new Review()));
		items.add(new Comment("bla bla truc", new User(), new Review()));
		items.add(new Comment("bla bla truc", new User(), new Review()));
		items.add(new Comment("bla bla truc", new User(), new Review()));
		items.add(new Comment("bla bla truc", new User(), new Review()));
		items.add(new Comment("bla bla truc", new User(), new Review()));
		
		return items;
	}
    
	/*
	public static ArrayList<UserItem> getUserList(){
		ArrayList<UserItem> items = new ArrayList<UserItem>();
		
		items.add(new UserItem("User1","mail@mail.com",new Date()));
		items.add(new UserItem("User2","mail@mail.com",new Date()));
		items.add(new UserItem("User3","mail@mail.com",new Date()));
		items.add(new UserItem("User4","mail@mail.com",new Date()));
		items.add(new UserItem("User5","mail@mail.com",new Date()));
		items.add(new UserItem("User6","mail@mail.com",new Date()));
		items.add(new UserItem("User7","mail@mail.com",new Date()));
		
		return items;
	}*/
	
	public static ArrayList<User> getUserModelList()
	{
		ArrayList<User> items = new ArrayList<User>();
		
		items.add(new User("User1","mail@mail.com"));
		items.add(new User("User2","mail@mail.com"));
		items.add(new User("User3","mail@mail.com"));
		
		return items;
	}
	
	/*public static ArrayList<ReviewItem> getReviewList(){
		ArrayList<ReviewItem> items = new ArrayList<ReviewItem>();
		
		items.add(new ReviewItem("Name1", "Bla1", 2, getUserList().get(0), 
				getCommentsList(), new Date(), new Date(), 
				gatGaleryList(), getTags(),new GaleryItem("", "")));
		items.add(new ReviewItem("Name2", "Bla2", 3, getUserList().get(0), 
				getCommentsList(), new Date(), new Date(), 
				gatGaleryList(), getTags(),new GaleryItem("", "")));
		items.add(new ReviewItem("Name3", "Bla3", 4, getUserList().get(0), 
				getCommentsList(), new Date(), new Date(), 
				gatGaleryList(), getTags(),new GaleryItem("", "")));
		items.add(new ReviewItem("Name4", "Bla4", 5, getUserList().get(0), 
				getCommentsList(), new Date(), new Date(), 
				gatGaleryList(), getTags(),new GaleryItem("", "")));
		items.add(new ReviewItem("Name5", "Bla5", 1, getUserList().get(0), 
				getCommentsList(), new Date(), new Date(), 
				gatGaleryList(), getTags(),new GaleryItem("", "")));
		
		return items;
	}*/
	/*
	public static ArrayList<Group> getGroupList(){
		ArrayList<Group> items = new ArrayList<Group>();
		
		items.add(new Group("Group1", new Date(), getReviewList(), getUserList()));
		items.add(new Group("Group2", new Date(), getReviewList(), getUserList()));
		items.add(new Group("Group3", new Date(), getReviewList(), getUserList()));
		items.add(new Group("Group4", new Date(), getReviewList(), getUserList()));
		items.add(new Group("Group5", new Date(), getReviewList(), getUserList()));
		
		return items;
	}
	
	public static Group getSingleGroup(){
		return getGroupList().get(0);
	}
    */
}
