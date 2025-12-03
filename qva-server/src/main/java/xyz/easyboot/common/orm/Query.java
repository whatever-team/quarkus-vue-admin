package xyz.easyboot.common.orm;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class Query<T extends PanacheEntityBase> implements IQuery<T> {

    private final PanacheQuery<T> query;

    public Query(Sort sort) {
        this.query = T.findAll(sort);
    }

    public Query(T t, Sort sort) {
        this.query = t.findAll(sort);
        log.info(this.query.getClass().getName());
    }


    public Query<T> eq(boolean condition, String column, Object value) {
        if (condition) {
            filter(column + " = :" + column, column, value);
        }
        return this;
    }

    public Query<T> ne(boolean condition, String column, Object value) {
        if (condition) {
            query.filter(column + " != :" + column, Parameters.with(column, value));
        }
        return this;
    }

    public Query<T> like(boolean condition, String column, Object value) {
        if (condition) {
            query.filter(column + "concat('%', :"+ column +", '%')", Parameters.with(column, value));
        }
        return this;
    }

    public Query<T> likeLeft(boolean condition, String column, Object value) {
        if (condition) {
            query.filter(column + "concat('%', :"+ column +")", Parameters.with(column, value));
        }
        return this;
    }

    public Query<T> likeRight(boolean condition, String column, Object value) {
        if (condition) {
            query.filter(column + "concat(:"+ column +", '%')", Parameters.with(column, value));
        }
        return this;
    }

//    public Query<T> orderBy(String column, Sort.Direction direction) {
//        return this;
//    }

    public Query<T> getPageResult(int pageIndex, int pageSize) {
        query.page(pageIndex, pageSize);
        return this;
    }

    public PageResult<T> getPageResult() {
        PageResult<T> result = new PageResult<>();
        result.count = query.count();
        result.pageCount = query.pageCount();
        result.hasNextPage = query.hasNextPage();
        result.hasPreviousPage = query.hasPreviousPage();
        result.list = list();
        return result;
    }

    public List<T> list() {
        return query.list();
    }

    public Stream<T> stream() {
        return query.stream();
    }

    private void filter(String filterName, String column, Object value) {
        query.filter(filterName, Parameters.with(column, value));
    }

    public static class PageResult<T> {
        public int pageCount;
        public long count;
        public boolean hasNextPage;
        public boolean hasPreviousPage;
        public List<T> list;
    }

}
