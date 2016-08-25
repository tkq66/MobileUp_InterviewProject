package com.interview.tk.mobileup.specification;

/**
 * Specification for performing an OR operation between two specification.
 * @author teekay
 *
 * @param <T> Entity to be used to apply specification to.
 */
public class OrSpecification<T> extends AbstractSpecification<T>	
{
	private Specification<T> firstSpecification;
	private Specification<T> secondSpecification;
	
	public OrSpecification(Specification<T> firstSpecification, Specification<T> secondSpecification)
	{
		this.firstSpecification = firstSpecification;
		this.secondSpecification = secondSpecification;
	}
	
	@Override
	public boolean isSatisfiedBy(T t)
	{
		return (this.firstSpecification.isSatisfiedBy(t) || 
				this.secondSpecification.isSatisfiedBy(t));
	}
	
	@Override
	public String toSQLQuery()
	{
		return String.format("%1s OR %2s",
				this.firstSpecification.toSQLQuery(),
				this.secondSpecification.toSQLQuery());
	}
}
