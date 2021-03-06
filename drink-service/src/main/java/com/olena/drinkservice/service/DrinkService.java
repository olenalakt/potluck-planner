package com.olena.drinkservice.service;

import com.olena.drinkservice.config.DrinkServiceProperties;
import com.olena.drinkservice.enums.ActionEnum;
import com.olena.drinkservice.exception.ServiceException;
import com.olena.drinkservice.model.DrinkDTO;
import com.olena.drinkservice.model.DrinkMessage;
import com.olena.drinkservice.producer.PotluckEventPublisher;
import com.olena.drinkservice.repository.DrinkRepository;
import com.olena.drinkservice.repository.entity.Drink;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
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

    @Autowired
    private Producer<String, DrinkMessage> potluckEventProducer;

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
    public void updateDrinks(DrinkDTO[] drinkDTOList, PotluckEventPublisher potluckEventPublisher) throws ServiceException {
        log.debug("updateDrinks: guestDTO={}", drinkDTOList);


        for (DrinkDTO drinkDTO : drinkDTOList) {

            try {

                Drink drink = new Drink(drinkDTO, drinkServiceProperties);
                Drink drinkExisting = drinkRepository.findFirstByGuestIdAndDrinkName(UUID.fromString(drinkDTO.getGuestId()), drinkDTO.getDrinkName());
                log.debug("updateDrinks: drinkExisting={}", drinkExisting);

                // check if drinkDTO  already  exists  -  update
                if (drinkExisting != null) {
                    drink.setId(drinkExisting.getId());
                    drink.setDrinkId(drinkExisting.getDrinkId());
                }
                // save to  DB
                setDrink(drink);

                // publish to  Kafka topic
                DrinkMessage drinkMessage = new DrinkMessage(drink, ActionEnum.ADD);
                potluckEventPublisher.publish(potluckEventProducer, drinkServiceProperties.getPotluckEventProducerTopic(), drinkMessage);

            } catch (ServiceException se) {
                throw se;
            } catch (Exception e) {
                StringBuffer errMsg = new StringBuffer();
                errMsg.append("Failed to process drinks: ").append(e);
                log.error("updateDrinks: drinkDTO={}, {}", drinkDTO, errMsg);
                throw new ServiceException(errMsg.toString());
            }

        }
    }

    /**
     * @param drinkDTOList
     * @return
     * @throws ServiceException
     */
    public void deleteDrinks(DrinkDTO[] drinkDTOList, PotluckEventPublisher potluckEventPublisher) throws ServiceException {
        log.debug("deleteDrinks: drinkDTOList={}", drinkDTOList);


        StringBuffer errMsg = new StringBuffer();
        Drink drink;
        for (DrinkDTO drinkDTO : drinkDTOList) {

            try {

                if (drinkDTO.getDrinkId() == null) {
                    errMsg.append("drinkId is missing: ");
                    log.error("deleteDrinks: drinkDTO={}, {}", drinkDTO, errMsg);
                    throw new ServiceException(errMsg.toString());
                }

                drink = drinkRepository.findFirstByDrinkId(UUID.fromString(drinkDTO.getDrinkId()));
                if (drink != null) {
                    drinkRepository.delete(drink);

                    // publish to  Kafka topic
                    DrinkMessage drinkMessage = new DrinkMessage(drink, ActionEnum.DELETE);
                    potluckEventPublisher.publish(potluckEventProducer, drinkServiceProperties.getPotluckEventProducerTopic(), drinkMessage);
                }

            } catch (ServiceException se) {
                throw se;
            } catch (Exception e) {
                errMsg.append("Failed to delete drinks: ").append(e);
                log.error("deleteDrinks: drinkDTO={}, {}", drinkDTO, errMsg);
                throw new ServiceException(errMsg.toString());
            }

        }
    }

    /**
     * @param guestId
     * @return
     * @throws ServiceException
     */
    public List<Drink> deleteDrinksByGuestId(UUID guestId, PotluckEventPublisher potluckEventPublisher) throws ServiceException {
        log.debug("deleteDrinksByGuestId: guestId={}", guestId);
        try {
            List<Drink> drinkList = drinkRepository.findAllByGuestIdOrderByDrinkName(guestId);

            for (Drink drink : drinkList) {
                drinkRepository.delete(drink);

                // publish to Kafka topic
                DrinkMessage drinkMessage = new DrinkMessage(drink, ActionEnum.DELETE);
                potluckEventPublisher.publish(potluckEventProducer, drinkServiceProperties.getPotluckEventProducerTopic(), drinkMessage);
            }
            return drinkList;
        } catch (Exception e) {
            String errMsg = e.toString();
            log.error("deleteDrinksByGuestId: guestId={}, {}", guestId, errMsg);
            throw new ServiceException(errMsg);
        }
    }

}
