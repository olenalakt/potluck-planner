package com.olena.userservice.service;

import com.olena.userservice.config.UserServiceConfig;
import com.olena.userservice.exception.ServiceException;
import com.olena.userservice.model.UserDTO;
import com.olena.userservice.repository.UserRepository;
import com.olena.userservice.repository.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserServiceConfig userServiceConfig;

    @Autowired
    UserRepository userRepository;


    public UserDTO getUserFromDb(String userName) throws ServiceException {
        log.debug("getUserFromDb: userName={}", userName);

        User user = userRepository.findFirstByUserName(userName);
        if (user == null) {
            String errMsg = "User not found";
            log.error("getUserFromDb: userName={}, {}", userName, errMsg);
            throw new ServiceException(errMsg);
        }

        return new UserDTO(user);
    }

    /**
     * @param user
     * @return
     * @throws ServiceException
     */
    public void setUser(User user) throws ServiceException {
        log.debug("setUser: user={}", user.toString());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            String errMsg = "Failed to update DB: " + e;
            log.error("setUser: user={}, {}", user, errMsg);
            throw new ServiceException(errMsg);
        }

        //TBD: push event  asynchronously  to  the Kafka queue for processing by  other services
        //            inventoryClient.updateInventory(user.getItems());
        //          user.setOrderId(UUID.randomUUID().toString());


    }

    private User mapToUser(UserDTO userDTO) throws ServiceException {

        User user = new User(userDTO, userServiceConfig);
        log.debug("addUser: user={}", user);

        return user;
    }

    /**
     * @param userDTO
     * @return
     * @throws ServiceException
     */
    public void addUser(UserDTO userDTO) throws ServiceException {
        log.debug("addUser: userName={}", userDTO.getUserName());

        // check if userDTO is already  exist  -  reject
        if (userRepository.findFirstByUserName(userDTO.getUserName()) != null) {
            StringBuffer errMsg = new StringBuffer();
            errMsg.append("User already exists in DB");
            log.error("addUser: userDTO={}, {}", userDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }

        try {
            User user = mapToUser(userDTO);

            setUser(user);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            StringBuffer errMsg = new StringBuffer();
            errMsg.append("Failed to map user: ").append(e);
            log.error("addUser: userDTO={}, {}", userDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }
    }


}
