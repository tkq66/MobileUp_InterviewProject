package com.interview.tk.mobileup.specification;

/**
 * And implementation of the Specification interface. This abstract layer is here to 
 * implement the AND, OR, and NOT operation as generic to all objects that further 
 * extends the specification concept. This way, it doesn't matter what specification 
 * there is for the database, the AND, OR, and NOT implementation remains the same. 
 * isSatisfiedBy and toSQLString are to be implemented by more specific implementations.
 * @author teekay
 *
 * @param <T> Entity to be used to apply specification to.
 */
public abstract class AbstractSpecification<T> implements Specification<T> 
{
	@Override
	public Specification<T> and(Specification<T> otherSpecification)
	{
		return new AndSpecification(this, otherSpecification);
	}
	
	@Override
	public Specification<T> or(Specification<T> otherSpecification)
	{
		return new OrSpecification(this, otherSpecification);		
	}
	
	@Override
	public Specification<T> not()
	{
		return new NotSpecification(this);
	}
	
	@Override
	public boolean isSatisfiedBy(T t)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toSQLQuery()
	{
		throw new UnsupportedOperationException();
	}
}
