package rs.reviewer.exceptions;

public class ValidationException extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	public ValidationException()
	{
		super();
	}

	public ValidationException(String detailMessage)
	{
		super(detailMessage);
	}
}
