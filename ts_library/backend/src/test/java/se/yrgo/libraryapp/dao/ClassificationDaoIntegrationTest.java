package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import se.yrgo.libraryapp.entities.DdsClassification;

@Tag("integration")
@H2
public class ClassificationDaoIntegrationTest {

    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        ClassificationDaoIntegrationTest.ds = ds;
    }

    @Test
    void getAvailableClassifications() {
        ClassificationDao dao = new ClassificationDao(ds);

        List<DdsClassification> classifications = dao.getAvailable();

        assertThat(classifications).isNotNull();
    }
}