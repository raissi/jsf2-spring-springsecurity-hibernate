package com.iptech.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.iptech.scope.SpringViewScoped;

@Component
@SpringViewScoped
public class SomeBean implements Serializable {
	private static final long serialVersionUID = -8765440815139181532L;

	private String someProperty;

	private long time;
	private List<String> listOfNames;
	private Date dateFrom;
	private Date dateTo;

	public String getSomeProperty() {
		return (someProperty);
	}

	public void setSomeProperty(String someProperty) {
		this.someProperty = someProperty;
	}

	public String someActionControllerMethod(String name) {
		System.out.println("In method: " + name);
		return "pretty:page2"; // Means to go to page-b.xhtml (since condition
								// is not mapped in faces-config.xml)
	}

	public String someOtherActionControllerMethod() {
		return "pretty:accueil"; // Means to go to page-a.xhtml (since condition
									// is not mapped in faces-config.xml)
	}

	public void handleDateSelectFrom() {
		System.out.println("Date from: " + dateFrom);
	}

	public void handleDateSelectTo() {
		System.out.println("Date from: " + dateTo);
	}

	public void aMethod() {
		time = System.currentTimeMillis();
		listOfNames = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			listOfNames.add("Name " + i + "-" + time);
		}
	}

	public String saySomething() {
		System.out.println("SaySomething called here");
		return "something";
	}

	public List<String> getListOfNames() {
		return listOfNames;
	}

	public void setListOfNames(List<String> listOfNames) {
		this.listOfNames = listOfNames;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

}
