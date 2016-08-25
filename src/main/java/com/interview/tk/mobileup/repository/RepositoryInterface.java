package com.interview.tk.mobileup.repository;

import com.interview.tk.mobileup.specification.Specification;
import java.util.List;

/**
 * Generic repository interface, waiting to be implemented depending on the domain.
 * @author teekay
 *
 * @param <T> The template waiting to be specified which database domain is being implemented.
 */
public interface RepositoryInterface<T>
{
	public void add(T item);

	public void add(Iterable<T> items);

	public void update(T item);

	public void remove();
	
	public void remove(Specification specification);

	public List<T> query();
	
	public List<T> query(Specification specification);
	
	public List<T> queryOrder(Specification specification);
}