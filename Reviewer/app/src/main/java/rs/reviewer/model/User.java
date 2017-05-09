package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "User", id="_id")
public class User extends AbstractModel
{
	@Column(name = "name", notNull=true, unique=true)
	private String name;
	
	@Column(name = "email", notNull=true, unique=true)
	private String email;

	public User() {} // required by activeandroid
	
	public User(String modelId, Date dateModified, String name, String email)
	{
		super(modelId, dateModified);
		this.name = name;
		this.email = email;
	}

	public User(String name, String email)
	{
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Grupe koje je kreirao.
	 */
	public List<Group> getGroupsCreated()
	{
        return getMany(Group.class, "userCreated");
    }
	
	/**
	 * Grupe u kojima je clan.
	 */
	public List<Group> getGroupsMember()
	{
		List<GroupToUser> manyToMany = getMany(GroupToUser.class, "user");
		List<Group> ret = new ArrayList<Group>();
		for(GroupToUser m2m : manyToMany)
		{
			ret.add(m2m.getGroup());
		}
		return ret;
    }
	
	public List<Comment> getComments()
	{
        return getMany(Comment.class, "userCreated");
    }
	
	public List<Review> getReviews()
	{
        return getMany(Review.class, "userCreated");
    }
	
	public List<ReviewObject> getReviewObjects()
	{
        return getMany(ReviewObject.class, "userCreated");
    }
	
	public static User getByEmail(String email)
	{
		return new Select().from(User.class).where("email = ?", email).executeSingle();
	}

	public boolean isInGroup(String groupId)
	{
		// ako postoji veza izmedju ovog reviewa i grupe sa modelId = groupId
		return new Select()
			.from(GroupToUser.class).as("GRP_TO_USR")
			.join(Group.class).as("GRP")
			.on("GRP._id = GRP_TO_USR.userGroup")
			.where("GRP_TO_USR.user = ? and GRP.modelId = ?", getId(), groupId).executeSingle() != null;
	}
}
