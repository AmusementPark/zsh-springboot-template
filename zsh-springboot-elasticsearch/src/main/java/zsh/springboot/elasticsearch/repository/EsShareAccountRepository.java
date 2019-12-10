package zsh.springboot.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import zsh.springboot.elasticsearch.model.ShareAccount;

@Component
public interface EsShareAccountRepository extends ElasticsearchRepository<ShareAccount, Long> {
}
