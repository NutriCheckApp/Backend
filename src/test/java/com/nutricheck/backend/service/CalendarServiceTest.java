package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.CalendarEntry;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.calendar.CalendarEntryRequest;
import com.nutricheck.backend.dto.calendar.CalendarEntryResponse;
import com.nutricheck.backend.dto.calendar.FileMetadata;
import com.nutricheck.backend.repository.CalendarRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private FileSaverService imageSaverService;

    @InjectMocks
    private CalendarService calendarService;

    private User testUser;
    private LocalDate testDate;
    private CalendarEntry existingEntry;
    private CalendarEntryRequest request;

    @BeforeEach
    void setUp() {
        testUser = User.builder().userId(1L).username("testuser").build();
        testDate = LocalDate.of(2023, 10, 26);

        existingEntry = CalendarEntry.builder()
                .id(1L)
                .user(testUser)
                .date(testDate)
                .memo("Old Memo")
                .imageName("old_image.jpg")
                .imageUrl("/path/to/old_image.jpg")
                .build();

        request = new CalendarEntryRequest(testDate, "New Memo");
    }


    @Test
    void getEntry_ShouldReturnEntry_WhenFound() {
        // Arrange
        when(calendarRepository.findByUserAndDate(testUser, testDate))
                .thenReturn(Optional.of(existingEntry));

        // Act
        CalendarEntryResponse response = calendarService.getEntry(testUser, testDate);

        // Assert
        assertNotNull(response);
        assertEquals(testDate, response.getDate());
        assertEquals("Old Memo", response.getMemo());
        assertEquals("old_image.jpg", response.getImageName());
        verify(calendarRepository, times(1)).findByUserAndDate(testUser, testDate);
    }

    @Test
    void getEntry_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(calendarRepository.findByUserAndDate(testUser, testDate))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> calendarService.getEntry(testUser, testDate));

        verify(calendarRepository, times(1)).findByUserAndDate(testUser, testDate);
    }


    @Test
    void saveImage_ShouldCreateNewEntry_WhenNoPreviousEntryExists() {
        // Arrange
        MultipartFile file = new MockMultipartFile("file", "new_image.png", "image/png", "some image data".getBytes());
        FileMetadata fileMetadata = FileMetadata.builder()
                .fileUrl("/path/to/new_image.png")
                .fileName("new_image.png")
                .build();


        when(imageSaverService.saveFile(testUser.getUsername(), file)).thenReturn(fileMetadata);
        when(calendarRepository.findByUserAndDate(testUser, testDate)).thenReturn(Optional.empty());

        when(calendarRepository.save(any(CalendarEntry.class))).thenAnswer(invocation -> {
            CalendarEntry newEntry = invocation.getArgument(0);
            newEntry.setId(2L);
            return newEntry;
        });

        // Act
        CalendarEntryResponse response = calendarService.saveImage(testUser, file, request);

        // Assert
        assertNotNull(response);
        assertEquals(testDate, response.getDate());
        assertEquals("New Memo", response.getMemo());
        assertEquals("new_image.png", response.getImageName());

        verify(calendarRepository, times(1)).findByUserAndDate(eq(testUser), eq(testDate));

        verify(imageSaverService, times(1)).saveFile(eq(testUser.getUsername()), eq(file));
        verify(calendarRepository, times(1)).save(any(CalendarEntry.class));

        verify(imageSaverService, never()).deleteFile(anyString());
    }

    @Test
    void saveImage_ShouldUpdateExistingEntryAndDeleteOldImage_WhenPreviousEntryExists() {
        // Arrange
        MultipartFile file = new MockMultipartFile("file", "updated_image.png", "image/png", "new image data".getBytes());
        FileMetadata fileMetadata = FileMetadata.builder()
                .fileUrl("/path/to/updated_image.png")
                .fileName("updated_image.png")
                .build();

        when(calendarRepository.findByUserAndDate(testUser, testDate)).thenReturn(Optional.of(existingEntry));
        when(imageSaverService.saveFile(testUser.getUsername(), file)).thenReturn(fileMetadata);

        when(calendarRepository.save(any(CalendarEntry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CalendarEntryResponse response = calendarService.saveImage(testUser, file, request);

        // Assert
        assertNotNull(response);
        assertEquals(testDate, response.getDate());
        assertEquals(request.getMemo(), response.getMemo());
        assertEquals(fileMetadata.getFileName(), response.getImageName());

        verify(calendarRepository, times(1)).findByUserAndDate(eq(testUser), eq(testDate));

        verify(imageSaverService, times(1)).saveFile(eq(testUser.getUsername()), eq(file));

        verify(calendarRepository, times(1)).save(existingEntry);

        log.info("existingEntry.getImageUrl()" + existingEntry.getImageUrl());
        verify(imageSaverService, times(1)).deleteFile("/path/to/old_image.jpg");

        assertEquals(fileMetadata.getFileName(), existingEntry.getImageName());
        assertEquals(fileMetadata.getFileUrl(), existingEntry.getImageUrl());
        assertEquals(request.getMemo(), existingEntry.getMemo());
    }

    @Test
    void getFileResource_ShouldReturnResource_WhenEntryAndFileFound() {
        // Arrange
        String filename = "existing_file.pdf";
        String fileUrl = "/path/to/existing_file.pdf";
        Resource mockResource = mock(Resource.class);

        CalendarEntry fileEntry = CalendarEntry.builder()
                .user(testUser)
                .imageName(filename)
                .imageUrl(fileUrl)
                .build();

        when(calendarRepository.findByUserAndImageName(testUser, filename))
                .thenReturn(Optional.of(fileEntry));
        when(imageSaverService.getFile(fileUrl)).thenReturn(mockResource);

        // Act
        Resource result = calendarService.getFileResource(testUser, filename);

        // Assert
        assertNotNull(result);
        assertEquals(mockResource, result);
        verify(calendarRepository, times(1)).findByUserAndImageName(testUser, filename);
        verify(imageSaverService, times(1)).getFile(fileUrl);
    }

    @Test
    void getFileResource_ShouldThrowException_WhenEntryNotFound() {
        // Arrange
        String filename = "non_existent.png";

        when(calendarRepository.findByUserAndImageName(testUser, filename))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> calendarService.getFileResource(testUser, filename));

        verify(calendarRepository, times(1)).findByUserAndImageName(testUser, filename);
        verify(imageSaverService, never()).getFile(anyString());
    }

}