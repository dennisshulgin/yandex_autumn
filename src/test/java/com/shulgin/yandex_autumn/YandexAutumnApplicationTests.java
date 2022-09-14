package com.shulgin.yandex_autumn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class YandexAutumnApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	String resourcesPath = "src/test/resources/";

	@Order(1)
	@Test
	public void importsCorrect() throws Exception{
		List<String> jsonStrings = getJsonStrings(resourcesPath + "imports/correct");
		for(String json : jsonStrings) {
			mockMvc.perform(post("/imports")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)).andExpect(status().isOk());
		}
	}

	@Order(2)
	@Test
	public void importsIncorrect() throws Exception{
		List<String> jsonStrings = getJsonStrings(resourcesPath + "imports/incorrect");
		for(String json : jsonStrings) {
			mockMvc.perform(post("/imports")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)).andExpect(status().is(400));
		}
	}

	@Order(3)
	@Test
	public void deleteNode() throws Exception{
		mockMvc.perform(delete("/delete/d515e43f-f3f6-4471-bb77-6b455017a2d2"))
				.andExpect(status().isOk());
	}

	@Order(4)
	@Test
	public void nodes() throws Exception {
		String response = mockMvc.perform(get("/nodes/069cb8d7-bbdd-47d3-ad8f-82ef4c269df1"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		String decodeResponseString = new String(response.getBytes(StandardCharsets.ISO_8859_1));
		JsonNode expectedTree = objectMapper.readTree(getJsonString(resourcesPath + "nodes/Response1.json"));
		JsonNode actualTree = objectMapper.readTree(decodeResponseString);
		System.out.println(actualTree);
		System.out.println(expectedTree);
		Assert.isTrue(expectedTree.equals(actualTree), "Ответы не совпадают.");
	}


	public List<String> getJsonStrings(String path) {
		List<String> jsonStrings = new ArrayList<>();
		File requestFilesPath  = new File(path);
		File[] requestFiles = requestFilesPath.listFiles();
		Objects.requireNonNull(requestFiles, "Не найдены файлы для тестирования");
		Arrays.sort(requestFiles);
		StringBuilder json = new StringBuilder();
		for(File file : requestFiles) {
			try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
				while(reader.ready()) {
					json.append(reader.readLine());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonStrings.add(json.toString());
			json.delete(0, json.length());
		}
		return jsonStrings;
	}

	public String getJsonString(String path) {
		File file = new File(path);
		StringBuilder json = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new FileReader(file));) {
			while(reader.ready()) {
				json.append(reader.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}
