package com.querydsl.hibernate.search;

import java.util.Date;
import java.util.List;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Field;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.Fetchable;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryResults;
import com.querydsl.core.SimpleQuery;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.Constant;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.lucene5.LuceneSerializer;

public class SearchQuery5<T> implements SimpleQuery<SearchQuery5<T>>, Fetchable<T> {
	private final Query luceneQuery;
	private final Sort sort;
	private final Session session;
	private final QueryMixin<SearchQuery5<T>> queryMixin = new QueryMixin<SearchQuery5<T>>(this);
	private final EntityPath<T> path;
	private final LuceneSerializer serializer = new LuceneSerializer(false, true) {
		@Override
		protected Query eq(final Operation<?> opr, final QueryMetadata metadata, final boolean ignoreCase) {
			if (Number.class.isAssignableFrom(opr.getArg(1).getType())
					|| Date.class.isAssignableFrom(opr.getArg(1).getType())) {
				return range(null, toField((Path<?>) opr.getArg(0)), opr.getArg(1), opr.getArg(1), true, true,
						metadata);
			}
			return super.eq(opr, metadata, ignoreCase);
		}

		@Override
		protected Query range(final Path<?> leftHandSide, final String field, final Expression<?> min,
				final Expression<?> max, final boolean minInc, final boolean maxInc, final QueryMetadata metadata) {
			if (min != null && Date.class.isAssignableFrom(min.getType())
					|| max != null && Date.class.isAssignableFrom(max.getType())) {
				return numericRange(Long.class, field,
						min == null ? null : ((Date) ((Constant<?>) min).getConstant()).getTime(),
						max == null ? null : ((Date) ((Constant<?>) max).getConstant()).getTime(), minInc, maxInc);
			}
			return super.range(leftHandSide, field, min, max, minInc, maxInc, metadata);
		}

		@Override
		public String toField(final Path<?> path1) {
			if (path1.getAnnotatedElement() != null) {
				final Field fieldAnn = path1.getAnnotatedElement().getAnnotation(Field.class);
				if (fieldAnn != null && fieldAnn.name().length() > 0) {
					return fieldAnn.name();
				}
			}
			return super.toField(path1);
		}
	};

	public SearchQuery5(final Session session_, final EntityPath<T> path1, final Query qry, final Sort sort_) {
		luceneQuery = qry;
		sort = sort_;
		session = session_;
		path = path1;
		queryMixin.from(path);
	}

	public SearchQuery5(final Session session1, final EntityPath<T> path1) {
		this(session1, path1, new MatchAllDocsQuery(), null);
	}

	@Override
	public SearchQuery5<T> distinct() {
		return queryMixin.distinct();
	}

	@Override
	public SearchQuery5<T> limit(final long limit) {
		return queryMixin.limit(limit);
	}

	@Override
	public SearchQuery5<T> offset(final long offset) {
		return queryMixin.offset(offset);
	}

	@Override
	public SearchQuery5<T> orderBy(final OrderSpecifier<?>... o) {
		return queryMixin.orderBy(o);
	}

	@Override
	public SearchQuery5<T> restrict(final QueryModifiers modifiers) {
		return queryMixin.restrict(modifiers);
	}

	@Override
	public <P> SearchQuery5<T> set(final ParamExpression<P> param, final P value) {
		return queryMixin.set(param, value);
	}

	@Override
	public SearchQuery5<T> where(final Predicate... e) {
		return queryMixin.where(e);
	}

	@Override
	public QueryResults<T> fetchResults() {
		return new QueryResults<T>(fetch(), queryMixin.getMetadata().getModifiers(), fetchCount());
	}

	@Override
	public T fetchFirst() {
		return queryMixin.limit(1).fetchOne();
	}

	@Override
	public CloseableIterator<T> iterate() {
		return new IteratorAdapter<T>(createQuery(false).iterate());
	}

	@Override
	public List<T> fetch() {
		return createQuery(false).list();
	}

	@Override
	public T fetchOne() {
		return (T) createQuery(false).uniqueResult();
	}

	protected FullTextQuery createQuery(final boolean forCount) {
		final QueryMetadata metadata = queryMixin.getMetadata();
		final FullTextQuery fullTextQuery = Search.getFullTextSession(session).createFullTextQuery(
				metadata.getWhere() == null ? luceneQuery : serializer.toQuery(metadata.getWhere(), metadata),
				path.getType());
		if (!forCount) {
			fullTextQuery.setSort(metadata.getOrderBy().isEmpty() ? sort : serializer.toSort(metadata.getOrderBy()));
			if (metadata.getModifiers().isRestricting()) {
				final Integer limit = metadata.getModifiers().getLimitAsInteger();
				final Integer offset = metadata.getModifiers().getOffsetAsInteger();
				if (limit != null) {
					fullTextQuery.setMaxResults(limit.intValue());
				}
				if (offset != null) {
					fullTextQuery.setFirstResult(offset.intValue());
				}
			}
		}
		return fullTextQuery;
	}

	@Override
	public long fetchCount() {
		return createQuery(true).getResultSize();
	}
}
