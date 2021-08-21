package ca.jrvs.apps.twitter.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

public class JsonUtil {

  /**
   * Convert Java object to a JSON string
   * @param object
   * @param prettyJson
   * @param includeNullValues
   * @return JSON String
   * @throws JsonProcessingException
   */
  public static String toPrettyJson(Object object, boolean prettyJson, boolean includeNullValues) throws JsonProcessingException {
    ObjectMapper m = new ObjectMapper();

    if( !includeNullValues) {
      m.setSerializationInclusion(Include.NON_NULL);
    }
    if (prettyJson) {
      m.enable(SerializationFeature.INDENT_OUTPUT);
    }
    return m.writeValueAsString(object);
  }

  /**
   * Parse JSON from a string and create a Java object
   * @param json
   * @param clazz
   * @param <T>
   * @return
   * @throws IOException
   */
  public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
    ObjectMapper m = new ObjectMapper();

    m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    m.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    return (T) m.readValue(json, clazz);
  }

}
