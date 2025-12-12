package com.rataknak.bookservice.Repository;

import com.rataknak.bookservice.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByUserId(Long userId);

    List<Book> findByCategoryId(Long categoryId);

    List<Book> findByIsFeaturedTrue();

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchByTitleOrAuthor(@Param("query") String query);

    @Query("SELECT b FROM Book b ORDER BY b.downloadCount DESC")
    List<Book> findTopDownloaded();

    @Query("SELECT b FROM Book b ORDER BY b.rating DESC")
    List<Book> findTopRated();

    @Query("SELECT b FROM Book b ORDER BY b.createdAt DESC")
    List<Book> findLatest();
}