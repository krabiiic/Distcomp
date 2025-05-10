package by.bsuir.romamuhtasarov.impl.service;

import by.bsuir.romamuhtasarov.api.Service;
import by.bsuir.romamuhtasarov.impl.bean.Creator;
import by.bsuir.romamuhtasarov.impl.dto.CreatorRequestTo;
import by.bsuir.romamuhtasarov.impl.dto.CreatorResponseTo;
import by.bsuir.romamuhtasarov.api.CreatorMapper;
import by.bsuir.romamuhtasarov.impl.repository.CreatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreatorService implements Service<CreatorResponseTo, CreatorRequestTo> {

    @Autowired
    private CreatorRepository creatorRepository;

    public CreatorService() {

    }

    public List<CreatorResponseTo> getAll() {
        List<Creator> creatorList = creatorRepository.getAll();
        List<CreatorResponseTo> resultList = new ArrayList<>();
        for (int i = 0; i < creatorList.size(); i++) {
            resultList.add(CreatorMapper.INSTANCE.CreatorToCreatorResponseTo(creatorList.get(i)));
        }
        return resultList;
    }

    public CreatorResponseTo update(CreatorRequestTo updatingCreator) {
        Creator creator = CreatorMapper.INSTANCE.CreatorRequestToToCreator(updatingCreator);
        if (validateCreator(creator)) {
            boolean result = creatorRepository.update(creator);
            CreatorResponseTo responseTo = result ? CreatorMapper.INSTANCE.CreatorToCreatorResponseTo(creator) : null;
            return responseTo;
        } else return new CreatorResponseTo();
        //return responseTo;
    }

    public CreatorResponseTo get(long id) {
        return CreatorMapper.INSTANCE.CreatorToCreatorResponseTo(creatorRepository.get(id));
    }

    public CreatorResponseTo delete(long id) {
        return CreatorMapper.INSTANCE.CreatorToCreatorResponseTo(creatorRepository.delete(id));
    }

    public CreatorResponseTo add(CreatorRequestTo creatorRequestTo) {
        Creator creator = CreatorMapper.INSTANCE.CreatorRequestToToCreator(creatorRequestTo);
        return CreatorMapper.INSTANCE.CreatorToCreatorResponseTo(creatorRepository.insert(creator));
    }

    private boolean validateCreator(Creator creator) {
        String firstname = creator.getFirstname();
        String lastname = creator.getLastname();
        String login = creator.getLogin();
        String password = creator.getPassword();
        return (firstname.length() >= 2 && firstname.length() <= 64) && (lastname.length() >= 2 && lastname.length() <= 64) && (password.length() >= 8 && firstname.length() <= 128) && (login.length() >= 2 && login.length() <= 64);
    }
}