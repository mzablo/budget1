package mza.thy.health;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Illness;
import mza.thy.domain.filter.FilterParams;
import mza.thy.repository.CureRepository;
import mza.thy.repository.IllnessRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthService {
    private final IllnessRepository illnessRepository;
    private final CureRepository cureRepository;

    @Transactional(readOnly = true)
    public List<IllnessDto> getIllnessList(FilterParams filterParams, @NotNull Sort sort) {
        if (Objects.nonNull(filterParams)
                && (Objects.nonNull(filterParams.getFilterId())
                || Objects.nonNull(filterParams.getFilterName())
                || Objects.nonNull(filterParams.getFilterDate())
                || Objects.nonNull(filterParams.getFilterWho())
                || Objects.nonNull(filterParams.getFilterDescription()))
        ) {
            return doFilter(filterParams);
        }
        return illnessRepository.findAll(sort)
                .stream()
                .map(IllnessDto::convert)
                .collect(Collectors.toList());
    }

    private List<IllnessDto> doFilter(FilterParams filterParams) {
        if (Objects.nonNull(filterParams.getFilterId())) {
            log.debug("Filter illness by id {}", filterParams.getFilterId());
            return illnessRepository.findById(filterParams.getFilterId())
                    .map(IllnessDto::convert)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
        if (Objects.nonNull(filterParams.getFilterName())) {
            log.debug("Filter illness by name {}", filterParams.getFilterName());
            return illnessRepository.findAllByNameLike(filterParams.getFilterName().getValue())
                    .map(IllnessDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterDescription())) {
            log.debug("Filter illness by description {}", filterParams.getFilterDescription());
            return illnessRepository.findAllByDescriptionLike(filterParams.getFilterDescription().getValue())
                    .map(IllnessDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterDate())) {
            log.debug("Filter illness by date {}", filterParams.getFilterDate());
            return illnessRepository.findAllByDate(filterParams.getFilterDate())
                    .map(IllnessDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterAmount())) {
            log.debug("Filter illness by who {}", filterParams.getFilterWho());
            return illnessRepository.findAllByWhoLike(filterParams.getFilterWho().getValue())
                    .map(IllnessDto::convert)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    IllnessDto getIllnessDto(Long id) {
        var result = illnessRepository.findById(id)
                .map(IllnessDto::convert)
                .orElseThrow(() -> new RuntimeException("Illness not found " + id));
        log.debug("Get ilness {}", result);
        return result;
    }

    @Transactional
    long saveIllness(IllnessDto illnessDto) {
        Illness illness;
        if (Objects.nonNull(illnessDto.getId())) {
            illness = updateIllness(illnessDto.getId(), illnessDto);
        } else {
            illness = saveNewIllness(illnessDto);
        }
        return illness.getId();
    }

    private Illness saveNewIllness(IllnessDto illnessDto) {
        if (StringUtils.hasLength(illnessDto.getName())) {
            log.debug("Saving new illness {}", illnessDto);
            return illnessRepository.save(illnessDto.convert());
        } else {
            log.debug("Not saved - empty illness {} ", illnessDto);
            return null;
        }
    }

    @Transactional
    public void deleteIllness(Long id) {
        //deeletecure todo
        illnessRepository.deleteById(id);
        log.debug("Deleted illness {}", id);
    }

    Illness getIllness(Long id) {
        return illnessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Illness not found " + id));
    }

    private Illness updateIllness(Long id, IllnessDto changedIllnessDto) {
        var illness = getIllness(id);
        illness.setDate(changedIllnessDto.getDate());
        illness.setName(changedIllnessDto.getName());
        illness.setWho(changedIllnessDto.getWho());
        illness.setFree(changedIllnessDto.getFree());
        illness.setDescription(changedIllnessDto.getDescription());
        log.debug("Update illness {}", changedIllnessDto);
        return illnessRepository.save(illness);
    }
}
