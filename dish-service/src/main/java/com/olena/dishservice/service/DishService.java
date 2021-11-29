package com.olena.dishservice.service;

import com.olena.dishservice.config.DishServiceConfig;
import com.olena.dishservice.exception.ServiceException;
import com.olena.dishservice.model.DishDTO;
import com.olena.dishservice.repository.DishRepository;
import com.olena.dishservice.repository.entity.Dish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DishService {

    @Autowired
    DishServiceConfig dishServiceConfig;

    @Autowired
    DishRepository dishRepository;

    /**
     * @param guestId
     * @return
     * @throws ServiceException
     */
    public List<Dish> getDishListByGuestId(UUID guestId) throws ServiceException {
        log.debug("getDishListByGuestId: guestId={}", guestId);
        try {
            List<Dish> dishList = dishRepository.findAllByGuestIdOrderByDishName(guestId);
            return dishList;
        } catch (Exception e) {
            String errMsg = e.toString();
            log.error("getDishListByGuestId: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param dish
     * @return
     * @throws ServiceException
     */
    public void setDish(Dish dish) throws ServiceException {
        log.debug("setDish: dish={}", dish.toString());
        try {
            dishRepository.save(dish);
        } catch (Exception e) {
            String errMsg = "Failed to update DB: " + e;
            log.error("setDish: dish={}, {}", dish, errMsg);
            throw new ServiceException(errMsg);
        }

        //TODO: push dish  asynchronously  to  the Kafka queue for processing by  other services
        //            inventoryClient.updateInventory(dish.getItems());
        //          dish.setOrderId(UUID.randomUUID().toString());


    }

    /**
     * @param dishDTOList
     * @return
     * @throws ServiceException
     */
    public void addDishes(DishDTO[] dishDTOList) throws ServiceException {
        log.debug("addDishes: guestDTO={}", dishDTOList);


        for (DishDTO dishDTO : dishDTOList) {

            try {

                Dish dish = new Dish(dishDTO, dishServiceConfig);
                Dish dishExisting = dishRepository.findFirstByDishName(UUID.fromString(dishDTO.getGuestId()), dishDTO.getDishName());

                // check if dishDTO  already  exists  -  update
                if (dishExisting != null) {
                    dish.setId(dishExisting.getId());
                }
                // save to  DB
                setDish(dish);

            } catch (ServiceException se) {
                throw se;
            } catch (Exception e) {
                StringBuffer errMsg = new StringBuffer();
                errMsg.append("Failed to map dishes: ").append(e);
                log.error("addDishes: dishDTO={}, {}", dishDTO, errMsg);
                throw new ServiceException(errMsg.toString());
            }

        }
    }


}
