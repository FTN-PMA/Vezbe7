package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

@Table(name = "TagToReviewObject", id="_id")
public class TagToReviewObject extends AbstractModel
{
	@Column(name = "reviewObject", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private ReviewObject reviewObject;
	
	@Column(name = "tag", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Tag tag;
	
	public TagToReviewObject() {} // required by activeandroid

	public TagToReviewObject(ReviewObject reviewObject, Tag tag)
	{
		this.reviewObject = reviewObject;
		this.tag = tag;
	}

	public ReviewObject getReviewObject() {
		return reviewObject;
	}

	public void setReviewObject(ReviewObject reviewObject) {
		this.reviewObject = reviewObject;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
