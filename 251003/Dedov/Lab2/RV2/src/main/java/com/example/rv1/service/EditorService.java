package com.example.rv1.service;

import com.example.rv1.dto.EditorDTO;
import com.example.rv1.entity.Editor;
import com.example.rv1.exception.ExceptionBadRequest;
import com.example.rv1.mapper.EditorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.rv1.exception.ExeptionForbidden;
import com.example.rv1.repository.EditorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EditorService {
    private final EditorMapper editorMapper;
    private final EditorRepository editorRepository;

    public EditorDTO createEditor(EditorDTO userDTO){
        Editor user = editorMapper.toEditor(userDTO);
        Optional<Editor> odubl = editorRepository.findEditorByLogin(user.getLogin());
        if(odubl.isPresent()){
            throw new ExeptionForbidden("403");
        }
        editorRepository.save(user);
        EditorDTO dto = editorMapper.toEditorDTO(user);
        return  dto;
    }

    public EditorDTO deleteEditor(int id) throws Exception {
        Optional<Editor> ouser = editorRepository.findEditorById(id);
        Editor user = ouser.orElseThrow(() -> new ExceptionBadRequest("400"));
        EditorDTO dto = editorMapper.toEditorDTO(user);
        editorRepository.delete(user);
        return  dto;
    }

    public EditorDTO getEditor(int id){
        Optional<Editor> ouser = editorRepository.findEditorById(id);
        Editor user = ouser.orElseThrow(() -> new ExceptionBadRequest("400"));
        EditorDTO dto = editorMapper.toEditorDTO(user);
        return  dto;
    }

    public List<EditorDTO> getEditors(){
        List<Editor> users = editorRepository.findAll();
        List<EditorDTO> dtos = editorMapper.toEditorDTOList(users);
        return  dtos;
    }

    public EditorDTO updateEditor(EditorDTO userDTO){
        Editor user = editorMapper.toEditor(userDTO);
        editorRepository.save(user);
        EditorDTO dto = editorMapper.toEditorDTO(user);
        return  dto;
    }



}
