package com.dedov.distributed_computing.dto.request;

import com.dedov.distributed_computing.validation.groups.OnCreateOrUpdate;
import com.dedov.distributed_computing.validation.groups.OnPatch;
import jakarta.validation.constraints.Size;

public class MarkRequestTo {

    private long id;

    @Size(min = 2, max = 32, message = "Name size must be between 2..64 characters", groups = {OnPatch.class, OnCreateOrUpdate.class})
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
