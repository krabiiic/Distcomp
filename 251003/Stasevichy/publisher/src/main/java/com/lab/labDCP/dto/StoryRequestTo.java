package com.lab.labDCP.dto;

import com.lab.labDCP.entity.Sticker;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoryRequestTo {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private List<String> stickers;
    private List<UUID> stickerIds;
}