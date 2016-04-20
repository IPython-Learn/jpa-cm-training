package com.innominds.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.innominds.entity.Person;
import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
@ComponentScan("com.innominds")
@EnableTransactionManagement
public class DatabaseConfig {

    // If you enable this make sure transactionManager also using same entity Manager
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        // em.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");// this also works and loads config form persistence.xml file
        em.afterPropertiesSet();// IMP
        return em.getObject();
    }

    // @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactoryJavaBased() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPersistenceUnitName("CM-PU");
        em.setPackagesToScan(new String[] { Person.class.getPackage().getName() });
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaDialect(jpaDialect());
        em.setJpaProperties(additionalJpaProperties());
        em.setSharedCacheMode(SharedCacheMode.ALL);// 2nd level cache
        em.afterPropertiesSet();
        return em.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory());// check this pointing to correct EMF
        return transactionManager;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.DERBY);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform(DerbyTenSevenDialect.class.getName());
        return adapter;
    }

    Properties additionalJpaProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // TODO: create-drop, create, none
        // properties.setProperty("hibernate.dialect", DerbyTenSevenDialect.class.getName());
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.ejb.naming_strategy", ImprovedNamingStrategy.class.getName());// fully qualifed name a string
        properties.setProperty("hibernate.cache.region.factory_class", org.hibernate.cache.ehcache.EhCacheRegionFactory.class.getName());
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.use_query_cache", "true");
        properties.setProperty("hibernate.generate_statistics", "false");

        return properties;
    }

    @Bean
    public HibernateJpaDialect jpaDialect() {
        return new HibernateJpaDialect();
    }

    @Bean
    public DataSource dataSource() {
        // instantiate, configure and return production DataSource
        final BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(org.apache.derby.jdbc.EmbeddedDriver.class.getName());
        dataSource.setJdbcUrl("jdbc:derby:target/derbyDB;create=true");
        dataSource.setIdleConnectionTestPeriodInMinutes(1);
        dataSource.setCloseOpenStatements(true);
        dataSource.setIdleMaxAgeInMinutes(1);
        dataSource.setMaxConnectionsPerPartition(100);
        dataSource.setMinConnectionsPerPartition(50);
        dataSource.setPartitionCount(1);
        dataSource.setAcquireIncrement(50);
        return dataSource;
    }
}
