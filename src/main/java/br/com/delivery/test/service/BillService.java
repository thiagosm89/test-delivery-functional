package br.com.delivery.test.service;

import br.com.delivery.test.model.Bill;
import reactor.core.publisher.Mono;

public interface BillService {
    Mono<Bill> save(Bill bill);

    Mono<Bill> list();
}
