package com.example.movie_review.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.movie_review.helpers.OmdbHelper;
import com.example.movie_review.models.Movie;
import com.example.movie_review.models.OmdbMovieDetail;
import com.example.movie_review.models.OmdbMovieSearch;
import com.example.movie_review.services.OmdbMovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OmdbMovieServiceImpl implements OmdbMovieService {
    
    private final OmdbHelper omdbHelper;

    @Override
    public List<Movie> searchMovie(String keyword, int page) throws Exception {

        log.info("영화 검색 keyword={}", keyword);

        OmdbMovieSearch result = omdbHelper.searchMovie(keyword, page);

        List<Movie> movieList = new ArrayList<>();

        if (result != null && result.getSearch() != null) {
            for (OmdbMovieDetail omdbMovie : result.getSearch()) {
                movieList.add(convertToMovie(omdbMovie));
            }
        }

        return movieList;
    }

    @Override
    public List<Movie> getPopularMovies(int page) throws Exception {
        String[] popularKeywords = {
                "marvel",
                "batman",
                "avengers",
                "harry potter",
                "star wars"
        };

        Random random = new Random();
        int index = random.nextInt(popularKeywords.length);

        String keyword = popularKeywords[index];

        log.info("기본 인기영화 keyword={}", keyword);

        OmdbMovieSearch result = omdbHelper.searchMovie(keyword, page);

        List<Movie> movieList = new ArrayList<>();

        if (result != null && result.getSearch() != null) {
            for (OmdbMovieDetail omdbMovie : result.getSearch()) {
                movieList.add(convertToMovie(omdbMovie));
            }
        }

        return movieList;
    }

    @Override
    public Movie getDetailItem(String movieId) throws Exception {

        log.info("영화 상세조회 imdbId={}", movieId);

        OmdbMovieDetail omdbMovie = omdbHelper.getMovieDetail(movieId);

        return convertToMovie(omdbMovie);
    }

    

    private Movie convertToMovie(OmdbMovieDetail omdb) {
        if (omdb == null) {
            return null;
        }

        Movie movie = new Movie();

        movie.setMovieGbn("omdb");
        movie.setMovieId(omdb.getImdbID());

        movie.setTitle(omdb.getTitle());
        movie.setPoster(omdb.getPoster());
        movie.setPlot(omdb.getPlot());

        movie.setYear(omdb.getYear());
        movie.setReleased(omdb.getReleased());
        movie.setRuntime(omdb.getRuntime());
        movie.setGenre(omdb.getGenre());
        movie.setDirector(omdb.getDirector());
        movie.setWriter(omdb.getWriter());
        movie.setActors(omdb.getActors());
        movie.setLanguage(omdb.getLanguage());
        movie.setCountry(omdb.getCountry());
        movie.setAwards(omdb.getAwards());
        movie.setRated(omdb.getRated());
        movie.setType(omdb.getType());

        movie.setImdbRating(omdb.getImdbRating());
        movie.setImdbVotes(omdb.getImdbVotes());

        movie.setDvd(omdb.getDvd());
        movie.setBoxOffice(omdb.getBoxOffice());
        movie.setProduction(omdb.getProduction());

        return movie;
    }
}
