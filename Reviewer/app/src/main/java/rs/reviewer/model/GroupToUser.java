package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "GroupToUser", id="_id")
public class GroupToUser extends AbstractModel
{
	@Column(name = "user", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private User user;
	
	@Column(name = "userGroup", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Group userGroup;
	
	public GroupToUser() {} // required by activeandroid

	public GroupToUser(User user, Group userGroup)
	{
		this.user = user;
		this.userGroup = userGroup;
	}

	public GroupToUser(String uuid, Date convertToDate, User user, Group group)
	{
		super(uuid, convertToDate);
		this.user = user;
		this.userGroup = group;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return userGroup;
	}

	public void setGroup(Group userGroup) {
		this.userGroup = userGroup;
	}
}
