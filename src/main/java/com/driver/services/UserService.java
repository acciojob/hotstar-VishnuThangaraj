package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){
        User u = new User();
        u.setName(user.getName());
        u.setAge(user.getAge());
        u.setSubscription(user.getSubscription());
        u.setMobNo(user.getMobNo());
        userRepository.save(user);
        //Jut simply add the user to the Db and return the userId returned by the repository
        return u.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        User user = userRepository.findById(userId).orElse(null);
        SubscriptionType type = user.getSubscription().getSubscriptionType();
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();

        int count = 0;
        for(WebSeries webSeries : webSeriesList){
            if(webSeries.getSubscriptionType() == type && user.getAge() >= webSeries.getAgeLimit())
                count++;
        }

        return count;
    }


}
