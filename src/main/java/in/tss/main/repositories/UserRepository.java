package in.tss.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import in.tss.main.entities.User;


public interface UserRepository extends JpaRepository<User, Integer>
{
	User findByEmail(String email);
}
