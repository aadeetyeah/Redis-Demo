package com.procrastinator.jbdl19.RedisDemo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class PersonController {

    //----------------------------------STRING OPERATIONS-----------------------------------------------------
    @Autowired
    RedisTemplate<String,Object> redisTemplate; //Autowiring an Inbuilt class who do not have @Component cuz we've created its Bean.

    @Autowired
    ObjectMapper objectMapper;

    //To differentiate between all keys use a prefix for creating a key.
    private static final String PERSON_KEY_PREFIX="person::";
    private static final String PERSON_LIST_KEY="person_list";
    private static final String PERSON_HASH_KEY_PREFIX="person_hash::";


    //e.g. person::1
    //      person::2

    /*Convert Person Java Object into a String
    and save that String in a Redis List and Hashes    */

    @PostMapping("/setValue")
    public void setValue(@RequestBody Person person){
        //store this person in a key named as person::person.getId();
        String key=PERSON_KEY_PREFIX+person.getId();
        redisTemplate.opsForValue().set(key,person);

    }


    @GetMapping("/getValue")
    public Person getValue(@RequestParam("id") long id){
        String key=PERSON_KEY_PREFIX + id;
        return (Person) redisTemplate.opsForValue().get(key);
    }


    //------------------------------------------LIST OPERATION-------------------------------------------
    @PostMapping("/lpush")
    public void lPush(@RequestBody Person person){
        redisTemplate.opsForList().leftPush(PERSON_LIST_KEY,person);
    }

    @PostMapping("/rpush")
    public void rPush(@RequestBody Person person){
        redisTemplate.opsForList().rightPush(PERSON_LIST_KEY,person);
    }

    @GetMapping("/lrange")
    public List<Person> lRange(@RequestParam("start") int start,@RequestParam("end")int end){
        List<Object> l1= (List<Object>) redisTemplate.opsForList().range(PERSON_LIST_KEY,start,end);

        List<Person> personList=new ArrayList<>();
        for(int itr1=0;itr1<l1.size();itr1++){
            personList.add((Person) l1.get(itr1));
        }
        return  personList;
    }


    //------------------------------------------HASH OPERATION-------------------------------------------

    @PostMapping("/hmSet") //Save Person object into hmSet
    public void hSet(@RequestBody Person person){

        Map map = objectMapper.convertValue(person, Map.class);

        redisTemplate.opsForHash().putAll(PERSON_HASH_KEY_PREFIX+person.getId(),map);
    }
    /* With the help of ObjectMapper we are COnverting a Java Object from one Type to Another TYpe.
    * In this case we are converting Person Object into a Map Object.
    * Similarly we can convert Person into a List as well. */


    @GetMapping("/hgetAll") //Retrieve Person object into hmGet
    public Person hGet(@RequestParam("id")int id){
        Map map=redisTemplate.opsForHash().entries(PERSON_HASH_KEY_PREFIX+id);

        if(map==null || map.isEmpty()){
            return null;
        }
        Person person=objectMapper.convertValue(map,Person.class);
        return person;
    }
}
