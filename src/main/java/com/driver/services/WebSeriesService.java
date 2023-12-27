package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).
                orElse(null);

        if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()) == null)
            throw new Exception("Series is already present");

        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        webSeries.setProductionHouse(productionHouse);
        webSeriesRepository.save(webSeries);
        productionHouse.getWebSeriesList().add(webSeries);
        productionHouseRepository.save(productionHouse);

        double ratings = 0;
        int count = 0;

        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();

        for(WebSeries webSeries1 : webSeriesList){
            ratings += webSeries1.getRating();
            count++;
        }

        productionHouse.setRatings(ratings / (count*1.0));

        return productionHouse.getWebSeriesList().size();
    }

}
