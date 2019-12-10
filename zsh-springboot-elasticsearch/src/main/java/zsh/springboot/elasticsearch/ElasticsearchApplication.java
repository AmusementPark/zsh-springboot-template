package zsh.springboot.elasticsearch;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zsh.springboot.elasticsearch.model.ShareAccount;
import zsh.springboot.elasticsearch.model.ShareBalance;
import zsh.springboot.elasticsearch.repository.EsShareAccountRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ElasticsearchApplication {

    @Autowired
    private EsShareAccountRepository esShareAccountRepository;

    public static void main(String[] args) {
        ElasticsearchApplication app = SpringApplication.run(ElasticsearchApplication.class, args).getBean(ElasticsearchApplication.class);
        app.insert();
    }

    public void insert() {
        List<ShareAccount> list =  new ArrayList<>();
        for (int i=0;i<1000;i++) {
            list.add(
                ShareAccount.builder()
                    .shareHolder(RandomStringUtils.randomAlphabetic(4)).shareBalances(
                        new ArrayList<ShareBalance>() {{
                            add(ShareBalance.builder().shareCode("600030").shareName("CITIC SECURITY").shareBalance("1000").build());
                        }}
                )
                .build()
            );
        }
        esShareAccountRepository.saveAll(list);
    }
}
