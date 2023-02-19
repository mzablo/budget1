package mza.thy.health;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Illness;
import mza.thy.domain.filter.FilterParams;
import mza.thy.filter.FilterHandler;
import mza.thy.repository.CureRepository;
import mza.thy.repository.IllnessRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
class HealthService {
    private final IllnessRepository illnessRepository;
    private final CureRepository cureRepository;
    private FilterHandler<Illness> filter;

    public HealthService(IllnessRepository illnessRepository, CureRepository cureRepository) {
        this.illnessRepository = illnessRepository;
        this.cureRepository = cureRepository;
        filter = illnessRepository.getFilter();
    }

    @Transactional(readOnly = true)
    public List<IllnessDto> getIllnessList(FilterParams filterParams, @NotNull Sort sort) {
        if (FilterParams.isFilled(filterParams)) {
            return doFilter(filterParams);
        }
        return illnessRepository.findAll(sort)
                .stream()
                .map(IllnessDto::convert)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CureDto> getCureList() {
        return cureRepository.findAll()
                .stream()
                .map(CureDto::convert)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CureDto> getCureForIllness(Long illnessId) {
        return cureRepository.findAllByIllnessId(illnessId)
                .map(CureDto::convert)
                .collect(Collectors.toList());
    }

    private List<IllnessDto> doFilter(FilterParams filterParams) {
        return filter.getFiltered(filterParams)
                .map(IllnessDto::convert)
                .collect(Collectors.toList());
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
