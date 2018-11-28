package me.potato.demooauth2.Users;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class UserService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public void deletebyId(Long id) {
		userRepository.deleteById(id);
	}

	@PostConstruct
	public void init() {
		Optional<User> byName = userRepository.findByUsername("potato");
		if (!byName.isPresent()) {
			User potato = new User();
			potato.setUsername("potato");
			potato.setPassword("potato123");

			User saved = this.save(potato);
			log.info(saved.toString());
		}
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> byName = userRepository.findByUsername(username);
		if (!byName.isPresent())
			throw new UsernameNotFoundException(username);

		User user = byName.get();
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAutorities());
	}

	private Collection<? extends GrantedAuthority> getAutorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
}
