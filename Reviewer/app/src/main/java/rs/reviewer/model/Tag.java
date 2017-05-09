package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "Tag", id="_id")
public class Tag extends AbstractModel
{
	@Column(name = "name", notNull=true, unique=true)
	private String name;

	public Tag() {} // required by activeandroid
	
	public Tag(String modelId, Date dateModified, String name)
	{
		super(modelId, dateModified);
		this.name = name;
	}

	public Tag(String name)
	{
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addReview(Review toAdd)
	{
		ValidationUtils.checkSaved(toAdd, this);
		new TagToReview(toAdd, this).save();
	}
	
	public void removeReview(Review toRemove)
	{
		ValidationUtils.checkSaved(toRemove, this);
		TagToReview m2m = new Select().from(TagToReview.class)
				.where("review = ? and tag = ?", toRemove.getId(), getId())
				.executeSingle();
		
		if(m2m != null) 
		{
			m2m.deleteSynced();
		}
	}
	
	public List<Review> getReviews()
	{
		List<TagToReview> manyToMany = getMany(TagToReview.class, "tag");
		List<Review> ret = new ArrayList<Review>();
		for(TagToReview m2m : manyToMany)
		{
			ret.add(m2m.getReview());
		}
        return ret;
    }
	
	public List<ReviewObject> getReviewObjects()
	{
		List<TagToReviewObject> manyToMany = getMany(TagToReviewObject.class, "tag");
		List<ReviewObject> ret = new ArrayList<ReviewObject>();
		for(TagToReviewObject m2m : manyToMany)
		{
			ret.add(m2m.getReviewObject());
		}
        return ret;
    }

	public static Tag getByName(String name)
	{
		return new Select().from(Tag.class).where("name = ?", name).executeSingle();
	}
	
	public boolean hasReview(String reviewId)
	{
		return new Select()
			.from(TagToReview.class).as("TAG_TO_REV")
			.join(Review.class).as("REV")
			.on("REV._id = TAG_TO_REV.review")
			.where("TAG_TO_REV.tag = ? and REV.modelId = ?", getId(), reviewId).executeSingle() != null;
	}

	public void addReviewObject(ReviewObject toAdd)
	{
		ValidationUtils.checkSaved(toAdd, this);
		new TagToReviewObject(toAdd, this).save();
	}
	
	public void removeReviewObject(ReviewObject toRemove)
	{
		ValidationUtils.checkSaved(toRemove, this);
		TagToReviewObject m2m = new Select().from(TagToReviewObject.class)
				.where("reviewObject = ? and tag = ?", toRemove.getId(), getId())
				.executeSingle();
		
		if(m2m != null) 
		{
			m2m.deleteSynced();
		}
	}
	
	public boolean hasReviewObject(String reviewObjId)
	{
		return new Select()
			.from(TagToReviewObject.class).as("TAG_TO_REVOB")
			.join(ReviewObject.class).as("REVOB")
			.on("REVOB._id = TAG_TO_REVOB.reviewObject")
			.where("TAG_TO_REVOB.tag = ? and REVOB.modelId = ?", getId(), reviewObjId).executeSingle() != null;
	}
}
