package se.yrgo.libraryapp.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.yrgo.libraryapp.dao.ClassificationDao;
import se.yrgo.libraryapp.entities.DdsClassification;

class ClassificationControllerTest {

    private ClassificationDao classificationDao;
    private ClassificationController classificationController;

    @BeforeEach
    void setUp() {
        classificationDao = mock(ClassificationDao.class);
        classificationController = new ClassificationController(classificationDao);
    }

    @Test
    void testGetAvailableClassifications() {
        List<DdsClassification> expectedClassifications = Collections.emptyList();
        when(classificationDao.getAvailable()).thenReturn(expectedClassifications);

        List<DdsClassification> result = classificationController.getAvailable();

        assertThat(result).isEqualTo(expectedClassifications);
        verify(classificationDao).getAvailable();
    }
}