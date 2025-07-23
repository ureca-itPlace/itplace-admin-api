package com.itplace.adminapi.log.repository;

import com.itplace.adminapi.log.dto.ClickRankResponse;
import com.itplace.adminapi.log.dto.ClickRankResult;
import com.itplace.adminapi.log.dto.SearchRankResult;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LogRepository {
    private final MongoTemplate mongoTemplate;

    public List<ClickRankResult> findTopClickRank(Instant from, int limit) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("event").is("click")
                        .and("loggingAt").gte(from)
        );
        GroupOperation groupOperation = Aggregation.group("benefitId")
                .count().as("clickCount");
        SortOperation sortOperation = Aggregation.sort(Direction.DESC,"clickCount");
        LimitOperation limitOperation = Aggregation.limit(limit);

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("benefitId")
                .and("clickCount").as("clickCount");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation, groupOperation, sortOperation, limitOperation, projectionOperation);

        return mongoTemplate.aggregate(aggregation, "logs", ClickRankResult.class)
                .getMappedResults();
    }

    public List<SearchRankResult> findTopSearchRank(Instant from, Instant to, int limit) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("event").is("search")
                        .and("loggingAt").gte(from).lt(to)
        );
        GroupOperation groupOperation = Aggregation.group("partnerId")
                .count().as("searchCount");
        SortOperation sortOperation = Aggregation.sort(Direction.DESC,"searchCount");
        LimitOperation limitOperation = Aggregation.limit(limit);

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("partnerId")
                .and("searchCount").as("searchCount");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation, groupOperation, sortOperation, limitOperation, projectionOperation);

        return mongoTemplate.aggregate(aggregation, "logs", SearchRankResult.class)
                .getMappedResults();
    }
}
