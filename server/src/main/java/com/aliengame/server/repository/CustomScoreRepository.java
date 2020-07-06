package com.aliengame.server.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CustomScoreRepository {
    String weekly_query = "select A.username, sum(S.score) as score_sum " +
            "from( account A inner join score S on A.account_id = S.account_id  ) " +
            "where S.date >= date_sub(now(), interval 7 day)" +
            "group by A.account_id order by score_sum desc";

    String monthly_query = "select A.username, sum(S.score) as score_sum " +
            "from( account A inner join score S on A.account_id = S.account_id  ) " +
            "where S.date >= date_sub(now(), interval 30 day)" +
            "group by A.account_id order by score_sum desc";

    String leaderboard_query = "select A.username,  sum(S.score) as score_sum " +
            "from( account A inner join score S on A.account_id = S.account_id  )" +
            "group by A.account_id order by score_sum desc";

    @Modifying
    @Query(value = weekly_query, nativeQuery = true)
    List<Map<String, String>> getLeaderBoardWeekly();

    @Modifying
    @Query(value = monthly_query, nativeQuery = true)
    List<Map<String, String>> getLeaderBoardMonthly();

    @Modifying
    @Query(value = leaderboard_query, nativeQuery = true)
    List<Map<String, String>> getLeaderBoard();
}
