package core.service.impl;

import com.epam.training.ticketservice.core.dto.RoomDto;
import com.epam.training.ticketservice.core.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.core.exceptions.NotFoundException;
import com.epam.training.ticketservice.core.model.Room;
import com.epam.training.ticketservice.core.repository.RoomRepo;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.core.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class RoomServiceImplTests {

    private final RoomRepo roomRepo = mock(RoomRepo.class);
    private final RoomService underTest = new RoomServiceImpl(roomRepo);
    private final String testName = "Tom Hardy Room";
    private final Room room = new Room(testName, 40, 30);
    private final Room updatedRoom = new Room(testName, 400, 300);

    @Test
    public void testCreateRoomShouldSaveRoomWhenRoomDoesNotExist() throws AlreadyExistsException {
        //Given
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.empty());

        //When
        underTest.createRoom(room.getName(), room.getRows(), room.getCols());

        //Then
        verify(roomRepo).save(any(Room.class));
    }

    @Test
    public void testCreateRoomShouldNotSaveRoomWhenRoomDoesExist() {
        //Given
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.of(room));

        //When
        assertThrows(AlreadyExistsException.class,
                () -> underTest.createRoom(
                        room.getName(),
                        room.getRows(),
                        room.getCols()
                )
        );

        //Then
        verify(roomRepo, never()).save(any(Room.class));
    }

    @Test
    public void testUpdateRoomShouldUpdateRoomWhenRoomDoesExist() throws NotFoundException {
        //Given
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.of(room));
        when(roomRepo.save(room)).thenReturn(room);

        //When
        underTest.updateRoom(room.getName(), updatedRoom.getRows(), updatedRoom.getCols());

        //Then
        verify(roomRepo).save(room);
    }

    @Test
    public void testUpdateRoomShouldNotUpdateRoomWhenRoomDoesNotExist() {
        //Given
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class,
                () -> underTest.updateRoom(
                        room.getName(),
                        updatedRoom.getRows(),
                        updatedRoom.getCols()
                )
        );

        //Then
        verify(roomRepo, never()).save(room);
    }

    @Test
    public void testDeleteRoomShouldDeleteRoomWhenRoomDoesExist() throws NotFoundException {
        //Given
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.of(room));
        doNothing().when(roomRepo).delete(room);

        //When
        underTest.deleteRoom(room.getName());

        //Then
        verify(roomRepo, never()).save(room);
        verify(roomRepo).delete(room);
    }

    @Test
    public void testDeleteRoomShouldNotDeleteRoomWhenRoomDoesNotExist() {
        //Given
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class,
                () -> underTest.deleteRoom(
                        room.getName()
                )
        );

        //Then
        verify(roomRepo, never()).save(room);
        verify(roomRepo, never()).delete(room);
    }

    @Test
    public void testRoomListShouldReturnRoomListWhenThereAreRooms() {
        //Given
        when(roomRepo.findAll()).thenReturn(Collections.singletonList(room));

        //When
        List<RoomDto> roomDtoList = underTest.roomList();

        //Then
        verify(roomRepo).findAll();
        assertEquals(1, roomDtoList.size());
        assertEquals(room.getName(), roomDtoList.get(0).getName());
        assertEquals(room.getRows(), roomDtoList.get(0).getRows());
        assertEquals(room.getCols(), roomDtoList.get(0).getCols());
    }

    @Test
    public void testRoomListShouldReturnNothingWhenThereAreNoRooms() {
        //Given

        //When
        List<RoomDto> roomDtoList = underTest.roomList();

        //Then
        assertEquals(emptyList(), roomDtoList);
    }
}
