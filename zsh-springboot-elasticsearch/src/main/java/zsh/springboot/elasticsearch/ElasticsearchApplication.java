package zsh.springboot.elasticsearch;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zsh.springboot.elasticsearch.model.*;
import zsh.springboot.elasticsearch.repository.EsSampleIndexNestedRepository;
import zsh.springboot.elasticsearch.repository.EsSampleIndexRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ElasticsearchApplication {

    @Autowired
    private EsSampleIndexRepository esSampleIndexRepository;
    @Autowired
    private EsSampleIndexNestedRepository esSampleIndexNestedRepository;

    public static void main(String[] args) {
        ElasticsearchApplication app = SpringApplication.run(ElasticsearchApplication.class, args).getBean(ElasticsearchApplication.class);
        app.insert();
    }

    public void insert() {
        for (int i=0; i<8; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doInsert();
                }
            }).start();
        }
    }

    public void doInsert() {
        for (int b=0; b <3000; b++) {
            List<Layer1Bean> layer1Beans = new ArrayList<>();
            for (int a = 0; a < 1000; a++) {
                Layer1Bean layer1Bean = Layer1Bean
                        .builder()
                        .l1String1(RandomStringUtils.randomAlphabetic(4))
                        .l1String2(RandomStringUtils.randomAlphabetic(4))
                        .build();
                List<Layer2Bean> list = new ArrayList<>();

                for (int i = 0; i < 2; i++) {
                    Layer3Bean layer3Bean = Layer3Bean
                            .builder()
                            .l3String1(RandomStringUtils.randomAlphabetic(4))
                            .l3String2(RandomStringUtils.randomAlphabetic(4))
                            .build();
                    Layer2Bean layer2Bean = Layer2Bean
                            .builder()
                            .l2String1(RandomStringUtils.randomAlphabetic(4))
                            .l2String2(RandomStringUtils.randomAlphabetic(4))
                            .layer3Bean(layer3Bean)
                            .build();
                    list.add(layer2Bean);
                }
                layer1Bean.setLayer2BeansList(list);
                layer1Beans.add(layer1Bean);
            }
            esSampleIndexRepository.saveAll(layer1Beans);
            System.out.println(Thread.currentThread().getId()+":"+(b+1)*1000+" TIMES");
        }
    }
}
