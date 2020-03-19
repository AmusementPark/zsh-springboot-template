package zsh.springboot.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import zsh.springboot.elasticsearch.model.Layer1Bean;

@Component
public interface EsSampleIndexRepository extends ElasticsearchRepository<Layer1Bean, Long> {
}
