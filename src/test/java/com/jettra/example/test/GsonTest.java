package com.jettra.example.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jettra.example.entity.library.Author;
import java.lang.reflect.Type;
import java.util.List;

public class GsonTest {
    public static void main(String[] args) throws Exception {
        String json = "[{\"id\":\"1\",\"name\":\"Gabriel Garcia Marquez\",\"country\":\"Colombia\"},{\"id\":\"2\",\"name\":\"J.K. Rowling\",\"country\":\"UK\"}]";
        Gson gson = new Gson();
        
        // This is what RestClientProxy does:
        // Type genericReturnType = method.getGenericReturnType();
        // Since we can't get it from method here, let's simulate what method.getGenericReturnType() returns for List<Author>
        Type genericReturnType = new TypeToken<List<Author>>(){}.getType();
        
        System.out.println("Type: " + genericReturnType);
        
        Object result = gson.fromJson(json, genericReturnType);
        
        System.out.println("Result class: " + result.getClass());
        if (result instanceof List) {
            List<?> list = (List<?>) result;
            System.out.println("List size: " + list.size());
            for (Object o : list) {
                System.out.println("Item class: " + o.getClass());
                System.out.println("Item: " + o);
            }
        }
    }
}
