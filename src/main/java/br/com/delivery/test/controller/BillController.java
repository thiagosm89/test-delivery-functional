package br.com.delivery.test.controller;

import br.com.delivery.test.dto.BillAddDTO;
import br.com.delivery.test.model.Bill;
import br.com.delivery.test.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class BillController {

    @Autowired
    private BillService billService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/save")
    public Mono<BillAddDTO> save(@RequestBody Mono<BillAddDTO> billDto) {
        return billDto
                .map(b ->  mapper.map(b, Bill.class))
                .map(b -> billService.save(b))
                .map(b -> mapper.map(b, BillAddDTO.class));
    }
}
