package com.example.rv1.storage;

import com.example.rv1.entity.Mark;
import com.example.rv1.entity.Message;
import com.example.rv1.entity.News;
import com.example.rv1.entity.Editor;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorage {
    public static List<Editor> editors = new ArrayList<>();
    public static List<News> news = new ArrayList<>();
    public static List<Message> messages = new ArrayList<>();
    public static List<Mark> marks = new ArrayList<>();
}
