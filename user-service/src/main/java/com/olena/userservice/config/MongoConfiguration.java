package com.olena.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfiguration {

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToTimestampConverter.INSTANCE);
        converters.add(TimestampToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    public enum DateToTimestampConverter implements Converter<Date, Timestamp> {

        INSTANCE;

        @Override
        public Timestamp convert(Date source) {
            return Timestamp.from(source.toInstant());
        }

    }

    enum TimestampToDateConverter implements Converter<Timestamp, Date> {

        INSTANCE;

        @Override
        public Date convert(Timestamp source) {
            return Date.from(source.toInstant());
        }

    }

}
