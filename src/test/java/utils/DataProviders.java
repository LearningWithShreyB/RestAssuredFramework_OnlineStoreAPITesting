package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
//import java.io.FileReader;

//import java.io.InputStream;
import java.io.InputStreamReader;

public class DataProviders {

	@DataProvider
	public Object[][] jsonDataProvider() throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		InputStream is = getClass().getClassLoader().getResourceAsStream("testdata/product.json");

		if (is == null) {
			throw new RuntimeException("product.json not found");
		}

		List<Map<String, String>> dataList = objectMapper.readValue(is, new TypeReference<List<Map<String, String>>>() {
		});

		Object[][] dataArray = new Object[dataList.size()][1];

		for (int i = 0; i < dataList.size(); i++) {
			dataArray[i][0] = dataList.get(i);
		}

		return dataArray;
	}

	@DataProvider
	public Object[][] csvDataProvider() throws IOException {

		List<String[]> dataList = new ArrayList<>();

		InputStream is = getClass().getClassLoader().getResourceAsStream("testdata/product.csv");

		if (is == null) {
			throw new RuntimeException("product.csv not found");
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

			// Skip header
			br.readLine();

			String line;
			while ((line = br.readLine()) != null) {
				dataList.add(line.split(","));
			}
		}

		Object[][] dataArray = new Object[dataList.size()][];

		for (int i = 0; i < dataList.size(); i++) {
			dataArray[i] = dataList.get(i);
		}

		return dataArray;
	}

}
