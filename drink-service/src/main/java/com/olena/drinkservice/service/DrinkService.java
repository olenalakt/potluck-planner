package com.olena.drinkservice.service;

import com.olena.drinkservice.config.DrinkServiceProperties;
import com.olena.drinkservice.exception.ServiceException;
import com.olena.drinkservice.model.DrinkDTO;
import com.olena.drinkservice.repository.DrinkRepository;
import com.olena.drinkservice.repository.entity.Drink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DrinkService {

    @Autowired
    DrinkServiceProperties drinkServiceProperties;

    @Autowired
    DrinkRepository drinkRepository;

    /**
     * @param guestId
     * @return
     * @throws ServiceException
     */
    public List<Drink> getDrinkListByGuestId(UUID guestId) throws ServiceException {
        log.debug("getDrinkListByGuestId: guestId={}", guestId);
        try {
            List<Drink> drinkList = drinkRepository.findAllByGuestIdOrderByDrinkName(guestId);
            return drinkList;
        } catch (Exception e) {
            String errMsg = e.toString();
            log.error("getDrinkListByGuestId: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @param drink
     * @return
     * @throws ServiceException
     */
    public void setDrink(Drink drink) throws ServiceException {
        log.debug("setDrink: drink={}", drink.toString());
        try {
            drinkRepository.save(drink);
        } catch (Exception e) {
            String errMsg = "Failed to update DB: " + e;
            log.error("setDrink: drink={}, {}", drink, errMsg);
            throw new ServiceException(errMsg);
        }

        //TODO: push drink  asynchronously  to  the Kafka queue for processing by  other services
        //            inventoryClient.updateInventory(drink.getItems());
        //          drink.setOrderId(UUID.randomUUID().toString());


    }

    /**
     * @param drinkDTOList
     * @return
     * @throws ServiceException
     */
    public void addDrinks(DrinkDTO[] drinkDTOList) throws ServiceException {
        log.debug("addDrinks: guestDTO={}", drinkDTOList);


        for (DrinkDTO drinkDTO : drinkDTOList) {

            try {

                Drink drink = new Drink(drinkDTO, drinkServiceProperties);
                Drink drinkExisting = drinkRepository.findFirstByDrinkName(UUID.fromString(drinkDTO.getGuestId()), drinkDTO.getDrinkName());

                // check if drinkDTO  already  exists  -  update
                if (drinkExisting != null) {
                    drink.setId(drinkExisting.getId());
                }
                // save to  DB
                setDrink(drink);

            } catch (ServiceException se) {
                throw se;
            } catch (Exception e) {
                StringBuffer errMsg = new StringBuffer();
                errMsg.append("Failed to process drinks: ").append(e);
                log.error("addDrinks: drinkDTO={}, {}", drinkDTO, errMsg);
                throw new ServiceException(errMsg.toString());
            }

        }
    }


}
