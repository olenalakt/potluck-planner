package com.olena.menuservice.controller;

import com.olena.menuservice.exception.ServiceException;
import com.olena.menuservice.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/menu")
// TODO -  get rid of @CrossOrigin -  should use CORS regex external config instead
@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {

    @Autowired
    MenuService menuService;

    /**
     * get  event by  id
     *
     * @param eventId
     * @return
     * @throws ServiceException
     */
    //    @PreAuthorize("#oauth2.hasScope('user')")
    @RequestMapping(value = "/{eventid}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventMenu(@PathVariable("eventid") final String eventId) throws ServiceException {
        // returns event menu
        return ResponseEntity.ok(menuService.getEventMenu(eventId));
    }

}
