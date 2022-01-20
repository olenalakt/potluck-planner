package com.olena.dishservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.olena.dishservice.enums.ActionEnum;
import com.olena.dishservice.enums.Constants;
import com.olena.dishservice.repository.entity.Dish;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DishMessage {

    private String userName;
    private String eventId;

    private Dish dish;

    private String messageType;
    private String actionType;

    public DishMessage(Dish dish, ActionEnum action) {

        this.userName = dish.getUserName();
        this.eventId = dish.getEventId().toString();

        this.dish = dish;

        this.messageType = Constants.DISH.getValue();
        this.actionType = action.getCode();

    }
}
