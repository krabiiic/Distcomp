package by.ustsinovich.distcomp.lab1.controller.impl.v1;

import by.ustsinovich.distcomp.lab1.controller.MarkController;
import by.ustsinovich.distcomp.lab1.dto.request.MarkRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.MarkResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.MarkFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.MarkSortCriteria;
import by.ustsinovich.distcomp.lab1.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1.0/marks")
@RestController
public class MarkControllerV1 implements MarkController {

    private final MarkService markService;

    @Override
    public MarkResponseTo createMark(MarkRequestTo createMarkDto) {
        return null;
    }

    @Override
    public List<MarkResponseTo> findAllMarks(
            MarkFilterCriteria filterCriteria,
            List<MarkSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    ) {
        return List.of();
    }

    @Override
    public MarkResponseTo findMarkById(Long id) {
        return null;
    }

    @Override
    public MarkResponseTo updateMarkById(Long id, MarkRequestTo updateMarkDto) {
        return null;
    }

    @Override
    public void deleteMarkById(Long id) {

    }

    @Override
    public MarkResponseTo patchMarkById(Long id, MarkRequestTo patchMarkDto) {
        return null;
    }
}
