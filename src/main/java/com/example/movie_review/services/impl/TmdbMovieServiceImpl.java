package com.example.movie_review.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movie_review.helpers.TmdbHelper;
import com.example.movie_review.models.Movie;
import com.example.movie_review.models.OmdbMovieDetail;
import com.example.movie_review.models.OmdbMovieSearch;
import com.example.movie_review.models.TmdbMovieCast;
import com.example.movie_review.models.TmdbMovieCrew;
import com.example.movie_review.models.TmdbMovieDetail;
import com.example.movie_review.models.TmdbMovieResult;
import com.example.movie_review.models.TmdbMovieSearch;
import com.example.movie_review.services.TmdbMovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TmdbMovieServiceImpl implements TmdbMovieService {
    
    private final TmdbHelper tmdbHelper;

    @Override
    public List<Movie> searchMovie(String keyword, int page) throws Exception {
        
        log.info("TMDB 영화 검색 keyword={}", keyword);

        TmdbMovieSearch result = tmdbHelper.searchMovie(keyword, page);
        
        List<Movie> movieList = new ArrayList<>();
        
        if (result != null && result.getResults() != null) {

            for (TmdbMovieResult tmdbMovie : result.getResults()) {

                movieList.add(convertToMovie(tmdbMovie));
            }
        }

        return movieList;
    }

    @Override
    public List<Movie> getPopularMovies(int page) throws Exception {
        TmdbMovieSearch result = tmdbHelper.getPopularMovies(page);

        List<Movie> movieList = new ArrayList<>();

        if (result != null && result.getResults() != null) {

            for (TmdbMovieResult tmdbMovie : result.getResults()) {

                movieList.add(convertToMovie(tmdbMovie));
            }
        }

        return movieList;
    }

    @Override
    public Movie getDetailItem(String movieId) throws Exception {
        
        log.info("TMDB 영화 상세조회 imdbId={}", movieId);

        TmdbMovieDetail result = tmdbHelper.getMovieDetail(movieId);

        return convertToMovie(result);
    }
    
    private Movie convertToMovie(TmdbMovieResult tmdbMovie) {

        Movie movie = new Movie();

        movie.setMovieGbn("tmdb");
        movie.setMovieId(String.valueOf(tmdbMovie.getId()));

        movie.setTitle(tmdbMovie.getTitle());
        if (tmdbMovie.getPosterPath() != null) {
            movie.setPoster(
                "https://image.tmdb.org/t/p/w500" + tmdbMovie.getPosterPath()
            );
        }
        
        if (tmdbMovie instanceof TmdbMovieDetail detail) {

            // runtime
            if (detail.getRuntime() != null) {
                movie.setRuntime(detail.getRuntime() + " min");
            }

            // 감독
            if(detail.getCredits() != null && detail.getCredits().getCrew() != null){
                for (TmdbMovieCrew crew : detail.getCredits().getCrew()) {

                    if ("Director".equals(crew.getJob())) {
                        movie.setDirector(crew.getName());
                        break;
                    }
                }
            }

            // 배우
            if(detail.getCredits() != null && detail.getCredits().getCast() !=null){
                List<String> actors = new ArrayList<>();
                for (TmdbMovieCast cast : detail.getCredits().getCast()) {
                        actors.add(cast.getName());

                        if (actors.size() >= 5) {
                            break;
                        }
                }

                movie.setActors(String.join(", ", actors));
            }
        }
        movie.setPlot(tmdbMovie.getOverview());

        movie.setReleased(tmdbMovie.getReleaseDate());
        if (tmdbMovie.getReleaseDate() != null && tmdbMovie.getReleaseDate().length() >= 4) {

            movie.setYear(tmdbMovie.getReleaseDate().substring(0, 4));
        }

        return movie;
    }
    
}
