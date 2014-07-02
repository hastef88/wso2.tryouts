package org.wso2.carbon.orderprocessor.backend.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;

/**
 * Created by hasithad on 4/10/14.
 */
public class JSONConverter<T> {

    public static <T> String convertToJSON(T object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(object);
    }

    public static <T> T convertToObject(String json,Class objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        return (T)mapper.readValue(json,objectClass);
    }
}
