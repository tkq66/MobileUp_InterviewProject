package com.interview.tk.mobileup.specification;

/**
 * This class is to be implemented depending on the nature of the query. 
 * 
 * The toSQLQuery method is to be override, providing different SQL query 
 * for different specification.
 * 
 * The isSatisfiedBy method is to be override, providing the implementation that checks,
 * for the relevant entity, does the object's property met the given specification.
 * 
 * The and method is to be when two specification are to be applied together, using
 * the AND operation. This is to be overrided on the abstract layer of this implementation
 * 
 * The or method is to be when two specification are to be applied together, using
 * the OR operation. This is to be overrided on the abstract layer of this implementation
 * 
 * The not method is to be when two specification are to be applied together, using
 * the NOT operation. This is to be overrided on the abstract layer of this implementation
 * @author teekay
 *
 */
public interface Specification<T>
{
	public Specification<T> and(Specification<T> otherSpecification);
	public Specification<T> or(Specification<T> otherSpecification);
	public Specification<T> not();
	public boolean isSatisfiedBy(T t);
	public String toSQLQuery();
}
