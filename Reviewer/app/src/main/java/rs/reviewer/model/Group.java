package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "UserGroup", id="_id")
public class Group extends AbstractModel
{
	@Column(name = "name", notNull=true)
	private String name;
	
	@Column(name = "userCreated", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private User userCreated;
	
	public Group() {} // required by activeandroid
	
	public Group(String modelId, Date dateModified, String name, User userCreated) {
		super(modelId, dateModified);
		this.name = name;
		this.userCreated = userCreated;
	}

	public Group(String name, User userCreated)
	{
		this.name = name;
		this.userCreated = userCreated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUserCreated() {
		return userCreated;
	}

	public void setUserCreated(User userCreated) {
		this.userCreated = userCreated;
	}
	
	public boolean isCreatedBy(String userId)
	{
		return userCreated.getModelId().equals(userId);
	}
	
	public List<User> getUsers()
	{
		List<GroupToUser> manyToMany = getMany(GroupToUser.class, "userGroup");
		List<User> ret = new ArrayList<User>();
		for(GroupToUser m2m : manyToMany)
		{
			ret.add(m2m.getUser());
		}
        return ret;
    }
	
	public void addUser(User toAdd)
	{
		ValidationUtils.checkSaved(toAdd, this);
		new GroupToUser(toAdd, this).save();
	}
	
	public void removeUser(User toRemove)
	{
		ValidationUtils.checkSaved(toRemove, this);
		GroupToUser m2m = new Select().from(GroupToUser.class)
				.where("user = ? and userGroup = ?", toRemove.getId(), getId())
				.executeSingle();
		
		if(m2m != null) 
		{
			m2m.deleteSynced();
		}
	}
	
	public List<Review> getReviews()
	{
		List<GroupToReview> manyToMany = getMany(GroupToReview.class, "userGroup");
		List<Review> ret = new ArrayList<Review>();
		for(GroupToReview m2m : manyToMany)
		{
			ret.add(m2m.getReview());
		}
        return ret;
    }
	
	public List<Review> getReviewsByUser(String userId)
	{
		List<GroupToReview> manyToMany = getMany(GroupToReview.class, "userGroup");
		List<Review> ret = new ArrayList<Review>();
		for(GroupToReview m2m : manyToMany)
		{
			Review rev = m2m.getReview();
			if(rev.getUserCreated().getId().equals(userId))
			{
				ret.add(rev);
			}
		}
        return ret;
    }
	
	public void addReview(Review toAdd)
	{
		ValidationUtils.checkSaved(toAdd, this);
		new GroupToReview(toAdd, this).save();
	}
	
	public void removeReview(Review toRemove)
	{
		ValidationUtils.checkSaved(toRemove, this);
		GroupToReview m2m = new Select().from(GroupToReview.class)
				.where("review = ? and userGroup = ?", toRemove.getId(), getId())
				.executeSingle();
		
		if(m2m != null) 
		{
			m2m.deleteSynced();
		}
	}
	
	public boolean hasReview(String reviewId)
	{
		// ako postoji veza izmedju ovog reviewa i grupe
		return new Select()
			.from(GroupToReview.class).as("GRP_TO_REV")
			.join(Review.class).as("REV")
			.on("REV._id = GRP_TO_REV.review")
			.where("GRP_TO_REV.userGroup = ? and REV.modelId = ?", getId(), reviewId).executeSingle() != null;
	}
}
