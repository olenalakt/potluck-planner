package com.olena.userservice.controller;

import com.olena.userservice.exception.BadInputException;
import com.olena.userservice.exception.ServiceException;
import com.olena.userservice.model.UserDTO;
import com.olena.userservice.producer.PotluckPlannerCleanupPublisher;
import com.olena.userservice.service.UserService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PotluckPlannerCleanupPublisher potluckPlannerCleanupPublisher;

    /**
     * @param userName
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("name") String userName) throws ServiceException {

        return ResponseEntity.ok(userService.getUserFromDb(userName));
    }

    /**
     * @param userDTO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws ServiceException, BadInputException {

        // userName and userRole can not be null -  check only  if pwd provided
        if (StringUtils.isBlank(userDTO.getPassword())) {
            throw new BadInputException("createUser: empty password");
        } else {
            return ResponseEntity.ok(userService.addUser(userDTO));
        }
    }

    /**
     * @param userDTO
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) throws ServiceException, BadInputException {

        if (!StringUtils.isBlank(userDTO.getUserId())) {
            return ResponseEntity.ok(userService.updateUser(userDTO));
        } else {
            throw new BadInputException("updateUser: userId can not be null");
        }
    }

    /**
     * @param userId
     * @return
     */
//    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{userid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("userid") String userId) throws ServiceException, BadInputException {

        if (!StringUtils.isBlank(userId)) {
            return ResponseEntity.ok(userService.deleteUser(userId, potluckPlannerCleanupPublisher));
        } else {
            throw new BadInputException("deleteUser: userId can not be null");
        }
    }

    @RequestMapping(value = "/username/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserByUserName(@PathVariable("username") String userName) throws ServiceException, BadInputException {

        if (!StringUtils.isBlank(userName)) {
            return ResponseEntity.ok(userService.deleteUserByName(userName, potluckPlannerCleanupPublisher));
        } else {
            throw new BadInputException("deleteUserByUserName: userName can not be null");
        }
    }

    @RequestMapping(value = "/missingInt", method = RequestMethod.GET)
    public ResponseEntity<?> getMissingInt(@RequestParam("intList") int[] intList) throws ServiceException, BadInputException {

        Integer  result =  -1;

        if (intList.length <= 0){
            // exit if customerQuery is shorter then 2 chars
            return ResponseEntity.ok(result);
        }
/*
        intList.sort((a,b) -> a.compareTo(b));
        log.debug("intList after sort:{}", intList);

        // find missing -  O(N)
        for (int i = 0; i < intList.size() - 1; i++) {
            if (intList.get(i+1) - intList.get(i)  > 1) {
                result =  intList.get(i) + 1;
                break;
            }
        }
*/

        Arrays.sort(intList);
        log.debug("intList after sort:{}", intList);

        // find missing -  O(N)
        for (int i = 0; i < intList.length - 1; i++) {
            if (intList[i+1] - intList[i]  > 1) {
                result = intList[i] + 1;
                break;
            }
        }
        if (intList[0] > 1) {
            result = 1;
        }


/*        boolean isFound;
        loop1:
        for (int i = 1; i <= intList.size(); i++) {

            isFound = false;
            loop2:
            for (int k = 0; k < intList.size(); k++) {
                if (intList.get(k) == i) {
                    isFound = true;
                    break loop2;
                }
            }
            if (!isFound) {
                result = i;
                break loop1;
            }
        }*/


        return ResponseEntity.ok(result);
    }

    int binarySearch(int[] list, int value, int low, int high) {
          int result = -1;

          while (low <= high )  {
              result =  ( low + high)  / 2;
              int middle = list[result];
              if (middle > value || middle ==  -1) {
                  high =  result - 1;
              } else if (middle < value) {
                  low =  result + 1;
              } else {
                  break;
              }
          }

          return result;
    }

    @RequestMapping(value = "/test/{searchString}", method = RequestMethod.GET)
    public ResponseEntity<?> testSearch(@PathVariable("searchString") String customerQuery, @RequestParam("strlist") List<String> repository) throws ServiceException, BadInputException {

     //   List<String> repository = Arrays.asList("mObile", "MOUSE", "moneypot", "moniTor", "mousepad");

        // initialize result array
        List<List<String>> result = new ArrayList<>();

        if (customerQuery.length() < 2 || repository == null){
            // exit if customerQuery is shorter then 2 chars
            return ResponseEntity.ok(result);
        }

        // sort original list: O(N log N)
        // convert to  lower case
        List<String> repositoryLower = repository.stream().parallel().map(s -> s.toLowerCase()).collect(Collectors.toList());
        log.debug("repository after map:{}", repositoryLower);

        // sort original list
        repositoryLower.sort((a,b) -> a.compareTo(b));
        log.debug("repository after sort:{}", repositoryLower);

        // initialize vars
        StringBuffer keyString =  new StringBuffer();
        keyString.append(customerQuery.substring(0, 2).toLowerCase());

        List<String> matchingList = repositoryLower.stream().parallel().filter(s -> s.startsWith(keyString.toString())).collect(Collectors.toList());
        List<String> tmpList = getTmpList(matchingList, 3);
        result.add(tmpList);

        //
        for ( int i = 3; i <= customerQuery.length() && matchingList.size() != 0; i++) {

            // ensure case is ignored
            keyString.append(customerQuery.substring(i-1, i).toLowerCase());
            matchingList = matchingList.stream().parallel().filter(s -> s.startsWith(keyString.toString())).collect(Collectors.toList());
            if (matchingList.size() == 0) {
                break;
            }
            log.debug("keyString:{}", keyString);
            log.debug("i={}, matchingList:{}", i, matchingList);

            tmpList = getTmpList(matchingList, 3);
            result.add(tmpList);
        }

            return ResponseEntity.ok(result);
    }

    private List<String> getTmpList(List<String> matchingList, int maxTmpListSize) {

        List<String> tmpList = new ArrayList<>();
        for (int k = 0; k < maxTmpListSize && k < matchingList.size() ; k++){
            tmpList.add(matchingList.get(k));
        }
        return tmpList;
    }
}
