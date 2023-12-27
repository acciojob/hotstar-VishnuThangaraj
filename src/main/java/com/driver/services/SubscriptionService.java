package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){
        int userId = subscriptionEntryDto.getUserId();
        User user = userRepository.findById(userId).orElse(null);

        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setUser(user);
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());

        // Calculate amount to be paid
        SubscriptionType subscriptionType = subscription.getSubscriptionType();
        int amount = 0;
        if(subscriptionType.toString().equals("BASIC")){
            amount = 500 + (200 * subscription.getNoOfScreensSubscribed());
        }
        else if(subscriptionType.toString().equals("PRO")){
            amount = 800 + (250 * subscription.getNoOfScreensSubscribed());
        }
        else{
            amount = 1000 + (350 * subscription.getNoOfScreensSubscribed());
        }

        subscription.setTotalAmountPaid(amount);

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        user.setSubscription(subscription);
        userRepository.save(user);
        subscriptionRepository.save(subscription);

        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).orElse(null);
        Subscription subscription = user.getSubscription();

        SubscriptionType subscriptionType = subscription.getSubscriptionType();
        int amount = 0;

        if(subscriptionType.toString().equals("ElITE"))
            throw new Exception("Already the best Subscription");

        if(subscriptionType.toString().equals("BASIC")){
            int newPrice = 800 + (250 * subscription.getNoOfScreensSubscribed());
            amount = newPrice - subscription.getTotalAmountPaid();
        }

        if(subscriptionType.toString().equals("PRO")){
            int newPrice = 1000 + (350 * subscription.getNoOfScreensSubscribed());
            amount = newPrice - subscription.getTotalAmountPaid();
        }

        return amount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        Integer totalRevenue = 0;
        //subscriptions.stream().forEach(subscription -> subscription.getTotalAmountPaid() += totalRevenue);
        for(Subscription subscription : subscriptions){
            totalRevenue += subscription.getTotalAmountPaid();
        }

        return totalRevenue;
    }

}
