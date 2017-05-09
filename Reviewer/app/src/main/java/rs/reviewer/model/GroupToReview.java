package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "GroupToReview", id="_id")
public class GroupToReview extends AbstractModel
{
	@Column(name = "review", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Review review;
	
	@Column(name = "userGroup", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Group userGroup;
	
	public GroupToReview() {} // required by activeandroid

	public GroupToReview(Review review, Group userGroup)
	{
		this.review = review;
		this.userGroup = userGroup;
	}

	public GroupToReview(String modelId, Date dateModified, Review review, Group userGroup)
	{
		super(modelId, dateModified);
		this.review = review;
		this.userGroup = userGroup;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Group getGroup() {
		return userGroup;
	}

	public void setGroup(Group userGroup) {
		this.userGroup = userGroup;
	}
}
