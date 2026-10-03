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
// public User createUser(User user){
//  String encodedPassword =
//             passwordEncoder.encode(user.getPassword());

//     user.setPassword(encodedPassword);

//     return userRepository.save(user);
// }
public User createUser(User user) {

    System.out.println("PASS NHẬN ĐƯỢC: " + user.getPassword());

    String encodedPassword =
            passwordEncoder.encode(user.getPassword());

    System.out.println("PASS SAU KHI BCRYPT: " + encodedPassword);

    user.setPassword(encodedPassword);

    System.out.println("PASS TRƯỚC KHI SAVE: " + user.getPassword());

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
    User user =userRepository.findByEmail(mail).orElseThrow(() ->
                        new IllegalArgumentException(
                                "mail không tồn tại"));
    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new IllegalArgumentException("Mật khẩu không đúng");
    }
    return user;
}
}
