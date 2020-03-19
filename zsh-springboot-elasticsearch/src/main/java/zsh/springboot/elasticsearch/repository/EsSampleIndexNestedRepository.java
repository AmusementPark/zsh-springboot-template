package zsh.springboot.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import zsh.springboot.elasticsearch.nested.NestedLayer1Bean;

@Component
public interface EsSampleIndexNestedRepository extends ElasticsearchRepository<NestedLayer1Bean, Long> {
}
