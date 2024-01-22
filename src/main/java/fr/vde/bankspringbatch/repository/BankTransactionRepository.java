package fr.vde.bankspringbatch.repository;

import fr.vde.bankspringbatch.entities.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction,Long> {

}
