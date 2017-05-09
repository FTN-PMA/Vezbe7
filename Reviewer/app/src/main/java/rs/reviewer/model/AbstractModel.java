package rs.reviewer.model;

import android.database.sqlite.SQLiteConstraintException;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public abstract class AbstractModel extends Model 
{
	/**
	 * Univerzalno jedinstveni id.
	 */
	@Column(name = "modelId", 
			index=true, 
			notNull=true, 
			unique=true, 
			onUniqueConflict=ConflictAction.REPLACE)
	private String modelId = null;
	
	/**
	 * Datum/vreme kada je poslednji put modifikovan. Ne azurira se automatski.
	 */
	@Column(name = "dateModified", notNull=true)
	private Date dateModified;

	/**
	 * Kad se kreira nov entitet od strane lokalne aplikacije.
	 */
	public AbstractModel() 
	{
		this.modelId = UUID.randomUUID().toString(); // ako je nov objekat generisi random id
		this.dateModified = new Date(); // dateModified je sad kad je kreiran
	}
	
	/**
	 * Kad se kreira postojeci entitet koji je preuzet sa servera.
	 * @param modelId
	 * @param dateModified
	 */
	public AbstractModel(String modelId, Date dateModified)
	{
		this.modelId = modelId;
		this.dateModified = dateModified;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getModelId() {
		return modelId;
	}
	
	public static <T extends AbstractModel> T getByModelId(Class<T> modelClass,String modelId)
	{
		return new Select().from(modelClass).where("modelId = ?", modelId).executeSingle();
	}
	
	/**
	 * Snima entitet u bazu.
	 * @return id entiteta
	 * @throws SQLiteConstraintException ako entitet nije snimljen
	 */
	public Long saveOrThrow() throws SQLiteConstraintException
	{
		Long newId = save();
		if(newId == -1)
		{
			throw new SQLiteConstraintException("Object not unique.");
		}
		else
		{
			return newId;
		}
	}
	
	/**
	 * Brise entitet iz baze i dodaje DeletedEntry radi sinhronizacije sa serverom.
	 * Koristiti umetso obicne delete() metode.
	 */
	public void deleteSynced()
	{
		delete();
		new DeletedEntry(getClass().getSimpleName(), modelId, new Date()).save();
	}
	
	public static <T extends AbstractModel> List<T> getNewerThan(Class<T> modelClass, Date date)
	{
		return new Select().from(modelClass).where("dateModified > ?", date.getTime()).execute();
	}
}
