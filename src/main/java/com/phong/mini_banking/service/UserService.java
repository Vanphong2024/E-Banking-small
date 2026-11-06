package com.phong.mini_banking.service;
import com.phong.mini_banking.entity.*;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.phong.mini_banking.repository.UserRepository;

@Service 
public class UserService {  
private final UserRepository userRepository; 
private final BCryptPasswordEncoder passwordEncoder =
        new BCryptPasswordEncoder();
public UserService(UserRepository userRepository)
{
    this.userRepository = userRepository;

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

// login user 
public User login(String mail , String password){
    User user =userRepository.findByNormalizedEmail(normalizeEmail(mail)).orElseThrow(() ->
                        new IllegalArgumentException(
                                "mail không tồn tại"));
    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new IllegalArgumentException("Mật khẩu không đúng");
    }
    return user;
}

private String normalizeEmail(String email) {
    if (email == null || email.strip().isEmpty()) {
        throw new IllegalArgumentException("Vui lòng nhập email khách hàng.");
    }
    return email.strip().toLowerCase(Locale.ROOT);
}
}
