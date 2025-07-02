package in.tss.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.tss.main.entities.User;
import in.tss.main.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public boolean registerUser(User user) {
	    // Optional: Check for duplicate email
	    if (userRepository.findByEmail(user.getEmail()) != null) {
	        return false; // Email already exists
	    }

	    try {
	        // ✅ Hash the password before saving
	        String hashedPassword = passwordEncoder.encode(user.getPassword());
	        user.setPassword(hashedPassword);

	        userRepository.save(user); // Save with hashed password
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public User loginUser(String email, String password) 
	{
		User validUser=userRepository.findByEmail(email);
		
		if (validUser != null && passwordEncoder.matches(password, validUser.getPassword()))
		{
			return validUser;
		}
		return null;
		
	}

}
