package com.ewolff.microservice.order.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.atom.Person;
import com.rometools.rome.feed.synd.SyndPerson;

public class OrderAtomFeedView extends AbstractAtomFeedView {

	private OrderRepository orderRepository;

	@Autowired
	public OrderAtomFeedView(OrderRepository orderRepository) {
		super();
		this.orderRepository = orderRepository;
	}

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Feed feed, HttpServletRequest request) {
		feed.setId("tag:ewolff.com/microservice-atom/order");
		feed.setTitle("Order");
		List<Link> alternateLinks = new ArrayList<>();
		Link link = new Link();
		link.setRel("self");
		link.setHref(baseUrl(request) + "feed");
		alternateLinks.add(link);
		List<SyndPerson> authors = new ArrayList<SyndPerson>();
		Person person = new Person();
		person.setName("Big Money Online Commerce Inc.");
		authors.add(person);
		feed.setAuthors(authors);

		feed.setAlternateLinks(alternateLinks);
		feed.setUpdated(orderRepository.lastUpdate());
		Content subtitle = new Content();
		subtitle.setValue("List of all orders");
		feed.setSubtitle(subtitle);
	}

	private String baseUrl(HttpServletRequest request) {
		return String.format("%s://%s:%d%s/", request.getScheme(), request.getServerName(), request.getServerPort(),
				request.getContextPath());
	}

	@Override
	protected List<Entry> buildFeedEntries(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<Entry> entries = new ArrayList<Entry>();
		List<Order> orderlist = (List<Order>) model.get("orders");

		for (Order o : orderlist) {
			Entry entry = new Entry();
			entry.setId("tag:ewolff.com/microservice-atom/order/" + Long.toString(o.getId()));
			entry.setUpdated(o.getUpdated());
			entry.setTitle("Order " + o.getId());
			List<Content> contents = new ArrayList<Content>();
			Content content = new Content();
			content.setSrc(baseUrl(request) + "order/" + Long.toString(o.getId()));
			content.setType("application/json");
			contents.add(content);
			entry.setContents(contents);
			Content summary = new Content();
			summary.setValue("This is the order " + o.getId());
			entry.setSummary(summary);
			entries.add(entry);
		}

		return entries;
	}

}
