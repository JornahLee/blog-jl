// package com.wip.configuration;
//
// import org.elasticsearch.client.RestHighLevelClient;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.elasticsearch.client.ClientConfiguration;
// import org.springframework.data.elasticsearch.client.RestClients;
// import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
// import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
// @Configuration
// @EnableElasticsearchRepositories(
//         basePackages = "org.springframework.data.elasticsearch.repository"
// )
// public class ESConfig extends AbstractElasticsearchConfiguration {
//
//
//     @Override
//     public RestHighLevelClient elasticsearchClient() {
//         return RestClients.create(ClientConfiguration.create("118.25.187.1:9200")).rest();
//     }
//
//     // @Bean
//     // public ElasticsearchOperations elasticsearchTemplate() {
//     //     // ...
//     // }
//
//
// }
