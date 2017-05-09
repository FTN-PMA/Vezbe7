package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

@Table(name = "TagToReview", id="_id")
public class TagToReview extends AbstractModel
{
	@Column(name = "review", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Review review;
	
	@Column(name = "tag", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Tag tag;
	
	public TagToReview() {} // required by activeandroid

	public TagToReview(Review review, Tag tag)
	{
		this.review = review;
		this.tag = tag;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
