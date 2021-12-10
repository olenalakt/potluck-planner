package com.olena.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.olena.gateway.config.GatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Class that does throttling of requests
 */
@Slf4j
public class ThrottlingFilter extends ZuulFilter {

    @Autowired
    GatewayProperties gatewayProperties;

    //Create a maxUserRequestsPerSec cache where each entry expires in 1 minute and the cache is cleared every 10 seconds.
/*    private CounterCache maxUserRequestsPerSec = new CounterCache(gatewayProperties.getCounterTimeToLive()
            , gatewayProperties.getCounterTimerInterval(), gatewayProperties.getMaxItems());*/
    private CounterCache maxUserRequestsPerSec = new CounterCache(60,10, 10000);

    public String filterType() {

        return "pre";
    }

    public int filterOrder() {

        return 2;
    }

    public boolean shouldFilter() {

        return true;
    }

    @Override
    public Object run() {

        log.info("Executing Throttling Filter");

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        //Avoid throttling the token endpoint
        if (request.getRequestURI().startsWith("/token")) {
            return null;
        }

        //Get the value of the Authorization header.
        String authHeader = request.getHeader("Authorization");

        //If the Authorization  header doesn't exist or is not in a valid format.
        if (StringUtils.isEmpty(authHeader)) {
            log.warn("No auth header found, not throttling");
            return null;
        }

        //Get the value of the token by splitting the Authorization header
        String key = authHeader.split("Bearer ")[1];

        log.info("Checking key.." + key);

        Object count = maxUserRequestsPerSec.get(key);

        //If key doesn't exist in cache.
        if (count == null) {
            log.info("Counter doesn't exist, putting count as 1");
            //Put count to cache as 1
            synchronized (key) {
                maxUserRequestsPerSec.put(key, 1);
            }
        }
        //If count is greater than or equal to maxUserRequestsPerSec
        else if ((int) count >= gatewayProperties.getMaxUserRequestsPerSec()) {
            log.info("Counter is greater than {}. Returning error",  gatewayProperties.getMaxUserRequestsPerSec());
            //Quota exceeded. Return error
            handleError(requestContext);
        }
        else {
            log.info("Current count is " + (int)count + " incrementing by 1");
            //Increment maxUserRequestsPerSec by 1
            synchronized (key) {
                maxUserRequestsPerSec.put(key, (int)count + 1);
            }
        }

        return null;
    }

    private void handleError(RequestContext requestContext) {

        requestContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
        requestContext.setResponseBody("{\"error\": true, \"reason\":\"Request Throttled.\"}");
        requestContext.setSendZuulResponse(false);
    }
}
