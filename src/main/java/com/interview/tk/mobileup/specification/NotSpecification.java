package com.interview.tk.mobileup.specification;

/**
 * Specification for performing an NOT operation on a specification.
 * @author teekay
 *
 * @param <T> Entity to be used to apply specification to.
 */
public class NotSpecification<T> extends AbstractSpecification<T>	
{
	private Specification<T> specification;
	
	public NotSpecification(Specification<T> specification)
	{
		this.specification = specification;
	}
	
	@Override
	public boolean isSatisfiedBy(T t)
	{
		return !(this.specification.isSatisfiedBy(t));
	}
	
	@Override
	public String toSQLQuery()
	{
		return String.format("NOT %1s",
				this.specification.toSQLQuery());
	}
}
