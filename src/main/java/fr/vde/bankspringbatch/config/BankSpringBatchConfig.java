package fr.vde.bankspringbatch.config;

import fr.vde.bankspringbatch.entities.BankTransaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * //@EnableBatchProcessing :
 * Cette annotation est spécifique à Spring Batch. Elle est utilisée pour configurer et activer la fonctionnalité de traitement par lots (batch processing) dans une application Spring.
 * L'annotation permet l'auto-configuration des éléments essentiels du traitement par lots, tels que les gestionnaires de tâches, les contextes de travail, etc.
 */

@Configuration
@EnableBatchProcessing
public class BankSpringBatchConfig {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private ItemReader<BankTransaction> bankTransactionItemReader;
  @Autowired
  private ItemWriter<BankTransaction> bankTransactionItemWriter;
//  @Autowired
//  private ItemProcessor<BankTransaction, BankTransaction> bankTransactionItemProcessor;

  @Bean
  public Job bankJob() {
    Step step1 = stepBuilderFactory.get("step-load-data")
      .<BankTransaction, BankTransaction>chunk(100)
      .reader(bankTransactionItemReader)
//      .processor(bankTransactionItemProcessor)
      .processor(compositeItemProceessor())
      .writer(bankTransactionItemWriter)
      .build();

    return jobBuilderFactory.get("bank-data-loader-job")
      .start(step1).build();
  }

  @Bean
  public ItemProcessor<BankTransaction, BankTransaction> compositeItemProceessor() {
    List<ItemProcessor<BankTransaction, BankTransaction>> itemProcessors = new ArrayList<>();
    itemProcessors.add(itemProcessor1());
    itemProcessors.add(itemProcessor2());

    CompositeItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor = new CompositeItemProcessor<>();
    compositeItemProcessor.setDelegates(itemProcessors);

    return compositeItemProcessor;
  }


  @Bean
  public BankTransactionItemProcessor itemProcessor1() {
    return new BankTransactionItemProcessor();
  }

  @Bean
  public BankTransactionItemAnalyticsProcessor itemProcessor2() {
    return new BankTransactionItemAnalyticsProcessor();
  }

  @Bean
  public FlatFileItemReader<BankTransaction> flatFileItemReader(@Value("${inputFile}") Resource inputFile) {
    FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
    flatFileItemReader.setName("CSV-READER");
    flatFileItemReader.setLinesToSkip(1); // entete du csv nom des col
    flatFileItemReader.setResource(inputFile);
    flatFileItemReader.setLineMapper(lineMapper());
    return flatFileItemReader;
  }

  @Bean
  public LineMapper<BankTransaction> lineMapper() {
    DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(",");
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames("id", "accountID", "strTransactionDate", "transactionType", "amount");
    lineMapper.setLineTokenizer(lineTokenizer);
    BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(BankTransaction.class);
    lineMapper.setFieldSetMapper(fieldSetMapper);
    return lineMapper;
  }
}
