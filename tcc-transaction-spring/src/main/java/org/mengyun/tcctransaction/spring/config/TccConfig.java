package org.mengyun.tcctransaction.spring.config;

import org.mengyun.tcctransaction.recover.TransactionRecovery;
import org.mengyun.tcctransaction.spring.ConfigurableCoordinatorAspect;
import org.mengyun.tcctransaction.spring.ConfigurableTransactionAspect;
import org.mengyun.tcctransaction.spring.recover.RecoverScheduledJob;
import org.mengyun.tcctransaction.spring.support.SpringBeanFactory;
import org.mengyun.tcctransaction.spring.support.SpringTransactionConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class TccConfig {

    @Bean
    public SpringBeanFactory springBeanFactory(){

        SpringBeanFactory springBeanFactory = new SpringBeanFactory();
        return springBeanFactory;
    }

    @Bean(initMethod = "init")
    public SpringTransactionConfigurator transactionConfigurator(){
        SpringTransactionConfigurator springTransactionConfigurator = new SpringTransactionConfigurator();
        return springTransactionConfigurator;
    }

    @Bean(initMethod = "init")
    public ConfigurableTransactionAspect configurableTransactionAspect(){
        ConfigurableTransactionAspect configurableTransactionAspect = new ConfigurableTransactionAspect();
        configurableTransactionAspect.setTransactionConfigurator(transactionConfigurator());
        return configurableTransactionAspect;
    }

    @Bean(initMethod = "init")
    public ConfigurableCoordinatorAspect configurableCoordinatorAspect(){

        ConfigurableCoordinatorAspect configurableCoordinatorAspect = new ConfigurableCoordinatorAspect();
        configurableCoordinatorAspect.setTransactionConfigurator(transactionConfigurator());
        return configurableCoordinatorAspect;
    }

    @Bean
    public TransactionRecovery transactionRecovery(){
        TransactionRecovery transactionRecovery = new TransactionRecovery();
        transactionRecovery.setTransactionConfigurator(transactionConfigurator());
        return transactionRecovery;
    }

    /*@Bean
    public Scheduler recoverScheduler(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        schedulerFactoryBean.setSchedulerFactory(new StdSchedulerFactory());


        Scheduler scheduler =  schedulerFactoryBean.getObject();
        return scheduler;
    }*/

    @Bean(initMethod = "init")
    public RecoverScheduledJob recoverScheduledJob(){

        RecoverScheduledJob recoverScheduledJob = new RecoverScheduledJob();
        recoverScheduledJob.setTransactionRecovery(transactionRecovery());
        recoverScheduledJob.setTransactionConfigurator(transactionConfigurator());

        return recoverScheduledJob;
    }
}
