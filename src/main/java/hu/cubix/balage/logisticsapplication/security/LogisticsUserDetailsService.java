package hu.cubix.balage.logisticsapplication.security;

import hu.cubix.balage.logisticsapplication.model.Users;
import hu.cubix.balage.logisticsapplication.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LogisticsUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public LogisticsUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		return new LogisticsUser(username, user.getPassword(), user.getRoles().stream().map(SimpleGrantedAuthority::new).toList());
	}
}