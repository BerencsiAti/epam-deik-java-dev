package core.service.impl;

import com.epam.training.ticketservice.core.dto.MovieDto;
import com.epam.training.ticketservice.core.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.core.exceptions.NotFoundException;
import com.epam.training.ticketservice.core.model.Movie;
import com.epam.training.ticketservice.core.repository.MovieRepo;
import com.epam.training.ticketservice.core.service.MovieService;
import com.epam.training.ticketservice.core.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MovieServiceImplTests {

    private final MovieRepo movieRepository = mock(MovieRepo.class);
    private final MovieService underTest = new MovieServiceImpl(movieRepository);
    private final String testName = "911";
    private final Movie movie = new Movie(testName, "action", 143);
    private final Movie updatedMovie = new Movie(testName, "horror", 876);

    @Test
    public void testCreateMovieShouldSaveMovieWhenMovieDoesNotExist() throws AlreadyExistsException {
        //Given
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.empty());

        //When
        underTest.createMovie(movie.getName(), movie.getGenre(), movie.getLength());

        //Then
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    public void testCreateMovieShouldNotSaveMovieWhenMovieDoesExist() {
        //Given
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.of(movie));

        //When
        assertThrows(AlreadyExistsException.class,
                () -> underTest.createMovie(
                        movie.getName(),
                        movie.getGenre(),
                        movie.getLength()
                )
        );

        //Then
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    public void testUpdateMovieShouldUpdateMovieWhenMovieDoesExist() throws NotFoundException {
        //Given
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        //When
        underTest.updateMovie(movie.getName(), updatedMovie.getGenre(), updatedMovie.getLength());

        //Then
        verify(movieRepository).save(movie);
    }

    @Test
    public void testUpdateMovieShouldNotUpdateMovieWhenMovieDoesNotExist() {
        //Given
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class,
                () -> underTest.updateMovie(
                        movie.getName(),
                        updatedMovie.getGenre(),
                        updatedMovie.getLength()
                )
        );

        //Then
        verify(movieRepository, never()).save(movie);
    }

    @Test
    public void testDeleteMovieShouldDeleteMovieWhenMovieDoesExist() throws NotFoundException {
        //Given
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.of(movie));
        doNothing().when(movieRepository).delete(movie);

        //When
        underTest.deleteMovie(movie.getName());

        //Then
        verify(movieRepository, never()).save(movie);
        verify(movieRepository).delete(movie);
    }

    @Test
    public void testDeleteMovieShouldNotDeleteMovieWhenMovieDoesNotExist() {
        //Given
        when(movieRepository.findByName(movie.getName())).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class,
                () -> underTest.deleteMovie(
                        movie.getName()
                )
        );

        //Then
        verify(movieRepository, never()).save(movie);
        verify(movieRepository, never()).delete(movie);
    }

    @Test
    public void testMovieListShouldReturnMovieListWhenThereAreMovies() {
        //Given
        when(movieRepository.findAll()).thenReturn(Collections.singletonList(movie));

        //When
        List<MovieDto> movieDtoList = underTest.movieList();

        //Then
        verify(movieRepository).findAll();
        assertEquals(1, movieDtoList.size());
        assertEquals(movie.getName(), movieDtoList.get(0).getName());
        assertEquals(movie.getGenre(), movieDtoList.get(0).getGenre());
        assertEquals(movie.getLength(), movieDtoList.get(0).getLength());
    }

    @Test
    public void testMovieListShouldReturnNothingWhenThereAreNoMovies() {
        //Given

        //When
        List<MovieDto> movieDtoList = underTest.movieList();

        //Then
        assertEquals(emptyList(), movieDtoList);
    }
}
