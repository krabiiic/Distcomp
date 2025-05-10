package com.example.rv1.storage;

import com.example.rv1.entity.Label;
import com.example.rv1.entity.Message;
import com.example.rv1.entity.Tweet;
import com.example.rv1.entity.User;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorage {
    public static List<User> users = new ArrayList<>();
    public static List<Tweet> tweets = new ArrayList<>();
    public static List<Message> messages = new ArrayList<>();
    public static List<Label> labels = new ArrayList<>();
}
