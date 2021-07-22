package com.mss.searchengine.security;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CustomeInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String documentId = request.getParameter("documentId");
        Map<String,String> map = readMap();
        System.out.println("\n\n\n\n\nprinting loaded map- ");
        for(String key: map.keySet())
            System.out.println(key + " " +map.get(key));
        System.out.println("\n\n");
        writeMap(map,documentId);
        return true;
    }
    public Map<String, String> readMap() {
        Map<String, String> map = new HashMap<>();
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream("count.dat")))) {

            try {
                map = (Map<String, String>) locFile.readObject();
//                    System.out.println("Read location: "+loc.getLocationId()+": "+loc.getDescription());
//                    System.out.println("Found "+(loc.getExits().size()-1)+" exits");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(" something gone wrong " + e.getMessage());
            return new HashMap<>();
        }
        return map;
    }

    public void writeMap(Map<String,String> map,String key) {
        int value = Integer.parseInt(map.getOrDefault(key,"0")) + 1;
        map.put(key,Integer.toString(value));
        try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("count.dat")))) {
            locFile.writeObject(map);
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
}
