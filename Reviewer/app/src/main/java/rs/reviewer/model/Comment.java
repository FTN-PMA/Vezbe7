package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "Comment", id="_id")
public class Comment extends AbstractModel
{
	@Column(name = "content", notNull=true)
	private String content;
	
	@Column(name = "userCreated", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private User userCreated;
	
	@Column(name = "review", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Review review;
	
	public Comment() {} // required by activeandroid
	
	public Comment(String modelId, Date dateModified, String content, User userCreated, Review review)
	{
		super(modelId, dateModified);
		this.content = content;
		this.userCreated = userCreated;
		this.review = review;
	}

	public Comment(String content, User userCreated, Review review)
	{
		this.content = content;
		this.userCreated = userCreated;
		this.review = review;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
}
