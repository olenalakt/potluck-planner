package com.olena.drinkservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.olena.drinkservice.enums.ActionEnum;
import com.olena.drinkservice.enums.Constants;
import com.olena.drinkservice.repository.entity.Drink;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DrinkMessage {

    private String userName;
    private String eventId;

    private Drink drink;

    private String messageType;
    private String actionType;

    public DrinkMessage(Drink drink, ActionEnum action) {

        this.userName = drink.getUserName();
        this.eventId = drink.getEventId().toString();

        this.drink = drink;

        this.messageType = Constants.DRINK.getValue();
        this.actionType = action.getCode();

    }
}
