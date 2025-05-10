package by.bsuir.romamuhtasarov.api;

import by.bsuir.romamuhtasarov.impl.bean.News;
import by.bsuir.romamuhtasarov.impl.dto.NewsRequestTo;
import by.bsuir.romamuhtasarov.impl.dto.NewsResponseTo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T17:15:23+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class NewsMapperImpl implements NewsMapper {

    @Override
    public NewsRequestTo NewsToNewsRequestTo(News News) {
        if ( News == null ) {
            return null;
        }

        NewsRequestTo newsRequestTo = new NewsRequestTo();

        newsRequestTo.setId( News.getId() );
        newsRequestTo.setCreatorId( News.getCreatorId() );
        newsRequestTo.setTitle( News.getTitle() );
        newsRequestTo.setContent( News.getContent() );
        newsRequestTo.setCreated( News.getCreated() );
        newsRequestTo.setModified( News.getModified() );

        return newsRequestTo;
    }

    @Override
    public NewsResponseTo NewsToNewsResponseTo(News News) {
        if ( News == null ) {
            return null;
        }

        NewsResponseTo newsResponseTo = new NewsResponseTo();

        newsResponseTo.setId( News.getId() );
        newsResponseTo.setCreatorId( News.getCreatorId() );
        newsResponseTo.setTitle( News.getTitle() );
        newsResponseTo.setContent( News.getContent() );
        newsResponseTo.setCreated( News.getCreated() );
        newsResponseTo.setModified( News.getModified() );

        return newsResponseTo;
    }

    @Override
    public News NewsResponseToToNews(NewsResponseTo responseTo) {
        if ( responseTo == null ) {
            return null;
        }

        News news = new News();

        news.setId( responseTo.getId() );
        news.setCreatorId( responseTo.getCreatorId() );
        news.setTitle( responseTo.getTitle() );
        news.setContent( responseTo.getContent() );
        news.setCreated( responseTo.getCreated() );
        news.setModified( responseTo.getModified() );

        return news;
    }

    @Override
    public News NewsRequestToToNews(NewsRequestTo requestTo) {
        if ( requestTo == null ) {
            return null;
        }

        News news = new News();

        news.setId( requestTo.getId() );
        news.setCreatorId( requestTo.getCreatorId() );
        news.setTitle( requestTo.getTitle() );
        news.setContent( requestTo.getContent() );
        news.setCreated( requestTo.getCreated() );
        news.setModified( requestTo.getModified() );

        return news;
    }
}
