package rs.reviewer.model;

import com.activeandroid.Model;

public class ValidationUtils 
{
	public static void checkSaved(Model... models)
	{
		for(Model model : models)
		{
			if(model.getId() == null) throw new RuntimeException("Model <"+model.toString()+"> mora biti snimljen, uradite save() pre ove operacije.");
		}
	}
}
