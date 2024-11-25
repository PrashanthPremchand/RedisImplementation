package com.prashanth.redisImplementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserControllerAndService {
    @Autowired
    RedisTemplate<String, User> redisTemplate;
    @Autowired
    ObjectMapper objectMapper;

    //Redis using key and value
    @PostMapping("/add")
    public void addUser(@RequestParam("id") String id, @RequestBody User user){
        redisTemplate.opsForValue().set(id, user);
    }
    @GetMapping("/get_value")
    public User getValue(@RequestParam("id") String id){
        return redisTemplate.opsForValue().get(id);
    }

    //Redis using Key and list
    @PostMapping("/add_list")
    public void addList(@RequestParam("id") String id, @RequestBody User user){
        redisTemplate.opsForList().leftPush(id, user);
    }
    @GetMapping("/get_range")
    public List<User> getLRange(@RequestParam("id") String id, @RequestParam("count") int count){
        return redisTemplate.opsForList().range(id, 0, count - 1);
    }
    @GetMapping("/get_lpop")
    public List<User> getLPop(@RequestParam("id") String id, @RequestParam("count") int count){
        return redisTemplate.opsForList().leftPop(id, count);
    }

    //Redis using key hashmap
    @PostMapping("/add_map")
    public void addMap(@RequestParam("id") String id, @RequestBody User user){
        Map map = objectMapper.convertValue(user, Map.class);
        redisTemplate.opsForHash().putAll(id, map);
    }
    @GetMapping("/get_map_attribute")
    public String getMapAttribute(@RequestParam("id") String id, @RequestParam("attribute") String attribute){
        return (String)redisTemplate.opsForHash().entries(id).get(attribute);
    }
}
