package com.facecloud.facecloudserverapp.service;

import com.facecloud.facecloudserverapp.entity.Location;
import com.facecloud.facecloudserverapp.exception.DataValidationException;
import com.facecloud.facecloudserverapp.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.lucene.util.SloppyMath.haversinMeters;


@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;


    public void addNewLocation(String xml) {
    }

    public String getAllLocationsXmlForUser(String username) {
        return null;
    }

    public String getStatistics(String userName) throws TransformerException, ParserConfigurationException {
        List<Location> locationList = locationRepository.findAllByUserName(userName);

        if(locationList.size() == 0) {
            throw new TransformerException("dany uzytkownik nie ma historii w tabeli");
        }

        Location firstPoint = locationList.get(0);
        Location lastPoint = locationList.get(locationList.size() - 1);

        double allDistance = haversinMeters(firstPoint.getLatitude(), firstPoint.getLongitude(), lastPoint.getLatitude(), lastPoint.getLongitude());
        long allDaysUsingApp = ChronoUnit.DAYS.between(firstPoint.getTime(), lastPoint.getTime()) + 1;

        List<Location> locationListFromToday = locationList.stream().filter(location ->
                (location.getTime().getYear() == LocalDateTime.now().getYear() &&
                        location.getTime().getMonth().equals(LocalDateTime.now().getMonth()) &&
                        location.getTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
        ).collect(Collectors.toList());

        double todayDistance;
        LocalDateTime todayUsingApp;

        if(locationListFromToday.size() != 0) {
            Location firstPointToday = locationListFromToday.get(0);
            Location lastPointToday = locationListFromToday.get(locationListFromToday.size() - 1);

            todayDistance = haversinMeters(firstPointToday.getLatitude(), firstPointToday.getLongitude(),
                    lastPointToday.getLatitude(), lastPointToday.getLongitude());
            todayUsingApp = LocalDateTime.of(1,1,1,
                    new Date((int) ChronoUnit.SECONDS.between(firstPoint.getTime(), lastPoint.getTime()) * 1000).getHours(),
                    new Date((int) ChronoUnit.SECONDS.between(firstPoint.getTime(), lastPoint.getTime()) * 1000).getMinutes(),
                    new Date((int) ChronoUnit.SECONDS.between(firstPoint.getTime(), lastPoint.getTime()) * 1000).getSeconds());
        } else {
            todayDistance = 0;
            todayUsingApp = LocalDateTime.of(1,1,1,0,0,0);
        }

        return parseToXmlStatistics(allDistance, allDaysUsingApp, todayDistance, todayUsingApp);
    }

    public String parseToXmlStatistics(double allDistance, long allDaysUsingApp, double todayDistance, LocalDateTime todayUsingApp) throws ParserConfigurationException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder().newDocument();
        Element root = document.createElement("statistics");
        document.appendChild(root);

        Element today = document.createElement("today");
        root.appendChild(today);

        Element distance1 = document.createElement("distance");
        distance1.appendChild(document.createTextNode(Double.toString(todayDistance)));
        today.appendChild(distance1);

        Element time = document.createElement("time");
        time.appendChild(document.createTextNode(todayUsingApp.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        today.appendChild(time);

        Element all = document.createElement("all");
        root.appendChild(all);

        Element distance2 = document.createElement("distance");
        distance2.appendChild(document.createTextNode(Double.toString(allDistance)));
        all.appendChild(distance2);

        Element days = document.createElement("days");
        days.appendChild(document.createTextNode(Long.toString(allDaysUsingApp)));
        all.appendChild(days);

        return transformDocumentToString(document);
    }

    private String transformDocumentToString(Document document) {
        return null;
    }
}
