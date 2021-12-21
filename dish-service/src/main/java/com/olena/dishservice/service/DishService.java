package com.olena.dishservice.service;

import com.olena.dishservice.config.DishServiceProperties;
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
    DishServiceProperties dishServiceProperties;

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

    }

    /**
     * @param dishDTOList
     * @return
     * @throws ServiceException
     */
    public void updateDishes(DishDTO[] dishDTOList) throws ServiceException {
        log.debug("updateDishes: guestDTO={}", dishDTOList);


        for (DishDTO dishDTO : dishDTOList) {

            try {

                Dish dish = new Dish(dishDTO, dishServiceProperties);
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
                errMsg.append("Failed to process dishes: ").append(e);
                log.error("updateDishes: dishDTO={}, {}", dishDTO, errMsg);
                throw new ServiceException(errMsg.toString());
            }

        }
    }

    /**
     *
     * @param guestId
     * @return
     * @throws ServiceException
     */
    public List<Dish> deleteDishesByGuestId(UUID guestId) throws ServiceException {
        log.debug("deleteDishesByGuestId: guestId={}", guestId);
        try {
            List<Dish> dishList = dishRepository.findAllByGuestIdOrderByDishName(guestId);

            for( Dish dish:  dishList) {
                dishRepository.delete(dish);
            }
            return dishList;
        } catch (Exception e) {
            String errMsg = e.toString();
            log.error("deleteDishesByGuestId: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

}
