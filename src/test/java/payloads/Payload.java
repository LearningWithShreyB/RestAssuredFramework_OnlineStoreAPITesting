package payloads;

import java.util.Random;

import com.github.javafaker.Faker;

import pojo.ProductPOJO;

public class Payload {
	
	private static final Faker faker=new Faker();
	private static final String categories[]= {"electronics", "furniture", "clothing", "books", "beauty"};
	
	private static final Random random=new Random();
	
		
	//Product
	public static ProductPOJO productPayload()
	{
		String name=faker.commerce().productName();
		double price = faker.number().randomDouble(2, 10, 500);
		String description=faker.lorem().sentence();
		String imageUrl="https://i.pravatar.cc/100";
		String category=categories[random.nextInt(categories.length)];
		
		//new Product(name, price, description, imageUrl, category);
		return new ProductPOJO(name, price, description, imageUrl, category);
	}
	
}