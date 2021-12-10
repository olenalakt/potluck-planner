package com.olena.drinkservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegexCorsConfiguration extends CorsConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<Pattern> allowedOriginsPatterns = new ArrayList<>();
    private List<String> allowedOriginsRegexes = new ArrayList<>();

    @Override
    /**
     * Check the origin of the request against the configured allowed origins.
     *
     * @param requestOrigin the origin to check
     * @return the origin to use for the response, possibly {@code null} which
     * means the request origin is not allowed
     */
    public String checkOrigin(String requestOrigin) {
        if (!StringUtils.hasText(requestOrigin)) {
            return null;
        }

        if (this.allowedOriginsRegexes.isEmpty()) {
            return null;
        }

        if (this.allowedOriginsRegexes.contains(ALL)) {
            if (!getAllowCredentials().equals(Boolean.TRUE)) {
                return ALL;
            } else {
                return requestOrigin;
            }
        }

        for (Pattern allowedOriginsPattern : this.allowedOriginsPatterns) {
            if (isMatches(requestOrigin, allowedOriginsPattern)) {
                logger.debug("{} matches regex={}", requestOrigin, allowedOriginsPattern);
                return requestOrigin;
            }
        }

        return null;
    }

    public void setAllowedOriginRegex(List<String> allowedOriginsRegexes) {
        if (allowedOriginsRegexes.contains(ALL)) {
            logger.warn("Using regex {} is very dangerous, please remove it", ALL);
        }
        this.allowedOriginsRegexes = allowedOriginsRegexes;
        for (String allowedOriginRegex : this.allowedOriginsRegexes) {
            allowedOriginsPatterns.add(createPattern(allowedOriginRegex));
            logger.info("Add allowedOrigins regex={}", allowedOriginRegex);
        }
    }

    private Pattern createPattern(String allowedOrigin) {
        String regex = this.parseAllowedWildcardOriginToRegex(allowedOrigin);
        return Pattern.compile(regex);
    }

    private boolean isMatches(String requestOrigin, Pattern pattern) {
        return pattern.matcher(requestOrigin).matches();
    }

    private String parseAllowedWildcardOriginToRegex(String allowedOrigin) {
        String regex = allowedOrigin.replace(".", "\\.");
        return regex.replace("*", ".*");
    }
}