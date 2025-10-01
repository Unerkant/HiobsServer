package HiobsServer.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Den 25.04.2025
 */

/**
 * Admin: H2 Datenbank configuration
 *
 * H2 → Database: adminHiobs → Tabelle: admin, weiter kommen
 * ACHTUNG: Datenbank nür füt Admin Bereich(HiobsServer, Login + verwaltung)
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "HiobsServer.admin.repository",
        entityManagerFactoryRef = "adminEntityManagerFactory",
        transactionManagerRef = "adminTransactionManager"
)
public class AdminConfig {

    @Bean(name = "adminDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.admin")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "adminEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("adminDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("HiobsServer.admin.model")
                .persistenceUnit("adminPU")
                .build();
    }

    @Bean(name = "adminTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("adminEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
