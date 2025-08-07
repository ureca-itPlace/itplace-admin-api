package com.itplace.adminapi.log.repository;

import com.itplace.adminapi.log.dto.RankResult;
import java.time.Duration;
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
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LogRepository {
    private final MongoTemplate mongoTemplate;

    public List<RankResult> findTopClickRank(Instant from, int limit) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("event").is("click")
                        .and("loggingAt").gte(from)
        );
        GroupOperation groupOperation = Aggregation.group("benefitId")
                .count().as("count");
        SortOperation sortOperation = Aggregation.sort(Direction.DESC,"count");
        LimitOperation limitOperation = Aggregation.limit(limit);

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("benefitId")
                .and("count").as("count");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation, groupOperation, sortOperation, limitOperation, projectionOperation);

        return mongoTemplate.aggregate(aggregation, "logs", RankResult.class)
                .getMappedResults();
    }

    public List<RankResult> findTopSearchRank(Instant from, Instant to, int limit) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("event").is("search")
                        .and("loggingAt").gte(from).lt(to)
        );
        GroupOperation groupOperation = Aggregation.group("partnerId")
                .count().as("count");
        SortOperation sortOperation = Aggregation.sort(Direction.DESC,"count");
        LimitOperation limitOperation = Aggregation.limit(limit);

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("partnerId")
                .and("count").as("count");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation, groupOperation, sortOperation, limitOperation, projectionOperation);

        return mongoTemplate.aggregate(aggregation, "logs", RankResult.class)
                .getMappedResults();
    }

    public List<RankResult> findSearchRank() {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("event").is("search")
                        .and("loggingAt").gte(Instant.now().minus(Duration.ofDays(7)))
        );
        GroupOperation groupOperation = Aggregation.group("benefitId")
                .count().as("count");
        SortOperation sortOperation = Aggregation.sort(Direction.DESC,"count");

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("id")
                .and("count").as("count");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation, groupOperation, sortOperation, projectionOperation);

        return mongoTemplate.aggregate(aggregation, "logs", RankResult.class)
                .getMappedResults();
    }
}
