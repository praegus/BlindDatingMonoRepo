package io.praegus.bda.matchingservice;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.scheduling.config.TaskManagementConfigUtils;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.kafka.properties.schema.registry.url=mock://matching-cucumber")
@ContextConfiguration(initializers = ProfileServiceWiremockInitializer.class)
@EmbeddedKafka(partitions = 1, topics = "matchings", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@Import(CucumberConfiguration.ManualMatchingConfiguration.class)
public class CucumberConfiguration {
    @TestConfiguration(proxyBeanMethods = false)
    static class ManualMatchingConfiguration {
        @Bean
        static BeanFactoryPostProcessor disableScheduledMatching() {
            // Scenarios trigger matching explicitly; scheduled runs could publish duplicate events.
            return beanFactory -> {
                var registry = (BeanDefinitionRegistry) beanFactory;
                registry.removeBeanDefinition(TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME);
            };
        }
    }
}
