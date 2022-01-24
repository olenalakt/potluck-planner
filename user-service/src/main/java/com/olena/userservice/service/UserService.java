package com.olena.userservice.service;

import com.olena.userservice.config.UserServiceProperties;
import com.olena.userservice.enums.ActionEnum;
import com.olena.userservice.exception.ServiceException;
import com.olena.userservice.model.PotluckPlannerCleanup;
import com.olena.userservice.model.UserDTO;
import com.olena.userservice.producer.PotluckPlannerCleanupPublisher;
import com.olena.userservice.repository.UserRepository;
import com.olena.userservice.repository.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserServiceProperties userServiceProperties;

    @Autowired
    private Producer<String, PotluckPlannerCleanup> potluckPlannerCleanupProducer;


    @Autowired
    UserRepository userRepository;


    public User getUserFromDb(String userName) throws ServiceException {
        log.debug("getUserFromDb: userName={}", userName);

        User user = userRepository.findFirstByUserName(userName);
        if (user == null) {
            String errMsg = "User not found";
            log.error("getUserFromDb: userName={}, {}", userName, errMsg);
            throw new ServiceException(errMsg);
        }

        return user;
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

    /**
     * @param userDTO
     * @return
     * @throws ServiceException
     */
    public User addUser(UserDTO userDTO) throws ServiceException {
        log.debug("addUser: userName={}", userDTO.getUserName());

        // check if userDTO is already  exist  -  reject
        User existingUser = userRepository.findFirstByUserName(userDTO.getUserName());
        if (existingUser != null) {
            // TODO -  remove, temporary fix the existing users
            if (existingUser.getUserId() == null) {
                existingUser.setUserId(UUID.randomUUID());
                setUser(existingUser);
            }
            StringBuffer errMsg = new StringBuffer();
            errMsg.append("User already exists in DB");
            log.error("addUser: userDTO={}, {}", userDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }

        try {
            // generate UUID for new user
            userDTO.setUserId(UUID.randomUUID().toString());
            // construct Event from EventDTO
            User user = new User(userDTO, ActionEnum.ADD, userServiceProperties);
            setUser(user);

            return user;
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            StringBuffer errMsg = new StringBuffer();
            errMsg.append("Failed to map user: ").append(e);
            log.error("addUser: userDTO={}, {}", userDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }
    }

    /**
     * @param userDTO
     * @return
     * @throws ServiceException
     */
    public User updateUser(UserDTO userDTO) throws ServiceException {
        log.debug("updateUser: userDTO={}", userDTO);

        StringBuffer errMsg = new StringBuffer();
        try {

            User user = new User(userDTO, ActionEnum.UPDATE, userServiceProperties);
            // verify user exists
            User userExisting = userRepository.findFirstByUserId(UUID.fromString(userDTO.getUserId()));

            // check if userDTO  already  exists  -  update
            if (userExisting != null) {

                // set PK
                user.setId(userExisting.getId());
                // save to  DB
                setUser(user);

                // TODO - publish user into Kafka topic
                //eventPublisher.publish(potluckEventProducer, kafkaProperties.getPotluckEventProducerTopic(), user);

                return user;

            } else {
                errMsg.append("User not found: ").append(userDTO.getUserId());
                throw new ServiceException(errMsg.toString());
            }

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            errMsg.append("Failed to update  user: ").append(e);
            log.error("updateUser: userDTO={}, {}", userDTO, errMsg);
            throw new ServiceException(errMsg.toString());
        }
    }

    /**
     * @param userId
     * @param potluckPlannerCleanupPublisher
     * @return
     * @throws ServiceException
     */
    public User deleteUser(String userId, PotluckPlannerCleanupPublisher potluckPlannerCleanupPublisher) throws ServiceException {
        log.debug("deleteUser: userId={}", userId);

        StringBuffer errMsg = new StringBuffer();
        try {
            User user = userRepository.findFirstByUserId(UUID.fromString(userId));

            if (user != null) {

                userRepository.delete(user);

                PotluckPlannerCleanup potluckPlannerCleanupUser = new PotluckPlannerCleanup(user);
                potluckPlannerCleanupPublisher.publish(potluckPlannerCleanupProducer, userServiceProperties.getPotluckPlannerCleanupProducerTopic(), potluckPlannerCleanupUser);

                return user;

            } else {
                errMsg.append("User not found: ").append(userId);
                throw new ServiceException(errMsg.toString());
            }

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("deleteUser: userId={}, {}", userId, errMsg);
            throw new ServiceException(errMsg.toString());
        }

    }


    /**
     * Backdoor service to  trigger  adhoc cleanup
     * @param userName
     * @param potluckPlannerCleanupPublisher
     * @return
     * @throws ServiceException
     */
    public User deleteUserByName(String userName, PotluckPlannerCleanupPublisher potluckPlannerCleanupPublisher) throws ServiceException {
        log.debug("deleteUserByName: userName={}", userName);

        StringBuffer errMsg = new StringBuffer();
        Boolean isUserNotFound = true;
        try {
            User user = userRepository.findFirstByUserId(UUID.fromString(userName));

            if (user != null) {
                isUserNotFound = false;
                userRepository.delete(user);
            }

            // send message even if user not found  to  cleanup  downstream systems
            PotluckPlannerCleanup potluckPlannerCleanupUser = new PotluckPlannerCleanup(userName);
            potluckPlannerCleanupPublisher.publish(potluckPlannerCleanupProducer, userServiceProperties.getPotluckPlannerCleanupProducerTopic(), potluckPlannerCleanupUser);

            if (isUserNotFound) {
                errMsg.append("User not found, cleanup abandoned data triggered for userName=").append(userName);
                throw new ServiceException(errMsg.toString());
            } else {
                return user;
            }

        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("deleteUserByName: userName={}, {}", userName, errMsg);
            throw new ServiceException(errMsg.toString());
        }
    }
}
