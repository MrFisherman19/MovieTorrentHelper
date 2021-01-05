package com.mrfisherman.movietorrenthelper.service.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserUtilTest {

    @Test
    void parseStringToJsonObject() {
        //given
        String jsonWithObject = "{\n" +
                "\"employee\":{\n" +
                "\"name\":\"sonoo\",\n" +
                "\"salary\":56000,\n" +
                "\"married\":true\n" +
                "}\n" +
                "}";
        //when
        JsonObject jsonObject = JsonParserUtil.parseStringToJsonObject(jsonWithObject);
        //then
        System.out.println(jsonObject);
        assertAll(() -> {
            assertTrue(jsonObject.isJsonObject());
            assertTrue(jsonObject.has("employee"));
            assertTrue(jsonObject.toString().contains("56000"));
        });
    }

    @Test
    void parseStringToJsonArray() {
        //given
        String jsonWithArray = "[  \n" +
                "    {\"name\":\"Ram\", \"email\":\"Ram@gmail.com\"},  \n" +
                "    {\"name\":\"Bob\", \"email\":\"bob32@gmail.com\"}  \n" +
                "] ";
        //when
        JsonArray jsonArray = JsonParserUtil.parseStringToJsonArray(jsonWithArray);
        //then
        assertAll(() -> {
            assertEquals(2, jsonArray.size());
            assertTrue(jsonArray.isJsonArray());
            assertTrue(jsonArray.get(0).toString().contains("Ram"));
        });
    }
}