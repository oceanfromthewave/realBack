package practice.phase13.repository;

import org.springframework.data.jpa.domain.Specification;
import practice.phase12.entity.User;

public class UserSpecifications
{
	public static Specification<User> nameContains(String keyword)
	{
		return ((root, query, cb) -> cb.like(root.get("name"), "%" + keyword + "%"));
	}

	public static Specification<User> emailEndsWith(String suffix)
	{
		return (root, query, cb) -> cb.like(root.get("email"), "%" + suffix);
	}
}
