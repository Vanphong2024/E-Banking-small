package com.phong.mini_banking.service;
import com.phong.mini_banking.entity.*;
import java.util.List;
import java.util.Optional;
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
    //add user 
public User createUser(User user){
return userRepository.save (user);
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
}
