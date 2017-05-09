package rs.reviewer.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Slika za review ili reviewObject. Moze da bude za jedno od ta dva, ne za oba.
 */
@Table(name = "Image", id="_id")
public class Image extends AbstractModel
{
	@Column(name = "path", notNull=true)
	private String path;
	
	@Column(name = "isMain")
	private boolean isMain;
	
	@Column(name = "reviewObject", onDelete=ForeignKeyAction.CASCADE)
	private ReviewObject reviewObject;
	
	@Column(name = "review", onDelete=ForeignKeyAction.CASCADE)
	private Review review;
	
	public Image() {} // required by activeandroid

	public Image(String modelId, Date dateModified, String path, boolean isMain, ReviewObject reviewObject)
	{
		super(modelId, dateModified);
		this.path = path;
		this.isMain = isMain;
		this.reviewObject = reviewObject;
		this.review = null;
	}
	
	public Image(String modelId, Date dateModified, String path, boolean isMain, Review review)
	{
		super(modelId, dateModified);
		this.path = path;
		this.isMain = isMain;
		this.review = review;
		this.reviewObject = null;
	}

	public Image(String path, boolean isMain, ReviewObject reviewObject)
	{
		this.path = path;
		this.isMain = isMain;
		this.reviewObject = reviewObject;
		this.review = null;
	}
	
	public Image(String path, boolean isMain, Review review)
	{
		this.path = path;
		this.isMain = isMain;
		this.review = review;
		this.reviewObject = null;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public boolean isMain()
	{
		return isMain;
	}

	public void setMain(boolean isMain)
	{
		this.isMain = isMain;
	}

	public ReviewObject getReviewObject() {
		return reviewObject;
	}

	public void setReviewObject(ReviewObject reviewObject) {
		if(review != null)
		{
			throw new RuntimeException("Image moze da ima ili ReviewObject ili Review, ne oba.");
		}
		this.reviewObject = reviewObject;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		if(reviewObject != null)
		{
			throw new RuntimeException("Image moze da ima ili ReviewObject ili Review, ne oba.");
		}
		this.review = review;
	}
}
