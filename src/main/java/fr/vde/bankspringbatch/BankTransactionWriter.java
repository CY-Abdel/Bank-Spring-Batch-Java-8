package fr.vde.bankspringbatch;

import fr.vde.bankspringbatch.entities.BankTransaction;
import fr.vde.bankspringbatch.repository.BankTransactionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankTransactionWriter implements ItemWriter<BankTransaction> {

  @Autowired
  private BankTransactionRepository bankTransactionRepository;

  @Override
  public void write(List<? extends BankTransaction> list) throws Exception {
    bankTransactionRepository.saveAll(list);
  }
}
