package in.tss.main.services;

import in.tss.main.entities.User;

public interface UserService 
{
	public boolean registerUser(User user);
	public User loginUser(String email,String password);
}
