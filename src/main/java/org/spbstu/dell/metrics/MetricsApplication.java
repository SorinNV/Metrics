package org.spbstu.dell.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.spbstu.dell.metrics.data.collection.DataCollectionModule;
import org.spbstu.dell.metrics.data.processing.DataProcessingModule;
import org.spbstu.dell.metrics.data.saving.InputData;
import org.spbstu.dell.metrics.data.saving.ResultData;
import org.spbstu.dell.metrics.data.visualization.DataVisualizationModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class MetricsApplication {
	private static final Logger LOGGER =
			Logger.getLogger(String.valueOf(MetricsApplication.class));
	static String path = "C:/Users/nikol/IdeaProjects/MAG_SEM3/Metrics";
	public static void main(String[] args) {
		MockTableGenerator generator =
				new MockTableGenerator(path, "mock/mockTable.csv");
		generator.generate(100);

		SpringApplication app = new SpringApplication(MetricsApplication.class);
		Map<String, Object> map = new HashMap<>();
		map.put("server.port", "8081");
		map.put("server.host", "localhost");
		app.setDefaultProperties(map);
		app.run();
	}

	@GetMapping("/result")
	public String getResult() {
		DataCollectionModule dcModule = new DataCollectionModule(path);
		dcModule.readData("mock/mockTable.csv");
		DataProcessingModule dpModule = new DataProcessingModule(path);
		try {
			dpModule.calculateData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataVisualizationModule dvModule = new DataVisualizationModule(path);
		try {
			List<ResultData> data = dvModule.visualizeData();
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data/*dataList*/);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/input")
	public String getInput() {
		try {
			MockTableGenerator generator =
					new MockTableGenerator(path, "mock/mockTable.csv");
			generator.generate(100);
			DataCollectionModule dcModule = new DataCollectionModule(path);
			List<InputData> data = dcModule.readData("mock/mockTable.csv");

			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
