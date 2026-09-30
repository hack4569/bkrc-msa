package com.example.aladinservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "aladin_book")
public class AladinBook {
    @Id
    private Integer itemId;
    private String title;
    private String link;
    private String author;
    private String pubDate;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String isbn;
    private String isbn13;
    private Integer priceSales;
    private Integer priceStandard;
    private String mallType;
    private String stockStatus;
    private Integer mileage;
    private String cover;
    private Integer categoryId;
    private String categoryName;
    private String publisher;
    private Integer salesPoint;
    private Boolean adult;
    private Boolean fixedPrice;
    private Integer customerReviewRank;
    @Transient
    private Integer bestRank;
    @Transient
    private SubInfo subInfo;
    @Transient
    private String fullDescription;
    @Transient
    private String fullDescription2;
    private String toc;

    @OneToMany(mappedBy = "aladinBook", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookComment> bookCommentList = new ArrayList<>();

    public void replaceBookComments(List<BookComment> comments) {
        bookCommentList.clear();
        if (comments == null) {
            return;
        }
        comments.forEach(comment -> comment.attachTo(this));
        bookCommentList.addAll(comments);
    }
}
