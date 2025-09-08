package co.edu.uniquindio.service.objects.impl;

import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.mapper.objects.BodegaMapper;
import co.edu.uniquindio.repository.objects.BodegaRepo;
import co.edu.uniquindio.service.objects.BodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepo bodegaRepo;
    private final BodegaMapper bodegaMapper;


    @Override
    public List<BodegaDto> listarTodasBodegas() {
        return bodegaRepo.findAll().stream().map(bodegaMapper::toDto).collect(Collectors.toList());
    }
}
