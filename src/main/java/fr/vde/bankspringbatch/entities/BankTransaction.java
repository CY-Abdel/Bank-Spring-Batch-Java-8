package fr.vde.bankspringbatch.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor  @ToString
public class BankTransaction {
  @Id
  private Long id;
  private long accountID;
  private Date transactionDate;
  @Transient // il ne sera pas persisté a la BDD avec JPA
  private String strTransactionDate;
  private Character transactionType;
  private Double amount;


}
