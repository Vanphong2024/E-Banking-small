package com.phong.mini_banking.service;
import com.phong.mini_banking.entity.*;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phong.mini_banking.repository.UserRepository;

@Service 
public class UserService {  
private final UserRepository userRepository; 
private final PasswordEncoder passwordEncoder;
public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
{
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
}
 // list  user
 public List<User> getAllUsers(){
    return userRepository.findAll();
 }
    // find user theo id 
    public Optional<User> getUsersById(Long id){
        return userRepository.findById(id);
    }

public User createUser(User user) {
 String email = normalizeEmail(user.getEmail());
 if (userRepository.findByNormalizedEmail(email).isPresent()) {
     throw new IllegalArgumentException("Email khách hàng đã tồn tại.");
 }
 user.setEmail(email);
 user.setRole(Role.CUSTOMER);
 String encodedPassword =passwordEncoder.encode(user.getPassword());
user.setPassword(encodedPassword);
return userRepository.save(user);
}
//update user 
public User updateUser(User user){
    String endcodePassword = passwordEncoder.encode(user.getPassword ());
    user.setPassword(endcodePassword);
    return userRepository.save(user);

}
//delet user 
public void deleteUser(Long id ){
    userRepository.deleteById(id);
}

private String normalizeEmail(String email) {
    if (email == null || email.strip().isEmpty()) {
        throw new IllegalArgumentException("Vui lòng nhập email khách hàng.");
    }
    return email.strip().toLowerCase(Locale.ROOT);
}
}
