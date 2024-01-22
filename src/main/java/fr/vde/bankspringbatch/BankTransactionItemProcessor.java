package fr.vde.bankspringbatch;

import fr.vde.bankspringbatch.entities.BankTransaction;
import org.springframework.batch.item.ItemProcessor;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;


@Component
public class BankTransactionItemProcessor implements ItemProcessor<BankTransaction, BankTransaction> {

//  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");

  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");



  @Override
  public BankTransaction process(BankTransaction bankTransaction) throws Exception {
    bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));
    return bankTransaction;
  }
}