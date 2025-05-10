package by.bsuir.romamuhtasarov.impl.repository;

import by.bsuir.romamuhtasarov.api.InMemoryRepository;
import by.bsuir.romamuhtasarov.impl.bean.Message;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageRepository implements InMemoryRepository<Message> {
    private final Map<Long, Message> MessageMemory = new HashMap<>();

    @Override
    public Message get(long id) {
        Message Message = MessageMemory.get(id);
        if (Message != null) {
            Message.setId(id);
        }
        return Message;
    }

    @Override
    public List<Message> getAll() {
        List<Message> MessageList = new ArrayList<>();
        for (Long key : MessageMemory.keySet()) {
            Message Message = MessageMemory.get(key);
            Message.setId(key);
            MessageList.add(Message);
        }
        return MessageList;
    }

    @Override
    public Message delete(long id) {

        return MessageMemory.remove(id);
    }

    @Override
    public Message insert(Message insertObject) {
        MessageMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(Message updatingValue) {
        return MessageMemory.replace(updatingValue.getId(), MessageMemory.get(updatingValue.getId()), updatingValue);
    }


}